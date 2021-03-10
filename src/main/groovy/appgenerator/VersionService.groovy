package appgenerator

import groovy.transform.CompileStatic
import io.micronaut.cache.annotation.CacheInvalidate
import io.micronaut.cache.annotation.Cacheable
import org.grails.model.GrailsVersion

import javax.inject.Inject
import javax.inject.Singleton

@CompileStatic
@Singleton
class VersionService {

    @Inject
    GrailsVersionService grailsVersionService

    @Cacheable("versions")
    List<String> getSupportedVersions() {
        grailsVersionService.loadFromMaven().collect { GrailsVersion grailsVersion ->
            grailsVersion.versionText
        }
    }

    @CacheInvalidate("versions")
    void clearVersionsCache() {

    }
}
