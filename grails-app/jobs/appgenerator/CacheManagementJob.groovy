package appgenerator

class CacheManagementJob {

    VersionService versionService

    static triggers = {
        cron name: 'everyHour', startDelay: 0, cronExpression: '0 0 * * * ?'
    }

    def execute() {
        versionService.clearVersionsCache()
    }
}
