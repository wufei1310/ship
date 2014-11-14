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
        <h3>退换货申请收入支出对照明细(${mapParam.startDate}~${mapParam.endDate})</h3>
      </div>
        
        <table class="table table-bordered">
        <thead>
          <tr>
            <td colspan="1" ></td>
            <td colspan="1" ></td>
            <td colspan="1" ></td>
            <td colspan="1" ></td>
            <td colspan="4">收入</td>
            <td colspan="3">支出</td>
            
          </tr>
        </thead>
        <tbody>
          
          <tr>
            <td>订单号</td>
            <td>类型</td>
            <td>状态</td>
            <td>提交时间</td>
            <td>会员支付退换货服务费</td>
            <td>会员支付换货运费</td>
            <td>会员换货支付差价</td>
            <td>退货档口回款</td>
            
            
            <td>发货运费(换货)</td>
            <td>退货退款</td>
            <td>换货差价支出</td>
          </tr>


          <g:set var="totalinshipfee" value="${new BigDecimal(0)}"/>
          <g:set var="totalinservicefee" value="${new BigDecimal(0)}"/>
          <g:set var="totaloutshipfee" value="${new BigDecimal(0)}"/>
          <g:set var="totalintuikuan" value="${new BigDecimal(0)}"/>
          <g:set var="totalouttuikuan" value="${new BigDecimal(0)}"/>
          <g:set var="totalinchajia" value="${new BigDecimal(0)}"/>
          <g:set var="totaloutchajia" value="${new BigDecimal(0)}"/>
          <g:each status="i" in="${returnOrderList}" var="order"> 
            <tr <g:if test="${order.status == '4'}"> style="color: red" </g:if>>
              <td>${order.orderSN}</td>
              <td><g:if test="${order.type == '0'}">退货</g:if>
                  <g:else>换货</g:else></td>
          <td><g:returnOrder status="${order.status}" /></td>
          <td>${order.dateCreated.toString()[0..18]}</td>
          
              <g:set var="totalinservicefee" value="${totalinservicefee+ order.serviceFee}"/>
              <td>${order.serviceFee}</td>
              <g:if test="${order.type == '1'}">
                  <g:set var="totalinshipfee" value="${totalinshipfee+ order.shipFee}"/>
                  <td>${order.shipFee}</td>
                  <g:set var="totalinchajia" value="${totalinchajia+order.chajia}"/>
                  <td>${order.chajia}</td>
                  <td>0.00</td>
                  <g:if test="${order.status == '5'}">
                      <g:set var="totaloutshipfee" value="${totaloutshipfee+order.actual_shipFee}"/>
                      <td>${order.actual_shipFee}</td>
                  </g:if><g:else>
                      <td>0.00</td>
                  </g:else>
                  <td>0.00</td>
                  <g:if test="${order.status == '5'}">
                      <g:set var="totaloutchajia" value="${totaloutchajia+order.chajia}"/>
                      <td>${order.chajia}</td>
                  </g:if><g:else>
                      <td>0.00</td>
                  </g:else>
              </g:if><g:else>
                  <td>0.00</td>
                  <td>0.00</td>
                  <g:if test="${order.status == '4' || order.status == '2'}">
                      <g:set var="totalintuikuan" value="${totalintuikuan+order.goodsFee}"/>
                      <td>${order.goodsFee}</td>
                  </g:if><g:else>
                      <td>0.00</td>
                  </g:else>
                  <td>0.00</td>
                  <g:if test="${order.status == '2'}">
                      <g:set var="totalouttuikuan" value="${totalouttuikuan+order.goodsFee}"/>
                      <td>${order.goodsFee}</td>
                  </g:if><g:else>
                      <td>0.00</td>
                  </g:else>
                  <td>0.00</td>
              </g:else>
              
             
              
            </tr>
          </g:each>
          <tr>
              <td colspan="4">总计</td>
            <td>${totalinservicefee.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()}</td>
            <td>${totalinshipfee.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()}</td>
            <td>${totalinchajia.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()}</td>
            <td>${totalintuikuan.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()}</td>
            <td>${totaloutshipfee.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()}</td>
            <td>${totalouttuikuan.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()}</td>
            <td>${totaloutchajia.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()}</td>
         
          </tr>
          <tr>
            <td colspan="4">总计</td>
            <td colspan="4">${(totalinservicefee+totalinshipfee+totalinchajia+totalintuikuan).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()}</td>
            <td colspan="3">${(totaloutshipfee+totalouttuikuan+totaloutchajia).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()}</td>
         
          </tr>
          <tr>
            <td colspan="4">净利</td>
            <td colspan="7">${(totalinservicefee+totalinshipfee+totalinchajia+totalintuikuan-totaloutshipfee-totalouttuikuan-totaloutchajia).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()}</td>
         
          </tr>
          
         
          
          
        </tbody>
      </table>
    </body>
</html>
