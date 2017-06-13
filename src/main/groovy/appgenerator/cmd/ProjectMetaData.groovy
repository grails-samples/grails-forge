package appgenerator.cmd

import grails.validation.Validateable

class ProjectMetaData implements Validateable {
    String name
    String version
    String profile = "web"
    List<String> features = []

    static constraints = {
    }

    String returnAppName() {
        return name?.split(/[.]/)?.last()
    }
}
