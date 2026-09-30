
const API_URL = "http://localhost:8080";
const PROFILE_API = `${API_URL}/candidate/profile`;

const form = document.getElementById("profileForm");
const resumeInput = document.getElementById("resume");
const message = document.getElementById("message");

let originalProfile = null;

// Add the JWT token to secured API requests.
function getHeaders() {
    const token = localStorage.getItem("token");

    return {
        "Authorization": `Bearer ${token}`
    };
}

// Load candidate profile when the page opens.
document.addEventListener("DOMContentLoaded", loadProfile);

async function loadProfile() {
    try {
        const response = await fetch(PROFILE_API, {
            method: "GET",
            headers: getHeaders()
        });

        if (!response.ok) {
            throw new Error("Unable to load candidate profile.");
        }

        const profile = await response.json();

        originalProfile = profile;
        populateForm(profile);
        updateSummary(profile);

    } catch (error) {
        showMessage(error.message, "danger");
    }
}

// Populate the form with backend data.
function populateForm(profile) {
    document.getElementById("fullName").value =
        profile.fullName || "";

    document.getElementById("email").value =
        profile.email || "";

    document.getElementById("phoneNumber").value =
        profile.phoneNumber || "";

    document.getElementById("location").value =
        profile.location || "";

    document.getElementById("designation").value =
        profile.designation || "";

    document.getElementById("experience").value =
        profile.experience ?? "";

    document.getElementById("skills").value =
        Array.isArray(profile.skills)
            ? profile.skills.join(", ")
            : (profile.skills || "");

    document.getElementById("summary").value =
        profile.summary || "";

    document.getElementById("resumeName").textContent =
        profile.resumeFileName || "No resume uploaded";

    document.getElementById("resumeInfo").textContent =
        profile.resumeFileSize
            ? `${(profile.resumeFileSize / 1024).toFixed(1)} KB`
            : "PDF, DOC or DOCX";

    const downloadLink =
        document.getElementById("resumeDownload");

    if (profile.resumeDownloadUrl) {
        downloadLink.href = profile.resumeDownloadUrl;
        downloadLink.classList.remove("d-none");
    } else {
        downloadLink.classList.add("d-none");
    }
}

// Update the profile summary card.
function updateSummary(profile) {
    document.getElementById("displayName").textContent =
        profile.fullName || "Candidate Name";

    document.getElementById("displayEmail").textContent =
        profile.email || "Not provided";

    document.getElementById("displayPhone").textContent =
        profile.phoneNumber || "Not provided";

    document.getElementById("displayLocation").innerHTML =
        `<i class="bi bi-geo-alt me-1"></i>${escapeHTML(profile.location || "Location not provided")
        }`;

    document.getElementById("displayDesignation").textContent =
        profile.designation || "Designation not provided";

    document.getElementById("displayExperience").textContent =
        profile.experience != null
            ? `${profile.experience} years experience`
            : "Experience not provided";
}

// Prevent HTML injection when showing user-provided text.
function escapeHTML(value) {
    return String(value).replace(/[&<>"']/g, char => ({
        "&": "&amp;",
        "<": "&lt;",
        ">": "&gt;",
        '"': "&quot;",
        "'": "&#039;"
    })[char]);
}

// Enable editing.
function enableEdit() {
    document.querySelectorAll(
        "#profileForm input:not([type=file]), #profileForm textarea"
    ).forEach(input => {
        if (input.id !== "email") {
            input.disabled = false;
        }
    });

    resumeInput.disabled = false;

    document.getElementById("formActions")
        .classList.remove("d-none");

    document.getElementById("editButton")
        .classList.add("d-none");
}

// Disable editing.
function disableEdit() {
    document.querySelectorAll(
        "#profileForm input, #profileForm textarea"
    ).forEach(input => input.disabled = true);

    document.getElementById("formActions")
        .classList.add("d-none");

    document.getElementById("editButton")
        .classList.remove("d-none");
}

// Cancel changes and restore original data.
function cancelEdit() {
    if (originalProfile) {
        populateForm(originalProfile);
        updateSummary(originalProfile);
    }

    form.reset();
    if (originalProfile) {
        populateForm(originalProfile);
    }

    resumeInput.value = "";
    message.innerHTML = "";
    disableEdit();
}

// Validate selected resume.
resumeInput.addEventListener("change", function () {
    const file = this.files[0];

    if (!file) return;

    const allowedTypes = [
        "application/pdf",
        "application/msword",
        "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
    ];

    const allowedExtensions = /\.(pdf|doc|docx)$/i;

    if (
        !allowedTypes.includes(file.type) ||
        !allowedExtensions.test(file.name)
    ) {
        showMessage(
            "Please upload a PDF, DOC or DOCX file.",
            "danger"
        );
        this.value = "";
        return;
    }

    if (file.size > 5 * 1024 * 1024) {
        showMessage(
            "Resume size should not exceed 5 MB.",
            "danger"
        );
        this.value = "";
        return;
    }

    document.getElementById("resumeName").textContent =
        file.name;

    document.getElementById("resumeInfo").textContent =
        `${(file.size / 1024).toFixed(1)} KB`;
});

// Submit profile changes.
form.addEventListener("submit", async function (event) {
    event.preventDefault();

    const saveButton = document.getElementById("saveButton");
    saveButton.disabled = true;
    saveButton.textContent = "Saving...";

    try {
        const profileData = {
            fullName: document.getElementById("fullName").value.trim(),
            phoneNumber: document.getElementById("phoneNumber").value.trim(),
            location: document.getElementById("location").value.trim(),
            designation: document.getElementById("designation").value.trim(),
            experience: document.getElementById("experience").value === ""
                ? null
                : Number(document.getElementById("experience").value),
            skills: document.getElementById("skills").value
                .split(",")
                .map(skill => skill.trim())
                .filter(Boolean),
            summary: document.getElementById("summary").value.trim()
        };

        // 1. Update profile information.
        const response = await fetch(PROFILE_API, {
            method: "PUT",
            headers: {
                ...getHeaders(),
                "Content-Type": "application/json"
            },
            body: JSON.stringify(profileData)
        });

        if (!response.ok) {
            throw new Error("Failed to update profile.");
        }

        // 2. Upload resume separately, if selected.
        const resumeFile = resumeInput.files[0];

        if (resumeFile) {
            const formData = new FormData();
            formData.append("file", resumeFile);

            const resumeResponse = await fetch(
                `${PROFILE_API}/resume`,
                {
                    method: "POST",
                    headers: getHeaders(),
                    body: formData
                }
            );

            if (!resumeResponse.ok) {
                throw new Error(
                    "Profile updated, but resume upload failed."
                );
            }
        }

        showMessage("Profile updated successfully!", "success");

        await loadProfile();
        resumeInput.value = "";
        disableEdit();

    } catch (error) {
        showMessage(error.message, "danger");
    } finally {
        saveButton.disabled = false;
        saveButton.innerHTML =
            '<i class="bi bi-check-circle me-1"></i>Save Changes';
    }
});

// Display success and error messages.
function showMessage(text, type) {
    message.innerHTML = "";

    const alert = document.createElement("div");
    alert.className = `alert alert-${type}`;
    alert.textContent = text;

    message.appendChild(alert);
}

// Logout.
function logout() {
    localStorage.removeItem("token");
    window.location.href = "login.html";
}