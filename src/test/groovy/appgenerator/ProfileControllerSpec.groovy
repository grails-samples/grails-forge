package appgenerator

import appgenerator.profile.Profile
import grails.plugin.json.view.mvc.JsonViewResolver
import grails.testing.web.controllers.ControllerUnitTest
import spock.lang.Specification

class ProfileControllerSpec extends Specification implements ControllerUnitTest<ProfileController> {

    static doWithSpring = {
        jsonSmartViewResolver(JsonViewResolver)
    }

    void "get profiles for a valid grails version"() {
        given:
        controller.profileService = Mock(ProfileService) {
            1 * getProfiles("3.0.0") >> []
        }
        controller.versionService = Mock(VersionService) {
            1 * getSupportedVersions() >> ["3.0.0"]
        }

        when:
        params.version = "3.0.0"
        webRequest.actionName = "profiles"
        controller.profiles()

        then:
        response.status == 200
        response.text == '[]'
    }

    void "get plugin profiles for a valid grails version"() {
        given:
        controller.profileService = Mock(ProfileService) {
            1 * getPluginProfiles("3.0.0") >> []
        }
        controller.versionService = Mock(VersionService) {
            1 * getSupportedVersions() >> ["3.0.0"]
        }

        when:
        params.type = "plugin"
        params.version = "3.0.0"
        webRequest.actionName = "profiles"
        controller.profiles()

        then:
        response.status == 200
        response.text == '[]'
    }

    void "get profiles for an invalid grails version"() {
        given:
        controller.versionService = Mock(VersionService) {
            1 * getSupportedVersions() >> ["3.0.0"]
        }

        when:
        params.version = "3.0.1"
        webRequest.actionName = "profiles"
        controller.profiles()

        then:
        response.status == 404
    }

    void "get profile - valid version, valid profile"() {
        given:
        controller.profileService = Mock(ProfileService) {
            1 * getProfiles("3.0.0") >> [new Profile(name: "x"), new Profile(name: "y")]
        }
        controller.versionService = Mock(VersionService) {
            1 * getSupportedVersions() >> ["3.0.0"]
        }

        when:
        params.profile = "x"
        params.version = "3.0.0"
        webRequest.actionName = "profile"
        controller.profile(params.version, params.profile)

        then:
        response.status == 200
        response.json.name == "x"
    }
}
