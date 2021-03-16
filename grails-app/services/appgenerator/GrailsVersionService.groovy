package appgenerator

import groovy.util.slurpersupport.GPathResult
import org.grails.model.GrailsVersion

class GrailsVersionService {

    final static GrailsVersion LOWEST_32X = GrailsVersion.build("3.2.2")
    final static GrailsVersion HIGHEST_40X = GrailsVersion.build("4.0.99")
    final static String MAVEN_METADATA= 'https://repo.grails.org/grails/core/org/grails/grails-core/maven-metadata.xml'
    final static String MAVEN_METADATA_OSS = 'https://repo1.maven.org/maven2/org/grails/grails-core/maven-metadata.xml'

    List<GrailsVersion> loadFromMaven() {
        GPathResult xml = new XmlSlurper().parse(new URL(MAVEN_METADATA).openStream())
        GPathResult ossXml = new XmlSlurper().parse(new URL(MAVEN_METADATA_OSS).openStream())
        SortedSet<String> versionList = new TreeSet<>()
        versionList.addAll((List<String>) xml.versioning.versions.version*.text())
        versionList.addAll((List<String>) ossXml.versioning.versions.version*.text())
        getSupported(versionList)
    }

    List<GrailsVersion> getSupported(SortedSet<String> versionList) {
        SortedSet<GrailsVersion> versions = new TreeSet<>()
        SortedSet<GrailsVersion> snapshots = new TreeSet<>()
        versionList
                .stream()
                .filter({ version -> version.matches('\\d+.\\d+.\\d+(\\..*)?') })
                .map({ GrailsVersion.build(it) })
                .filter({ version -> version != null && isSupported(version) })
                .forEach({ GrailsVersion version ->
                    if (version.isSnapshot()) {
                        snapshots.add(version)
                    } else {
                        versions.add(version)
                    }
                })

        final List<GrailsVersion> finalVersionList = []

        versions
                .groupBy { it.major }
                .forEach({ Integer major, List<GrailsVersion> allVersionForMajor ->
                    final GrailsVersion maxVersion = allVersionForMajor.max()
                    finalVersionList.add(maxVersion)
                })

        finalVersionList.add(snapshots.max())
        finalVersionList
    }

    boolean isSupported(GrailsVersion grailsVersion) {
        grailsVersion >= LOWEST_32X && grailsVersion <= HIGHEST_40X
    }
}

