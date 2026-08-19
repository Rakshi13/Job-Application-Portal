console.log("Employer JS Loaded");
document.getElementById("register-employer").addEventListener("click", async function (e) {

    console.log("Register Button Clicked");
    e.preventDefault();
    clearErrors();
    const request = {
        username: document.getElementById("employer-username").value,
        password: document.getElementById("employer-password").value
    };

    console.log(request);

    const response = await fetch("http://localhost:8080/employer/register", {
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
        const fieldMap = {
            username: "username-error",
            password: "password-error"
        };

        for (const key in data) {
            const elementId = fieldMap[key];

            if (elementId) {
                document.getElementById(elementId).textContent = data[key];
            }
        }
        return;
    }
});


function clearErrors() {
    document.getElementById("password-error").textContent = "";
    document.getElementById("username-error").textContent = "";
}

document.getElementById("employer-username")
    .addEventListener("input", function () {
        document.getElementById("username-error").textContent = "";
    });

document.getElementById("employer-password")
    .addEventListener("input", function () {
        document.getElementById("password-error").textContent = "";
    });