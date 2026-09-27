const username = localStorage.getItem("username");
const token = localStorage.getItem("token");


// Check login
if (!username || !token) {

    alert("Please login again.");

    window.location.href = "../login.html";
}


// Display username
document.getElementById("username").textContent = username;


// Load applied jobs
loadAppliedJobs();


async function loadAppliedJobs() {

    try {

        const response = await fetch(
            "http://localhost:8080/candidate/applied-jobs",
            {
                method: "GET",

                headers: {
                    "Authorization": `Bearer ${token}`
                }
            }
        );


        // Unauthorized
        if (response.status === 401) {

            alert("Session expired. Please login again.");

            window.location.href = "../login.html";

            return;
        }


        // Forbidden
        if (response.status === 403) {

            alert("You are not allowed to view applied jobs.");

            return;
        }


        if (!response.ok) {

            const errorMessage = await response.text();

            throw new Error(
                errorMessage || "Failed to load applied jobs"
            );
        }


        const applications = await response.json();


        document.getElementById("loadingMessage").style.display = "none";


        if (applications.length === 0) {

            document.getElementById("noApplications").style.display = "block";

            return;
        }


        displayApplications(applications);

    }

    catch (error) {

        console.error(
            "Error loading applied jobs:",
            error
        );

        document.getElementById("loadingMessage").textContent =
            "Failed to load applied jobs. Please try again.";
    }
}

function displayApplications(applications) {

    const container =
        document.getElementById("applicationsContainer");

    container.innerHTML = "";

    applications.forEach(application => {

        const card = document.createElement("div");

        card.className = "col-md-6";

        card.innerHTML = `

            <div class="card shadow-sm h-100">

                <div class="card-body">

                    <h4 class="card-title">
                        ${application.title}
                    </h4>

                    <p class="mb-2">
                        <strong>Company:</strong>
                        ${application.companyName}
                    </p>

                    <p class="mb-2">
                        <strong>Location:</strong>
                        ${application.location || "Not specified"}
                    </p>

                    <p class="mb-2">
                        <strong>Applied On:</strong>
                        ${formatDate(application.appliedDate)}
                    </p>

                    <p class="mb-3">
                        <strong>Status:</strong>

                        <span class="badge bg-success">
                            ${application.status}
                        </span>
                    </p>

                </div>

            </div>
        `;

        container.appendChild(card);
    });
}

function formatDate(dateString) {

    const date = new Date(dateString);

    return date.toLocaleDateString("en-IN", {
        day: "2-digit",
        month: "short",
        year: "numeric"
    });
}

function logout() {

    localStorage.removeItem("username");

    localStorage.removeItem("token");

    window.location.href = "../login.html";
}

function viewJob(jobId) {

    window.location.href =
        `job-details.html?id=${jobId}`;
}