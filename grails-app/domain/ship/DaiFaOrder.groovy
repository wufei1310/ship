package ship

class DaiFaOrder {

    
    static constraints = {
        payTime(blank:true,nullable:true)
        processing_user_id(blank:true,nullable:true)
        processing_user_name(blank:true,nullable:true)
        processing_time(blank:true,nullable:true)
        ship_time(blank:true,nullable:true)
        wuliu_no(blank:true,nullable:true)
        diffFee(blank:true,nullable:true)
        senddesc(blank:true,nullable:true)
        actual_shipfee(blank:true,nullable:true)
        diffShip(blank:true,nullable:true)
        h_senddesc(blank:true,nullable:true)
        area_id(blank:true,nullable:true)
        
        goodsFee(min:new BigDecimal(0))
        shipFee(min:new BigDecimal(0))
        serviceFee(min:new BigDecimal(0))
        totalFee(min:new BigDecimal(0))
        diffFee(min:new BigDecimal(0))
        actual_shipfee(min:new BigDecimal(0))
        diffShip(min:new BigDecimal(0))
        is_explore(blank:true,nullable:true)
        isCanExport(blank:true,nullable:true)
        canExport_date(blank:true,nullable:true)
    }
    
    static mapping = {
        daiFaGoods sort: 'id', order: 'desc'
    }

    static hasMany = [ daiFaGoods : DaiFaGoods,diffOrder : DiffOrder ]


    String orderSN //订单号
    String status //待付款(waitpay),已付款等待拿货(waitaccept),部分拿货(partaccept),拿货完成(allaccept),已发货(shipped) 已删除（delete） 等待补款/缺货（error）问题订单（problem） 已取消（close） 需要补运费（diffship）强制停止（kill）
    String reperson //收货人
    String contphone //收货人联系电话 
    String address //收货地址
    String sendperson//发件人
    String sendaddress //发件人地址
    String senddesc//发件人备注
    
    String h_senddesc //历史备注
    
    String sendcontphone //发件人手机
    
    Long processing_user_id //受理人id
    String processing_user_name //受理人
    
    Date processing_time //受理时间 
    Date ship_time //发货时间
    String wuliu_no //物流单号
    
    String wuliu //物流公司
    
    BigDecimal goodsFee; //商品总价
    BigDecimal shipFee //运费
    BigDecimal serviceFee //服务费
    BigDecimal totalFee //总费用 
    BigDecimal diffFee//差额价格
    BigDecimal actual_shipfee //实际发货运费
    BigDecimal diffShip;//运费差额
    BigDecimal regardsFee //礼品费
    
    String regards //礼品
    
    Date payTime;
    
    Long add_user //创建的人
    
   // String num //订单的数量
   // String orderDate //下订单日期
   // static transients = ['num','orderDate']
    
    String area_id
    
    String type = "0" //0没有退换货  1有退换货
   // String is_tuihuo = "0" //0 正常订单  1 退货 2换货


    
    Date dateCreated
    Date lastUpdated
    String isCanExport //0否1可 当订单下所有商品都已拿货时，表示可以导出物流单号了。2订单已经发出.3 已关联
    Date canExport_date; //可以导出的时间
    String is_explore = "0" //0 未导出 1已导出
    
}
