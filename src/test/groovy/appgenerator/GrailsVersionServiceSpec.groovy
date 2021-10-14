package appgenerator

import spock.lang.Shared
import spock.lang.Specification

class GrailsVersionServiceSpec extends Specification {

    @Shared
    GrailsVersionService service = new GrailsVersionService()

    void "test getLatestSupported"() {
        given:
        SortedSet<String> versions = new TreeSet<>(["2.99.99", "3.0.99", "3.1.0", "3.1.12", "3.1.13", "3.1.13.BUILD-SNAPSHOT", "3.1.14.BUILD-SNAPSHOT",
                                 "3.2.0.M1", "3.2.0.M2", "3.2.0.RC1", "3.2.0.RC2", "3.2.0", "3.2.0.BUILD-SNAPSHOT", "3.2.3.BUILD-SNAPSHOT",
                                 "3.2.1", "3.2.1.BUILD-SNAPSHOT", "3.2.2", "3.2.2.BUILD-SNAPSHOT", "3.2.3.BUILD-SNAPSHOT",
                                 "3.3.0.M1", "3.3.0.M2", "3.3.0.RC1", "3.3.0.BUILD-SNAPSHOT",
                                 "3.4.0.M1", "3.4.0.M2", "3.4.0.RC1", "3.4.0.BUILD-SNAPSHOT", "3.4.0", "3.5.0", "3.5.1", "3.5.2.BUILD-SNAPSHOT", '5.0.0'])

        when:
        List<GrailsVersion> grailsVersions = service.getLatestSupported(versions)

        then:
        grailsVersions[0].versionText == "5.0.0"
        grailsVersions.size() == 1
    }

    void "test getSupported"() {
        given:
        SortedSet<String> versions = new TreeSet<>(["2.99.99", "3.0.99", "3.1.0", "3.1.12", "3.1.13", "3.1.13.BUILD-SNAPSHOT", "3.1.14.BUILD-SNAPSHOT",
                                                    "3.2.0.M1", "3.2.0.M2", "3.2.0.RC1", "3.2.0.RC2", "3.2.0", "3.2.0.BUILD-SNAPSHOT", "3.2.3.BUILD-SNAPSHOT",
                                                    "3.2.1", "3.2.1.BUILD-SNAPSHOT", "3.2.2", "3.2.2.BUILD-SNAPSHOT", "3.2.3.BUILD-SNAPSHOT",
                                                    "3.3.0.M1", "3.3.0.M2", "3.3.0.RC1", "3.3.0.BUILD-SNAPSHOT",
                                                    "3.4.0.M1", "3.4.0.M2", "3.4.0.RC1", "3.4.0.BUILD-SNAPSHOT", "3.4.0", "3.5.0", "3.5.1", "3.5.2.BUILD-SNAPSHOT", '5.0.0'])

        when:
        List<GrailsVersion> grailsVersions = service.getSupported(versions)

        then:
        grailsVersions.size() == 1
        grailsVersions[0].versionText == "5.0.0"
    }

    void "test getSupported filter out non-parsable versions"() {

        given:
        SortedSet<String> versions = new TreeSet<>(['4.0.9S', '5.0.0', '6.0.0'])

        expect:
        service.getSupported(versions).get(0).versionText == '5.0.0'
    }
}
