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
        <h3>设置支付密码</h3>
      </div>
        
        <g:form  name="updateUserInfoForm"  data-validate="parsley" url="[controller: 'memberUser', action:'doSetSafepass']"   method="post" class="form-horizontal">
     

  <fieldset>
      <div class="control-group">
          <label>登录密码</label>
          <input value=""  type="password" name="password" id="password" placeholder="" data-required-message="登录密码不能为空" data-required="true">
          
        </div>
      <div class="control-group">
          <label>支付密码</label>
          <input  value="" type="password" name="safepass" id="safepass" placeholder="" data-required-message="支付密码不能为空" data-required="true">
          
        </div>
    
    <div class="control-group">
          <label>重复输入支付密码</label>
          <input  value="" type="password" name="safepass_again" id="safepass_again" placeholder="" data-equalto="#safepass" data-equalto-message="两次密码输入的不一致" data-required-message="请重复输入支付密码" data-required="true">
          
        </div>
    
    <button type="submit" class="btn btn-large btn-primary">提交</button>
  </fieldset>
</g:form>

<g:render template="/layouts/footer"/>
      </div> <!-- /container -->
  </body>
</html>
