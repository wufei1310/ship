<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="adminMain"/>
    <title>金士代发</title>
  </head>
  <body>
    <g:if test="${flash.message}">
         <script>
         alert('${flash.message}')
        </script>
      </g:if>

    <div class="container body" id="page">
      <div class="page-header">
        <h3>放弃的退货不成商品

        <g:link controller="adminDaiFaGoods" action="clearReturnGoodsAndNoOwnerPack" target="_blank" class="btn btn-primary btn-large pull-right">清理30天前数据</g:link>

        </h3>
      </div>
      <div class="bs-docs-example">
        
        <table class="table table-bordered">
          <thead>
            <tr>
              <th>订单号</th>
              <th>市场</th>
              <th>货号</th>
              <th style="width: 100px">档口</th>
              <th style="width: 100px">规格</th>
              <th>件数</th>
              <th>单价</th>
               <th>受理人</th>
                <th>退货不成时间</th>
            </tr>
          </thead>
          <tbody>
            
          <g:each in="${list}" status="i" var="returnGoods">
            <tr>
              <td>
                  <g:link target="_blank" controller="adminDaiFaOrder" action="saleReturnList" params="[orderfrom:'kings',orderSN:'K'+returnGoods.daiFaGoods.daiFaOrder.orderSN]">
                  ${returnGoods.daiFaGoods.daiFaOrder.orderSN}
                  </g:link></td>
              <td>${returnGoods.daiFaGoods.market}</td>
              <td>${returnGoods.daiFaGoods.goods_sn}</td>
              <td>${returnGoods.daiFaGoods.floor}-${returnGoods.daiFaGoods.stalls}</td>
              <td>${returnGoods.daiFaGoods.spec}</td>
              <td>${returnGoods.daiFaGoods.num}</td>
              <td>${returnGoods.daiFaGoods.price}</td>
              <td>${returnGoods.shouliUser}
              </td>
              <td>${returnGoods.actual_returnTime}</td>
            </tr>
          </g:each>
            
            
            
          </tbody>
        </table>
      </div>
<div class="row-fluid">
      <div class="span12">
      <div  class="pagination pagination-right new-pagination" >
          


            <g:if test="${total != 0}">
                <g:paginate total="${total}"  params="${params}" action="clearReturnGoods"  update="page" />
           </g:if>
            
            <span class="currentStep">共${total}条数据</span>
          </div>
      </div>
    </div>
         <form     method="post" id="commonActionForm" target="innerFrame">
           <input type="hidden" name="id" id="id" class="key">
        </form>
        <iframe id="innerFrame" name="innerFrame" height="0" frameborder="0"></iframe>
         <g:form  action="${actionName}" params="${params}"  method="post" class="commonListForm">
        </g:form >       
      <g:render template="/layouts/footer"/>
    </div> <!-- /container -->
  </body>
</html>

