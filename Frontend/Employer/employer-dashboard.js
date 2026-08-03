// Redirect if user is not logged in

const token = localStorage.getItem("token");

if (!token) {

    window.location.href = "login.html";

}

// Show logged-in username

const username = localStorage.getItem("username");

document.getElementById("welcomeUser").textContent =
    "Welcome, " + username;

// Logout

document.getElementById("logoutBtn")
    .addEventListener("click", function () {

        localStorage.removeItem("token");
        localStorage.removeItem("username");
        localStorage.removeItem("role");

        window.location.href = "login.html";

    });


// Temporary Dashboard Statistics

document.getElementById("totalJobs").textContent = "0";
document.getElementById("totalApplications").textContent = "0";
document.getElementById("activeJobs").textContent = "0";