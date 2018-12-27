package appgenerator

import appgenerator.profile.Feature
import appgenerator.profile.Profile
import grails.testing.services.ServiceUnitTest
import spock.lang.Specification

class ProfileServiceSpec extends Specification implements ServiceUnitTest<ProfileService> {

    void "test get profiles"() {
        when:
        List<Profile> profiles = service.getProfiles("3.2.2")

        then:
        profiles.size() == 4
    }

    void "test get features"() {
        when:
        List<Feature> features = service.getFeatures("3.2.2", "angular2")

        then:
        features.size() == 10
    }
}
