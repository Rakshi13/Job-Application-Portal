const username = localStorage.getItem("username");

if (!username) {
    window.location.href = "../login.html";
    return;
}

document.getElementById("welcomeUser").textContent =
    "Welcome, " + username;

document.getElementById("logout-btn")
    .addEventListener("click", function () {

        localStorage.removeItem("token");
        localStorage.removeItem("username");
        localStorage.removeItem("role");

        window.location.href = "../login.html";
    });