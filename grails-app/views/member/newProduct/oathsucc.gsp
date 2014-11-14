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
      max-width: 500px;
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

    <g:form  class="memberNewProductForm" controller="memberNewProduct" name="memberNewProductForm"  action="list" ></g:form>



  <div class="container body" style="margin-top: 50px;">

    <g:form class="form-signin" >
      <h3 class="form-signin-heading">金士代发一键上传应用授权成功！</h3>
      <well>我们为您精选了当前最热门的商品，提供最便捷的上货方式，助您生意兴隆！</well>
        <br/>
        <br/>
        <br/>
      <button class="btn btn-large btn-primary" type="button" onclick="$('.memberNewProductForm').submit();">去上传商品吧</button>
    </g:form>
    <input type="hidden" name="sessionFail" id="sessionFail" value="${params.sessionFail}">
  </div> <!-- /container -->
  <script>
  </script>
</body>
</html>
