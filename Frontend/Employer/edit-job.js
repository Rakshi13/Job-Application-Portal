document.addEventListener("DOMContentLoaded", function () {


    // ============================================
    // Get JWT Token
    // ============================================

    const token = localStorage.getItem("token");


    // Redirect if user is not logged in

    if (!token) {

        window.location.href = "../login.html";

        return;
    }



    // ============================================
    // Get Job ID from URL
    // Example:
    // edit-job.html?id=2
    // ============================================

    const urlParams =
        new URLSearchParams(window.location.search);

    const jobId =
        urlParams.get("id");



    // Check Job ID

    if (!jobId) {

        alert("Job ID is missing.");

        window.location.href =
            "my-jobs.html";

        return;
    }



    console.log("Editing Job ID:", jobId);



    // ============================================
    // DOM Elements
    // ============================================

    const editJobForm =
        document.getElementById("editJobForm");

    const titleInput =
        document.getElementById("title");

    const descriptionInput =
        document.getElementById("description");

    const locationInput =
        document.getElementById("location");

    const minSalaryInput =
        document.getElementById("minSalary");

    const maxSalaryInput =
        document.getElementById("maxSalary");

    const updateJobBtn =
        document.getElementById("updateJobBtn");

    const cancelBtn =
        document.getElementById("cancelBtn");

    const logoutBtn =
        document.getElementById("logoutBtn");

    const errorMessage =
        document.getElementById("errorMessage");

    const successMessage =
        document.getElementById("successMessage");



    // ============================================
    // Load Job Details
    // ============================================

    loadJob();



    async function loadJob() {

        try {

            const response = await fetch(
                `http://localhost:8080/jobs/${jobId}`,
                {
                    method: "GET",

                    headers: {
                        "Authorization":
                            "Bearer " + token,

                        "Content-Type":
                            "application/json"
                    }
                }
            );


            // ====================================
            // Unauthorized
            // ====================================

            if (response.status === 401) {

                localStorage.removeItem("token");
                localStorage.removeItem("username");
                localStorage.removeItem("role");

                window.location.href =
                    "../login.html";

                return;
            }



            // ====================================
            // Forbidden
            // ====================================

            if (response.status === 403) {

                showError(
                    "You don't have permission to view this job."
                );

                return;
            }



            // ====================================
            // Job Not Found
            // ====================================

            if (response.status === 404) {

                showError(
                    "Job not found."
                );

                return;
            }



            // ====================================
            // Other Errors
            // ====================================

            if (!response.ok) {

                throw new Error(
                    "Failed to load job."
                );
            }



            // ====================================
            // Convert Response to JSON
            // ====================================

            const job =
                await response.json();

            console.log(
                "Job Details:",
                job
            );



            // ====================================
            // Populate Form
            // ====================================

            titleInput.value =
                job.title || "";

            descriptionInput.value =
                job.description || "";

            locationInput.value =
                job.location || "";

            minSalaryInput.value =
                job.minSalary ?? "";

            maxSalaryInput.value =
                job.maxSalary ?? "";



        } catch (error) {

            console.error(
                "Error loading job:",
                error
            );

            showError(
                "Unable to load job details."
            );

        }

    }



    // ============================================
    // Update Job
    // ============================================

    editJobForm.addEventListener(
        "submit",
        async function (event) {

            event.preventDefault();


            // Clear previous messages

            hideMessages();



            // ====================================
            // Get Form Values
            // ====================================

            const title =
                titleInput.value.trim();

            const description =
                descriptionInput.value.trim();

            const location =
                locationInput.value.trim();

            const minSalary =
                Number(minSalaryInput.value);

            const maxSalary =
                Number(maxSalaryInput.value);



            // ====================================
            // Frontend Validation
            // ====================================

            if (!title) {

                showError(
                    "Job title cannot be empty."
                );

                return;
            }


            if (!description) {

                showError(
                    "Job description cannot be empty."
                );

                return;
            }


            if (!location) {

                showError(
                    "Job location cannot be empty."
                );

                return;
            }


            if (
                Number.isNaN(minSalary) ||
                Number.isNaN(maxSalary)
            ) {

                showError(
                    "Please enter valid salary values."
                );

                return;
            }


            if (minSalary < 0 || maxSalary < 0) {

                showError(
                    "Salary cannot be negative."
                );

                return;
            }


            if (minSalary > maxSalary) {

                showError(
                    "Minimum salary cannot be greater than maximum salary."
                );

                return;
            }



            // ====================================
            // Request Body
            // ====================================

            const jobData = {

                title: title,

                description: description,

                minSalary: minSalary,

                maxSalary: maxSalary,

                location: location

            };



            console.log(
                "Updating Job:",
                jobData
            );



            // ====================================
            // Disable Button
            // ====================================

            updateJobBtn.disabled = true;

            updateJobBtn.textContent =
                "Updating...";



            try {

                const response = await fetch(
                    `http://localhost:8080/jobs/${jobId}`,
                    {
                        method: "PUT",

                        headers: {

                            "Authorization":
                                "Bearer " + token,

                            "Content-Type":
                                "application/json"
                        },

                        body:
                            JSON.stringify(jobData)
                    }
                );



                // =================================
                // Unauthorized
                // =================================

                if (response.status === 401) {

                    localStorage.removeItem("token");
                    localStorage.removeItem("username");
                    localStorage.removeItem("role");

                    window.location.href =
                        "../login.html";

                    return;
                }



                // =================================
                // Forbidden
                // =================================

                if (response.status === 403) {

                    showError(
                        "You are not authorized to update this job."
                    );

                    return;
                }



                // =================================
                // Validation / Backend Error
                // =================================

                if (!response.ok) {

                    const errorText =
                        await response.text();

                    console.error(
                        "Update Error:",
                        errorText
                    );

                    showError(
                        errorText ||
                        "Failed to update job."
                    );

                    return;
                }



                // =================================
                // Success
                // =================================

                const responseMessage =
                    await response.text();

                console.log(
                    "Update Response:",
                    responseMessage
                );


                showSuccess(
                    "Job updated successfully!"
                );



                // Wait a little so user
                // can see success message

                setTimeout(function () {

                    window.location.href =
                        "my-jobs.html";

                }, 1000);



            } catch (error) {

                console.error(
                    "Error updating job:",
                    error
                );

                showError(
                    "Unable to update job. Please try again."
                );

            } finally {

                updateJobBtn.disabled = false;

                updateJobBtn.textContent =
                    "Update Job";

            }

        }
    );



    // ============================================
    // Cancel Button
    // ============================================

    cancelBtn.addEventListener(
        "click",
        function () {

            window.location.href =
                "my-jobs.html";

        }
    );



    // ============================================
    // Logout
    // ============================================

    logoutBtn.addEventListener(
        "click",
        function () {

            localStorage.removeItem("token");
            localStorage.removeItem("username");
            localStorage.removeItem("role");

            window.location.href =
                "../login.html";

        }
    );



    // ============================================
    // Show Error
    // ============================================

    function showError(message) {

        errorMessage.textContent =
            message;

        errorMessage.classList.remove(
            "d-none"
        );

        successMessage.classList.add(
            "d-none"
        );

        window.scrollTo({
            top: 0,
            behavior: "smooth"
        });
    }



    // ============================================
    // Show Success
    // ============================================

    function showSuccess(message) {

        successMessage.textContent =
            message;

        successMessage.classList.remove(
            "d-none"
        );

        errorMessage.classList.add(
            "d-none"
        );

    }



    // ============================================
    // Hide Messages
    // ============================================

    function hideMessages() {

        errorMessage.classList.add(
            "d-none"
        );

        successMessage.classList.add(
            "d-none"
        );

    }

});