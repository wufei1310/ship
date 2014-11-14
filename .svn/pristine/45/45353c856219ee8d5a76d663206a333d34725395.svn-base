<!--
To change this template, choose Tools | Templates
and open the template in the editor.
-->
<!DOCTYPE html>
<html>
    <head>
        <title></title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <!-- 最新 Bootstrap 核心 CSS 文件 -->
        <style type="text/css">


.head table{
  width:100%;
}

.content{
  width:100%;
}

 table
        {
            border-collapse: collapse;
            border: none;
            width: 100%;
        }
     table    td
        {
            border: solid #000 1px;
        }
</style>

    </head>
    <body>
        <div class="page-header">
        <h3>已发货订单收入支出对照明细(${mapParam.startDate}~${mapParam.endDate})</h3>
      </div>
      
      实际拿货款：${mapParam.t_shipped_actual_price}<br/>
      实际发货运费：${mapParam.t_shipped_actual_shipfee}<br/>
      换货发货运费：${mapParam.t_shipped_actualhuanhuo_shipfee}<br/>
      档口退货回款：${mapParam.t_return_goodsFee}<br/>
      凡衣拨款：${mapParam.t_shipped_actual_price+mapParam.t_shipped_actual_shipfee-mapParam.t_return_goodsFee}<br/>
      
        <table class="table table-bordered">
        <thead>
          <tr>
            <td colspan="1" width="10%"></td>
            <td colspan="1" width="25%"></td>
            <td colspan="1" ></td>
            <td colspan="2">收入</td>
            <td colspan="3">支出</td>
            
          </tr>
        </thead>
        <tbody>
          
          <tr>
            <td>订单号</td>
            <td>发货时间</td>
            <td>服务费（不包括空包）</td>
            <td>货款</td>
            <td>运费</td>
            <td>商品验收货款</td>
            <td>商品取消退回货款</td>
            <td>运费</td>
         
          </tr>
          <g:set var="totalserviceFee" value="0"/>
          <g:set var="totalingoodsfee" value="0"/>
          <g:set var="totalinshipfee" value="0"/>
          <g:set var="totaloutgoodsfee" value="0"/>
          <g:set var="totalreturngoodsfee" value="0"/>
          <g:set var="totaloutshipfee" value="0"/>
          <g:each status="i" in="${shipOrder}" var="order"> 
            <tr>
              <td>${order.orderSN}</td>
              <td>${order.ship_time.toString()[0..18]}</td>
              <td>${order.serviceFee}</td>
              <g:set var="totalserviceFee" value="${new BigDecimal(totalserviceFee)+ order.serviceFee}"/>
              <td>${order.goodsFee}</td>
              <g:set var="totalingoodsfee" value="${new BigDecimal(totalingoodsfee)+ order.goodsFee}"/>
              <td>${order.shipFee}</td>
              <g:set var="totalinshipfee" value="${new BigDecimal(totalinshipfee)+ order.shipFee}"/>
              <td>${shipGoodsMap.get(order.orderSN)}</td>
              <g:set var="totaloutgoodsfee" value="${new BigDecimal(totaloutgoodsfee)+ shipGoodsMap.get(order.orderSN)}"/>
              <td>${returnGoodsMap.get(order.orderSN)}</td>
              <g:set var="totalreturngoodsfee" value="${new BigDecimal(totalreturngoodsfee)+ returnGoodsMap.get(order.orderSN)}"/>
              <td>${order.actual_shipfee}</td>
              <g:set var="totaloutshipfee" value="${new BigDecimal(totaloutshipfee)+ order.actual_shipfee}"/>
            </tr>
          </g:each>
          <tr>
              <td colspan="2">总计</td>
            <td>${totalserviceFee }</td>
            <td>${totalingoodsfee }</td>
            <td>${totalinshipfee }</td>
            <td>${totaloutgoodsfee }</td>
            <td>${totalreturngoodsfee }</td>
            <td>${totaloutshipfee }</td>
         
          </tr>
          <tr>
            <td colspan="2">总计</td>
            <td>${totalserviceFee as float }</td>
            <td colspan="2">${(totalingoodsfee as float)+(totalinshipfee as float) }</td>
            <td colspan="3">${(totaloutgoodsfee as float)+(totaloutshipfee as float)+(totalreturngoodsfee as float) }</td>
         
          </tr>
          <tr>
            <td colspan="3">净利</td>
            <td colspan="5">${(totalingoodsfee+totalinshipfee-totaloutgoodsfee-totaloutshipfee-totalreturngoodsfee) }</td>
         
          </tr>
          
         
          
          
        </tbody>
      </table>
    </body>
</html>
