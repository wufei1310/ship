package ship

class DiffOrder {

    static constraints = {
        reason(blank:true,nullable:true)
        payTime(blank:true,nullable:true)
    }
    
    
    int compareTo(obj) {
        dateCreated.compareTo(obj.dateCreated)
    }
    
    static belongsTo = [daiFaOrder:DaiFaOrder]
    BigDecimal diffFee //差额
    String status //待付款(waitpay),已付款(haspay)
    String reason
    Date payTime;
    String orderSN //订单号
    
    Long add_user //创建的人
    
    Date dateCreated
    Date lastUpdated
}
