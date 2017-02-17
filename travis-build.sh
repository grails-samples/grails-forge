#!/bin/bash
set -e

export EXIT_STATUS=0

rm -rf *.zip

./gradlew clean check || EXIT_STATUS=$?

if [[ $EXIT_STATUS ]]; then

    if [[ ( $TRAVIS_BRANCH == master || $TRAVIS_TAG == prod_* ) && $TRAVIS_PULL_REQUEST == 'false' ]]; then

        echo "Publishing to PWS"

        ./gradlew -PcfUsername=$CF_USERNAME -PcfPassword=$CF_PASSWORD assemble cfPush || EXIT_STATUS=$i

    fi
fi
exit $EXIT_STATUS
