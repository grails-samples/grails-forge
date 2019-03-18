import grails.util.Environment
import groovy.transform.CompileStatic
//import org.quartz.Scheduler

class BootStrap {
    //Scheduler quartzScheduler

    def init = { servletContext ->
        if ( Environment.current == Environment.DEVELOPMENT ) {
            configureForDevelopment()
        } else if ( Environment.current == Environment.TEST ) {
            configureForTest()
        } else if ( Environment.current == Environment.PRODUCTION ) {
            configureForProduction()
        }
    }

    def destroy = {
    }

    @CompileStatic
    void configureForTest() {
    }

    @CompileStatic
    void configureForDevelopment() {
        //quartzScheduler.start()
    }

    @CompileStatic
    void configureForProduction() {
        //quartzScheduler.start()
    }
}
