package appgenerator

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.springframework.scheduling.annotation.Scheduled

@CompileStatic
@Slf4j
class CacheManagementJobService {

    VersionService versionService

    @Scheduled(cron = "0 0 * * * ?")
    void execute() {
        log.debug 'cache management triggered'
        versionService.clearVersionsCache()
        //Re-populate cache
        versionService.getSupportedVersions()
    }
}
