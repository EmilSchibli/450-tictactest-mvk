"""Render terminal output as a PNG screenshot for test documentation."""
from __future__ import annotations

import sys
from pathlib import Path

from PIL import Image, ImageDraw, ImageFont


def render_terminal_screenshot(text: str, output_path: Path, title: str) -> None:
    lines = [title, "=" * len(title), ""] + text.strip().splitlines()
    font = ImageFont.load_default()
    padding = 16
    line_height = 18
    width = max(font.getlength(line) for line in lines) + padding * 2
    height = len(lines) * line_height + padding * 2
    image = Image.new("RGB", (int(width), int(height)), "#1e1e1e")
    draw = ImageDraw.Draw(image)
    y = padding
    for index, line in enumerate(lines):
        color = "#4ec9b0" if index == 0 else "#d4d4d4"
        if "BUILD SUCCESSFUL" in line:
            color = "#6a9955"
        if "BUILD FAILED" in line or "FAILED" in line or "AssertionError" in line:
            color = "#f44747"
        draw.text((padding, y), line, fill=color, font=font)
        y += line_height
    output_path.parent.mkdir(parents=True, exist_ok=True)
    image.save(output_path)


if __name__ == "__main__":
    if len(sys.argv) != 4:
        raise SystemExit("usage: render_test_screenshot.py <input.txt> <output.png> <title>")
    input_path = Path(sys.argv[1])
    output_path = Path(sys.argv[2])
    title = sys.argv[3]
    render_terminal_screenshot(input_path.read_text(encoding="utf-8"), output_path, title)
