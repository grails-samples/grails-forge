package appgenerator

import appgenerator.profile.Feature
import appgenerator.profile.Profile

class ProfileController implements CurlAware {

    static responseFormats = ['json']

    ProfileService profileService
    VersionService versionService

    def profiles(String version) {
        if (!version.empty && versionService.supportedVersions.contains(version)) {
            List<Profile> profiles
            if (params.type == "plugin") {
                profiles = profileService.getPluginProfiles(version)
            } else {
                profiles = profileService.getProfiles(version)
            }
            if (isCurlRequest()) {
                respond([profiles: profiles], view: 'curlProfiles')
            } else {
                respond([profiles: profiles])
            }
        } else {
            render(status: 404)
        }
    }

    def profile(String version, String profile) {
        if (!version.empty && !profile.empty && versionService.supportedVersions.contains(version)) {
            List<Profile> profiles = profileService.getProfiles(version)
            Profile foundProfile = profiles.find { it.name == profile }
            if (foundProfile) {
                respond([profile: foundProfile])
                return
            }
        }
        render(status: 404)
    }

    def features(String version, String profile) {
        if (!version.empty && !profile.empty && versionService.supportedVersions.contains(version)) {
            List<Feature> features = profileService.getFeatures(version, profile)
            if (features) {
                if (isCurlRequest()) {
                    respond([features: features], view: 'curlFeatures')
                } else {
                    respond([features: features])
                }
                return
            }
        }
        render(status: 404)
    }
}
