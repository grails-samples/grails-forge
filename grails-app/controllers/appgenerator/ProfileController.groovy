package appgenerator

import appgenerator.profile.Feature
import appgenerator.profile.Profile

class ProfileController implements CurlAware {

    static responseFormats = ['json']

    ProfileService profileService
    VersionService versionService

    def profiles(String version) {
        if (!version.empty && versionService.supportedVersions.contains(version)) {
            List<Profile> profiles = profileService.getProfiles(version)
            if (isCurlRequest()) {
                respond profiles, view: 'curlProfiles'
            } else {
                respond profiles
            }
        } else {
            render(status: 404)
        }
    }

    def features(String version, String profile) {
        if (!version.empty && !profile.empty && versionService.supportedVersions.contains(version)) {
            List<Feature> features = profileService.getFeatures(version, profile)
            if (features) {
                if (isCurlRequest()) {
                    respond features, view: 'curlFeatures'
                } else {
                    respond features
                }
                return
            }
        }
        render(status: 404)
    }
}
