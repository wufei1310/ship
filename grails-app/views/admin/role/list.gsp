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
  <h3>角色列表
    <g:link params="${params}" action="toAdd" class="btn btn-primary pull-right">创建新角色</g:link>
  </h3>
</div>
<div class="well well-small">
  <form class="form-inline">
    角色名称：
    <input name="roleName" type="text" class="" placeholder="" value="${params.roleName}">

    <button type="submit" class="btn btn-primary">查询</button>
  </form>
</div>

<div class="bs-docs-example" >

  <table class="table table-bordered">
    <thead>
      <tr>
        <th>角色名称</th>
        <th>类型</th>
        <th>简介</th>
        
        <th>操作</th>
      </tr>
    </thead>
    <tbody>

    <g:each in="${list}" status="i" var="role">
      <tr>
        <td>${role.roleName}</td>
        <td><g:if test="${role.type == "admin"}">管理员</g:if>
            <g:if test="${role.type == "member"}">会员</g:if>
            <g:if test="${role.type == "daifa"}">代发人员</g:if>
            <g:if test="${role.type == "wuliu"}">物流人员</g:if>
        </td>
        <td>${role.roleDesc}</td>
        <td>
      <g:link controller="adminRole" action="toUpdate"  params="${params}" id="${role.id}" class="btn">修改</g:link>
      <g:link controller="adminRole" action="toAuth"  params="${params}" id="${role.id}" class="btn">分配权限</g:link>
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

        <g:paginate  params="${params}" action="list" total="${total}" />
      </g:if>

      <span class="currentStep">共${total}条数据</span>
    </div>
  </div>
</div>
  </div>
 </body>
</html>