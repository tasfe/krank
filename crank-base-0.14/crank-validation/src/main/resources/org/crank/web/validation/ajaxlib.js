/** ------------------------------------------------ */
/** ------------------------------------------------ */
/** ------------------------------------------------ */
/** ------------------------------------------------ */
/** This is from org.crank.web.validation.ajaxlib.js. */
function createAjaxRequest() {
	try {
		var ajaxRequest = new XMLHttpRequest();
	}
	catch(error) {
		var ieXmlHttpVersions = new Array();
		ieXmlHttpVersions[ieXmlHttpVersions.length] = "MSXML2.XMLHttp.5.0";
		ieXmlHttpVersions[ieXmlHttpVersions.length] = "MSXML2.XMLHttp.4.0";
		ieXmlHttpVersions[ieXmlHttpVersions.length] = "MSXML2.XMLHttp.3.0";
		ieXmlHttpVersions[ieXmlHttpVersions.length] = "MSXML2.XMLHttp";
		ieXmlHttpVersions[ieXmlHttpVersions.length] = "Microsoft.XMLHttp";
		
		var index;
		for (index=0; index < ieXmlHttpVersions.length; index++) {
			try {
				var ajaxRequest = new ActiveXObject(ieXmlHttpVersions[index]);
				break;
			}
			catch (error) {
				return false;
			}
		}
	}
	return ajaxRequest;
}
/** End of org.crank.web.validation.ajaxlib.js. */
/** ------------------------------------------------ */
/** ------------------------------------------------ */
/** ------------------------------------------------ */