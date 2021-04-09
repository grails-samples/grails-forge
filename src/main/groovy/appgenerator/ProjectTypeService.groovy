package appgenerator

import groovy.transform.CompileStatic

import javax.inject.Singleton

@CompileStatic
@Singleton
class ProjectTypeService {

    List<String> findAllProjectTypes() {
        ['Application', 'Plugin']
    }
}