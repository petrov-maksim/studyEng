let checkBoxes;

/**
 * Добавляет актуальные WS в options
 */
function updateWSOptions() {
    let wsList = document.querySelector(".options-ws-list");
    wsList.style.display ="";

    if (document.querySelector(".options-ws-list > ul") !== null)
        document.querySelector(".options-ws-list > ul").remove();

    if (wordSets_idMap.size === 1) {
        wsList.style.display = "none";
        return;
    }

    let ul = document.createElement("ul");

    for (let ws of wordSets_idMap.values()){
        if (ws["main"] === true)
            continue;
        let li = document.createElement("li");
        li.textContent = ws["name"];
        li.setAttribute("wordSetId", ws["wordSetId"]);
        li.addEventListener("click", () => onMoveWordsToWS(ws["wordSetId"]));
        ul.append(li);
    }

    wsList.append(ul);
}

function onMoveToTrainings(event) {
    let trainingId = event.currentTarget.getAttribute("trainingId");
    let wordIds = getSelectedRows().map(item => item.getAttribute("word-id"));

    fetch(ROOT_URL + "trainings/moveWordsToLearning?trainingId=" + trainingId + "&wordId=" + wordIds.join(","),{
        method: "POST"
    });

    dropSelection();
    toggleAwFormAndOptions();
}

function onMoveWordsToWS(wordSetId){
    let wordIds = getSelectedCheckBoxes().map(checkBox => checkBox.parentElement.parentElement.getAttribute("word-id"));

    fetch(ROOT_URL + "dictionary/addWordsToWordSet?wordSetId="+wordSetId + "&wordId=" + wordIds.join(","), {
        method:'POST'
    });

    dropSelection();
    toggleAwFormAndOptions();
}

function onRemoveWordsFromWS() {
    let wordIds = getSelectedCheckBoxes().map(checkBox => checkBox.closest(".words-table-row").getAttribute("word-id"));

    if (confirm("Вы уверены, что хотите удалить выбранные слова?")){
        removeWords(wordIds);

        getSelectedCheckBoxes().forEach(checkBox => checkBox.closest(".words-table-row").remove());
        toggleAwFormAndOptions();
        resetIndexes();
    }
}


function toggleAwFormAndOptions() {
    //Live HtmlCollection
    if(checkBoxes == null)
        checkBoxes = document.getElementsByClassName("select-word-checkbox");

    if (getSelectedCheckBoxes().length > 0)
        hideAwForm();
    else
        hideOptions();
}

function hideAwForm() {
    document.getElementById("options-container").classList.remove("options-hide");
    document.getElementById("options-container").classList.add("options-show");

    document.getElementById("add-word-form").classList.remove("add-word-show");
    document.getElementById("add-word-form").classList.add("add-word-hide");
}

function hideOptions() {
    document.getElementById("options-container").classList.add("options-hide");
    document.getElementById("options-container").classList.remove("options-show");

    document.getElementById("add-word-form").classList.add("add-word-show");
    document.getElementById("add-word-form").classList.remove("add-word-hide");
}

function getSelectedCheckBoxes() {
    let selected = [];
    for (let checkBox of checkBoxes)
        if (checkBox.checked)
            selected.push(checkBox);

    return selected;
}

function dropSelection() {
    getSelectedCheckBoxes().forEach(checkBox => checkBox.checked = false);
}