package appgenerator

import grails.testing.services.ServiceUnitTest
import org.grails.model.GrailsVersion
import spock.lang.Specification

class GrailsVersionServiceSpec extends Specification implements ServiceUnitTest<GrailsVersionService> {

    void "test getSupported"() {
        given:
        List<String> versions = ["2.99.99", "3.0.99", "3.1.0", "3.1.12", "3.1.13", "3.1.13.BUILD-SNAPSHOT", "3.1.14.BUILD-SNAPSHOT",
                                 "3.2.0.M1", "3.2.0.M2", "3.2.0.RC1", "3.2.0.RC2", "3.2.0", "3.2.0.BUILD-SNAPSHOT", "3.2.3.BUILD-SNAPSHOT",
                                 "3.2.1", "3.2.1.BUILD-SNAPSHOT", "3.2.2", "3.2.2.BUILD-SNAPSHOT", "3.2.3.BUILD-SNAPSHOT",
                                 "3.3.0.M1", "3.3.0.M2", "3.3.0.RC1", "3.3.0.BUILD-SNAPSHOT",
                                 "3.4.0.M1", "3.4.0.M2", "3.4.0.RC1", "3.4.0.BUILD-SNAPSHOT", "3.4.0", "3.5.0", "3.5.1"]

        when:
        List<GrailsVersion> grailsVersions = service.getSupported(versions)

        then:
        grailsVersions[0].versionText == "3.2.2"
        grailsVersions[1].versionText == "3.2.3.BUILD-SNAPSHOT"
        grailsVersions[2].versionText == "3.3.0.RC1"
        grailsVersions[3].versionText == "3.4.0"
        grailsVersions[4].versionText == "3.5.0"
        grailsVersions[5].versionText == "3.5.1"
        grailsVersions.size() == 6
    }
}
