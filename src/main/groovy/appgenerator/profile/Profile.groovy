package appgenerator.profile

import groovy.transform.ToString

@ToString(excludes = ['description'])
class Profile {
    String name
    String description
    String version
    List<Feature> features
}
