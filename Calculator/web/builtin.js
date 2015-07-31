/* (c) 2012, Carl Burch, cburch@cburch.com
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
Builtin = (function () {
	"use strict";

	var numericValueRegex = /^-?[0-9]+$/;

	function BinaryOp(computeFunction) {
		this.computeFunction = computeFunction;
		this.numArguments = 2;
	}

	BinaryOp.prototype.canApply = function (args) {
		return (args[0].hasOwnProperty('ident') &&
				args[1].hasOwnProperty('ident') &&
				numericValueRegex.test(args[0].ident.name) &&
				numericValueRegex.test(args[0].ident.name));
	};

	BinaryOp.prototype.apply = function (args) {
		var a0 = parseInt(args[0].ident.name, 10);
		var a1 = parseInt(args[1].ident.name, 10);
		var ret = this.computeFunction(a0, a1);
		return new Expr.Ident(new Expr.Symbol('' + ret));
	};

	function IfOperator() {
		this.numArguments = 2;
	}

	IfOperator.prototype.canApply = function (args) {
		return (args[0].hasOwnProperty('ident') &&
					(args[0].ident.name === 'true' ||
					args[0].ident.name === 'false'));
	};

	IfOperator.prototype.apply = function (args) {
		if (args[0].ident.name === 'true') {
			return args[1];
		} else {
			return args[2];
		}
	};

	return {
		'+': new BinaryOp(function (a, b) { return a + b; }),
		'-': new BinaryOp(function (a, b) { return a - b; }),
		'*': new BinaryOp(function (a, b) { return a * b; }),
		'/': new BinaryOp(function (a, b) { return Math.floor(a / b); }),
		'=': new BinaryOp(function (a, b) { return a === b ? 'true' : 'false'; }),
		'/=': new BinaryOp(function (a, b) { return a !== b ? 'true' : 'false'; }),
		'<': new BinaryOp(function (a, b) { return a < b ? 'true' : 'false'; }),
		'>': new BinaryOp(function (a, b) { return a < b ? 'true' : 'false'; }),
		'<=': new BinaryOp(function (a, b) { return a <= b ? 'true' : 'false'; }),
		'>=': new BinaryOp(function (a, b) { return a >= b ? 'true' : 'false'; }),
		'if': new IfOperator()
	};
}());
