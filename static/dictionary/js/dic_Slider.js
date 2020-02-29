let sliderMainElement;
let sliderWrapper;
let sliderItems;
let sliderControls;
let sliderControlLeft;
let sliderControlRight;
let sliderWrapperWidth;
let sliderItemWidth;
let positionLeftItem;
let transform;
let step;
let items;
let position;


function initSlider() {
    sliderMainElement = document.querySelector('.slider');// основный элемент блока
    sliderWrapper = sliderMainElement.querySelector('.slider__wrapper'); // обертка для .slider-item
    sliderItems = sliderMainElement.querySelectorAll('.slider__item'); // элементы (.slider-item)
    sliderControls = sliderMainElement.querySelectorAll('.slider__control'); // элементы управления
    sliderControlLeft = sliderMainElement.querySelector('.slider__control_left'); // кнопка "LEFT"
    sliderControlRight = sliderMainElement.querySelector('.slider__control_right'); // кнопка "RIGHT"
    sliderWrapperWidth = parseFloat(getComputedStyle(sliderWrapper).width); // ширина обёртки
    sliderItemWidth = parseFloat(getComputedStyle(sliderItems[0]).width); // ширина одного элемента
    positionLeftItem = 0; // позиция левого активного элемента
    transform = 0; // значение транфсофрмации .slider_wrapper
    step = sliderItemWidth / sliderWrapperWidth * 100; // величина шага (для трансформации)
    items = []; // массив элементов

    sliderItems.forEach((item, index) => items.push({ item: item, position: index, transform: 0 }));

    position = {
        getMin: 0,
        getMax: items.length - 1,
    };

    setSliderListeners();
}

// обработчик события click для кнопок "назад" и "вперед"
function onControlClick(event) {
    if (event.target.classList.contains('slider__control')) {
        event.preventDefault();
        let direction = event.target.classList.contains('slider__control_right') ? 'right' : 'left';
        transformSliderItem(direction);
    }
}

function setSliderListeners() {
    sliderControls.forEach(control => control.addEventListener("click", onControlClick));
}

function transformSliderItem(direction) {
    if (direction === "right") {
        if ((positionLeftItem + sliderWrapperWidth / sliderItemWidth - 1) >= position.getMax) {
            return;
        }
        if (!sliderControlLeft.classList.contains("slider__control_show"))
            sliderControlLeft.classList.add("slider__control_show");

        if (sliderControlRight.classList.contains("slider__control_show") && (positionLeftItem + sliderWrapperWidth / sliderItemWidth) >= position.getMax)
            sliderControlRight.classList.remove("slider__control_show");

        positionLeftItem++;
        transform -= step;
    }
    else if (direction === "left") {
        if (positionLeftItem <= position.getMin) {
            return;
        }
        if (!sliderControlRight.classList.contains("slider__control_show"))
            sliderControlRight.classList.add("slider__control_show");

        if (sliderControlLeft.classList.contains("slider__control_show") && positionLeftItem - 1 <= position.getMin)
            sliderControlLeft.classList.remove("slider__control_show");

        positionLeftItem--;
        transform += step;
    }
    sliderWrapper.style.transform = "translateX(" + transform + "%)";
}

function restoreSlider() {
    sliderWrapper.style.transform = 'translateX(' + 0 + '%)';
    sliderControlRight.classList.add("slider__control_show");
    sliderControlLeft.classList.remove("slider__control_show");
    initSlider();
}