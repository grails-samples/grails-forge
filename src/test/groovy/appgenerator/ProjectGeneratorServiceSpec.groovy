package appgenerator

import appgenerator.cmd.ProjectMetaData
import grails.testing.services.ServiceUnitTest
import spock.lang.Specification

class ProjectGeneratorServiceSpec extends Specification implements ServiceUnitTest<ProjectGeneratorService> {

    void "test project app generation 3.2.2"() {
        given: 'the data to generate a project'
        def projectMetaData = new ProjectMetaData(
                name: 'foobar',
                version: "3.2.2",
                profile: 'web'
        )

        when: 'generating the project'
        def outputFile = service.getProject(projectMetaData)

        then: 'the project is created'
        outputFile != null
        outputFile.length() > 0
    }

    void "test project app generation 3.3.0.M1"() {
        given: 'the data to generate a project'
        def projectMetaData = new ProjectMetaData(
                name: 'foobar',
                version: "3.3.0.M1",
                profile: 'web'
        )

        when: 'generating the project'
        def outputFile = service.getProject(projectMetaData)

        then: 'the project is created'
        outputFile != null
        outputFile.length() > 0
    }

    void "test project app generation with base package name"() {
        given: 'the data to generate a project'
        def projectMetaData = new ProjectMetaData(
                name: 'com.test.foobar',
                version: "3.3.0.M1",
                profile: 'web'
        )

        when: 'generating the project'
        def outputFile = service.getProject(projectMetaData)

        then: 'the project is created'
        outputFile != null
        outputFile.length() > 0
    }
}
