package ship

class DaiFaGoods{

    static constraints = {
        actual_price(blank:true,nullable:true)
        daifa_user(blank:true,nullable:true)
        check_user(blank:true,nullable:true)
        processtime(blank:true,nullable:true)
        checktime(blank:true,nullable:true)
        nahuotime(blank:true,nullable:true)
        diffFee(blank:true,nullable:true)
        tuihuo_price(blank:true,nullable:true)
        tuihuo_num(blank:true,nullable:true)
        shuoming(blank:true,nullable:true)
        qianshou_user(blank:true,nullable:true)
        qianshoutime(blank:true,nullable:true)
        attach_id(blank:true,nullable:true)
        num(min:0l)
        tuihuo_num(min:0l)
        price(min:new BigDecimal(0))
        actual_price(min:new BigDecimal(0))
        diffFee(min:new BigDecimal(0))
        tuihuo_price(min:new BigDecimal(0))
        yanshoudesc(blank:true,nullable:true)
        changeStallsNum(blank:true,nullable:true)
        tip(blank:true,nullable:true)
    }
    static hasMany = [ diffGoods : DiffGoods ]
    static belongsTo = [daiFaOrder:DaiFaOrder]



    String market
    String floor
    String stalls //档口

    Long changeStallsNum = 0

    String goods_sn
    String spec //规格
    String shuoming //缺货说明
    Long num //件数
    BigDecimal price; //单价
    BigDecimal actual_price //实际拿货价格
    String status //0未受理 1已受理 2已拿货 3价格过高，暂不拿货 4等待补款 5缺货
    // 6已取消 7已验收
    // 8 暂时缺货
    // 9已发货 10 已退货退款
    // killing(紧急中止中)　
    // kill_wait(已经受理的商品等待确认是否拿货，如果已经拿货则将进入退货程序)
    // kill_return(由代发人员确认已拿货，进入此状态。
    // 当订单下商品状态全都是killing 订单直接中止，退款
    // 当订单下商品状态kill_return和killing时，退款和生成退货申请
    // 当订单下商品全是kill_return时，生成退货申请
    // killed(已中止货发)
    User daifa_user
    User check_user
    BigDecimal diffFee //差额
    
    BigDecimal tuihuo_price //退货金额
    Long tuihuo_num   //退货数量
    
//    User quehuo_user //确定缺货人
//    Date quehuo_time;//缺货时间
    Date nahuotime;//拿货时间
    Date processtime;//受理时间
    Date checktime//验收时间
    
    String is_qianshou //是否提现 0 未提现 1申请提现 2已提现
    User qianshou_user //签收人
    Date qianshoutime//签收时间
    
    Long add_user //创建的人
    Date dateCreated
    Date lastUpdated

    String attach_id

    String tip


    String yanshoudesc;//验收备注


}
