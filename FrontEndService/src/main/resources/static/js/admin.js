document.getElementById("updateButton").addEventListener("click", async function() {
    const jwtToken = localStorage.getItem("jwtToken"); // Отримуємо JWT з локального сховища або змініть на ваш спосіб збереження
    try {
        const response = await fetch("/admin/update-data", {
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