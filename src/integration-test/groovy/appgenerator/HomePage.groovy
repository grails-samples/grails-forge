package appgenerator

import geb.Page

class HomePage extends Page {

    static url = '/'
    static at = { title == 'Grails Application Forge' }

    static content = {
        generateProjectButton { $('input#btn-generate') }
        appNameInput { $('input#name') }
    }

    void generateProject() {
        generateProjectButton.click()
    }

    void setAppName() {
        appNameInput.value('com.test.myapp2')
    }
}
