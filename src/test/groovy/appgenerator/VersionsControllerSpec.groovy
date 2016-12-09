package appgenerator

import appgenerator.profile.Profile
import grails.test.mixin.TestFor
import org.springframework.http.HttpStatus
import spock.lang.Specification

@TestFor(VersionsController)
class VersionsControllerSpec extends Specification {

    void "test supported grails versions"() {
        given:
        controller.versionService = Mock(VersionService) {
            1 * getSupportedVersions() >> ["a", "b"]
        }

        when:
        controller.grailsVersions()

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
        controller.appData()

        then:
        response.json[0].version == 'a'
        response.json[0].profiles.size() == 1
        response.json[1].version == 'b'
        response.json[1].profiles.size() == 1
    }
}
