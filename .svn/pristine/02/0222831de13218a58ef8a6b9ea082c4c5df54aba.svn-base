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
        <h3>菜单列表
          <g:link params="${params}" action="toAdd" class="btn btn-primary  pull-right">创建菜单</g:link>
        </h3>
      </div>
      <div class="well well-small">
        <form class="form-inline">
          菜单名称：
          <input name="menuName" type="text" class="" placeholder="" value="${params.menuName}">
          
          <button type="submit" class="btn btn-primary">查询</button> 
          <g:if test="${params.pId!="0"}"><g:link  action="list"  class="btn">返回上级菜单</g:link></g:if>
        </form>
      </div>
      
      <div class="bs-docs-example" >
        
        <table class="table table-bordered">
          <thead>
            <tr>
              <th>菜单名称</th>
              <th>菜单路径</th>
              <th>简介</th>
              <th>状态</th>
              <th>操作</th>
            </tr>
          </thead>
          <tbody>
            
          <g:each in="${list}" status="i" var="menu">
            <tr>
              <td><g:if test="${params.pId=="0"}"><g:link  action="list"  params="[pId:menu.id]">${menu.menuName}</g:link></g:if>
                  <g:else>${menu.menuName}</g:else>
              </td>
              <td>${menu.menuPath}</td>
              <td>${menu.menuDesc}</td>
              <td><g:if test="${menu.status=="1"}">有效</g:if>
                  <g:else>无效</g:else></td>
              <td>
                <g:link controller="adminMenu" action="toUpdate"  params="${params}" id="${menu.id}" class="btn">修改</g:link>
                <a class="btn" href="javascript:void(0)" onclick="toActionCom({id:'${menu.id}'},'<%=request.getContextPath()%>/adminMenu/doDelete','确认删除此菜单吗？')">删除</a>
              </td>
            </tr>
          </g:each>
            
            
            
          </tbody>
        </table>
      </div>
<div class="row-fluid">
      <div class="span12">
      <div  class="pagination pagination-right new-pagination" >
          


            <g:if test="${total != 0}">
               
                <g:paginate  total="${total}"  params="${params}" action="list" />
           </g:if>
            
            <span class="currentStep">共${total}条数据</span>
          </div>
      </div>
    </div>
   
        <form     method="post" id="commonActionForm" target="innerFrame">
                  <input type='hidden' name='id' id='id' class='key'>
        </form>
        <iframe id="innerFrame" name="innerFrame" height="0" frameborder="0"></iframe>
         <g:form  action="${actionName}" params="${params}"  method="post" class="commonListForm">
        </g:form >
    </div>
  </body>
</html>