package appgenerator

import grails.test.mixin.integration.Integration
import spock.lang.Specification

import static grails.web.http.HttpHeaders.CONTENT_TYPE
import static org.springframework.http.HttpStatus.OK

@Integration
class ProfileControllerIntegrationSpec extends Specification implements RestSpec {


    void "test get profiles with curl"() {
        given:
        def resp = restBuilder().get("$baseUrl/3.2.3/profiles") {
            header("User-Agent", "curl")
        }

        expect:"The response is correct"
        resp.status == OK.value()
        resp.headers[CONTENT_TYPE] == ['application/json;charset=UTF-8']
        resp.json == ["angular","rest-api","angular2","react","web","webpack"]
    }

    void "test get profiles without curl"() {
        given:
        def resp = restBuilder().get("$baseUrl/3.2.3/profiles")

        expect:"The response is correct"
        resp.status == OK.value()
        resp.headers[CONTENT_TYPE] == ['application/json;charset=UTF-8']
        resp.text.startsWith '[{"version":"3.2.0","features":[{"required":true,"defaultFeature":false,"description":"Adds Asset Pipeline to a Grails project","name":"asset-pipeline"},{"required":false,"defaultFeature":false,"description":"Adds GORM for Hibernate 4 to the project","name":"hibernate4"},{"required":false,"defaultFeature":true,"description":"Adds GORM for Hibernate 5 to the project","name":"hibernate5"},{"required":true,"defaultFeature":false,"description":"Adds support for JSON Views to the project","name":"json-views"},{"required":false,"defaultFeature":false,"description":"Adds LESS Transpiler Asset Pipeline to a Grails project","name":"less-asset-pipeline"},{"required":false,"defaultFeature":false,"description":"Adds support for Markup Views to the project","name":"markup-views"},{"required":false,"defaultFeature":false,"description":"Adds GORM for MongoDB to the project","name":"mongodb"},{"required":false,"defaultFeature":false,"description":"Adds GORM for Neo4j to the project","name":"neo4j"},{"required":false,"defaultFeature":false,"description":"Adds RxGORM for MongoDB to the project","name":"rx-mongodb"},{"required":false,"defaultFeature":false,"description":"Adds Spring Security REST to the project","name":"security"}],"description":"A profile for creating applications using AngularJS","name":"angular"}'
    }

    void "test get features with curl"() {
        def resp = restBuilder().get("$baseUrl/3.2.3/angular2/features") {
            header("User-Agent", "curl")
        }

        expect:"The response is correct"
        resp.status == OK.value()
        resp.headers[CONTENT_TYPE] == ['application/json;charset=UTF-8']
        resp.json == ["asset-pipeline","hibernate4","hibernate5","json-views","less-asset-pipeline","markup-views","mongodb","neo4j","rx-mongodb","security"]
    }

    void "test get features without curl"() {
        given:
        def resp = restBuilder().get("$baseUrl/3.2.3/angular2/features")

        expect:"The response is correct"
        resp.status == OK.value()
        resp.headers[CONTENT_TYPE] == ['application/json;charset=UTF-8']
        resp.text == '[{"required":false,"defaultFeature":false,"description":"Adds Asset Pipeline to a Grails project","name":"asset-pipeline"},{"required":false,"defaultFeature":false,"description":"Adds GORM for Hibernate 4 to the project","name":"hibernate4"},{"required":false,"defaultFeature":true,"description":"Adds GORM for Hibernate 5 to the project","name":"hibernate5"},{"required":true,"defaultFeature":false,"description":"Adds support for JSON Views to the project","name":"json-views"},{"required":false,"defaultFeature":false,"description":"Adds LESS Transpiler Asset Pipeline to a Grails project","name":"less-asset-pipeline"},{"required":false,"defaultFeature":false,"description":"Adds support for Markup Views to the project","name":"markup-views"},{"required":false,"defaultFeature":false,"description":"Adds GORM for MongoDB to the project","name":"mongodb"},{"required":false,"defaultFeature":false,"description":"Adds GORM for Neo4j to the project","name":"neo4j"},{"required":false,"defaultFeature":false,"description":"Adds RxGORM for MongoDB to the project","name":"rx-mongodb"},{"required":false,"defaultFeature":false,"description":"Adds Spring Security REST to the project","name":"security"}]'
    }
}
