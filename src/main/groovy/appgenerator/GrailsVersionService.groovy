package appgenerator

import groovy.util.slurpersupport.GPathResult
import org.grails.model.GrailsVersion

import javax.inject.Singleton

@SuppressWarnings('GrMethodMayBeStatic')
@Singleton
class GrailsVersionService {

    final static GrailsVersion LOWEST_32X = GrailsVersion.build("3.2.2")
    final static GrailsVersion HIGHEST_40X = GrailsVersion.build("4.0.99")
    final static String MAVEN_METADATA= 'https://repo.grails.org/grails/core/org/grails/grails-core/maven-metadata.xml'
    final static String MAVEN_METADATA_OSS = 'https://repo1.maven.org/maven2/org/grails/grails-core/maven-metadata.xml'

    List<GrailsVersion> loadAllVersionFromMaven() {
        SortedSet<String> versionList = readVersionsFromMavenMetadata()
        getSupported(versionList)
    }

    List<GrailsVersion> loadLatestFromMaven() {
        SortedSet<String> versionList = readVersionsFromMavenMetadata()
        getLatestSupported(versionList)
    }

    List<GrailsVersion> getSupported(SortedSet<String> versionList) {
        final SortedSet<GrailsVersion> versions = new TreeSet<>()
        final SortedSet<GrailsVersion> snapshots = new TreeSet<>()
        versionList
                .stream()
                .filter({ version -> version.matches('\\d+.\\d+.\\d+(\\..*)?') })
                .map({ GrailsVersion.build(it) })
                .filter({ version -> version != null && isSupported(version) })
                .forEach({version->
                    if (version.isSnapshot()) {
                        snapshots.add(version)
                    } else {
                        versions.add(version)
                    }
                })


        Map<String, List<GrailsVersion>> snapshotMap = snapshots.groupBy {"${it.major}.${it.minor}".toString() }
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

    List<GrailsVersion> getLatestSupported(SortedSet<String> versionList) {
        SortedSet<GrailsVersion> versions = new TreeSet<>()
        SortedSet<GrailsVersion> snapshots = new TreeSet<>()
        getSupported(versionList)
                .forEach({ GrailsVersion version ->
                    if (version.isSnapshot()) {
                        snapshots.add(version)
                    } else {
                        versions.add(version)
                    }
                })

        filterLatestVersions(versions, snapshots)
    }

    boolean isSupported(GrailsVersion grailsVersion) {
        grailsVersion >= LOWEST_32X && grailsVersion <= HIGHEST_40X
    }

    private TreeSet<String> readVersionsFromMavenMetadata() {
        GPathResult xml = new XmlSlurper().parse(new URL(MAVEN_METADATA).openStream())
        GPathResult ossXml = new XmlSlurper().parse(new URL(MAVEN_METADATA_OSS).openStream())
        SortedSet<String> versionList = new TreeSet<>()
        versionList.addAll((List<String>) xml.versioning.versions.version*.text())
        versionList.addAll((List<String>) ossXml.versioning.versions.version*.text())
        versionList
    }

    private List<GrailsVersion> filterLatestVersions(SortedSet<GrailsVersion> versions,
                                                     SortedSet<GrailsVersion> snapshots) {
        final SortedSet<GrailsVersion> finalVersionList = new TreeSet<>()
        versions
                .groupBy { it.major }
                .forEach({ Integer major, List<GrailsVersion> allVersionForMajor ->
                    if (allVersionForMajor) {
                        final GrailsVersion maxVersion = allVersionForMajor.max()
                        finalVersionList.add(maxVersion)
                    }
                })

        if (snapshots) {
            finalVersionList.add(snapshots.max())
        }
        finalVersionList.toList()
    }
}

