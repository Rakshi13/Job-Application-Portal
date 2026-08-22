document.addEventListener("DOMContentLoaded", function () {

    // Get JWT token
    const token = localStorage.getItem("token");


    // User is not logged in
    if (!token) {

        window.location.href = "../login.html";

        return;
    }


    const postJobForm =
        document.getElementById("postJobForm");

    const errorMessage =
        document.getElementById("errorMessage");

    const postJobBtn =
        document.getElementById("postJobBtn");


    postJobForm.addEventListener("submit", async function (event) {

        // Prevent page refresh
        event.preventDefault();


        // Get values from form
        const title =
            document.getElementById("title").value.trim();

        const description =
            document.getElementById("description").value.trim();

        const location =
            document.getElementById("location").value.trim();

        const minSalary =
            Number(document.getElementById("minSalary").value);

        const maxSalary =
            Number(document.getElementById("maxSalary").value);


        // Clear previous error
        errorMessage.classList.add("d-none");


        // Frontend salary validation
        if (minSalary > maxSalary) {

            showError(
                "Minimum salary cannot be greater than maximum salary."
            );

            return;
        }


        // Create request body
        const jobData = {

            title: title,

            description: description,

            maxSalary: maxSalary,

            minSalary: minSalary,

            location: location

        };


        console.log("POST Job Request:", jobData);


        try {

            // Disable button while API is running
            postJobBtn.disabled = true;

            postJobBtn.textContent = "Posting...";


            const response = await fetch(
                "http://localhost:8080/jobs",
                {
                    method: "POST",

                    headers: {

                        "Authorization": "Bearer " + token,

                        "Content-Type": "application/json"

                    },

                    body: JSON.stringify(jobData)

                }
            );


            // JWT expired / invalid
            if (response.status === 401) {

                localStorage.removeItem("token");
                localStorage.removeItem("username");
                localStorage.removeItem("role");

                window.location.href = "../login.html";

                return;
            }


            // Employer doesn't have permission
            if (response.status === 403) {

                showError(
                    "You don't have permission to post a job."
                );

                return;
            }


            // Validation / server error
            if (!response.ok) {

                let errorMessageFromServer =
                    "Failed to post job.";

                try {

                    const errorData =
                        await response.json();

                    console.error(
                        "Backend error:",
                        errorData
                    );

                    if (errorData.message) {

                        errorMessageFromServer =
                            errorData.message;

                    }

                } catch (error) {

                    console.error(
                        "Unable to parse error response",
                        error
                    );

                }


                showError(errorMessageFromServer);

                return;
            }


            // Success
            const data = await response.json();


            console.log(
                "Job created successfully:",
                data
            );


            alert("Job posted successfully!");


            // Navigate to Employer Dashboard
            window.location.href =
                "employer-dashboard.html";


        } catch (error) {

            console.error(
                "Error posting job:",
                error
            );


            showError(
                "Unable to connect to the server. Please try again."
            );

        } finally {

            // Enable button again
            postJobBtn.disabled = false;

            postJobBtn.textContent = "Post Job";

        }

    });



    function showError(message) {

        errorMessage.textContent = message;

        errorMessage.classList.remove("d-none");

    }



    // Logout
    document
        .getElementById("logoutBtn")
        .addEventListener("click", function () {

            localStorage.removeItem("token");
            localStorage.removeItem("username");
            localStorage.removeItem("role");

            window.location.href =
                "../login.html";

        });

});