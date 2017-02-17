package appgenerator

import geb.Browser
import grails.test.mixin.integration.Integration
import spock.lang.IgnoreIf
import spock.lang.Specification

@Integration
class HomePageSpec extends Specification {

    // Tested with Firefox 39.0
    @IgnoreIf({ System.getProperty('geb.env') != 'firefox' && !System.getProperty('download.folder') } )
    def "test a user is able to generate a project without changing any setting"() {
        given:
        def expectedFileDownloadPath = "${System.getProperty('download.folder')}/myapp.zip"
        def browser = new Browser()
        browser.baseUrl = "http://localhost:$serverPort"

        when:
        browser.to HomePage
        sleep(5_000) // 'Wait for the page to load the async features'

        then:
        !new File(expectedFileDownloadPath).exists()
        browser.at HomePage

        when:
        def page = browser.page as HomePage
        page.generateProject()
        sleep(2_000) // Wait for the download to finish


        then:
        new File(expectedFileDownloadPath).exists()
    }
}
