<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
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
        <h3>找回支付密码</h3>
      </div>
        
        <g:form  name="doSafepassEmailForm"  data-validate="parsley" url="[controller: 'memberUser', action:'doSafepassEmail']"   method="post" class="form-horizontal">
     

  <fieldset>
      <div class="control-group">
          <label>提交注册时的电子邮箱</label>
           ${session.loginPOJO.user.email}
        </div>
  
    <button type="submit" class="btn btn-large btn-primary">发送找回密码邮件</button>
  </fieldset>
</g:form>

<g:render template="/layouts/footer"/>
      </div> <!-- /container -->
  </body>
</html>
