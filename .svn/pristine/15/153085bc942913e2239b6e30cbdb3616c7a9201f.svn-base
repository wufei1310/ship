
<r:require modules="bootstrapDatetimepicker"/>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="adminMain"/>
    <title>金士代发</title>
  </head>
  <body>


    <div class="container body" id="page">
      <div class="page-header">
        <h3>导出代发采购报表
        </h3>
      </div>
      <div class="well well-large">
        <g:form class="form-inline" action="orderExport" >
          时间：
          <input class="datetime" value="${params.start_date}"  type="text" name="start_date">  ~  <input class="datetime" value="${params.end_date}"  type="text" name="end_date"> 
          <button type="submit" class="btn btn-primary">导出</button>
        </g:form>
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
    </div>

  </body>
</html>

