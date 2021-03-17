package appgenerator

import org.grails.model.GrailsVersion
import grails.plugin.cache.CacheEvict
import grails.plugin.cache.Cacheable
import groovy.transform.CompileStatic

@CompileStatic
class VersionService {

    GrailsVersionService grailsVersionService

    @Cacheable("versions")
    List<String> getSupportedVersions() {
        grailsVersionService.loadLatestFromMaven().collect { GrailsVersion grailsVersion ->
            grailsVersion.versionText
        }
    }

    @Cacheable("allversions")
    List<String> getAllSupportedVersions() {
        grailsVersionService.loadAllVersionFromMaven().collect { GrailsVersion grailsVersion ->
            grailsVersion.versionText
        }
    }

    @CacheEvict("versions")
    void clearVersionsCache() {
        clearAllVersionsCache()
    }

    @CacheEvict("allversions")
    private void clearAllVersionsCache() {

    }

}
