/* (c) 2012, Carl Burch, cburch@cburch.com
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
var dictionary = (function ($) {
	"use strict";

	var dictionary = {};

	dictionary.updatePage = function () {
		var dict = options.context;
		var keys = dict.getKeys();
		var table = $('#dictionary');

		$('#dictionaryText').hide();
		$('#dictionaryShow button').text("Show Text");
		
		table.empty();
		if (keys.length === 0) {
			table.append($('<tr><td>Dictionary is empty.</td></tr>'));
		} else {
			$.each(keys, function (i, key) {
				var expr = dict.get(key);
				var tr = $('<tr></tr>');
				tr.append($('<td></td>')
					.html(ExprStr.toHtml(parse(key).expr, options)));
				tr.append($('<td>=</td>'));
				tr.append($('<td></td>').addClass('defn')
					.html(ExprStr.toHtmlSubstituteBelow(expr, options)));
				tr.append($('<td></td>').addClass('defndel'));
				$('td', tr).slice(0, 3).click(defnClickHandler(key));
				table.append(tr);
			});
		}
	};

	function defnClickHandler(id) {
		return function (e) {
			e.preventDefault();
			var tr = $(this).closest('tr');
			var td = $('.defndel', tr);
			if ($('form', td).size() === 0) {
				td.append($('<form></form>').submit(defnDeleter(id))
					.append($('<button class="imgbutton" type="submit"></button>')
						.append($('<img src="img/clear.png"></img>'))));
			} else {
				td.empty();
			}
		};
	}

	function defnDeleter(id) {
		return function (e) {
			e.preventDefault();
			var tr = $(this).closest('tr');
			if (options.context.remove(id)) {
				tr.remove();
				$('#dictionaryText').hide();
				var table = $('#dictionary');
				if ($('tr', table).size() === 0) {
					table.append($('<tr><td>Dictionary is empty.</td></tr>'));
				}
			}
		};
	}

	dictionary.showText = function (e) {
		e.preventDefault();

		var dest = $('#dictionaryText');
		if (dest.css('display') !== 'none') {
			dest.hide();
			$('#dictionaryShow button').text("Show Text");
		} else {
			var allText = '';
			var dict = options.context;
			var keys = dict.getKeys();
			$.each(keys, function (i, key) {
				if (i > 0) {
					allText += '\n';
				}
				var s = ExprStr.toTextSubstituteBelow(dict.get(key), options);
				var line;
				line = key;
				var baseExpr = dict.getBase(key);
				if (baseExpr !== null) {
					line += ' [' + baseExpr + ']';
				}
				line += ' = ' + s;
				allText += line;
			});
			dest.val(allText).show().focus().select();
			$('#dictionaryShow button').text("Hide Text");
		}
	};

	dictionary.add = function (id, exprStr) {
		var parseResult = parse(exprStr, options);
		if (parseResult.ok) {
			var reduceResult = reduce(parseResult.expr, options);
			options.context.put(id, reduceResult.expr, null);
		}
	};

	return dictionary;
}(jQuery));
