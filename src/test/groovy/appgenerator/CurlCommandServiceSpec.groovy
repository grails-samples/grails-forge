package appgenerator

import appgenerator.profile.Feature
import appgenerator.profile.Profile
import grails.testing.services.ServiceUnitTest
import spock.lang.Specification
import spock.lang.Unroll

class CurlCommandServiceSpec extends Specification implements ServiceUnitTest<CurlCommandService> {

    @Unroll
    def "curl for #name #projectType #version #profile #features is #expected"(String name, String projectType, String version, String profile, String features, String expected) {
        when:
        service.profileService = Stub(ProfileService) {
            getProfiles(_) >> webProfiles()
            getPluginProfiles(_) >> pluginProfiles()
        }
        ProjectOptions projectOptions = new ProjectOptions()
        projectOptions.name = name
        projectOptions.selectedVersion = version
        projectOptions.selectedProfile = profile
        projectOptions.selectedProjectType = projectType
        projectOptions.requiredSelectedFeatures = ['asset-pipeline', 'gsp']
        projectOptions.defaultSelectedFeatures = ['events', 'hibernate5']
        projectOptions.projectTypes = ProjectTypeService.findAll()
        projectOptions.versions = ['3.3.1', '3.3.0']
        projectOptions.profiles = ['web']
        List<String> featureList = features.split(',')
        projectOptions.selectedFeatures = featureList

        String cmd = service.curlCommand(projectOptions)

        then:
        cmd == expected

        where:
        name       | projectType   | version | profile           | features                                            | expected
        'myapp'    | 'application' | '3.3.1' | 'web'             | 'asset-pipeline,events,gsp,hibernate5'              | 'curl -O start.grails.org/myapp.zip'
        'myapp'    | 'application' | '3.3.0' | 'web'             | 'asset-pipeline,events,gsp,hibernate5'              | 'curl -O start.grails.org/myapp.zip -d version=3.3.0'
        'myapp'    | 'application' | '3.3.1' | 'rest-api'        | 'events,json-views,hibernate5,markup-views'         | 'curl -O start.grails.org/myapp.zip -d profile=rest-api -d features=events,hibernate5,markup-views'
        'myplugin' | 'plugin'      | '3.3.1' | 'web-plugin'      | 'asset-pipeline-plugin,gsp'                         | 'curl -O start.grails.org/myplugin.zip -d type=plugin'
        'myplugin' | 'plugin'      | '3.3.1' | 'rest-api-plugin' | 'hibernate5,json-views'                             | 'curl -O start.grails.org/myplugin.zip -d profile=rest-api-plugin'
        'myapp'    | 'application' | '3.3.1' | 'web'             | 'asset-pipeline,events,gsp,hibernate5,markup-views' | 'curl -O start.grails.org/myapp.zip -d features=events,hibernate5,markup-views'
    }

    List<Profile> webProfiles() {
        [
                new Profile(name: 'rest-api', description: 'Profile for REST API applications', version: '3.3.1', features:
                        [
                                new Feature(name: 'security', description: 'Adds Spring Security REST to the project', defaultFeature: false, required: false),
                                new Feature(name: 'asset-pipeline', description: 'Adds Asset Pipeline to a Grails project', defaultFeature: false, required: false),
                                new Feature(name: 'events', description: 'Adds support for the Grails EventBus abstraction', defaultFeature: true, required: false),
                                new Feature(name: 'gsp', description: 'Adds support for GSP to the project', defaultFeature: false, required: false),
                                new Feature(name: 'hibernate4', description: 'Adds GORM for Hibernate 4 to the project', defaultFeature: false, required: false),
                                new Feature(name: 'hibernate5', description: 'Adds GORM for Hibernate 5 to the project', defaultFeature: true, required: false),
                                new Feature(name: 'json-views', description: 'Adds support for JSON Views to the project', defaultFeature: false, required: true),
                                new Feature(name: 'less-asset-pipeline', description: 'Adds LESS Transpiler Asset Pipeline to a Grails project', defaultFeature: false, required: false),
                                new Feature(name: 'markup-views', description: 'Adds support for Markup Views to the project', defaultFeature: false, required: false),
                                new Feature(name: 'mongodb', description: 'Adds GORM for MongoDB to the project', defaultFeature: false, required: false),
                                new Feature(name: 'neo4j', description: 'Adds GORM for Neo4j to the project', defaultFeature: false, required: false),
                                new Feature(name: 'rx-mongodb', description: 'Adds RxGORM for MongoDB to the project', defaultFeature: false, required: false),

                        ]),
                new Profile(name: 'web', description: 'Profile for Web applications', version: '3.3.0', features:
                        [
                                new Feature(name: 'asset-pipeline', description: 'Adds Asset Pipeline to a Grails project', defaultFeature: false, required: true),
                                new Feature(name: 'events', description: 'Adds support for the Grails EventBus abstraction', defaultFeature: true, required: false),
                                new Feature(name: 'gsp', description: 'Adds support for GSP to the project', defaultFeature: false, required: true),
                                new Feature(name: 'hibernate4', description: 'Adds GORM for Hibernate 4 to the project', defaultFeature: false, required: false),
                                new Feature(name: 'hibernate5', description: 'Adds GORM for Hibernate 5 to the project', defaultFeature: true, required: false),
                                new Feature(name: 'json-views', description: 'Adds support for JSON Views to the project', defaultFeature: false, required: false),
                                new Feature(name: 'less-asset-pipeline', description: 'Adds LESS Transpiler Asset Pipeline to a Grails project', defaultFeature: false, required: false),
                                new Feature(name: 'markup-views', description: 'Adds support for Markup Views to the project', defaultFeature: false, required: false),
                                new Feature(name: 'mongodb', description: 'Adds GORM for MongoDB to the project', defaultFeature: false, required: false),
                                new Feature(name: 'neo4j', description: 'Adds GORM for Neo4j to the project', defaultFeature: false, required: false),
                                new Feature(name: 'rx-mongodb', description: 'Adds RxGORM for MongoDB to the project', defaultFeature: false, required: false),
                        ]),
        ]
    }

    List<Profile> pluginProfiles() {
        [
                new Profile(name: 'plugin', description: 'Profile for plugins designed to work across all profiles', version: '3.2.2', features:
                        [
                                new Feature(name: 'asset-pipeline-plugin', description: 'Adds Asset Pipeline to a Grails Plugin for packaging', defaultFeature: false, required: false),
                                new Feature(name: 'asset-pipeline', description: 'Adds Asset Pipeline to a Grails project', defaultFeature: false, required: false),
                                new Feature(name: 'events', description: 'Adds support for the Grails EventBus abstraction', defaultFeature: false, required: false),
                                new Feature(name: 'gsp', description: 'Adds support for GSP to the project', defaultFeature: false, required: false),
                                new Feature(name: 'hibernate4', description: 'Adds GORM for Hibernate 4 to the project', defaultFeature: false, required: false),
                                new Feature(name: 'hibernate5', description: 'Adds GORM for Hibernate 5 to the project', defaultFeature: true, required: false),
                                new Feature(name: 'json-views', description: 'Adds support for JSON Views to the project', defaultFeature: false, required: true),
                                new Feature(name: 'less-asset-pipeline', description: 'Adds LESS Transpiler Asset Pipeline to a Grails project', defaultFeature: false, required: false),
                                new Feature(name: 'markup-views', description: 'Adds support for Markup Views to the project', defaultFeature: false, required: false),
                                new Feature(name: 'mongodb', description: 'Adds GORM for MongoDB to the project', defaultFeature: false, required: false),
                                new Feature(name: 'neo4j', description: 'Adds GORM for Neo4j to the project', defaultFeature: false, required: false),
                                new Feature(name: 'rx-mongodb', description: 'Adds RxGORM for MongoDB to the project', defaultFeature: false, required: false),
                        ]),
                new Profile(name: 'rest-api-plugin', description: 'Profile for REST API plugins', version: '3.2.0', features:
                        [
                                new Feature(name: 'asset-pipeline-plugin', description: 'Adds Asset Pipeline to a Grails Plugin for packaging', defaultFeature: false, required: false),
                                new Feature(name: 'asset-pipeline', description: 'Adds Asset Pipeline to a Grails project', defaultFeature: false, required: false),
                                new Feature(name: 'events', description: 'Adds support for the Grails EventBus abstraction', defaultFeature: false, required: false),
                                new Feature(name: 'gsp', description: 'Adds support for GSP to the project', defaultFeature: false, required: false),
                                new Feature(name: 'hibernate4', description: 'Adds GORM for Hibernate 4 to the project', defaultFeature: false, required: false),
                                new Feature(name: 'hibernate5', description: 'Adds GORM for Hibernate 5 to the project', defaultFeature: true, required: false),
                                new Feature(name: 'json-views', description: 'Adds support for JSON Views to the project', defaultFeature: false, required: true),
                                new Feature(name: 'less-asset-pipeline', description: 'Adds LESS Transpiler Asset Pipeline to a Grails project', defaultFeature: false, required: false),
                                new Feature(name: 'markup-views', description: 'Adds support for Markup Views to the project', defaultFeature: false, required: false),
                                new Feature(name: 'mongodb', description: 'Adds GORM for MongoDB to the project', defaultFeature: false, required: false),
                                new Feature(name: 'neo4j', description: 'Adds GORM for Neo4j to the project', defaultFeature: false, required: false),
                                new Feature(name: 'rx-mongodb', description: 'Adds RxGORM for MongoDB to the project', defaultFeature: false, required: false),
                                new Feature(name: 'security', description: 'Adds Spring Security REST to the project', defaultFeature: false, required: false),
                        ]),
                new Profile(name: 'web-plugin', description: 'Profile for Plugins designed for Web applications', version: '3.3.0', features:
                        [
                                new Feature(name: 'asset-pipeline-plugin', description: 'Adds Asset Pipeline to a Grails Plugin for packaging', defaultFeature: true, required: false),
                                new Feature(name: 'asset-pipeline', description: 'Adds Asset Pipeline to a Grails project', defaultFeature: false, required: false),
                                new Feature(name: 'events', description: 'Adds support for the Grails EventBus abstraction', defaultFeature: false, required: false),
                                new Feature(name: 'gsp', description: 'Adds support for GSP to the project', defaultFeature: false, required: true),
                                new Feature(name: 'hibernate4', description: 'Adds GORM for Hibernate 4 to the project', defaultFeature: false, required: false),
                                new Feature(name: 'hibernate5', description: 'Adds GORM for Hibernate 5 to the project', defaultFeature: false, required: false),
                                new Feature(name: 'json-views', description: 'Adds support for JSON Views to the project', defaultFeature: false, required: false),
                                new Feature(name: 'less-asset-pipeline', description: 'Adds LESS Transpiler Asset Pipeline to a Grails project', defaultFeature: false, required: false),
                                new Feature(name: 'markup-views', description: 'Adds support for Markup Views to the project', defaultFeature: false, required: false),
                                new Feature(name: 'mongodb', description: 'Adds GORM for MongoDB to the project', defaultFeature: false, required: false),
                                new Feature(name: 'neo4j', description: 'Adds GORM for Neo4j to the project', defaultFeature: false, required: false),
                                new Feature(name: 'rx-mongodb', description: 'Adds RxGORM for MongoDB to the project', defaultFeature: false, required: false),
                        ]),
        ]
    }
}
