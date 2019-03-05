package appgenerator

import grails.testing.spock.OnceBefore
import io.micronaut.http.HttpHeaders
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.MediaType
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.exceptions.HttpClientResponseException
import spock.lang.Shared

import grails.testing.mixin.integration.Integration
import spock.lang.Specification

@Integration
class GenerateControllerIntegrationSpec extends Specification {

    @Shared HttpClient client

    @OnceBefore
    void init() {
        String baseUrl = "http://localhost:${serverPort}"
        this.client  = HttpClient.create(baseUrl.toURL())
    }

    void "test validate"() {
        HttpResponse resp = client.toBlocking()
                .exchange(HttpRequest.POST("/validate", "name=foo&version=3.2.3")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_TYPE))

        expect:"The response is correct"
        resp.status == HttpStatus.NO_CONTENT
    }

    void "test validate unsupported version"() {
        when:
        client.toBlocking()
                .exchange(HttpRequest.POST("/validate", "name=foo&version=3.2.0")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_TYPE), Map)

        then:"The response is correct"

        def e = thrown(HttpClientResponseException)
        e.response.status == HttpStatus.UNPROCESSABLE_ENTITY
        e.response.header(HttpHeaders.CONTENT_TYPE) == "application/json;charset=UTF-8"

        when:
        def result = e.response.body()

        then:
        result.version == "The version specified is not supported"
        result.size() == 1
    }

    void "test validate validation errors"() {
        when:
        client.toBlocking()
                .exchange(HttpRequest.POST("/validate", ""), Map)

        then:"The response is correct"

        def e = thrown(HttpClientResponseException)

        e.response.status == HttpStatus.UNPROCESSABLE_ENTITY
        e.response.header(HttpHeaders.CONTENT_TYPE) == "application/json;charset=UTF-8"

        when:
        def result = e.response.body()

        then:
        result.version == "You must specify a version for your project"
        result.name == "You must specify a name for your project"
        result.size() == 3

    }
}
