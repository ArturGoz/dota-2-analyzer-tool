document.getElementById("updateButton").addEventListener("click", async function () {
    const jwtToken = localStorage.getItem("jwtToken"); // Отримуємо JWT з локального сховища або змініть на ваш спосіб збереження
    try {
        const response = await fetch("/stats/update-data", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `${jwtToken}` // Додаємо токен у заголовок
            }
        });

        const result = await response.text();
        alert(result);
    } catch (error) {
        console.error("Error:", error);
        alert("Failed to update data");
    }
});


document.getElementById("addButton").addEventListener("click", async function () {
    const jwtToken = localStorage.getItem("jwtToken"); // Отримуємо JWT з локального сховища або змініть на ваш спосіб збереження
    try {
        const response = await fetch("/stats/add-data", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `${jwtToken}` // Додаємо токен у заголовок
            }
        });

        const result = await response.text();
        alert(result);
    } catch (error) {
        console.error("Error:", error);
        alert("Failed to update data");
    }
});


document.getElementById("add-tournament").addEventListener("submit", function (event) {
    event.preventDefault();

    const tournamentInfo = {
        tournament: {
            name: document.getElementById("tournamentName").value,
            division: document.getElementById("division").value
        },
        tournamentUrl: document.getElementById("tournamentUrl").value
    };
    const jwtToken = localStorage.getItem("jwtToken"); // Отримуємо JWT з локального сховища або змініть на ваш спосіб збереження
    fetch("/tournament/add", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `${jwtToken}`
        },
        body: JSON.stringify(tournamentInfo)
    })
        .then(response => response.text())
        .then(data => alert(data))
        .catch(error => console.error("Error:", error));
});
