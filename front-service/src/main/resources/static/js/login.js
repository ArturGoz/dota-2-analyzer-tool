document.getElementById('loginForm').addEventListener('submit', async function (e) {
    e.preventDefault(); // Зупиняє стандартне відправлення форми

    // Отримання даних з форми
    const name = document.getElementById('name').value;
    const password = document.getElementById('password').value;

    try {
    // Відправка POST-запиту до REST API
    const response = await fetch('/auth/login', {
    method: 'POST',
    headers: {
    'Content-Type': 'application/json',
},
    body: JSON.stringify({ name, password }),
});

    // Обробка відповіді
    const messageDiv = document.getElementById('message');
    if (response.ok) {
    // Отримання JWT токена з тіла відповіді
    const jwtResponse = await response.json();
    const token = jwtResponse.token;

    if (token) {
    // Додавання префіксу Bearer та збереження токена у Local Storage
    localStorage.setItem('jwtToken', `Bearer ${token}`);

    messageDiv.style.color = 'green';
    messageDiv.textContent = 'Login successful! Redirecting...';

    // Перенаправлення на іншу сторінку
    setTimeout(() => {
    window.location.href = '/page/getAnalyze';
}, 1000);
} else {
    messageDiv.style.color = 'red';
    messageDiv.textContent = 'Login successful, but no token received.';
}
} else {
    const errorText = await response.text();
    messageDiv.style.color = 'red';
    messageDiv.textContent = errorText; // Вивід повідомлення про помилку
}
} catch (error) {
    // Виведення помилки у разі проблем із запитом
    const messageDiv = document.getElementById('message');
    messageDiv.style.color = 'red';
    messageDiv.textContent = 'Error: Unable to login';
}
});

