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
          支付宝充值列表
          <g:link  action="toAdd" class="btn btn-primary btn-large pull-right">支付宝充值</g:link>
      </h3>
        
        </div>



      <div class="bs-docs-example">
        <table class="table table-bordered">
          <thead>
            <tr>
              <th>充值金额</th>
              <th>充值时间</th>
            </tr>
          </thead>
          <tbody>
            
          <g:each in="${list}" status="i" var="alipayRemit">
            <tr>
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
      
<g:render template="/layouts/footer"/>
    </div> <!-- /container -->
    
  </body>
</html>
