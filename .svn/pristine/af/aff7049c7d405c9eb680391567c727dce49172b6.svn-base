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
        <h3>（${express.name}）地区运费列表
        </h3>
      </div>
      
      <g:form class="doUpdateForm" action="doUpdate" method="post" data-validate="parsley" target="innerFrame" >
      <div class="bs-docs-example">
        
        <table class="table table-bordered">
          <thead>
            <tr>
              <th>省份</th>
              <th>首重费用（元/kg）</th>
              <th>续重费用（元/kg）</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td>全国</td>
              
              <td><input value="${!shipMap.get('0100')?'':shipMap.get('0100').f_price}" onkeyup="if(isNaN(value))execCommand('undo')" onafterpaste="if(isNaN(value))execCommand('undo')"  name="f_price" class="input-small" type="text" placeholder="" data-error-message="全国首重费用不能为空" data-required="true"></td>
              <td><input value="${!shipMap.get('0100')?'':shipMap.get('0100').x_price}" onkeyup="if(isNaN(value))execCommand('undo')" onafterpaste="if(isNaN(value))execCommand('undo')"  name="x_price" class="input-small" type="text" placeholder="" data-error-message="全国续重费用不能为空" data-required="true"></td>
                  <input type="hidden" name="area_id" value="0100">
            </tr>
          <g:each in="${areaList}" status="i" var="area">
            <tr>
              <td>${area.name}</td>
              <td><input value="${!shipMap.get(area.id)?'':shipMap.get(area.id).f_price}" onkeyup="if(isNaN(value))execCommand('undo')" onafterpaste="if(isNaN(value))execCommand('undo')"  name="f_price" class="input-small" type="text" placeholder=""></td>
              <td><input value="${!shipMap.get(area.id)?'':shipMap.get(area.id).x_price}" onkeyup="if(isNaN(value))execCommand('undo')" onafterpaste="if(isNaN(value))execCommand('undo')"  name="x_price" class="input-small" type="text" placeholder=""></td>
                  <input type="hidden" name="area_id" value="${area.id}">
            </tr>
          </g:each>
            
            
            
          </tbody>
        </table>
      </div>
        <input type="hidden" name="express_id" value="${express.id}">
        <button type="button" class="btn btn-large btn-primary" onclick="toActionFormCom('doUpdateForm')">提交</button>
        <g:link  action="expressList"  params="${params}" class=" btn btn-large">返回</g:link>
      </g:form>
          <iframe id="innerFrame" name="innerFrame" height="0" frameborder="0"></iframe>
         <g:form  action="${actionName}" params="${params}"  method="post" class="commonListForm">
        </g:form >    
      <g:render template="/layouts/footer"/>
    </div> <!-- /container -->
  </body>
</html>

