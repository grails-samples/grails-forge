package appgenerator

import appgenerator.versions.GrailsVersion
import grails.plugin.cache.CacheEvict
import grails.plugin.cache.Cacheable
import groovy.transform.CompileStatic

@CompileStatic
class VersionService {

    @Cacheable("versions")
    List<String> getSupportedVersions() {
        GrailsVersion.loadFromMaven().collect { it.versionText }
    }

    @CacheEvict("versions")
    void clearVersionsCache() {

    }
}
