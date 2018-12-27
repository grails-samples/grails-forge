package appgenerator

import grails.testing.web.controllers.ControllerUnitTest
import spock.lang.Specification

class GeneratorControllerSpec extends Specification implements ControllerUnitTest<GeneratorController> {

    void setup() {
        controller.versionService = Mock(VersionService) {
            getSupportedVersions() >> ["x"]
        }
    }

    void "test generate error"() {
        given:
        controller.projectGeneratorService = Mock(ProjectGeneratorService) {
            1 * getProject(_) >> null
        }

        when:
        params.version = "x"
        params.name = "foo"
        controller.generate()

        then:
        response.status == 403
        response.text == '{"error":"Could not complete that request"}'
    }

    void "test generate not supported version"() {
        when:
        params.version = "y"
        params.name = "foo"
        controller.generate()

        then:
        response.status == 422
    }

    void "test generate sets profile if type == plugin and profile == web"() {
        given:
        File file = File.createTempFile("abc", ".txt")
        file.text = "abc"
        controller.projectGeneratorService = Mock(ProjectGeneratorService) {
            1 * getProject({it.profile == "web-plugin"}) >> file
        }

        when:
        params.version = "x"
        params.name = "foo"
        params.type = "plugin"
        controller.generate()

        then:
        response.status == 200
        response.contentAsByteArray == "abc".bytes
        response.contentLength == 3
        response.contentType == "application/octet-stream"
        response.header("Content-disposition") == "filename=\"foo.zip\""
        !file.exists()
    }

    void "test generate doesn't set profile if type == plugin and profile != web"() {
        given:
        File file = File.createTempFile("abc", ".txt")
        file.text = "abc"
        controller.projectGeneratorService = Mock(ProjectGeneratorService) {
            1 * getProject({it.profile == "plugin"}) >> file
        }

        when:
        params.version = "x"
        params.name = "foo"
        params.type = "plugin"
        params.profile = "plugin"
        controller.generate()

        then:
        response.status == 200
        response.contentAsByteArray == "abc".bytes
        response.contentLength == 3
        response.contentType == "application/octet-stream"
        response.header("Content-disposition") == "filename=\"foo.zip\""
        !file.exists()
    }

    void "test generate success"() {
        given:
        File file = File.createTempFile("abc", ".txt")
        file.text = "abc"
        controller.projectGeneratorService = Mock(ProjectGeneratorService) {
            1 * getProject(_) >> file
        }

        when:
        params.version = "x"
        params.name = "foo"
        controller.generate()

        then:
        response.status == 200
        response.contentAsByteArray == "abc".bytes
        response.contentLength == 3
        response.contentType == "application/octet-stream"
        response.header("Content-disposition") == "filename=\"foo.zip\""
        !file.exists()
    }
}
