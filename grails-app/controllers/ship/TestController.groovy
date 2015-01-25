package ship

import grails.converters.JSON

class TestController {

    def index() {

        def startDate = Date.parse("yyyy-MM-dd HH:mm:ss", "2015-1-14 00:00:00")
        def endDate = Date.parse("yyyy-MM-dd HH:mm:ss", "2015-1-14 23:59:59")

        def shippedOrderList = DaiFaOrder.findAllByStatusInListAndShip_timeBetween(['shipped', 'kill'], startDate, endDate)

        def shippedTranList = TranLog.findAllByTypeInListAndOrderSNInList(['26','27'],shippedOrderList.orderSN)



    }
}
