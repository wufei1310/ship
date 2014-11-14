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
        <h3>找回登录密码</h3>
      </div>
        
        <g:form  name="doSafepassEmailForm"  data-validate="parsley" url="[controller: 'index', action:'doLoginpassEmail']"   method="post" class="form-horizontal">
     

  <fieldset>
      <div class="control-group">
          <label>提交注册时的电子邮箱</label>
           <input value=""  type="text" name="email"  data-type-email-message="邮箱格式不正确"  data-type="email" data-required-message="邮箱不能为空" data-required="true"   placeholder="邮箱">
        </div>
        <div class="control-group">
          <label>验证码</label>  
          <input data-error-message="验证码不能为空" data-required="true" type="text" name="emailValidCode" id="emailValidCode" placeholder="" class="input-small">
            <span id="validCodeImg">
              <jcaptcha:jpeg name="emailImageCaptcha"  style="width: 120px;" />
            </span>
            <a href="javascript:void(0)" onclick="changeEmailCode()">看不清?</a>
          </div>
    <button type="submit" class="btn btn-large btn-primary">发送找回密码邮件</button>
  </fieldset>
</g:form>
      <script>
          function changeEmailCode(){
      
            document.getElementById("emailImageCaptcha").src="<%=request.getContextPath()%>/jcaptcha/jpeg/emailImageCaptcha?id="+ new Date();
          }
        </script>
<g:render template="/layouts/footer"/>
      </div> <!-- /container -->
  </body>
</html>
