$(document).ready(function() {
    var activeItemsIndexArray = [];
    // #createActionInput Input Field
	$("#actionCreateForm\\:createActionInput").on({
		keyup: function(event) {
            $this = $(this);
            var currentGlossary = getCurrentGlossary(false);
			checkForRedundancy($this, event, currentGlossary, activeItemsIndexArray);
		}
	});
	// #createDecisionInput Input Field
	$("#decisionCreateForm\\:createDecisionInput").on({
		keyup: function(event) {
            $this = $(this);
            var currentGlossary = getCurrentGlossary(false);
			checkForRedundancy($this, event, currentGlossary, activeItemsIndexArray);
		}
	});
	// #createReasonInput Input Field
	$("#reasonCreateForm\\:createReasonInput").on({
		keyup: function(event) {
            $this = $(this);
            var currentGlossary = getCurrentGlossary(false);
			checkForRedundancy($this, event, currentGlossary, activeItemsIndexArray);
		}
	});
});

var checkForRedundancy = function ($this, event, currentGlossary, activeItemsIndexArray) {
    var $input = $this;
    var $inputText = $input.val();
    var inputTextLower = $inputText.toLowerCase();
    var inputTextLowerArray = inputTextLower.split(" ");
    // check if item matches the $input-text
    // add class "match" for highlighting and add its index to activeItemsIndexArray
    if ($inputText.length > 3) {   
        // "Gehaltserhöhung beantragen" soll von
        // "gehalt" oder "beantragen" getroffen werden
        inputTextLowerArray.forEach(function (inputTextLowerPart, index) {
            findMatchingElements(currentGlossary, inputTextLowerArray, activeItemsIndexArray);        
        });
    }    
    // check if item still matches the $input-text
    // otherwise delete item
    if (activeItemsIndexArray.length > 0) {
        var arrayIndexesToDelete = [];
        findUnmatchedElements(currentGlossary, inputTextLowerArray, activeItemsIndexArray, arrayIndexesToDelete);
        // clean up activeItemsIndexArray
        if (arrayIndexesToDelete.length > 1) {
            deleteUnmatchedElements(activeItemsIndexArray, arrayIndexesToDelete);
        } 
    }
    //console.log("arraysToDelete: " + arrayIndexesToDelete);
    //console.log("activeItemsIndexArray: " + activeItemsIndexArray);
};

var findMatchingElements = function (currentGlossary, inputTextLowerArray, activeItemsIndexArray) {
    for (var i = 0; i < inputTextLowerArray.length; i++) {
        for (var j = 0; j < currentGlossary.length; j++) {
            var currentGlossaryItemTextLower = currentGlossary[j].innerHTML.toLowerCase();
            if (currentGlossaryItemTextLower.indexOf(inputTextLowerArray[i]) != -1 && inputTextLowerArray[i].length > 3) {
                // add Class "match" to highlight the active element
                currentGlossary[j].classList.add("match");
                // push item to activeItemsIndexArray only if not already in there
                if (activeItemsIndexArray.indexOf(j) == -1) {
                    activeItemsIndexArray.push(j);
                }
            }
        };        
    };
};

var findUnmatchedElements = function (currentGlossary, inputTextLowerArray, activeItemsIndexArray, arraysToDelete) {
    var iterateCount = activeItemsIndexArray.length;
    for (var i = 0; i < iterateCount; i++) {
        var deleteElement = true;
        var currentActiveGlossaryItemTextLower = currentGlossary[activeItemsIndexArray[i]].innerHTML.toLowerCase();
        for (var j = 0; j < inputTextLowerArray.length; j++) {
            if (currentActiveGlossaryItemTextLower.indexOf(inputTextLowerArray[j]) == -1 || inputTextLowerArray[j].length < 4) {
            
            } else {
                deleteElement = false;
            }
        }
        if (deleteElement) {
            // dehighlight the item
            currentGlossary[activeItemsIndexArray[i]].classList.remove("match");
            // remove from activeItemsIndexArray
            arraysToDelete.push(activeItemsIndexArray[i]);
        }
    }
};

var deleteUnmatchedElements = function (activeItemsIndexArray, arraysToDelete) {
    for (var i = 0; i < activeItemsIndexArray.length; i++) {
        for (var j = 0; j < arraysToDelete.length; j++) {
            if (activeItemsIndexArray[i] == arraysToDelete[j]) {
                activeItemsIndexArray.splice(i,1);
            }
        }
    }
};

var getCurrentGlossary = function (asArray) {
    asArray = typeof asArray !== 'undefined' ? asArray : false;
    var $glossary = $(".glossary-labels");
    var $glossaryItems = $glossary.find(".badget-info");
    var currentGlossary;
    if (!asArray) {
        currentGlossary = $glossaryItems;
    } else {
        var $glossaryItemArray = [];
        $glossaryItems.each(function() {
            $glossaryItemArray.push($(this).text());
        });
        currentGlossary = $glossaryItemArray;
    }
    return currentGlossary;
};