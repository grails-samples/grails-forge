package appgenerator

import appgenerator.profile.Profile
import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(ProfileController)
class ProfileControllerSpec extends Specification {

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
        controller.profiles()

        then:
        response.text == '[]'
        response.status == 200
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
        controller.profiles()

        then:
        response.text == '[]'
        response.status == 200
    }

    void "get profiles for an invalid grails version"() {
        given:
        controller.versionService = Mock(VersionService) {
            1 * getSupportedVersions() >> ["3.0.0"]
        }

        when:
        params.version = "3.0.1"
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
        controller.profile()

        then:
        response.json.name == "x"
        response.status == 200
    }
}
