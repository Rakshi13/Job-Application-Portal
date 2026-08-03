console.log("Employer JS Loaded");
document.getElementById("register-employer").addEventListener("click", async function (e) {

    console.log("Register Button Clicked");
    e.preventDefault();
    clearErrors();
    const request = {
        companyName: document.getElementById("employer-companyname").value,
        companyEmail: document.getElementById("employer-companyemail").value,
        companyWebsite: document.getElementById("employer-companywebsite").value,
        HrName: document.getElementById("employer-hrname").value,
        username: document.getElementById("employer-username").value,
        password: document.getElementById("employer-password").value
    };

    console.log(request);

    // const response = await fetch("http://localhost:8080/employer/register", {

    //     method: "POST",

    //     headers: {
    //         "Content-Type": "application/json"
    //     },

    //     body: JSON.stringify(request)

    // });
    // console.log(response);

    // if(response.ok){
    //     alert("Candidate Registered Successfully");
    //     window.location.href="login.html";
    // }else{
    //     alert("Registration Failed");
    // }

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
            companyName: "companyname-error",
            companyEmail: "companyemail-error",
            companyWebsite: "companywebsite-error",
            HrName: "hrname-error",
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
    document.getElementById("companyname-error").textContent = "";
    document.getElementById("companyemail-error").textContent = "";
    document.getElementById("companywebsite-error").textContent = "";
    document.getElementById("password-error").textContent = "";
    document.getElementById("username-error").textContent = "";
    document.getElementById("hrname-error").textContent = "";
}


document.getElementById("employer-companyname")
    .addEventListener("input", function () {
        document.getElementById("companyname-error").textContent = "";
    });

document.getElementById("employer-companywebsite")
    .addEventListener("input", function () {
        document.getElementById("companywebsite-error").textContent = "";
    });

document.getElementById("employer-companyemail")
    .addEventListener("input", function () {
        document.getElementById("companyemail-error").textContent = "";
    });

document.getElementById("employer-hrname")
    .addEventListener("input", function () {
        document.getElementById("hrname-error").textContent = "";
    });

document.getElementById("employer-username")
    .addEventListener("input", function () {
        document.getElementById("username-error").textContent = "";
    });

document.getElementById("employer-password")
    .addEventListener("input", function () {
        document.getElementById("password-error").textContent = "";
    });