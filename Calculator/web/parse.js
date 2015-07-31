/* (c) 2012, Carl Burch, cburch@cburch.com
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
parse = (function () {
	"use strict";

	var LAMBDA = 0;
	var LPAREN = 1;
	var RPAREN = 2;
	var LBRACKET = 3;
	var RBRACKET = 4;
	var LBRACE = 5;
	var RBRACE = 6;
	var DOT = 7;
	var ID = 8;
	var EOF = 9;

	function Scanner(source) {
		this.source = source;
		this.pos = 0;
		this.next = 0;
		this.cur = 0;
		this.data = '';
		this.chomp();
	}

	var scanner_dict = { '\\': LAMBDA, '(': LPAREN, ')': RPAREN,
		'[': LBRACKET, ']': RBRACKET, '{': LBRACE, '}': RBRACE, '.': DOT };

	Scanner.prototype.chomp = function () {
		var source = this.source;
		var len = source.length;
		var i = this.next;
		var c;
		do {
			if (i === len) {
				this.cur = EOF;
				this.data = '$';
				this.pos = i;
				this.next = len;
				return;
			}
			c = source.charAt(i);
			i++;
		} while (c === ' ');
		this.pos = i - 1;

		if (scanner_dict.hasOwnProperty(c)) {
			this.cur = scanner_dict[c];
			this.data = c;
			this.next = i;
		} else {
			while (i < len && " \\()[]{}.".indexOf(source.charAt(i)) < 0) {
				i++;
			}
			this.cur = ID;
			this.data = source.substring(this.pos, i);
			this.next = i;
		}
	};

	Scanner.prototype.newError = function (msg) {
		return { isError: true, message: this.pos + ': ' + msg };
	};

	function isError(obj) {
		return obj.hasOwnProperty('isError') && obj.isError;
	}

	var parseFactorAction = {};
	function expectingClose(closeToken, closeText) {
		return function (scan, syms) {
			scan.chomp();
			var ret = parseExpr(scan, syms);
			if (isError(ret)) {
				return ret;
			} else if (scan.cur !== closeToken) {
				return scan.newError(scan, "Expected '" + closeText + "'");
			} else {
				scan.chomp();
				return ret;
			}
		};
	}
	parseFactorAction['' + LPAREN] = expectingClose(RPAREN, ')');
	parseFactorAction['' + LBRACKET] = expectingClose(RBRACKET, ']');
	parseFactorAction['' + LBRACE] = expectingClose(RBRACE, '}');
	parseFactorAction['' + ID] = function (scan, syms) {
		var sym;
		if (syms.hasOwnProperty(scan.data)) {
			sym = syms[scan.data];
		} else {
			sym = new Expr.Symbol(scan.data);
			syms[sym.name] = sym;
		}
		scan.chomp();
		return new Expr.Ident(sym);
	};
	parseFactorAction['' + LAMBDA] = function (scan, syms) {
		scan.chomp();
		if (scan.cur !== ID) {
			return scan.newError("Parameter name missing following lambda");
		}
		var name = scan.data;
		var oldSym = syms[name];
		var newSym = new Expr.Symbol(name);
		syms[name] = newSym;
		scan.chomp();
		if (scan.cur !== DOT) {
			return scan.newError("Period missing following parameter name");
		}
		scan.chomp();

		var expr = parseExpr(scan, syms);
		if (isError(expr)) {
			return expr;
		} else {
			if (typeof oldSym === 'undefined') {
				delete syms[name];
			} else {
				syms[name] = oldSym;
			}
			return new Expr.Abstract(newSym, expr);
		}
	};

	function parseFactor(scan, syms) {
		var key = '' + scan.cur;
		if (parseFactorAction.hasOwnProperty(key)) {
			return parseFactorAction[key](scan, syms);
		} else {
			return scan.newError("Unexpected token");
		}
	}

	function parseExpr(scan, syms) {
		var ret = parseFactor(scan, syms);
		if (isError(ret)) {
			return ret;
		}
		while (scan.cur !== EOF && scan.cur !== RPAREN &&
				scan.cur !== RBRACKET && scan.cur !== RBRACE) {
			var next = parseFactor(scan, syms);
			if (isError(next)) {
				return next;
			}
			ret = new Expr.Apply(ret, next);
		}
		return ret;
	}

	return function (source) {
		var scan = new Scanner(source);
		var syms = {};
		var ret = parseExpr(scan, syms);
		if (isError(ret)) {
			return { ok: false, error: ret.message };
		} else if (scan.cur !== EOF) {
			return { ok: false, error: scan.newError("Could not parse all of expression").message };
		} else {
			return { ok: true, expr: ret };
		}
	};
}());
