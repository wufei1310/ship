package ship

class GoodsLog {

    static constraints = {
        operuser(blank:true,nullable:true)
    }
    static mapping = {
        sort lastUpdated: "desc" // or "asc"
    }
    DaiFaGoods daiFaGoods
    User addUser
    String operuser;
    String logdesc
    
    Date dateCreated
    Date lastUpdated
}
