package ship

class ShipSNTagLib {
    static namespace = "shipSN"
    def shipSNStatus = {attrs->


        switch (attrs.status){
            case "0": out << "物流单不存在"
                break;
            case "1": out << "单号，数据无误，已入库"
                break;
            case "2": out << "单号匹配，商品数量不符"
                break;
            case "3": out << "无主包裹，手工关联已入库"
                break;
            case "4": out << "已分配代发人员"
                break;
            case "5": out << "已由代发人员退货完成"
                break;
            case "6": out << "与代发人员已结账"
                break;
        }

    }


    def needTui = {attrs->
        switch (attrs.status){
            case "0": out << "未关联退换货申请"
                break;
            case "1": out << "已关联退换货申请，退货未结束"
                break;
            case "2": out << "退货结束，未关联退换货申请"
                break;
            case "3": out << "退货结束，已关联退换货申请，等待审核退款"
                break;
            case "4": out << "退款结束"
                break;

        }
    }
}
