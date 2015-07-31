/* (c) 2012, Carl Burch, cburch@cburch.com
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
docs = (function ($) {
	"use strict";

	function showSectionHandler(secName) {
		return function (e) {
			var link = $('#doc' + secName + 'Item');
			var section = $('#doc' + secName + 'Sec');

			e.preventDefault();
			if (section.css('display') === 'none') {
				$('.docSection:not(#doc' + secName + 'Sec)').fadeOut(50);
				section.fadeIn(50);

				$('.docContents a:not(#doc' + secName + 'Item)').removeClass('docItemSelected');
				link.addClass('docItemSelected');
			}
		};
	}

	function initItem(secName, selected) {
		var link = $('#doc' + secName + 'Item');
		var section = $('#doc' + secName + 'Sec');

		link.click(showSectionHandler(secName));

		if (selected) {
			link.addClass("docItemSelected");
			section.show();
		} else {
			link.removeClass("docItemSelected");
			section.hide();
		}
	}

	var docs = {};

	docs.init = function () {
		initItem("About", true);
		initItem("Background", false);
		initItem("Basics", false);
		initItem("Dictionary", false);
		initItem("Options", false);
	};

	return docs;
}(jQuery));
