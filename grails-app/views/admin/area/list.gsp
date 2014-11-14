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
        <h3>地区列表
          <g:link params="${params}" action="toAdd" class="btn btn-primary  pull-right">创建地区</g:link>
        </h3>
      </div>

      <div class="well well-large">
        <form class="form-inline">
          地区名称：
          <input name="name" type="text" class="" placeholder="" value="${params.name}">
          <input type="hidden" name="pId" value="${params.pId }">
	  <input type="hidden" name="level" value="${params.level }">
          <button type="submit" class="btn btn-primary">查询</button> 
        </form>
      </div>
      <g:link  action="list" params="[pId:0,level:0]" >全部</g:link>
      <g:each in="${topList}" status="i" var="area">
           《<g:link  action="list" params="[pId:area.id,level:Integer.parseInt(area.level)+1]" >${area.name}</g:link>
       </g:each>
      <div class="bs-docs-example">
        
        <table class="table table-bordered">
          <thead>
            <tr>
              <th>地区名称</th>
              <th>排序</th>
              <th>操作</th>
            </tr>
          </thead>
          <tbody>
            
          <g:each in="${list}" status="i" var="area">
            <tr>
              <td><g:if test="${area.level!="3"}"><g:link  action="list"  params="[pId:area.id,level:Integer.parseInt(area.level)+1]">${area.name}</g:link></g:if>
                  <g:else>${area.name}</g:else>
              </td>
              <td>${area.sortNo}</td>
              <td>
                <g:link  action="toUpdate"  params="${params}" id="${area.id}" class="btn">修改</g:link>
                <a class="btn" href="javascript:void(0)" onclick="toActionCom({id:'${area.id}'},'<%=request.getContextPath()%>/adminArea/doDelete','确认删除此地区吗？')">删除</a>
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
                <g:paginate total="${total}"  params="${params}" action="list"  update="page" />
           </g:if>
            
            <span class="currentStep">共${total}条数据</span>
          </div>
      </div>
    </div>
         <form     method="post" id="commonActionForm" target="innerFrame">
           <input type="hidden" name="id" id="id" class="key">
        </form>
        <iframe id="innerFrame" name="innerFrame" height="0" frameborder="0"></iframe>
         <g:form  action="${actionName}" params="${params}"  method="post" class="commonListForm">
        </g:form >       
      <g:render template="/layouts/footer"/>
    </div> <!-- /container -->
  </body>
</html>

