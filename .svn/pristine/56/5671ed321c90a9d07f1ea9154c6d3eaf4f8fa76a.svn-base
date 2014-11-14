<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="adminMain"/>
    <title>金士代发</title>
  </head>
  <body>


  
    <div class="container body" id="page">
      <div class="page-header">
        <h3>快递列表
        </h3>
      </div>
      
      
      <div class="bs-docs-example">
        
        <table class="table table-bordered">
          <thead>
            <tr>
              <th>快递公司</th>
              
            </tr>
          </thead>
          <tbody>
            
          <g:each in="${list}" status="i" var="express">
            <tr>
              <td>${express.name}</td>
              
              <td>
                <g:link  action="shipList"  params="${params}" id="${express.id}" class="btn">设置运费</g:link>
               
              </td>
            </tr>
          </g:each>
            
            
            
          </tbody>
        </table>
      </div>

         <form     method="post" id="commonActionForm" target="innerFrame">
           <input type="hidden" name="id" id="id" class="key">
        </form>
        <iframe id="innerFrame" name="innerFrame" height="0" frameborder="0"></iframe>
         <g:form  action="${actionName}" params="${params}"  method="post" class="commonListForm">
        </g:form >       
      <g:render template="/layouts/footer"/>
    </div> <!-- /container -->
  </body>
</html>

