package appgenerator.profile

import groovy.transform.ToString
import groovy.transform.TupleConstructor

@ToString(excludes = ['description'])
@TupleConstructor(includes = ['name', 'description'])
class Feature {
    String name
    String description
    boolean defaultFeature
    boolean required
}
