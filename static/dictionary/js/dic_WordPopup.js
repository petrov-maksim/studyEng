function CreateWordPopup(event){
    document.body.style.overflow = "hidden";

    let rowTarget = event.currentTarget.closest(".words-table-row");

    let targetWord = word_idMap.get(+rowTarget.getAttribute("word-id"));
    let word = targetWord["word"];
    let wordId = +targetWord["id"];
    let translations = targetWord["translations"];
    let example = targetWord["example"];


    let shadowDiv = document.createElement("div");
    shadowDiv.classList.add("shadow");
    document.body.append(shadowDiv);


    let popupContainer = document.createElement("div");
    popupContainer.classList.add("popup-container");


    let popup = document.createElement("div");
    popup.classList.add("popup");
    popupContainer.append(popup);

    let popupWord = document.createElement("div");
    popupWord.classList.add("popup-word");
    let wordSpan = document.createElement("span");
    wordSpan.innerText = word;

    popup.append(popupWord);
    popupWord.append(wordSpan);


    let translationWrapper = document.createElement("div");
    translationWrapper.classList.add("popup-translation-wrapper");
    popup.append(translationWrapper);

    let addTranslationDiv = createAddTranslationDiv();

    for (let i = 0; i < translations.length; i++){
        let translation = translations[i];

        let translationDiv = document.createElement("div");
        translationDiv.classList.add("popup-translation");

        let translationSpan = document.createElement("span");
        translationSpan.textContent = translation;

        let deleteTranslationSign = document.createElement("img");
        deleteTranslationSign.setAttribute("height", "25px");
        deleteTranslationSign.setAttribute("width", "25px");
        deleteTranslationSign.setAttribute("src", DELETE_SIGN_PATH);

        translationSpan.append(deleteTranslationSign);
        translationWrapper.append(translationDiv);
        translationDiv.append(translationSpan);

        deleteTranslationSign.addEventListener("click", function (event) {
            let translation = event.currentTarget.previousSibling.textContent;
            fetch(ROOT_URL + "dictionary/removeTranslation",{
                method:"POST",
                body: JSON.stringify({"id":wordId, "translations":[translation]})
            });

            translationDiv.remove();
            translations.splice(translations.indexOf(translation), 1);

            if (!document.getElementById("addTranslationDiv"))
                translationWrapper.append(addTranslationDiv);
        });
    }

    if (translations.length < 4)
        translationWrapper.append(addTranslationDiv);


    let exampleDivWrapper = document.createElement("div");
    exampleDivWrapper.classList.add("popup-example-wrapper");

    if (example === null || example === "")
        exampleDivWrapper.append(createAddExampleDiv());
    else
        exampleDivWrapper.append(createExampleDiv());

    function createAddTranslationDiv() {
        let addTranslationDiv = document.createElement("div");
        addTranslationDiv.classList.add("popup-translation");
        addTranslationDiv.classList.add("popup-add-translation-wrapper");
        addTranslationDiv.setAttribute("id", "addTranslationDiv");

        let addTranslationSpan = document.createElement("span");
        addTranslationSpan.textContent = "Добавить преревод";

        addTranslationDiv.append(addTranslationSpan);

        addTranslationSpan.addEventListener("click", function () {
            let inputTranslationDiv = document.createElement("div");
            inputTranslationDiv.classList.add("popup-translation-input");

            let translationInput = document.createElement("input");
            translationInput.setAttribute("type", "text");
            translationInput.setAttribute("placeholder", "Перевод");

            let addTranslationBtn = document.createElement("button");
            addTranslationBtn.textContent = "Добавить";

            addTranslationDiv.replaceWith(inputTranslationDiv);
            inputTranslationDiv.append(translationInput);
            inputTranslationDiv.append(addTranslationBtn);

            addTranslationBtn.addEventListener("click", function () {
                let newTranslation = translationInput.value.trim();
                translationInput.value = "";
                if (newTranslation === ""){
                    inputTranslationDiv.replaceWith(addTranslationDiv);
                    return;
                }else
                    inputTranslationDiv.remove();

                fetch(ROOT_URL + "dictionary/addTranslation",{
                    method:"POST",
                    body: JSON.stringify({"id":wordId, "translations":[newTranslation]})
                });


                translations.push(newTranslation);

                let translationDiv = document.createElement("div");
                translationDiv.classList.add("popup-translation");

                let translationSpan = document.createElement("span");
                translationSpan.textContent = newTranslation;

                let deleteTranslationSign = document.createElement("img");
                deleteTranslationSign.setAttribute("height", "25px");
                deleteTranslationSign.setAttribute("width", "25px");
                deleteTranslationSign.setAttribute("src", DELETE_SIGN_PATH);

                translationSpan.append(deleteTranslationSign);
                translationDiv.append(translationSpan);
                translationWrapper.append(translationDiv);

                deleteTranslationSign.addEventListener("click", function (event) {
                    fetch(ROOT_URL + "dictionary/removeTranslation",{
                        method:"POST",
                        body: JSON.stringify({"id":wordId, "translations":[newTranslation]})
                    });

                    translationDiv.remove();
                    translations.splice(translations.indexOf(event.currentTarget.previousSibling.textContent), 1);

                    if (!document.getElementById("addTranslationDiv"))
                        translationWrapper.append(addTranslationDiv);
                });

                if (translations.length < 4)
                    translationWrapper.append(addTranslationDiv);
            })
        });
        return addTranslationDiv;
    }

    /**------------------------------------------------Example Part---------------------------------------------*/
    popup.append(exampleDivWrapper);


    function createAddExampleDiv() {
        let addExampleDiv = document.createElement("div");
        addExampleDiv.classList.add("popup-example");

        let addExampleSpan = document.createElement("span");
        addExampleSpan.textContent = "Добавить пример";
        addExampleDiv.append(addExampleSpan);

        addExampleSpan.addEventListener("click", function () {
            let inputExampleDiv = document.createElement("div");
            inputExampleDiv.classList.add("popup-translation-input");

            let exampleInput = document.createElement("input");
            exampleInput.setAttribute("type", "text");
            exampleInput.setAttribute("placeholder", "Перевод");
            exampleInput.focus();

            let addExampleBtn = document.createElement("button");
            addExampleBtn.textContent = "Добавить";

            addExampleDiv.replaceWith(inputExampleDiv);
            inputExampleDiv.append(exampleInput);
            inputExampleDiv.append(addExampleBtn);

            addExampleBtn.addEventListener("click", function () {
                let newExample = exampleInput.value;
                exampleInput.value = "";
                if (newExample === ""){
                    inputExampleDiv.replaceWith(addExampleDiv);
                    return;
                }else
                    inputExampleDiv.remove();
                targetWord["example"] = newExample;

                fetch(ROOT_URL + "dictionary/addExample",{
                    method:"POST",
                    body: JSON.stringify({"id":wordId, "example":newExample})
                });


                let exampleDiv = document.createElement("div");
                exampleDiv.classList.add("popup-example");

                let exampleSpan = document.createElement("span");
                exampleSpan.innerText = newExample;

                let deleteExampleSign = document.createElement("img");
                deleteExampleSign.setAttribute("height", "25px");
                deleteExampleSign.setAttribute("width", "25px");
                deleteExampleSign.setAttribute("src", DELETE_SIGN_PATH);

                exampleSpan.append(deleteExampleSign);
                exampleDiv.append(exampleSpan);
                exampleDivWrapper.append(exampleDiv);

                deleteExampleSign.addEventListener("click", function () {
                    fetch(ROOT_URL + "dictionary/addExample",{
                        method:"POST",
                        body: JSON.stringify({"id":wordId, "example":""})
                    });
                    targetWord["example"] = "";

                    exampleDiv.remove();
                    exampleDivWrapper.append(addExampleDiv);
                });
            })
        });

        return addExampleDiv;
    }

    function createExampleDiv() {
        let exampleDiv = document.createElement("div");
        exampleDiv.classList.add("popup-example");

        let exampleSpan = document.createElement("span");
        exampleSpan.textContent = example;
        exampleSpan.classList.add("example-span");

        let deleteExampleSign = document.createElement("img");
        deleteExampleSign.setAttribute("height", "25px");
        deleteExampleSign.setAttribute("width", "25px");
        deleteExampleSign.setAttribute("src", DELETE_SIGN_PATH);

        exampleSpan.append(deleteExampleSign);
        exampleDiv.append(exampleSpan);
        exampleDivWrapper.append(exampleDiv);

        deleteExampleSign.addEventListener("click", function () {
            fetch(ROOT_URL + "dictionary/addExample",{
                method:"POST",
                body: JSON.stringify({"id":wordId, "example":""})
            });

            targetWord["example"] = "";

            exampleDiv.remove();
            exampleDivWrapper.append(createAddExampleDiv());
        });
        return exampleDiv;
    }


    document.body.prepend(shadowDiv, popupContainer);

    popupContainer.addEventListener("click", function(){
        document.body.firstElementChild.remove();
        document.body.firstElementChild.remove();
        document.body.style.overflow = "";

        rowTarget.children[3].firstElementChild.innerText = targetWord["translations"].join(", ");
    });

    popup.addEventListener("click", (event) => event.stopPropagation());
}