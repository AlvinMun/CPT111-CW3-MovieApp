let movies = [];
let currentUser = {
    username: "alice",
    password: "alice123",
    watchlist: ["M008", "M015"],
    history: ["M001", "M011"]
};

async function loadMovies() {
    const res = await fetch('data/movies.json');
    movies = await res.json();
}

function findMovieById(id) {
    return movies.find(m => m.id === id);
}

function init() {
    const loginBtn = document.getElementById('login-btn');
    loginBtn.addEventListener('click', handleLogin);

    document.getElementById('browse-btn').addEventListener('click', browseMovies);
    document.getElementById('watchlist-btn').addEventListener('click', viewWatchlist);
    document.getElementById('history-btn').addEventListener('click', viewHistory);
    document.getElementById('recommend-btn').addEventListener('click', showRecommendations);
    document.getElementById('logout-btn').addEventListener('click', logout);

    loadMovies();
}

function handleLogin() {
    const username = document.getElementById('username').value.trim();
    const password = document.getElementById('password').value.trim();
    const msg = document.getElementById('login-message');

    if (username === currentUser.username && password === currentUser.password) {
        document.getElementById('login-screen').style.display = 'none';
        document.getElementById('main-screen').style.display = 'block';
        document.getElementById('welcome').textContent = `Welcome, ${currentUser.username}`;   
    } else {
        msg.textContent = "Invalid username or password.";
    }
}

function browseMovies() {
    const content = document.getElementById('content');
    content.innerHTML = "<h3>All Movies</h3>";

    let html = "<ul>";
    for (const m of movies) {
        html += `<li>[${m.id}] ${m.title} (${m.year}) - ${m.genre} - ${m.rating}</li>`;
    }
    html += "</ul>";

    content.innerHTML += html;
}

function viewWatchlist() {
    const content = document.getElementById('content');
    content.innerHTML = "<h3>Your Watchlist</h3>";

    if (currentUser.watchlist.length === 0) {
        content.innerHTML += "<p>Your watchlist is empty.</p>";
        return;
    }

    let html = "<ul>";
    for (const id of currentUser.watchlist) {
        const m = findMovieById(id);
        if (m) {
            html += `<li>[${m.id}] ${m.title} (${m.year}) - ${m.genre} - ${m.rating}</li>`;
        } else {
            html += `<li>(Unknown movie ID: ${id})</li>`
        }
    }
    html += "</ul>";

    content.innerHTML += html;
}

function viewHistory() {
    const content = document.getElementById('content');
    content.innerHTML = "<h3>Your Viewing History</h3>";

    if (currentUser.history.length === 0) {
        content.innerHTML += "<p>You have not watched any movies yet.</p>";
        return;
    }

    let html = "<ul>";
    for (const id of currentUser.history) {
        const m = findMovieById(id);
        if (m) {
            html += `<li>[${m.id}] ${m.title} (${m.year}) - ${m.genre} - ${m.rating}</li>`;
        } else {
            html += `<li>(Unknown movie ID: ${id})</li>`;
        }
    }
    html += "</ul>";

    content.innerHTML += html;
}

function showRecommendations() {
    const content = document.getElementById('content');
    content.innerHTML = "<h3>Recommended Movies</h3>";

    if (movies.length === 0) {
        content.innerHTML += "<p>No movies loaded yet.</p>";
        return;
    }

    let nStr = prompt("How many recommendations would you like?", "5");
    if (nStr === null) {
        return; //cancelled
    }
    const n = parseInt(nStr, 10);
    if (isNaN(n) || n <= 0) {
        content.innerHTML += "<p>Please enter a valid positive number.</p>";
        return;
    }

    const watchedSet = new Set(currentUser.history);
    const watchlistSet = new Set(currentUser.watchlist);

    const candidates = movies.filter(m => !watchedSet.has(m.id));

    candidates.sort((a, b) => b.rating - a.rating);

    const recs = candidates.slice(0, n);

    if (recs.length === 0) {
        content.innerHTML += "<p>No recommendations available. Try adding more movies!</p>";
        return;
    }

    let html = "<ul>";
    for (const m of recs) {
        html += `<li>[${m.id}] ${m.title} (${m.year}) - ${m.genre} - ${m.rating}</li>`;
    }
    html += "</ul>";

    content.innerHTML += html;
}

function logout() {
    document.getElementById('main-screen').style.display = 'none';
    document.getElementById('login-screen').style.display = 'block';
}

window.addEventListener('DOMContentLoaded', init);