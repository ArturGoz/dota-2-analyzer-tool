
    // Function to fetch user info from the endpoint
    async function fetchUserInfo() {
    try {
    const token = localStorage.getItem('jwtToken'); // Retrieve the JWT token from localStorage
    if (!token) {
    console.error('JWT token not found. Please log in.');
    return;
}

    const response = await fetch('/profile/getUserInfo', {
    headers: {
    'Authorization': token
}
});

    if (response.ok) {
    const user = await response.json();

    // Populate the HTML elements with data from the JSON response
    document.getElementById('userName').textContent = user.name || 'N/A';
    document.getElementById('userEmail').textContent = user.email || 'N/A';
    document.getElementById('userRole').textContent = user.role || 'N/A';
} else {
    console.error('Failed to fetch user info:', response.status);
}
} catch (error) {
    console.error('Error fetching user info:', error);
}
}

    // Call the function when the page loads
    window.onload = fetchUserInfo;
