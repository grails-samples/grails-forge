package appgenerator

import appgenerator.cmd.ProjectMetaData
import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(ProjectGeneratorService)
class ProjectGeneratorServiceSpec extends Specification {

    void "test project app generation 3.1.13"() {
        given: 'the data to generate a project'
            def projectMetaData = new ProjectMetaData(
                name: 'foo',
                version: "3.1.13",
                profile: 'web'
            )

        when: 'generating the project'
            def outputFile = service.getProject(projectMetaData)

        then: 'the project is created'
            outputFile != null
            outputFile.length() > 0
    }

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
}
