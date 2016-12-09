package appgenerator.versions

import spock.lang.Specification

/**
 * Created by jameskleeh on 11/9/16.
 */
class GrailsVersionSpec extends Specification {

    void "test compareTo"() {
        expect:
        GrailsVersion.build(greater) > GrailsVersion.build(lesser)

        where:
        greater                 | lesser
        "3.0.0"                 | "2.99.99.BUILD-SNAPSHOT"
        "3.0.0"                 | "2.99.99"
        "3.0.1"                 | "3.0.1.BUILD-SNAPSHOT"
        "3.1.2"                 | "3.1.1"
        "3.2.2"                 | "3.1.2"
        "4.1.1"                 | "3.1.1"
    }

    void "test getSupported"() {
        given:
        List<String> versions = ["2.99.99", "3.0.99", "3.1.0", "3.1.12", "3.1.13", "3.1.13.BUILD-SNAPSHOT", "3.1.14.BUILD-SNAPSHOT",
                                 "3.2.0.M1", "3.2.0.M2", "3.2.0.RC1", "3.2.0.RC2", "3.2.0", "3.2.0.BUILD-SNAPSHOT", "3.2.3.BUILD-SNAPSHOT",
                                 "3.2.1", "3.2.1.BUILD-SNAPSHOT", "3.2.2", "3.2.2.BUILD-SNAPSHOT", "3.2.3.BUILD-SNAPSHOT",
                                 "3.3.0.M1", "3.3.0.M2", "3.3.0.RC1", "3.3.0.BUILD-SNAPSHOT",
                                 "3.4.0.M1", "3.4.0.M2", "3.4.0.RC1", "3.4.0.BUILD-SNAPSHOT", "3.4.0", "3.5.0", "3.5.1"]

        when:
        List<GrailsVersion> grailsVersions = GrailsVersion.getSupported(versions)

        then:
        grailsVersions[0].versionText == "3.1.13"
        grailsVersions[1].versionText == "3.1.14.BUILD-SNAPSHOT"
        grailsVersions[2].versionText == "3.2.2"
        grailsVersions[3].versionText == "3.2.3.BUILD-SNAPSHOT"
        grailsVersions[4].versionText == "3.3.0.RC1"
        grailsVersions[5].versionText == "3.4.0"
        grailsVersions[6].versionText == "3.5.0"
        grailsVersions[7].versionText == "3.5.1"
        grailsVersions.size() == 8
    }
}
