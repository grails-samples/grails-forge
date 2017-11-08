package appgenerator

import appgenerator.profile.Feature
import appgenerator.profile.Profile
import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(ProfileService)
class ProfileServiceSpec extends Specification {

    void "test get profiles"() {
        List<Profile> profiles = service.getProfiles("3.2.2")

        expect:
        profiles.size() == 4
    }

    void "test get features"() {
        List<Feature> features = service.getFeatures("3.2.2", "angular2")

        expect:
        features.size() == 10
    }
}
