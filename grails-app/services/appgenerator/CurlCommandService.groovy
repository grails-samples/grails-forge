package appgenerator

import appgenerator.profile.Profile
import groovy.transform.CompileStatic

@CompileStatic
class CurlCommandService {

    public static final String DEFAULT_PLUGIN_PROFILE = 'web-plugin'
    public static final String DEFAULT_APP_PROFILE = 'web'

    ProfileService profileService

    String curlCommand(ProjectOptions projectOptions) {
        if ( projectOptions == null ) {
            return null
        }
        StringBuffer sb = new StringBuffer()
        sb.append('curl -O start.grails.org/')
        sb.append(projectOptions.name)
        sb.append('.zip ')

        if ( projectOptions.versions.getAt(0) != projectOptions.selectedVersion ) {
            sb.append("-d version=${projectOptions.selectedVersion} ")
        }

        if ( projectOptions.selectedProjectType?.toLowerCase() == 'plugin' &&
                projectOptions.selectedProfile?.toLowerCase() == 'web-plugin'
        ) {
            sb.append('-d type=plugin ')
        }

        if ( projectOptions.selectedProjectType?.toLowerCase() == 'plugin') {
            if ( projectOptions.selectedProfile != DEFAULT_PLUGIN_PROFILE ) {
                sb.append("-d profile=${projectOptions.selectedProfile} " as String)
            }
        } else if ( projectOptions.selectedProjectType?.toLowerCase() == 'application') {
            if ( projectOptions.selectedProfile != DEFAULT_APP_PROFILE ) {
                sb.append("-d profile=${projectOptions.selectedProfile} " as String)
            }
        }

        List<Profile> profileList
        if ( projectOptions.selectedProjectType?.toLowerCase() == 'plugin') {
            profileList = profileService.getPluginProfiles(projectOptions.selectedVersion)
        } else {
            profileList = profileService.getProfiles(projectOptions.selectedVersion)
        }
        List<String> curlFeatures = projectOptions.selectedFeatures
        List<String> defaultFeatures = []
        if ( profileList ) {
            Profile profile = profileList.find { Profile profile -> profile.name == projectOptions.selectedProfile }
            if( profile ) {
                List<String> requiredFeatures = profile.features.findAll { it.required }*.name as List<String>
                defaultFeatures = profile.features.findAll { it.defaultFeature }*.name as List<String>
                curlFeatures.removeAll(requiredFeatures)
            }
        }

        if ( curlFeatures && defaultFeatures?.sort() != curlFeatures?.sort() ) {
            sb.append("-d features=${curlFeatures?.join(',')} " as String)
        }
        sb.toString().trim()
    }
}

