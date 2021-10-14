package grails.testing.json

import grails.testing.web.GrailsWebUnitTest
import grails.web.Controller
import org.grails.web.util.GrailsApplicationAttributes
import org.springframework.web.servlet.ModelAndView

trait JsonControllerUnitTest implements GrailsWebUnitTest {

    void render() {
        ModelAndView modelView =(request.getAttribute(GrailsApplicationAttributes.CONTROLLER) as Controller)?.modelAndView
        modelView?.view?.render(modelView?.model, request, response)
    }
}
