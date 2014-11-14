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
        <h3>退货退款申请列表
        </h3>
      </div>
      <div class="well well-large">
        <g:form class="form-inline" action="list" >
          订单号：
          <input name="orderSN" type="text" class="" placeholder="" value="${params.orderSN}">
          货号：
          <input name="goods_sn" type="text" class="" placeholder="" value="${params.goods_sn}">
          
          状态：
           <g:select class="input-medium" value="${params.status}" name="status" optionKey="status" optionValue="queryShow" from="${[[status:"",queryShow:"全部"],[status:"0",queryShow:"未审核"],[status:"1",queryShow:"审核通过"],[status:"2",queryShow:"审核不通过"]]}" />

          <button type="submit" class="btn btn-primary">查询</button>
        </g:form>
      </div>
      
      <div class="bs-docs-example">
        
        <table class="table table-bordered">
          <thead>
            <tr>
              <th>订单号</th>
              <th>货号</th>
              <th>件数</th>
              <th>单件拿货金额</th>
              <th>退货件数</th>
              <th>单件退款金额</th>
              <th>状态</th>
              <th>申请人</th>
              <th>申请时间</th>          
              <th>操作</th>
            </tr>
          </thead>
          <tbody>
            
          <g:each in="${list}" status="i" var="returnGoodsApp">
            <tr>
              <td>${returnGoodsApp.orderSN}</td>
              <td>${returnGoodsApp.daiFaGoods.goods_sn}</td>
              <td>${returnGoodsApp.daiFaGoods.num}</td>
              <td>${returnGoodsApp.daiFaGoods.actual_price}</td>
              <td>${returnGoodsApp.tuihuo_num}</td>
              <td>${returnGoodsApp.tuihuo_price}</td>
              
              <td>
                   <g:if test="${returnGoodsApp.status == '0'}">未审核</g:if>
                   <g:if test="${returnGoodsApp.status == '1'}">审核通过</g:if>
                   <g:if test="${returnGoodsApp.status == '2'}">审核不通过</g:if>
              </td>
              <td>${returnGoodsApp.addUser.email}</td>
              <td>${returnGoodsApp.dateCreated.toString()[0..18]}</td>
              <td>
              <g:if test="${session.loginPOJO.user.email == 'superquan' && returnGoodsApp.status == '0'}">
                <a class="btn btn-primary" href="javascript:void(0)" onclick="toActionCom({id:'${returnGoodsApp.id}',status:'1'},'<%=request.getContextPath()%>/adminReturnGoodsApp/checkReturnGoods','确认审核通过吗？')">审核通过</a>
                <a class="btn btn-primary" href="javascript:void(0)" onclick="toActionCom({id:'${returnGoodsApp.id}',status:'2'},'<%=request.getContextPath()%>/adminReturnGoodsApp/checkReturnGoods','确认审核不通过吗？')">审核不通过</a>
              </g:if>
              </td>
            </tr>
          </g:each>
            
            
            
          </tbody>
        </table>
      </div>
<div class="row-fluid">
      <div class="span12">
      <div  class="pagination pagination-right new-pagination" >
          


            <g:if test="${total != 0}">
                <g:paginate total="${total}"  params="${params}" action="list"  update="page" />
           </g:if>
            
            <span class="currentStep">共${total}条数据</span>
          </div>
      </div>
    </div>
         <form     method="post" id="commonActionForm" target="innerFrame">
           <input type="hidden" name="id" id="id" class="key">
           <input type="hidden" name="status" id="status" class="key">
           
        </form>
        <iframe id="innerFrame" name="innerFrame" height="0" frameborder="0"></iframe>
         <g:form  action="${actionName}" params="${params}"  method="post" class="commonListForm">
        </g:form >       
      <g:render template="/layouts/footer"/>
    </div> <!-- /container -->
  </body>
</html>

