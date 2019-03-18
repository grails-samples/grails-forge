package appgenerator

import grails.testing.spock.OnceBefore
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpHeaders
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.DefaultHttpClient
import io.micronaut.http.client.DefaultHttpClientConfiguration
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.HttpClientConfiguration
import spock.lang.Shared

import grails.testing.mixin.integration.Integration
import spock.lang.Specification

import java.time.Duration

@Integration
class ProfileControllerIntegrationSpec extends Specification {

    @Shared HttpClient client

    @OnceBefore
    void init() {
        String baseUrl = "http://localhost:${serverPort}"
        HttpClientConfiguration config = new DefaultHttpClientConfiguration()
        config.readTimeout = Duration.ofSeconds(120)

        this.client  = new DefaultHttpClient(baseUrl.toURL(), config)
    }

    void "test get profiles with curl"() {
        given:
        HttpResponse<List<String>> resp = client.toBlocking().exchange(HttpRequest.GET("/3.2.3/profiles").header("User-Agent", "curl"), List)

        expect:"The response is correct"

        resp.status() == HttpStatus.OK
        resp.header(HttpHeaders.CONTENT_TYPE) == "application/json;charset=UTF-8"
        resp.body().unique() == ["angular", "rest-api", "angular2", "react", "web", "webpack"]
    }

    void "test get profiles without curl contains angular profile"() {
        when:
        HttpResponse<List<Map>> resp = client.toBlocking().exchange(HttpRequest.GET("/3.2.3/profiles"), Argument.of(List, Map))

        then: "The response is correct"
        resp.status() == HttpStatus.OK
        resp.header(HttpHeaders.CONTENT_TYPE) == "application/json;charset=UTF-8"

        when:
        def result = resp.body()

        then:
        result.find { profile -> profile.name == 'angular' }
        result.find { profile -> profile.name == 'angular' }.description == 'A profile for creating applications using AngularJS'
        result.find { profile -> profile.name == 'angular' }.features.find { feature -> feature.name == 'asset-pipeline' }
        result.find { profile -> profile.name == 'angular' }.features.find { feature -> feature.name == 'asset-pipeline' }.defaultFeature == false
        result.find { profile -> profile.name == 'angular' }.features.find { feature -> feature.name == 'asset-pipeline' }.description == "Adds Asset Pipeline to a Grails project"
        result.find { profile -> profile.name == 'angular' }.features.find { feature -> feature.name == 'asset-pipeline' }.required == true
        result.find { profile -> profile.name == 'angular' }.features.find { feature -> feature.name == 'hibernate4' }
        result.find { profile -> profile.name == 'angular' }.features.find { feature -> feature.name == 'hibernate4' }.defaultFeature == false
        result.find { profile -> profile.name == 'angular' }.features.find { feature -> feature.name == 'hibernate4' }.description == 'Adds GORM for Hibernate 4 to the project'
        result.find { profile -> profile.name == 'angular' }.features.find { feature -> feature.name == 'hibernate4' }.required == false
        result.find { profile -> profile.name == 'angular' }.features.find { feature -> feature.name == 'hibernate5' }
        result.find { profile -> profile.name == 'angular' }.features.find { feature -> feature.name == 'hibernate5' }.defaultFeature == true
        result.find { profile -> profile.name == 'angular' }.features.find { feature -> feature.name == 'hibernate5' }.description == "Adds GORM for Hibernate 5 to the project"
        result.find { profile -> profile.name == 'angular' }.features.find { feature -> feature.name == 'hibernate5' }.required == false
        result.find { profile -> profile.name == 'angular' }.features.find { feature -> feature.name == 'json-views' }
        result.find { profile -> profile.name == 'angular' }.features.find { feature -> feature.name == 'json-views' }.defaultFeature == false
        result.find { profile -> profile.name == 'angular' }.features.find { feature -> feature.name == 'json-views' }.description == 'Adds support for JSON Views to the project'
        result.find { profile -> profile.name == 'angular' }.features.find { feature -> feature.name == 'json-views' }.required == true
        result.find { profile -> profile.name == 'angular' }.features.find { feature -> feature.name == 'less-asset-pipeline' }
        result.find { profile -> profile.name == 'angular' }.features.find { feature -> feature.name == 'less-asset-pipeline' }.defaultFeature == false
        result.find { profile -> profile.name == 'angular' }.features.find { feature -> feature.name == 'less-asset-pipeline' }.description == "Adds LESS Transpiler Asset Pipeline to a Grails project"
        result.find { profile -> profile.name == 'angular' }.features.find { feature -> feature.name == 'less-asset-pipeline' }.required == false
        result.find { profile -> profile.name == 'angular' }.features.find { feature -> feature.name == 'markup-views' }
        result.find { profile -> profile.name == 'angular' }.features.find { feature -> feature.name == 'markup-views' }.defaultFeature == false
        result.find { profile -> profile.name == 'angular' }.features.find { feature -> feature.name == 'markup-views' }.description == "Adds support for Markup Views to the project"
        result.find { profile -> profile.name == 'angular' }.features.find { feature -> feature.name == 'markup-views' }.required == false
        result.find { profile -> profile.name == 'angular' }.features.find { feature -> feature.name == 'mongodb' }
        result.find { profile -> profile.name == 'angular' }.features.find { feature -> feature.name == 'mongodb' }.defaultFeature == false
        result.find { profile -> profile.name == 'angular' }.features.find { feature -> feature.name == 'mongodb' }.description == "Adds GORM for MongoDB to the project"
        result.find { profile -> profile.name == 'angular' }.features.find { feature -> feature.name == 'mongodb' }.required == false
        result.find { profile -> profile.name == 'angular' }.features.find { feature -> feature.name == 'neo4j' }
        result.find { profile -> profile.name == 'angular' }.features.find { feature -> feature.name == 'neo4j' }.defaultFeature == false
        result.find { profile -> profile.name == 'angular' }.features.find { feature -> feature.name == 'neo4j' }.description == "Adds GORM for Neo4j to the project"
        result.find { profile -> profile.name == 'angular' }.features.find { feature -> feature.name == 'neo4j' }.required == false
        result.find { profile -> profile.name == 'angular' }.features.find { feature -> feature.name == 'rx-mongodb' }
        result.find { profile -> profile.name == 'angular' }.features.find { feature -> feature.name == 'rx-mongodb' }.defaultFeature == false
        result.find { profile -> profile.name == 'angular' }.features.find { feature -> feature.name == 'rx-mongodb' }.description == "Adds RxGORM for MongoDB to the project"
        result.find { profile -> profile.name == 'angular' }.features.find { feature -> feature.name == 'rx-mongodb' }.required == false
        result.find { profile -> profile.name == 'angular' }.features.find { feature -> feature.name == 'security' }
        result.find { profile -> profile.name == 'angular' }.features.find { feature -> feature.name == 'security' }.defaultFeature == false
        result.find { profile -> profile.name == 'angular' }.features.find { feature -> feature.name == 'security' }.description == "Adds Spring Security REST to the project"
        result.find { profile -> profile.name == 'angular' }.features.find { feature -> feature.name == 'security' }.required == false
    }

    void "test get profiles without curl contains rest-api profile"() {
        when:
        HttpResponse<List<Map>> resp = client.toBlocking().exchange(HttpRequest.GET("/3.2.3/profiles"), Argument.of(List, Map))

        then:"The response is correct"
        resp.status() == HttpStatus.OK
        resp.header(HttpHeaders.CONTENT_TYPE) == "application/json;charset=UTF-8"

        when:
        def result = resp.body()

        then:
        result.find { it.name == 'rest-api' }
        result.find { profile -> profile.name == 'rest-api' }.description == 'Profile for REST API applications'
        result.find { profile -> profile.name == 'rest-api' }.features.find { feature -> feature.name == 'asset-pipeline'}
        result.find { profile -> profile.name == 'rest-api' }.features.find { feature -> feature.name == 'asset-pipeline'}.defaultFeature == false
        result.find { profile -> profile.name == 'rest-api' }.features.find { feature -> feature.name == 'asset-pipeline'}.description == "Adds Asset Pipeline to a Grails project"
        result.find { profile -> profile.name == 'rest-api' }.features.find { feature -> feature.name == 'asset-pipeline'}.required == false
        result.find { profile -> profile.name == 'rest-api' }.features.find { feature -> feature.name == 'hibernate4'}
        result.find { profile -> profile.name == 'rest-api' }.features.find { feature -> feature.name == 'hibernate4'}.defaultFeature == false
        result.find { profile -> profile.name == 'rest-api' }.features.find { feature -> feature.name == 'hibernate4'}.description == 'Adds GORM for Hibernate 4 to the project'
        result.find { profile -> profile.name == 'rest-api' }.features.find { feature -> feature.name == 'hibernate4'}.required == false
        result.find { profile -> profile.name == 'rest-api' }.features.find { feature -> feature.name == 'hibernate5'}
        result.find { profile -> profile.name == 'rest-api' }.features.find { feature -> feature.name == 'hibernate5'}.defaultFeature == true
        result.find { profile -> profile.name == 'rest-api' }.features.find { feature -> feature.name == 'hibernate5'}.description == "Adds GORM for Hibernate 5 to the project"
        result.find { profile -> profile.name == 'rest-api' }.features.find { feature -> feature.name == 'hibernate5'}.required == false
        result.find { profile -> profile.name == 'rest-api' }.features.find { feature -> feature.name == 'json-views'}
        result.find { profile -> profile.name == 'rest-api' }.features.find { feature -> feature.name == 'json-views'}.defaultFeature == false
        result.find { profile -> profile.name == 'rest-api' }.features.find { feature -> feature.name == 'json-views'}.description == 'Adds support for JSON Views to the project'
        result.find { profile -> profile.name == 'rest-api' }.features.find { feature -> feature.name == 'json-views'}.required == true
        result.find { profile -> profile.name == 'rest-api' }.features.find { feature -> feature.name == 'less-asset-pipeline'}
        result.find { profile -> profile.name == 'rest-api' }.features.find { feature -> feature.name == 'less-asset-pipeline'}.defaultFeature == false
        result.find { profile -> profile.name == 'rest-api' }.features.find { feature -> feature.name == 'less-asset-pipeline'}.description == "Adds LESS Transpiler Asset Pipeline to a Grails project"
        result.find { profile -> profile.name == 'rest-api' }.features.find { feature -> feature.name == 'less-asset-pipeline'}.required == false
        result.find { profile -> profile.name == 'rest-api' }.features.find { feature -> feature.name == 'markup-views'}
        result.find { profile -> profile.name == 'rest-api' }.features.find { feature -> feature.name == 'markup-views'}.defaultFeature == false
        result.find { profile -> profile.name == 'rest-api' }.features.find { feature -> feature.name == 'markup-views'}.description == "Adds support for Markup Views to the project"
        result.find { profile -> profile.name == 'rest-api' }.features.find { feature -> feature.name == 'markup-views'}.required == false
        result.find { profile -> profile.name == 'rest-api' }.features.find { feature -> feature.name == 'mongodb'}
        result.find { profile -> profile.name == 'rest-api' }.features.find { feature -> feature.name == 'mongodb'}.defaultFeature  == false
        result.find { profile -> profile.name == 'rest-api' }.features.find { feature -> feature.name == 'mongodb'}.description == "Adds GORM for MongoDB to the project"
        result.find { profile -> profile.name == 'rest-api' }.features.find { feature -> feature.name == 'mongodb'}.required == false
        result.find { profile -> profile.name == 'rest-api' }.features.find { feature -> feature.name == 'neo4j'}
        result.find { profile -> profile.name == 'rest-api' }.features.find { feature -> feature.name == 'neo4j'}.defaultFeature == false
        result.find { profile -> profile.name == 'rest-api' }.features.find { feature -> feature.name == 'neo4j'}.description  == "Adds GORM for Neo4j to the project"
        result.find { profile -> profile.name == 'rest-api' }.features.find { feature -> feature.name == 'neo4j'}.required == false
        result.find { profile -> profile.name == 'rest-api' }.features.find { feature -> feature.name == 'rx-mongodb'}
        result.find { profile -> profile.name == 'rest-api' }.features.find { feature -> feature.name == 'rx-mongodb'}.defaultFeature == false
        result.find { profile -> profile.name == 'rest-api' }.features.find { feature -> feature.name == 'rx-mongodb'}.description == "Adds RxGORM for MongoDB to the project"
        result.find { profile -> profile.name == 'rest-api' }.features.find { feature -> feature.name == 'rx-mongodb'}.required == false
        result.find { profile -> profile.name == 'rest-api' }.features.find { feature -> feature.name == 'security'}
        result.find { profile -> profile.name == 'rest-api' }.features.find { feature -> feature.name == 'security'}.defaultFeature == false
        result.find { profile -> profile.name == 'rest-api' }.features.find { feature -> feature.name == 'security'}.description == "Adds Spring Security REST to the project"
        result.find { profile -> profile.name == 'rest-api' }.features.find { feature -> feature.name == 'security'}.required == false
    }

    void "test get profiles without curl contains angular2 profile"() {
        when:
        HttpResponse<List<Map>> resp = client.toBlocking().exchange(HttpRequest.GET("/3.2.3/profiles"), Argument.of(List, Map))

        then:"The response is correct"
        resp.status() == HttpStatus.OK
        resp.header(HttpHeaders.CONTENT_TYPE) == "application/json;charset=UTF-8"

        when:
        def result = resp.body()

        then:
        result.find { it.name == 'angular2' }
        result.find { profile -> profile.name == 'angular2' }.description == 'A profile for creating Grails applications with Angular 2'
        result.find { profile -> profile.name == 'angular2' }.features.find { feature -> feature.name == 'asset-pipeline'}
        result.find { profile -> profile.name == 'angular2' }.features.find { feature -> feature.name == 'asset-pipeline'}.defaultFeature == false
        result.find { profile -> profile.name == 'angular2' }.features.find { feature -> feature.name == 'asset-pipeline'}.description == "Adds Asset Pipeline to a Grails project"
        result.find { profile -> profile.name == 'angular2' }.features.find { feature -> feature.name == 'asset-pipeline'}.required == false
        result.find { profile -> profile.name == 'angular2' }.features.find { feature -> feature.name == 'hibernate4'}
        result.find { profile -> profile.name == 'angular2' }.features.find { feature -> feature.name == 'hibernate4'}.defaultFeature == false
        result.find { profile -> profile.name == 'angular2' }.features.find { feature -> feature.name == 'hibernate4'}.description == 'Adds GORM for Hibernate 4 to the project'
        result.find { profile -> profile.name == 'angular2' }.features.find { feature -> feature.name == 'hibernate4'}.required == false
        result.find { profile -> profile.name == 'angular2' }.features.find { feature -> feature.name == 'hibernate5'}
        result.find { profile -> profile.name == 'angular2' }.features.find { feature -> feature.name == 'hibernate5'}.defaultFeature == true
        result.find { profile -> profile.name == 'angular2' }.features.find { feature -> feature.name == 'hibernate5'}.description == "Adds GORM for Hibernate 5 to the project"
        result.find { profile -> profile.name == 'angular2' }.features.find { feature -> feature.name == 'hibernate5'}.required == false
        result.find { profile -> profile.name == 'angular2' }.features.find { feature -> feature.name == 'json-views'}
        result.find { profile -> profile.name == 'angular2' }.features.find { feature -> feature.name == 'json-views'}.defaultFeature == false
        result.find { profile -> profile.name == 'angular2' }.features.find { feature -> feature.name == 'json-views'}.description == 'Adds support for JSON Views to the project'
        result.find { profile -> profile.name == 'angular2' }.features.find { feature -> feature.name == 'json-views'}.required == true
        result.find { profile -> profile.name == 'angular2' }.features.find { feature -> feature.name == 'less-asset-pipeline'}
        result.find { profile -> profile.name == 'angular2' }.features.find { feature -> feature.name == 'less-asset-pipeline'}.defaultFeature == false
        result.find { profile -> profile.name == 'angular2' }.features.find { feature -> feature.name == 'less-asset-pipeline'}.description == "Adds LESS Transpiler Asset Pipeline to a Grails project"
        result.find { profile -> profile.name == 'angular2' }.features.find { feature -> feature.name == 'less-asset-pipeline'}.required == false
        result.find { profile -> profile.name == 'angular2' }.features.find { feature -> feature.name == 'markup-views'}
        result.find { profile -> profile.name == 'angular2' }.features.find { feature -> feature.name == 'markup-views'}.defaultFeature == false
        result.find { profile -> profile.name == 'angular2' }.features.find { feature -> feature.name == 'markup-views'}.description == "Adds support for Markup Views to the project"
        result.find { profile -> profile.name == 'angular2' }.features.find { feature -> feature.name == 'markup-views'}.required == false
        result.find { profile -> profile.name == 'angular2' }.features.find { feature -> feature.name == 'mongodb'}
        result.find { profile -> profile.name == 'angular2' }.features.find { feature -> feature.name == 'mongodb'}.defaultFeature  == false
        result.find { profile -> profile.name == 'angular2' }.features.find { feature -> feature.name == 'mongodb'}.description == "Adds GORM for MongoDB to the project"
        result.find { profile -> profile.name == 'angular2' }.features.find { feature -> feature.name == 'mongodb'}.required == false
        result.find { profile -> profile.name == 'angular2' }.features.find { feature -> feature.name == 'neo4j'}
        result.find { profile -> profile.name == 'angular2' }.features.find { feature -> feature.name == 'neo4j'}.defaultFeature == false
        result.find { profile -> profile.name == 'angular2' }.features.find { feature -> feature.name == 'neo4j'}.description  == "Adds GORM for Neo4j to the project"
        result.find { profile -> profile.name == 'angular2' }.features.find { feature -> feature.name == 'neo4j'}.required == false
        result.find { profile -> profile.name == 'angular2' }.features.find { feature -> feature.name == 'rx-mongodb'}
        result.find { profile -> profile.name == 'angular2' }.features.find { feature -> feature.name == 'rx-mongodb'}.defaultFeature == false
        result.find { profile -> profile.name == 'angular2' }.features.find { feature -> feature.name == 'rx-mongodb'}.description == "Adds RxGORM for MongoDB to the project"
        result.find { profile -> profile.name == 'angular2' }.features.find { feature -> feature.name == 'rx-mongodb'}.required == false
        result.find { profile -> profile.name == 'angular2' }.features.find { feature -> feature.name == 'security'}
        result.find { profile -> profile.name == 'angular2' }.features.find { feature -> feature.name == 'security'}.defaultFeature == false
        result.find { profile -> profile.name == 'angular2' }.features.find { feature -> feature.name == 'security'}.description == "Adds Spring Security REST to the project"
        result.find { profile -> profile.name == 'angular2' }.features.find { feature -> feature.name == 'security'}.required == false

    }

    void "test get profiles without curl contains react profile"() {
        when:
        HttpResponse<List<Map>> resp = client.toBlocking().exchange(HttpRequest.GET("/3.2.3/profiles"), Argument.of(List, Map))

        then:"The response is correct"
        resp.status() == HttpStatus.OK
        resp.header(HttpHeaders.CONTENT_TYPE) == "application/json;charset=UTF-8"

        when:
        def result = resp.body()

        then:
        result.find { it.name == 'react' }
        result.find { profile -> profile.name == 'react' }.description == 'A profile for creating Grails applications with a React frontend'
        result.find { profile -> profile.name == 'react' }.features.find { feature -> feature.name == 'asset-pipeline' }
        result.find { profile -> profile.name == 'react' }.features.find { feature -> feature.name == 'asset-pipeline' }.defaultFeature == false
        result.find { profile -> profile.name == 'react' }.features.find { feature -> feature.name == 'asset-pipeline' }.description == "Adds Asset Pipeline to a Grails project"
        result.find { profile -> profile.name == 'react' }.features.find { feature -> feature.name == 'asset-pipeline' }.required == false
        result.find { profile -> profile.name == 'react' }.features.find { feature -> feature.name == 'hibernate4' }
        result.find { profile -> profile.name == 'react' }.features.find { feature -> feature.name == 'hibernate4' }.defaultFeature == false
        result.find { profile -> profile.name == 'react' }.features.find { feature -> feature.name == 'hibernate4' }.description == 'Adds GORM for Hibernate 4 to the project'
        result.find { profile -> profile.name == 'react' }.features.find { feature -> feature.name == 'hibernate4' }.required == false
        result.find { profile -> profile.name == 'react' }.features.find { feature -> feature.name == 'hibernate5' }
        result.find { profile -> profile.name == 'react' }.features.find { feature -> feature.name == 'hibernate5' }.defaultFeature == true
        result.find { profile -> profile.name == 'react' }.features.find { feature -> feature.name == 'hibernate5' }.description == "Adds GORM for Hibernate 5 to the project"
        result.find { profile -> profile.name == 'react' }.features.find { feature -> feature.name == 'hibernate5' }.required == false
        result.find { profile -> profile.name == 'react' }.features.find { feature -> feature.name == 'json-views' }
        result.find { profile -> profile.name == 'react' }.features.find { feature -> feature.name == 'json-views' }.defaultFeature == false
        result.find { profile -> profile.name == 'react' }.features.find { feature -> feature.name == 'json-views' }.description == 'Adds support for JSON Views to the project'
        result.find { profile -> profile.name == 'react' }.features.find { feature -> feature.name == 'json-views' }.required == true
        result.find { profile -> profile.name == 'react' }.features.find { feature -> feature.name == 'less-asset-pipeline' }
        result.find { profile -> profile.name == 'react' }.features.find { feature -> feature.name == 'less-asset-pipeline' }.defaultFeature == false
        result.find { profile -> profile.name == 'react' }.features.find { feature -> feature.name == 'less-asset-pipeline' }.description == "Adds LESS Transpiler Asset Pipeline to a Grails project"
        result.find { profile -> profile.name == 'react' }.features.find { feature -> feature.name == 'less-asset-pipeline' }.required == false
        result.find { profile -> profile.name == 'react' }.features.find { feature -> feature.name == 'markup-views' }
        result.find { profile -> profile.name == 'react' }.features.find { feature -> feature.name == 'markup-views' }.defaultFeature == false
        result.find { profile -> profile.name == 'react' }.features.find { feature -> feature.name == 'markup-views' }.description == "Adds support for Markup Views to the project"
        result.find { profile -> profile.name == 'react' }.features.find { feature -> feature.name == 'markup-views' }.required == false
        result.find { profile -> profile.name == 'react' }.features.find { feature -> feature.name == 'mongodb' }
        result.find { profile -> profile.name == 'react' }.features.find { feature -> feature.name == 'mongodb' }.defaultFeature == false
        result.find { profile -> profile.name == 'react' }.features.find { feature -> feature.name == 'mongodb' }.description == "Adds GORM for MongoDB to the project"
        result.find { profile -> profile.name == 'react' }.features.find { feature -> feature.name == 'mongodb' }.required == false
        result.find { profile -> profile.name == 'react' }.features.find { feature -> feature.name == 'neo4j' }
        result.find { profile -> profile.name == 'react' }.features.find { feature -> feature.name == 'neo4j' }.defaultFeature == false
        result.find { profile -> profile.name == 'react' }.features.find { feature -> feature.name == 'neo4j' }.description == "Adds GORM for Neo4j to the project"
        result.find { profile -> profile.name == 'react' }.features.find { feature -> feature.name == 'neo4j' }.required == false
        result.find { profile -> profile.name == 'react' }.features.find { feature -> feature.name == 'rx-mongodb' }
        result.find { profile -> profile.name == 'react' }.features.find { feature -> feature.name == 'rx-mongodb' }.defaultFeature == false
        result.find { profile -> profile.name == 'react' }.features.find { feature -> feature.name == 'rx-mongodb' }.description == "Adds RxGORM for MongoDB to the project"
        result.find { profile -> profile.name == 'react' }.features.find { feature -> feature.name == 'rx-mongodb' }.required == false
        result.find { profile -> profile.name == 'react' }.features.find { feature -> feature.name == 'security' }
        result.find { profile -> profile.name == 'react' }.features.find { feature -> feature.name == 'security' }.defaultFeature == false
        result.find { profile -> profile.name == 'react' }.features.find { feature -> feature.name == 'security' }.description == "Adds Spring Security REST to the project"
        result.find { profile -> profile.name == 'react' }.features.find { feature -> feature.name == 'security' }.required == false
    }

    void "test get profiles without curl contains web profile"() {
        when:
        HttpResponse<List<Map>> resp = client.toBlocking().exchange(HttpRequest.GET("/3.2.3/profiles"), Argument.of(List, Map))

        then:"The response is correct"
        resp.status() == HttpStatus.OK
        resp.header(HttpHeaders.CONTENT_TYPE) == "application/json;charset=UTF-8"

        when:
        def result = resp.body()

        then:
        result.find { it.name == 'web' }
        result.find { profile -> profile.name == 'web' }.description == 'Profile for Web applications'
        result.find { profile -> profile.name == 'web' }.features.find { feature -> feature.name == 'asset-pipeline' }
        result.find { profile -> profile.name == 'web' }.features.find { feature -> feature.name == 'asset-pipeline' }.defaultFeature == true
        result.find { profile -> profile.name == 'web' }.features.find { feature -> feature.name == 'asset-pipeline' }.description == "Adds Asset Pipeline to a Grails project"
        result.find { profile -> profile.name == 'web' }.features.find { feature -> feature.name == 'asset-pipeline' }.required == false
        result.find { profile -> profile.name == 'web' }.features.find { feature -> feature.name == 'hibernate4' }
        result.find { profile -> profile.name == 'web' }.features.find { feature -> feature.name == 'hibernate4' }.defaultFeature == false
        result.find { profile -> profile.name == 'web' }.features.find { feature -> feature.name == 'hibernate4' }.description == 'Adds GORM for Hibernate 4 to the project'
        result.find { profile -> profile.name == 'web' }.features.find { feature -> feature.name == 'hibernate4' }.required == false
        result.find { profile -> profile.name == 'web' }.features.find { feature -> feature.name == 'hibernate5' }
        result.find { profile -> profile.name == 'web' }.features.find { feature -> feature.name == 'hibernate5' }.defaultFeature == true
        result.find { profile -> profile.name == 'web' }.features.find { feature -> feature.name == 'hibernate5' }.description == "Adds GORM for Hibernate 5 to the project"
        result.find { profile -> profile.name == 'web' }.features.find { feature -> feature.name == 'hibernate5' }.required == false
        result.find { profile -> profile.name == 'web' }.features.find { feature -> feature.name == 'json-views' }
        result.find { profile -> profile.name == 'web' }.features.find { feature -> feature.name == 'json-views' }.defaultFeature == false
        result.find { profile -> profile.name == 'web' }.features.find { feature -> feature.name == 'json-views' }.description == 'Adds support for JSON Views to the project'
        result.find { profile -> profile.name == 'web' }.features.find { feature -> feature.name == 'json-views' }.required == false
        result.find { profile -> profile.name == 'web' }.features.find { feature -> feature.name == 'less-asset-pipeline' }
        result.find { profile -> profile.name == 'web' }.features.find { feature -> feature.name == 'less-asset-pipeline' }.defaultFeature == false
        result.find { profile -> profile.name == 'web' }.features.find { feature -> feature.name == 'less-asset-pipeline' }.description == "Adds LESS Transpiler Asset Pipeline to a Grails project"
        result.find { profile -> profile.name == 'web' }.features.find { feature -> feature.name == 'less-asset-pipeline' }.required == false
        result.find { profile -> profile.name == 'web' }.features.find { feature -> feature.name == 'markup-views' }
        result.find { profile -> profile.name == 'web' }.features.find { feature -> feature.name == 'markup-views' }.defaultFeature == false
        result.find { profile -> profile.name == 'web' }.features.find { feature -> feature.name == 'markup-views' }.description == "Adds support for Markup Views to the project"
        result.find { profile -> profile.name == 'web' }.features.find { feature -> feature.name == 'markup-views' }.required == false
        result.find { profile -> profile.name == 'web' }.features.find { feature -> feature.name == 'mongodb' }
        result.find { profile -> profile.name == 'web' }.features.find { feature -> feature.name == 'mongodb' }.defaultFeature == false
        result.find { profile -> profile.name == 'web' }.features.find { feature -> feature.name == 'mongodb' }.description == "Adds GORM for MongoDB to the project"
        result.find { profile -> profile.name == 'web' }.features.find { feature -> feature.name == 'mongodb' }.required == false
        result.find { profile -> profile.name == 'web' }.features.find { feature -> feature.name == 'neo4j' }
        result.find { profile -> profile.name == 'web' }.features.find { feature -> feature.name == 'neo4j' }.defaultFeature == false
        result.find { profile -> profile.name == 'web' }.features.find { feature -> feature.name == 'neo4j' }.description == "Adds GORM for Neo4j to the project"
        result.find { profile -> profile.name == 'web' }.features.find { feature -> feature.name == 'neo4j' }.required == false
        result.find { profile -> profile.name == 'web' }.features.find { feature -> feature.name == 'rx-mongodb' }
        result.find { profile -> profile.name == 'web' }.features.find { feature -> feature.name == 'rx-mongodb' }.defaultFeature == false
        result.find { profile -> profile.name == 'web' }.features.find { feature -> feature.name == 'rx-mongodb' }.description == "Adds RxGORM for MongoDB to the project"
        result.find { profile -> profile.name == 'web' }.features.find { feature -> feature.name == 'rx-mongodb' }.required == false
    }

    void "test get profiles without curl contains webpack profile"() {
        when:
        HttpResponse<List<Map>> resp = client.toBlocking().exchange(HttpRequest.GET("/3.2.3/profiles"), Argument.of(List, Map))

        then:"The response is correct"
        resp.status() == HttpStatus.OK
        resp.header(HttpHeaders.CONTENT_TYPE) == "application/json;charset=UTF-8"

        when:
        def result = resp.body()
        then:
        result.find { it.name == 'webpack'}
        result.find { profile -> profile.name == 'webpack' }.description == 'A profile for creating applications with node-based frontends using webpack'
        result.find { profile -> profile.name == 'webpack' }.features.find { feature -> feature.name == 'asset-pipeline'}
        result.find { profile -> profile.name == 'webpack' }.features.find { feature -> feature.name == 'asset-pipeline'}.defaultFeature == false
        result.find { profile -> profile.name == 'webpack' }.features.find { feature -> feature.name == 'asset-pipeline'}.description == "Adds Asset Pipeline to a Grails project"
        result.find { profile -> profile.name == 'webpack' }.features.find { feature -> feature.name == 'asset-pipeline'}.required == true
        result.find { profile -> profile.name == 'webpack' }.features.find { feature -> feature.name == 'hibernate4'}
        result.find { profile -> profile.name == 'webpack' }.features.find { feature -> feature.name == 'hibernate4'}.defaultFeature == false
        result.find { profile -> profile.name == 'webpack' }.features.find { feature -> feature.name == 'hibernate4'}.description == 'Adds GORM for Hibernate 4 to the project'
        result.find { profile -> profile.name == 'webpack' }.features.find { feature -> feature.name == 'hibernate4'}.required == false
        result.find { profile -> profile.name == 'webpack' }.features.find { feature -> feature.name == 'hibernate5'}
        result.find { profile -> profile.name == 'webpack' }.features.find { feature -> feature.name == 'hibernate5'}.defaultFeature == false
        result.find { profile -> profile.name == 'webpack' }.features.find { feature -> feature.name == 'hibernate5'}.description == "Adds GORM for Hibernate 5 to the project"
        result.find { profile -> profile.name == 'webpack' }.features.find { feature -> feature.name == 'hibernate5'}.required == false
        result.find { profile -> profile.name == 'webpack' }.features.find { feature -> feature.name == 'json-views'}
        result.find { profile -> profile.name == 'webpack' }.features.find { feature -> feature.name == 'json-views'}.defaultFeature == false
        result.find { profile -> profile.name == 'webpack' }.features.find { feature -> feature.name == 'json-views'}.description == 'Adds support for JSON Views to the project'
        result.find { profile -> profile.name == 'webpack' }.features.find { feature -> feature.name == 'json-views'}.required == true
        result.find { profile -> profile.name == 'webpack' }.features.find { feature -> feature.name == 'less-asset-pipeline'}
        result.find { profile -> profile.name == 'webpack' }.features.find { feature -> feature.name == 'less-asset-pipeline'}.defaultFeature == false
        result.find { profile -> profile.name == 'webpack' }.features.find { feature -> feature.name == 'less-asset-pipeline'}.description == "Adds LESS Transpiler Asset Pipeline to a Grails project"
        result.find { profile -> profile.name == 'webpack' }.features.find { feature -> feature.name == 'less-asset-pipeline'}.required == false
        result.find { profile -> profile.name == 'webpack' }.features.find { feature -> feature.name == 'markup-views'}
        result.find { profile -> profile.name == 'webpack' }.features.find { feature -> feature.name == 'markup-views'}.defaultFeature == false
        result.find { profile -> profile.name == 'webpack' }.features.find { feature -> feature.name == 'markup-views'}.description == "Adds support for Markup Views to the project"
        result.find { profile -> profile.name == 'webpack' }.features.find { feature -> feature.name == 'markup-views'}.required == false
        result.find { profile -> profile.name == 'webpack' }.features.find { feature -> feature.name == 'mongodb'}
        result.find { profile -> profile.name == 'webpack' }.features.find { feature -> feature.name == 'mongodb'}.defaultFeature  == false
        result.find { profile -> profile.name == 'webpack' }.features.find { feature -> feature.name == 'mongodb'}.description == "Adds GORM for MongoDB to the project"
        result.find { profile -> profile.name == 'webpack' }.features.find { feature -> feature.name == 'mongodb'}.required == false
        result.find { profile -> profile.name == 'webpack' }.features.find { feature -> feature.name == 'neo4j'}
        result.find { profile -> profile.name == 'webpack' }.features.find { feature -> feature.name == 'neo4j'}.defaultFeature == false
        result.find { profile -> profile.name == 'webpack' }.features.find { feature -> feature.name == 'neo4j'}.description  == "Adds GORM for Neo4j to the project"
        result.find { profile -> profile.name == 'webpack' }.features.find { feature -> feature.name == 'neo4j'}.required == false
        result.find { profile -> profile.name == 'webpack' }.features.find { feature -> feature.name == 'rx-mongodb'}
        result.find { profile -> profile.name == 'webpack' }.features.find { feature -> feature.name == 'rx-mongodb'}.defaultFeature == false
        result.find { profile -> profile.name == 'webpack' }.features.find { feature -> feature.name == 'rx-mongodb'}.description == "Adds RxGORM for MongoDB to the project"
        result.find { profile -> profile.name == 'webpack' }.features.find { feature -> feature.name == 'rx-mongodb'}.required == false
        result.find { profile -> profile.name == 'webpack' }.features.find { feature -> feature.name == 'security'}
        result.find { profile -> profile.name == 'webpack' }.features.find { feature -> feature.name == 'security'}.defaultFeature == false
        result.find { profile -> profile.name == 'webpack' }.features.find { feature -> feature.name == 'security'}.description == "Adds Spring Security REST to the project"
        result.find { profile -> profile.name == 'webpack' }.features.find { feature -> feature.name == 'security'}.required == false
    }

    void "test get features with curl"() {
        when:
        HttpResponse<List<String>> resp = client.toBlocking().exchange(HttpRequest.GET("/3.2.3/angular2/features").header("User-Agent", "curl"), Argument.of(List, String))

        then:"The response is correct"
        resp.status() == HttpStatus.OK
        resp.header(HttpHeaders.CONTENT_TYPE) == "application/json;charset=UTF-8"
        resp.body() == ["asset-pipeline","hibernate4","hibernate5","json-views","less-asset-pipeline","markup-views","mongodb","neo4j","rx-mongodb","security"]
    }

    void "test get features without curl"() {
        when:
        HttpResponse<List<Map>> resp = client.toBlocking().exchange(HttpRequest.GET("/3.2.3/angular2/features"), Argument.of(List, Map))


        then:"The response is correct"
        resp.status() == HttpStatus.OK
        resp.header(HttpHeaders.CONTENT_TYPE) == "application/json;charset=UTF-8"

        when:
        def result = resp.body()

        then:
        result.find { feature -> feature.name == 'asset-pipeline'}
        result.find { feature -> feature.name == 'asset-pipeline'}.defaultFeature == false
        result.find { feature -> feature.name == 'asset-pipeline'}.description == "Adds Asset Pipeline to a Grails project"
        result.find { feature -> feature.name == 'asset-pipeline'}.required == false
        result.find { feature -> feature.name == 'hibernate4'}
        result.find { feature -> feature.name == 'hibernate4'}.defaultFeature == false
        result.find { feature -> feature.name == 'hibernate4'}.description == 'Adds GORM for Hibernate 4 to the project'
        result.find { feature -> feature.name == 'hibernate4'}.required == false
        result.find { feature -> feature.name == 'hibernate5'}
        result.find { feature -> feature.name == 'hibernate5'}.defaultFeature == true
        result.find { feature -> feature.name == 'hibernate5'}.description == "Adds GORM for Hibernate 5 to the project"
        result.find { feature -> feature.name == 'hibernate5'}.required == false
        result.find { feature -> feature.name == 'json-views'}
        result.find { feature -> feature.name == 'json-views'}.defaultFeature == false
        result.find { feature -> feature.name == 'json-views'}.description == 'Adds support for JSON Views to the project'
        result.find { feature -> feature.name == 'json-views'}.required == true
        result.find { feature -> feature.name == 'less-asset-pipeline'}
        result.find { feature -> feature.name == 'less-asset-pipeline'}.defaultFeature == false
        result.find { feature -> feature.name == 'less-asset-pipeline'}.description == "Adds LESS Transpiler Asset Pipeline to a Grails project"
        result.find { feature -> feature.name == 'less-asset-pipeline'}.required == false
        result.find { feature -> feature.name == 'markup-views'}
        result.find { feature -> feature.name == 'markup-views'}.defaultFeature == false
        result.find { feature -> feature.name == 'markup-views'}.description == "Adds support for Markup Views to the project"
        result.find { feature -> feature.name == 'markup-views'}.required == false
        result.find { feature -> feature.name == 'mongodb'}
        result.find { feature -> feature.name == 'mongodb'}.defaultFeature  == false
        result.find { feature -> feature.name == 'mongodb'}.description == "Adds GORM for MongoDB to the project"
        result.find { feature -> feature.name == 'mongodb'}.required == false
        result.find { feature -> feature.name == 'neo4j'}
        result.find { feature -> feature.name == 'neo4j'}.defaultFeature == false
        result.find { feature -> feature.name == 'neo4j'}.description  == "Adds GORM for Neo4j to the project"
        result.find { feature -> feature.name == 'neo4j'}.required == false
        result.find { feature -> feature.name == 'rx-mongodb'}
        result.find { feature -> feature.name == 'rx-mongodb'}.defaultFeature == false
        result.find { feature -> feature.name == 'rx-mongodb'}.description == "Adds RxGORM for MongoDB to the project"
        result.find { feature -> feature.name == 'rx-mongodb'}.required == false
        result.find { feature -> feature.name == 'security'}
        result.find { feature -> feature.name == 'security'}.defaultFeature == false
        result.find { feature -> feature.name == 'security'}.description == "Adds Spring Security REST to the project"
        result.find { feature -> feature.name == 'security'}.required == false
    }
}
