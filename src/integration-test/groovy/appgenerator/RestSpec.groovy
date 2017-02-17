package appgenerator

import grails.plugins.rest.client.RestBuilder
import groovy.transform.CompileStatic

@CompileStatic
trait RestSpec {

    RestBuilder restBuilder() {
        new RestBuilder()
    }
}
