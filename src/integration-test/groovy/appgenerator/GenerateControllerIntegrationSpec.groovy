package appgenerator

import grails.plugins.rest.client.RestBuilder
import grails.testing.mixin.integration.Integration
import spock.lang.Specification

import static grails.web.http.HttpHeaders.CONTENT_TYPE
import static org.springframework.http.HttpStatus.NO_CONTENT
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY

@Integration
class GenerateControllerIntegrationSpec extends Specification {

    void "test validate"() {
        given:
        RestBuilder restBuilder = new RestBuilder()

        when:
        def resp = restBuilder.post("http://localhost:${serverPort}/validate") {
            contentType('application/x-www-form-urlencoded')
            body('name=foo&version=3.2.3')
        }

        then:"The response is correct"
        resp.status == NO_CONTENT.value()
    }

    void "test validate unsupported version"() {
        given:
        RestBuilder restBuilder = new RestBuilder()

        when:
        def resp = restBuilder.post("http://localhost:${serverPort}/validate") {
            contentType('application/x-www-form-urlencoded')
            body('name=foo&version=3.2.0')
        }

        then:"The response is correct"
        resp.status == UNPROCESSABLE_ENTITY.value()
        resp.headers[CONTENT_TYPE] == ['application/json;charset=UTF-8']
        resp.json.version == "The version specified is not supported"
        resp.json.size() == 1
    }

    void "test validate validation errors"() {
        given:
        RestBuilder restBuilder = new RestBuilder()

        when:
        def resp = restBuilder.post("http://localhost:${serverPort}/validate")

        then:"The response is correct"
        resp.status == UNPROCESSABLE_ENTITY.value()
        resp.headers[CONTENT_TYPE] == ['application/json;charset=UTF-8']
        resp.json.version == "You must specify a version for your project"
        resp.json.name == "You must specify a name for your project"
        resp.json.size() == 3
    }
}

