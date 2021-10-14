package appgenerator

import appgenerator.profile.Profile
import grails.plugin.json.view.mvc.JsonViewResolver
import grails.testing.json.JsonControllerUnitTest
import grails.testing.web.controllers.ControllerUnitTest
import spock.lang.Specification

class ProfileControllerSpec extends Specification implements ControllerUnitTest<ProfileController>, JsonControllerUnitTest{

    static doWithSpring = {
        jsonSmartViewResolver(JsonViewResolver)
    }

    void "get profiles for a valid grails version"() {
        given:
        controller.profileService = Mock(ProfileService) {
            1 * getProfiles("5.0.0") >> []
        }
        controller.versionService = Mock(VersionService) {
            1 * getAllSupportedVersions() >> ["5.0.0"]
        }

        when:
        params.version = "5.0.0"
        webRequest.actionName = "profiles"
        controller.profiles()
        render()

        then:
        response.status == 200
        response.text == '[]'
    }

    void "get plugin profiles for a valid grails version"() {
        given:
        controller.profileService = Mock(ProfileService) {
            1 * getPluginProfiles("5.0.0") >> []
        }
        controller.versionService = Mock(VersionService) {
            1 * getAllSupportedVersions() >> ["5.0.0"]
        }

        when:
        params.type = "plugin"
        params.version = "5.0.0"
        webRequest.actionName = "profiles"
        controller.profiles()
        render()

        then:
        response.status == 200
        response.text == '[]'
    }

    void "get profiles for an invalid grails version"() {
        given:
        controller.versionService = Mock(VersionService) {
            1 * getAllSupportedVersions() >> ["3.0.0"]
        }

        when:
        webRequest.actionName = "profiles"
        controller.profiles("3.0.1")
        render()

        then:
        response.status == 404
    }

    void "get profile - valid version, valid profile"() {
        given:
        controller.profileService = Mock(ProfileService) {
            1 * getProfiles("5.0.0") >> [new Profile(name: "x"), new Profile(name: "y")]
        }
        controller.versionService = Mock(VersionService) {
            1 * getAllSupportedVersions() >> ["5.0.0"]
        }

        when:
        params.profile = "x"
        params.version = "5.0.0"
        webRequest.actionName = "profile"
        controller.profile(params.version, params.profile)
        render()

        then:
        response.status == 200
        response.json.name == "x"
    }
}
