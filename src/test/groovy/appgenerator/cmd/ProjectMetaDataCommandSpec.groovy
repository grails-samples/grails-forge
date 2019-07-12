package appgenerator.cmd

import grails.testing.web.controllers.ControllerUnitTest
import spock.lang.Specification
import spock.lang.Unroll

class ProjectMetaDataCommandSpec extends Specification implements ControllerUnitTest<ProjectMetaData> {

    ProjectMetaData cmd = new ProjectMetaData()

    void 'validate all params'() {
        given: 'all the params'
            cmd.name = 'foo'
            cmd.version = "3.2.2"
            cmd.profile = 'web'
            cmd.features = ['mongodb']

        when: 'validate'
            cmd.validate()

        then: 'the param is valid'
            !cmd.hasErrors()
    }

    @Unroll
    void 'check all mandatory params'() {
        given: 'an instance with some errors'
            cmd.name = name
            cmd.version = version
            cmd.profile = profile
            cmd.features = []

        when: 'validate'
            cmd.validate()

        then: 'the command is not valid'
            cmd.hasErrors()
            cmd.errors[field].code == error

        where:
            name  | version  | profile | field     | error
            null  | "3.2.2"  | 'web'   | 'name'    | 'nullable'
            'foo' | null     | 'web'   | 'version' | 'nullable'
            'foo' | "3.3.0"  | null    | 'profile' | 'nullable'
    }

}
