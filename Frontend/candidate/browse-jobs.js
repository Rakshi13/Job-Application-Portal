const username = localStorage.getItem("username");

if (!username) {
    window.location.href = "../login.html";
}

document.getElementById("username").textContent = username;


// Load jobs when page opens
loadJobs();

async function loadJobs() {
    const token = localStorage.getItem("token");

    if (!token) {
        alert("Session expired. Please login again.");
        window.location.href = "../login.html";
        return;
    }

    try {
        const response = await fetch("https://job-application-portal-ke3u.onrender.com/AllJobs", {
            method: "GET",
            headers: {
                "Authorization": `Bearer ${token}`
            }
        });

        if (response.status === 401) {
            throw new Error("Please login again.");
        }

        if (response.status === 403) {
            throw new Error("You don't have permission to view jobs.");
        }

        if (!response.ok) {
            throw new Error("Failed to fetch jobs.");
        }

        const jobs = await response.json();

        displayJobs(jobs);

    } catch (error) {
        console.error(error);

        document.getElementById("jobsContainer").textContent =
            error.message;
    }
}


function displayJobs(jobs) {

    const container = document.getElementById("jobsContainer");

    container.innerHTML = "";

    if (jobs.length === 0) {

        container.innerHTML = `
            <div class="alert alert-info">
                No jobs available.
            </div>
        `;

        return;
    }


    jobs.forEach(job => {

        const jobCard = document.createElement("div");

        jobCard.className = "card mb-3 shadow-sm";


        jobCard.innerHTML = `
            <div class="card-body">

                <h4 class="card-title">
                    ${job.title}
                </h4>

                <p class="mb-2">
                    <strong>Company Name:</strong>
                    ${job.companyName || "Not specified"}
                </p>

                <p class="mb-2">
                    <strong>Location:</strong>
                    ${job.location || "Not specified"}
                </p>

                <p class="mb-2">
                    <strong>Salary:</strong>
                    ${job.maxSalary || "Not specified"}
                </p>

                <p class="mb-2">
                    <strong>Description:</strong>
                    ${job.description || "Not specified"}
                </p>

                <button
                    class="btn btn-primary"
                    onclick="viewJob(${job.id})">
                    View Details
                </button>

            </div>
        `;

        container.appendChild(jobCard);

    });
}


function viewJob(jobId) {

    window.location.href = `job-details.html?id=${jobId}`;

}


function logout() {

    localStorage.removeItem("username");
    localStorage.removeItem("token");

    window.location.href = "../login.html";
}