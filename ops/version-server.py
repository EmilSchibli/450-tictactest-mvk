#!/usr/bin/env python3
"""Serves the running release version as plain text. The game jar is unchanged.

The deploy workflow reads GET /version after a backend release.
"""

import os
from http.server import BaseHTTPRequestHandler, ThreadingHTTPServer

VERSION = os.environ["TICTACTEST_VERSION"]
HOST = os.environ.get("TICTACTEST_BIND", "127.0.0.1")
PORT = int(os.environ.get("TICTACTEST_VERSION_PORT", "7682"))


class Handler(BaseHTTPRequestHandler):
	def do_GET(self):
		if self.path.split("?", 1)[0] != "/version":
			self.send_error(404)
			return
		body = (VERSION + "\n").encode("utf-8")
		self.send_response(200)
		self.send_header("Content-Type", "text/plain; charset=utf-8")
		self.send_header("Access-Control-Allow-Origin", "*")
		self.send_header("Content-Length", str(len(body)))
		self.end_headers()
		self.wfile.write(body)

	def log_message(self, fmt, *args):
		print("%s - %s" % (self.address_string(), fmt % args))


if __name__ == "__main__":
	ThreadingHTTPServer((HOST, PORT), Handler).serve_forever()
