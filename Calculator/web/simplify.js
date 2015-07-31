/* (c) 2012, Carl Burch, cburch@cburch.com
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
simplify = (function () {
	"use strict";

	var EAGER_EVALUATION = 0;
	var LAZY_EVALUATION = 1;
	var LAZY_SLOW_EVALUATION = 2;

	var APPLIED = 0;
	var PURE_CHURCH = 1;
	var PURE = 2;

	function CandidateFinder(options) {
		this.options = options;
	}

	CandidateFinder.prototype.visitAbstract = function (e) {
		if (this.canReduce(e)) return e;
		else return e.expr.visit(this);
	};
	
	CandidateFinder.prototype.visitIdent = function (e) {
		if (this.canReduce(e)) return e;
		else return null;
	};

	CandidateFinder.prototype.canReduce = function (e) {
		// handle beta-reductions
		if (e.hasOwnProperty('left') && e.left.hasOwnProperty('expr')) {
			return true;
		}

		// handle eta-reductions
		if (this.options.useEtas && e.hasOwnProperty('expr') &&
				e.expr.hasOwnProperty('left') &&
				e.expr.right.hasOwnProperty('ident') &&
				e.expr.right.ident === e.sym &&
				!e.expr.left.uses(e.sym)) {
			return true;
		}

		// handle builtins
		if (this.options.calcType === APPLIED) {
			var funcExpr = e;
			var argCount = 0;
			while (funcExpr.hasOwnProperty('left')) {
				funcExpr = funcExpr.left;
				argCount++;
			}
			if (funcExpr.hasOwnProperty('ident') &&
					Builtin.hasOwnProperty(funcExpr.ident.name)) {
				var builtin = Builtin[funcExpr.ident.name];
				if (builtin.numArguments === argCount) {
					var args = this.getArguments(e, argCount);
					if (builtin.canApply(args)) return true;
				}
			}
		}

		return false;
	};

	CandidateFinder.prototype.reduce = function (e) {
		// handle beta-reductions
		if (e.hasOwnProperty('left') && e.left.hasOwnProperty('expr')) {
			return e.left.expr.visit(new SymbolSubstitutor(e.left.sym,
				e.right));
		}

		// handle eta-reductions
		if (this.options.useEtas && e.hasOwnProperty('expr') &&
				e.expr.hasOwnProperty('left')) {
			return e.expr.left;
		}

		// handle builtins
		if (this.options.calcType === APPLIED) {
			var funcExpr = e;
			var argCount = 0;
			while (funcExpr.hasOwnProperty('left')) {
				funcExpr = funcExpr.left;
				argCount++;
			}
			if (funcExpr.hasOwnProperty('ident') &&
					Builtin.hasOwnProperty(funcExpr.ident.name)) {
				var builtin = Builtin[funcExpr.ident.name];
				if (builtin.numArguments === argCount) {
					var args = this.getArguments(e, argCount);
					return builtin.apply(args);
				}
			}
		}

		return null;
	};

	CandidateFinder.prototype.getArguments = function (expr, argCount) {
		var argExpr = expr;
		var args = new Array(argCount);
		var argPos = argCount - 1;
		while (argExpr.hasOwnProperty('left')) {
			args[argPos] = argExpr.right;
			argPos--;
			argExpr = argExpr.left;
		}
		return args;
	};

	function LazyCandidateFinder(options) {
		this.options = options;
	}
	LazyCandidateFinder.prototype = new CandidateFinder();
	LazyCandidateFinder.prototype.constructor = LazyCandidateFinder;
	LazyCandidateFinder.prototype.visitApply = function (expr) {
		if (this.canReduce(expr)) return expr;

		var ret = expr.left.visit(this);
		if (ret !== null) return ret;
		return expr.right.visit(this);
	};

	function EagerCandidateFinder(options) {
		this.options = options;
	}
	EagerCandidateFinder.prototype = new CandidateFinder();
	EagerCandidateFinder.prototype.constructor = EagerCandidateFinder;
	EagerCandidateFinder.prototype.visitApply = function (expr) {
		var ret = expr.right.visit(this);
		if (ret !== null) return ret;

		if (this.canReduce(expr)) return expr;

		return expr.left.visit(this);
	};

	function SymbolSubstitutor(symbolToFind, exprToReplace) {
		this.toFind = symbolToFind;
		this.toReplace = exprToReplace;
	}
	SymbolSubstitutor.prototype.visitAbstract = function (e) {
		if (e.sym === this.toFind) return e;

		var expr = e.expr.visit(this);
		if (expr === e.expr) return e;
		else return new Expr.Abstract(e.sym, expr);
	};
	SymbolSubstitutor.prototype.visitApply = function (e) {
		var left = e.left.visit(this);
		var right = e.right.visit(this);
		if (left === e.left && right === e.right) return e;
		else return new Expr.Apply(left, right);
	};
	SymbolSubstitutor.prototype.visitIdent = function (e) {
		return e.ident === this.toFind ? this.toReplace : e;
	};

	function LazySubstitutor(exprSource, exprDest) {
		this.creations = new Collections.Map();
		this.source = exprSource;
		this.dest = exprDest;
	}
	LazySubstitutor.prototype.visitAbstract = function (e) {
		if (e === this.source) return this.dest;

		var expr = e.expr.visit(this);
		if (expr === e.expr) {
			return e;
		} else {
			var ret = this.creations.get(e);
			if (ret === null) {
				ret = new Expr.Abstract(e.sym, expr);
				this.creations.put(e, ret);
			}
			return ret;
		}
	};
	LazySubstitutor.prototype.visitApply = function (e) {
		if (e === this.source) return this.dest;

		var left = e.left.visit(this);
		var right = e.right.visit(this);
		if (left === e.left && right === e.right) {
			return e;
		} else {
			var ret = this.creations.get(e);
			if (ret === null) {
				ret = new Expr.Apply(left, right);
				this.creations.put(e, ret);
			}
			return ret;
		}
	};
	LazySubstitutor.prototype.visitIdent = function (e) {
		return e === this.source ? this.dest : e;
	};

	function EagerSubstitutor(exprSource, exprDest) {
		this.source = exprSource;
		this.dest = exprDest;
	}
	EagerSubstitutor.prototype.visitAbstract = function (e) {
		if (e === this.source) return this.dest;

		var expr = e.expr.visit(this);
		return expr === e.expr ? e : new Expr.Abstract(e.sym, expr);
	};
	EagerSubstitutor.prototype.visitApply = function (e) {
		if (e === this.source) return this.dest;

		var right = e.right.visit(this);
		if (right !== e.right) return new Expr.Apply(e.left, right);
		var left = e.left.visit(this);
		if (left !== e.left) return new Expr.Apply(left, e.right);
		return e;
	};
	EagerSubstitutor.prototype.visitIdent = function (e) {
		return e === this.source ? this.dest : e;
	};

	function LazySlowSubstitutor(exprSource, exprDest) {
		this.source = exprSource;
		this.dest = exprDest;
	}
	LazySlowSubstitutor.prototype.visitAbstract = function (e) {
		if (e === this.source) return this.dest;

		var expr = e.expr.visit(this);
		return expr === e.expr ? e : new Expr.Abstract(e.sym, expr);
	};
	LazySlowSubstitutor.prototype.visitApply = function (e) {
		if (e === this.source) return this.dest;

		var left = e.left.visit(this);
		if (left !== e.left) return new Expr.Apply(left, e.right);
		var right = e.right.visit(this);
		if (right !== e.right) return new Expr.Apply(e.left, right);
		return e;
	};
	LazySlowSubstitutor.prototype.visitIdent = function (e) {
		return e === this.source ? this.dest : e;
	};

	var simplify = function (original, options) {
		var candFinder;
		var substClass;
		if (options.evalOrder === EAGER_EVALUATION) {
			candFinder = new EagerCandidateFinder(options);
			substClass = EagerSubstitutor;
		} else if (options.evalOrder === LAZY_SLOW_EVALUATION) {
			candFinder = new LazyCandidateFinder(options);
			substClass = LazySlowSubstitutor;
		} else {
			candFinder = new LazyCandidateFinder(options);
			substClass = LazySubstitutor;
		}

		var source = original.visit(candFinder);
		if (source === null) return original;
		var dest = candFinder.reduce(source);
		return original.visit(new substClass(source, dest));
	};

	simplify.EAGER_EVALUATION = EAGER_EVALUATION;
	simplify.LAZY_EVALUATION = LAZY_EVALUATION;
	simplify.LAZY_SLOW_EVALUATION = LAZY_SLOW_EVALUATION;

	simplify.APPLIED = 0;
	simplify.PURE_CHURCH = PURE_CHURCH;
	simplify.PURE = PURE;

	return simplify;
}());
