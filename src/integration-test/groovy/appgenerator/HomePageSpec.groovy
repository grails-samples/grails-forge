package appgenerator

import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration
import spock.lang.IgnoreIf

@Integration
class HomePageSpec extends GebSpec {

    // Tested with Firefox 39.0
    @IgnoreIf({ System.getProperty('geb.env') != 'firefox' && !System.getProperty('download.folder') } )
    def "test a user is able to generate a project without changing any setting"() {
        given:
        String expectedFileDownloadPath = "${System.getProperty('download.folder')}/myapp.zip"
        browser.baseUrl = "http://localhost:$serverPort"

        expect:
        !new File(expectedFileDownloadPath).exists()

        when:
        browser.to HomePage

        then:
        browser.at HomePage

        when:
        HomePage page = browser.page as HomePage
        page.generateProject()
        sleep(10_000) // Wait for the download to finish


        then:
        new File(expectedFileDownloadPath).exists()

        cleanup:
        new File(expectedFileDownloadPath).delete()
    }

    // Tested with Firefox 39.0
    @IgnoreIf({ System.getProperty('geb.env') != 'firefox' && !System.getProperty('download.folder') } )
    def "test a user is able to generate a project with base package name"() {
        given:
        String expectedFileDownloadPath = "${System.getProperty('download.folder')}/myapp2.zip"

        expect:
        !new File(expectedFileDownloadPath).exists()

        when:
        browser.to HomePage

        then:
        browser.at HomePage

        when:
        HomePage page = browser.page as HomePage
        page.setAppName()
        page.generateProject()
        sleep(10_000) // Wait for the download to finish

        then:
        new File(expectedFileDownloadPath).exists()

        cleanup:
        new File(expectedFileDownloadPath).delete()
    }
}
