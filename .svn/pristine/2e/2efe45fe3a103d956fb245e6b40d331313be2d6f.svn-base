<r:require modules="bootstrapDatetimepicker"/>
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
          支付宝充值列表
      </h3>
        
        </div>
      <div class="well well-large">
        <g:form class="form-inline" action="list" >
          交易流水：
          <input name="remitSN" type="text" class="" placeholder="" value="${params.remitSN}">
          时间：
          <input class="datetime" value="${params.start_date}"  type="text" name="start_date">  ~  <input class="datetime" value="${params.end_date}"  type="text" name="end_date"> 
          <br/>
          会员账号：
          <input  value="${params.email}"  type="text" name="email">
          <button type="submit" class="btn btn-primary">查询</button> <br/>
        </g:form>
      </div>


      <div class="bs-docs-example">
        <table class="table table-bordered">
          <thead>
            <tr>
              <th>交易流水</th>
              <th>会员账号</th>
              <th>充值金额</th>
              <th>充值时间</th>
            </tr>
          </thead>
          <tbody>
            
          <g:each in="${list}" status="i" var="alipayRemit">
            <tr>
              <td>${alipayRemit.remitSN}</td>
              <td>${alipayRemit.user.email}</td>
              <td>${alipayRemit.amount}</td>
              <td>${alipayRemit.dateCreated.toString()[0..18]}</td>
              
              
            </tr>
          </g:each>
            
            
            
          </tbody>
        </table>
      </div>
      <div class="row-fluid">
        <div class="span12">
          <div  class="pagination pagination-right new-pagination">
          


            <g:if test="${total != 0}">
                <g:paginate  params="${params}" action="list" total="${total}" />
           </g:if>
            
            <span class="currentStep">共${total}条数据</span>
          </div>
        
        </div>
      </div>
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
<g:render template="/layouts/footer"/>
    </div> <!-- /container -->
    
  </body>
</html>
