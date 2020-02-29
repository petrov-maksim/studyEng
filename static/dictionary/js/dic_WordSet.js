const shadowWS_onSelection = document.createElement("div");
shadowWS_onSelection.classList.add("shadowWS_onSelection");

async function getWordSets(){
    let response = await fetch(ROOT_URL +"dictionary/getWordSets");

    if (!response.ok)
        return;
    do{
        response = await fetch(ROOT_URL + "dictionary/getWordSets",{
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

function addWSToSlider(wordSets){
    for (let wordSet of wordSets){
        wordSets_idMap.set(+wordSet["wordSetId"], wordSet);
        let wsWrapper = document.querySelector(".slider__wrapper");
        let sliderItem = createSliderItem(wordSet, false);

        if (wordSet["main"] === true) {
            document.getElementById("addWsDiv").after(sliderItem);
            sliderItem.firstElementChild.prepend(shadowWS_onSelection);
        }
        else
            wsWrapper.append(sliderItem);
    }
}

async function OnWSSelection(event) {
    let target = event.currentTarget;
    currentWordSet = wordSets_idMap.get(+target.getAttribute("wordSetId"));

    event.stopPropagation();
    createLoader();
    DBIndex = 1;
    word_idMap.clear();
    hideOptions();

    removeWordsFromTable();
    addWordsToTable(await getWordsFromWS(event.currentTarget.getAttribute("wordSetId")));
    resetIndexes();
    setListeners();

    removeLoader();
    toggleSelectedWS(target);
}

async function getWordsFromWS(wordSetId) {
    let response = await fetch(ROOT_URL + "dictionary/getWordsFromWordSet?index=" + DBIndex + "&wordSetId=" + wordSetId);

    if (!response.ok)
        return;

    do{
        response = await fetch(ROOT_URL + "dictionary/getWordsFromWordSet",{
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

function addWordToWordSet(wordId){
    fetch(ROOT_URL + "dictionary/addWordsToWordSet?wordId="+wordId +"&wordSetId=" + currentWordSet["wordSetId"],{
        method:'POST'
    });
}

function removeWordFromWS(wordIds) {
    fetch(ROOT_URL + "dictionary/removeWordsFromWordSet?wordId=" + wordIds.join(",") + "&wordSetId=" + currentWordSet["wordSetId"],{
        method:'POST',
    });
}

function addWSListener() {
    createWordSetPopup();
    setWSPopupListeners(async function () {
        if (!checkWSName() || !(await checkWSImg())) {
            return;
        }

        addNewSliderItem();
        await sendAddWSData();
        removeWSPopup();
        updateWSOptions();
        initSlider();
    })
}

function editWSListener(event) {
    event.stopPropagation();
    let parentDiv = event.target.closest(".ws__item");
    let wordSetId = +parentDiv.getAttribute("wordSetId");
    createWordSetPopup();

    setWSPopupListeners(function () {
        if (!checkWSName() || !checkWSImg())
            return;

        updateSliderItem(parentDiv, wordSetId);
        sendEditWSData(wordSetId);
        removeWSPopup();
        updateWSOptions();
    });

    document.querySelector(".ws-popup > button").textContent = "Сохранить";
    document.querySelector(".ws-popup > input").value = parentDiv.querySelector(".si-num-name > div").textContent;
}

function removeWSListener(event) {
    event.stopPropagation();
    let parentDiv = event.target.closest(".ws__item");
    parentDiv.parentElement.remove();

    sendRemoveWSData(parentDiv.getAttribute("wordSetId"));
    wordSets_idMap.delete(+parentDiv.getAttribute("wordSetId"));
    updateWSOptions();
    restoreSlider();
}

function setWSPopupListeners(btnClickListener) {
    let wsUploadBtn = document.querySelector(".ws-popup > button");
    let wsPopupContainer = document.querySelector(".ws-popup-container");
    let wsPopup = document.querySelector(".ws-popup");

    wsPopupContainer.addEventListener("click", () => removeWSPopup());
    wsPopup.addEventListener("click", (event) => event.stopPropagation());
    wsUploadBtn.addEventListener("click", btnClickListener);
}

function addNewSliderItem() {
    let mainWordSet = document.querySelector(".main-ws");

    let wordSet = {};
    wordSet["wordSetId"] = -1;
    wordSet["main"] = false;
    wordSet["size"] = 0;
    wordSet["name"] = document.querySelector(".ws-name-input").value;
    try{
        if (document.getElementById("input__file").files.length !== 0)
            wordSet["image"] = URL.createObjectURL(document.getElementById("input__file").files[0]);
        else
            wordSet["image"] = DEFAULT_WS_COVER_PATH;
    }catch (e) {}

    mainWordSet.after(createSliderItem(wordSet, true));
}

function updateSliderItem(parentDiv, wordSetId) {
    let img = new Image();
    try{
        img.src = URL.createObjectURL(document.getElementById("input__file").files[0]);
        img.onload = function (){
            parentDiv.style.backgroundImage = "url(" + img.src + ")";
        };
    }catch (e) {}

    parentDiv.querySelector(".si-num-name > div").textContent = document.querySelector(".ws-name-input").value.trim();

    let ws = wordSets_idMap.get(wordSetId);
    ws["image"] = img.src;
    ws["name"] = document.querySelector(".ws-name-input").value.trim();
}

async function sendAddWSData() {
    let img = document.getElementById("input__file").files[0];
    let name = document.querySelector(".ws-name-input").value;
    let requestWitImg = false;

    let wordSet = {};
    wordSet["name"] = name;
    wordSet["size"] = 0;
    wordSet["isMain"] = false;
    if (img) {
        wordSet["image"] = img;
        requestWitImg = true;
    }

    let formData = new FormData();
    formData.append("file", img);

    let response = await fetch(ROOT_URL + "dictionary/addWordSet?name=" + name, {
        method: "POST",
        headers: {
            "img": requestWitImg
        },
        body: formData
    });

    if (!response.ok)
        return;

    do{
        response = await fetch(ROOT_URL + "dictionary/addWordSet",{
            method: 'POST',
            headers:{
                'handling':"true"
            }
        });

        if(response.headers.get("ready") === "false")
            await new Promise(resolve => setTimeout(resolve, TIMEOUT));
    }while (response.headers.get("ready") === "false" && response.ok);

    if(response.ok) {
        try{
            wordSet["wordSetId"] = Number.parseInt(response.headers.get("wordSetId"));
            wordSets_idMap.set(wordSet["wordSetId"], wordSet);
            wordSets_idMap.delete(-1);
            document.querySelector("div[wordSetId='-1']").setAttribute("wordSetId", wordSet["wordSetId"]+"");
        }catch (e) {}
    }
}

function sendEditWSData(wordSetId) {
    let img = document.getElementById("input__file").files[0];
    let name = document.querySelector(".ws-name-input").value;
    let requestWithImg = false;
    let formData;

    if (img) {
        requestWithImg = true;
        formData = new FormData();
    }

    fetch(ROOT_URL + "dictionary/updateWordSet?name=" + name, {
        method: "POST",
        headers: {
            "wordSetId": wordSetId,
            "img":requestWithImg
        },
        body: formData
    });
}

function sendRemoveWSData(wordSetId) {
    fetch(ROOT_URL + "dictionary/removeWordSet", {
        method: "POST",
        headers: {
            "wordSetId": wordSetId
        },
    });
}


function checkWSName() {
    let name = document.querySelector(".ws-popup > input").value.trim();
    if (name === ""){
        alert("Введите название");
        return false;
    }

    if (name.length > 30){
        alert("Название слишком длинное");
        return false;
    }
    return true;
}

async function checkWSImg() {
    let img = new Image();
    try{
        //Будет использованна обложка по умолчанию
        if (document.getElementById("input__file").files.length === 0)
            return true;
        img.src = URL.createObjectURL(document.getElementById("input__file").files[0]);
        if(document.getElementById("input__file").files[0].type.indexOf("image") === -1) {
            alert("Данный тип файла не поддерживается");
            return false;
        }
    }catch (e) {}

    let flag;

    img.onload = function () {
        if(img.size > 1048576) {
            alert("Размер обложки слишком большой, максимум: 1мб");
            flag = false;
            return;
        }

        let width  = img.naturalWidth  || img.width;
        let height = img.naturalHeight || img.height;

        if(width > 400 || height > 400) {
            alert("Размер обложки слишком большой, максимум 400х400px");
            flag = false;
            return;
        }

        if(width < 250 || height < 250) {
            alert("Размер обложки слишком мальенький, минимум 250х250px");
            flag = false;
            return;
        }
        flag = true;
    };

    while(!(img.naturalWidth  || img.width) || !img.src){
        await new Promise(resolve => setTimeout(resolve, TIMEOUT));
    }
    return flag;
}


function removeWSPopup(){
    document.getElementById("shadow").remove();
    document.getElementById("wsPopupContainer").remove();
    document.body.style.overflow = "";
}

function createWordSetPopup() {
    document.body.style.overflow = "hidden";

    let shadowDiv = document.createElement("div");
    shadowDiv.classList.add("shadow");
    shadowDiv.setAttribute("id", "shadow");

    let wsPopupContainer = document.createElement("div");
    wsPopupContainer.classList.add("ws-popup-container");
    wsPopupContainer.setAttribute("id", "wsPopupContainer");

    let wsPopup = document.createElement("div");
    wsPopup.classList.add("ws-popup");

    let inputName = document.createElement("input");
    inputName.setAttribute("type", "text");
    inputName.setAttribute("placeholder", "Название");
    inputName.classList.add("ws-name-input");

    let inputWrapper = document.createElement("div");
    inputWrapper.classList.add("input__wrapper");

    let fileInput = document.createElement("input");
    fileInput.setAttribute("type", "file");
    fileInput.setAttribute("id", "input__file");
    fileInput.classList.add("input");
    fileInput.classList.add("input__file");

    let fileLabel = document.createElement("label");
    fileLabel.setAttribute("for", "input__file");
    fileLabel.classList.add("input__file-button");

    let uploadSpanImg = document.createElement("span");
    uploadSpanImg.classList.add("input__file-icon-wrapper");

    let uploadImg = document.createElement("img");
    uploadImg.classList.add("input__file-icon");
    uploadImg.setAttribute("src",DOWNLOAD_PIC_PATH);
    uploadImg.setAttribute("alt","Выбрать файл");
    uploadImg.setAttribute("width","35px");

    let uploadSpanTxt = document.createElement("span");
    uploadSpanTxt.classList.add("input__file-button-text");
    uploadSpanTxt.textContent = "Выберите файл";

    let uploadBtn = document.createElement("button");
    uploadBtn.textContent = "Добавить";


    wsPopupContainer.append(wsPopup);
    wsPopup.append(inputName);
    wsPopup.append(inputWrapper);

    inputWrapper.append(fileInput);
    inputWrapper.append(fileLabel);

    fileLabel.append(uploadSpanImg);
    uploadSpanImg.append(uploadImg);
    fileLabel.append(uploadSpanTxt);

    wsPopup.append(uploadBtn);

    document.body.prepend(shadowDiv, wsPopupContainer);
}

function createSliderItem(wordSet, urlCreated) {
    wordSets_idMap.set(wordSet["wordSetId"], wordSet);

    let sliderItem = document.createElement("div");
    sliderItem.classList.add("slider__item");
    if(wordSet["main"] === true)
        sliderItem.classList.add("main-ws");

    let backgroundImgDiv = document.createElement("div");
    if (urlCreated)
        backgroundImgDiv.style.backgroundImage = "url('" + wordSet["image"] + "')";
    else
        backgroundImgDiv.style.backgroundImage = "url('" + URL.createObjectURL(b64toBlob(wordSet["image"])) + "')";

    backgroundImgDiv.setAttribute("wordSetId",wordSet["wordSetId"]);
    backgroundImgDiv.addEventListener("mouseover", ()=> backgroundImgDiv.querySelector(".si-edit-delete").classList.remove("si-edit-delete-hide"));
    backgroundImgDiv.addEventListener("mouseout", ()=> backgroundImgDiv.querySelector(".si-edit-delete").classList.add("si-edit-delete-hide"));
    backgroundImgDiv.addEventListener("click", OnWSSelection);
    backgroundImgDiv.classList.add("ws__item");


    let edit_deleteDiv = document.createElement("div");
    edit_deleteDiv.classList.add("si-edit-delete");
    edit_deleteDiv.classList.add("si-edit-delete-hide");
    if(wordSet["main"] === false) {
        let penImgDiv = document.createElement("div");
        penImgDiv.style.backgroundImage = "url('"+ PENCIL_PIC_PATH +"')";
        penImgDiv.style.backgroundColor = "#D4E3F2";
        penImgDiv.style.borderRadius = "30px";
        penImgDiv.addEventListener("click", editWSListener);

        let deleteImgDiv = document.createElement("div");
        deleteImgDiv.style.backgroundImage = "url('" + DELETE_SIGN_PATH + "')";
        deleteImgDiv.style.backgroundColor = "#D4E3F2";
        deleteImgDiv.style.borderRadius = "30px";
        deleteImgDiv.addEventListener("click", removeWSListener);

        edit_deleteDiv.append(penImgDiv);
        edit_deleteDiv.append(deleteImgDiv);
    }

    let numNameDiv = document.createElement("div");
    numNameDiv.classList.add("si-num-name");

    let numDiv = document.createElement("div");
    numDiv.textContent = wordSet["size"];

    let nameDiv = document.createElement("div");
    nameDiv.textContent = wordSet["name"];


    sliderItem.append(backgroundImgDiv);
    backgroundImgDiv.append(edit_deleteDiv);
    backgroundImgDiv.append(numNameDiv);
    numNameDiv.append(nameDiv);
    numNameDiv.append(numDiv);

    return sliderItem;
}

function toggleSelectedWS(target) {
    target.prepend(shadowWS_onSelection);
}

function b64toBlob(b64Data, contentType, sliceSize) {
    contentType = contentType || '';
    sliceSize = sliceSize || 512;

    let byteCharacters = atob(b64Data);
    let byteArrays = [];

    for (let offset = 0; offset < byteCharacters.length; offset += sliceSize) {
        let slice = byteCharacters.slice(offset, offset + sliceSize);

        let byteNumbers = new Array(slice.length);
        for (let i = 0; i < slice.length; i++) {
            byteNumbers[i] = slice.charCodeAt(i);
        }

        let byteArray = new Uint8Array(byteNumbers);

        byteArrays.push(byteArray);
    }

    return new Blob(byteArrays, {
        type : contentType
    });
}