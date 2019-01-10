package appgenerator

import grails.config.Config
import grails.core.support.GrailsConfigurationAware
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.springframework.scheduling.annotation.Scheduled

/**
 * This is a workaround for https://github.com/grails-samples/grails-forge/issues/7 to delete the temporal directories
 * that are created when a new application is created with the forge.
 *
 * The root issue is fixed here https://github.com/grails/grails-core/pull/11209 but that only applies to Grails 3.3.10+
 * and Grails 4.0.0+, hence the need of this workaround to avoid filling the disk.
 */
@CompileStatic
@Slf4j
class CleanupJobService implements GrailsConfigurationAware {

    boolean lazyInit = false

    String cleanupDir
    String groovyTmpDir
    Integer threshold

    @Override
    void setConfiguration(Config co) {
        cleanupDir = co.getProperty('cleanup.tmpdir', String, '/home/vcap/tmp')
        groovyTmpDir = co.getProperty('cleanup.groovytmpdir', String, 'groovy-generated')
        threshold = co.getProperty('cleanup.threshold', Integer, 1)
    }

    @Scheduled(initialDelay = 10_000L, fixedDelay = 60_000L)
    void removeTemporalDirectories() {
        log.info "Cleanup job started ${new Date()}"
        long purgeTime = System.currentTimeMillis() - threshold * 60 * 1000

        File dir = new File(cleanupDir)
        if (!dir.exists()) {
            log.error "The temporal directory ${cleanupDir} doesn't exist"
            return
        }

        // Get directories older than "threshold" (in minutes) and delete them
        dir.eachDirMatch(~/${groovyTmpDir}.*/) { File tmpDir ->
            if (tmpDir.lastModified() < purgeTime) {
                boolean deletionResult = tmpDir.deleteDir()
                log.info "Deleting temporal directory ${tmpDir.absolutePath} with result ${deletionResult}"
            }
        }
    }
}
