package ship

class DiffGoods {

    static constraints = {
        reason(blank:true,nullable:true)
        payTime(blank:true,nullable:true)
    }
    static mapping = {
        sort lastUpdated: "desc" // or "asc"
    }
    
    static belongsTo = [daiFaGoods:DaiFaGoods]
    BigDecimal diffFee //差额
    String status //0待付款,1已付款2已取消
    String reason
    String orderSN
    Date addTime
    Date payTime;
    User addUser //操作人
    
    Date dateCreated
    Date lastUpdated
}
