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
        <h3>用户列表
        </h3>
      </div>
      <div class="well well-large">
        <form class="form-inline">
            ID:
            <input name="userid" type="text" class="" placeholder="" value="${params.userid}">
          用户名/email：
          <input name="email" type="text" class="" placeholder="" value="${params.email}">
          用户类型：
          <g:select class="input-medium" value="${params.role}" name="role" optionKey="v" optionValue="queryShow" from="${[[v:"",queryShow:"全部"],[v:"admin",queryShow:"管理员"],[v:"member",queryShow:"会员"],[v:"daifa",queryShow:"代发人员"],[v:"wuliu",queryShow:"物流人员"]]}" />

          
          <button type="submit" class="btn btn-primary">查询</button>
        </form>
      </div>
      
      <div class="bs-docs-example">
        
        <table class="table table-bordered">
          <thead>
            <tr>
                <th>ID</th>
              <th>用户名/email</th>
              <th>用户类型</th>
              <th>姓名</th>
              <th>余额</th>
              <th>注册时间</th>
              <th>联系电话</th>
              <th>QQ</th>
              <th>店铺url</th>
              <th>状态</th>
              <th>操作</th>
            </tr>
          </thead>
          <tbody>
            
          <g:each in="${list}" status="i" var="user">
            <tr>
                <td>
                    <g:link target="_blank" controller="adminTranLog" action="list" params="[userid:user.id]">${user.id}</g:link>
                    </td>
              <td>${user.email}</td>
              <td>
                <g:if test="${user.role=='admin'}">管理员</g:if>
                <g:if test="${user.role=='member'}">会员</g:if>
                <g:if test="${user.role=='daifa'}">代发人员</g:if>
                <g:if test="${user.role=='wuliu'}">物流人员</g:if>
              </td>
              <td>${user.name}</td>
              <th>${user.account.amount}</th>
              <th>${user.dateCreated.toString()[0..10]}</th>
              <td>${user.phone}</td>
              <td>${user.qq}</td>
              <td>${user.store_url}
              </td>
              <td><g:if test="${user.status == '1'}">
                 冻结
              </g:if><g:else>
                正常
              </g:else>

                &nbsp;
              <g:if test="${user.isCanPrint == '1'}">
                  可以打印
              </g:if><g:else>
                  不能打印　
              </g:else>

              </td>
              <td>


                  <g:if test="${user.isCanPrint == '1'}">

                      <a class="btn" href="javascript:void(0)"  onclick=" toActionCom({id:'${user.id}'},'<%=request.getContextPath()%>/adminUser/isCanPrint?isCanPrint=0','取消打印权限？')">取消打印</a>
                  </g:if><g:else>
                  <a class="btn" href="javascript:void(0)"  onclick=" toActionCom({id:'${user.id}'},'<%=request.getContextPath()%>/adminUser/isCanPrint?isCanPrint=1','授权该用户可以打印？')">授权打印</a>
              </g:else>


                <g:if test="${user.status == '1'}">

                  <a class="btn" href="javascript:void(0)"  onclick=" toActionCom({id:'${user.id}'},'<%=request.getContextPath()%>/adminUser/noDongjie','确定取消该会员的冻结状态吗？')">取消冻结</a>
                </g:if><g:else>
                  <a class="btn" href="javascript:void(0)"  onclick=" toActionCom({id:'${user.id}'},'<%=request.getContextPath()%>/adminUser/dongjie','确定冻结该会员吗？')">冻结</a>
                </g:else>
                <g:if test="${user.role != 'member'}">
                  <g:link  action="toUpdateDaiFaUser"  params="${params}" id="${user.id}" class="btn btn-primary">分配角色市场</g:link>
                </g:if>
                  <g:link controller="adminUser"  action="cancleBind"  params="[email1:user.email]"  class="btn">取消绑定</g:link>

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
                  <input type='hidden' name='id' id='id' class='key'>
        </form>
        <iframe id="innerFrame" name="innerFrame" height="0" frameborder="0"></iframe>
         <g:form  action="${actionName}" params="${params}"  method="post" class="commonListForm">
        </g:form >    
      <g:render template="/layouts/footer"/>
    </div> <!-- /container -->
  </body>
</html>

