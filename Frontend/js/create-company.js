const companyForm = document.getElementById("companyForm");

const successMessage = document.getElementById("successMessage");
const errorMessage = document.getElementById("errorMessage");

const createCompanyBtn =
    document.getElementById("createCompanyBtn");

const logoutBtn =
    document.getElementById("logoutBtn");


companyForm.addEventListener("submit", async function (event) {

    event.preventDefault();

    // Hide previous messages

    successMessage.classList.add("d-none");
    errorMessage.classList.add("d-none");


    // Get JWT

    const token = localStorage.getItem("token");

    if (!token) {

        window.location.href = "login.html";

        return;
    }


    // Get form values

    const companyData = {

        name: document.getElementById("name").value.trim(),

        email: document.getElementById("email").value.trim(),

        website: document.getElementById("website").value.trim(),

        description:
            document.getElementById("description").value.trim()

    };


    try {

        // Disable button

        createCompanyBtn.disabled = true;

        createCompanyBtn.textContent = "Creating...";


        const response = await fetch(
            "http://localhost:8080/companies",
            {
                method: "POST",

                headers: {

                    "Content-Type": "application/json",

                    "Authorization": "Bearer " + token

                },

                body: JSON.stringify(companyData)

            }
        );


        // Handle unauthorized

        if (response.status === 401) {

            localStorage.removeItem("token");

            window.location.href = "login.html";

            return;
        }


        // Handle forbidden

        if (response.status === 403) {

            throw new Error(
                "You are not authorized to create a company."
            );
        }


        if (!response.ok) {

            const errorText = await response.text();

            throw new Error(
                errorText || "Failed to create company."
            );
        }


        // Success

        successMessage.textContent =
            "Company created successfully!";

        successMessage.classList.remove("d-none");


        // Redirect to dashboard

        setTimeout(() => {

            window.location.href =
                "Employer/employer-dashboard.html";

        }, 1000);


    } catch (error) {

        console.error("Create company error:", error);

        errorMessage.textContent =
            error.message || "Something went wrong.";

        errorMessage.classList.remove("d-none");


        createCompanyBtn.disabled = false;

        createCompanyBtn.textContent =
            "Create Company";
    }

});


/* Logout */

logoutBtn.addEventListener("click", function () {

    localStorage.removeItem("token");

    window.location.href = "login.html";

});