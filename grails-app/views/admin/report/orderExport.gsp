
<%   
response.setContentType("application/vnd.ms-excel");
   response.addHeader("Content-Disposition", "attachment;filename=orderReport(${params.start_date} - ${params.end_date}).xlsx");
   %>
<html>
  <head>
    <title>金士代发</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <style>
      table td{mso-number-format:"\@";} 
　　</style>
  </head>
  <body>

    <table width="100%" border="1" cellpadding="2" cellspacing="0">
          <thead>
            <tr><th colspan="14" height="80px">代发采购报表( 已发货  ${params.start_date} 至 ${params.end_date})</th></tr>
            <tr>
              <th>序号</th>
              <th>支付日期</th>
              <th>订单号</th>              
              <th>寄件人信息</th>
              <th>收件人信息</th>
              <th>物流运费</th>
              <th>物流名称</th>
              <th>商品编号</th>
              <th>档口</th>
              <th>产品规格</th>
              <th>数量</th>
              <th>采购单价</th>
              <th>货款小计</th>      
              <th>备注</th>
            </tr>
          </thead>
          <tbody>
            <g:set var="count" value="0"/>
            <g:set var="wuliu_amount" value="0"/>
            <g:set var="goods_count" value="0"/>
            <g:set var="goods_amount" value="0"/>
            <g:set var="goods_all_amount" value="0"/>
           <g:each status="i" in="${dateMap}" var="date"> 
             <g:each status="j" in="${date.value}" var="order"> 
                <g:each status="k" in="${order.daiFaGoods}" var="goods">
                  <tr>
                    <td>${count}</td>
                  <g:if test="${j==0 && k==0}">
                    <td rowspan="${countMap.get(date.key)}">${date.key}</td>
                  </g:if>
                  <g:if test="${k==0}">
                    <td rowspan="${order.daiFaGoods.size()}">${order.orderSN}</td>
                    <td rowspan="${order.daiFaGoods.size()}">${order.sendperson}<br/>${order.sendcontphone}<br/>${order.sendaddress}</td>
                    <td rowspan="${order.daiFaGoods.size()}">${order.reperson}<br/>${order.contphone}<br/>${order.address}</td>
                    <td rowspan="${order.daiFaGoods.size()}">${order.actual_shipfee}</td>
                    <td rowspan="${order.daiFaGoods.size()}">${order.wuliu}</td>
                    
                    <g:set var="wuliu_amount" value="${order.actual_shipfee + new BigDecimal(wuliu_amount)}"/>
                  </g:if>
                    <td>${goods.goods_sn}</td>
                    <td>${goods.market}-${goods.floor}-${goods.stalls}</td>
                    <td>${goods.spec}</td>
                    <td>${goods.num}</td>
                    <g:set var="goods_count" value="${goods.num + Long.valueOf(goods_count)}"/>
                    <g:if test="${goods.status == '6'}">
                      <td>缺货</td>
                      <td>缺货</td>
                    </g:if><g:else>
                      <td>${goods.actual_price}</td>
                      <td>${goods.actual_price*goods.num}</td>  
                      
                      <g:set var="goods_amount" value="${goods.actual_price + new BigDecimal(goods_amount)}"/>
                      <g:set var="goods_all_amount" value="${goods.actual_price*goods.num + new BigDecimal(goods_all_amount)}"/>
                    </g:else>
                    <td>&nbsp;</td>
                  </tr>
                  <g:set var="count" value="${Integer.valueOf(count) + 1}"/>
                </g:each>
              </g:each>
            </g:each>
            <tr height="40px">
              <td colspan="5" style="text-align:center">小计</td>
              <td>${wuliu_amount.setScale(2, BigDecimal.ROUND_HALF_UP)}</td><td>&nbsp;</td> <td>&nbsp;</td> <td> &nbsp;</td> <td>&nbsp; </td> <td>${goods_count}</td><td>${goods_amount.setScale(2, BigDecimal.ROUND_HALF_UP)}</td><td>${goods_all_amount.setScale(2, BigDecimal.ROUND_HALF_UP)}</td><td>&nbsp;</td>
            </tr>
          </tbody>
        </table>
    
  </body>
</html>

