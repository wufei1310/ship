package ship

/**
 * Created by wufei on 14-8-8.
 */

//用于收集退货商品订单相关信息，返回给前端
class ReturnGoodsPOJO {



    Long returnOrderId
    Long returnGoodsId
    Long goodsId

    String market
    String floor
    String stalls //档口
    String goods_sn
    String spec //规格

    String status



    String changeMarket
    String changeFloor
    String changeStalls //档口
    String changeGoods_sn
    String changeSpec //规格


    BigDecimal actual_price //实际拿货价格
    BigDecimal return_fee //退货单价
    BigDecimal shipFee //运费
    Long num //退货/换货件数


    BigDecimal actual_return_fee  //实际退货单价

    Date actual_returnTime  //档口退货时间

    String wuliu  //退回物流公司
    String wuliu_sn //退回物流单号
    String orderSN //订单号

    String reason//
    String returnReason
    String type //0 退货 1 换货


    String shouliUser
    Date shouliTime
}
