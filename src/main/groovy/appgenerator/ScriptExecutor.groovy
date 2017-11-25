package appgenerator

import org.grails.model.GrailsVersion
import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap
import org.springframework.boot.cli.compiler.grape.AetherGrapeEngineFactory
import org.springframework.boot.cli.compiler.grape.DependencyResolutionContext
import org.springframework.boot.cli.compiler.grape.RepositoryConfiguration

class ScriptExecutor {

    private static List<RepositoryConfiguration> REPO_CONFIG = [new RepositoryConfiguration("grailsCentral", new URI("https://repo.grails.org/grails/core"), true)]

    private static DependencyResolutionContext RESOLUTION_CONTEXT = new DependencyResolutionContext()

    private static Map<String, GroovyClassLoader> cachedClassLoaders = new ConcurrentLinkedHashMap.Builder<String, GroovyClassLoader>()
            .maximumWeightedCapacity(200)
            .build().withDefault { String grailsVersion ->

        GroovyClassLoader gcl = new GroovyClassLoader(Thread.currentThread().contextClassLoader)
        resolveGrailsShell(gcl, grailsVersion)
        gcl
    }

    static executeScript(String grailsVersion, String scriptText, String scriptName, Map bindingParams = [:]) {
        ClassLoader oldClassLoader = Thread.currentThread().contextClassLoader
        GroovyClassLoader groovyClassLoader = cachedClassLoaders.get(grailsVersion)

        GrailsVersion version = GrailsVersion.build(grailsVersion)
        if (version.snapshot && version.getSnapshot().buildSnapshot) {
            resolveGrailsShell(groovyClassLoader, grailsVersion)
        }

        Thread.currentThread().setContextClassLoader(groovyClassLoader)

        def result

        try {
            Class scriptClass

            try {
                scriptClass = groovyClassLoader.loadClass(scriptName)
            } catch (ClassNotFoundException e) {
                scriptClass = groovyClassLoader.parseClass(scriptText, scriptName + ".groovy")
            }

            def script = scriptClass.newInstance()
            script.binding = new Binding(bindingParams)
            result = script.run()
        } finally {
            Thread.currentThread().setContextClassLoader(oldClassLoader)
        }

        return result
    }

    private static void resolveGrailsShell(GroovyClassLoader groovyClassLoader, String version) {
        def engine = AetherGrapeEngineFactory.create(groovyClassLoader, REPO_CONFIG, RESOLUTION_CONTEXT, false)
        engine.grab([:], [group: "org.grails", module: "grails-shell", version: version])
    }

}
