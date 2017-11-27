package appgenerator

import groovy.transform.CompileStatic

@CompileStatic
class ProjectTypeService {

    List<String> findAllProjectTypes() {
        ['Application', 'Plugin']
    }
}