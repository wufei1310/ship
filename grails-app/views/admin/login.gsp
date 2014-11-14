<!DOCTYPE html>
<html>
  <head>
    <title>金士代发</title>
  <r:require modules="application"/>
  <r:require modules="jquery"/>
  <r:require modules="bootstrap"/>
  <r:require modules="parsley"/>
  <r:layoutResources />
  <r:layoutResources />
  <style type="text/css">
    body {
      padding-top: 40px;
      padding-bottom: 40px;
      background-color: #f5f5f5;
    }

    .form-signin {
      max-width: 400px;
      padding: 19px 29px 29px;
      margin: 0 auto 20px;
      background-color: #fff;
      border: 1px solid #e5e5e5;
      -webkit-border-radius: 5px;
      -moz-border-radius: 5px;
      border-radius: 5px;
      -webkit-box-shadow: 0 1px 2px rgba(0,0,0,.05);
      -moz-box-shadow: 0 1px 2px rgba(0,0,0,.05);
      box-shadow: 0 1px 2px rgba(0,0,0,.05);
    }
    .form-signin .form-signin-heading,
    .form-signin .checkbox {
      margin-bottom: 10px;
    }
    .form-signin input[type="text"],
    .form-signin input[type="password"] {
      font-size: 16px;
      height: auto;
      margin-bottom: 15px;
      padding: 7px 9px;
    }

  </style>
</head>
<body>



  <div class="container body" style="margin-top: 50px;">

    <g:form class="form-signin" url="[controller: 'adminUser', action:'doLogin']" data-validate="parsley"  method="post">
      <h2 class="form-signin-heading">金士代发管理后台</h2>
      <input name="email" type="text"  data-required-message="管理员账号不能为空" data-required="true"  class="input-block-level" placeholder="输入管理员账号">
      <input data-required-message="密码不能为空" data-required="true" name="password" type="password" class="input-block-level" placeholder="输入密码">
      <input name="loginValidCode" type="text" class="input-block-level span2" placeholder="输入验证码">
      <jcaptcha:jpeg name="imageCaptcha"  style="width: 120px;margin-bottom: 20px;" />
      <a href="javascript:void(0)" onclick="changeLoginCode()">看不清?</a>
      <button class="btn btn-large btn-primary" type="submit">登录</button>
    </g:form>
    <input type="hidden" name="sessionFail" id="sessionFail" value="${params.sessionFail}">
  </div> <!-- /container -->
  <script>
    function changeLoginCode(){
    document.getElementById("imageCaptcha").src="<%=request.getContextPath()%>/jcaptcha/jpeg/imageCaptcha?id="+ new Date();
  }
  </script>
</body>
</html>
<g:if test="${flash.message}">
         <script>
         alert('${flash.message}')
        </script>
      </g:if>