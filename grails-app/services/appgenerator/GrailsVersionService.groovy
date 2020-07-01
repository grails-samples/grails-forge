package appgenerator

import org.grails.model.GrailsVersion
import groovy.util.slurpersupport.GPathResult

class GrailsVersionService {

    final static GrailsVersion LOWEST_32X = GrailsVersion.build("3.2.2")
    final static GrailsVersion HIGHEST_40X = GrailsVersion.build("4.0.99")
    final static String MAVEN_METADATA= 'https://repo.grails.org/grails/core/org/grails/grails-core/maven-metadata.xml'

    List<GrailsVersion> loadFromMaven() {
        GPathResult xml = new XmlSlurper().parse(new URL(MAVEN_METADATA).openStream())
        getSupported(xml.versioning.versions.version*.text())
    }

    List<GrailsVersion> getSupported(List<String> versionList) {
        SortedSet<GrailsVersion> versions = new TreeSet<>()
        SortedSet<GrailsVersion> snapshots = new TreeSet<>()
        versionList.each { String version ->
            GrailsVersion grailsVersion = GrailsVersion.build(version)
            if (grailsVersion && isSupported(grailsVersion)) {
                if (grailsVersion.snapshot) {
                    snapshots.add(grailsVersion)
                } else {
                    versions.add(grailsVersion)
                }
            }
        }

        Map<String, List<GrailsVersion>> snapshotMap = snapshots.groupBy {
            "${it.major}.${it.minor}"
        }
        snapshotMap.each { String key, List<GrailsVersion> snapshotList ->
            Set comparableVersions = versions.findAll { "${it.major}.${it.minor}".toString() == key }
            GrailsVersion latestSnapshot = snapshotList.max()

            if (!comparableVersions.empty && comparableVersions.max() < latestSnapshot) {
                versions.add(latestSnapshot)
            } else if (comparableVersions.empty) {
                List milestonesAndRcs = snapshotList.findAll { it.snapshot.releaseCandidate || it.snapshot.milestone }
                if (!milestonesAndRcs.empty) {
                    versions.add(milestonesAndRcs.max())
                }
            }
        }

        versions.toList()
    }

    boolean isSupported(GrailsVersion grailsVersion) {
        grailsVersion >= LOWEST_32X && grailsVersion <= HIGHEST_40X
    }
}

