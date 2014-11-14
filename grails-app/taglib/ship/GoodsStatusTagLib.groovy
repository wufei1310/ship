package ship


//05519318594
class GoodsStatusTagLib {
    static namespace = "goods"

    def goodsStatusDic = { attrs ->
        switch (attrs.status) {
            case "0": out << "未受理"
                break;
            case "1": out << "已受理"
                break;
            case "2": out << "已拿货"
                break;
            case "3": out << "价格过高，暂不拿货"
                break;
            case "4": out << "等待补款"
                break;
            case "5": out << "缺货"
                break;
            case "6": out << "已取消"
                break;
            case "7": out << "已验收"
                break;
            case "8": out << "暂时缺货"
                break;

            case "9": out << "已发货"
                break;
            case "10": out << "已退货退款"
                break;


            case "killing": out << "紧急中止中"
                break;
            case "kill_wait": out << "紧急中止,商品已经受理，待确认是否拿货"
                break;
            case "kill_return": out << "紧急中止,商品已经拿货"
                break;
            case "killed": out << "已中止发货"
                break;
        }

    }
}
