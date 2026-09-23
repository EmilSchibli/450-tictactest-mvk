#!/usr/bin/env bash
set -euo pipefail

xml="${1:-build/reports/jacoco/test/jacocoTestReport.xml}"

line=$(grep -o '<counter type="LINE"[^>]*>' "$xml" | tail -1)
covered=$(echo "$line" | grep -o 'covered="[0-9]*"' | grep -o '[0-9]*')
missed=$(echo "$line" | grep -o 'missed="[0-9]*"' | grep -o '[0-9]*')

awk "BEGIN { printf \"%.1f\", 100 * $covered / ($covered + $missed) }"
