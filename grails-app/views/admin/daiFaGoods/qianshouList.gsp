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
        <h3>付款给代发员
        </h3>
      </div>
      <div class="well well-large">
        <g:form class="form-inline" action="qianshouList" >
          订单号：
          <input name="orderSN" type="text"  placeholder="" value="${params.orderSN}"  >

           受理人：
           <input name="email" type="text"  placeholder="" value="${params.email}" class="input-small">
           
           状态：
           <select name="is_qianshou" class="input-small">
             <option value=""  >全部</option>
             <option value="0" <g:if test="${params.is_qianshou == '0'}">selected</g:if> >未申请提现</option>
             <option value="1" <g:if test="${params.is_qianshou == '1'}">selected</g:if> >已申请提现</option>
             <option value="2" <g:if test="${params.is_qianshou == '2'}">selected</g:if> >已提现</option>
           </select>
           
           时间：
           <input class="datetime input-small" value="${params.start_date}"  type="text" name="start_date" >  ~  <input class="datetime input-small" value="${params.end_date}"  type="text" name="end_date" > 
          
          <button type="submit" class="btn btn-primary">查询</button> &nbsp;&nbsp;&nbsp;&nbsp;<g:link  action="explorQianshou"   class="btn btn-primary">导出全部未签收报表</g:link>
        </g:form>
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
              <th>拿货单价</th>
              <th>受理人</th>
              <th>状态</th>
              <th>验收时间</th>
              <th>签收人</th>
              <th>操作</th>
            </tr>
          </thead>
          <tbody>
            
          <g:each in="${list}" status="i" var="daiFaGoods">
            <tr>
              <td>${daiFaGoods.daiFaOrder.orderSN}</td>
              <td>${daiFaGoods.market}</td>
              <td>${daiFaGoods.goods_sn}</td>
              <td>${daiFaGoods.floor}-${daiFaGoods.stalls}</td>
              <td>${daiFaGoods.spec}</td>
              <td>${daiFaGoods.num}</td>
              <td>${daiFaGoods.actual_price}</td>
              <td title="受理时间：${daiFaGoods.processtime?daiFaGoods.processtime.toString()[0..18]:''}"><a href="javascript:void(0)">${daiFaGoods.daifa_user?daiFaGoods.daifa_user.email:''}</a></td>
              <td><g:if test="${daiFaGoods.is_qianshou == '2'}">已提现</g:if>
                  <g:if test="${daiFaGoods.is_qianshou == '1'}">已申请提现</g:if>
                  <g:else>未申请提现</g:else>
              </td>
              
              <td>${daiFaGoods.checktime?daiFaGoods.checktime.toString()[0..18]:''}</td>
              <td title="签收时间：${daiFaGoods.qianshoutime?daiFaGoods.qianshoutime.toString()[0..18]:''}"><a href="javascript:void(0)">${daiFaGoods.qianshou_user?daiFaGoods.qianshou_user.email:''}</a></td>
              <td>
<!--              <g:if test="${daiFaGoods.is_qianshou != '1'}">
                 <a class="btn btn-primary" href="javascript:void(0)" onclick="toActionCom({id:'${daiFaGoods.id}'},'<%=request.getContextPath()%>/adminDaiFaGoods/doQianshou','确认签收吗？')">签收</a>
              </g:if>-->
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
                <g:paginate total="${total}"  params="${params}" action="qianshouList"  update="page" />
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

