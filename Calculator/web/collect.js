/* (c) 2012, Carl Burch, cburch@cburch.com
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
Collections = (function () {
	"use strict";

	function Set() {
		this.data = {};
	}

	Set.prototype.contains = function (value) {
		var key = '' + value.hashCode;
		if (this.data.hasOwnProperty(key)) {
			var candidates = this.data[key];
			for (var i = candidates.length - 1; i >= 0; i--) {
				if (candidates[i].equals(value)) {
					return true;
				}
			}
		}
		return false;
	};

	Set.prototype.add = function (value) {
		var key = '' + value.hashCode;
		if (this.data.hasOwnProperty(key)) {
			if (this.contains(value)) {
				return false;
			} else {
				this.data[key].push(value);
				return true;
			}
		} else {
			this.data[key] = [value];
			return true;
		}
	};

	Set.prototype.remove = function (value) {
		var key = '' + value.hashCode;
		if (this.data.hasOwnProperty(key)) {
			var candidates = this.data[key];
			for (var i = candidates.length - 1; i >= 0; i--) {
				if (candidates[i].equals(value)) {
					if (candidates.length === 1) {
						delete this.data[key];
					} else {
						candidates.splice(i, 1);
					}
					return true;
				}
			}
		}
		return false;
	};

	function Map() {
		this.data = {};
	}

	Map.prototype.get = function (key) {
		var hashCode = '' + key.hashCode;
		if (this.data.hasOwnProperty(hashCode)) {
			var candidates = this.data[hashCode];
			for (var i = candidates.length - 1; i >= 0; i--) {
				if (candidates[i].key.equals(key)) {
					return candidates[i].value;
				}
			}
		}
		return null;
	};

	Map.prototype.put = function (key, value) {
		var hashCode = '' + key.hashCode;
		if (this.data.hasOwnProperty(hashCode)) {
			var candidates = this.data[hashCode];
			for (var i = candidates.length - 1; i >= 0; i--) {
				if (candidates[i].key.equals(key)) {
					var old = candidates[i].value;
					candidates[i].value = value;
					return old;
				}
			}
			candidates.push({ key: key, value: value });
			return null;
		} else {
			this.data[hashCode] = [{ key: key, value: value }];
			return null;
		}
	};

	Map.prototype.remove = function (key, value) {
		var hashCode = '' + key.hashCode;
		if (this.data.hasOwnProperty(hashCode)) {
			var candidates = this.data[hashCode];
			for (var i = candidates.length - 1; i >= 0; i--) {
				if (candidates[i].key.equals(key)) {
					var old = candidates[i].value;
					candidates.splice(i, 1);
					return old;
				}
			}
			return null;
		} else {
			return null;
		}
	};

	return { Set: Set, Map: Map };
}());
