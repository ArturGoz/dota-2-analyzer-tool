const dota2Heroes = [
    "Anti-Mage", "Axe", "Bane", "Bloodseeker", "Crystal Maiden",
    "Drow Ranger", "Earthshaker", "Juggernaut", "Mirana", "Morphling",
    "Shadow Fiend", "Phantom Lancer", "Puck", "Pudge", "Razor",
    "Sand King", "Storm Spirit", "Sven", "Tiny", "Vengeful Spirit",
    "Windranger", "Zeus", "Kunkka", "Lina", "Lion",
    "Shadow Shaman", "Slardar", "Tidehunter", "Witch Doctor", "Lich",
    "Riki", "Enigma", "Tinker", "Sniper", "Necrophos",
    "Warlock", "Beastmaster", "Queen of Pain", "Venomancer", "Faceless Void",
    "Wraith King", "Death Prophet", "Phantom Assassin", "Pugna", "Templar Assassin",
    "Viper", "Luna", "Dragon Knight", "Dazzle", "Clockwerk",
    "Leshrac", "Nature's Prophet", "Lifestealer", "Dark Seer", "Clinkz",
    "Omniknight", "Enchantress", "Huskar", "Night Stalker", "Broodmother",
    "Bounty Hunter", "Weaver", "Jakiro", "Batrider", "Chen",
    "Spectre", "Ringmaster", "Ancient Apparition", "Doom", "Ursa", "Spirit Breaker",
    "Gyrocopter", "Alchemist", "Invoker", "Silencer", "Outworld Devourer",
    "Lycan", "Brewmaster", "Shadow Demon", "Lone Druid", "Chaos Knight",
    "Meepo", "Treant Protector", "Ogre Magi", "Undying", "Rubick",
    "Disruptor", "Nyx Assassin", "Naga Siren", "Keeper of the Light", "Io",
    "Visage", "Slark", "Medusa", "Troll Warlord", "Centaur Warrunner",
    "Magnus", "Timbersaw", "Bristleback", "Tusk", "Skywrath Mage",
    "Abaddon", "Elder Titan", "Legion Commander", "Techies", "Ember Spirit",
    "Earth Spirit", "Underlord", "Terrorblade", "Phoenix", "Oracle",
    "Winter Wyvern", "Arc Warden", "Monkey King", "Pangolier", "Dark Willow",
    "Grimstroke", "Mars", "Snapfire", "Void Spirit", "Hoodwink",
    "Dawnbreaker", "Marci", "Primal Beast", "Muerta", "Kez"
];


for (let i = 1; i <= 10; i++) {
    const comboBox = document.getElementById('comboBox' + i); // Access the combo box element by ID

    const emptyOption = document.createElement('option'); // Create an empty option element
    emptyOption.value = ""; // Set the value of the empty option
    emptyOption.text = "Hero";  // Set the displayed text of the empty option
    comboBox.appendChild(emptyOption); // Append the empty option to the combo box

    dota2Heroes.forEach(hero => { // Loop through the heroes array
        const option = document.createElement('option'); // Create a new option element
        option.value = hero; // Set the value of the option
        option.text = hero;  // Set the displayed text of the option
        comboBox.appendChild(option); // Append the option to the combo box
    });
}


document.getElementById("check-winrate").addEventListener("click", function () {
    const heroes = [];
    for (let i = 1; i <= 10; i++) {
        const hero = document.getElementById(`comboBox${i}`).value;
        const heroLowerCase = hero.toLowerCase();
        const imgElement = document.getElementById(`heroImg${i}`); // Отримуємо елемент <img> за його id

        if (hero != "") {
            document.querySelectorAll(`.pos${i}`).forEach(img => {
                img.src = `/miniheroes/${heroLowerCase}.png`;
            });
        } else {
            document.querySelectorAll(`.pos${i}`).forEach(img => {
                if (i > 5) {
                    img.src = `/roles/pos_${i - 5}.png`;
                } else {
                    img.src = `/roles/pos_${i}.png`;
                }
            });
        }

        // Змінюємо атрибут src зображення на основі вибраного героя
        imgElement.src = `/images/${heroLowerCase}_lg.png`;
        heroes.push(hero);
    }


    fetch('/stats/game', {
        method: 'POST',
        headers: {
            "Authorization": localStorage.getItem("jwtToken"),
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(heroes)
    })
        .then(async response => {
            // Спроба отримати JSON або текст для обробки помилок
            let errorData;
            try {
                errorData = await response.clone().json();
            } catch {
                errorData = await response.text();
            }

            if (!response.ok) {
                // Обробка 401 Unauthorized
                if (response.status === 401) {
                    localStorage.removeItem("jwtToken");
                    window.location.href = "/page/getLogin"; // Перенаправлення на логін
                    return; // Припинити виконання
                }

                // Обробка LIMIT_EXCEEDED
                if (typeof errorData === 'object' && errorData.statusMessage?.includes("LIMIT_EXCEEDED")) {
                    return { radiantWinrate: "no limit", direWinrate: "no limit", Stats: {} };
                }

                // Генерувати помилку з текстом або об'єктом
                throw new Error(
                    typeof errorData === 'object'
                        ? errorData.statusMessage || "Unknown error"
                        : errorData
                );
            }

            return response.json();
        })
        .then(data => {
            if (!data) return;

            const statsData = data.results ? data.results[0] : data;

            if (statsData.radiantWinrate === "no limit" && statsData.direWinrate === "no limit") {
                document.getElementById("radiant-winrate").textContent = "no limit";
                document.getElementById("dire-winrate").textContent = "no limit";

                // Очищаємо статистику
                for (let j = 1; j <= 5; j++) {
                    for (let i = 1; i <= 5; i++) {
                        document.getElementById(`winrate${j}${i}`).textContent = "N/A";
                        document.getElementById(`matches${j}${i}`).textContent = "N/A";
                        document.getElementById(`winrate${i + 5}${j}`).textContent = "N/A";
                        document.getElementById(`matches${i + 5}${j}`).textContent = "N/A";
                    }
                }
                return;
            }

            document.getElementById("radiant-winrate").textContent = statsData.radiantWinrate + "%";
            document.getElementById("dire-winrate").textContent = statsData.direWinrate + "%";
            document.getElementById("emptyLineUps").textContent = statsData.emptyLineUps;

            // Оновлення статистики для героїв
            for (let j = 1; j <= 5; j++) {
                const hero1 = heroes[j - 1];
                for (let i = 1; i <= 5; i++) {
                    const enemyHero = heroes[i + 4];
                    const statKey = `${hero1}_vs_${enemyHero}`;
                    const stats = statsData.Stats[statKey];

                    const winrateElementId = `winrate${j}${i}`;
                    const matchesElementId = `matches${j}${i}`;

                    if (stats && stats.winrate !== -1 && stats.matches !== -1) {
                        document.getElementById(winrateElementId).textContent = stats.winrate + "%";
                        document.getElementById(matchesElementId).textContent = stats.matches;

                        document.getElementById(`winrate${i + 5}${j}`).textContent = (100.0 - stats.winrate) + "%";
                        document.getElementById(`matches${i + 5}${j}`).textContent = stats.matches;
                    } else {
                        document.getElementById(winrateElementId).textContent = "N/A";
                        document.getElementById(matchesElementId).textContent = "N/A";

                        document.getElementById(`winrate${i + 5}${j}`).textContent = "N/A";
                        document.getElementById(`matches${i + 5}${j}`).textContent = "N/A";
                    }
                }
            }
        })
        .catch(error => {
            console.error('Error:', error.message);
        });


});

const depositButton = document.getElementById('depositButton');
const depositForm = document.getElementById('depositForm');
const closeForm = document.getElementById('closeForm');

depositButton.addEventListener('click', (e) => {
    e.preventDefault();
    depositForm.style.display = 'block';
});

closeForm.addEventListener('click', () => {
    depositForm.style.display = 'none';
});








