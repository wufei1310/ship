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
        <h4>添加新角色</h4>
      </div>
        
   <g:form  name="addRoleForm"  data-validate="parsley" url="[controller: 'adminRole', action:'doAdd']" target="innerFrame"   method="post" class="form-horizontal  form-inline addRoleForm" >
     

  <fieldset>
      <div class="control-group">
          <label>角色名称</label>
          <input value=""  type="text" data-required-message="角色名称不能为空" data-required="true" name="roleName" placeholder="角色名称">
          
        </div>
     <div class="control-group">
          <label>类型</label>
          <select name="type">
            <option value="admin">管理员</option>
            <option value="daifa">代发人员</option>
            <option value="member">会员</option>
            <option value="wuliu">物流人员</option>
          </select>
        </div>
      <div class="control-group">
          <label>简介</label>
          <textarea name="roleDesc" style="width: 400px;height: 200px;" ></textarea>
          
        </div>
    
    
    
    <button type="button" class="btn btn-large btn-primary" onclick="toActionFormCom('addRoleForm')">提交</button>
    <g:link  action="list"  params="${params}" class=" btn btn-large">返回</g:link>
  </fieldset>
</g:form>
<!--        <form     method="post" id="commonActionForm" target="innerFrame">
                  <input type='hidden' name='id' id='id' class='key'>
                  <input type='hidden' name='wuliu_no' id='wuliu_no' class='key'>
        </form>-->
        <iframe id="innerFrame" name="innerFrame" height="0" frameborder="0"></iframe>
         <g:form  action="${actionName}" params="${params}"  method="post" class="commonListForm">
        </g:form >
 </div>
 </body>
</html>  