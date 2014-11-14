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
        <h3>今日支出明细(${mapParam.startDate}~${mapParam.endDate})</h3>
      </div>
        
        <table class="table table-bordered">
        <thead>
          <tr>
            <td>订单号</td>        
            <td>金额</td>
            <td>描述</td>
            <td>时间</td>
            <td>小计</td>
          </tr>
        </thead>
        <tbody>
          
            <g:set var="shipzongji" value="0"/>
            <g:set var="noshipzongji" value="0"/>
            <tr><td colspan="5" style="color: red">已发货订单</td></tr>
            <g:each status="i" in="${orderShipMap}" var="order"> 
                <g:each status="j" in="${order.value}" var="goods"> 
                  <tr>
                    <g:if test="${j==0}">
                     <td rowspan="${order.value.size()}">${order.key}</td>
                     
                    </g:if>
                    <td><font <g:if test="${goods.type == '5'}">style="color:red"</g:if> >${goods.amount}</font></td>
                    <td><font <g:if test="${goods.type == '5'}">style="color:red"</g:if> ><tranLog:tranLogStatusDic status="${goods.type}"/></font></td>
                    <td>${goods.dateCreated.toString()[0..18]}</td>
                    <g:if test="${j==0}">
                     <td rowspan="${order.value.size()}">${orderAmountMap.get(order.key)}</td>
                     <g:set var="shipzongji" value="${orderAmountMap.get(order.key)+ Double.valueOf(shipzongji)}"/>
                    </g:if>
                    </tr>
                </g:each>
            </g:each>
          
          
          
          
          <tr>
            <td>小计</td>
            <td></td>
            <td></td>
            <td></td>
            <td>${shipzongji}</td>
            
          </tr>
          
          
          <tr><td colspan="5" style="color: red">未发货订单</td></tr>
            <g:each status="i" in="${orderNoShipMap}" var="order"> 
                <g:each status="j" in="${order.value}" var="goods"> 
                  <tr>
                    <g:if test="${j==0}">
                     <td rowspan="${order.value.size()}">${order.key}</td>
                     
                    </g:if>
                    <td><font <g:if test="${goods.type == '5'}">style="color:red"</g:if> >${goods.amount}</font></td>
                    <td><font <g:if test="${goods.type == '5'}">style="color:red"</g:if> ><tranLog:tranLogStatusDic status="${goods.type}"/></font></td>
                    <td>${goods.dateCreated.toString()[0..18]}</td>
                    <g:if test="${j==0}">
                     <td rowspan="${order.value.size()}">${orderAmountMap.get(order.key)}</td>
                     <g:set var="noshipzongji" value="${orderAmountMap.get(order.key)+ Double.valueOf(noshipzongji)}"/>
                    </g:if>
                    </tr>
                </g:each>
            </g:each>
          
          
          
          
          <tr>
            <td>小计</td>
            <td></td>
            <td></td>
            <td></td>
            <td>${noshipzongji}</td>
            
          </tr>
          <tr>
            <td>总计</td>
            <td></td>
            <td></td>
            <td></td>
            <td>${noshipzongji+shipzongji}</td>
            
          </tr>
          
        </tbody>
      </table>
        
    </body>
</html>
