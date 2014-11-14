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
        <h4>分配菜单权限-${role.roleName}</h4>
      </div>
        
   <g:form  name="updateForm"  data-validate="parsley" action="doAuth" id="${role.id}"  method="post" class="form-horizontal  form-inline updateForm" target="innerFrame" >
     

  <fieldset>
    <g:each in="${menuList}"  var="menu">
      <div class="control-group">
          <label><input <g:if test="${checkedMenu.contains(menu.id)}">checked</g:if> class="parent"  onclick="parentCheck(this)" type="checkbox"  name="menu_id" value="${menu.id }" />&nbsp;${menu.menuName}</label>
          <g:each in="${menu.childrenMenu}"  var="childrenMenu">
            <input <g:if test="${checkedMenu.contains(childrenMenu.id)}">checked</g:if> class="child"  onclick="childCheck(this)" type="checkbox"  name="menu_id" value="${childrenMenu.id }" />&nbsp;${childrenMenu.menuName}&nbsp;&nbsp;
          </g:each>
        </div>
      <hr/>
    </g:each>
    
    
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
      <script>
          function parentCheck(obj){
		if($(obj).attr("checked")){
			$(obj).parents(".control-group").find("input").attr("checked",true);
		}else{
			$(obj).parents(".control-group").find("input").attr("checked",false);
		}
	}
      function childCheck(obj){
		if($(obj).attr("checked")){
			$(obj).parents(".control-group").find("input.parent").attr("checked",true);
		}else{
			var boo = false;
			$(obj).parents(".control-group").find("input.child").each(function(){
				if($(this).attr("checked")){
					boo = true;
					return;
				}
			});
			$(obj).parents(".control-group").find("input.parent").attr("checked",boo);
		}
	}
      </script>
 </div>
 </body>
</html>  