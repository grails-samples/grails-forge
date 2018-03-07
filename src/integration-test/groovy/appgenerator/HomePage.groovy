package appgenerator

import geb.Page
import geb.module.Checkbox
import geb.module.Select
import geb.module.TextInput

class HomePage extends Page {

    static url = '/'

    static at = { title == 'Grails Application Forge' }

    static content = {
        generateProjectButton { $('#btn-generate', 0) }
        projectTypeSelect { $('#projectType', 0).module(Select) }
        inputName { $('#name', 0).module(TextInput) }
        versionSelect { $('#version', 0).module(Select) }
        profileSelect { $('#profile', 0).module(Select) }
        featureLi { $('#featureList li span', text: it).parent() }
        featureCheckbox { featureLi(it).find('input', type: 'checkbox', 0).module(Checkbox) }
        curlCommandCode { $('#curlCommand', 0) }
    }

    String getCurl() {
        curlCommandCode.text()
    }

    void version(String version) {
        versionSelect.setSelected(version)
    }

    void check(String name) {
        featureCheckbox(name).check()
    }

    void generateProject() {
        generateProjectButton.click()
    }

    void setName(String value) {
        inputName.setText(value)
    }
}
