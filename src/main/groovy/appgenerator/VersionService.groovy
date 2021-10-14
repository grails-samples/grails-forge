package appgenerator

import groovy.transform.CompileStatic
import io.micronaut.cache.annotation.CacheInvalidate
import io.micronaut.cache.annotation.Cacheable

import javax.inject.Inject
import javax.inject.Singleton

@CompileStatic
@Singleton
class VersionService {

    @Inject
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

    @CacheInvalidate("versions")
    void clearVersionsCache() {
        clearAllVersionsCache()
    }

    @CacheInvalidate("allversions")
    void clearAllVersionsCache() {

    }

}
