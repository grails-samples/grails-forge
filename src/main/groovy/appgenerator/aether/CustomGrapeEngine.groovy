package appgenerator.aether

import org.eclipse.aether.DefaultRepositorySystemSession
import org.eclipse.aether.RepositorySystem
import org.eclipse.aether.artifact.Artifact
import org.eclipse.aether.artifact.DefaultArtifact
import org.eclipse.aether.collection.CollectRequest
import org.eclipse.aether.graph.Dependency
import org.eclipse.aether.graph.Exclusion
import org.eclipse.aether.repository.RemoteRepository
import org.eclipse.aether.resolution.ArtifactResolutionException
import org.eclipse.aether.resolution.ArtifactResult
import org.eclipse.aether.resolution.DependencyRequest
import org.eclipse.aether.resolution.DependencyResult
import org.eclipse.aether.util.artifact.JavaScopes
import org.eclipse.aether.util.filter.DependencyFilterUtils


class CustomGrapeEngine {

    private static final Collection<Exclusion> WILDCARD_EXCLUSION;

    static {
        List<Exclusion> exclusions = new ArrayList<Exclusion>();
        exclusions.add(new Exclusion("*", "*", "*", "*"));
        WILDCARD_EXCLUSION = Collections.unmodifiableList(exclusions);
    }

    private final DependencyResolutionContext resolutionContext;

    private final GroovyClassLoader classLoader;

    private final DefaultRepositorySystemSession session;

    private final RepositorySystem repositorySystem;

    private final List<RemoteRepository> repositories;

    CustomGrapeEngine(GroovyClassLoader classLoader,
                             RepositorySystem repositorySystem,
                             DefaultRepositorySystemSession repositorySystemSession,
                             List<RemoteRepository> remoteRepositories,
                             DependencyResolutionContext resolutionContext) {
        this.classLoader = classLoader;
        this.repositorySystem = repositorySystem;
        this.session = repositorySystemSession;
        this.resolutionContext = resolutionContext;
        this.repositories = new ArrayList<RemoteRepository>();
        List<RemoteRepository> remotes = new ArrayList<RemoteRepository>(
                remoteRepositories);
        Collections.reverse(remotes); // priority is reversed in addRepository
        for (RemoteRepository repository : remotes) {
            addRepository(repository);
        }
    }

    public Object grab(Map args, Map... dependencyMaps) {
        List<Dependency> dependencies = createDependencies(dependencyMaps, []);
        try {
            List<File> files = resolve(dependencies);
            GroovyClassLoader classLoader = getClassLoader(args);
            for (File file : files) {
                classLoader.addURL(file.toURI().toURL());
            }
        }
        catch (ArtifactResolutionException ex) {
            throw new DependencyResolutionFailedException(ex);
        }
        catch (MalformedURLException ex) {
            throw new DependencyResolutionFailedException(ex);
        }
        return null;
    }

    private List<Dependency> createDependencies(Map<?, ?>[] dependencyMaps,
                                                List<Exclusion> exclusions) {
        List<Dependency> dependencies = new ArrayList<Dependency>(dependencyMaps.length);
        for (Map<?, ?> dependencyMap : dependencyMaps) {
            dependencies.add(createDependency(dependencyMap, exclusions));
        }
        return dependencies;
    }

    private Dependency createDependency(Map<?, ?> dependencyMap,
                                        List<Exclusion> exclusions) {
        Artifact artifact = createArtifact(dependencyMap);
        return new Dependency(artifact, JavaScopes.COMPILE, null, WILDCARD_EXCLUSION);
    }

    private Artifact createArtifact(Map<?, ?> dependencyMap) {
        String group = (String) dependencyMap.get("group");
        String module = (String) dependencyMap.get("module");
        String version = (String) dependencyMap.get("version");
        if (version == null) {
            version = this.resolutionContext.getManagedVersion(group, module);
        }
        String classifier = (String) dependencyMap.get("classifier");
        String type = determineType(dependencyMap);
        return new DefaultArtifact(group, module, classifier, type, version);
    }

    private String determineType(Map<?, ?> dependencyMap) {
        String type = (String) dependencyMap.get("type");
        String ext = (String) dependencyMap.get("ext");
        if (type == null) {
            type = ext;
            if (type == null) {
                type = "jar";
            }
        } else if (ext != null && !type.equals(ext)) {
            throw new IllegalArgumentException(
                    "If both type and ext are specified they must have the same value");
        }
        return type;
    }

    private List<Dependency> getDependencies(DependencyResult dependencyResult) {
        List<Dependency> dependencies = new ArrayList<Dependency>();
        for (ArtifactResult artifactResult : dependencyResult.getArtifactResults()) {
            dependencies.add(
                    new Dependency(artifactResult.getArtifact(), JavaScopes.COMPILE));
        }
        return dependencies;
    }

    private List<File> getFiles(DependencyResult dependencyResult) {
        List<File> files = new ArrayList<File>();
        for (ArtifactResult result : dependencyResult.getArtifactResults()) {
            files.add(result.getArtifact().getFile());
        }
        return files;
    }

    private GroovyClassLoader getClassLoader(Map args) {
        GroovyClassLoader classLoader = (GroovyClassLoader) args.get("classLoader");
        return (classLoader == null ? this.classLoader : classLoader);
    }


    protected void addRepository(RemoteRepository repository) {
        if (this.repositories.contains(repository)) {
            return;
        }
        repository = getPossibleMirror(repository);
        repository = applyProxy(repository);
        repository = applyAuthentication(repository);
        this.repositories.add(0, repository);
    }

    private RemoteRepository getPossibleMirror(RemoteRepository remoteRepository) {
        RemoteRepository mirror = this.session.getMirrorSelector()
                .getMirror(remoteRepository);
        if (mirror != null) {
            return mirror;
        }
        return remoteRepository;
    }

    private RemoteRepository applyProxy(RemoteRepository repository) {
        if (repository.getProxy() == null) {
            RemoteRepository.Builder builder = new RemoteRepository.Builder(repository);
            builder.setProxy(this.session.getProxySelector().getProxy(repository));
            repository = builder.build();
        }
        return repository;
    }

    private RemoteRepository applyAuthentication(RemoteRepository repository) {
        if (repository.getAuthentication() == null) {
            RemoteRepository.Builder builder = new RemoteRepository.Builder(repository);
            builder.setAuthentication(this.session.getAuthenticationSelector()
                    .getAuthentication(repository));
            repository = builder.build();
        }
        return repository;
    }


    private List<File> resolve(List<Dependency> dependencies)
            throws ArtifactResolutionException {
        try {
            CollectRequest collectRequest = getCollectRequest(dependencies);
            DependencyRequest dependencyRequest = getDependencyRequest(collectRequest);
            DependencyResult result = this.repositorySystem
                    .resolveDependencies(this.session, dependencyRequest);
            addManagedDependencies(result);
            return getFiles(result);
        }
        catch (Exception ex) {
            throw new DependencyResolutionFailedException(ex);
        }
        finally {
        }
    }

    private CollectRequest getCollectRequest(List<Dependency> dependencies) {
        CollectRequest collectRequest = new CollectRequest((Dependency) null,
                dependencies, new ArrayList<RemoteRepository>(this.repositories));
        collectRequest
                .setManagedDependencies(this.resolutionContext.getManagedDependencies());
        return collectRequest;
    }

    private DependencyRequest getDependencyRequest(CollectRequest collectRequest) {
        DependencyRequest dependencyRequest = new DependencyRequest(collectRequest,
                DependencyFilterUtils.classpathFilter(JavaScopes.COMPILE,
                        JavaScopes.RUNTIME));
        return dependencyRequest;
    }

    private void addManagedDependencies(DependencyResult result) {
        this.resolutionContext.addManagedDependencies(getDependencies(result));
    }
}