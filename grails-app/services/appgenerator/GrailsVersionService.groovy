package appgenerator

import appgenerator.versions.GrailsVersion
import groovy.util.slurpersupport.GPathResult

class GrailsVersionService {

    static List<GrailsVersion> loadFromMaven() {
        GPathResult xml = new XmlSlurper().parse(new URL("http://repo.grails.org/grails/core/org/grails/grails-core/maven-metadata.xml").openStream())
        GrailsVersion.getSupported(xml.versioning.versions.version*.text())
    }

}

