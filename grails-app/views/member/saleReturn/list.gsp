<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
    <title>金士代发</title>
  </head>
  <body>
    <div class="container body" id="page">

      <g:if test="${flash.message}">
         <script>
         alert('${flash.message}')
        </script>
      </g:if>
      
      

      <div class="page-header">
        <h3>
          退货申请列表
            <g:link controller="memberDaiFaOrder" action="list" class="btn btn-primary btn-large pull-right">申请退货</g:link>
        </h3>
       </div>

      <div class="well well-large">
        <form class="form-inline">
           订单号：
          <input name="orderSN" type="text" class="input-small" placeholder="" value="${params.orderSN}">
           状态：
           <g:select class="input-medium" value="${params.status}" name="status" optionKey="status" optionValue="queryShow" from="${[[status:"",queryShow:"全部"],[status:"0",queryShow:"未支付"],[status:"1",queryShow:"处理中"],[status:"2",queryShow:"处理结束"]]}" />

          <button type="submit" class="btn btn-primary">查询</button>
        </form>
      </div>


      <div class="bs-docs-example">
        <table class="table table-bordered">
          <thead>
            <tr>
              <th>订单号</th>
<!--              <th>物流单号</th>
              <th>物流公司</th>-->
              <th>状态</th>
              <th>创建时间</th>
              <th>操作</th>
            </tr>
          </thead>
          <tbody>
            
          <g:each in="${list}" status="i" var="returnOrder">
            <tr>
              <td>${returnOrder.orderSN}<g:if test="${returnOrder.flat == '1'}">(非平台订单)</g:if>
              </td>
<!--              <td>${returnOrder.wuliu_sn}</td>
              <td>${returnOrder.wuliu}</td>-->
              <td>

                <g:returnOrder status="${returnOrder.status}" />

                 <g:if test="${returnOrder.needTui=='1'}">等待退款</g:if>
                 <g:if test="${returnOrder.needTui=='2'}">已退款</g:if>

                 &nbsp;&nbsp;
                <g:if test="${returnOrder.needShip=='1'}">等待发货</g:if>
                <g:if test="${returnOrder.needShip=='2'}">已发货</g:if>
              </td>
             
              <td>${returnOrder.dateCreated.toString()[0..18]}</td>
              <td>
                <g:link controller="memberDaiFaOrder" action="saleReturnShow" id="${returnOrder.id}" params="${params}" class="btn">查看</g:link>
              </td>
            </tr>
          </g:each>
            
            
            
          </tbody>
        </table>
      </div>
      <div class="row-fluid">
        <div class="span12">
          <div  class="pagination pagination-right new-pagination">
          


            <g:if test="${total != 0}">
                <g:paginate total="${total}"  params="${params}" action="saleReturnList"  update="page" />
           </g:if>
            
            <span class="currentStep">共${total}条数据</span>
          </div>
        
        </div>
      </div>
        
<g:render template="/layouts/footer"/>


    </div> <!-- /container -->
    
  </body>
</html>
