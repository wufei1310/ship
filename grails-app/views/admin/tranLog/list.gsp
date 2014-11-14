<r:require modules="bootstrapDatetimepicker"/>
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
        <h3>订单金额流水列表
        </h3>
      </div>
      <div class="well well-large">
        <g:form class="form-inline" action="list" >
          订单号：
          <input name="orderSN" type="text" class="" placeholder="" value="${params.orderSN}">
          时间：
          <input class="datetime" value="${params.start_date}"  type="text" name="start_date">  ~  <input class="datetime" value="${params.end_date}"  type="text" name="end_date"> 
         
          <br/><br/>
           收/支：
          <g:select  value="${params.direction}" name="direction" optionKey="v" optionValue="queryShow" from="${[[v:"",queryShow:"全部"],[v:"0",queryShow:"收入"],[v:"1",queryShow:"支出"]]}" />

            方式：
            <g:select  value="${params.shouru_type}" name="shouru_type" optionKey="v" optionValue="queryShow" from="${[[v:"",queryShow:"全部"],[v:"0",queryShow:"会员账户余额支付"],[v:"1",queryShow:"支付宝支付"]]}" />

            收支类型：
          <g:select  value="${params.type}" name="type" optionKey="v" optionValue="queryShow" from="${[[v:"",queryShow:"全部"],[v:"1",queryShow:"[支出]验收商品"],[v:"2",queryShow:"[支出]发货运费 "],[v:"5",queryShow:"[支出]会员取消商品退款"],[v:"6",queryShow:"[支出]会员取消商品退运费"],[v:"10",queryShow:"[支出]退货退款"],[v:"11",queryShow:"[支出]空包退还代发费用"],[v:"15",queryShow:"[支出]发货运费(换货)"],[v:"17",queryShow:"[支出]换货差价支出"],[v:"19",queryShow:"[支出]会员充值"],[v:"21",queryShow:"[支出]会员取消商品退礼品费"],[v:"22",queryShow:"[支出]会员更换档口退货款"],[v:"25",queryShow:"[支出]会员强制停止订单退回货款和物流费"],[v:"26",queryShow:"[支出]管理员直接对订单退运费"],[v:"27",queryShow:"[支出]管理员直接对商品退货款"],[v:"7",queryShow:"[收入]会员支付商品"],[v:"8",queryShow:"[收入]会员支付运费"],[v:"9",queryShow:"[收入]会员支付服务费"],[v:"3",queryShow:"[收入]会员对商品补款"],[v:"4",queryShow:"[收入]会员对运费补款"],[v:"12",queryShow:"[收入]会员支付退换货服务费"],[v:"13",queryShow:"[收入]会员换货支付差价"],[v:"14",queryShow:"[收入]会员支付换货运费"],[v:"16",queryShow:"[收入]退货档口回款"],[v:"18",queryShow:"[收入]会员充值"],[v:"20",queryShow:"[收入]会员支付礼品费"],[v:"23",queryShow:"[收入]会员更换档口超过2次支付费用"],[v:"24",queryShow:"[收入]会员强制停止订单收取物流费"]]}" />
          <button type="submit" class="btn btn-primary">查询</button> <br/>
        </g:form>
      </div>
      
      <div class="bs-docs-example">
        <span style="float: right"> <font color="red">收入/支出：${shouru}/${zhichu}</font></span>
        <table class="table table-bordered">
          <thead>
            <tr>
              <th>订单号</th>
              <th>收支</th>
              <th>金额</th>
              <th>商品数量</th>
              <th>支付方式</th>
              <th>描述</th>
              <th>时间</th>          
            </tr>
          </thead>
          <tbody>
            
          <g:each in="${list}" status="i" var="tranLog">
            <tr>
              <td>${tranLog.orderSN}</td>
              <td><g:if test="${tranLog.direction == '0'}">收入</g:if>
                  <g:if test="${tranLog.direction == '1'}">支出</g:if>
              </td>
              <td>${tranLog.amount}</td>
              <td>${tranLog.num}</td>
              <td><g:if test="${tranLog.shouru_type == '0'}">会员账户余额支付</g:if>
                  <g:if test="${tranLog.shouru_type == '1'}">支付宝支付</g:if>
                  <g:if test="${tranLog.shouru_type == '2'}">线下支付</g:if>
              </td>
              <td><tranLog:tranLogStatusDic status="${tranLog.type}"/></td>
              <td>${tranLog.dateCreated.toString()[0..18]}</td>
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
      <script>
    $(document).ready(function(){
		 $(".datetime").datetimepicker({
			format: "yyyy-mm-dd",
			autoclose: true,
			todayBtn: true,
			pickerPosition: "bottom-left",
			minView:4,
			language:'zh-CN'
		    });
	  })

</script>
    </div> <!-- /container -->
  </body>
</html>

