package appgenerator

import grails.artefact.Controller

/**
 * Created by jameskleeh on 11/17/16.
 */
trait CurlAware implements Controller {

    Boolean isCurlRequest() {
        request.getHeader("User-Agent")?.startsWith('curl')
    }
}