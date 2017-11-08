package appgenerator

import groovy.transform.CompileStatic

@CompileStatic
class ProjectOptions {
    String name
    String selectedVersion
    String selectedProfile
    String selectedProjectType
    List<String> selectedFeatures
    List<String> requiredSelectedFeatures
    List<String> defaultSelectedFeatures
    List<String> projectTypes
    List<String> versions
    List<String> profiles
    List<String> features
}
