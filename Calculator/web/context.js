/* (c) 2012, Carl Burch, cburch@cburch.com
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
Context = (function () {
	"use strict";

	function Substitutor(context) {
		this.context = context;
	}

	Substitutor.prototype.visitAbstract = function (e) {
		var map = this.context.map;
		var sym = e.sym;
		var symName = sym.name;
		var oldValue = null;
		if (map.hasOwnProperty(symName)) {
			oldValue = map[symName];
			delete map[symName];
		}
		var ret = new Expr.Abstract(sym, e.expr.visit(this));
		if (oldValue !== null) {
			map[symName] = oldValue;
		}
		return ret;
	};

	Substitutor.prototype.visitApply = function (e) {
		return new Expr.Apply(e.left.visit(this), e.right.visit(this));
	};

	Substitutor.prototype.visitIdent = function (e) {
		var map = this.context.map;
		if (map.hasOwnProperty(e.ident.name)) {
			return map[e.ident.name];
		} else {
			return e;
		}
	};

	function Context () {
		this.baseMap = {};
		this.map = {};
		this.inverse = new Collections.Map();
		this.substitutor = new Substitutor(this);
	}

	Context.prototype.substitute = function (e) {
		return e.visit(this.substitutor);
	};

	Context.prototype.contains = function (symName) {
		return this.map.hasOwnProperty(symName.trim());
	};

	Context.prototype.isEmpty = function () {
		var map = this.map;
		for (var key in map) {
			if (map.hasOwnProperty(key)) {
				return false;
			}
		}
		return true;
	};

	Context.prototype.getKeys = function () {
		var map = this.map;
		var ret = [];
		for (var key in map) {
			if (map.hasOwnProperty(key)) {
				ret.push(key);
			}
		}
		ret.sort();
		return ret;
	};

	Context.prototype.put = function (symName, e, baseExpr) {
		var map = this.map;
		var inv = this.inverse;
		var name = symName.trim();
		var expr = e;
		var oldExpr = null;
		if (map.hasOwnProperty(name)) {
			oldExpr = map[name];
			inv.remove(oldExpr);
		}
		map[name] = expr;
		if (typeof baseExpr === 'undefined' || baseExpr === null) {
			if (this.baseMap.hasOwnProperty[name]) {
				delete this.baseMap[name];
			}
		} else {
			this.baseMap[name] = baseExpr;
		}
		inv.put(expr, name);
		return oldExpr;
	};

	Context.prototype.remove = function (symName) {
		var map = this.map;
		var inv = this.inverse;
		var name = symName.trim();
		var expr;
		if (this.baseMap.hasOwnProperty(name)) {
			delete this.baseMap[name];
		}
		if (map.hasOwnProperty(name)) {
			expr = map[name];
			delete map[name];
			inv.remove(expr);
			return true;
		} else {
			return false;
		}
	};

	Context.prototype.get = function (symName) {
		var map = this.map;
		var name = symName.trim();
		if (map.hasOwnProperty(name)) {
			return map[name];
		} else {
			return null;
		}
	};

	Context.prototype.getBase = function (symName) {
		var baseMap = this.baseMap;
		var name = symName.trim();
		if (baseMap.hasOwnProperty(name)) {
			return baseMap[name];
		} else {
			return null;
		}
	};

	Context.prototype.invert = function (e) {
		return this.inverse.get(e);
	};

	return Context;
}());
