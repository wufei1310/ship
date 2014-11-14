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
        
        <form   class="form-horizontal">
     

  <fieldset>
      <div class="control-group">
          重置登录密码邮件即将发送到：${email}，请稍等片刻。<br/>
          温馨提示：收件箱未收到邮件时，请查看垃圾箱的邮件。
        </div>
  
  </fieldset>
</form>

<g:render template="/layouts/footer"/>
      </div> <!-- /container -->
  </body>
</html>
