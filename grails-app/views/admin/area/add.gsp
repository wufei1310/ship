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
        <h3>添加地区</h3>
      </div>
        
<g:form  name="addForm"  data-validate="parsley" url="[controller: 'adminArea', action:'doAdd']"   method="post" class="form-horizontal  form-inline addForm" target="innerFrame" >
     

  <fieldset>
      <div class="control-group">
          <label>地区名称</label>
          <input value=""  type="text" data-required-message="地区名称不能为空" data-required="true" name="name" placeholder="地区名称">
          
        </div>
      <div class="control-group">
          <label>父地区</label>
          ${p_name}
        </div>
    <input type="hidden" name="pId" value="${params.pId}">
    <input type="hidden" name="level" value="${params.level}">
      <div class="control-group">
          <label>排序</label>
          <input value=""  type="text" data-required-message="排序不能为空" data-required="true" data-type-number-message="排序格式不正确"  data-type="number"   name="sortNo" placeholder="排序">
           (填写整数，数字越大排名越靠前)
      </div>
     
    
    
    <button type="button" class="btn btn-large btn-primary" onclick="toActionFormCom('addForm')">提交</button>
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
