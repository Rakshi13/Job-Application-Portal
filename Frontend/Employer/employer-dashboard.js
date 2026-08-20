// on load of the page
document.addEventListener("DOMContentLoaded", function () {

    // Get JWT token
    const token = localStorage.getItem("token");
    debugger
    // Redirect if user is not logged in
    if (!token) {
        window.location.href = "../login.html";
        return;
    }

    // Show logged-in username
    // const username = localStorage.getItem("username");

    // document.getElementById("welcomeUser").textContent =
    //     "Welcome, " + username;


    // Temporary Dashboard Statistics
    document.getElementById("totalJobs").textContent = "0";
    document.getElementById("totalApplications").textContent = "0";
    document.getElementById("activeJobs").textContent = "0";

    debugger
    // Call GET /employer/dashboard
    loadEmployerDashboard();


    async function loadEmployerDashboard() {

        try {

            const response = await fetch(
                "http://localhost:8080/employer/dashboard",
                {
                    method: "GET",
                    headers: {
                        "Authorization": "Bearer " + token,
                        "Content-Type": "application/json"
                    }
                }
            );
            debugger


            // JWT invalid / expired
            if (response.status === 401) {

                localStorage.removeItem("token");
                localStorage.removeItem("username");
                localStorage.removeItem("role");

                window.location.href = "../login.html";

                return;
            }


            // User doesn't have permission
            if (response.status === 403) {

                alert("You don't have permission to access this page.");

                return;
            }


            if (!response.ok) {

                throw new Error(
                    "Failed to load employer dashboard"
                );
            }


            const data = await response.json();

            console.log("Dashboard response:", data);

            displayDashboard(data);


        } catch (error) {

            console.error(
                "Error loading employer dashboard:",
                error
            );
        }
    }


    function displayDashboard(data) {

        console.log("Username:", data.username);
        console.log("Has company:", data.hasCompany);
        console.log("Company:", data.company);

        const createCompanySection = document.getElementById("createCompanySection");

        const employerActionsSection = document.getElementById("employerActionsSection");

        const viewSection = document.getElementById("viewSection");


        if (data.hasCompany === false) {

            // Employer doesn't have a company
            createCompanySection.style.display = "block";
            employerActionsSection.style.display = "none";
            viewSection.style.display = "none";

        } else {

            // Employer already has a company
            createCompanySection.style.display = "none";
            employerActionsSection.style.display = "flex";
            viewSection.style.display = "flex";
        }
    }


    // Logout
    document.getElementById("logoutBtn")
        .addEventListener("click", function () {

            localStorage.removeItem("token");
            localStorage.removeItem("username");
            localStorage.removeItem("role");

            window.location.href = "../login.html";
        });


    const createCompanyBtn = document.getElementById("createCompanyBtn");

    createCompanyBtn.addEventListener("click", function () {
        debugger
        window.location.href = "../create-company.html";

    });

});