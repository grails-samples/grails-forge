package appgenerator

import grails.testing.spock.OnceBefore
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpHeaders
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.MediaType
import io.micronaut.http.client.HttpClient
import spock.lang.Shared

import static grails.web.http.HttpHeaders.CONTENT_TYPE

import grails.testing.mixin.integration.Integration
import spock.lang.Specification

@Integration
class IndexControllerIntegrationSpec extends Specification {

    @Shared HttpClient client

    @OnceBefore
    void init() {
        String baseUrl = "http://localhost:$serverPort"
        this.client  = HttpClient.create(baseUrl.toURL())
    }

    void "test index with curl"() {
        HttpResponse<String> resp = client.toBlocking().exchange(HttpRequest.GET("/").header("User-Agent", "curl"), String)

        expect:"The response is correct"
        resp.status() == HttpStatus.OK
        resp.header(HttpHeaders.CONTENT_TYPE) == "text/plain"
        resp.body().startsWith('Grails Application Forge')
    }
}
