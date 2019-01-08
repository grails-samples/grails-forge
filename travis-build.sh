#!/bin/bash
set -e

export EXIT_STATUS=0

./gradlew -Dgeb.env=chromeHeadless check || EXIT_STATUS=$?

if [[ $EXIT_STATUS ]]; then

    if [[ -n $TRAVIS_TAG ]]; then
        echo "Publishing to PWS"
        ./gradlew --no-daemon  -PcfUsername=$CF_USERNAME -PcfPassword=$CF_PASSWORD cfPush || EXIT_STATUS=$?

    fi
fi
exit $EXIT_STATUS
