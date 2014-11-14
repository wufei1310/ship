package ship
class Role {

    static constraints = {
        roleDesc(blank:true,nullable:true)
    }
    static mapping = {
        sort lastUpdated: "desc" // or "asc"
    }
    
    static hasMany = [menu:Menu]
    String roleName
    String roleDesc
    String type //member daifa admin
    
    Date dateCreated
    Date lastUpdated
}
