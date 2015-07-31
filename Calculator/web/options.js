/* (c) 2012, Carl Burch, cburch@cburch.com
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
options = (function ($) {
	"use strict";

	var options = {
		// elements filled in by BooleanOption and ComboOption constructors
		context: new Context()
	};

	function BooleanOption(varName, eltName, defaultValue) {
		this.varName = varName;
		this.eltName = eltName;
		options[this.varName] = defaultValue;
	}

	BooleanOption.prototype.eltFromValue = function () {
		var elt = $('#' + this.eltName);
		if (options[this.varName]) {
			elt.attr('checked', 'checked');
		} else {
			elt.removeAttr('checked');
		}
	};

	BooleanOption.prototype.valueFromElt = function () {
		options[this.varName] = $('#' + this.eltName).is(':checked');
	};

	function ComboOption(varName, eltName, defaultValue, optionNames, optionValues) {
		this.varName = varName;
		this.eltName = eltName;
		options[this.varName] = defaultValue;
		this.optionNames = optionNames;
		this.optionValues = optionValues;
	}

	ComboOption.prototype.eltFromValue = function () {
		var val = options[this.varName];
		var vals = this.optionValues;
		var elt = $('#' + this.eltName);
		elt.empty();
		$.each(this.optionNames, function (i, text) {
			var opt = $('<option></option>').attr('value', '' + i).text(text);
			if (vals[i] === val) {
				opt.attr('selected', 'selected');
			}
			elt.append(opt);
		});
	};

	ComboOption.prototype.valueFromElt = function () {
		var val = parseInt($('#' + this.eltName).val(), 10);
		if (val >= 0 && val < this.optionValues.length) {
			options[this.varName] = this.optionValues[val];
		}
	};

	var optionManagers = [
		new BooleanOption('useEtas', 'useEtasCheck', true),
		new BooleanOption('substSymbols', 'substSymbolsCheck', true),
		new BooleanOption('showIntermediate', 'showIntermediateCheck', true),
		new BooleanOption('varyParens', 'varyParensCheck', true),
		new ComboOption('evalOrder', 'evalOrderCombo', simplify.LAZY_EVALUATION,
			['Lazy Evaluation', 'Eager Evaluation', 'Normal Evaluation'],
			[simplify.LAZY_EVALUATION, simplify.EAGER_EVALUATION,
				simplify.LAZY_SLOW_EVALUATION]),
		new ComboOption('calcType', 'calcTypeCombo', simplify.APPLIED,
			['Applied Calculus', 'Pure Calculus With Numerals',
				'Pure Calculus Without Numerals'],
			[simplify.APPLIED, simplify.PURE_CHURCH, simplify.PURE]),
		new ComboOption('maxReductions', 'maxReductionsCombo', 200,
			['25', '50', '100', '200', '500', '1000', '2000'],
			[25, 50, 100, 200, 500, 1000, 2000]),
		new ComboOption('maxLength', 'maxLengthCombo', 200,
			['25', '50', '100', '200', '500', '1000', '2000'],
			[25, 50, 100, 200, 500, 1000, 2000])
	];

	options.updatePage = function () {
		$.each(optionManagers, function (i, option) {
			option.eltFromValue();
		});
	};

	options.loadFromPage = function () {
		$.each(optionManagers, function (i, option) {
			option.valueFromElt();
		});
	};

	return options;
}(jQuery));
