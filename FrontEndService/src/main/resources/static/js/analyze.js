document.addEventListener("DOMContentLoaded", () => {
    const greetingElement = document.getElementById("greeting");
    const showWinrateElement = document.getElementById("showWinrate");

    // Отримати токен із localStorage
    const token = localStorage.getItem("jwtToken");

    if (!token) {
        // Користувач не авторизований
        greetingElement.textContent = "Будь ласка, авторизуйтесь";
        showWinrateElement.textContent = "Авторизуйтесь, щоб побачити результати";
    } else {
        // Перевірка валідності токена (можна зробити запит до сервера)
        fetch("/getUserFromToken", {
            method: "POST",
            headers: {
                "Authorization": token,
                "Content-Type": "application/json",
            },
        })
            .then((response) => {
                if (response.ok) {
                    // Токен валідний
                    return response.json();
                } else {
                    throw new Error("Invalid token");
                }
            })
            .then((data) => {
                // Користувач авторизований
                const username = data.username;
                greetingElement.textContent = `Hi!, ${username}`;
                showWinrateElement.textContent = "Choose heroes and click 'Check winrate' to see winrates of team victory";
            })
            .catch(() => {
                // Якщо токен недійсний
                greetingElement.textContent = "Будь ласка, авторизуйтесь";
                showWinrateElement.textContent = "Авторизуйтесь, щоб побачити результати";
            });
    }
});
