package appgenerator.cmd

import grails.validation.Validateable

class ProjectMetaData implements Validateable {
    String name
    String version
    String profile = "web"
    List<String> features = []

    static constraints = {
        appName nullable: true
    }

    String getAppName() {
        return this.name?.split(/[.]/)?.last()
    }
}
