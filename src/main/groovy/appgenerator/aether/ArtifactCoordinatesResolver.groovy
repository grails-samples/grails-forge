package appgenerator.aether

/**
 * A resolver for artifacts' Maven coordinates, allowing group id, artifact id, or version
 * to be obtained from a module identifier. A module identifier may be in the form
 * {@code groupId:artifactId:version}, in which case coordinate resolution simply extracts
 * the relevant piece from the identifier. Alternatively the identifier may be in the form
 * {@code artifactId}, in which case coordinate resolution uses implementation-specific
 * metadata to resolve the groupId and version.
 *
 * @author Andy Wilkinson
 */
public interface ArtifactCoordinatesResolver {

    /**
     * Gets the group id of the artifact identified by the given {@code module}. Returns
     * {@code null} if the artifact is unknown to the resolver.
     * @param module The id of the module
     * @return The group id of the module
     */
    String getGroupId(String module);

    /**
     * Gets the artifact id of the artifact identified by the given {@code module}.
     * Returns {@code null} if the artifact is unknown to the resolver.
     * @param module The id of the module
     * @return The artifact id of the module
     */
    String getArtifactId(String module);

    /**
     * Gets the version of the artifact identified by the given {@code module}. Returns
     * {@code null} if the artifact is unknown to the resolver.
     * @param module The id of the module
     * @return The version of the module
     */
    String getVersion(String module);
}
