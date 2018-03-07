package appgenerator

import appgenerator.profile.Feature
import appgenerator.profile.Profile
import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(ProfileService)
class ProfileServiceSpec extends Specification {

    void "test get profiles"() {
        when:
        List<Profile> profiles = service.getProfiles("3.2.2")

        then:
        profiles.size() == ['angular','rest-api','web','neo4j','vue'].size()
    }

    void "test get features"() {
        when:
        List<Feature> features = service.getFeatures("3.2.2", "angular2")

        then:
        features.size() == 10
    }
}
