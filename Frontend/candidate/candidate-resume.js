const RESUME_API = "http://localhost:8080/candidate/profile/resume";
const MAX_FILE_SIZE = 5 * 1024 * 1024; // 5 MB

const resumeStatus = document.getElementById("resumeStatus");
const resumeDetails = document.getElementById("resumeDetails");
const noResume = document.getElementById("noResume");
const resumeFileName = document.getElementById("resumeFileName");
const resumeFileSize = document.getElementById("resumeFileSize");

const resumeUploadForm = document.getElementById("resumeUploadForm");
const resumeFileInput = document.getElementById("resumeFile");
const selectedFileName = document.getElementById("selectedFileName");

const uploadResumeBtn = document.getElementById("uploadResumeBtn");
const downloadResumeBtn = document.getElementById("downloadResumeBtn");
const deleteResumeBtn = document.getElementById("deleteResumeBtn");
const resumeMessage = document.getElementById("resumeMessage");

// Get the JWT token stored at login
function getResumeToken() {
    return localStorage.getItem("token");
}

// Make an authenticated request
async function resumeApiRequest(url, options = {}) {
    const token = getResumeToken();

    if (!token) {
        window.location.href = "candidate-login.html";
        throw new Error("Please log in to continue.");
    }

    const headers = {
        Authorization: `Bearer ${token}`,
        ...options.headers
    };

    return fetch(url, {
        ...options,
        headers
    });
}

// Show a message to the candidate
function showResumeMessage(message, type = "danger") {
    resumeMessage.textContent = message;
    resumeMessage.className = `alert alert-${type} mt-3`;
    resumeMessage.classList.remove("d-none");
}

function hideResumeMessage() {
    resumeMessage.textContent = "";
    resumeMessage.className = "alert d-none mt-3";
}

// Show loading status
function showResumeLoading() {
    resumeStatus.classList.remove("d-none");
    resumeStatus.innerHTML = `
        <span class="spinner-border spinner-border-sm text-primary"
              role="status"></span>
        <span>Checking resume...</span>
    `;

    resumeDetails.classList.add("d-none");
    noResume.classList.add("d-none");
}

// Format file size
function formatFileSize(bytes) {
    if (bytes === null || bytes === undefined || isNaN(bytes)) {
        return "";
    }

    if (bytes < 1024) {
        return `${bytes} bytes`;
    }

    if (bytes < 1024 * 1024) {
        return `${(bytes / 1024).toFixed(1)} KB`;
    }

    return `${(bytes / (1024 * 1024)).toFixed(2)} MB`;
}

// Display existing resume details
function displayResume(metadata) {
    const name =
        metadata.originalFileName ||
        metadata.fileName ||
        metadata.filename ||
        "Uploaded Resume";

    const size =
        metadata.fileSize ??
        metadata.size ??
        null;

    resumeFileName.textContent = name;
    resumeFileSize.textContent = formatFileSize(size);

    resumeStatus.classList.add("d-none");
    noResume.classList.add("d-none");
    resumeDetails.classList.remove("d-none");
}

// Display empty resume state
function displayNoResume() {
    resumeStatus.classList.add("d-none");
    resumeDetails.classList.add("d-none");
    noResume.classList.remove("d-none");
}

// Load resume metadata
async function loadResumeDetails() {
    hideResumeMessage();
    showResumeLoading();

    try {
        const response = await resumeApiRequest(RESUME_API, {
            method: "GET"
        });

        if (response.status === 401) {
            localStorage.removeItem("token");
            window.location.href = "candidate-login.html";
            return;
        }

        if (response.status === 403) {
            throw new Error(
                "You are not authorized to access this resume."
            );
        }

        if (response.status === 404) {
            displayNoResume();
            return;
        }

        if (!response.ok) {
            throw new Error("Unable to load resume details.");
        }

        const metadata = await response.json();
        displayResume(metadata);

    } catch (error) {
        resumeStatus.classList.add("d-none");
        showResumeMessage(
            error.message || "Something went wrong while loading the resume."
        );
        console.error("Resume metadata error:", error);
    }
}

// Validate the selected file
function validateResumeFile(file) {
    if (!file) {
        return "Please select a resume file.";
    }

    const allowedExtensions = ["pdf", "doc", "docx"];
    const extension = file.name.split(".").pop().toLowerCase();

    if (!allowedExtensions.includes(extension)) {
        return "Only PDF, DOC and DOCX files are allowed.";
    }

    if (file.size > MAX_FILE_SIZE) {
        return "File size must not exceed 5 MB.";
    }

    if (file.size === 0) {
        return "The selected file is empty.";
    }

    return null;
}

// Display selected file name
resumeFileInput.addEventListener("change", function () {
    hideResumeMessage();

    const file = resumeFileInput.files[0];

    if (!file) {
        selectedFileName.textContent = "";
        selectedFileName.classList.add("d-none");
        return;
    }

    const validationError = validateResumeFile(file);

    if (validationError) {
        showResumeMessage(validationError);
        resumeFileInput.value = "";
        selectedFileName.textContent = "";
        selectedFileName.classList.add("d-none");
        return;
    }

    selectedFileName.textContent =
        `Selected file: ${file.name} (${formatFileSize(file.size)})`;

    selectedFileName.classList.remove("d-none");
});

// Upload or replace resume
async function uploadResume(event) {
    event.preventDefault();
    hideResumeMessage();

    const file = resumeFileInput.files[0];
    const validationError = validateResumeFile(file);

    if (validationError) {
        showResumeMessage(validationError);
        return;
    }

    const formData = new FormData();
    formData.append("file", file);

    uploadResumeBtn.disabled = true;
    uploadResumeBtn.innerHTML = `
        <span class="spinner-border spinner-border-sm me-2"
              role="status"></span>
        Uploading...
    `;

    try {
        const response = await resumeApiRequest(RESUME_API, {
            method: "POST",
            body: formData
        });

        const responseText = await response.text();

        if (response.status === 401) {
            localStorage.removeItem("token");
            window.location.href = "candidate-login.html";
            return;
        }

        if (response.status === 403) {
            throw new Error("You are not authorized to upload a resume.");
        }

        if (!response.ok) {
            throw new Error(responseText || "Resume upload failed.");
        }

        resumeUploadForm.reset();
        selectedFileName.textContent = "";
        selectedFileName.classList.add("d-none");

        showResumeMessage(
            responseText || "Resume uploaded successfully.",
            "success"
        );

        await loadResumeDetails();

    } catch (error) {
        showResumeMessage(
            error.message || "Something went wrong while uploading."
        );
        console.error("Resume upload error:", error);
    } finally {
        uploadResumeBtn.disabled = false;
        uploadResumeBtn.innerHTML =
            '<i class="bi bi-cloud-arrow-up me-2"></i>Upload Resume';
    }
}

// Extract filename from Content-Disposition header
function getDownloadFileName(response) {
    const disposition = response.headers.get("Content-Disposition");

    if (!disposition) {
        return resumeFileName.textContent || "resume";
    }

    const utf8Match = disposition.match(/filename\*=UTF-8''([^;]+)/i);

    if (utf8Match && utf8Match[1]) {
        return decodeURIComponent(utf8Match[1].replace(/["']/g, ""));
    }

    const normalMatch = disposition.match(/filename="?([^";]+)"?/i);

    if (normalMatch && normalMatch[1]) {
        return normalMatch[1];
    }

    return resumeFileName.textContent || "resume";
}

// Download resume
async function downloadResume() {
    hideResumeMessage();

    downloadResumeBtn.disabled = true;
    downloadResumeBtn.innerHTML = `
        <span class="spinner-border spinner-border-sm me-2"
              role="status"></span>
        Downloading...
    `;

    try {
        const response = await resumeApiRequest(
            `${RESUME_API}/download`,
            { method: "GET" }
        );

        if (response.status === 401) {
            localStorage.removeItem("token");
            window.location.href = "candidate-login.html";
            return;
        }

        if (response.status === 403) {
            throw new Error("You are not authorized to download this resume.");
        }

        if (response.status === 404) {
            throw new Error("Resume not found. Please upload your resume.");
        }

        if (!response.ok) {
            throw new Error("Unable to download the resume.");
        }

        const blob = await response.blob();
        const fileName = getDownloadFileName(response);
        const fileUrl = URL.createObjectURL(blob);

        const link = document.createElement("a");
        link.href = fileUrl;
        link.download = fileName;

        document.body.appendChild(link);
        link.click();
        link.remove();

        URL.revokeObjectURL(fileUrl);

    } catch (error) {
        showResumeMessage(
            error.message || "Something went wrong while downloading."
        );
        console.error("Resume download error:", error);
    } finally {
        downloadResumeBtn.disabled = false;
        downloadResumeBtn.innerHTML =
            '<i class="bi bi-download me-2"></i>Download';
    }
}

// Delete resume
async function deleteResume() {
    const confirmed = window.confirm(
        "Are you sure you want to delete your resume?"
    );

    if (!confirmed) {
        return;
    }

    hideResumeMessage();

    deleteResumeBtn.disabled = true;
    deleteResumeBtn.innerHTML = `
        <span class="spinner-border spinner-border-sm me-2"
              role="status"></span>
        Deleting...
    `;

    try {
        const response = await resumeApiRequest(RESUME_API, {
            method: "DELETE"
        });

        const responseText = await response.text();

        if (response.status === 401) {
            localStorage.removeItem("token");
            window.location.href = "candidate-login.html";
            return;
        }

        if (response.status === 403) {
            throw new Error("You are not authorized to delete this resume.");
        }

        if (response.status === 404) {
            throw new Error("No resume was found to delete.");
        }

        if (!response.ok) {
            throw new Error(responseText || "Unable to delete resume.");
        }

        resumeUploadForm.reset();
        selectedFileName.textContent = "";
        selectedFileName.classList.add("d-none");

        displayNoResume();
        showResumeMessage(
            responseText || "Resume deleted successfully.",
            "success"
        );

    } catch (error) {
        showResumeMessage(
            error.message || "Something went wrong while deleting."
        );
        console.error("Resume delete error:", error);
    } finally {
        deleteResumeBtn.disabled = false;
        deleteResumeBtn.innerHTML =
            '<i class="bi bi-trash me-2"></i>Delete';
    }
}

// Event listeners
resumeUploadForm.addEventListener("submit", uploadResume);
downloadResumeBtn.addEventListener("click", downloadResume);
deleteResumeBtn.addEventListener("click", deleteResume);

// Load resume details when page opens
l//oadResumeDetails();