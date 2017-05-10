package appgenerator.aether

/**
 * An encapsulation of dependency management information.
 *
 * @author Andy Wilkinson
 * @since 1.3.0
 */
public interface DependencyManagement {

    /**
     * Returns the managed dependencies.
     * @return the managed dependencies
     */
    List<Dependency> getDependencies();

    /**
     * Returns the managed version of Spring Boot. May be {@code null}.
     * @return the Spring Boot version, or {@code null}
     */
    String getSpringBootVersion();

    /**
     * Finds the managed dependency with the given {@code artifactId}.
     * @param artifactId The artifact ID of the dependency to find
     * @return the dependency, or {@code null}
     */
    Dependency find(String artifactId);

}
