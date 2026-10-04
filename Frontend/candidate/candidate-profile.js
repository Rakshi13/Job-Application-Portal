const API_URL = "http://localhost:8080/candidate/profile";

let currentMode = "loading";
let savedProfile = null;
let resumeUploadedForCurrentSelection = false;

const resumeFileInput = document.getElementById("resumeFile");

const loadingBox = document.getElementById("loadingBox");
const profileContent = document.getElementById("profileContent");
const messageBox = document.getElementById("messageBox");

const viewSection = document.getElementById("viewSection");
const formSection = document.getElementById("formSection");
const profileForm = document.getElementById("profileForm");

const formTitle = document.getElementById("formTitle");
const formSubtitle = document.getElementById("formSubtitle");
const saveBtn = document.getElementById("saveBtn");

const editBtn = document.getElementById("editBtn");
const viewEditBtn = document.getElementById("viewEditBtn");
const cancelBtn = document.getElementById("cancelBtn");
const logoutBtn = document.getElementById("logoutBtn");

const fields = {
    fullName: document.getElementById("fullName"),
    email: document.getElementById("email"),
    phoneNo: document.getElementById("phoneNumber"),
    location: document.getElementById("location"),
    currentDesignation: document.getElementById("currentDesignation"),
    experience: document.getElementById("experience"),
    skills: document.getElementById("skills"),
    professionalSummary: document.getElementById("professionalSummary")
};

// Get JWT token
function getToken() {
    return localStorage.getItem("token");
}

// Common API request
async function apiRequest(url, options = {}) {
    const token = getToken();

    if (!token) {
        window.location.href = "candidate-login.html";
        throw new Error("Please log in to continue.");
    }

    const headers = {
        Authorization: `Bearer ${token}`,
        ...options.headers
    };

    if (options.body) {
        headers["Content-Type"] = "application/json";
    }

    return fetch(url, {
        ...options,
        headers
    });
}

// Display messages
function showMessage(message, type = "danger") {
    messageBox.textContent = message;
    messageBox.className = `alert alert-${type}`;
    messageBox.classList.remove("d-none");
}

function hideMessage() {
    messageBox.textContent = "";
    messageBox.className = "alert d-none";
}

// Show main profile area
function showPageContent() {
    loadingBox.classList.add("d-none");
    profileContent.classList.remove("d-none");
}

// Switch between view, create and update modes
function setMode(mode) {
    currentMode = mode;

    viewSection.classList.add("d-none");
    formSection.classList.add("d-none");
    editBtn.classList.add("d-none");

    if (mode === "view") {
        viewSection.classList.remove("d-none");
        editBtn.classList.remove("d-none");
    }

    if (mode === "create" || mode === "update") {
        formSection.classList.remove("d-none");

        if (mode === "create") {
            formTitle.textContent = "Create Your Profile";
            formSubtitle.textContent =
                "Enter your personal and professional details.";

            saveBtn.innerHTML =
                '<i class="bi bi-check-circle me-2"></i>Create Profile';
        } else {
            formTitle.textContent = "Update Your Profile";
            formSubtitle.textContent =
                "Update your personal and professional details.";

            saveBtn.innerHTML =
                '<i class="bi bi-save me-2"></i>Save Changes';
        }
    }
}

// Display empty values safely
function displayValue(value) {
    return value === null || value === undefined || value === ""
        ? "-"
        : String(value);
}

// Render saved profile in view mode
function renderProfile(profile) {
    document.getElementById("summaryName").textContent =
        displayValue(profile.fullName);

    document.getElementById("summaryDesignation").textContent =
        displayValue(profile.currentDesignation);

    document.getElementById("summaryLocation").textContent =
        displayValue(profile.location);

    document.getElementById("summaryEmail").textContent =
        displayValue(profile.email);

    document.getElementById("summaryPhone").textContent =
        displayValue(profile.phoneNo);

    document.getElementById("summaryExperience").textContent =
        profile.experience == null
            ? "Experience not added"
            : `${profile.experience} years experience`;

    document.getElementById("viewFullName").textContent =
        displayValue(profile.fullName);

    document.getElementById("viewEmail").textContent =
        displayValue(profile.email);

    document.getElementById("viewPhone").textContent =
        displayValue(profile.phoneNo);

    document.getElementById("viewLocation").textContent =
        displayValue(profile.location);

    document.getElementById("viewDesignation").textContent =
        displayValue(profile.currentDesignation);

    document.getElementById("viewExperience").textContent =
        profile.experience == null
            ? "-"
            : `${profile.experience} years`;

    document.getElementById("viewSkills").textContent =
        displayValue(profile.skills);

    document.getElementById("viewSummary").textContent =
        displayValue(profile.professionalSummary);
}

// Fill form with existing profile data
function fillForm(profile) {
    fields.fullName.value = profile.fullName ?? "";
    fields.email.value = profile.email ?? "";
    fields.phoneNo.value = profile.phoneNo ?? "";
    fields.location.value = profile.location ?? "";
    fields.currentDesignation.value =
        profile.currentDesignation ?? "";
    fields.experience.value = profile.experience ?? "";
    fields.skills.value = profile.skills ?? "";
    fields.professionalSummary.value =
        profile.professionalSummary ?? "";
}

// Clear form for first-time profile creation
function clearForm() {
    profileForm.reset();
}

// Create request body matching CandidateProfileRequestDto
function getRequestBody() {
    return {
        fullName: fields.fullName.value.trim(),
        email: fields.email.value.trim(),
        phoneNo: fields.phoneNo.value.trim(),
        location: fields.location.value.trim(),
        currentDesignation: fields.currentDesignation.value.trim(),
        experience: fields.experience.value === ""
            ? null
            : Number(fields.experience.value),
        skills: fields.skills.value.trim(),
        professionalSummary:
            fields.professionalSummary.value.trim()
    };
}

// Load candidate profile
async function loadProfile() {
    hideMessage();
    loadingBox.classList.remove("d-none");
    profileContent.classList.add("d-none");

    try {
        const response = await apiRequest(API_URL, {
            method: "GET"
        });

        // Candidate has not created a profile yet
        if (response.status === 404) {
            savedProfile = null;
            clearForm();
            showPageContent();
            setMode("create");
            return;
        }

        if (response.status === 401) {
            localStorage.removeItem("token");
            window.location.href = "candidate-login.html";
            return;
        }

        if (response.status === 403) {
            throw new Error(
                "You are not authorized to access this profile. " +
                "Please check your candidate role."
            );
        }

        if (!response.ok) {
            throw new Error("Unable to load your profile.");
        }

        savedProfile = await response.json();

        renderProfile(savedProfile);
        showPageContent();
        setMode("view");

        // Display resume information.
        displayResume(savedProfile);

    } catch (error) {
        loadingBox.classList.add("d-none");
        profileContent.classList.add("d-none");

        showMessage(error.message || "Something went wrong.");
        console.error("Profile loading error:", error);
    }
}

// Open edit mode
function openEditMode() {
    if (!savedProfile) {
        return;
    }

    hideMessage();
    fillForm(savedProfile);
    setMode("update");
}

// Cancel form changes
function cancelForm() {
    hideMessage();

    if (currentMode === "update" && savedProfile) {
        fillForm(savedProfile);
        setMode("view");
    } else {
        clearForm();
        setMode("create");
    }
}

// Create or update profile
async function saveProfile(event) {
    event.preventDefault();
    hideMessage();

    if (resumeFileInput.files.length > 0 && !resumeUploadedForCurrentSelection) {
        const confirmSave = confirm(
            "You selected a resume but haven't uploaded it. " +
            "Do you want to save your profile without uploading this resume?"
        );

        if (!confirmSave) {
            return;
        }
    }

    if (!profileForm.reportValidity()) {
        return;
    }

    const isCreate = currentMode === "create";

    if (!isCreate && currentMode !== "update") {
        return;
    }

    const requestBody = getRequestBody();

    if (
        requestBody.experience === null ||
        !Number.isInteger(requestBody.experience) ||
        requestBody.experience < 0
    ) {
        showMessage("Please enter valid experience in whole years.");
        return;
    }

    saveBtn.disabled = true;
    saveBtn.innerHTML =
        '<span class="spinner-border spinner-border-sm me-2"></span>Saving...';

    try {
        const response = await apiRequest(API_URL, {
            method: isCreate ? "POST" : "PUT",
            body: JSON.stringify(requestBody)
        });

        const responseText = await response.text();

        if (response.status === 401) {
            localStorage.removeItem("token");
            window.location.href = "candidate-login.html";
            return;
        }

        if (response.status === 403) {
            throw new Error(
                "You are not authorized to save this profile."
            );
        }

        if (response.status === 409) {
            throw new Error(
                responseText || "Candidate profile already exists."
            );
        }

        if (!response.ok) {
            throw new Error(
                responseText || "Unable to save your profile."
            );
        }

        // Refresh saved data after a successful save
        await loadProfile();
        resumeFileInput.value = "";
        resumeUploadedForCurrentSelection = false;

        showMessage(
            isCreate
                ? "Candidate profile created successfully."
                : "Candidate profile updated successfully.",
            "success"
        );

    } catch (error) {
        showMessage(error.message || "Something went wrong.");
        console.error("Profile save error:", error);
    } finally {
        saveBtn.disabled = false;

        if (currentMode === "create") {
            saveBtn.innerHTML =
                '<i class="bi bi-check-circle me-2"></i>Create Profile';
        } else {
            saveBtn.innerHTML =
                '<i class="bi bi-save me-2"></i>Save Changes';
        }
    }
}

async function uploadResume() {
    const fileInput = document.getElementById("resumeFile");
    const file = fileInput.files[0];

    if (!file) {
        alert("Please select a resume first.");
        return;
    }

    const formData = new FormData();
    formData.append("file", file);

    try {
        const response = await fetch(
            "http://localhost:8080/candidate/profile/resume",
            {
                method: "POST",
                headers: {
                    Authorization: "Bearer " + localStorage.getItem("token")
                },
                body: formData
            }
        );

        const message = await response.text();

        if (!response.ok) {
            throw new Error(message || "Resume upload failed");
        }

        resumeUploadedForCurrentSelection = true;
        alert(message);
        showMessage("Resume uploaded successfully!", "success");

        resumeFileInput.value = "";
        resumeUploadedForCurrentSelection = false;
    } catch (error) {
        alert(error.message);
    }
}

function displayResume(profile) {
    const resumeAvailable = document.getElementById("resumeAvailable");
    const noResume = document.getElementById("noResume");
    const resumeFileName = document.getElementById("resumeFileName");
    const resumeError = document.getElementById("resumeError");

    resumeError.style.display = "none";

    if (profile.resumeUploaded && profile.resumeFileName) {
        resumeFileName.textContent = profile.resumeFileName;

        resumeAvailable.style.display = "block";
        noResume.style.display = "none";
    } else {
        resumeAvailable.style.display = "none";
        noResume.style.display = "block";
    }
}

async function downloadResume() {
    const resumeError = document.getElementById("resumeError");

    resumeError.style.display = "none";
    resumeError.textContent = "";

    try {
        // Get the JWT token saved during login.
        // Change "token" if your application uses a different key.
        const token = localStorage.getItem("token");

        if (!token) {
            throw new Error("Please login again to download your resume.");
        }

        const response = await fetch(
            "http://localhost:8080/candidate/profile/resume/download",
            {
                method: "GET",
                headers: {
                    "Authorization": `Bearer ${token}`
                }
            }
        );

        if (!response.ok) {
            const errorMessage = await response.text();
            throw new Error(errorMessage || "Failed to download resume.");
        }

        // Convert the response into a downloadable file.
        const blob = await response.blob();

        // Read the filename from the profile page.
        const fileName = document.getElementById("resumeFileName").textContent
            || "resume";

        // Create a temporary URL for the file.
        const fileURL = window.URL.createObjectURL(blob);

        // Create a temporary link and trigger the download.
        const link = document.createElement("a");
        link.href = fileURL;
        link.download = fileName;

        document.body.appendChild(link);
        link.click();

        // Clean up.
        link.remove();
        window.URL.revokeObjectURL(fileURL);

    } catch (error) {
        console.error("Resume download error:", error);

        resumeError.textContent =
            error.message || "Something went wrong while downloading your resume.";

        resumeError.style.display = "block";
    }
}

// Logout
function logout() {
    localStorage.removeItem("token");
    window.location.href = "candidate-login.html";
}

// Event listeners
editBtn.addEventListener("click", openEditMode);
viewEditBtn.addEventListener("click", openEditMode);
cancelBtn.addEventListener("click", cancelForm);
profileForm.addEventListener("submit", saveProfile);
logoutBtn.addEventListener("click", logout);

document.getElementById("uploadResumeBtn").addEventListener("click", uploadResume);
resumeFileInput.addEventListener("change", () => {
    resumeUploadedForCurrentSelection = false;
});

// Initial load
loadProfile();