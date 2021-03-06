<%@ page import="ship.User" %>
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
          退货审核列表
          
        </h3>
       </div>

      <div class="well well-large">
        <form class="form-inline">
           订单号：
          <input name="orderSN" type="text" class="" placeholder="" value="${params.orderSN}">
          状态：
          <g:select class="input-medium" value="${params.needTui}" name="needTui" optionKey="status" optionValue="queryShow" from="${[[status:"",queryShow:"全部"],[status:"1",queryShow:"退货处理完成，等待退款"],[status:"2",queryShow:"已退款给会员"]]}" />

          是否认领：
          <g:select class="input-medium" value="${params.isling}" name="isling" optionKey="status" optionValue="queryShow" from="${[[status:"",queryShow:"全部"],[status:"member",queryShow:"已经认领"],[status:"admin",queryShow:"没有认领"]]}" />

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
              <th>类型</th>
              <th>状态</th>
              <th>退款人</th>
              <th>创建时间</th>
              <th>操作</th>
            </tr>
          </thead>
          <tbody>
            
          <g:each in="${list}" status="i" var="returnOrder">
            <tr>
              <td>${returnOrder.orderSN}<g:if test="${returnOrder.flat == '1'}">(非平台订单)</g:if>
              </td>

              <td>退货</td>
              <td>
                  <g:if test="${returnOrder.status=='1'}">退货处理中</g:if>
                  <g:if test="${returnOrder.status=='2'}">退货处理结束</g:if>



                  <g:if test="${returnOrder.needTui=='1'}">等待退款</g:if>
                  <g:if test="${returnOrder.needTui=='2'}">已退款</g:if>

                  &nbsp;&nbsp;
                  <g:if test="${returnOrder.needShip=='1'}">等待发货</g:if>
                  <g:if test="${returnOrder.needShip=='2'}">已发货</g:if>


                  <br/>
                  <span style="color: red">
                      <g:if test="${User.get(returnOrder.add_user).user_type=='admin'}">会员还没认领</g:if>
                      <g:if test="${User.get(returnOrder.add_user).user_type=='member'}">会员已经认领</g:if>

                  </span>


              </td>
              <td>${returnOrder.tui_user?.email}</td>
              <td>${returnOrder.dateCreated.toString()[0..18]}</td>
              <td>
                <g:link controller="adminDaiFaOrder" action="saleReturnAmountShow" id="${returnOrder.id}" params="${params}" class="btn">查看</g:link>
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
                <g:paginate total="${total}"  params="${params}" action="saleReturnAmountList"  update="page" />
           </g:if>
            
            <span class="currentStep">共${total}条数据</span>
          </div>
        
        </div>
      </div>
        
<g:render template="/layouts/footer"/>


    </div> <!-- /container -->
    
  </body>
</html>
