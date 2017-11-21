package appgenerator

import appgenerator.versions.GrailsVersion
import groovy.util.slurpersupport.GPathResult

class GrailsVersionService {

    static List<GrailsVersion> loadFromMaven() {
        GPathResult xml = new XmlSlurper().parse(new URL("http://repo.grails.org/grails/core/org/grails/grails-core/maven-metadata.xml").openStream())
        getSupported(xml.versioning.versions.version*.text())
    }

    static GrailsVersion LOWEST_31X = GrailsVersion.build("3.1.13")
    static GrailsVersion HIGHEST_31X = GrailsVersion.build("3.1.99")
    static GrailsVersion LOWEST_32X = GrailsVersion.build("3.2.2")

    static List<GrailsVersion> getSupported(List<String> versionList) {
        SortedSet<GrailsVersion> versions = new TreeSet<>()
        SortedSet<GrailsVersion> snapshots = new TreeSet<>()
        versionList.each { String version ->
            GrailsVersion grailsVersion = build(version)
            if (grailsVersion?.supported) {
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

    boolean isSupported() {
        (this >= LOWEST_31X && HIGHEST_31X >= this) ||
                (this >= LOWEST_32X)
    }
}

