/* (c) 2012, Carl Burch, cburch@cburch.com
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
Expr = (function () {
	"use strict";

	var lastSymbolCodeAssigned = 0;
	var lastExprIdAssigned = 0;

	var Expr = function () {
		var myId = lastExprIdAssigned + 1;
		lastExprIdAssigned = myId;
		this.id = myId;
	};

	Expr.prototype.equals = function (other) {
		if (other === this) {
			return true;
		} else if (this.hashCode === other.hashCode) {
			return this.visit(new EqualityTester(), other);
		} else {
			return false;
		}
	};

	function HashCodeComputer() {
		this.map = new Collections.Map();
		this.lastIdent = 1;
	}

	HashCodeComputer.prototype.visitAbstract = function (e) {
		var ident = this.lastIdent;
		this.lastIdent = ident + 1;
		var oldId = this.map.put(e.sym, ident);
		var ret = e.expr.visit(this);
		if (oldId === null) {
			this.map.remove(e.sym);
		} else {
			this.map.put(e.sym, oldId);
		}
		return ret;
	};

	HashCodeComputer.prototype.visitApply = function (e) {
		var left = e.left.visit(this);
		var right = e.right.visit(this);
		var ret = (left << 5) - left + right;
		return ret & ret;
	};

	HashCodeComputer.prototype.visitIdent = function (e) {
		var ret = this.map.get(e.ident);
		if (ret === null) {
			return stringHashCode(e.ident.name);
		} else {
			return ret;
		}
	};

	function stringHashCode(name) {
		var hash = 0;
		for (var i = 0; i < name.length; i += 1) {
			hash = ((hash << 5) - hash) + name.charCodeAt(i);
			hash = hash & hash; // Convert to 32-bit integer
		}
		return hash;
	};

	function EqualityTester() {
		this.bound0 = new Collections.Map();
		this.bound1 = new Collections.Map();
		this.lastIdent = 0;
	}

	EqualityTester.prototype.visitAbstract = function (e0, e1) {
		if (e1.hasOwnProperty('sym')) {
			this.bound0.put(e0.sym, e1.sym);
			this.bound1.put(e1.sym, e0.sym);
			var ret = e0.expr.visit(this, e1.expr);
			this.bound0.remove(e0.sym);
			this.bound1.remove(e1.sym);
			return ret;
		} else {
			return false;
		}
	};

	EqualityTester.prototype.visitApply = function (e0, e1) {
		if (e1.hasOwnProperty('left')) {
			var ret = e0.left.visit(this, e1.left) &&
				e0.right.visit(this, e1.right);
			return ret;
		} else {
			return false;
		}
	};

	EqualityTester.prototype.visitIdent = function (e0, e1) {
		if (e1.hasOwnProperty('ident')) {
			var want1 = this.bound0.get(e0.ident);
			var want0 = this.bound1.get(e1.ident);
			if (want1 === null && want0 === null) {
				var ret = e0.ident.name === e1.ident.name;
				return ret;
			} else {
				return e1.ident === want1;
			}
		} else {
			return false;
		}
	};

	Expr.Symbol = function (name) {
		var hashCode = lastSymbolCodeAssigned + 1;
		lastSymbolCodeAssigned = hashCode;
		this.hashCode = hashCode;
		this.name = name;
	};

	Expr.Symbol.prototype.equals = function (other) {
		return this === other;
	};

	Expr.Abstract = function (sym, expr) {
		Expr.call(this);
		this.sym = sym;
		this.expr = expr;
		this.hashCode = this.visit(new HashCodeComputer());
	};
	Expr.Abstract.prototype = new Expr();
	Expr.Abstract.prototype.constructor = Expr.Abstract;

	Expr.Abstract.prototype.addBounds = function (bounds, frees, curBounds) {
		var wasBounded = curBounds.hasOwnProperty(this.sym.hashCode);
		if (!wasBounded) {
			curBounds[this.sym.hashCode] = this.sym;
			bounds[this.sym.hashCode] = this.sym;
		}
		this.expr.addBounds(bounds, frees, curBounds);
		if (!wasBounded) {
			delete bounds[this.sym.hashCode];
		}
	};

	Expr.Abstract.prototype.visit = function (visitor, arg) {
		return visitor.visitAbstract(this, arg);
	};

	Expr.Abstract.prototype.size = function () {
		return 1 + this.expr.size();
	};

	Expr.Abstract.prototype.uses = function (sym) {
		return this.expr.uses(sym);
	};


	Expr.Apply = function (leftExpr, rightExpr) {
		Expr.call(this);
		this.left = leftExpr;
		this.right = rightExpr;

		var hash = leftExpr.hashCode;
		hash = (hash << 5) - hash + rightExpr.hashCode;
		this.hashCode = hash & hash;
	};
	Expr.Apply.prototype = new Expr();
	Expr.Apply.prototype.constructor = Expr.Apply;

	Expr.Apply.prototype.addBounds = function (bounds, frees, curBounds) {
		this.left.addBounds(bounds, frees, curBounds);
		this.right.addBounds(bounds, frees, curBounds);
	};

	Expr.Apply.prototype.visit = function (visitor, arg) {
		return visitor.visitApply(this, arg);
	};

	Expr.Apply.prototype.size = function () {
		return 1 + this.left.size() + this.right.size();
	};

	Expr.Apply.prototype.uses = function (sym) {
		return this.left.uses(sym) || this.right.uses(sym);
	};


	Expr.Ident = function (sym) {
		Expr.call(this);
		this.ident = sym;
		this.hashCode = stringHashCode(sym.name);
	};
	Expr.Ident.prototype = new Expr();
	Expr.Ident.prototype.constructor = Expr.Ident;

	Expr.Ident.prototype.addBounds = function (bounds, frees, curBounds) {
		if (!curBounds.hasOwnProperty(this.ident.hashCode)) {
			frees[this.ident.hashCode] = this.ident;
		}
	};

	Expr.Ident.prototype.visit = function (visitor, arg) {
		return visitor.visitIdent(this, arg);
	};

	Expr.Ident.prototype.size = function () {
		return 1;
	};

	Expr.Ident.prototype.uses = function (sym) {
		return this.ident === sym;
	};

	return Expr;
}());
