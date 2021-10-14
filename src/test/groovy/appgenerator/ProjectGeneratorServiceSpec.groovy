package appgenerator

import appgenerator.cmd.ProjectMetaData
import spock.lang.Shared
import spock.lang.Specification

class ProjectGeneratorServiceSpec extends Specification {

    @Shared
    ProjectGeneratorService service = new ProjectGeneratorService()

    void "test project app generation 5.0.0"() {
        given: 'the data to generate a project'
        def projectMetaData = new ProjectMetaData(
                name: 'foobar',
                version: "5.0.0",
                profile: 'web'
        )

        when: 'generating the project'
        def outputFile = service.getProject(projectMetaData)

        then: 'the project is created'
        outputFile != null
        outputFile.length() > 0
    }

    void "test project app generation 5.0.0-RC3"() {
        given: 'the data to generate a project'
        def projectMetaData = new ProjectMetaData(
                name: 'foobar',
                version: "5.0.0-RC3",
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
                version: "5.0.0",
                profile: 'web'
        )

        when: 'generating the project'
        def outputFile = service.getProject(projectMetaData)

        then: 'the project is created'
        outputFile != null
        outputFile.length() > 0
    }
}
