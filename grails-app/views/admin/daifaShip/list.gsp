<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="adminMain"/>
    <r:require modules="bootstrapDatetimepicker"/>
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
        <h3>物流单打印
        </h3>
      </div>

      
      <div class="bs-docs-example">
        
        <table class="table table-bordered">
          <thead>
            <tr>
              <th>快递公司</th>
                <th>已导出</th>
                <th>未导出</th>
            </tr>
          </thead>
          <tbody>
            
          <g:each in="${list}" status="i" var="express">
              <tr>
                <td>${express.name}</td>
              <td>${express.y_explore}&nbsp;&nbsp;&nbsp;<g:link controller="adminOrderShip" action="explore" params="[name:express.name,is_explore:'1']" >再次导出</g:link></td>
              <td>${express.n_explore}&nbsp;&nbsp;&nbsp;<g:link controller="adminOrderShip" action="explore" params="[name:express.name,is_explore:'0']" >导出</g:link></td>

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

