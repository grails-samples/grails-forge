package appgenerator

import appgenerator.versions.GrailsVersion
import grails.config.Config
import grails.core.support.GrailsConfigurationAware
import grails.plugin.cache.CacheEvict
import grails.plugin.cache.Cacheable
import groovy.transform.CompileStatic

@CompileStatic
class VersionService implements GrailsConfigurationAware {

    List<String> excludeSuffixes

    @Cacheable("versions")
    List<String> getSupportedVersions() {
        GrailsVersion.loadFromMaven().collect { it.versionText }.findAll { String version ->
            !excludeSuffixes.any { String suffix -> version.endsWith(suffix) }
        }
    }

    @CacheEvict("versions")
    void clearVersionsCache() {

    }

    @Override
    void setConfiguration(Config co) {
        excludeSuffixes = co.getProperty('appgenerator.version.excludeSuffixes', List, ['BUILD-SNAPSHOT'])
    }
}
