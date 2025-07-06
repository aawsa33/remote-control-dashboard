
const WebSocket = require('ws');
const wss = new WebSocket.Server({ port: 8080 });

let clients = {};

wss.on('connection', (ws, req) => {
    const urlParams = new URLSearchParams(req.url.replace('/', ''));
    const deviceId = urlParams.get('id') || 'unknown';
    clients[deviceId] = ws;
    console.log(`Device connected: ${deviceId}`);

    ws.on('message', (msg) => {
        console.log(`From ${deviceId}: ${msg}`);
    });

    ws.on('close', () => {
        delete clients[deviceId];
        console.log(`Device disconnected: ${deviceId}`);
    });
});

// Command sender example:
function sendCommand(deviceId, command) {
    if (clients[deviceId]) {
        clients[deviceId].send(JSON.stringify(command));
    }
}

module.exports = { sendCommand };
