package appgenerator

class VersionsController {

    static responseFormats = ['json']

    VersionService versionService
    ProfileService profileService

    def grailsVersions() {
        respond([versions: versionService.supportedVersions])
    }

    def appData() {
        respond([appData: versionService.supportedVersions.collect {
            [version: it, profiles: profileService.getProfiles(it)]
        }])
    }
}
