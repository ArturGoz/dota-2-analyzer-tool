document.addEventListener("DOMContentLoaded", () => {
    const greetingElement = document.getElementById("greeting");
    const showWinrateElement = document.getElementById("showWinrate");

    // Отримати токен із localStorage
    const token = localStorage.getItem("jwtToken");

    if (!token) {
        const logoutElement = document.getElementById("logout");
        const depositFormElement = document.getElementById("depositForm");

        logoutElement.style.display = "none";
        depositFormElement.style.display = "none";

        // Користувач не авторизований
        greetingElement.textContent = "Будь ласка, авторизуйтесь";
        showWinrateElement.textContent = "Авторизуйтесь, щоб побачити результати";
    } else {
        // Перевірка валідності токена (можна зробити запит до сервера)
        fetch("/get/user-info-jwt", {
            method: "GET",
            headers: {
                "Authorization": token,
                "Content-Type": "application/json",
            },
        })
            .then((response) => {
                if (response.ok) {
                    return response.json();
                } else {
                    throw new Error("Invalid token");
                }
            })
            .then((data) => {
                const username = data.username;
                greetingElement.textContent = `Hi!, ${username}`;
                showWinrateElement.textContent = "Choose heroes and click 'Check winrate' to see winrates of team victory";

                loadUserInfo(data.roles); // Передаємо ролі користувача
            })
            .catch(() => {
                const logoutElement = document.getElementById("logout");
                const depositFormElement = document.getElementById("depositForm");

                logoutElement.style.display = "none";
                depositFormElement.style.display = "none";

                greetingElement.textContent = "Будь ласка, авторизуйтесь";
                showWinrateElement.textContent = "Авторизуйтесь, щоб побачити результати";
            });
    }
});

function loadUserInfo(roles) { // Видалено неправильний тип параметра
    if (roles && roles.includes("ADMIN")) {
        const navList = document.querySelector("header nav ul");
        const adminItem = document.createElement("li");
        const adminLink = document.createElement("a");
        adminLink.href = "/page/getAdmin";
        adminLink.innerHTML = `<i class="fas fa-user-shield"></i> Admin Page`;
        adminItem.appendChild(adminLink);
        navList.appendChild(adminItem);
    }
}
