package appgenerator

import geb.Page

class HomePage extends Page {

    static url = '/#/index'

    static at = { title == 'Grails Application Forge' }

    static content = {
        generateProjectButton(wait: true) { $('button#btn-generate', 0) }
    }

    void generateProject() {
        generateProjectButton.click()
    }

}
