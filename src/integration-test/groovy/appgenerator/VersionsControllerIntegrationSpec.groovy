package appgenerator

import grails.testing.spock.OnceBefore
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import spock.lang.Shared

import grails.testing.mixin.integration.Integration
import spock.lang.Specification

@Integration
class VersionsControllerIntegrationSpec extends Specification {


    @Shared HttpClient client

    @OnceBefore
    void init() {
        String baseUrl = "http://localhost:${serverPort}"
        this.client  = HttpClient.create(baseUrl.toURL())
    }

    void "test versions"() {
        HttpResponse<List<String>> resp = client.toBlocking().exchange(HttpRequest.GET("/versions"), Argument.of(List, String))
        def result = resp.body()

        expect:"The response is correct"
        resp.status == HttpStatus.OK
        result.size() > 0
    }

    void "test appData"() {
        when:
        HttpResponse<Map> resp = client.toBlocking().exchange(HttpRequest.GET("/appData"), Map)
        def result = resp.body()

        then:"The response is correct"
        resp.status == HttpStatus.OK
        result.size() > 0

        when:
        List<String> versions = result.collect { it.version }

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
