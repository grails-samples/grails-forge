package appgenerator

class VersionsController {

    static responseFormats = ['json']

    VersionService versionService
    ProfileService profileService

    def grailsVersions() {
        respond versionService.supportedVersions
    }

    def appData() {
        respond versionService.supportedVersions.collect {
            [version: it, profiles: profileService.getProfiles(it)]
        }
    }
}
