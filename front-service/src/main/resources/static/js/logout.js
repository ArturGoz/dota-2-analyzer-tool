document.getElementById("logout").addEventListener("click", function() {
    localStorage.removeItem("jwtToken"); // Remove the token from localStorage after successful logout
});
