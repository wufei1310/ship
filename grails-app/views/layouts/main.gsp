<%@ page import="org.springframework.core.io.support.PropertiesLoaderUtils; org.springframework.core.io.ClassPathResource" %>
<%
def properties = PropertiesLoaderUtils.loadProperties(new ClassPathResource("sysSetting.properties"))
%>
<!DOCTYPE html>
<!--[if lt IE 7 ]> <html lang="en" class="no-js ie6"> <![endif]-->
<!--[if IE 7 ]>    <html lang="en" class="no-js ie7"> <![endif]-->
<!--[if IE 8 ]>    <html lang="en" class="no-js ie8"> <![endif]-->
<!--[if IE 9 ]>    <html lang="en" class="no-js ie9"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--> <html lang="en" class="no-js"><!--<![endif]-->
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
        <title>${grailsApplication.config.seo.title}</title>
        <meta name="description" content="${grailsApplication.config.seo.description}"/>
        <meta name="keywords" content="${grailsApplication.config.seo.keywords}"/>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="shortcut icon" href="<g:resource dir='images' file='kingsdf.ico' />" >
        <g:layoutHead/>

    <r:require modules="application"/>
    <r:require modules="jquery"/>
    <r:require modules="bootstrap"/>
    <r:require modules="parsley"/>
    <r:require modules="fancyBox"/>
    <r:require modules="layer"/>

    <r:layoutResources/>
    <g:javascript src="respond.src.js"/>

    <style>

        /* GLOBAL STYLES
        -------------------------------------------------- */
        /* Padding below the footer and lighter body text */



        <g:if test="${actionName=='index'}">
            body {
            padding-bottom: 40px;
            color: #5a5a5a;
            }
        </g:if>
        <g:else>
            body {
            margin-top:80px;
            padding-bottom: 40px;
            color: #5a5a5a;
            }
        </g:else>



        /* CUSTOMIZE THE NAVBAR
        -------------------------------------------------- */

        /* Special class on .container surrounding .navbar, used for positioning it into place. */
        .navbar-wrapper {
        position: absolute;
        top: 0;
        left: 0;
        right: 0;
        z-index: 10;
        margin-top: 20px;
        margin-bottom: -90px; /* Negative margin to pull up carousel. 90px is roughly margins and height of navbar. */
        }
        .navbar-wrapper .navbar {

        }

        /* Remove border and change up box shadow for more contrast */
        .navbar .navbar-inner {
        border: 0;
        -webkit-box-shadow: 0 2px 10px rgba(0,0,0,.25);
        -moz-box-shadow: 0 2px 10px rgba(0,0,0,.25);
        box-shadow: 0 2px 10px rgba(0,0,0,.25);
        }

        /* Downsize the brand/project name a bit */
        .navbar .brand {
        padding: 14px 20px 16px; /* Increase vertical padding to match navbar links */
        font-size: 16px;
        font-weight: bold;
        text-shadow: 0 -1px 0 rgba(0,0,0,.5);
        }

        /* Navbar links: increase padding for taller navbar */
        .navbar .nav > li > a {
        padding: 15px 20px;
        }

        /* Offset the responsive button for proper vertical alignment */
        .navbar .btn-navbar {
        margin-top: 10px;
        }



        /* CUSTOMIZE THE CAROUSEL
        -------------------------------------------------- */

        /* Carousel base class */
        .carousel {
        margin-bottom: 60px;
        }

        .carousel .container {
        position: relative;
        z-index: 9;
        }

        .carousel-control {
        height: 80px;
        margin-top: 0;
        font-size: 120px;
        text-shadow: 0 1px 1px rgba(0,0,0,.4);
        background-color: transparent;
        border: 0;
        z-index: 10;
        }

        .carousel .item {
        height: 500px;
        }
        .carousel img {
        position: absolute;
        top: 0;
        left: 0;
        min-width: 100%;
        height: 500px;
        }

        .carousel-caption {
        background-color: transparent;
        position: static;
        max-width: 550px;
        padding: 0 20px;
        margin-top: 200px;
        }
        .carousel-caption h1,
        .carousel-caption .lead {
        margin: 0;
        line-height: 1.25;
        color: #fff;
        text-shadow: 0 1px 1px rgba(0,0,0,.4);
        }
        .carousel-caption .btn {
        margin-top: 10px;
        }



        /* MARKETING CONTENT
        -------------------------------------------------- */

        /* Center align the text within the three columns below the carousel */
        .marketing .span4 {
        text-align: center;
        }
        .marketing h2 {
        font-weight: normal;
        }
        .marketing .span4 p {
        margin-left: 10px;
        margin-right: 10px;
        }


        /* Featurettes
        ------------------------- */

        .featurette-divider {
        margin: 10px 0; /* Space out the Bootstrap <hr> more */
        }
        .featurette {
        padding-top: 0px; /* Vertically center images part 1: add padding above and below text. */
        overflow: hidden; /* Vertically center images part 2: clear their floats. */
        }
        .featurette-image {
        margin-top: -120px; /* Vertically center images part 3: negative margin up the image the same amount of the padding to center it. */
        }

        /* Give some space on the sides of the floated elements so text doesn't run right into it. */
        .featurette-image.pull-left {
        margin-right: 40px;
        }
        .featurette-image.pull-right {
        margin-left: 40px;
        }

        /* Thin out the marketing headings */
        .featurette-heading {
        font-size: 50px;
        font-weight: 300;
        line-height: 1;
        letter-spacing: -1px;
        }



        /* RESPONSIVE CSS
        -------------------------------------------------- */

        @media (max-width: 979px) {

        .container.navbar-wrapper {
        margin-bottom: 0;
        width: auto;
        }
        .navbar-inner {
        border-radius: 0;
        margin: -20px 0;
        }

        .carousel .item {
        height: 500px;
        }
        .carousel img {
        width: auto;
        height: 500px;
        }

        .featurette {
        height: auto;
        padding: 0;
        }
        .featurette-image.pull-left,
        .featurette-image.pull-right {
        display: block;
        float: none;
        max-width: 40%;
        margin: 0 auto 20px;
        }
        }


        @media (max-width: 767px) {

        .navbar-inner {
        margin: -20px;
        }

        .carousel {
        margin-left: -20px;
        margin-right: -20px;
        }
        .carousel .container {

        }
        .carousel .item {
        height: 300px;
        }
        .carousel img {
        height: 300px;
        }
        .carousel-caption {
        width: 65%;
        padding: 0 70px;
        margin-top: 100px;
        }
        .carousel-caption h1 {
        font-size: 30px;
        }
        .carousel-caption .lead,
        .carousel-caption .btn {
        font-size: 18px;
        }

        .marketing .span4 + .span4 {
        margin-top: 40px;
        }

        .featurette-heading {
        font-size: 30px;
        }
        .featurette .lead {
        font-size: 18px;
        line-height: 1.5;
        }

        }
    </style>
</head>
<body >
    <div class="navbar-wrapper" >
        <!-- Wrap the .navbar in .container to center it within the absolutely positioned parent. -->
        <div class="container">

            <div class="navbar navbar">
                <div class="navbar-inner">
                  <!-- Responsive Navbar Part 1: Button for triggering responsive navbar (not covered in tutorial). Include responsive CSS to utilize. -->
                    <button type="button" class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </button>
                    <g:link class="brand"  controller="index" action="index">
                        <g:img uri="/images/logo.PNG" style="width:80px;" />
                    </g:link>
                <!-- Responsive Navbar Part 2: Place all navbar contents you want collapsed withing .navbar-collapse.collapse. -->
                    <div class="nav-collapse collapse">
                        <ul class="nav">


                            <g:if test="${session?.loginPOJO?.user}">



                                <li class="dropdown">
                                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">我的订单<b class="caret"></b></a>
                                    <ul class="dropdown-menu">

                                        <li><g:link controller="memberDaiFaOrder" action="list">我的代发订单</g:link></li>
                                        <li><g:link controller="memberDaiFaOrder" action="saleReturnList">我的退货</g:link></li>
                                        </ul>
                                    </li>

                                <li <g:if test="${actionName == 'list' && controllerName == 'memberNewProduct'}">class="active"</g:if>><g:link controller="memberNewProduct" action="list">热门批发</g:link></li>


                                </g:if>
                                <g:else>
                                <li <g:if test="${actionName == 'index' && controllerName == 'index'}">class="active"</g:if>><g:link controller="index" action="index">首页</g:link></li>


                                </g:else>


                            <li class="dropdown">
                                <a href="#" class="dropdown-toggle" data-toggle="dropdown">创建新订单<b class="caret"></b></a>
                                <ul class="dropdown-menu">
                                    <li><g:link controller="memberDaiFaOrder" action="add">创建新订单</g:link></li>

                                    <li><g:link controller="memberDaiFaOrder" action="list">我要退货</g:link></li>

                                    <li><g:link controller="memberImportGoods" action="toGoodsImport">批量上传商品</g:link></li>
                                    </ul>
                                </li>


                            <li class="dropdown">
                                <a href="#" class="dropdown-toggle" data-toggle="dropdown">支付与安全<b class="caret"></b></a>
                                <ul class="dropdown-menu">
                                    <li <g:if test="${actionName == 'list' && controllerName == 'memberAlipayRemit'}">class="active"</g:if>><g:link
                                            controller="memberAlipayRemit" action="list">支付宝充值</g:link></li>
                                    <li <g:if test="${actionName == 'list' && controllerName == 'memberRemit'}">class="active"</g:if>><g:link
                                            controller="memberRemit" action="list">汇款充值</g:link></li>
                                        <li class="divider"></li>

                                    <li <g:if test="${actionName == 'toSetSafepass'}">class="active"</g:if>><g:link
                                            controller="memberUser" action="toSetSafepass">设置安全支付密码</g:link></li>
                                    <li <g:if test="${actionName == 'list' && controllerName == 'memberTranLog'}">class="active"</g:if>><g:link
                                            controller="memberTranLog" action="list">订单支付流水</g:link></li>
                                    </ul>
                                </li>


                            <li class="dropdown">
                                <a href="#" class="dropdown-toggle" data-toggle="dropdown">运费与服务<b class="caret"></b></a>
                                <ul class="dropdown-menu">
                                    <li <g:if test="${actionName == 'liucheng'}">class="active"</g:if>><g:link controller="index"
                                            action="liucheng">产品&服务</g:link></li>
                                    <li <g:if test="${actionName == 'policy'}">class="active"</g:if>><g:link controller="index"
                                            action="policy">代发协议</g:link></li>
                                    <li <g:if test="${actionName == 'gywm'}">class="active"</g:if>><g:link controller="index"
                                            action="gywm">关于我们</g:link></li>
                                    <li <g:if test="${actionName == 'ship'}">class="active"</g:if>><g:link controller="index"
                                            action="ship">最新运费</g:link></li>
                                    </ul>
                                </li>
                                <li class="dropdown">
                                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">快递跟踪<b class="caret"></b></a>
                                    <ul class="dropdown-menu">
                                        <li><a href="http://www.zto.cn/" target="_blank">中通</a></li>
                                        <li><a href="http://www.ttkdex.com/ttkdweb/index.html" target="_blank">天天</a></li>

                                    <li><a href="http://www.sf-express.com/cn/sc/" target="_blank">顺丰</a></li>
                                    <li><a href="http://www.yto.net.cn/cn/index/index.html" target="_blank">圆通</a></li>
                                    <li><a href="http://www.sto.cn/" target="_blank">申通</a></li>
                                    <li><a href="http://www.yundaex.com/" target="_blank">韵达</a></li>
                                    <li><a href="http://www.ems.com.cn/?COLLCC=306421325&" target="_blank">EMS</a></li>

                                </ul>
                            </li>

                            <li class="dropdown">
                                <a href="#" class="dropdown-toggle" data-toggle="dropdown">帮助<b class="caret"></b></a>
                                <ul class="dropdown-menu">
                                    <li><g:link controller="Help" action="h1" target="_blank">1.注册账号及微信绑定</g:link></li>
                                    <li><g:link controller="Help" action="h2" target="_blank">2.设置安全支付密码</g:link></li>
                                    <li><g:link controller="Help" action="h3" target="_blank">3.代发服务项目</g:link></li>
                                    <li><g:link controller="Help" action="h4" target="_blank">4.下单注意事项</g:link></li>
                                    <li><g:link controller="Help" action="h5" target="_blank">5.下单流程</g:link></li>
                                    <li><g:link controller="Help" action="h6" target="_blank">6.支付宝直接支付、充值手续费</g:link></li>
                                    <li><g:link controller="Help" action="h7" target="_blank">7.充值与查询</g:link></li>
                                    <li><g:link controller="Help" action="h8" target="_blank">8.账户余额查询</g:link></li>
                                    <li><g:link controller="Help" action="h9" target="_blank">9.支付未付款订单</g:link></li>
                                    <li><g:link controller="Help" action="h10" target="_blank">10.查看订单状态</g:link></li>
                                    <li><g:link controller="Help" action="h11" target="_blank">11.客户质疑缺货状态</g:link></li>
                                    <li><g:link controller="Help" action="h12" target="_blank">12.客户质疑货号、档口不对</g:link></li>
                                    <li><g:link controller="Help" action="h13" target="_blank">13.缺货换档口</g:link></li>
                                    <li><g:link controller="Help" action="h14" target="_blank">14.商品补款,补运费</g:link></li>
                                    <li><g:link controller="Help" action="h15" target="_blank">15.快递单号及快件跟踪</g:link></li>
                                    <li><g:link controller="Help" action="h16" target="_blank">16.换货流程</g:link></li>
                                    <li><g:link controller="Help" action="h17" target="_blank">17.退货流程</g:link></li>
                                    <li><g:link controller="Help" action="h18" target="_blank">18.查看订单收支流水</g:link></li>

                                </ul>
                            </li>


                            <g:if test="${session?.loginPOJO?.user}">
                                <li class="dropdown pull-right">
                                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">欢迎您，${session.loginPOJO.user.email}<b class="caret"></b></a>
                                    <ul class="dropdown-menu">
                                        <li><a href="javascript:void(0);"> 余额：<span id="yueDivHtml"></span></a></li>
                                        <li><g:link
                                                controller="memberRemit" action="toAdd">线下汇款充值<font style="font-size: 0.4em">（手续费:0%）</font></g:link></li>
                                        <li><a href="<%=request.getContextPath()%>/memberAlipayRemit/toAdd">支付宝充值<font style="font-size: 0.4em">（手续费:1.2%）</font></a></li>








                                        <li><g:link controller="login" action="logout"  >退出</g:link></li>

                                    </ul>
                                </li>
                            </g:if>

                        </ul>


                    </div><!--/.nav-collapse -->
                    <g:if test="${session?.loginPOJO?.user}">


                    </g:if><g:else>
                        <a  class="btn btn-success pull-right" href="#registerModal" data-toggle="modal">免费注册</a>
                        <a  style="margin-right: 5px;" class="btn  btn-primary pull-right" href="#loginModal" data-toggle="modal">快速登录</a>
                    </g:else>
                </div><!-- /.navbar-inner -->
            </div><!-- /.navbar -->

        </div> <!-- /.container -->
    </div><!-- /.navbar-wrapper -->



    <div id="loginModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
    aria-hidden="true" style="display: none;">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>

        <h3 id="myModalLabel">登录金士代发</h3>
    </div>

    <div class="modal-body">
        <g:if test="${flash.message}">
            <div class="alert alert-error">
                <button type="button" class="close" data-dismiss="alert">&times;</button>
                <strong>${flash.message}</strong>
            </div>
        </g:if>
        <g:form id="loginForm" name="loginForm" data-validate="parsley" url="[controller: 'login', action: 'doLogin']"
            method="post" class="form-horizontal">

            <div class="control-group">
                <label class="control-label" for="inputEmail">邮箱</label>

                <div class="controls">
                    <input data-type-email-message="邮箱格式不正确" data-type="email" data-required-message="邮箱不能为空"
                    data-required="true" type="text" name="email" placeholder="">
                </div>
            </div>

            <div class="control-group">
                <label class="control-label" for="inputPassword">密码</label>

                <div class="controls">
                    <input data-error-message="密码不能为空" data-required="true" type="password" name="loginPassword"
                    placeholder="">
                    <g:link controller="index" action="toLoginpassEmail">忘记密码？</g:link>
                    </div>
                </div>

            <div class="control-group">
                <label class="control-label" for="inputPassword">验证码</label>

                <div class="controls">
                    <input data-error-message="验证码不能为空" data-required="true" type="text" name="loginValidCode"
                    id="loginValidCode" placeholder="" class="input-small">
                    <span id="validCodeImg">
                        <jcaptcha:jpeg name="imageCaptcha" style="width: 120px;"/>
                    </span>
                    <a href="javascript:void(0)" onclick="changeLoginCode()">看不清?</a>
                </div>
            </div>

            <div class="control-group">
                <div class="controls">
                    <button type="submit" class="btn btn-primary btn-large">登录</button>
                    <button type="button" onclick="reqRegister()" class="btn btn-danger btn-large">没有账号，去注册</button>

                </div>
            </div>
        </g:form>
    </div>
    <!--    <div class="modal-footer">
          <button class="btn" data-dismiss="modal">关闭</button>
          <button class="btn btn-primary">Save changes</button>
        </div>-->
</div>

<div id="registerModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
aria-hidden="true" style="display: none;">
<div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>

    <h3 id="myModalLabel">注册金士代发，让您的代发不再困难</h3>
</div>

<div class="modal-body">

    <g:if test="${flash.message}">
        <div class="alert alert-error">
            <button type="button" class="close" data-dismiss="alert">&times;</button>
            <strong>${flash.message}</strong>
        </div>
    </g:if>

    <g:form id="registerForm" name="registerForm" data-validate="parsley"
        url="[controller: 'register', action: 'doRegister']" method="post" class="form-horizontal">

        <div class="control-group">
            <label class="control-label" for="inputEmail">QQ邮箱</label>

            <div class="controls">
                <input class="input-medium" data-required-message="QQ邮箱不能为空" data-required="true" type="text"
                name="email" placeholder="只需要在此输入QQ号码" onblur="checkQQEmail(this)"><span style="font-size: 18px;">@qq.com</span>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label" for="inputPassword">密码</label>

            <div class="controls">
                <input data-minlength-message="密码至少需要6位数字" data-minlength="6" data-required-message="密码不能为空"
                data-required="true" type="password" id="password" name="password" placeholder="">
            </div>
        </div>

        <div class="control-group">
            <label class="control-label" for="inputPassword">确认密码</label>

            <div class="controls">
                <input data-equalto-message="两次密码输入的不一致" data-required-message="密码不能为空" data-equalto="#password"
                data-required="true" type="password" name="repassword" placeholder="">
            </div>
        </div>

        <div class="control-group">
            <label class="control-label" for="inputPassword">验证码</label>

            <div class="controls">
                <input data-error-message="验证码不能为空" data-required="true" type="text" name="registerImageCaptcha"
                placeholder="" class="input-small">
                <span id="registerValidCode">
                    <jcaptcha:jpeg name="registerImageCaptcha" style="width: 120px;"/>
                </span>


                <a href="javascript:void(0)" onclick="changeReigsterCode()">看不清?</a>
            </div>
        </div>

        <div class="control-group">
            <div class="controls">
                <button type="button" class="btn btn-primary btn-large" onclick="doRegister()">提交</button>
                <button type="button" onclick="reqLogin()" class="btn btn-success btn-large">已有账号，去登录</button>

            </div>
        </div>

    </g:form>
    <div class="form-horizontal control-group">
        <div class="controls">
            <input type="checkbox" id="is_agree"/>我已阅读并同意<g:link controller="index" target="_blank"
                action="policy">《金士代发服务须知》</g:link>条款
            </div>
        </div>
    </div>
    <!--    <div class="modal-footer">
          <button class="btn" data-dismiss="modal">关闭</button>
          <button class="btn btn-primary">Save changes</button>
        </div>-->
</div>
</body>

<g:layoutBody/>
<r:layoutResources/>


<script>

    function checkQQEmail(obj){
    var val = obj.value
    val = val.toLowerCase();
    if(val.endWith("@qq.com")){
    obj.value = val.substring(0,val.indexOf("@"))
    }
    }

    function doRegister() {
    if (!$("#is_agree").is(":checked")) {
    alert("请先阅读《金士代发服务须知》条款")
    return false;
    } else {
    $("#registerForm").submit()
    }
    }

    function reqLogin() {
    $('#registerModal').modal('hide')
    $('#loginModal').modal('show')
    }

    function reqRegister() {
    $('#loginModal').modal('hide')
    $('#registerModal').modal('show')
    }


    function changeLoginCode() {


    document.getElementById("imageCaptcha").src = "<%=request.getContextPath()%>/jcaptcha/jpeg/imageCaptcha?id=" + new Date();
    }

    function changeReigsterCode() {


    document.getElementById("registerImageCaptcha").src = "<%=request.getContextPath()%>/jcaptcha/jpeg/imageCaptcha?id=" + new Date();
    }
    <g:if test="${session.loginPOJO?.user && session.loginPOJO.user.user_type=='member'}">
        $.ajax(
        { url: "<%=request.getContextPath()%>/memberUser/selAccount",
        cache: false,
        success: function (msg) {
        $("#yueDivHtml").html(msg)
        }
        });
    </g:if>
</script>
<g:if test="${params.reqAction == 'reqLogin'}">
    <script>
        $('#loginModal').modal('show')
    </script>

</g:if>

<g:if test="${params.reqAction == 'reqRegister'}">
    <script>
        $('#registerModal').modal('show')
    </script>

</g:if>

</body>
</html>
