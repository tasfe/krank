var DH = 0;
var an = 0;
var al = 0;
var ai = 0;

if (document.getElementById) {
	ai = 1; 
	DH = 1;
} else { 
	if (document.all) {
		al = 1; DH = 1;
	} else { 
		browserVersion = parseInt(navigator.appVersion); 
		if ((navigator.appName.indexOf('Netscape') != -1) && (browserVersion == 4)) {
			an = 1; DH = 1;
		}
	}
} 

function fd(oi, wS) {
	if (ai) return wS ? document.getElementById(oi).style:document.getElementById(oi); 
	if (al) return wS ? document.all[oi].style: document.all[oi]; 
	if (an) return document.layers[oi];
}

function pw() {
	return window.innerWidth != null? window.innerWidth: document.body.clientWidth != null? document.body.clientWidth:null;
}

function mouseX(evt) {
	if (evt.pageX) return evt.pageX; 
	else if (evt.clientX) return evt.clientX + (document.documentElement.scrollLeft ?  document.documentElement.scrollLeft : document.body.scrollLeft); 
	else return null;
}

function mouseY(evt) {
	if (evt.pageY) return evt.pageY; 
	else if (evt.clientY)return evt.clientY + (document.documentElement.scrollTop ? document.documentElement.scrollTop : document.body.scrollTop); 
	else return null;
}

function popUp(evt,oi) {
	if (DH) {
		var wp = pw(); 
		ds = fd(oi,1); 
		dm = fd(oi,0); 
		st = ds.visibility; 
		if (dm.offsetWidth) ew = dm.offsetWidth; 
		else if (dm.clip.width) ew = dm.clip.width; 
		if (st == "visible" || st == "show") { 
			ds.visibility = "hidden"; 
		} else {
			tv = mouseY(evt) + 20; lv = mouseX(evt) - (ew/4); 
			if (lv < 2) lv = 2; 
			else if (lv + ew > wp) lv -= ew/2; 
			if (!an) {
				lv += 'px';tv += 'px';
			} 
			ds.left = lv; ds.top = tv; ds.visibility = "visible";
		}
	}
}

function wipePopUp(evt,oi) {
	if (DH) {
		var wp = pw(); 
		ds = fd(oi,1); 
		dm = fd(oi,0); 
		ds.visibility = "hidden"; 
	}
}
                
function toggleAllCheckBoxes(FormName, CheckValue) {
	if(!document.forms[FormName])
		return;

	var oInp = document.forms[FormName].getElementsByTagName('input');
	var objCheckBoxes = new Array();
	var j=0;
	for(var i=0;i<oInp.length;i++){
		if(oInp[i].getAttribute('type')=='checkbox'){
			objCheckBoxes[j] = oInp[i];
			j++;
		}
	}	
	
	if(!objCheckBoxes)
		return;
		
	var countCheckBoxes = objCheckBoxes.length;
	
	if(!countCheckBoxes)
		objCheckBoxes.checked = CheckValue;
	else
		// set the check value for all check boxes!
		for(var i = 0; i < countCheckBoxes; i++)
			objCheckBoxes[i].checked = CheckValue;
}

function setExclusiveGroup(FormName, Caller, GroupId, ExclusiveValue) {
	if(!document.forms[FormName])
		return;

	var oInp = document.forms[FormName].getElementsByTagName('input');
	var objCheckBoxes = new Array();
	var j=0;
	for(var i=0;i<oInp.length;i++){
		if(oInp[i].getAttribute('type')=='checkbox'){
			if (oInp[i].getAttribute('id').indexOf(FormName+":"+GroupId) == 0) {
				if (oInp[i].getAttribute('id').indexOf(FormName+":"+GroupId+Caller) == -1) {
					objCheckBoxes[j++] = oInp[i];
				}
			}
		}
	}	
	
	if(!objCheckBoxes)
		return;
		
	var countCheckBoxes = objCheckBoxes.length;
	
	if(!countCheckBoxes)
		objCheckBoxes.checked = ExclusiveValue;
	else
		// set the check value for all check boxes!
		for(var i = 0; i < countCheckBoxes; i++)
			objCheckBoxes[i].checked = ExclusiveValue;
}

function setEnableGroup(FormName, Caller, GroupId) {
	if(!document.forms[FormName])
		return;

	// Grab all the inputs and look for the one that controls the enable / disable
	var objInputs = new Array();
	var DisableValue = true;
	var j=0;
	var oInp = document.forms[FormName].getElementsByTagName('input');
	for(var i=0;i<oInp.length;i++){
		if (oInp[i].getAttribute('id').indexOf(FormName+":"+GroupId) == 0) {
			if (oInp[i].getAttribute('id').indexOf(FormName+":"+GroupId+Caller) == -1) {
				objInputs[j++] = oInp[i];
			} else {
				DisableValue = !oInp[i].checked; 		
			}
		}
	}	
	
	if(!objInputs)
		return;
		
	// Toggle the state of the remaining INPUT components
	var countInputs = objInputs.length;
	for(var i = 0; i < countInputs; i++)
		objInputs[i].disabled = DisableValue;
		

	// Toggle the state of the remaining TEXTAREA components
	objInputs = new Array();
	j = 0;
	oInp = document.forms[FormName].getElementsByTagName('textarea');
	for(var i=0;i<oInp.length;i++){
		if (oInp[i].getAttribute('id').indexOf(FormName+":"+GroupId) == 0) {
			if (oInp[i].getAttribute('id').indexOf(FormName+":"+GroupId+Caller) == -1) {
				objInputs[j++] = oInp[i];
			}
		}
	}	

	countInputs = objInputs.length;
	for(var i = 0; i < countInputs; i++)
		objInputs[i].disabled = DisableValue;
		
		
	// Toggle the state of the remaining SELECT components
	objInputs = new Array();
	j = 0;
	oInp = document.forms[FormName].getElementsByTagName('select');
	for(var i=0;i<oInp.length;i++){
		if (oInp[i].getAttribute('id').indexOf(FormName+":"+GroupId) == 0) {
			if (oInp[i].getAttribute('id').indexOf(FormName+":"+GroupId+Caller) == -1) {
				objInputs[j++] = oInp[i];
			}
		}
	}	

	countInputs = objInputs.length;
	for(var i = 0; i < countInputs; i++)
		objInputs[i].disabled = DisableValue;
		
		
	// Toggle the state of the remaining BUTTON components
	objInputs = new Array();
	j = 0;
	oInp = document.forms[FormName].getElementsByTagName('button');
	for(var i=0;i<oInp.length;i++){
		if (oInp[i].getAttribute('id').indexOf(FormName+":"+GroupId) == 0) {
			if (oInp[i].getAttribute('id').indexOf(FormName+":"+GroupId+Caller) == -1) {
				objInputs[j++] = oInp[i];
			}
		}
	}	

	countInputs = objInputs.length;
	for(var i = 0; i < countInputs; i++)
		objInputs[i].disabled = DisableValue;
		
		
	// Toggle the state of the remaining IMG components
	objInputs = new Array();
	j = 0;
	oInp = document.forms[FormName].getElementsByTagName('img');
	for(var i=0;i<oInp.length;i++){
		if (oInp[i].getAttribute('id').indexOf(FormName+":"+GroupId) == 0) {
			if (oInp[i].getAttribute('id').indexOf(FormName+":"+GroupId+Caller) == -1) {
				objInputs[j++] = oInp[i];
			}
		}
	}	

	countInputs = objInputs.length;
	for(var i = 0; i < countInputs; i++)
		objInputs[i].disabled = DisableValue;
		
		
}

