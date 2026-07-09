console.log("Candidate JS Loaded");

document.getElementById("register-candidate")
.addEventListener("click", async function (e) {

    e.preventDefault();

    const request = {
        username: document.getElementById("candidate-username").value,
        password: document.getElementById("candidate-password").value,
        firstName: document.getElementById("candidate-firstname").value,
        lastName: document.getElementById("candidate-lastname").value,
        email: document.getElementById("candidate-email").value,
        mobile: document.getElementById("candidate-mobile").value
    };

    const response = await fetch("http://localhost:8080/candidate/register", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(request)
    });
    
    const data = await response.json();
    
    console.log(data);
    
    if (response.ok) {
    
        alert(data.message);
    
        setTimeout(() => {
            window.location.href = "login.html";
        }, 1000);
    
    } else {
    
        alert(data.message);
    
    }

});