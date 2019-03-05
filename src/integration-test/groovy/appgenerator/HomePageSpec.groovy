package appgenerator

import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration
import org.openqa.selenium.Keys
import spock.lang.IgnoreIf
import spock.util.concurrent.PollingConditions

@Integration
class HomePageSpec extends GebSpec {

    @IgnoreIf({ ['firefox', 'firefoxHeadless'].contains(sys['geb.env']) || env['TRAVIS'] })
    def "if you select features and change name features changes are not lost"() {
        when:
        HomePage homePage = to HomePage
        homePage.version('3.3.2')

        then:
        waitFor { homePage.curl == 'curl -O start.grails.org/myapp.zip -d version=3.3.2' }

        when: 'if you change name curl commands gets updated'
        homePage.name = 'myappcool'

        then:
        waitFor { homePage.curl == 'curl -O start.grails.org/myappcool.zip -d version=3.3.2' }

        when:
        homePage.check('json-views')

        then:
        waitFor { homePage.curl.contains('json-views') }

        when: 'if you delete a character'
        homePage.inputName << Keys.BACK_SPACE

        then: 'the name in curl command changes'
        waitFor 15, { homePage.curl.contains('myappcoo.zip') }

        when:
        homePage.name = 'app'

        then:
        waitFor { homePage.curl.contains('app.zip') }

        when:
        homePage.version('3.3.3')

        then:
        waitFor { homePage.curl.contains  'version=3.3.3' }

        when:
        homePage.profile('vue')

        then:
        waitFor { homePage.curl == 'curl -O start.grails.org/app.zip -d version=3.3.3 -d profile=vue' }

        and:
        ['hibernate5','json-views'] == homePage.checkedFeatures()
    }

    @IgnoreIf({ !(sys['geb.env'] == 'chrome') || !sys['download.folder'] } )
    def "test a user is able to generate a project without changing any setting"() {
        given:
        PollingConditions conditions = new PollingConditions(timeout: 30)

        String expectedFileDownloadPath = "${System.getProperty('download.folder')}/myapp.zip".toString()

        when:
        HomePage homePage = to HomePage
        homePage.generateProject()

        then:
        conditions.eventually { new File(expectedFileDownloadPath).exists() }

        cleanup:
        new File(expectedFileDownloadPath).delete()
    }

    @IgnoreIf({ !(sys['geb.env'] == 'chrome') || !sys['download.folder'] } )
    def "if you set the name input field with #packageName.#appname it is possible to generate a project"(String packageName, String appname) {
        given:
        PollingConditions conditions = new PollingConditions(timeout: 30)
        String expectedFileDownloadPath = "${System.getProperty('download.folder')}/${packageName}${appname}.zip"

        when:
        HomePage homePage = to HomePage
        homePage.name = "${packageName}${appname}".toString()
        homePage.generateProject()

        then:
        conditions.eventually { new File(expectedFileDownloadPath).exists() }

        cleanup:
        new File(expectedFileDownloadPath).delete()

        where:
        packageName | appname
        'demo'      | 'test2'
    }
}
