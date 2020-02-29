async function asyncSignIn() {
    const TIMEOUT = 50;
    const URL = "http://localhost:8080/signIn";
    let mail = document.getElementById("mail").value.trim();
    let pass = document.getElementById("pass").value.trim();

    if (!isValid(mail, pass)){
        alert("Invalid data");
        return ;
    }

    let response = await fetch(URL,{
        method: 'POST',
        headers:{
            'mail': mail,
            'password': pass
        }
    });

    if (response.headers.get("authorized") === "true")
        window.location.href = "http://localhost:8080/dictionary/dictionary.html";

    if (!response.ok){
        alert("Invalid data");
        window.location.reload();
    }

    do{
        response = await fetch(URL,{
            method: 'POST',
            headers:{
                'handling':'true'
            }
        });

        if(response.ok && response.headers.get("ready") === "false")
            await new Promise(resolve => setTimeout(resolve, TIMEOUT));
    }while(response.headers.get("ready") === "false");

    if (response.status === 400){
        alert("Invalid data");
        window.location.reload();
    }

    if (response.ok)
        window.location.href = "http://localhost:8080/dictionary/dictionary.html";
}

function isValid(mail, pass) {
    if (pass.length === 0 || pass.length > 30)
        return false;

    if(mail.length === 0 || mail.length > 64)
        return false;

    let re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return re.test(String(mail).toLowerCase());
}