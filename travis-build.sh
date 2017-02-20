#!/bin/bash
set -e

export EXIT_STATUS=0

rm -rf *.zip

./gradlew clean check || EXIT_STATUS=$?

if [[ $EXIT_STATUS ]]; then

    if [[ -n $TRAVIS_TAG ]] || [[ $TRAVIS_BRANCH == 'master' && $TRAVIS_PULL_REQUEST == 'false' ]]; then

        if [[ -n $TRAVIS_TAG ]]; then

            echo "Publishing to PWS"

            ./gradlew -PcfUsername=$CF_USERNAME -PcfPassword=$CF_PASSWORD assemble cfPush || EXIT_STATUS=$i
        fi
    fi
fi
exit $EXIT_STATUS
