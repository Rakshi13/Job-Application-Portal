
document.addEventListener("DOMContentLoaded", function () {

    // Get JWT token
    const token = localStorage.getItem("token");

    // Redirect if user is not logged in
    if (!token) {
        window.location.href = "../login.html";
        return;
    }

    // Get HTML elements
    const createCompanySection =
        document.getElementById("createCompanySection");

    const employerActionsSection =
        document.getElementById("employerActionsSection");

    const logoutBtn =
        document.getElementById("logoutBtn");

    // Check required HTML elements
    if (!createCompanySection || !employerActionsSection || !logoutBtn) {
        console.error("One or more dashboard HTML elements are missing.");
        return;
    }

    // Load employer dashboard
    loadEmployerDashboard();

    async function loadEmployerDashboard() {
        try {
            const response = await fetch(
                "https://job-application-portal-ke3u.onrender.com/employer/dashboard",
                {
                    method: "GET",
                    headers: {
                        "Authorization": "Bearer " + token,
                        "Accept": "application/json"
                    }
                }
            );

            // JWT invalid or expired
            if (response.status === 401) {
                logout();
                return;
            }

            // User doesn't have permission
            if (response.status === 403) {
                alert("You don't have permission to access this page.");
                return;
            }

            if (!response.ok) {
                throw new Error("Failed to load employer dashboard");
            }

            const data = await response.json();

            displayDashboard(data);

        } catch (error) {
            console.error("Error loading employer dashboard:", error);
            alert("Unable to load the employer dashboard. Please try again.");
        }
    }

    function displayDashboard(data) {

        if (typeof data.hasCompany !== "boolean") {
            console.error("Invalid hasCompany value");
            return;
        }

        const createCompanySection =
            document.getElementById("createCompanySection");

        const employerActionsSection =
            document.getElementById("employerActionsSection");

        if (data.hasCompany === false) {

            // Employer hasn't created a company
            createCompanySection.style.display = "block";
            employerActionsSection.style.display = "none";

        } else {

            // Employer has created a company
            createCompanySection.style.display = "none";
            employerActionsSection.style.display = "flex";
        }
    }

    // Logout function
    function logout() {
        localStorage.removeItem("token");
        localStorage.removeItem("username");
        localStorage.removeItem("role");

        window.location.href = "../login.html";
    }

    // Logout button
    logoutBtn.addEventListener("click", logout);

});
