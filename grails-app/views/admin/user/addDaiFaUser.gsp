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
        <h3>添加用户</h3>
      </div>
        
        <g:form  name="addDaiFaUserForm"  data-validate="parsley" url="[controller: 'adminUser', action:'doAddDaiFaUser']"   method="post" class="addDaiFaUserForm form-horizontal" target="innerFrame">


  <fieldset>
      <div class="control-group">
          <label>账号</label>
          <input  data-required-message="账号不能为空"  data-required="true" type="text" name="email" >
        </div>
      <div class="control-group">
          <label>密码</label>
          <input  value="" type="password" name="password" id="password" placeholder="" data-required-message="密码不能为空"  data-required="true">
          
        </div>
     <div class="control-group">
          <label>重复输入密码</label>
          <input  value="" type="password" name="password_again"  placeholder="" data-required-message="密码不能为空" data-equalto="#password" data-equalto-message="两次密码输入的不一致" data-required="true">
          
      </div>
        
    <div class="control-group">
          <label>手机号码</label>
           <input  data-required-message="手机号码不能为空"  data-required="true" type="text" name="phone" >
        </div>
    <div class="control-group">
          <label>类型</label>
          <select name="role">
            <option value="admin">管理员</option>
            <option value="daifa">代发人员</option>
            <option value="wuliu">物流人员</option>
              <option value="kf">客服</option>
          </select>
      </div>
    <div class="control-group">
          <label>角色</label>
          <g:select    name="role_key" optionKey="id" optionValue="roleName" from="${role}" noSelection="['':'请选择']" data-required-message="角色不能为空" data-required="true" />
      </div>
    <div class="control-group">
          <label>市场</label>
          <g:each in="${marketList}" status="i" var="market">
            <input type="checkbox" name="marketList" value="${market}"/>${market}<br/>
          </g:each>
      </div>
    <button type="button" onclick="toActionFormCom('addDaiFaUserForm')" class="btn btn-large btn-primary">提交</button>
  </fieldset>
</g:form>
<iframe id="innerFrame" name="innerFrame" height="0" frameborder="0"></iframe>
         <g:form  action="toAddDaiFaUser" params="${params}"  method="post" class="commonListForm">
        </g:form >
<g:render template="/layouts/footer"/>
      </div> <!-- /container -->
  </body>
</html>
