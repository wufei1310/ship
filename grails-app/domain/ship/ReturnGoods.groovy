package ship
import groovy.transform.AutoClone

@AutoClone
class ReturnGoods{

    static constraints = {
        daiFaGoods(blank:true,nullable:true)
        num(blank:true,nullable:true)
        price(blank:true,nullable:true)
        actual_price(blank:true,nullable:true)
        price(min:new BigDecimal(0))
        actual_price(min:new BigDecimal(0))
        return_fee(min:new BigDecimal(0))
        return_num(min:0l)



        rukuUser(blank:true,nullable:true)
        rukuTime(blank:true,nullable:true)

        fenjianUser(blank:true,nullable:true)
        fenjianTime(blank:true,nullable:true)

        shouliUser(blank:true,nullable:true)
        shouliTime(blank:true,nullable:true)

        shouliUserId(blank:true,nullable:true)

        actual_return_fee(blank:true,nullable:true)
        actual_returnTime(blank:true,nullable:true)

        reason (blank:true,nullable:true)
        is_qianshou(blank:true,nullable:true)

        qianshoutime(blank:true,nullable:true)
        qianshou_user(blank:true,nullable:true)

        returnReason(blank:true,nullable:true)
        orderfrom (blank:true,nullable:true)
    }

    static belongsTo = [returnOrder:ReturnOrder]
    
    DaiFaGoods daiFaGoods
    
    String market
    String floor
    String stalls //档口
    String goods_sn
    String spec //规格
    String num //拿货件数
    BigDecimal price; //单价
    BigDecimal actual_price //实际拿货价格
    
    String type //0 退货 1 换货
    Long add_user //创建人
    Long return_num //退货/换货件数
    BigDecimal return_fee //退货单价/换货商品单价

    BigDecimal actual_return_fee  //实际退货单价

    Date actual_returnTime  //档口退货时间


    String status;//0:新提交退换货，1:广州办入库 2:分拣完成，等待受理  4：已退货，档口退款 5 已受理,领出办事处  6 退货不成功  7 换货成功 8 换货不成功
    String is_qianshou //是否提现 0 未提现 1申请提现 2已提现
    String returnReason //会员要求退换货原因
    String reason //退换货不成功原因

    String rukuUser;//入库人
    Date rukuTime; //入库时间

    String qianshou_user;
    Date qianshoutime


    String fenjianUser
    Date fenjianTime

    Long shouliUserId //受理人id
    String shouliUser
    Date shouliTime



    String orderfrom="kings" //member

    
    Date dateCreated
    Date lastUpdated

}
