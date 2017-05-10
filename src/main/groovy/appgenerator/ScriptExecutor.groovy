package appgenerator

import appgenerator.aether.CustomGrapeEngine
import appgenerator.aether.CustomGrapeEngineFactory
import appgenerator.aether.DependencyResolutionContext
import appgenerator.versions.GrailsVersion
import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap

class ScriptExecutor {

    private static REPO_URI = new URI("https://repo.grails.org/grails/core")
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
        CustomGrapeEngine grapeEngine = CustomGrapeEngineFactory.create(groovyClassLoader, REPO_URI, RESOLUTION_CONTEXT)
        grapeEngine.grab([:], [group: "org.grails", module: "grails-shell", version: version])
    }

}
