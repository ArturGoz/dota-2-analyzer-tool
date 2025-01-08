document.addEventListener('DOMContentLoaded', function () {
    const token = localStorage.getItem('jwtToken');
    if (token) {
        // Додаємо токен до заголовка кожного запиту
        axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
    } else {
        console.warn("JWT Token not found in localStorage.");
    }
});
