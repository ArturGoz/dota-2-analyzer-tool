// Функція для отримання списку турнірів з сервера
async function fetchTournaments() {
    try {
        const response = await fetch('/tournament/getList');
        const data = await response.json();
        if (!data.succeeded) {
            throw new Error(data.statusMessage);
        } populateTournaments(data.results[0]);
    } catch (error) {
        console.error('Error fetching tournaments:', error);
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


function submitTournamentName() {
    const tournamentName = document.getElementById("tournamentName").value;

    fetch(`/tournament/getResults?tournamentName=${(tournamentName)}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
        }
    })
        .then(response => response.json())
        .then(data => {
            if (!data.succeeded) {
                throw new Error(data.statusMessage);
            }
            const tournament = data.results[0];

            const resultsContainer = document.getElementById("results");
            resultsContainer.innerHTML = ''; // Очистити попередні результати

            if (tournament.length > 0) {
                resultsContainer.innerHTML = `
                        <h2>Результати для турніру: ${tournamentName}</h2>
                        <table>
                            <thead>
                                <tr>
                                    <th>Команда Победителя</th>
                                    <th>Команда Лузера</th>
                                    <th>Винрейт Победителя</th>
                                    <th>Герои без статистики в игре</th>
                                    <th>Драфт Победителя</th>
                                    <th>Драфт Лузера</th>
                                    <th></th>
                                </tr>
                            </thead>
                            <tbody>
                                ${tournament.map(result => `
                                    <tr>
                                        <td>${result.teamWinnerName}</td>
                                        <td>${result.teamLoserName}</td>
                                        <td>${result.teamWinnerWinrate}</td>
                                        <td>${result.emptyLineUps}</td>
                                        <td>
                                            <ul>
                                                ${result.teamHeroesWinner.map(hero => `<li>${hero}</li>`).join('')}
                                            </ul>
                                        </td>
                                        <td>
                                            <ul>
                                                ${result.teamHeroesLoser.map(hero => `<li>${hero}</li>`).join('')}
                                            </ul>
                                        </td>
                                    </tr>
                                `).join('')}
                            </tbody>
                        </table>
                    `;
            } else {
                resultsContainer.innerHTML = '<p>Немає результатів для цього турніру.</p>';
            }
        })
        .catch(error => console.error('Error:', error));
}
