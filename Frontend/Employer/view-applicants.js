const applicantsButton = document.createElement("button");

applicantsButton.type = "button";
applicantsButton.className = "btn btn-primary";
applicantsButton.textContent = "View Applicants";

applicantsButton.addEventListener("click", () => {
    const params = new URLSearchParams({
        jobId: job.id,
        jobTitle: job.title || "Job Applicants"
    });

    window.location.href =
        `view-applicants.html?${params.toString()}`;
});