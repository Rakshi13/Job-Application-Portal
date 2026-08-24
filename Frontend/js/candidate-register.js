//calling register for candidate api.
document.getElementById("register-candidate")
    .addEventListener("click", async function (e) {
        e.preventDefault();
        clearErrors();

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

            const fieldMap = {
                firstName: "firstname-error",
                lastName: "lastname-error",
                username: "username-error",
                password: "password-error",
                email: "email-error",
                mobile: "mobile-error"
            };

            for (const key in data) {
                const elementId = fieldMap[key];

                if (elementId) {
                    document.getElementById(elementId).textContent = data[key];
                }
            }

            return;

            //alert(data.message);

        }

    });


function clearErrors() {

    document.getElementById("firstname-error").textContent = "";
    document.getElementById("lastname-error").textContent = "";
    document.getElementById("username-error").textContent = "";
    document.getElementById("password-error").textContent = "";
    document.getElementById("email-error").textContent = "";
    document.getElementById("mobile-error").textContent = "";

}


document.getElementById("candidate-firstname")
    .addEventListener("input", function () {
        document.getElementById("firstname-error").textContent = "";
    });

document.getElementById("candidate-lastname")
    .addEventListener("input", function () {
        document.getElementById("lastname-error").textContent = "";
    });

document.getElementById("candidate-email")
    .addEventListener("input", function () {
        document.getElementById("email-error").textContent = "";
    });

document.getElementById("candidate-mobile")
    .addEventListener("input", function () {
        document.getElementById("mobile-error").textContent = "";
    });

document.getElementById("candidate-username")
    .addEventListener("input", function () {
        document.getElementById("username-error").textContent = "";
    });

document.getElementById("candidate-password")
    .addEventListener("input", function () {
        document.getElementById("password-error").textContent = "";
    });
