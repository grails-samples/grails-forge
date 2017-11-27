package appgenerator

import com.agileorbit.schwartz.SchwartzJob
import org.quartz.DisallowConcurrentExecution
import org.quartz.JobExecutionContext
import org.quartz.JobExecutionException
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

@SuppressWarnings(['UnusedMethodParameter'])
@CompileStatic
@Slf4j
@DisallowConcurrentExecution
class CacheManagementJobService implements SchwartzJob {

    VersionService versionService

    void execute(JobExecutionContext context) throws JobExecutionException {
        log.debug 'cache management triggered'
        versionService.clearVersionsCache()
        //Re-populate cache
        versionService.getSupportedVersions()
    }

    @SuppressWarnings(['BuilderMethodWithSideEffects', 'EmptyMethod'])
    void buildTriggers() {
        triggers << factory('Cbox Backend Sync Cron Trigger').cronSchedule('0 0 * * * ?').build()
    }
}
