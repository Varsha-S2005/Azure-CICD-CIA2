
const equipmentList = document.getElementById("equipmentList");
const searchInput = document.getElementById("searchInput");
const messageBox = document.getElementById("message");

let equipmentData = [];

const equipmentIcons = {
    Laptop: "💻",
    Projector: "📽️",
    Tablet: "📱"
};

// Fetch equipment from Spring Boot backend
async function loadEquipment() {
    try {
        equipmentList.innerHTML =
            '<p class="loading">Loading equipment...</p>';

        const response = await fetch("/api/equipment");

        if (!response.ok) {
            throw new Error("Unable to fetch equipment.");
        }

        equipmentData = await response.json();

        updateDashboard();
        displayEquipment(equipmentData);

    } catch (error) {
        equipmentList.innerHTML =
            '<p class="loading">Unable to load equipment. Please try again.</p>';

        showMessage(error.message, "error");
    }
}

// Update dashboard summary cards
function updateDashboard() {
    const total = equipmentData.reduce(
        (sum, item) => sum + item.quantity, 0
    );

    const available = equipmentData.reduce(
        (sum, item) => sum + item.available, 0
    );

    const booked = total - available;

    document.getElementById("totalEquipment").textContent = total;
    document.getElementById("availableEquipment").textContent = available;
    document.getElementById("bookedEquipment").textContent = booked;
}

// Display equipment cards dynamically
function displayEquipment(items) {
    equipmentList.innerHTML = "";

    if (items.length === 0) {
        equipmentList.innerHTML =
            '<p class="loading">No matching equipment found.</p>';
        return;
    }

    items.forEach(item => {
        const isAvailable = item.available > 0;
        const hasBookings = item.available < item.quantity;

        const card = document.createElement("div");
        card.className = "equipment-card";

        const icon = equipmentIcons[item.name] || "📦";

        card.innerHTML = `
            <div class="equipment-icon">${icon}</div>

            <h3>${escapeHTML(item.name)}</h3>

            <p class="quantity">
                Available: ${item.available} / ${item.quantity}
            </p>

            <span class="availability-label ${
                isAvailable ? "in-stock" : "out-of-stock"
            }">
                ${isAvailable ? "Available" : "Out of Stock"}
            </span>

            <div class="button-group">
                <button
                    class="btn book-btn"
                    ${!isAvailable ? "disabled" : ""}
                >
                    Book
                </button>

                <button
                    class="btn release-btn"
                    ${!hasBookings ? "disabled" : ""}
                >
                    Release
                </button>
            </div>
        `;

        const buttons = card.querySelectorAll("button");

        buttons[0].addEventListener("click", () => {
            bookEquipment(item.id);
        });

        buttons[1].addEventListener("click", () => {
            releaseEquipment(item.id);
        });

        equipmentList.appendChild(card);
    });
}

// Book equipment
async function bookEquipment(id) {
    try {
        const response = await fetch(`/api/equipment/${id}/book`, {
            method: "POST"
        });

        const result = await response.json();

        showMessage(result.message, response.ok ? "success" : "error");

        await loadEquipment();

    } catch (error) {
        showMessage("Booking failed. Please try again.", "error");
    }
}

// Release equipment
async function releaseEquipment(id) {
    try {
        const response = await fetch(`/api/equipment/${id}/release`, {
            method: "POST"
        });

        const result = await response.json();

        showMessage(result.message, response.ok ? "success" : "error");

        await loadEquipment();

    } catch (error) {
        showMessage("Release failed. Please try again.", "error");
    }
}

// Search equipment
searchInput.addEventListener("input", function () {
    const searchText = this.value.toLowerCase().trim();

    const filteredEquipment = equipmentData.filter(item =>
        item.name.toLowerCase().includes(searchText)
    );

    displayEquipment(filteredEquipment);
});

// Display success and error messages
function showMessage(message, type) {
    messageBox.textContent = message;
    messageBox.className = `message ${type}`;

    setTimeout(() => {
        messageBox.className = "message";
        messageBox.textContent = "";
    }, 3000);
}

// Escape HTML characters before inserting equipment names
function escapeHTML(value) {
    return String(value).replace(/[&<>"']/g, character => ({
        "&": "&amp;",
        "<": "&lt;",
        ">": "&gt;",
        '"': "&quot;",
        "'": "&#39;"
    })[character]);
}

// Fetch application version
async function loadVersion() {
    try {
        const response = await fetch("/api/version");

        if (!response.ok) {
            throw new Error("Version unavailable");
        }

        const data = await response.json();

        document.getElementById("appVersion").textContent =
            data.version;

    } catch (error) {
        document.getElementById("appVersion").textContent =
            "Unavailable";
    }
}

// Initialize dashboard
loadEquipment();
loadVersion();
