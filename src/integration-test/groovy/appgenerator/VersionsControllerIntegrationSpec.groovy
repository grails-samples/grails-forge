package appgenerator

import grails.test.mixin.integration.Integration
import spock.lang.Specification
import static org.springframework.http.HttpStatus.OK

/**
 * Created by jameskleeh on 6/14/17.
 */
@Integration
class VersionsControllerIntegrationSpec extends Specification implements RestSpec {

    void "test versions"() {
        def resp = restBuilder().get("$baseUrl/versions")

        expect:"The response is correct"
        resp.json.size() > 0
        resp.status == OK.value()
    }

    void "test appData"() {
        def resp = restBuilder().get("$baseUrl/appData")

        expect:"The response is correct"
        resp.json.size() > 0
        resp.status == OK.value()
        resp.json[0].version
        resp.json[0].profiles.size() > 0
    }

}
