package appgenerator

import grails.plugins.rest.client.RestBuilder
import grails.plugins.rest.client.RestResponse
import groovy.transform.CompileStatic
import groovy.transform.TypeCheckingMode

@CompileStatic
trait RestSpec {

    @CompileStatic(TypeCheckingMode.SKIP)
    String getBaseUrl() {
        "http://localhost:$serverPort"
    }

    RestBuilder restBuilder() {
        new RestBuilder()
    }

    RestResponse get(String relativePath, Closure cls = null) {
        restBuilder().get("$baseUrl$relativePath", cls)
    }

    RestResponse post(String relativePath, Closure cls = null) {
        restBuilder().post("$baseUrl$relativePath", cls)
    }

    RestResponse put(String relativePath, Closure cls = null) {
        restBuilder().put("$baseUrl$relativePath", cls)
    }

    RestResponse delete(String relativePath, Closure cls = null) {
        restBuilder().delete("$baseUrl$relativePath", cls)
    }
}
