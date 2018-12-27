package appgenerator

import static grails.web.http.HttpHeaders.CONTENT_TYPE
import static org.springframework.http.HttpStatus.OK

import grails.plugins.rest.client.RestResponse
import grails.testing.mixin.integration.Integration
import spock.lang.Specification

@Integration
class IndexControllerIntegrationSpec extends Specification implements RestSpec {

    void "test index with curl"() {
        RestResponse resp = get('/') {
            header("User-Agent", "curl")
        }

        expect:"The response is correct"
        resp.status == OK.value()
        resp.headers[CONTENT_TYPE] == ['text/plain']
        resp.text.startsWith('Grails Application Forge')
    }
}
