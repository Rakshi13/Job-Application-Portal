document.addEventListener("DOMContentLoaded", function () {


    // ============================================
    // Get JWT Token
    // ============================================

    const token =
        localStorage.getItem("token");


    // Redirect if user is not logged in

    if (!token) {

        window.location.href =
            "../login.html";

        return;
    }



    // ============================================
    // DOM Elements
    // ============================================

    const jobsTableBody =
        document.getElementById("jobsTableBody");

    const noJobsMessage =
        document.getElementById("noJobsMessage");

    const errorMessage =
        document.getElementById("errorMessage");

    const successMessage =
        document.getElementById("successMessage");

    const logoutBtn =
        document.getElementById("logoutBtn");



    // ============================================
    // Load Jobs
    // ============================================

    loadJobs();



    async function loadJobs() {

        try {

            const response = await fetch(
                "http://localhost:8080/jobs",
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
                    "You don't have permission to view jobs."
                );

                return;
            }


            // ====================================
            // Other Errors
            // ====================================

            if (!response.ok) {

                throw new Error(
                    "Failed to load jobs."
                );
            }


            // ====================================
            // Convert Response
            // ====================================

            const jobs =
                await response.json();

            console.log(
                "Jobs:",
                jobs
            );


            displayJobs(jobs);


        } catch (error) {

            console.error(
                "Error loading jobs:",
                error
            );

            showError(
                "Unable to load jobs."
            );

        }

    }



    // ============================================
    // Display Jobs
    // ============================================

    function displayJobs(jobs) {

        jobsTableBody.innerHTML = "";


        // No jobs

        if (!jobs || jobs.length === 0) {

            noJobsMessage.classList.remove(
                "d-none"
            );

            return;
        }


        noJobsMessage.classList.add(
            "d-none"
        );


        jobs.forEach(function (job) {


            const row =
                document.createElement("tr");


            // IMPORTANT:
            // Store Job ID on the row

            row.setAttribute(
                "data-job-id",
                job.id
            );


            row.innerHTML = `

                <td>
                    ${job.id}
                </td>

                <td>
                    <strong>
                        ${job.title}
                    </strong>
                </td>

                <td>
                    ${job.location}
                </td>

                <td>
                    ₹${job.minSalary}
                </td>

                <td>
                    ₹${job.maxSalary}
                </td>

                <td>

                    <button
                        class="btn btn-primary btn-sm me-2 edit-btn"
                        data-id="${job.id}">

                        Edit

                    </button>


                    <button
                        class="btn btn-danger btn-sm delete-btn"
                        data-id="${job.id}">

                        Delete

                    </button>

                </td>

            `;


            jobsTableBody.appendChild(row);

        });

    }



    // ============================================
    // Handle Edit / Delete Buttons
    // ============================================

    jobsTableBody.addEventListener(
        "click",
        function (event) {


            // ====================================
            // Edit
            // ====================================

            if (
                event.target.classList.contains(
                    "edit-btn"
                )
            ) {

                const jobId =
                    event.target.getAttribute(
                        "data-id"
                    );


                editJob(jobId);

                return;
            }



            // ====================================
            // Delete
            // ====================================

            if (
                event.target.classList.contains(
                    "delete-btn"
                )
            ) {

                const jobId =
                    event.target.getAttribute(
                        "data-id"
                    );


                deleteJob(
                    jobId,
                    event.target
                );

            }

        }
    );



    // ============================================
    // Edit Job
    // ============================================

    function editJob(jobId) {

        console.log(
            "Edit Job:",
            jobId
        );


        window.location.href =
            `edit-job.html?id=${jobId}`;

    }



    // ============================================
    // Delete Job
    // ============================================

    async function deleteJob(
        jobId,
        deleteButton
    ) {


        // ========================================
        // Confirmation
        // ========================================

        const confirmed =
            confirm(
                "Are you sure you want to delete this job?"
            );


        if (!confirmed) {

            return;
        }



        // ========================================
        // Find Table Row
        // ========================================

        const row =
            deleteButton.closest("tr");



        // ========================================
        // Disable Button
        // ========================================

        deleteButton.disabled = true;

        deleteButton.textContent =
            "Deleting...";



        try {


            // ====================================
            // Call DELETE API
            // ====================================

            const response = await fetch(
                `http://localhost:8080/jobs/${jobId}`,
                {
                    method: "DELETE",

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
                    "You are not authorized to delete this job."
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

                const errorText =
                    await response.text();

                throw new Error(
                    errorText ||
                    "Failed to delete job."
                );
            }



            // ====================================
            // SUCCESS
            // ====================================

            console.log(
                "Job deleted:",
                jobId
            );


            // Remove ONLY this row

            row.remove();



            // Show success message

            showSuccess(
                "Job deleted successfully."
            );



            // Check if table is empty

            if (
                jobsTableBody.children.length === 0
            ) {

                noJobsMessage.classList.remove(
                    "d-none"
                );

            }


        } catch (error) {

            console.error(
                "Error deleting job:",
                error
            );


            showError(
                "Unable to delete job. Please try again."
            );


            // Re-enable button

            deleteButton.disabled = false;

            deleteButton.textContent =
                "Delete";

        }

    }



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

});