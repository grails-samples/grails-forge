import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.firefox.FirefoxOptions
import org.openqa.selenium.remote.CapabilityType
import org.openqa.selenium.remote.DesiredCapabilities

DesiredCapabilities cap
if (System.getProperty('download.folder')) {
    String downloadFilepath = System.getProperty('download.folder')
    HashMap<String, Object> chromePrefs = new HashMap<String, Object>()
    chromePrefs.put("profile.default_content_settings.popups", 0)
    chromePrefs.put("download.default_directory", downloadFilepath)
    ChromeOptions options = new ChromeOptions()
    options.setExperimentalOption("prefs", chromePrefs)
    cap = DesiredCapabilities.chrome()
    cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true)
    cap.setCapability(ChromeOptions.CAPABILITY, options)
}

environments {

    // run via “./gradlew -Dgeb.env=chrome iT”
    chrome {
        driver = { cap ? new ChromeDriver(cap) : new ChromeDriver() }
    }

    // run via “./gradlew -Dgeb.env=chromeHeadless iT”
    chromeHeadless {
        driver = {
            ChromeOptions o = new ChromeOptions()
            o.addArguments('headless')
            new ChromeDriver(o)
        }
    }

    // run via “./gradlew -Dgeb.env=firefoxHeadless iT”
    firefoxHeadless {
        driver = {
            FirefoxOptions o = new FirefoxOptions()
            o.addArguments('-headless')
            new FirefoxDriver(o)
        }
    }

    // run via “./gradlew -Dgeb.env=firefox iT”
    firefox {
        driver = { new FirefoxDriver() }
    }
}
