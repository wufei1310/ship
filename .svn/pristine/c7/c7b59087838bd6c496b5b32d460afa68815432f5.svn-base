import grails.converters.JSON;
import sysinit.SysInitParams
class BootStrap {

    def init = { 
        JSON.registerObjectMarshaller(Date) {

            return it?.format("yyyy-MM-dd HH:mm:ss")
        }
        
        SysInitParams.getInstance()

    }
    def destroy = {
    }
}
