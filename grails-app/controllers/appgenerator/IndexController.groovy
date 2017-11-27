package appgenerator

import groovy.transform.CompileStatic

@CompileStatic
class IndexController implements CurlAware {

    ProjectOptionsService projectOptionsService
    CurlCommandService curlCommandService

    def index() {
        if(isCurlRequest()) {
            response.contentType = "text/plain"
            response.outputStream << this.class.classLoader.getResourceAsStream('curl_instructions.txt').bytes
            return
        }
        ProjectOptions projectOptions = projectOptionsService.defaultProjectOptions()
        projectOptions.selectedFeatures = [projectOptions.requiredSelectedFeatures, projectOptions.defaultSelectedFeatures].flatten() as List<String>
        String curlCommand = curlCommandService.curlCommand(projectOptions)
        render view: '/index', model: [projectOptions: projectOptions, curlCommand: curlCommand]
    }

}
