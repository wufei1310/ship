package ship

class TranLogStatusTagLib {
    static namespace = "tranLog"
	
    def tranLogStatusDic = { attrs ->
        switch (attrs.status){
        case "1": out << "验收商品"
            break;
        case "2": out << "发货运费"
            break;
        case "3": out << "会员对商品补款"
            break;
        case "4": out << "会员对运费补款"
            break;
        case "5": out << "会员取消商品退款"
            break;
        case "6": out << "会员取消商品退运费"
            break;
        case "7": out << "会员支付商品"
            break;
        case "8": out << "会员支付运费"
            break;
        
        case "9": out << "会员支付服务费"
            break;
        case "10": out << "退货退款"
            break;
        case "11": out << "空包退还代发费用"
            break;
        case "12": out << "会员支付退货服务费"
            break;
        case "13": out << "会员换货支付差价"
            break;
        case "14": out << "会员支付换货运费"
            break;
        case "15": out << "发货运费（换货）"
            break;
        case "16": out << "退货档口回款"
            break;
        case "17": out << "换货差价支出"
            break;
        case "18": out << "会员充值"
            break;
        case "19": out << "会员充值"
            break;
        case "20": out << "会员支付礼品费"
            break;
        case "21": out << "会员取消商品退礼品费"
            break;
        case "22": out << "会员更换档口退货款"
            break;
        case "23": out << "会员更换档口超过2次支付费用"
            break;
            case "24": out << "会员终止订单收取服务费"
                break;
            case "25": out << "会员终止订单退回货款"
                break;
            case "28": out << "会员终止订单退回物流费"
                break;
            case "26": out << "管理员直接对订单退运费"
                break;
            case "27": out << "管理员直接对商品退货款"
                break;
        }
         //370612
    }
}
