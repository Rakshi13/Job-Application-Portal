const API_URL = "https://job-application-portal-ke3u.onrender.com/candidate/profile";

const loadingBox = document.getElementById("loadingBox");
const formContainer = document.getElementById("formContainer");
const profileForm = document.getElementById("editProfileForm");
const messageBox = document.getElementById("messageBox");
const saveBtn = document.getElementById("saveBtn");
const summaryCount = document.getElementById("summaryCount");

const fields = {
    fullName: document.getElementById("fullName"),
    email: document.getElementById("email"),
    phoneNo: document.getElementById("phoneNo"),
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

// Display message
function showMessage(message, type = "danger") {
    messageBox.textContent = message;
    messageBox.className = `alert alert-${type}`;
    messageBox.classList.remove("d-none");
}

function hideMessage() {
    messageBox.textContent = "";
    messageBox.className = "alert d-none";
}

// API request with JWT
async function apiRequest(method, body = null) {
    const token = getToken();

    if (!token) {
        window.location.href = "candidate-login.html";
        throw new Error("Please log in to continue.");
    }

    const options = {
        method,
        headers: {
            Authorization: `Bearer ${token}`
        }
    };

    if (body !== null) {
        options.headers["Content-Type"] = "application/json";
        options.body = JSON.stringify(body);
    }

    return fetch(API_URL, options);
}

// Populate form with existing profile data
function populateForm(profile) {
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

    updateSummaryCount();
}

// Load profile using GET API
async function loadProfile() {
    hideMessage();

    loadingBox.classList.remove("d-none");
    formContainer.classList.add("d-none");

    try {
        const response = await apiRequest("GET");

        if (response.status === 401) {
            localStorage.removeItem("token");
            window.location.href = "candidate-login.html";
            return;
        }

        if (response.status === 403) {
            throw new Error(
                "You are not authorized to access this profile."
            );
        }

        if (response.status === 404) {
            throw new Error(
                "Candidate profile not found. Please create your profile first."
            );
        }

        if (!response.ok) {
            throw new Error("Unable to load candidate profile.");
        }

        let profile = await response.json();

        // Supports either a single object or a list response
        if (Array.isArray(profile)) {
            profile = profile[0];
        }

        if (!profile) {
            throw new Error("No candidate profile data was returned.");
        }

        populateForm(profile);

        loadingBox.classList.add("d-none");
        formContainer.classList.remove("d-none");

    } catch (error) {
        loadingBox.classList.add("d-none");
        showMessage(error.message || "Something went wrong.");
        console.error("Load profile error:", error);
    }
}

// Build request body for PUT API
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

// Submit updated profile
async function updateProfile(event) {
    event.preventDefault();
    hideMessage();

    if (!profileForm.reportValidity()) {
        return;
    }

    const requestBody = getRequestBody();

    if (
        requestBody.experience === null ||
        !Number.isInteger(requestBody.experience) ||
        requestBody.experience < 0
    ) {
        showMessage("Please enter a valid number of years of experience.");
        return;
    }

    saveBtn.disabled = true;
    saveBtn.innerHTML =
        '<span class="spinner-border spinner-border-sm me-2"></span>Saving...';

    try {
        const response = await apiRequest("PUT", requestBody);
        const responseText = await response.text();

        if (response.status === 401) {
            localStorage.removeItem("token");
            window.location.href = "candidate-login.html";
            return;
        }

        if (response.status === 403) {
            throw new Error(
                "You are not authorized to update this profile."
            );
        }

        if (!response.ok) {
            throw new Error(
                responseText || "Unable to update your profile."
            );
        }

        showMessage(
            responseText || "Profile updated successfully.",
            "success"
        );

        // Redirect after the candidate sees the success message
        // The candidate can also use the Back to Profile link.

    } catch (error) {
        showMessage(error.message || "Something went wrong.");
        console.error("Update profile error:", error);
    } finally {
        saveBtn.disabled = false;
        saveBtn.innerHTML =
            '<i class="bi bi-check-circle me-2"></i>Save Changes';
    }
}

// Character count for professional summary
function updateSummaryCount() {
    summaryCount.textContent =
        fields.professionalSummary.value.length;
}

// Logout
function logout() {
    localStorage.removeItem("token");
    window.location.href = "candidate-login.html";
}

// Event listeners
profileForm.addEventListener("submit", updateProfile);

fields.professionalSummary.addEventListener(
    "input",
    updateSummaryCount
);

document.getElementById("logoutBtn").addEventListener(
    "click",
    logout
);

// Load profile when page opens
loadProfile();