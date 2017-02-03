package appgenerator

import geb.spock.GebSpec
import grails.plugins.rest.client.RestBuilder
import grails.test.mixin.integration.Integration

import static grails.web.http.HttpHeaders.CONTENT_TYPE
import static org.springframework.http.HttpStatus.OK

@Integration
class IndexControllerIntegrationSpec extends GebSpec {

    void "test index with curl"() {
        def resp = restBuilder().get("$baseUrl/") {
            header("User-Agent", "curl")
        }

        expect:"The response is correct"
        resp.status == OK.value()
        resp.headers[CONTENT_TYPE] == ['text/plain']
        resp.text.startsWith('Grails Application Forge')
    }

    RestBuilder restBuilder() {
        new RestBuilder()
    }
}
