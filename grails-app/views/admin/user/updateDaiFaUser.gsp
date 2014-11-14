<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="adminMain"/>
    <title>金士代发</title>
  </head>
  <body>
    <div class="container body">
      <g:if test="${flash.message}">
         <script>
         alert('${flash.message}')
        </script>
      </g:if>

      <div class="page-header">
        <h3>修改用户</h3>
      </div>
        
        <g:form  name="addDaiFaUserForm"  data-validate="parsley" url="[controller: 'adminUser', action:'doUpdateDaiFaUser']"   method="post" class="updateDaiFaUserForm form-horizontal" target="innerFrame">
          <input type="hidden" name="id" value="${user.id}">

  <fieldset>
      <div class="control-group">
          <label>账号</label>
          ${user.email}
        </div>
      <div class="control-group">
          <label>角色</label>
          <g:select value="${user.role_id.id}" class="input-medium"  name="role_key" optionKey="id" optionValue="roleName" from="${role}" noSelection="['':'请选择']" data-required-message="角色不能为空" data-required="true" />
      </div>
    
      <div class="control-group">
          <label>市场</label>
          <g:each in="${marketList}" status="i" var="market">
            <input type="checkbox" name="marketList" <g:if test="${user_market.contains(market)}">checked</g:if>  value="${market}"/>${market}<br/>
          </g:each>
      </div>
    
    
    <button type="button" onclick="toActionFormCom('updateDaiFaUserForm')" class="btn btn-large btn-primary">提交</button>
    <g:link  action="list"  params="${params}" class=" btn btn-large">返回</g:link>
  </fieldset>
</g:form>
       <iframe id="innerFrame" name="innerFrame" height="0" frameborder="0"></iframe>
         <g:form  action="${actionName}" params="${params}"  method="post" class="commonListForm">
        </g:form >
<g:render template="/layouts/footer"/>
      </div> <!-- /container -->
  </body>
</html>
