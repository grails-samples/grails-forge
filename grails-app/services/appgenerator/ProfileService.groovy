package appgenerator

import appgenerator.profile.Feature
import appgenerator.profile.Profile
import grails.plugin.cache.Cacheable

class ProfileService {

    private static final String PROFILE_COLLECT = """
        Iterable<Feature> requiredFeatures = p.requiredFeatures
        Iterable<Feature> defaultFeatures = p.defaultFeatures
        new appgenerator.profile.Profile(
            name: p.name,
            description: p.description,
            version: p.version,
            features: p.features.toList().collect { f ->
                def feature = new appgenerator.profile.Feature(f.name, f.description)
                if (requiredFeatures.contains(f)) {
                    feature.required = true
                }
                if (defaultFeatures.contains(f)) {
                    feature.defaultFeature = true
                }
                feature
            }
        )
    """

    private static final String APP_PROFILES_SCRIPT = """
        import org.grails.cli.profile.Profile
        import org.grails.cli.profile.Feature
        import org.grails.cli.profile.ProfileRepository
        import org.grails.cli.profile.repository.MavenProfileRepository

        ProfileRepository profileRepository = new MavenProfileRepository()
        return profileRepository.allProfiles.findAll {
                !['plugin', 'profile', 'base'].contains(it.name) &&
                !it.extends.find() { Profile parent -> ['plugin','profile'].contains(parent.name) }
            }.collect { Profile p ->
                $PROFILE_COLLECT
            }
    """.toString()

    private static final String PLUGIN_PROFILES_SCRIPT = """
        import org.grails.cli.profile.Profile
        import org.grails.cli.profile.Feature
        import org.grails.cli.profile.ProfileRepository
        import org.grails.cli.profile.repository.MavenProfileRepository

        ProfileRepository profileRepository = new MavenProfileRepository()
        return profileRepository.allProfiles.findAll {
                !['profile', 'base'].contains(it.name) &&
                (it.extends.find() { Profile parent -> parent.name == 'plugin' } || it.name == 'plugin')
            }.collect { Profile p ->
                $PROFILE_COLLECT
            }
    """.toString()

    private static final String FEATURES_SCRIPT = """
        import org.grails.cli.profile.Profile
        import org.grails.cli.profile.Feature
        import org.grails.cli.profile.ProfileRepository
        import org.grails.cli.profile.repository.MavenProfileRepository

        ProfileRepository profileRepository = new MavenProfileRepository()
        Profile profile = profileRepository.getProfile(profileName)
        if (profile) {
            Iterable<Feature> requiredFeatures = profile.requiredFeatures
            Iterable<Feature> defaultFeatures = profile.defaultFeatures
            return profile.features.toList().collect { f ->
                    def feature = new appgenerator.profile.Feature(f.name, f.description)
                    if (requiredFeatures.contains(f)) {
                        feature.required = true
                    }
                    if (defaultFeatures.contains(f)) {
                        feature.defaultFeature = true
                    }
                    feature
                }
        } else { return null }
    """

    @Cacheable(value="profiles", key = { "#p0" } )
    List<Profile> getProfiles(String version) {
        (List<Profile>)ScriptExecutor.executeScript(version, APP_PROFILES_SCRIPT, "profiles")
    }

    @Cacheable(value="pluginProfiles", key = { "#p0" } )
    List<Profile> getPluginProfiles(String version) {
        (List<Profile>)ScriptExecutor.executeScript(version, PLUGIN_PROFILES_SCRIPT, "pluginProfiles")
    }

    @Cacheable(value="features", key = { "{#p0, #p1}" })
    List<Feature> getFeatures(String version, String profile) {
        (List<Feature>)ScriptExecutor.executeScript(version, FEATURES_SCRIPT, "features", [profileName: profile])
    }
}
