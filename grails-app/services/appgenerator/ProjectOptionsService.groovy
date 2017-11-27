package appgenerator

import appgenerator.profile.Feature
import appgenerator.profile.Profile
import org.grails.model.GrailsVersion
import grails.config.Config
import grails.core.support.GrailsConfigurationAware
import groovy.transform.CompileStatic

@CompileStatic
class ProjectOptionsService  implements  GrailsConfigurationAware {

    VersionService versionService
    ProfileService profileService
    ProjectTypeService projectTypeService
    String defaultProjectName
    String defaultAppProfile
    String defaultPluginProfile

    @Override
    void setConfiguration(Config co) {
        defaultProjectName = co.getProperty('appgenerator.defaultProjectName', String, 'myapp')
        defaultAppProfile = co.getProperty('appgenerator.defaultAppProfile', String, 'web')
        defaultPluginProfile = co.getProperty('appgenerator.defaultPluginProfile', String, 'web-plugin')
    }

    List<GrailsVersion> sortedSupportedVersions() {
        List<GrailsVersion> versionList = versionService.supportedVersions.collect { String version ->
            GrailsVersion.build(version)
        }
        Collections.sort(versionList)
        versionList = versionList.reverse()
        versionList
    }

    List<Profile> profileListByProjectType(String projectType, GrailsVersion version) {
        if ( projectType?.toLowerCase() == 'application' ) {
            return profileService.getProfiles(version.versionText)
        } else if ( projectType?.toLowerCase() == 'plugin' ) {
            return profileService.getPluginProfiles(version.versionText)
        }
        null
    }

    ProjectOptions defaultProjectOptions() {
        List<String> projectTypeList = projectTypeService.findAllProjectTypes()
        String projectType = projectTypeList[0]
        String selectedProjectType = selectedProjectType(projectTypeList, projectType)
        List<GrailsVersion> versionList = sortedSupportedVersions()
        GrailsVersion selectedVersion = selectedGrailsVersion(versionList, null)
        List<Profile> profileList = profileListByProjectType(selectedProjectType, selectedVersion)
        Profile selectedProfile = selectedProfile(profileList, null, projectType)

        ProjectOptions projectOptions = projectOptionsByProjectTypeAndVersionAndProfile(projectType,
                selectedVersion,
                selectedProfile,
                projectTypeList,
                versionList,
                profileList)
        projectOptions.name = defaultProjectName
        projectOptions
    }

    String defaultProfileForProjectType(String projectType) {
        if ( projectType?.toLowerCase( )== 'application' ) {
            return 'web'
        } else if ( projectType?.toLowerCase( )== 'plugin' ) {
            return 'plugin'
        }
        null
    }

    Profile defaultProfile(String projectType, List<Profile> profileList) {
        String defaultProfile = defaultProfileForProjectType(projectType)
        Profile profile = profileList.find { Profile p -> p.name == defaultProfile }
        if ( !profile ) {
            profile = profileList.getAt(0)
        }
        profile
    }

    ProjectOptions projectOptionsByProjectTypeAndVersionAndProfile(String projectType,
                                                                   GrailsVersion version,
                                                                   Profile profile,
                                                                   List<String> projectTypeList,
                                                                   List<GrailsVersion> versionList,
                                                                   List<Profile> profileList
    ) {

        List<Feature> requiredFeatures = profile.features.findAll {  Feature feature ->
            feature.required
        }
        List<Feature> defaultFeatures = profile.features.findAll {  Feature feature ->
            feature.defaultFeature
        }
        ProjectOptions projectOptions = new ProjectOptions()
        projectOptions.with {
            versions = versionList.collect { GrailsVersion grailsVersion -> grailsVersion.versionText }
            selectedVersion = version.versionText
            projectTypes = projectTypeList
            selectedProjectType = projectType
            profiles = profileList.collect { Profile p -> p.name } as List<String>
            selectedProfile = profile.name
            features = profile.features.collect { Feature feature -> feature.name }
            defaultSelectedFeatures = defaultFeatures.collect { Feature feature -> feature.name }
            requiredSelectedFeatures = requiredFeatures.collect { Feature feature -> feature.name }
        }
        projectOptions
    }

    GrailsVersion selectedGrailsVersion(List<GrailsVersion> versionList, String version) {

        GrailsVersion selectedVersion = versionList.find { GrailsVersion grailsVersion -> grailsVersion.versionText == version }
        if ( !selectedVersion ) {
            selectedVersion = versionList.findAll { !it.isSnapshot() }?.getAt(0)
            if ( !selectedVersion ) {
                selectedVersion = versionList.getAt(0)
            }
        }
        selectedVersion
    }

    String selectedProjectType(List<String> projectTypeList, String projectType) {
        String selectedProjectType = projectTypeList.find { it == projectType }
        if ( !selectedProjectType ) {
            selectedProjectType = projectTypeList.getAt(0)
        }
        selectedProjectType
    }

    Profile selectedProfile(List<Profile> profileList, String profile, String projectType) {

        Profile selectedProfile = profileList.find { Profile p -> p.name == profile }
        if ( !selectedProfile ) {
            selectedProfile = profileList.find { Profile p -> p.name == defaultProfileNameByProjectType(projectType) }
        }
        if ( !selectedProfile ) {
            selectedProfile  = profileList.getAt(0)
        }
        selectedProfile
    }

    ProjectOptions projectOptionsByProjectTypeAndVersionAndProfileAndFeatures(String projectType, String version, String profile) {
        List<String> projectTypeList = projectTypeService.findAllProjectTypes()
        String selectedProjectType = selectedProjectType(projectTypeList, projectType)
        List<GrailsVersion> versionList = sortedSupportedVersions()
        GrailsVersion selectedVersion = selectedGrailsVersion(versionList, version)
        List<Profile> profileList = profileListByProjectType(selectedProjectType, selectedVersion)
        Profile selectedProfile = selectedProfile(profileList, profile, projectType)

        projectOptionsByProjectTypeAndVersionAndProfile(selectedProjectType,
                selectedVersion,
                selectedProfile,
                projectTypeList,
                versionList,
                profileList)
    }

    String defaultProfileNameByProjectType(String projectType) {
        if( projectType?.toLowerCase() == 'application' ) {
            return defaultAppProfile
        }
        if( projectType?.toLowerCase() == 'plugin' ) {
            return defaultPluginProfile
        }
        null
    }
}