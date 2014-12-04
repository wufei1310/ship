/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package admin

import ship.Express
import ship.DaiFaOrder
import ship.MobileMessage
import grails.converters.JSON

/**
 *
 * @author DELL
 */
class AdminOrderShipController extends BaseController {



    def addMoreOrderWuliuSN(){

        println params

        def startOrderSN = params.startOrderSN_wuliuSN.split("\\|")[0]
        def startDaifaOrder = DaiFaOrder.findByOrderSN(startOrderSN)



        def startWuliuSN = params.startOrderSN_wuliuSN.split("\\|")[1].toString()
        def endWuliuSN = params.endOrderSN_wuliuSN.split("\\|")[1].toString()

        def len = startWuliuSN.length();
        def preString = startWuliuSN.substring(0,len-5);
        Long backStartWuliuSN = ("1"+ startWuliuSN.substring(len-4,len)) as Long;
        Long backEndWuliuSN = ("1"+ endWuliuSN.substring(len-4,len)) as Long;


        def daifaOrderList = DaiFaOrder.findAllByWuliuAndIsCanExportInListAndStatusNotEqual(startDaifaOrder.wuliu, ['1', '3'],'kill', [sort: "canExport_date", order: "asc"])



        boolean isIndex = false;

        daifaOrderList.each {



            if (it.orderSN == startOrderSN) {
                isIndex = true;
            }
            if (isIndex) {
                println "============"
                println "backStartWuliuSN:"+backStartWuliuSN + "  backEndWuliuSN:"+backEndWuliuSN
                if(backStartWuliuSN<=backEndWuliuSN){

                    println "订单号："+it.orderSN
                    println "分配快递单号：" + preString + backStartWuliuSN.toString().substring(1)
                    it.wuliu_no = preString + backStartWuliuSN.toString().substring(1)
                    it.save();
                    backStartWuliuSN++;
                }

            }

        }


        def mm = new MobileMessage()
        mm.message = "批量为订单绑定快递单号成功"
        mm.result = "success"

        render mm as JSON

    }

    def searchEndOrder() {

        def orderSN = params.orderSN_wuliuSN.split("\\|")[0]
        def startWuliuSN = params.orderSN_wuliuSN.split("\\|")[1]

        def endWuliuSN = params.endWuliuSN

        def len = startWuliuSN.length();
        println len
        def preString = startWuliuSN.substring(0,len-5);
        Long backStartWuliuSN = ("1"+ startWuliuSN.substring(len-4,len)) as Long;
        Long backEndWuliuSN = ("1"+ endWuliuSN.substring(len-4,len)) as Long;


        println  "backStartWuliuSN:"+backStartWuliuSN
        println  "backEndWuliuSN:"+backEndWuliuSN
        def daifaOrder = DaiFaOrder.findByOrderSN(orderSN)
        daifaOrder.wuliu_no = startWuliuSN;
        def daifaOrderList = DaiFaOrder.findAllByWuliuAndIsCanExportInListAndStatusNotEqual(daifaOrder.wuliu, ['1', '3'],'kill', [sort: "canExport_date", order: "asc"])
        boolean isIndex = false;
        def endDaifaOrder;

        daifaOrderList.each {
            if (it.orderSN == orderSN) {
                isIndex = true;
            }
            if(backStartWuliuSN==backEndWuliuSN){
                endDaifaOrder = DaiFaOrder.findByOrderSN(it.orderSN);
                endDaifaOrder.wuliu_no = endWuliuSN.toString();
            }
            if (isIndex) {
                backStartWuliuSN++;
            }
        }

        def map=[startOrder:daifaOrder,endOrder:endDaifaOrder]

        def mm = new MobileMessage()
        mm.message = "获取截止订单成功"
        mm.result = "success"
        mm.model = map

        render mm as JSON

    }


    def list() {
        def express = Express.list();
        express.each {
            String name = it.name
            def n_explore = DaiFaOrder.countByWuliuAndIsCanExportInListAndIs_exploreAndStatusNotEqual(name, ['1', '3'], "0",'kill')
            def y_explore = DaiFaOrder.countByWuliuAndIsCanExportInListAndIs_exploreAndStatusNotEqual(name, ['1', '3'],'1','kill')
            it.y_explore = y_explore
            it.n_explore = n_explore
        }
        def map = [list: express]
        render(view: "/admin/daifaShip/list", model: map)
    }


    def expressMobile() {


        def dabao = new Express();
        dabao.name = "大包"

        def express = Express.list();
        express.add(dabao)
        express.each {
            String name = it.name
            def n_explore = DaiFaOrder.countByWuliuAndIsCanExportInListAndStatusNotEqual(name, ['1', '3'],'kill')
            it.n_explore = n_explore
        }
        def map = [list: express]

        if (params.mobile == "mobile") {

            def mm = new MobileMessage()
            mm.message = "获取快递公司及其下订单数量成功"
            mm.result = "success"
            mm.model = map

            render mm as JSON
        }

    }


    def explore() {
        def date = new Date()
        def explore = DaiFaOrder.findAllByWuliuAndIsCanExportInListAndStatusNotEqualAndIs_explore(params.name, ['1', '3'],'kill',params.is_explore, [sort: "canExport_date", order: "asc"])
        def num_map = [:]

        explore.each {
            int goodsNum = 0
            def goods = it.daiFaGoods
            goods.each { i ->
                goodsNum = goodsNum + i.num
            }
            num_map.put(it.id, goodsNum)
            if(params.is_explore == '0'){
                DaiFaOrder.executeUpdate("update DaiFaOrder set is_explore = '1' where id = '"+it.id+"'")
            }
        }
        def map = [list: explore, num_map: num_map]
        String file = date.format('MM-dd-yyyy') + params.name + date.format('HH：mm') + ".xls"
        map.file = file
        render(view: "/admin/daifaShip/explore", model: map)
    }
}

