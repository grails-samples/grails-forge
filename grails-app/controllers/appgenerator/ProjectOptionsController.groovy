package appgenerator

import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic

@CompileStatic
class ProjectOptionsController {

    static responseFormats = ['json']

    ProjectOptionsService projectOptionsService
    CurlCommandService curlCommandService

    @CompileDynamic
    def index(String projectType, String version, String profile, String name) {
        ProjectOptions projectOptions = projectOptionsService.projectOptionsByProjectTypeAndVersionAndProfileAndFeatures(projectType, version, profile)
        projectOptions.name = defaultName(projectType, name)
        if ( params.features == null ) {
            projectOptions.selectedFeatures = []
            projectOptions.selectedFeatures.addAll(projectOptions.requiredSelectedFeatures)
            projectOptions.selectedFeatures.addAll(projectOptions.defaultSelectedFeatures)
        } else {
            projectOptions.selectedFeatures = params.features.split(',')
        }

        String curlCommand = curlCommandService.curlCommand(projectOptions)

        [projectOptions: projectOptions, curlCommand: curlCommand]
    }

    protected String defaultName(String projectType, String name) {
        if ( projectType?.toLowerCase() == 'application' && name == 'myplugin' ) {
            return 'myapp'
        } else if ( projectType?.toLowerCase() == 'plugin' && name == 'myapp' ) {
            return 'myplugin'
        }
        name
    }
}