package appgenerator.aether

import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.repository.LocalRepository;
import org.eclipse.aether.repository.LocalRepositoryManager;
import org.eclipse.aether.repository.ProxySelector;
import org.eclipse.aether.util.repository.JreProxySelector;

import org.springframework.util.StringUtils;

/**
 * A {@link RepositorySystemSessionAutoConfiguration} that, in the absence of any
 * configuration, applies sensible defaults.
 *
 * @author Andy Wilkinson
 */
public class DefaultRepositorySystemSessionAutoConfiguration
        implements RepositorySystemSessionAutoConfiguration {

    @Override
    public void apply(DefaultRepositorySystemSession session,
                      RepositorySystem repositorySystem) {

        if (session.getLocalRepositoryManager() == null) {
            LocalRepository localRepository = new LocalRepository(getM2RepoDirectory());
            LocalRepositoryManager localRepositoryManager = repositorySystem
                    .newLocalRepositoryManager(session, localRepository);
            session.setLocalRepositoryManager(localRepositoryManager);
        }

        ProxySelector existing = session.getProxySelector();
        if (existing == null || !(existing instanceof CompositeProxySelector)) {
            JreProxySelector fallback = new JreProxySelector();
            ProxySelector selector = existing == null ? fallback
                    : new CompositeProxySelector(Arrays.asList(existing, fallback));
            session.setProxySelector(selector);
        }
    }

    private File getM2RepoDirectory() {
        return new File(getDefaultM2HomeDirectory(), "repository");
    }

    private File getDefaultM2HomeDirectory() {
        String mavenRoot = System.getProperty("maven.home");
        if (StringUtils.hasLength(mavenRoot)) {
            return new File(mavenRoot);
        }
        return new File(System.getProperty("user.home"), ".m2");
    }
}
