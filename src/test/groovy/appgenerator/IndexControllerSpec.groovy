package appgenerator

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * Created by jameskleeh on 2/3/17.
 */
@TestFor(IndexController)
class IndexControllerSpec extends Specification {

    void "test index"() {
        when:
        controller.index()

        then:
        response.forwardedUrl == '/index.html'
    }
}
