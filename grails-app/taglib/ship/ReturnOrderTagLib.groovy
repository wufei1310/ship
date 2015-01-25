package ship

import grails.converters.JSON

class ReturnOrderTagLib {

//    String needTui;//0：暂不需要 1：退货处理完成，等待退款 2 已退款给会员 3 处理完成，无退款商品
//    String needShip;// 0：暂不需要 1：换货处理完成，等待发货 2 已发货给会员 3 处理完成，无发货商品


    def getMemberReturnFee = {attrs->
        println "==================:"+attrs.id
        def returnGoods = ReturnGoods.get(attrs.id)
        println returnGoods as JSON
        def memberReturnGoods = ReturnGoods.findByDaiFaGoodsAndOrderfrom(returnGoods.daiFaGoods,"member")
        out << memberReturnGoods?.return_fee
    }

    def getKingsReturnFee = {attrs->
        def returnGoods = ReturnGoods.get(attrs.id)
        def kingsReturnGoods = ReturnGoods.findByDaiFaGoodsAndOrderfrom(returnGoods.daiFaGoods,"kings")
        out << kingsReturnGoods?.actual_return_fee
    }


    def checkMKWuliusn = { attrs ->
        def memberReturnOrder = ReturnOrder.findByOrderSNAndOrderfrom("M" + attrs.ordersn.substring(1), "member")

        if(attrs.wuliu_sn&&memberReturnOrder){



            if(memberReturnOrder.wuliu_sn != attrs.wuliu_sn){
                out << "<span style='color:red'>" + attrs.ordersn + "</span>"
            }else{
                out << attrs.ordersn
            }
        }else{
            out << attrs.ordersn
        }


    }


    def memberReturnOrderTime = { attrs ->
        def mordersn = attrs.orderSN.replace("K", "M");
        def mReturnOrder = ReturnOrder.findByOrderSN(mordersn);
        if (mReturnOrder) {
            out << mReturnOrder.dateCreated
        }
    }


    def needTui = { attrs ->
        switch (attrs.status) {
            case "0": out << "退货处理中"
                break;
            case "1": out << "退货处理完成，等待退款"
                break;
            case "2": out << "已退款给会员"
                break;
            case "3": out << "处理完成，无退款商品"
                break;

        }

    }


    def needShip = { attrs ->
        switch (attrs.status) {
            case "0": out << "换货处理中"
                break;
            case "1": out << "换货处理完成，等待发货"
                break;
            case "2": out << "已发货给会员"
                break;
            case "3": out << "处理完成，无发货商品"
                break;

        }

    }

    def returnGoods = { attrs ->
        switch (attrs.status) {
            case "0": out << "收到退货申请"
                break;
            case "1": out << "收到退回货物"
                break;
            case "2": out << "已指派专人处理"
                break;
            case "4": out << "已退货"//"已退货，档口退款"
                break;
            case "5": out << "正在退中"//"已受理,领出办事处"
                break;
            case "6": out << "退货不成功"
                break;
            case "7": out << "已换货"
                break;
            case "8": out << "换货不成功"
                break;
        }
    }

    def returnOrder = { attrs ->
        try {
            if (session.loginPOJO.user.user_type == 'admin') {
                switch (attrs.status) {
                    case "0": out << "未支付"
                        break;
                    case "1": out << "已支付,等待处理"
                        break;
                    case "2": out << "退货处理结束"
                        break;
                }
            }
            if (session.loginPOJO.user.user_type == 'member') {
                switch (attrs.status) {
                    case "0": out << "未支付"
                        break;
                    case "1": out << "正在处理中"
                        break;
                    case "2": out << "处理结束"
                        break;
                }
            }
        } catch (Exception e) {
            switch (attrs.status) {
                case "0": out << "未支付"
                    break;
                case "1": out << "已支付,等待处理"
                    break;
                case "2": out << "退货完成,已退款"
                    break;
                case "4": out << "已退货,档口已回款"
                    break;
                case "5": out << "换货完成,已发货"
                    break;
            }
        }


    }
}
