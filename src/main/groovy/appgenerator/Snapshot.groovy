package appgenerator

import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode

/**
 * A class to represent the snapshot version of Grails for comparison
 *
 * @author James Kleeh
 */
@CompileStatic
@EqualsAndHashCode(includes = ['text'])
class Snapshot implements Comparable<Snapshot> {

    private static final String SNAPSHOT = "SNAPSHOT"
    private static final String RC = "RC"
    private static final String MILESTONE = "M"

    final String text

    @Override
    String toString() {
        text
    }

    int getMilestoneVersion() {
        text.replace(MILESTONE, "").toInteger()
    }

    int getReleaseCandidateVersion() {
        text.replace(RC, "").toInteger()
    }

    boolean isBuildSnapshot() {
        text.endsWith(SNAPSHOT)
    }

    boolean isReleaseCandidate() {
        text.startsWith(RC)
    }

    boolean isMilestone() {
        text.startsWith(MILESTONE)
    }

    Snapshot(String text) {
        this.text = text
        if (!text.matches(/^(M|RC|Final)\d*$/) && !isBuildSnapshot()) {
            throw new IllegalArgumentException("GrailsVersion snapshot is not in the expected format")
        }
    }

    @Override
    int compareTo(Snapshot o) {

        if (this.buildSnapshot && !o.buildSnapshot) {
            return 1
        } else if (!this.buildSnapshot && o.buildSnapshot) {
            return -1
        } else if (this.buildSnapshot && o.buildSnapshot) {
            return 0
        }

        if (this.releaseCandidate && !o.releaseCandidate) {
            return 1
        } else if (!this.releaseCandidate && o.releaseCandidate) {
            return -1
        } else if (this.releaseCandidate && o.releaseCandidate) {
            return this.releaseCandidateVersion <=> o.releaseCandidateVersion
        }

        if (this.milestone && !o.milestone) {
            return 1
        } else if (!this.milestone && o.milestone) {
            return -1
        } else if (this.milestone && o.milestone) {
            return this.milestoneVersion <=> o.milestoneVersion
        }

        return 0
    }
}
