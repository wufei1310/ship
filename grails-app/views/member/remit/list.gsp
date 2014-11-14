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
          汇款充值列表
          <g:link  action="toAdd" class="btn btn-primary btn-large pull-right">汇款充值</g:link>
      </h3>
        
        </div>



      <div class="bs-docs-example">
        <table class="table table-bordered">
          <thead>
            <tr>
              <th>汇款人姓名</th>
              <th>汇出款银行账号</th>
              <th>汇出款银行名称</th>
              <th>汇出款单号</th>
              <th>汇出款金额</th>
              <th>汇出款时间</th>
              <th>状态</th>
            </tr>
          </thead>
          <tbody>
            
          <g:each in="${list}" status="i" var="remit">
            <tr>
              <td>${remit.user_name}</td>
              <td>${remit.bank_account}</td>
              <td>${remit.bank_name}</td>
              <td>${remit.bank_order}</td>
              <td>${remit.bank_amount}</td>
              <td>${remit.remit_date.toString()[0..10]}</td>
              <td><g:if test="${remit.status == '0'}">
                      处理中
                  </g:if>
                  <g:if test="${remit.status == '1'}">
                      审核通过
                  </g:if>
                  <g:if test="${remit.status == '2'}">
                    审核不通过 <a title="${remit.reason}" href="javascript:void(0)">查看原因</a>
                  </g:if>
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
                <g:paginate  params="${params}" action="list" total="${total}" />
           </g:if>
            
            <span class="currentStep">共${total}条数据</span>
          </div>
        
        </div>
      </div>
      
<g:render template="/layouts/footer"/>
    </div> <!-- /container -->
    
  </body>
</html>
