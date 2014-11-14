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
        <h4>修改角色</h4>
      </div>
        
   <g:form  name="updateRoleForm" id="${params.id}"   data-validate="parsley" action="doUpdate" target="innerFrame"  method="post" class="form-horizontal  form-inline updateRoleForm">

  <fieldset>
      <div class="control-group">
          <label>角色名称</label>
          <input value="${role.roleName}"  type="text" data-required-message="角色名称不能为空" data-required="true" name="roleName">
          
        </div>
        <div class="control-group">
          <label>类型</label>
          <select name="type">
            <option value="admin" <g:if test="${role.type=='admin'}">selected</g:if> >管理员</option>
            <option value="daifa" <g:if test="${role.type=='daifa'}">selected</g:if> >代发人员</option>
            <option value="member" <g:if test="${role.type=='member'}">selected</g:if> >会员</option>
            <option value="wuliu" <g:if test="${role.type=='wuliu'}">selected</g:if> >物流人员</option>
          </select>
        </div>
      <div class="control-group">
          <label>简介</label>
          <textarea name="roleDesc" style="width: 400px;height: 200px;" >${role.roleDesc}</textarea>
          
        </div>
    
    
    
    <button type="button" class="btn btn-large btn-primary" onclick="toActionFormCom('updateRoleForm')" >提交</button>
    <g:link  action="list"  params="${params}" class=" btn btn-large">返回</g:link>
  </fieldset>
</g:form>
       <iframe id="innerFrame" name="innerFrame" height="0" frameborder="0"></iframe>
         <g:form  action="${actionName}" params="${params}"  method="post" class="commonListForm">
        </g:form >
  </div>
 </body>
</html>