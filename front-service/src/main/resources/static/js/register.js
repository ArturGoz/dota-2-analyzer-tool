document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("registration-form");
    const errorMessage = document.querySelector(".error-message");

    form.addEventListener("submit", async (event) => {
    event.preventDefault(); // Prevent default form submission

    // Collect form data
    const formData = {
    name: document.getElementById("name").value,
    email: document.getElementById("email").value,
    password: document.getElementById("password").value,
    confirmPassword: document.getElementById("confirm-password").value,
};

    try {
    // Send POST request to the server
    const response = await fetch("/auth/register", {
    method: "POST",
    headers: {
    "Content-Type": "application/json",
},
    body: JSON.stringify(formData),
});

    // Handle the response
    if (response.ok) {
    alert("User registered successfully! Redirecting to login page...");
    window.location.href = "/page/getLogin"; // Redirect to the login page
} else {
    const errorText = await response.text();
    errorMessage.textContent = errorText; // Display the error message
    errorMessage.style.display = "block";
}
} catch (error) {
    console.error("Error occurred during registration:", error);
    errorMessage.textContent = "An error occurred. Please try again.";
    errorMessage.style.display = "block";
}
});
});
