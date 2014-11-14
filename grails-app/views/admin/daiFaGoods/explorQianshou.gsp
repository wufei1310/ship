

<%   
response.setContentType("application/vnd.ms-excel");
   response.addHeader("Content-Disposition", "attachment;filename=qianshou.xlsx");
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
            <tr><th colspan="14" height="80px">代发商品金额签收报表(导出时间：${nowDate})</th></tr>
            <tr>
              <th>序号</th>
              <th>受理人</th>
              <th>受理时间</th>
              <th>验收时间</th>
              <th>发货时间</th>
              <th>订单号</th> 
              <th>商品编号</th>
              <th>档口</th>
              <th>产品规格</th>
              <th>数量</th>
              <th>采购单价</th>
              <th>采购总价</th>
              <th>货款小计</th>
              <th>签收</th>
              <th>备注</th>
            </tr>
          </thead>
          <tbody>
          <g:set var="count" value="0"/>
           <g:each status="i" in="${daifaMap}" var="daifa"> 
             <g:each status="j" in="${daifa.value}" var="goods"> 
             <g:set var="count" value="${Integer.valueOf(count) + 1}"/>
                  <tr>
                    <td>${count}</td>
                  <g:if test="${j==0}">
                    <td rowspan="${daifa.value.size()}">${daifa.key}</td>
                  </g:if>
                    <td>${goods.processtime.toString()[0..18]}</td>
                    <td>${goods.checktime.toString()[0..18]}</td>
                    <td>${goods.daiFaOrder.ship_time.toString()[0..18]}</td>
                    <td>${goods.daiFaOrder.orderSN}</td>
                    <td>${goods.goods_sn}</td>
                    <td>${goods.market}-${goods.floor}-${goods.stalls}</td>
                    <td>${goods.spec}</td>
                    <td>${goods.num}</td>
                    <td>${goods.actual_price}</td>
                    <td>${goods.actual_price*goods.num}</td>
                    <g:if test="${j==0}">
                      <td rowspan="${daifa.value.size()}">${xiaojiMap.get(goods.daifa_user.email)}</td>
                      <td rowspan="${daifa.value.size()}">&nbsp;</td>
                      <td rowspan="${daifa.value.size()}">&nbsp;</td>
                    </g:if>                   
                  </tr>
              </g:each>
            </g:each>
            
          </tbody>
        </table>
    
  </body>
</html>

