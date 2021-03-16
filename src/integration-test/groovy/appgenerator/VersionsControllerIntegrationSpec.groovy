package appgenerator

import grails.testing.spock.OnceBefore
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.DefaultHttpClient
import io.micronaut.http.client.DefaultHttpClientConfiguration
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.HttpClientConfiguration
import spock.lang.Shared

import grails.testing.mixin.integration.Integration
import spock.lang.Specification

import java.time.Duration

@Integration
class VersionsControllerIntegrationSpec extends Specification {


    @Shared HttpClient client

    @OnceBefore
    void init() {
        String baseUrl = "http://localhost:${serverPort}"
        HttpClientConfiguration config = new DefaultHttpClientConfiguration()
        config.readTimeout = Duration.ofSeconds(120)

        this.client  = new DefaultHttpClient(baseUrl.toURL(), config)
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
        HttpResponse<List<Map>> resp = client.toBlocking().exchange(HttpRequest.GET("/appData"), Argument.of(List, Map))
        def result = resp.body()

        then:"The response is correct"
        resp.status == HttpStatus.OK
        result.size() > 0

        when:
        List<String> versions = result.collect { it.version } as List<String>

        then:
        versions.containsAll(['3.3.12', '4.0.9', '4.0.10.BUILD-SNAPSHOT'])
    }
}
