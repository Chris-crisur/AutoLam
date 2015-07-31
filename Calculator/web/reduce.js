/* (c) 2012, Carl Burch, cburch@cburch.com
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
reduce = (function (Collections) {
	"use strict";

	var NUMBER_REGEX = /^(0|[1-9][0-9]*)$/;

	function ChurchConverter() {
		this.bounds = {};
	}
	ChurchConverter.prototype.visitAbstract = function (e) {
		var wasBound = this.bounds.hasOwnProperty(e.sym.name);
		this.bounds[e.sym.name] = true;
		var sub = e.expr.visit(this);
		if (!wasBound) {
			delete this.bounds[e.sym.name];
		}
		if (sub === e.expr) {
			return e;
		} else {
			return new Expr.Abstract(e.sym, sub);
		}
	};
	ChurchConverter.prototype.visitApply = function (e) {
		var sub0 = e.left.visit(this);
		var sub1 = e.right.visit(this);
		if (sub0 === e.left && sub1 === e.right) {
			return e;
		} else {
			return new Expr.Apply(sub0, sub1);
		}
	};
	ChurchConverter.prototype.visitIdent = function (e) {
		var name = e.ident.name;
		if (NUMBER_REGEX.test(name) && !this.bounds.hasOwnProperty(name)) {
			var s = new Expr.Ident(new Expr.Symbol('s'));
			var z = new Expr.Ident(new Expr.Symbol('z'));
			var cur = z;
			var toAdd = parseInt(name, 10);
			while (toAdd > 0) {
				cur = new Expr.Apply(s, cur);
				toAdd--;
			}
			return new Expr.Abstract(s.ident, new Expr.Abstract(z.ident, cur));
		} else {
			return e;
		}
	};

	function reduce(expr, options) {
		var steps = [];

		var curExpr = expr;
		if (options.context !== null) {
			curExpr = options.context.substitute(curExpr);
		}
		if (options.calcType === simplify.PURE_CHURCH) {
			curExpr = curExpr.visit(new ChurchConverter());
		}
		var original = curExpr;
		var smallest = curExpr;
		var smallestSize = curExpr.size();
		var nextExpr = simplify(curExpr, options);
		var histSet = new Collections.Set();
		var hist = new Array(100);
		var histPos = -1;
		var count = 0;
		while (curExpr !== nextExpr) {
			if (options.showIntermediate && curExpr !== original) {
				steps.push(curExpr);
			}

			curExpr = nextExpr;

			// see whether to stop
			++count;
			if (count > options.maxReductions ||
					histSet.contains(curExpr)) {
				if (options.showIntermediate && curExpr !== smallest) {
					steps.push(null);
				}
				curExpr = smallest;
				break;
			}

			// log the current expression
			++histPos;
			if (histPos === hist.length) histPos = 0;
			if (typeof hist[histPos] !== 'undefined') {
				histSet.remove(hist[histPos]);
			}
			hist[histPos] = curExpr;
			histSet.add(curExpr);
			var exprSize = curExpr.size();
			if (exprSize < smallestSize) {
				smallest = curExpr;
				smallestSize = exprSize;
			}

			// find next expression
			nextExpr = simplify(curExpr, options);
		}
		return { steps: steps, expr: curExpr };
	}

	return reduce;
}(Collections));
