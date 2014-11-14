package admin

class AdminApk {

    static constraints = {
        app_type(inList:["cg_android_daifa_native"] )
        platform(inList:["android","ios"] )
    }
    
    int versioncode
    String versionname
    String newapkfeature
    String app_type
    String platform
    Date dateCreated
    Date lastUpdated
   
}
