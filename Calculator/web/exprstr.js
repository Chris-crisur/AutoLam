/* (c) 2012, Carl Burch, cburch@cburch.com
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
ExprStr = (function () {
	"use strict";

	var ret = {};

	var MAX_INT = 9007199254740992;
	var PARENS_LEFT = "(([(({";
	var PARENS_RIGHT = "))]))}";
	var VAR_RE = /^[a-zA-Z_][a-zA-Z0-9_]*$/;

	ret.toText = function (expr, options) {
		var visitor = new PrintVisitor(expr, options, true, false);
		expr.visit(visitor, false);
		return visitor.output;
	};

	ret.toTextSubstituteBelow = function (expr, options) {
		var visitor = new PrintVisitor(expr, options, false, false);
		expr.visit(visitor, false);
		return visitor.output;
	};

	ret.toHtml = function (expr, options) {
		var visitor = new PrintVisitor(expr, options, true, true);
		expr.visit(visitor, false);
		return visitor.output;
	};

	ret.toHtmlSubstituteBelow = function (expr, options) {
		var visitor = new PrintVisitor(expr, options, false, true);
		expr.visit(visitor, false);
		return visitor.output;
	};

	function identifyChurchNumeral(e) {
		var syms;
		var symz;
		var count;
		var esub;
		if (e.hasOwnProperty('expr') && e.expr.hasOwnProperty('expr')) {
			syms = e.sym;
			symz = e.expr.sym;
			esub = e.expr.expr;
			count = 0;
			while (esub.hasOwnProperty('left')) {
				if (esub.left.hasOwnProperty('ident') && esub.left.ident === syms) {
					count++;
					esub = esub.right;
				} else {
					return null;
				}
			}
			if (esub.hasOwnProperty('ident') && esub.ident === symz) {
				return "" + count;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	function PrintVisitor(expr, options, substAtTop, toHtml) {
		var sym;

		this.substAtTop = substAtTop;
		this.toHtml = toHtml;
		this.context = options && options.substSymbols ? options.context : null;
		this.maxLength = options ? options.maxLength : MAX_INT;
		this.varyParens = options ? options.varyParens : false;
		this.convertChurch = options ? options.calcType === simplify.PURE_CHURCH : false;

		this.output = '';
		this.outputLength = 0;
		this.parenDepth = 0;
		this.parenCur = -1;
		this.bounds = {};
		this.frees = {};
		this.freeNames = {};
		this.identNames = {};
		this.symMap = {};
		this.exceeded = false;
		this.lastAlloc = -1;
		this.visited = false;

		expr.addBounds(this.bounds, this.frees, {});
		for (var symHash in this.frees) {
			if (this.frees.hasOwnProperty(symHash)) {
				sym = this.frees[symHash];
				this.freeNames[sym.name] = true;
				this.identNames[sym.name] = true;
			}
		}
		for (symHash in this.bounds) {
			if (this.bounds.hasOwnProperty(symHash)) {
				sym = this.bounds[symHash];
				this.identNames[sym.name] = true;
			}
		}
	}

	PrintVisitor.prototype.allocate = function () {
		var identNames = this.identNames;
		var lastAlloc = this.lastAlloc;
		while (lastAlloc >= 0 && !identNames.hasOwnProperty("i" + lastAlloc)) {
			--lastAlloc;
		}
		while (true) {
			++lastAlloc;
			var name = "i" + lastAlloc;
			if(!identNames.hasOwnProperty(name)) {
				this.lastAlloc = lastAlloc;
				identNames[name] = true;
				return name;
			}
		}
	};

	PrintVisitor.prototype.getLeftParen = function () {
		++(this.parenDepth);
		if (this.varyParens) {
			var newCur = this.parenCur + 1;
			if (newCur >= PARENS_LEFT.length) {
				newCur = 0;
			}
			this.parenCur = newCur;
			return PARENS_LEFT.charAt(newCur);
		} else {
			return '(';
		}
	};

	PrintVisitor.prototype.getRightParen = function () {
		--(this.parenDepth);
		if (this.varyParens) {
			var cur = this.parenCur;
			var ret = PARENS_RIGHT.charAt(cur);
			if (cur === 0) {
				this.parenCur = PARENS_RIGHT.length - 1;
			} else {
				this.parenCur = cur - 1;
			}
			return ret;
		} else {
			return ')';
		}
	};

	PrintVisitor.prototype.append = function (add, length, force) {
		var curOut = this.output;
		var curLen = this.outputLength;
		var addLen = length;
		if (typeof add === 'undefined') {
			throw new TypeError();
		}
		if (typeof addLen === 'undefined') {
			addLen = add.length;
		}
		if (this.exceeded) {
			return false;
		} else if (curLen + addLen + this.parenDepth + 5 <= this.maxLength) {
			this.output = curOut + add;
			this.outputLength = curLen + addLen;
			return true;
		} else {
			if (this.toHtml) {
				this.output = curOut + ' &hellip; ';
			} else {
				this.output = curOut + ' ... ';
			}
			this.outputLength = curLen + 5;
			this.exceeded = true;
			return false;
		}
	};

	PrintVisitor.prototype.checkForSubstitution = function (expr) {
		if (this.context !== null) {
			if (this.substAtTop || this.visited) {
				var s = this.context.invert(expr);
				if (s !== null) {
					if (this.toHtml && VAR_RE.test(s)) {
						this.append('<var>' + s + '</var>', s.length);
					} else {
						this.append(s, s.length);
					}
					return true;
				}
				if (this.convertChurch) {
					s = identifyChurchNumeral(expr);
					if (s !== null) {
						this.append(s, s.length);
						return true;
					}
				}
			}
			this.visited = true;
		}
		return false;
	};

	PrintVisitor.prototype.visitAbstract = function (expr, parens) {
		if (this.exceeded || this.checkForSubstitution(expr)) {
			return;
		}

		var name = expr.sym.name;
		if (this.freeNames.hasOwnProperty(name)) {
			name = this.allocate();
			this.symMap[expr.sym.hashCode] = name;
		}
		this.freeNames[name] = true;
		this.identNames[name] = true;

		var added = false;
		if (parens) {
			added = this.append(this.getLeftParen(), 1);
		}
		if (this.toHtml && VAR_RE.test(name)) {
			this.append('&lambda;<var>' + name + '</var>.', name.length + 2);
		} else {
			this.append('\\' + name + '.', name.length + 2);
		}
		expr.expr.visit(this, false);
		if (parens) {
			var paren = this.getRightParen();
			if (added) this.output += paren;
		}

		delete this.freeNames[name];
		delete this.identNames[name];
		if (name !== expr.sym.name) {
			delete this.symMap[expr.sym.hashCode];
		}
	};

	PrintVisitor.prototype.visitApply = function (expr, parens) {
		if (this.exceeded || this.checkForSubstitution(expr)) {
			return;
		}

		expr.left.visit(this, true);
		this.append(' ', 1);
		if (expr.right instanceof Expr.Apply) {
			var added = this.append(this.getLeftParen(), 1);
			expr.right.visit(this, false);
			var paren = this.getRightParen();
			if(added) this.append(paren, 1, true);
		} else {
			expr.right.visit(this, true);
		}
	};

	PrintVisitor.prototype.visitIdent = function (expr, parens) {
		var name;
		if (this.exceeded || this.checkForSubstitution(expr)) {
			return;
		}

		if (this.symMap.hasOwnProperty(expr.ident.hashCode)) {
			name = this.symMap[expr.ident.hashCode];
		} else {
			name = expr.ident.name;
		}
		if (this.toHtml && VAR_RE.test(name)) {
			this.append('<var>' + name + '</var>', name.length);
		} else {
			this.append(name, name.length);
		}
	};

	return ret;
}());
