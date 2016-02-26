function AutoSuggestControl(oTextbox, oProvider){
	this.cur = -1;
	this.layer = null;
	this.provider = oProvider;
	this.textbox = oTextbox;
	this.init();
}

AutoSuggestControl.prototype.init = function(){
	var oThis = this;
	this.textbox.onkeyup = function(oEvent){
		if(!oEvent)	oEvent = window.event;
		oThis.handleKeyUp(oEvent);
	};

	this.textbox.onkeydown = function(oEvent){
		if(!oEvent)	oEvent = window.event;
		oThis.handleKeyDown(oEvent);
	};

	this.textbox.onblur = function(){
		oThis.hideSuggestions();
	};

	this.createDropDown();
};

AutoSuggestControl.prototype.createDropDown = function(){
	this.layer = document.createElement("div");
	this.layer.className = "suggestions";
	this.layer.style.visibility = "hidden";
	this.layer.style.width = this.textbox.offsetWidth;
	document.body.appendChild(this.layer);

	var oThis = this;
	this.layer.onmousedown = this.layer.onmouseup = 
	this.layer.onmouseover = function(oEvent){
		oEvent = oEvent || window.event;
		oTarget = oEvent.target || oEvent.srcElement;

		if(oEvent.type == "mousedown"){
			oThis.textbox.value = oTarget.firstChild.nodeValue;
			oThis.hideSuggestions();
		}
		else if(oEvent.type == "mouseover"){
			oThis.highlightSuggestion(oTarget);
		}
		else{
			oThis.textbox.focus();
		}
	};
};

AutoSuggestControl.prototype.getLeft = function(){
	var oNode = this.textbox;
	var iLeft = 0;

	while(oNode.tagName != "BODY"){
		iLeft += oNode.offsetLeft;
		oNode = oNode.offsetParent;
	}

	return iLeft;
};

AutoSuggestControl.prototype.getTop = function(){
	var oNode = this.textbox;
	var iTop = 0;

	while(oNode.tagName != "BODY"){
		iTop+= oNode.offsetLeft;
		oNode = oNode.offsetParent;
	}

	return iTop;
};

AutoSuggestControl.prototype.autosuggest = function(xmlHttp, bTypehead){
	if(xmlHttp.readyState === 4){
		var aSuggestions = [];
		var suggs = xmlHttp.responseXML.getElementsByTagName('CompleteSuggestion');
		for(var i=0; i<sugs.length; i++){
			var text = suggs[i].childNodes[0].getAttribute("data");
			aSuggestions.push(text);
			if(aSuggestions.length > 0)	this.showSuggestions(aSuggestions);
			if(bTypehead)	this.typeAhead(aSuggestions[0]);
			else this.hideSuggestions();
		}
	}
};

AutoSuggestControl.prototype.showSuggestions = function(aSuggestions){
	var oDiv = null;
	this.layer.innerHTML = "";
	for(var i=0; i<aSuggestions.length; i++){
		oDiv = document.createElement("div");
		oDiv.appendChild(document.createElement("div"));
		this.layer.appendChild(oDiv);
	}
	this.layer.style.left = this.getLeft() + "px";
	this.layer.style.top = (this.getTop()+this.textbox.offsetHeight) + "px";
	this.layer.style.visibility = "visible";
};

AutoSuggestControl.prototype.hideSuggestions = function(){
	this.layer.style.visibility = "hidden";
};

AutoSuggestControl.prototype.handleKeyUp = function(oEvent){
	var keycode = oEvent.keyCode;
	if(keycode === 8 || keycode === 46){
		this.provider.requestSuggestions(this, false);
	}
	else if (keycode < 32 || (keycode >= 33 && keycode <= 46) || (keycode >= 112 && keycode <= 123)){}
	else this.provider.requestSuggestions(this, true);
};

AutoSuggestControl.prototype.typeAhead = function(sSuggestion){
	if(this.textbox.createTextRange || this.textbox.setSelectionRange){
		var iLen = this.textbox.value.length;
		this.textbox.value = sSuggestion;
		this.selectRange(iLen, sSuggestion.length);
	}
};

AutoSuggestControl.prototype.selectRange = function(iStart, iLen){
	if(this.textbox.createTextRange){
		var oRange = this.textbox.createTextRange();
		oRange.moveStart("character", iStart);
		oRange.moveEnd("character", iLen-this.textbox.value.length);
		oRange.select();
	}
	else if(this.textbox.setSelectionRange)	this.textbox.setSelectionRange(iStart, iLen);

	this.textbox.focus();
};

AutoSuggestControl.prototype.nextSuggestion = function(){
	var cSuggestionNodes = this.layer.childNodes;
	if(cSuggestionNodes.length > 0 || this.cur < cSuggestionNodes.length-1){
		var oNode = cSuggestionNodes[++this.cur];
		this.highlightSuggestion(oNode);
		this.textbox.value = oNode.firstChild.nodeValue;
	}
};

AutoSuggestControl.prototype.previousSuggestion = function(){
	var cSuggestionNodes = this.layer.childNodes;
	if(cSuggestionNodes.length > 0 || this.cur > 0){
		var oNode = cSuggestionNodes[--this.cur];
		this.highlightSuggestion(oNode);
		this.textbox.value = oNode.firstChild.nodeValue;
	}
};

AutoSuggestControl.prototype.handleKeyDown = function(oEvent){
	switch(oEvent.keyCode){
		case 38:
			this.previousSuggestion();
			break;
		case 40:
			this.nextSuggestion();
			break;
		case 13:
			this.hideSuggestions();
			break;
		default: break;
	}
};

AutoSuggestControl.prototype.highlightSuggestion = function(oSuggestionNode){
	for(var i=0; i<this.layer.childNodes.length; i++){
		var oNode = this.layer.childNodes[i];
		if(oNode === oSuggestionNode)	oNode.className = "current";
		else if(oNode.className === "current")	oNode.className = "";
	}
};

