/* (c) 2012, Carl Burch, cburch@cburch.com
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
(function ($) {
	"use strict";

	var curBase = null;
	var curExpr = null;

	function reduceRequested(e) {
		e.preventDefault();

		var reduceInput = $('#reduceForm input');
		var exprStr = reduceInput.val().trim();
		if (exprStr === '') {
			$('#reduction').empty();
			return;
		}

		var parseResult = parse(exprStr);
		var destination = $('#reduction');
		if (parseResult.ok) {
			var reduceResult = reduce(parseResult.expr, options);

			var row0 = ExprStr.toHtmlSubstituteBelow(parseResult.expr, options);
			var row1;
			if (parseResult.expr.hasOwnProperty('ident')) {
				row1 = ExprStr.toHtmlSubstituteBelow(reduceResult.expr, options);
			} else {
				row1 = ExprStr.toHtml(reduceResult.expr, options);
			}

			var table = $('<table></table>');
			table.append($('<tr></tr>')
				.append($('<td></td>').attr('colspan', 2).html(row0)));
			if (reduceResult.steps.length === 1) {
				var rowi = ExprStr.toHtml(reduceResult.steps[0], options);
				table.append($('<tr></tr>')
					.append($('<td>&rArr;</td>'))
					.append($('<td></td>').html(rowi)));
			} else if (reduceResult.steps.length > 1) {
				var showText = 'Show ' + reduceResult.steps.length + ' Intermediate Steps';
				var showForm = $('<form id="showform"></form')
					.append($('<button type="submit"></button>').text(showText));
				showForm.submit(showIntermediateHandler(reduceResult.steps));
				table.append($('<tr></tr>')
					.append($('<td>&hellip;</td>'))
					.append($('<td></td>').append(showForm)));
			}
			table.append($('<tr></tr>')
				.append($('<td>&rArr;</td>'))
				.append($('<td></td>').html(row1)));

			$('#reduction').empty().append(table);
			curBase = exprStr.trim();
			curExpr = reduceResult.expr;
			$('#addFeedback').empty();
			$('#addForm').show();
		} else {
			$('#reduction').empty().text('' + parseResult.error);
			curBase = null;
			curExpr = null;
			$('#addForm').hide();
			$('#addFeedback').empty();
		}
		reduceInput.focus().select();
	}

	function showIntermediateHandler(steps) {
		return function (e) {
			e.preventDefault();
			var base = $(this).closest('tr');
			$.each(steps, function (i, expr) {
				var text = expr === null ? '&hellip;' : ExprStr.toHtml(expr, options);
				base.before($('<tr></tr>')
					.append($('<td></td>').html('&rArr;'))
					.append($('<td></td>').html(text)));
			});
			base.remove();
		};
	}

	function addFocused(e) {
		$('#addFeedback').empty();
	}

	function addRequested(e) {
		e.preventDefault();
		var input = $('#addForm input');
		if (input.size() !== 1) {
			console.log('ID input not identified'); // OK
		} else {
			var id = input.val();
			var expr = curExpr;
			if (id === '') {
				$('#addFeedback').text('Enter symbol name at left.')
				input.focus().select();
			} else if (expr === null) {
				$('#addFeedback').text('Value not specified.');
				input.focus().select();
			} else {
				var oldExpr = options.context.put(id, expr, curBase);
				input.val('');
				$('#reduceForm input').focus();
				if (oldExpr === null) {
					$('#addFeedback').text('Added.');
				} else {
					$('#addFeedback').text('Replaced.');
				}
				$('#reduceForm input').focus().select();
			}
		}
	}

	function dlogOpen(dlogDiv) {
		$('#overlay').fadeIn(100);
		dlogDiv.fadeIn(50);
	}

	function dlogClose(dlogDiv) {
		dlogDiv.fadeOut(50);
		$('#overlay').fadeOut(100);
	}

	function dictionaryOpen(e) {
		e.preventDefault();
		dictionary.updatePage();
		dlogOpen($('#dictionaryDlog'));
	}

	function dictionaryClose(e) {
		e.preventDefault();
		dlogClose($('#dictionaryDlog'));
	}

	function optionsOpen(e) {
		e.preventDefault();
		options.updatePage();
		dlogOpen($('#optionsDlog'));
	}

	function optionsClose(e) {
		e.preventDefault();
		options.loadFromPage();
		dlogClose($('#optionsDlog'));
	}

	function docOpen(e) {
		e.preventDefault();
		dlogOpen($('#docDlog'));
		docs.init();
	}

	function docClose(e) {
		e.preventDefault();
		dlogClose($('#docDlog'));
	}

	$(document).ready(function () {
		$('#reduceForm').submit(reduceRequested);
		$('#addForm input').focus(addFocused);
		$('#addForm').submit(addRequested);
		$('#addForm').hide();

		$('#overlay').hide();

		$('#dictionaryDlog').hide();
		$('#dictionaryIcon').click(dictionaryOpen);
		$('#dictionaryForm').submit(dictionaryClose);
		$('#dictionaryShow').submit(dictionary.showText);

		$('#optionsDlog').hide();
		$('#optionsIcon').click(optionsOpen);
		$('#optionsForm').submit(optionsClose);

		$('#docDlog').hide();
		$('#docIcon').click(docOpen);
		$('#docForm').submit(docClose);
	});
}(jQuery));
