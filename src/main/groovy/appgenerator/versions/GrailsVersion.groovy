package appgenerator.versions

import groovy.transform.ToString
import groovy.util.slurpersupport.GPathResult

/**
 * Created by jameskleeh on 11/9/16.
 */
class GrailsVersion implements Comparable<GrailsVersion> {

    int major
    int minor
    int patch

    Snapshot snapshot

    String versionText

    static GrailsVersion LOWEST_31X = build("3.1.13")
    static GrailsVersion HIGHEST_31X = build("3.1.99")
    static GrailsVersion LOWEST_32X = build("3.2.2")

    static List<GrailsVersion> loadFromMaven() {
        GPathResult xml = new XmlSlurper().parse(new URL("http://repo.grails.org/grails/core/org/grails/grails-core/maven-metadata.xml").openStream())
        getSupported(xml.versioning.versions.version*.text())
    }

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

    static GrailsVersion build(String version) {
        String[] parts = version.split("\\.")
        GrailsVersion grailsVersion
        if (parts.length >= 3) {
            grailsVersion = new GrailsVersion()
            grailsVersion.versionText = version
            if (parts.length > 3) {
                grailsVersion.snapshot = new Snapshot(parts[3])
            }
            grailsVersion.major = parts[0].toInteger()
            grailsVersion.minor = parts[1].toInteger()
            grailsVersion.patch = parts[2].toInteger()
        }
        grailsVersion
    }

    boolean isSupported() {
        (this >= LOWEST_31X && HIGHEST_31X >= this) ||
        (this >= LOWEST_32X)
    }

    boolean isSnapshot() {
        snapshot != null
    }

    @Override
    int compareTo(GrailsVersion o) {
        int majorCompare = this.major <=> o.major
        if (majorCompare != 0) {
            return majorCompare
        }

        int minorCompare = this.minor <=> o.minor
        if (minorCompare != 0) {
            return minorCompare
        }

        int patchCompare = this.patch <=> o.patch
        if (patchCompare != 0) {
            return patchCompare
        }

        if (this.isSnapshot() && !o.isSnapshot()) {
            return -1
        } else if (!this.isSnapshot() && o.isSnapshot()) {
            return 1
        } else if (this.isSnapshot() && o.isSnapshot()) {
            return this.getSnapshot() <=> o.getSnapshot()
        } else {
            return 0
        }
    }

    String toString() {
        this.versionText
    }
}
