package appgenerator

import grails.plugins.rest.client.RestResponse
import grails.test.mixin.integration.Integration
import spock.lang.Specification
import static org.springframework.http.HttpStatus.OK

/**
 * Created by jameskleeh on 6/14/17.
 */
@Integration
class VersionsControllerIntegrationSpec extends Specification implements RestSpec {

    void "test versions"() {
        RestResponse resp = get('/versions')

        expect:"The response is correct"
        resp.json.size() > 0
        resp.status == OK.value()
    }

    void "test appData"() {
        RestResponse resp = get('/appData')

        expect:"The response is correct"
        resp.json.size() > 0
        resp.status == OK.value()
        resp.text.startsWith('[{"version":"3.1.13","profiles":[{"version":"3.1.9","features":[{"required":true,"defaultFeature":false,"description":"Adds Asset Pipeline to a Grails project","name":"asset-pipeline"},{"required":false,"defaultFeature":true,"description":"Adds GORM for Hibernate to the project","name":"hibernate"},{"required":true,"defaultFeature":false,"description":"Adds support for JSON Views to the project","name":"json-views"},{"required":false,"defaultFeature":false,"description":"Adds LESS Transpiler Asset Pipeline to a Grails project","name":"less-asset-pipeline"},{"required":false,"defaultFeature":false,"description":"Adds support for Markup Views to the project","name":"markup-views"},{"required":false,"defaultFeature":false,"description":"Adds GORM for MongoDB to the project","name":"mongodb"},{"required":false,"defaultFeature":false,"description":"Adds GORM for Neo4j to the project","name":"neo4j"},{"required":false,"defaultFeature":false,"description":"Adds Spring Security REST to the project","name":"security"}],"description":"A profile for creating applications using AngularJS","name":"angular"},{"version":"3.1.9","features":[{"required":false,"defaultFeature":false,"description":"Adds Asset Pipeline to a Grails project","name":"asset-pipeline"},{"required":false,"defaultFeature":true,"description":"Adds GORM for Hibernate to the project","name":"hibernate"},{"required":true,"defaultFeature":false,"description":"Adds support for JSON Views to the project","name":"json-views"},{"required":false,"defaultFeature":false,"description":"Adds LESS Transpiler Asset Pipeline to a Grails project","name":"less-asset-pipeline"},{"required":false,"defaultFeature":false,"description":"Adds support for Markup Views to the project","name":"markup-views"},{"required":false,"defaultFeature":false,"description":"Adds GORM for MongoDB to the project","name":"mongodb"},{"required":false,"defaultFeature":false,"description":"Adds GORM for Neo4j to the project","name":"neo4j"},{"required":false,"defaultFeature":false,"description":"Adds Spring Security REST to the project","name":"security"}],"description":"Profile for REST API applications","name":"rest-api"},{"version":"3.1.9","features":[{"required":false,"defaultFeature":true,"description":"Adds Asset Pipeline to a Grails project","name":"asset-pipeline"},{"required":false,"defaultFeature":true,"description":"Adds GORM for Hibernate to the project","name":"hibernate"},{"required":false,"defaultFeature":false,"description":"Adds support for JSON Views to the project","name":"json-views"},{"required":false,"defaultFeature":false,"description":"Adds LESS Transpiler Asset Pipeline to a Grails project","name":"less-asset-pipeline"},{"required":false,"defaultFeature":false,"description":"Adds support for Markup Views to the project","name":"markup-views"},{"required":false,"defaultFeature":false,"description":"Adds GORM for MongoDB to the project","name":"mongodb"},{"required":false,"defaultFeature":false,"description":"Adds GORM for Neo4j to the project","name":"neo4j"}],"description":"Profile for Web applications","name":"web"}]}')
    }

}
