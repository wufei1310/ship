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
        <h3>修改支付密码</h3>
      </div>
        
        <g:form  name="updateUserInfoForm"  data-validate="parsley" url="[controller: 'memberUser', action:'doSetSafepassAgain']"   method="post" class="form-horizontal">
     

  <fieldset>
      <div class="control-group">
          <label>原支付密码</label>
          <input value=""  type="password" name="safepass" id="safepass" placeholder="" data-required-message="原支付密码不能为空" data-required="true">
          <g:link  action="toSafepassEmail" target="_blank" >找回密码</g:link>
        </div>
      <div class="control-group">
          <label>新支付密码</label>
          <input  value="" type="password" name="new_safepass" id="new_safepass" placeholder="" data-required-message="新支付密码不能为空" data-required="true">
          
        </div>
    
    <div class="control-group">
          <label>重复输入新支付密码</label>
          <input  value="" type="password" name="safepass_again" id="safepass_again" placeholder="" data-equalto="#new_safepass" data-equalto-message="两次密码输入的不一致" data-required-message="请重复输入新支付密码" data-required="true">
          
        </div>
    
    <button type="submit" class="btn btn-large btn-primary">提交</button>
  </fieldset>
</g:form>

<g:render template="/layouts/footer"/>
      </div> <!-- /container -->
  </body>
</html>
