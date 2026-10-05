const username = localStorage.getItem("username");
const token = localStorage.getItem("token");


// Check login
if (!username || !token) {
    window.location.href = "../login.html";
}


// Display username
document.getElementById("username").textContent = username;


// Get job ID from URL
const urlParams = new URLSearchParams(window.location.search);
const jobId = urlParams.get("id");


// Validate job ID
if (!jobId) {

    document.getElementById("jobContainer").innerHTML = `
        <div class="alert alert-danger">
            Job ID is missing.
        </div>
    `;

} else {

    loadJobDetails(jobId);

}


async function loadJobDetails(jobId) {

    try {

        const response = await fetch(
            `https://job-application-portal-ke3u.onrender.com/jobs/${jobId}`,
            {
                method: "GET",
                headers: {
                    "Authorization": `Bearer ${token}`
                }
            }
        );


        if (response.status === 401) {
            throw new Error("Session expired. Please login again.");
        }


        if (response.status === 403) {
            throw new Error("You don't have permission to view this job.");
        }


        if (response.status === 404) {
            throw new Error("Job not found.");
        }


        if (!response.ok) {
            throw new Error("Failed to load job details.");
        }


        const job = await response.json();

        displayJob(job);


    } catch (error) {

        console.error(error);

        document.getElementById("jobContainer").innerHTML = `
            <div class="alert alert-danger">
                ${error.message}
            </div>

            <button
                class="btn btn-secondary"
                onclick="goBack()">
                Back to Jobs
            </button>
        `;
    }
}


function displayJob(job) {

    const container = document.getElementById("jobContainer");


    container.innerHTML = `

        <div class="card shadow-sm">

            <div class="card-body p-4">

                <h2 class="mb-3">
                    ${job.title}
                </h2>


                <h5 class="text-primary mb-4">
                    ${job.companyName}
                </h5>


                <p>
                    <strong>Location:</strong>
                    ${job.location || "Not specified"}
                </p>


                <p>
                    <strong>Salary:</strong>
                    ₹${job.minSalary} - ₹${job.maxSalary}
                </p>


                <hr>


                <h5>Job Description</h5>

                <p>
                    ${job.description || "No description available."}
                </p>


                <div class="mt-4">

                    <button
                        class="btn btn-primary"
                        onclick="applyForJob(${job.id})">
                        Apply Now
                    </button>


                    <button
                        class="btn btn-outline-primary ms-2"
                        onclick="saveJob(${job.id})">
                        Save Job
                    </button>


                    <button
                        class="btn btn-secondary ms-2"
                        onclick="goBack()">
                        Back to Jobs
                    </button>

                </div>

            </div>

        </div>
    `;
}


async function applyForJob(jobId) {

    const token = localStorage.getItem("token");

    if (!token) {
        alert("Please login again.");
        window.location.href = "../login.html";
        return;
    }

    const confirmApply = confirm(
        "Are you sure you want to apply for this job?"
    );

    if (!confirmApply) {
        return;
    }

    try {

        const response = await fetch(
            `https://job-application-portal-ke3u.onrender.com/jobs/${jobId}/apply`,
            {
                method: "POST",
                headers: {
                    "Authorization": `Bearer ${token}`
                }
            }
        );

        if (response.status === 401) {

            alert("Session expired. Please login again.");
            window.location.href = "../login.html";
            return;
        }

        if (response.status === 403) {

            alert("You are not allowed to apply for this job.");
            return;
        }

        if (!response.ok) {
            const errorMessage = await response.text();
            alert(errorMessage || "Failed to apply for the job.");
            return;
        }

        const result = await response.text();
        alert(result);

    } catch (error) {

        console.error("Error applying for job:", error);

        alert("Something went wrong. Please try again.");
    }
}


async function saveJob(jobId) {
    const token = localStorage.getItem("token");

    if (!token) {
        alert("Please login to save jobs.");
        window.location.href = "login.html";
        return;
    }

    try {
        const response = await fetch(
            `https://job-application-portal-ke3u.onrender.com/jobs/${jobId}/save`,
            {
                method: "POST",
                headers: {
                    "Authorization": `Bearer ${token}`
                }
            }
        );

        const message = await response.text();

        if (response.ok) {
            alert(message);
        } else if (response.status === 409) {
            alert("You have already saved this job.");
        } else if (response.status === 401 || response.status === 403) {
            alert("Session expired or you are not authorized. Please login again.");
        } else if (response.status === 404) {
            alert("Job not found.");
        } else {
            alert(message || "Unable to save job. Please try again.");
        }

    } catch (error) {
        console.error("Error saving job:", error);
        alert("Unable to connect to the server. Please try again.");
    }
}


function goBack() {

    window.location.href = "browse-jobs.html";

}


function logout() {

    localStorage.removeItem("username");
    localStorage.removeItem("token");

    window.location.href = "../login.html";
}