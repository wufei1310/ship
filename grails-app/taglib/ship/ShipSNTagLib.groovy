package ship

class ShipSNTagLib {
    static namespace = "shipSN"
    def shipSNStatus = {attrs->


        switch (attrs.status){
            case "new": out << "新登记包裹"
                break;
            case "noowner": out << "无主包裹"
                break;
            case "giveup": out << "放弃不要了 "
                break;
        }

    }


    def needTui = {attrs->
        switch (attrs.status){
            case "0": out << "未关联退货申请"
                break;
            case "1": out << "已关联退货申请，退货未结束"
                break;
            case "2": out << "退货结束，未关联退货申请"
                break;
            case "3": out << "退货结束，已关联退货申请，等待审核退款"
                break;
            case "4": out << "退款结束"
                break;

        }
    }

    def scanTime = { attrs->
        def shipSN = ShipSN.findByWuliu_sn(attrs.wuliu_sn)
        if(shipSN){
            out << shipSN.scanTime
        }

    }

    def packOrderSN = {  attrs->
        def shipSN = ShipSN.findByWuliu_sn(attrs.wuliu_sn)
        if(shipSN){
            out << shipSN.orderSN
        }
    }
}
