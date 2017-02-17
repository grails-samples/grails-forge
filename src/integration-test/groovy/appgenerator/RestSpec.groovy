package appgenerator

import grails.plugins.rest.client.RestBuilder
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
}
