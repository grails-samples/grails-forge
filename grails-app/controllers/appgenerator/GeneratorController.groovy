package appgenerator

import appgenerator.cmd.ProjectMetaData
import appgenerator.versions.GrailsVersion

class GeneratorController implements StreamsData {

    static responseFormats = ['json']

    ProjectGeneratorService projectGeneratorService
    VersionService versionService

    def generate(ProjectMetaData projectMetaData) {
        if (projectMetaData.version && !versionService.supportedVersions.contains(projectMetaData.version)) {
            projectMetaData.errors.rejectValue("version", "invalid", "The version specified is not supported")
        }

        if (projectMetaData.hasErrors()) {
            respond(projectMetaData)
            return
        }

        File projectFile = projectGeneratorService.getProject(projectMetaData)
        if(!projectFile) {
            respond([error: "Could not complete that request"], status: 403)
            return
        }

        outputToStream(projectMetaData.name + ".zip") { OutputStream outputStream ->
            streamBytes(projectFile.bytes, outputStream)
        }

        try {
            log.info("Deleting project file [${projectFile.absolutePath}]")
            projectFile.delete()
        } catch (Throwable t) {
            log.error "An error occurred deleting project file [${projectFile.absolutePath}]", t
        }
    }

    def validate(ProjectMetaData projectMetaData) {
        if (projectMetaData.version && !versionService.supportedVersions.contains(projectMetaData.version)) {
            projectMetaData.errors.rejectValue("version", "invalid", "The version specified is not supported")
        }

        if (projectMetaData.hasErrors()) {
            respond(projectMetaData)
        } else {
            render(status: 204)
        }
    }

    def generateDefault() {
        String version = params.get('version', defaultVersion)
        String name = params.name
        String profile = params.get('profile', 'web')
        List features
        if (params.features instanceof String && params.features.contains(',')) {
            features = ((String)params.features).split(',')
        } else {
            features = params.list('features')
        }
        ProjectMetaData metadata = new ProjectMetaData(version: version, name: name, profile: profile, features: features)
        metadata.validate()
        generate(metadata)
    }

    private String getDefaultVersion() {
        versionService.supportedVersions.collect {
            GrailsVersion.build(it)
        }.findAll {
            !it.isSnapshot()
        }.max().versionText
    }
}
