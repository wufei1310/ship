package ship

class GoodsLog {

    static constraints = {
        
    }
    
     static mapping = {
        sort lastUpdated: "desc" // or "asc"
    }
    DaiFaGoods daiFaGoods
    User addUser
    String logdesc
    
    Date dateCreated
    Date lastUpdated
}
