package appgenerator.aether

import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.RepositorySystem;

/**
 * Strategy that can be used to apply some auto-configuration during the installation of
 * an {@link org.springframework.boot.cli.compiler.grape.AetherGrapeEngine}.
 *
 * @author Andy Wilkinson
 */
public interface RepositorySystemSessionAutoConfiguration {

    /**
     * Apply the configuration.
     * @param session the repository system session
     * @param repositorySystem the repository system
     */
    void apply(DefaultRepositorySystemSession session, RepositorySystem repositorySystem);

}
