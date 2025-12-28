const canvas = document.getElementById('background');
const ctx = canvas.getContext('2d');

// Make canvas full screen
canvas.width = window.innerWidth;
canvas.height = window.innerHeight;

// Binary characters
const characters = ['0', '1'];

// Font size and number of columns
const fontSize = 16;
const columns = Math.floor(canvas.width / fontSize);

// Track y-position for each column
const drops = new Array(columns).fill(1);

function draw() {
    // Slightly transparent black rectangle to fade trails
    ctx.fillStyle = 'rgba(0, 0, 0, 0.05)';
    ctx.fillRect(0, 0, canvas.width, canvas.height);

    // Matrix-green text
    ctx.fillStyle = 'white';
    ctx.font = fontSize + 'px monospace';

    for (let i = 0; i < drops.length; i++) {
        const text = characters[Math.floor(Math.random() * characters.length)];
        const x = i * fontSize;
        const y = drops[i] * fontSize;

        ctx.fillText(text, x, y);

        // Random reset so the drops vary in length
        if (y > canvas.height && Math.random() > 0.975) {
            drops[i] = 0;
        }

        drops[i]++;
    }
}

let interval;

// fast at the beginning
function startFast() {
    interval = setInterval(draw, 15);   // ⚡ warp speed
}

// later, slow it down
function switchToSlow() {
    clearInterval(interval);
    interval = setInterval(draw, 30);   // 🌿 chill mode
}

startFast();

// change speed after 3 seconds (3000 ms)
setTimeout(switchToSlow, 1000);
// Handle window resize
window.addEventListener('resize', () => {
    canvas.width = window.innerWidth;
    canvas.height = window.innerHeight;
});


