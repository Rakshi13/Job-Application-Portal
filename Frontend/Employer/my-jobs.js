document.addEventListener("DOMContentLoaded", function () {

    const token = localStorage.getItem("token");


    // Check authentication
    if (!token) {

        window.location.href = "../login.html";

        return;
    }


    const loadingMessage =
        document.getElementById("loadingMessage");

    const errorMessage =
        document.getElementById("errorMessage");

    const noJobsMessage =
        document.getElementById("noJobsMessage");

    const jobsTableContainer =
        document.getElementById("jobsTableContainer");

    const jobsTableBody =
        document.getElementById("jobsTableBody");


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


            // Unauthorized
            if (response.status === 401) {

                localStorage.removeItem("token");
                localStorage.removeItem("username");
                localStorage.removeItem("role");

                window.location.href =
                    "../login.html";

                return;
            }


            // Forbidden
            if (response.status === 403) {

                showError(
                    "You don't have permission to view jobs."
                );

                return;
            }


            if (!response.ok) {

                throw new Error(
                    "Failed to load jobs"
                );
            }


            const jobs = await response.json();

            console.log("Jobs:", jobs);


            loadingMessage.classList.add("d-none");


            if (!jobs || jobs.length === 0) {

                noJobsMessage.classList.remove("d-none");

                return;
            }


            displayJobs(jobs);


        } catch (error) {

            console.error(
                "Error loading jobs:",
                error
            );

            loadingMessage.classList.add("d-none");

            showError(
                "Unable to load jobs. Please try again."
            );

        }

    }



    function displayJobs(jobs) {

        jobsTableBody.innerHTML = "";


        jobs.forEach(function (job) {


            const row =
                document.createElement("tr");


            row.innerHTML = `

                <td>

                    <div class="fw-semibold">
                        ${job.title}
                    </div>

                </td>


                <td>

                    ${job.location}

                </td>


                <td>

                    ₹${job.minSalary.toLocaleString("en-IN")}

                </td>


                <td>

                    ₹${job.maxSalary.toLocaleString("en-IN")}

                </td>


                <td class="text-center">

                    <button
                        class="btn btn-sm btn-outline-primary me-2"
                        onclick="editJob(${job.id})">

                        Edit

                    </button>


                    <button
                        class="btn btn-sm btn-outline-danger"
                        onclick="deleteJob(${job.id})">

                        Delete

                    </button>

                </td>

            `;


            jobsTableBody.appendChild(row);

        });


        jobsTableContainer.classList.remove("d-none");

    }



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



function editJob(jobId) {

    console.log(
        "Edit job:",
        jobId
    );

    // We will implement this next

}



function deleteJob(jobId) {

    console.log(
        "Delete job:",
        jobId
    );

    // We will implement this next

}