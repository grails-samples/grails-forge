package appgenerator.aether

import org.springframework.util.Assert;

/**
 * A single dependency.
 *
 * @author Phillip Webb
 * @since 1.3.0
 */
public final class Dependency {

    private final String groupId;

    private final String artifactId;

    private final String version;

    private final List<Dependency.Exclusion> exclusions;

    /**
     * Create a new {@link Dependency} instance.
     * @param groupId the group ID
     * @param artifactId the artifact ID
     * @param version the version
     */
    public Dependency(String groupId, String artifactId, String version) {
        this(groupId, artifactId, version, Collections.<Exclusion>emptyList());
    }

    /**
     * Create a new {@link Dependency} instance.
     * @param groupId the group ID
     * @param artifactId the artifact ID
     * @param version the version
     * @param exclusions the exclusions
     */
    public Dependency(String groupId, String artifactId, String version,
                      List<Exclusion> exclusions) {
        Assert.notNull(groupId, "GroupId must not be null");
        Assert.notNull(artifactId, "ArtifactId must not be null");
        Assert.notNull(version, "Version must not be null");
        Assert.notNull(exclusions, "Exclusions must not be null");
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.exclusions = Collections.unmodifiableList(exclusions);
    }

    /**
     * Return the dependency group id.
     * @return the group ID
     */
    public String getGroupId() {
        return this.groupId;
    }

    /**
     * Return the dependency artifact id.
     * @return the artifact ID
     */
    public String getArtifactId() {
        return this.artifactId;
    }

    /**
     * Return the dependency version.
     * @return the version
     */
    public String getVersion() {
        return this.version;
    }

    /**
     * Return the dependency exclusions.
     * @return the exclusions
     */
    public List<Exclusion> getExclusions() {
        return this.exclusions;
    }

    @Override
    public String toString() {
        return this.groupId + ":" + this.artifactId + ":" + this.version;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.groupId.hashCode();
        result = prime * result + this.artifactId.hashCode();
        result = prime * result + this.version.hashCode();
        result = prime * result + this.exclusions.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() == obj.getClass()) {
            Dependency other = (Dependency) obj;
            boolean result = true;
            result &= this.groupId.equals(other.groupId);
            result &= this.artifactId.equals(other.artifactId);
            result &= this.version.equals(other.version);
            result &= this.exclusions.equals(other.exclusions);
            return result;
        }
        return false;
    }

    /**
     * A dependency exclusion.
     */
    public static final class Exclusion {

        private final String groupId;

        private final String artifactId;

        Exclusion(String groupId, String artifactId) {
            Assert.notNull(groupId, "GroupId must not be null");
            Assert.notNull(groupId, "ArtifactId must not be null");
            this.groupId = groupId;
            this.artifactId = artifactId;
        }

        /**
         * Return the exclusion artifact ID.
         * @return the exclusion artifact ID
         */
        public String getArtifactId() {
            return this.artifactId;
        }

        /**
         * Return the exclusion group ID.
         * @return the exclusion group ID
         */
        public String getGroupId() {
            return this.groupId;
        }

        @Override
        public String toString() {
            return this.groupId + ":" + this.artifactId;
        }

        @Override
        public int hashCode() {
            return this.groupId.hashCode() * 31 + this.artifactId.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() == obj.getClass()) {
                Exclusion other = (Exclusion) obj;
                boolean result = true;
                result &= this.groupId.equals(other.groupId);
                result &= this.artifactId.equals(other.artifactId);
                return result;
            }
            return false;
        }

    }

}

