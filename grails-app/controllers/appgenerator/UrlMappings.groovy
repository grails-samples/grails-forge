package appgenerator

class UrlMappings {

    static mappings = {
        "/generate"(controller: 'generator', action: 'generate')
        "/validate"(controller: 'generator', action: 'validate')
        "/$name(.zip)"(controller: 'generator', action: 'generateDefault')
        "/appData"(controller: 'versions', action: 'appData')
        "/versions"(controller: 'versions', action: 'grailsVersions')
        "/$version/profiles"(controller: 'profile', action: 'profiles')
        "/$version/$profile/features"(controller: 'profile', action: 'features')
        "/projectoptions"(controller: "projectOptions", action: 'index')

        "/"(controller: "index")

        "500"(view:'/error')
        "404"(view:'/notFound')
    }
}
