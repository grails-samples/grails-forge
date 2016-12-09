package appgenerator.profile

import groovy.transform.TupleConstructor

@TupleConstructor(includes = ['name', 'description'])
class Feature {
    String name
    String description
    boolean defaultFeature
    boolean required
}
