<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="adminMain"/>
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
            退换货列表(<small>匹配物流单:</small>${params.wuliu_sn})


            <g:link  controller="adminShipSN" action="list" class=" btn btn-large btn-primary">返回包裹管理</g:link>
        </h3>
       </div>



      <div class="bs-docs-example">
        <table class="table table-bordered">
          <thead>
            <tr>
              <th>订单号</th>
              <th>物流单号</th>
              <th>物流公司</th>
              <th>发货地</th>
              <th>创建时间</th>
              <th>操作</th>
            </tr>
          </thead>
          <tbody>
            
          <g:each in="${list}" status="i" var="returnOrder">
            <tr>
              <td>${returnOrder.orderSN}<g:if test="${returnOrder.flat == '1'}">(非平台订单)</g:if>
              </td>
              <td>${returnOrder.wuliu_sn}</td>
              <td>${returnOrder.wuliu}</td>
              <td>
                  <g:if test="${returnOrder.daiFaOrder}">

                      <g:areaName area_id="${returnOrder.daiFaOrder.area_id}"/>
                  </g:if>


              </td>
             
              <td>${returnOrder.dateCreated.toString()[0..18]}</td>
              <td>
                <g:link controller="adminShipSN" action="returnShow" id="${returnOrder.id}" params="${params}" class="btn">查看</g:link>
              </td>
            </tr>
          </g:each>
            
            
            
          </tbody>
        </table>
      </div>

<g:render template="/layouts/footer"/>


    </div> <!-- /container -->
    
  </body>
</html>
