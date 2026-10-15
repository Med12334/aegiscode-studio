const { app, BrowserWindow, ipcMain } = require('electron');
const path = require('path');

function createWindowsWorkstationShell() {
  const win = new BrowserWindow({
    width: 1600,
    height: 960,
    title: 'AegisCode Studio // Polyglot AI Workstation & Mistake Memory Ledger',
    backgroundColor: '#0d1117',
    webPreferences: {
      nodeIntegration: true,
      contextIsolation: false
    }
  });

  win.loadURL('http://localhost:4200');
}

app.whenReady().then(() => {
  createWindowsWorkstationShell();
});

app.on('window-all-closed', () => {
  if (process.platform !== 'darwin') {
    app.quit();
  }
});
