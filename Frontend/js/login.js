
document.getElementById('login-user').addEventListener("click", async function (e) {

    e.preventDefault();

    clearLoginData();

    const request = {
        username: document.getElementById('login-username').value,
        password: document.getElementById('login-password').value
    };

    const loginResponse = await fetch("https://job-application-portal-ke3u.onrender.com/login", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(request)
    });

    const data = await loginResponse.json();

    if (loginResponse.ok) {
        localStorage.setItem("token", data.token);
        localStorage.setItem("username", data.username);
        localStorage.setItem("role", data.role);

        if (data.role === "ROLE_CANDIDATE") {
            window.location.href = "candidate/candidate-dashboard.html";
        } else if (data.role === "ROLE_EMPLOYER") {
            window.location.href = "Employer/employer-dashboard.html";
        }else{
            window.location.href = "admin/dashboard.html";
        }
    } else {
        const fieldMap = {
            message: "submit-error"
        };

        for (const key in data) {
            const elementId = fieldMap[key];

            if (elementId) {
                document.getElementById(elementId).textContent = data[key];
            }
        }
        return;
    }
})

function clearLoginData() {
    document.getElementById("submit-error").textContent = "";
}
