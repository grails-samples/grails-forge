package appgenerator

import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired

@CompileStatic
class VersionsController {

    static responseFormats = ['json']

    @Autowired
    VersionService versionService
    @Autowired
    ProfileService profileService

    def grailsVersions() {
        respond([versions: versionService.supportedVersions])
    }

    def appData() {
        respond([appData: versionService.supportedVersions.collect {
            [version: it, profiles: profileService.getProfiles(it)]
        }])
    }

    def pluginData() {
        respond([pluginData: versionService.supportedVersions.collect {
            [version: it, profiles: profileService.getPluginProfiles(it)]
        }])
    }
}
