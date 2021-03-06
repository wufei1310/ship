package ship

class OrderAdminStatusTagLib {
    static namespace = "orderAdmin"
	
    def orderStatusDic = { attrs ->
        switch (attrs.status){
        case "waitpay": out << "未付款"
            break;
        case "waitaccept": out << "已付款等待拿货"
            break;
        case "partaccept": out << "已部分验收"
            break;
        case "allaccept": out << "验收完成，等待发货"
            break;
        case "shipped": out << "已发货"
            break;
        case "error": out << "等待补款/缺货"
            break;
        case "close": out << "已取消"
            break;
        case "problem": out << "问题订单"
            break;
        case "diffship": out << "需要补运费"
            break;
            case "kill": out << "终止订单"
                break;
        }
		
    }
}
