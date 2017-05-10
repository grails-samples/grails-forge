package appgenerator.aether

import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.graph.Exclusion;
import org.eclipse.aether.util.artifact.JavaScopes;

/**
 * Context used when resolving dependencies.
 *
 * @author Andy Wilkinson
 * @since 1.1.0
 */
public class DependencyResolutionContext {

    private final Map<String, Dependency> managedDependencyByGroupAndArtifact = new HashMap<String, Dependency>();

    private final List<Dependency> managedDependencies = new ArrayList<Dependency>();

    private DependencyManagement dependencyManagement = null;

    private ArtifactCoordinatesResolver artifactCoordinatesResolver;

    public DependencyResolutionContext() {
        addDependencyManagement(new SpringBootDependenciesDependencyManagement());
    }

    private String getIdentifier(Dependency dependency) {
        return getIdentifier(dependency.getArtifact().getGroupId(),
                dependency.getArtifact().getArtifactId());
    }

    private String getIdentifier(String groupId, String artifactId) {
        return groupId + ":" + artifactId;
    }

    public ArtifactCoordinatesResolver getArtifactCoordinatesResolver() {
        return this.artifactCoordinatesResolver;
    }

    public String getManagedVersion(String groupId, String artifactId) {
        Dependency dependency = getManagedDependency(groupId, artifactId);
        if (dependency == null) {
            dependency = this.managedDependencyByGroupAndArtifact
                    .get(getIdentifier(groupId, artifactId));
        }
        return dependency != null ? dependency.getArtifact().getVersion() : null;
    }

    public List<Dependency> getManagedDependencies() {
        return Collections.unmodifiableList(this.managedDependencies);
    }

    private Dependency getManagedDependency(String group, String artifact) {
        return this.managedDependencyByGroupAndArtifact
                .get(getIdentifier(group, artifact));
    }

    void addManagedDependencies(List<Dependency> dependencies) {
        this.managedDependencies.addAll(dependencies);
        for (Dependency dependency : dependencies) {
            this.managedDependencyByGroupAndArtifact.put(getIdentifier(dependency),
                    dependency);
        }
    }

    public void addDependencyManagement(DependencyManagement dependencyManagement) {
        for (appgenerator.aether.Dependency dependency : dependencyManagement
                .getDependencies()) {
            List<Exclusion> aetherExclusions = new ArrayList<Exclusion>();
            for (appgenerator.aether.Dependency.Exclusion exclusion : dependency
                    .getExclusions()) {
                aetherExclusions.add(new Exclusion(exclusion.getGroupId(),
                        exclusion.getArtifactId(), "*", "*"));
            }
            Dependency aetherDependency = new Dependency(
                    new DefaultArtifact(dependency.getGroupId(),
                            dependency.getArtifactId(), "jar", dependency.getVersion()),
                    JavaScopes.COMPILE, false, aetherExclusions);
            this.managedDependencies.add(0, aetherDependency);
            this.managedDependencyByGroupAndArtifact.put(getIdentifier(aetherDependency),
                    aetherDependency);
        }
        this.dependencyManagement = this.dependencyManagement == null ? dependencyManagement :
                new CompositeDependencyManagement(dependencyManagement, this.dependencyManagement);
        this.artifactCoordinatesResolver = new DependencyManagementArtifactCoordinatesResolver(this.dependencyManagement);
    }

}
