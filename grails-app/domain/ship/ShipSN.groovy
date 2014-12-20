package ship
 //58151 14964
class ShipSN {

    static constraints = {
        actualnum(blank:true,nullable:true)
        area_id(blank:true,nullable:true)
        addUser(blank:true,nullable:true)
        addEmail(blank:true,nullable:true)
        orderSN(blank:true,nullable:true)
        shouliUserId(blank:true,nullable:true)
        shouliUser(blank:true,nullable:true)
        shouliTime(blank:true,nullable:true)
        actual_return_fee(blank:true,nullable:true)
        actual_returnTime(blank:true,nullable:true)
        shopOrderSN (blank:true,nullable:true)
        needTui(blank:true,nullable:true)
        tui_user(blank:true,nullable:true)
        tui_time(blank:true,nullable:true)
        is_qianshou (blank:true,nullable:true)
        qianshou_user (blank:true,nullable:true)
        qianshoutime (blank:true,nullable:true)
        goods_sn (blank:true,nullable:true)
        scanTime(blank:true,nullable:true)
        imgStr(blank:true,nullable:true)
    }

    String wuliu_sn;
    int num;  //输入数量
    int lurunum = 0 //录入商品数据数量 ,用于管理员对无主商品数据录入时统计已经录入的商品数量。当录入商品量与扫描输入的数量相等时，可以去退货了
    String actualnum;//实际订单数量
    String status;//            //0：物流单不存在 1:已核实，广州办收货确认2：单号匹配，商品数量不符 3:无主包裹，手工关联入库  4:已分配代发人员 5:已由代发人员退货完成 6与代发人员已结账 7:已关联订单号包裹 8　退货进行中无主包裹
    String needTui;//0:未关联退货申请１已关联退货申请，退货未结束２退货结束，未关联退货申请３退货结束，已关联退货申请，等待审核退款４：退款结束
    BigDecimal actual_return_fee  //实际退货金额
    String is_qianshou //是否提现 0 未提现 1申请提现 2已提现
    String area_id

    String orderSN //关联退货申请单号

    String shopOrderSN //关联原订单号


    Long addUser
    String addEmail

    User tui_user //退货操作人
    Date tui_time //退货操作时间

    Long shouliUserId //受理人id
    String shouliUser
    Date shouliTime

    Date actual_returnTime  //档口退货时间

    Date scanTime ;//包裹扫描　日期　
    String qianshou_user;
    Date qianshoutime


    String goods_sn

    String imgStr;


    Date dateCreated
    Date lastUpdated
}
