document.getElementById("changePasswordForm").addEventListener("submit", async function (event) {
    event.preventDefault();

    const oldPassword = document.getElementById("oldPassword").value;
    const newPassword = document.getElementById("newPassword").value;

    const changePasswordData = {
        oldPassword: oldPassword,
        newPassword: newPassword,
    };

    try {
        const response = await fetch("/profile/passwordChange", {
            method: "PATCH",
            headers: {
                "Content-Type": "application/json",
                "X-User-Name": "exampleUsername", // Replace with actual username if needed
            },
            body: JSON.stringify(changePasswordData),
        });

        const message = await response.text();

        const messageElement = document.getElementById("changePasswordMessage");
        if (response.ok) {
            messageElement.style.color = "green";
        } else {
            messageElement.style.color = "red";
        }
        messageElement.textContent = message;
    } catch (error) {
        console.error("Error changing password:", error);
        document.getElementById("changePasswordMessage").textContent =
            "An error occurred while changing the password.";
    }
});
