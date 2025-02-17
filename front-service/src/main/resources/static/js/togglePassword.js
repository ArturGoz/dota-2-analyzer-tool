
    document.addEventListener("DOMContentLoaded", () => {
        const passwordInput = document.getElementById("password");
        const toggleButton = document.getElementById("togglePassword");

        toggleButton.addEventListener("click", () => {
            const type = passwordInput.type === "password" ? "text" : "password";
            passwordInput.type = type;
            toggleButton.textContent = type === "password" ? "Show" : "Hide";
        });
    });
