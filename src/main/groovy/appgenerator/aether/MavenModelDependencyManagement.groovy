package appgenerator.aether

import org.apache.maven.model.Model;
import appgenerator.aether.Dependency.Exclusion

/**
 *
 * @author Andy Wilkinson
 * @since 1.3.0
 */
public class MavenModelDependencyManagement implements DependencyManagement {

    private final List<Dependency> dependencies;

    private final Map<String, Dependency> byArtifactId = new LinkedHashMap<String, Dependency>();

    public MavenModelDependencyManagement(Model model) {
        this.dependencies = extractDependenciesFromModel(model);
        for (Dependency dependency : this.dependencies) {
            this.byArtifactId.put(dependency.getArtifactId(), dependency);
        }
    }

    private static List<Dependency> extractDependenciesFromModel(Model model) {
        List<Dependency> dependencies = new ArrayList<Dependency>();
        for (org.apache.maven.model.Dependency mavenDependency : model
                .getDependencyManagement().getDependencies()) {
            List<Exclusion> exclusions = new ArrayList<Exclusion>();
            for (org.apache.maven.model.Exclusion mavenExclusion : mavenDependency
                    .getExclusions()) {
                exclusions.add(new Exclusion(mavenExclusion.getGroupId(),
                        mavenExclusion.getArtifactId()));
            }
            Dependency dependency = new Dependency(mavenDependency.getGroupId(),
                    mavenDependency.getArtifactId(), mavenDependency.getVersion(),
                    exclusions);
            dependencies.add(dependency);
        }
        return dependencies;
    }

    @Override
    public List<Dependency> getDependencies() {
        return this.dependencies;
    }

    @Override
    public String getSpringBootVersion() {
        return find("spring-boot").getVersion();
    }

    @Override
    public Dependency find(String artifactId) {
        return this.byArtifactId.get(artifactId);
    }

}

