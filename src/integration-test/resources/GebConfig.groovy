import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.firefox.FirefoxProfile
import org.openqa.selenium.htmlunit.HtmlUnitDriver

environments {

    htmlUnit {
        driver = { new HtmlUnitDriver() }
    }

    firefox {
        def fxProfile = new FirefoxProfile()
        def prefs = [
                'browser.download.dir': System.getProperty('download.folder'),
                'browser.helperApps.neverAsk.saveToDisk': 'application/octet-stream'
        ]
        prefs.each { k, v ->
            fxProfile.setPreference(k, v)
        }
        driver = { new FirefoxDriver(fxProfile) }
    }
}