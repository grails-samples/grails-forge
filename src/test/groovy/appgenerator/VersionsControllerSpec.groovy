package appgenerator

import appgenerator.profile.Profile
import grails.plugin.json.view.mvc.JsonViewResolver
import grails.testing.json.JsonControllerUnitTest
import grails.testing.web.controllers.ControllerUnitTest
import org.springframework.http.HttpStatus
import spock.lang.Specification

class VersionsControllerSpec extends Specification implements ControllerUnitTest<VersionsController>, JsonControllerUnitTest {

    static doWithSpring = {
        jsonSmartViewResolver(JsonViewResolver)
    }

    void "test supported grails versions"() {
        given:
        controller.versionService = Mock(VersionService) {
            1 * getSupportedVersions() >> ["a", "b"]
        }

        when:
        webRequest.actionName = "grailsVersions"
        controller.grailsVersions()
        render()

        then:
        response.status == HttpStatus.OK.value()
        response.text == '["a","b"]'
    }

    void "test appData"() {
        given:
        controller.versionService = Mock(VersionService) {
            1 * getSupportedVersions() >> ["a", "b"]
        }
        def profile1 = new Profile()
        def profile2 = new Profile()
        controller.profileService = Mock(ProfileService) {
            1 * getProfiles("a") >> [profile1]
            1 * getProfiles("b") >> [profile2]
        }

        when:
        webRequest.actionName = "appData"
        controller.appData()
        render()

        then:
        response.json[0].version == 'a'
        response.json[0].profiles.size() == 1
        response.json[1].version == 'b'
        response.json[1].profiles.size() == 1
    }

    void "test pluginData"() {
        given:
        controller.versionService = Mock(VersionService) {
            1 * getSupportedVersions() >> ["a", "b"]
        }
        def profile1 = new Profile()
        def profile2 = new Profile()
        controller.profileService = Mock(ProfileService) {
            1 * getPluginProfiles("a") >> [profile1]
            1 * getPluginProfiles("b") >> [profile2]
        }

        when:
        webRequest.actionName = "pluginData"
        controller.pluginData()
        render()

        then:
        response.json[0].version == 'a'
        response.json[0].profiles.size() == 1
        response.json[1].version == 'b'
        response.json[1].profiles.size() == 1
    }
}
