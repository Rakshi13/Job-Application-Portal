const username = localStorage.getItem("username");

if (!username) {
    window.location.href = "login.html";
}

document.getElementById("welcomeUser").textContent =
    "Welcome, " + username;

document.getElementById("logoutBtn")
    .addEventListener("click", function () {

        localStorage.removeItem("token");
        localStorage.removeItem("username");
        localStorage.removeItem("role");

        window.location.href = "login.html";
    });