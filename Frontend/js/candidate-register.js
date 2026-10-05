console.log("Candidate JS Loaded");

const candidateForm =
    document.getElementById("candidateForm");


candidateForm.addEventListener("submit", async function (e) {

    e.preventDefault();

    clearErrors();

    // Get form values
    const username =
        document.getElementById("candidate-username").value;

    const password =
        document.getElementById("candidate-password").value;

    const confirmPassword =
        document.getElementById("candidate-confirm-password").value;


    // ============================================
    // Confirm Password Validation
    // ============================================

    if (password !== confirmPassword) {

        document.getElementById("confirm-password-error")
            .textContent = "Passwords do not match.";

        return;
    }


    // ============================================
    // Create Request
    // ============================================

    const request = {
        username: username,
        password: password
    };


    console.log("Registration request:", {
        username: username
    });


    try {

        // ============================================
        // Call Candidate Registration API
        // ============================================

        const response = await fetch(
            "https://job-application-portal-ke3u.onrender.com/candidate/register",
            {
                method: "POST",

                headers: {
                    "Content-Type": "application/json"
                },

                body: JSON.stringify(request)
            }
        );


        const data = await response.json();

        console.log("Registration response:", data);


        // ============================================
        // Registration Successful
        // ============================================

        if (response.ok) {

            alert(data.message);

            window.location.href = "login.html";

        }


        // ============================================
        // Backend Validation Errors
        // ============================================

        else {

            const fieldMap = {
                username: "username-error",
                password: "password-error"
            };


            for (const key in data) {

                const elementId = fieldMap[key];

                if (elementId) {

                    document.getElementById(elementId)
                        .textContent = data[key];

                }
            }

        }

    } catch (error) {

        console.error(
            "Candidate registration error:",
            error
        );

        alert(
            "Unable to register. Please try again."
        );
    }

});


// ============================================
// Clear Errors
// ============================================

function clearErrors() {

    document.getElementById("username-error")
        .textContent = "";

    document.getElementById("password-error")
        .textContent = "";

    document.getElementById("confirm-password-error")
        .textContent = "";
}


// ============================================
// Username Error Clear
// ============================================

document.getElementById("candidate-username")
    .addEventListener("input", function () {

        document.getElementById("username-error")
            .textContent = "";
    });


// ============================================
// Password Error Clear
// ============================================

document.getElementById("candidate-password")
    .addEventListener("input", function () {

        document.getElementById("password-error")
            .textContent = "";

        // Also clear confirm password error
        document.getElementById("confirm-password-error")
            .textContent = "";
    });


// ============================================
// Confirm Password Error Clear
// ============================================

document.getElementById("candidate-confirm-password")
    .addEventListener("input", function () {

        document.getElementById("confirm-password-error")
            .textContent = "";
    });