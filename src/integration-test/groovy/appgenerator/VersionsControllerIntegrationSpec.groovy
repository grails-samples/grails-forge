package appgenerator

import static org.springframework.http.HttpStatus.OK

import grails.testing.mixin.integration.Integration
import spock.lang.Specification

@Integration
class VersionsControllerIntegrationSpec extends Specification {

    void "test versions"() {
        def resp = get('/versions')

        expect:"The response is correct"
        resp.json.size() > 0
        resp.status == OK.value()
    }

    void "test appData"() {
        when:
        def resp = get('/appData')

        then:"The response is correct"
        resp.json.size() > 0
        resp.status == OK.value()

        when:
        List<String> versions = resp.json.collect { it.version }

        then:
        versions.contains('3.1.13')
        versions.contains('3.1.13')
        versions.contains('3.1.14')
        versions.contains('3.1.15')
        versions.contains('3.1.16')
        versions.contains('3.2.2')
        versions.contains('3.2.3')
        versions.contains('3.2.4')
        versions.contains('3.2.5')
        versions.contains('3.2.6')
        versions.contains('3.2.7')
        versions.contains('3.2.8')
        versions.contains('3.2.9')
        versions.contains('3.2.10')
        versions.contains('3.2.11')
        versions.contains('3.2.12')
        versions.contains('3.3.0')
        versions.contains('3.3.1')
        versions.contains('3.3.2')
    }
}
