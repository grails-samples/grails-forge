package appgenerator

import appgenerator.profile.Feature
import appgenerator.profile.Profile
import com.sun.org.apache.xpath.internal.operations.String
import grails.testing.services.ServiceUnitTest
import spock.lang.Specification

class ProfileServiceSpec extends Specification implements ServiceUnitTest<ProfileService> {

    void "test getProfiles for 3.2.2 retrieves web, rest-api, angular2 and angular"() {
        given:
        Set<String> expectedProfileNames = ['web', 'rest-api', 'angular2', 'angular'] as Set<String>

        when:
        List<Profile> profiles = service.getProfiles("3.2.2")

        then:
        profiles*.name.unique().size() == expectedProfileNames.size()
    }

    void "test get features"() {
        Set<String> expectedFeatures = ['asset-pipeline',
                                        'hibernate4',
                                        'hibernate5',
                                        'json-views',
                                        'less-asset-pipeline',
                                        'markup-views',
                                        'mongodb',
                                        'neo4j',
                                        'rx-mongodb',
                                        'security'] as Set<String>

        when:
        List<Feature> features = service.getFeatures("3.2.2", "angular2")

        then:
        features*.name.size() == expectedFeatures.size()
    }
}
