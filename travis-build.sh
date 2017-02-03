#!/bin/bash
set -e
rm -rf *.zip
./gradlew clean check
