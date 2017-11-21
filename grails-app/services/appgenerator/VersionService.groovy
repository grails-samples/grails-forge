package appgenerator

import appgenerator.versions.GrailsVersion
import grails.plugin.cache.CacheEvict
import grails.plugin.cache.Cacheable
import groovy.transform.CompileStatic

@CompileStatic
class VersionService {

    GrailsVersionService grailsVersionService

    @Cacheable("versions")
    List<String> getSupportedVersions() {
        grailsVersionService.loadFromMaven().collect { it.versionText }
    }

    @CacheEvict("versions")
    void clearVersionsCache() {

    }
}
