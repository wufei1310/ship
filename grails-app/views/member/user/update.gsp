<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
    <title>金士代发</title>
  </head>
  <body>
    <div class="container body">
      

      <div class="page-header">
        <h3>${session.loginPOJO.user.email}个人资料</h3>
      </div>
        
        <g:form  name="updateUserInfoForm"  data-validate="parsley" url="[controller: 'memberUser', action:'doUpdateUserInfo']"   method="post" class="form-horizontal">
<g:if test="${flash.message}">
        <div class="alert alert-error">
          <button type="button" class="close" onclick="$(this).parents('.alert').hide()" data-dismiss="alert">&times;</button>
          <strong>${flash.message}</strong>
        </div>
      </g:if>

  <fieldset>
      <div class="control-group">
          <label>寄件人，或店名，显示在快递单上</label>
          <input value="${user.name}"  type="text" name="name" placeholder="">
          
        </div>
      <div class="control-group">
          <label>联系电话</label>
          <input  value="${user.phone}" type="text" name="phone" placeholder="">
          
        </div>
    
    <div class="control-group">
          <label>QQ(请认真填写，缺货没货会通知此QQ)</label>
          <input  value="${user.qq}" type="text" name="qq" placeholder="">
          
        </div>
    
    <button type="submit" class="btn btn-large btn-primary">提交</button>
  </fieldset>
</g:form>

<g:render template="/layouts/footer"/>
      </div> <!-- /container -->
  </body>
</html>
