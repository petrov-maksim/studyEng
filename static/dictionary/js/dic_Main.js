const pageHeight = document.documentElement.getBoundingClientRect().bottom;
// Актуальные слова (Отображаемые)
const word_idMap = new Map();
// Актуальные наборы слов (Отображаемые)
const wordSets_idMap = new Map();
// Паузы между запросами
const TIMEOUT = 50;
const ROOT_URL = "http://localhost:8080/";
const DELETE_SIGN_PATH = "../images/delete-sign.png";
const PENCIL_PIC_PATH = "../images/pencil.png";
const DOWNLOAD_PIC_PATH = "../images/download.png";
const DEFAULT_WS_COVER_PATH = "../images/defaultWordSet.png";

let currentWordSet;
let index = 1;
let DBIndex = 0;
let userAuthorised;


async function init() {
    if (!isUserAuthorised())
        return;
    addWordsToTable(await getWordsForUser());
    addWSToSlider(await getWordSets());
    initCurrentWS();
    initSlider();
    setListeners();
    updateWSOptions();
}

async function addWord() {
    let word = {};
    word["word"] = document.getElementById("word-input").value.trim();
    word["translations"] = [document.getElementById("translation-input").value.trim()];

    if (word["word"].length === 0)
        return;

    word["id"] = await addWordForUser(word);
    document.getElementById("word-input").value = "";
    document.getElementById("translation-input").value = "";

    if (word["id"] === 400){
        alert("Данное слово уже существует.");
        return;
    }else if (word["id"] === 500){
        alert("Произошла непредвиденная ошибка, перезагрузите страницу и попробуйте снова.");
        return;
    }

    if (currentWordSet["main"] !== true)
        addWordToWordSet(word["id"]);

    addWordsToTable([word], true);
}
function removeWords(wordIds){
    if (currentWordSet["main"] === true)
        removeWordForUser(wordIds);
    else
        removeWordFromWS(wordIds);
}

async function getWordsForUser() {
    let response = await fetch(ROOT_URL + "dictionary/getWordsForUser?index=" + DBIndex);

    if (!response.ok)
        return;

    do{
        response = await fetch(ROOT_URL + "dictionary/getWordsForUser",{
            method: 'GET',
            headers:{
                'handling':"true"
            }
        });

        if (response.headers.get("ready") === "false" && response.ok)
            await new Promise(resolve => setTimeout(resolve, TIMEOUT));
    }while (response.headers.get("ready") === "false" && response.ok);

    if (response.ok){
        try{
            return await response.json();
        }catch (e) {}
    }
    return null;
}
async function addWordForUser(word){
    let response = await fetch(ROOT_URL + "dictionary/addWordForUser",{
        method:'POST',
        body: JSON.stringify(word)
    });

    if (!response.ok)
        return;

    do{
        response = await fetch(ROOT_URL + "dictionary/addWordForUser",{
            method: 'POST',
            headers:{
                'handling':"true"
            }
        });

        if (response.headers.get("ready") === "false" && response.ok)
            await new Promise(resolve => setTimeout(resolve, TIMEOUT));
    }while (response.headers.get("ready") === "false" && response.ok);

    if(response.ok)
        return Number.parseInt(await response.text());
    else
        return response.status;

}
function removeWordForUser(wordIds) {
    fetch(ROOT_URL + "dictionary/removeWordForUser?wordId=" + wordIds.join(","),{
        method:'POST',
    });
}

function removeWordsListener(event){
    let checkedRows = getSelectedRows();

    if (checkedRows.length === 0) {
        removeWords([event.currentTarget.closest(".words-table-row").getAttribute("word-id")]);
        event.currentTarget.closest(".words-table-row").remove();
    }
    else {
        removeWords(checkedRows.map(item => item.getAttribute("word-id")));
        checkedRows.forEach(value => value.remove());
    }

    resetIndexes();
}

function setListeners(){
    window.addEventListener('scroll', loadWordsOnScroll);
    document.querySelectorAll(".cursor-pointer").forEach(elem => elem.addEventListener("click", CreateWordPopup));
    document.querySelector(".profile-pic").addEventListener("click", onProfileClick);
}


function addWordsToTable(words, alone) {
    if (words === null)
        return;

    let tbody = document.querySelector("#words-table > tbody");

    let row;  //class="words-table-row"

    let tdCheckBox;
    let inpCheckBox; //type = "checkbox" class="select-word-checkbox"

    let tdIndex;    //class="index"

    let tdWord;
    let strongWord; //class="word"

    let tdTranslation;
    let strongTranslation;  //class="translation"

    let tdImg;
    let img;    //src="../images/delete-sign.png" height="30px"


    for (let word of words){
        word_idMap.set(+word["id"], word);
        if (+word["index"] > DBIndex)
            DBIndex = +word["index"];

        row = document.createElement("tr");
        row.setAttribute("class", "words-table-row");
        row.setAttribute("word-id", word['id']);

        tdCheckBox = document.createElement("td");
        inpCheckBox = document.createElement("input");
        inpCheckBox.setAttribute("class", "select-word-checkbox");
        inpCheckBox.setAttribute("type", "checkbox");
        inpCheckBox.addEventListener("input", toggleAwFormAndOptions);

        tdIndex = document.createElement("td");
        tdIndex.setAttribute("class", "index");
        tdIndex.innerText = index++ + "";

        tdWord = document.createElement("td");
        tdWord.setAttribute("class", "cursor-pointer");
        strongWord = document.createElement("strong");
        strongWord.setAttribute("class", "cursor-pointer word");
        strongWord.innerText = word['word'];

        tdTranslation = document.createElement("td");
        strongTranslation = document.createElement("strong");
        strongTranslation.setAttribute("class", "cursor-pointer translation");
        strongTranslation.innerText = word['translations'].join(", ");

        tdImg = document.createElement("td");
        img = document.createElement("img");
        img.setAttribute("src", DELETE_SIGN_PATH);
        img.setAttribute("height", "30px");
        img.addEventListener("click", removeWordsListener);

        tdCheckBox.append(inpCheckBox);
        tdWord.append(strongWord);
        tdTranslation.append(strongTranslation);
        tdImg.append(img);

        row.append(tdCheckBox);
        row.append(tdIndex);
        row.append(tdWord);
        row.append(tdTranslation);
        row.append(tdImg);

        if (alone) {
            tbody.prepend(row);
            resetIndexes();
        }
        else
            tbody.append(row);
    }
}

function removeWordsFromTable(){
    document.querySelector("#words-table > tbody").replaceWith(document.createElement("tbody"));
}

async function loadWordsOnScroll() {
    window.removeEventListener('scroll', loadWordsOnScroll);
    if (pageHeight < document.documentElement.clientHeight + 100) {
        let words = await getWordsForUser();
        if (words !== null){
            window.addEventListener('scroll', loadWordsOnScroll);
            addWordsToTable(words);
        }
    }
}

function getSelectedRows() {
    let checkedRows = [];
    for (let checkBox of document.getElementsByClassName("select-word-checkbox"))
        if (checkBox.checked)
            checkedRows.push(checkBox.parentElement.parentElement);
    return checkedRows;
}

function resetIndexes() {
    index = 1;
    document.querySelector("#words-table > tbody").querySelectorAll(".index").forEach(elem => elem.textContent = index++ + "");
}

function initCurrentWS() {
    for (let ws of wordSets_idMap.values())
        if (ws["main"] === true){
            currentWordSet = ws;
            return;
        }
}

async function isUserAuthorised() {
    if (userAuthorised === null){
        let response = await fetch(ROOT_URL + "authorised");
        userAuthorised = response.headers.get("auth");
    }
    return userAuthorised;
}

function onProfileClick() {

}