<r:require modules="bootstrapDatetimepicker"/>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
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
        <h3>账户收支明细<span style="font-size: 0.5em">(友情提醒：网站暂不支持提现服务，您的账户余额可用于支付货款等相关费用)</span>
        </h3>
      </div>
      <div class="well well-large">
        <g:form class="form-inline" action="list" >
          订单号：
          <input name="orderSN" type="text" class="" placeholder="" value="${params.orderSN}">
          时间：
          <input class="datetime" value="${params.start_date}"  type="text" name="start_date">  ~  <input class="datetime" value="${params.end_date}"  type="text" name="end_date"> 
         
          
          <button type="submit" class="btn btn-primary">查询</button> 
        </g:form>
      </div>
      
      <div class="bs-docs-example">

        <table class="table table-bordered">
          <thead>
            <tr>
              <th>订单号</th>
              <th>收支</th>
              <th>金额</th>
              <th>支付方式</th>
              <th>描述</th>
              <th>时间</th>
            <th>账户余额</th>
            </tr>
          </thead>
          <tbody>
            
          <g:each in="${list}" status="i" var="tranLog">
            <tr>
              <td>${tranLog.orderSN}</td>
              <td><g:if test="${tranLog.direction == '0'}">支出</g:if>
                  <g:if test="${tranLog.direction == '1'}">收入</g:if>
              </td>
              <td>${tranLog.amount}</td>
              <td><g:if test="${tranLog.shouru_type == '0'}">会员账户余额支付</g:if>
                  <g:if test="${tranLog.shouru_type == '1'}">支付宝支付</g:if>
              </td>
              <td><tranLog:tranLogStatusDic4Member status="${tranLog.type}"/></td>
              <td>${tranLog.dateCreated.toString()[0..18]}</td>
                <td>${tranLog.memberamount}</td>
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

