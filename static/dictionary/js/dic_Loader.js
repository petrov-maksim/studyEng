function createLoader() {
    let shadowDiv = document.createElement("div");
    shadowDiv.classList.add("shadow");

    let loader = document.createElement("div");
    loader.classList.add("loader");

    let canvas = document.createElement("canvas");
    canvas.setAttribute("id", "canvas");
    canvas.setAttribute("width", "105px");
    canvas.setAttribute("height", "105px");

    let p = document.createElement("p");
    p.setAttribute("title", "Загрузка...");

    document.body.prepend(shadowDiv, loader);

    loader.append(canvas);
    loader.append(p);

    document.body.style.overflow = "none";


    let cvs = document.getElementById("canvas");
    let ctx = cvs.getContext("2d");
    let sA = (Math.PI / 180) * 45;
    let sE = (Math.PI / 180) * 90;
    let ca = cvs.width;
    let ch = cvs.height;

    setInterval(function(){
        ctx.clearRect(0, 0, ca, ch);
        ctx.lineWidth = 15;

        ctx.beginPath();
        ctx.strokeStyle = "green";
        ctx.shadowColor = "darkgreen";
        ctx.shadowOffsetX = 2;
        ctx.shadowOffsetY = 2;
        ctx.shadowBlur = 5;
        ctx.arc(50, 50, 25, 0, 360, false);
        ctx.stroke();
        ctx.closePath();

        sE += 0.05;
        sA += 0.05;

        ctx.beginPath();
        ctx.strokeStyle = "yellow";
        ctx.arc(50, 50, 25, sA, sE, false);
        ctx.stroke();
        ctx.closePath();

    }, 6);
}

function removeLoader() {
    document.querySelector(".shadow").remove();
    document.querySelector(".loader").remove();
    document.body.style.overflow = "none";
}