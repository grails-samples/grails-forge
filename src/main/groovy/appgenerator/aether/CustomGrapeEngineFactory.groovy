package appgenerator.aether

import org.apache.maven.repository.internal.MavenRepositorySystemUtils
import org.eclipse.aether.DefaultRepositorySystemSession
import org.eclipse.aether.RepositorySystem
import org.eclipse.aether.connector.basic.BasicRepositoryConnectorFactory
import org.eclipse.aether.impl.DefaultServiceLocator
import org.eclipse.aether.internal.impl.DefaultRepositorySystem
import org.eclipse.aether.repository.RemoteRepository
import org.eclipse.aether.repository.RepositoryPolicy
import org.eclipse.aether.spi.connector.RepositoryConnectorFactory
import org.eclipse.aether.spi.connector.transport.TransporterFactory
import org.eclipse.aether.spi.locator.ServiceLocator
import org.eclipse.aether.transport.file.FileTransporterFactory
import org.eclipse.aether.transport.http.HttpTransporterFactory

/**
 * Created by jameskleeh on 5/10/17.
 */
class CustomGrapeEngineFactory {
    static CustomGrapeEngine create(GroovyClassLoader classLoader,
                                           URI repoUri,
                                           DependencyResolutionContext dependencyResolutionContext) {

        RepositorySystem repositorySystem = createServiceLocator()
                .getService(RepositorySystem.class);

        DefaultRepositorySystemSession repositorySystemSession = MavenRepositorySystemUtils
                .newSession();

        ServiceLoader<RepositorySystemSessionAutoConfiguration> autoConfigurations = ServiceLoader
                .load(RepositorySystemSessionAutoConfiguration.class);

        for (RepositorySystemSessionAutoConfiguration autoConfiguration : autoConfigurations) {
            autoConfiguration.apply(repositorySystemSession, repositorySystem);
        }

        new DefaultRepositorySystemSessionAutoConfiguration()
                .apply(repositorySystemSession, repositorySystem);

        return new CustomGrapeEngine(classLoader, repositorySystem,
                repositorySystemSession, [createRepository(repoUri)],
                dependencyResolutionContext);
    }

    private static ServiceLocator createServiceLocator() {
        DefaultServiceLocator locator = MavenRepositorySystemUtils.newServiceLocator();
        locator.addService(RepositorySystem.class, DefaultRepositorySystem.class);
        locator.addService(RepositoryConnectorFactory.class,
                BasicRepositoryConnectorFactory.class);
        locator.addService(TransporterFactory.class, HttpTransporterFactory.class);
        locator.addService(TransporterFactory.class, FileTransporterFactory.class);
        return locator;
    }

    private static RemoteRepository createRepository(URI uri) {
        RemoteRepository.Builder builder = new RemoteRepository.Builder(
                "yourRepo", "default",
                uri.toASCIIString());

        builder.build()
    }
}
