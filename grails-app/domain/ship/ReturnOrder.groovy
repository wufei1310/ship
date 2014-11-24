package ship

import groovy.transform.AutoClone

@AutoClone
class ReturnOrder {

    static constraints = {
        check_user(blank:true,nullable:true)
        check_time(blank:true,nullable:true)
        reason(blank:true,nullable:true)
        shipFee(blank:true,nullable:true)
        payTime(blank:true,nullable:true)
        daiFaOrder(blank:true,nullable:true)
        orderSN(blank:true,nullable:true)
        chajia(blank:true,nullable:true)
        h_wuliu_sn(blank:true,nullable:true)
        shipTime(blank:true,nullable:true)
        actual_shipFee(blank:true,nullable:true)
        tui_user(blank:true,nullable:true)
        tui_time(blank:true,nullable:true)
        chajia(min:new BigDecimal(0))
        actual_shipFee(min:new BigDecimal(0))
        shipFee(min:new BigDecimal(0))
        serviceFee(min:new BigDecimal(0))
        serviceFee(blank:true,nullable:true)

        goodsFee(min:new BigDecimal(0))
        goodsFee(blank:true,nullable:true)
        totalFee(blank:true,nullable:true)
        totalFee(min:new BigDecimal(0))
        wuliupic(blank:true,nullable:true)

        needTui(blank:true,nullable:true)
        needShip(blank:true,nullable:true)
        isScan(blank:true,nullable:true)
        flat(blank:true,nullable:true)
        orderfrom(blank:true,nullable:true)
        ishuiyuanxiadan(blank:true,nullable:true)
    }
    static mapping = {
            sort lastUpdated: "desc" // or "asc"
            returnGoods sort: 'id', order: 'desc'
     }
     
    static hasMany = [ returnGoods : ReturnGoods]

     DaiFaOrder daiFaOrder
     String wuliu  //退回物流公司
     String wuliu_sn //退回物流单号
     String orderSN //订单号

    String needTui;//0：暂不需要 1：退货处理完成，等待退款 2 已退款给会员 3 处理完成，无退款商品
    String needShip;// 0：暂不需要 1：换货处理完成，等待发货 2 已发货给会员 3 处理完成，无发货商品

     String status //0 未支付 1 已支付,系统分配处理中    2 处理结束         //2 退货完成,已退款会员账户 3 审核不通过 4 退货处理结束，等待审核退款会员 5 换货完成,已发货  6 换货处理结束，等待发货
     String type //0 退货 1换货 2 同时含有退货  3 会员中止订单自动生成退货 4 扫描无主包裹自动生成退货申请　5　管理员对无主包裹录入商品后生成退货
     Long add_user //下单人
     User tui_user //退货操作人
     Date tui_time //退货操作时间
     User check_user //审核人
     Date check_time
     String reason
     Date payTime //支付时间

    BigDecimal chajia; //差价
    String h_wuliu_sn //换货发货物流单号
    Date shipTime //发货时间
    BigDecimal actual_shipFee //实际发货运费
    
    BigDecimal goodsFee; //商品退回的费用/换货商品支付费用
    BigDecimal shipFee //运费
    BigDecimal serviceFee //服务费
    BigDecimal totalFee //支付总费用
    
    String flat //0 平台订单  1非平台订单 
    
    String sendperson//发件人
    String sendaddress //发件人地址
    String sendcontphone //发件人手机

    String wuliupic//快递单　图片

    String isScan//是否已经正常扫描入库0否１是


    String orderfrom="kings" //member
    String ishuiyuanxiadan //会员是否已经下单　
    
    Date dateCreated
    Date lastUpdated
}
