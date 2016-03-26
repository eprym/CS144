function SuggestionProvider(){
	this.xmlHttp = new XMLHttpRequest();
}

SuggestionProvider.prototype.requestSuggestions = function(oAutoSuggestControl, bTypeAhead){
	var content = oAutoSuggestControl.textbox.value;
	if(content !== ""){
		var request = "suggest?q=" + encodeURI(content);
		this.xmlHttp.open("GET", request);
		this.xmlHttp.onreadystatechange = oAutoSuggestControl.autosuggest(this.xmlHttp, bTypeAhead);
		this.xmlHttp.send(null);
	}
	else	oAutoSuggestControl.hideSuggestions();
	
};