package appgenerator

import appgenerator.profile.Feature
import appgenerator.profile.Profile
import com.sun.org.apache.xpath.internal.operations.String
import grails.testing.services.ServiceUnitTest
import spock.lang.Specification

import java.util.stream.Collectors

class ProfileServiceSpec extends Specification implements ServiceUnitTest<ProfileService> {

    void "test getProfiles for 5.0.0 retrieves web, rest-api, angular2 and angular"() {
        given:
        Set<String> expectedProfileNames = ['vue', 'web', 'rest-api', 'react', 'angular'] as Set<String>

        when:
        List<Profile> profiles = service.getProfiles("5.0.0")
        Set<String> profileNames = profiles.stream().map(profile -> profile.name).collect(Collectors.toSet())

        then:
        expectedProfileNames == profileNames
    }

    void "test get features"() {
        Set<String> expectedFeatures = ['asset-pipeline',
                                        'hibernate5',
                                        'json-views',
                                        'less-asset-pipeline',
                                        'markup-views',
                                        'mongodb',
                                        'neo4j',
                                        'rx-mongodb',
                                        'gsp',
                                        'geb2',
                                        'events'] as Set<String>

        when:
        List<Feature> features = service.getFeatures("5.0.0", "web")
        Set<String> featureNames = features.stream().map(feature -> feature.name).collect(Collectors.toSet())

        then:
        expectedFeatures == featureNames
    }
}
