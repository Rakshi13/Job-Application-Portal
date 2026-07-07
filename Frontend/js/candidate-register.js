console.log("Candidate JS Loaded");
document.getElementById("register-candidate").addEventListener("click", async function (e) {

    console.log("Register Button Clicked");
    e.preventDefault();
    debugger
    const request = {
        username: document.getElementById("candidate-username").value,
        password: document.getElementById("candidate-password").value,

        // Hardcoded because this page is for candidates
        role: "ROLE_CANDIDATE"
    };
    console.log(request);

    const response = await fetch("http://localhost:8080/register", {

        method: "POST",

        headers: {
            "Content-Type": "application/json"
        },

        body: JSON.stringify(request)

    });
    console.log(response);

    if(response.ok){
        alert("Candidate Registered Successfully");
        window.location.href="login.html";
    }else{
        alert("Registration Failed");
    }

});