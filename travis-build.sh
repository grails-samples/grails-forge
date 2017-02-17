#!/bin/bash
set -e
rm -rf *.zip

./gradlew clean check || EXIT_STATUS=$?

if [[ $EXIT_STATUS ]]; then

    if [[ ( $TRAVIS_BRANCH == master || $TRAVIS_TAG == prod_* ) && $TRAVIS_PULL_REQUEST == 'false' ]]; then

        echo "Publishing to PWS"

        ./grailsw war || EXIT_STATUS=$?

        if [[ $EXIT_STATUS ]]; then
            ./gradlew -PcfUsername=$CF_USERNAME -PcfPassword=$CF_PASSWORD clean assemble cfPush || EXIT_STATUS=$i
        fi

    fi
fi
exit $EXIT_STATUS
