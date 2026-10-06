

const employerForm = document.getElementById("employerForm");

employerForm.addEventListener("submit", async function (e) {

    e.preventDefault();

    clearErrors();

    const username =
        document.getElementById("employer-username").value;

    const password =
        document.getElementById("employer-password").value;

    const confirmPassword =
        document.getElementById("employer-confirm-password").value;

    // Step 1: Validate Confirm Password
    if (password !== confirmPassword) {

        document.getElementById("confirm-password-error")
            .textContent = "Passwords do not match.";

        return;
    }

    // Step 2: Prepare request
    const request = {
        username: username,
        password: password
    };

    console.log("Registration request:", {
        username: username
    });

    try {

        // Step 3: Call registration API
        const response = await fetch(
            "https://job-application-portal-ke3u.onrender.com/employer/register",
            {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(request)
            }
        );

        const data = await response.json();

        // Step 4: Handle successful registration
        if (response.ok) {

            alert(data.message);

            window.location.href = "login.html";

        } else {

            // Step 5: Display backend validation errors
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

        console.error("Registration error:", error);

        alert("Unable to register. Please try again.");
    }
});


// Clear all validation errors
function clearErrors() {

    document.getElementById("username-error").textContent = "";
    document.getElementById("password-error").textContent = "";
    document.getElementById("confirm-password-error").textContent = "";
}


// Clear username error when typing
document.getElementById("employer-username")
    .addEventListener("input", function () {

        document.getElementById("username-error").textContent = "";
    });


// Clear password error when typing
document.getElementById("employer-password")
    .addEventListener("input", function () {

        document.getElementById("password-error").textContent = "";
    });


// Clear confirm password error when typing
document.getElementById("employer-confirm-password")
    .addEventListener("input", function () {

        document.getElementById("confirm-password-error").textContent = "";
    });
