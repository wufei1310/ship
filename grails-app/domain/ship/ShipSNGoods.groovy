package ship

class ShipSNGoods {

    static constraints = {
        market(blank:true,nullable:true)
        floor(blank:true,nullable:true)
        stalls(blank:true,nullable:true)
    }

    String wuliu_sn;
    String market
    String floor
    String stalls //档口
    String goods_sn
    int num;  //输入数量

    Long addUser;

    Date dateCreated
    Date lastUpdated
}
