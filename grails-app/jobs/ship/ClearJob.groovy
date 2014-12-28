package ship

import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.support.PropertiesLoaderUtils

import java.text.SimpleDateFormat


class ClearJob {

    def mailService
    def properties = PropertiesLoaderUtils.loadProperties(new ClassPathResource("sysSetting.properties"))
    def isPro = properties.getProperty("isPro");

    static triggers = {
        //simple repeatInterval: 5000l // execute job once in 5 seconds
        cron name: 'clearTrigger', cronExpression: "1 1 3 * * ?" //每天3点清理数据
    }

    def execute() {
        // execute job

        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Calendar lastDate = Calendar.getInstance();
//        lastDate.set(Calendar.DATE,29);//设为当前月的1 号
//        lastDate.set(Calendar.MONTH,2)
        lastDate.add(Calendar.MONTH,-1);//减一个月，变为下月的1 号
        def str=sdf.format(lastDate.getTime());

        Date lastMonthDay = Date.parse("yyyy-MM-dd HH:mm:ss",str+" 23:59:59")

        def returnGoodsList = ReturnGoods.findAllByStatusAndActual_returnTimeLessThan("6",lastMonthDay)
        def goodsSN = ""
        returnGoodsList.each {
            it.status = "10"
            goodsSN = goodsSN+ "<br/>" + it.goods_sn + " # " + it.market + " # " + it.floor + " # " +it.stalls;
            it.save();
        }
        goodsSN = "退货不成商品：" +  goodsSN
//
        def shipSNList = ShipSN.findAllByStatusAndScanTimeLessThan("noowner",lastMonthDay)
        def shipSNStr = ""

        shipSNList.each {
            it.status = "giveup"
            shipSNStr = shipSNStr+ "<br/>" + it.wuliu_sn
            it.save();
        }

        shipSNStr = "无主包裹："+ shipSNStr

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        println new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + ":清理 " + df.format(lastMonthDay) + "之前的无主包裹和退货不成的数据成功 "


        Thread.start {
            mailService.sendMail {
                async true
                from "service@findyi.com"
                if(isPro=="true"){
                    to "findyi@icloud.com","mail3298@gmail.com","wufei1310@126.com"
                }else{
                    to "wufei1310@126.com"
                }

                subject new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + ":清理 " + df.format(lastMonthDay) + "之前的无主包裹和退货不成的数据成功 "
                html goodsSN+"<br/><hr/><br/>" + shipSNStr

            }
        }

    }
}
