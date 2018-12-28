package appgenerator

import appgenerator.cmd.ProjectMetaData
import appgenerator.zip.ZipHelper

import java.nio.file.Paths

class ProjectGeneratorService {

    private static final String GENERATE_SCRIPT = """
        import org.grails.cli.profile.commands.CreateAppCommand
        import org.grails.cli.profile.repository.MavenProfileRepository
        import grails.build.logging.GrailsConsole

        def cmd = new CreateAppCommand.CreateAppCommandObject(
            appName: applicationName,
            baseDir: baseDirectory,
            profileName: profileName,
            grailsVersion: grailsVersion,
            features: featureList,
            inplace: false
        )
        if (cmd.hasProperty('console')) {
            cmd.console = GrailsConsole.instance
        }

        new CreateAppCommand(profileRepository: new MavenProfileRepository()).handle(cmd)
    """

    protected ZipHelper zipHelper = new ZipHelper()

    File getProject(ProjectMetaData projectMetaData) {

        String version = projectMetaData.version
        String applicationName = projectMetaData.appName
        String profile = projectMetaData.profile

        log.info "Generating application '${applicationName}' for Grails version '${version}' with profile '${profile}'"

        File tmpDirectory = File.createTempDir()
        File projectDirectory = Paths.get(tmpDirectory.absolutePath, applicationName).toFile()

        generateApp(projectMetaData, tmpDirectory)
        if (projectDirectory.exists()) {
            File outputZipFile = zipProject(projectDirectory)
            deleteProjectDirectory(tmpDirectory)

            outputZipFile
        } else {
            null
        }
    }

    private static void generateApp(ProjectMetaData projectMetaData, File tmpDirectory) {
        Map params = [
            applicationName: projectMetaData.name,
            baseDirectory: tmpDirectory.canonicalFile,
            profileName: projectMetaData.profile,
            grailsVersion: projectMetaData.version,
            featureList: projectMetaData.features
        ]

        ScriptExecutor.executeScript(projectMetaData.version, GENERATE_SCRIPT, "generate", params)
    }

    private File zipProject(File projectDirectory) {
        File outputZipFile = File.createTempFile("tmp_", "grails")
        zipHelper.zipFolder(projectDirectory.absolutePath, outputZipFile)

        return outputZipFile
    }

    private void deleteProjectDirectory(File projectDirectory) {
        try {
            log.info "Deleting application directory [${projectDirectory.absolutePath}]"
            projectDirectory.deleteDir()
        } catch (Throwable t) {
            log.error "An error occurred deleting project file [${projectDirectory.absolutePath}]", t
        }
    }
}