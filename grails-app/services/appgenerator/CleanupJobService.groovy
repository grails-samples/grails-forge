package appgenerator

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled

@CompileStatic
@Slf4j
class CleanupJobService {

    boolean lazyInit = false

    @Value('${TEMP_DIR_TO_CLEANUP:/home/vcap/tmp}')
    private final String TEMP_DIR_TO_CLEANUP

    private static final String GROOVY_TMP_DIR = 'groovy-generated'
    private static final int MINUTES_TO_PURGE = 1

    @Scheduled(initialDelay = 10_000L, fixedDelay = 60_000L)
    void removeTemporalDirectories() {
        log.info "Cleanup job started ${new Date()}"
        long purgeTime = System.currentTimeMillis() - MINUTES_TO_PURGE * 60 * 1000;

        File dir = new File(TEMP_DIR_TO_CLEANUP)
        if (!dir.exists()) {
            log.error "The temporal directory ${TEMP_DIR_TO_CLEANUP} doesn't exist"
            return
        }

        // Get directories older than MINUTES_TO_PURGE and delete them
        dir.eachDirMatch(~/${GROOVY_TMP_DIR}.*/) { File tmpDir ->
            if (tmpDir.lastModified() < purgeTime) {
                tmpDir.deleteDir()
                log.info "Deleting temporal directory ${tmpDir.absolutePath}"
            }
        }
    }
}
