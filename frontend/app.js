const config = window.TICTACTEST_CONFIG;
const status = document.getElementById("status");

if (!config || !config.consoleUrl || !config.versionUrl) {
	status.textContent = "config.js legt consoleUrl und versionUrl nicht fest.";
} else {
	document.getElementById("terminal").src = config.consoleUrl;
	fetch(config.versionUrl)
		.then(function (response) {
			if (!response.ok) {
				throw new Error(String(response.status));
			}
			return response.text();
		})
		.then(function (text) {
			document.getElementById("backend-version").textContent = text.trim();
		})
		.catch(function () {
			document.getElementById("backend-version").textContent = "nicht erreichbar";
		});
}

fetch("version.txt")
	.then(function (response) {
		if (!response.ok) {
			throw new Error(String(response.status));
		}
		return response.text();
	})
	.then(function (text) {
		document.getElementById("frontend-version").textContent = text.trim();
	})
	.catch(function () {
		document.getElementById("frontend-version").textContent = "unbekannt";
	});
