package appgenerator

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * Created by jameskleeh on 11/9/16.
 */
@TestFor(GeneratorController)
class GeneratorControllerSpec extends Specification {

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
