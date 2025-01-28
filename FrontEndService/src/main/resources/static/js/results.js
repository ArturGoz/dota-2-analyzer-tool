
// Функція для отримання списку турнірів з сервера
async function fetchTournaments() {
    try {
        const response = await fetch('/tournament/getList');
        const tournaments = await response.json();
        populateTournaments(tournaments);
    } catch (error) {
        console.error('Помилка при завантаженні турнірів:', error);
    }
}

// Функція для додавання турнірів до списку
function populateTournaments(tournaments) {
    const selectElement = document.getElementById('tournamentName');
    tournaments.forEach(tournament => {
        const option = document.createElement('option');
        option.value = tournament;
        option.textContent = tournament;
        selectElement.appendChild(option);
    });
}

// Виклик функції для отримання та додавання турнірів до списку
fetchTournaments();