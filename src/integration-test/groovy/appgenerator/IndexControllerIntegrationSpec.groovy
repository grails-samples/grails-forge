package appgenerator

import grails.plugins.rest.client.RestBuilder
import grails.plugins.rest.client.RestResponse
import grails.testing.mixin.integration.Integration
import spock.lang.Specification

import static grails.web.http.HttpHeaders.CONTENT_TYPE
import static org.springframework.http.HttpStatus.OK

@Integration
class IndexControllerIntegrationSpec extends Specification {

    void "test index with curl"() {
        given:
        RestBuilder restBuilder = new RestBuilder()

        when:
        RestResponse resp = restBuilder.get("http://localhost:${serverPort}/") {
            header("User-Agent", "curl")
        }

        then: 'The response is correct'
        resp.status == OK.value()
        resp.headers[CONTENT_TYPE] == ['text/plain']
        resp.text.startsWith('Grails Application Forge')
    }
}
