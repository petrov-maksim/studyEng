const URL = "http://localhost:8080/";
const POST = "POST";
const GET = "GET";
function init() {
    getWordsForUser();
    getWordSets();
}

/**                                            Get Functions                                          */
async function getWordsForUser() {
    let words;
    let response = await fetch(URL + "dictionary/getWordsForUser?index=0");

    if (response.status === 401){
        alert("U're not authorized");
        return;
    }

    if (!response.ok){
        alert("Smt wrong probably bad request");
        return;
    }

    do{
        alert("making req");
        response = await fetch(URL + "dictionary/getWordsForUser",{
            method: 'GET',
            headers:{
                'handling':"true"
            }
        });
    }while (response.status === 100);

    if (response.ok){
        words = await response.json();

        for (let word of words)
            alert(word);
    }
    else
        alert("Smt wrong");
}



async function getWordSets() {

}

function getWordsFromWordSet() {

}

function getTranslationForWord() {

}


/**                                           Add Functions                                          */
function addWordForUser() {
    
}

function addWordSet() {

}

function addWordsToWordSet() {

}

function addExample() {

}

function addTranslation() {

}

/**                                           Remove Functions                                          */
function removeWordsForUser() {

}

function removeWordsFromWordSet() {

}

function removeWordSet() {

}

function removeTranslation() {

}

/**                                           Update Functions                                          */
function updateWordSet() {

}

/**                                           frontend Functions                                          */
function setWords(words) {
    let table = document.getElementById("words-table");

}