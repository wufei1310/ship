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
        <h3>修改菜单</h3>
      </div>
        
   <g:form  name="updateForm" id="${params.id}"  data-validate="parsley" action="doUpdate"   method="post" class="form-horizontal  form-inline updateForm" target="innerFrame" >
     

  <fieldset>
      <div class="control-group">
          <label>菜单名称</label>
          <input value="${menu.menuName}"  type="text" data-required-message="菜单名称不能为空" data-required="true" name="menuName" placeholder="菜单名称">
          
        </div>
      <div class="control-group">
          <label>父菜单</label>
          ${p_name}
        </div>
      <div class="control-group">
          <label>菜单路径</label>
          <input value="${menu.menuPath}"  type="text"  name="menuPath" placeholder="菜单路径">
        </div>
      <div class="control-group">
          <label>菜单排序</label>
          <input value="${menu.sortNo}"  type="text" data-required-message="菜单排序不能为空" data-required="true" data-type-number-message="菜单排序格式不正确"  data-type="number"   name="sortNo" placeholder="菜单排序">
           (填写整数，数字越大排名越靠前)
      </div>
      <div class="control-group">
          <label>简介</label>
          <textarea name="menuDesc" style="width: 400px;height: 200px;" >${menu.menuDesc}</textarea>
          
      </div>
      <div class="control-group">
          <label>状态</label>
          <input type="radio" name="status" value="1" <g:if test="${menu.status=='1'}">checked</g:if>>有效
          <input type="radio" name="status" value="0" <g:if test="${menu.status=='0'}">checked</g:if>>无效
      </div>
    
    
    <button type="button" class="btn btn-large btn-primary" onclick="toActionFormCom('updateForm')">提交</button>
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
   
