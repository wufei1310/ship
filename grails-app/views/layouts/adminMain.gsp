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
    <g:layoutHead/>
    <r:require modules="application"/>
    <r:require modules="jquery"/>
    <r:require modules="bootstrap"/>
    <r:require modules="parsley"/>
    <r:require modules="layer"/>


    <r:layoutResources/>
    <g:javascript src="respond.src.js"/>
</head>

<body>
<div class="navbar  navbar-fixed-top">
    <div class="navbar-inner">
        <div class="container">
            <button type="button" class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <g:link class="brand" controller="adminIndex" action="index">金士代发管理后台</g:link>
            <div class="nav-collapse collapse">
                <ul class="nav">
                    <g:if test="${session.loginPOJO.user.email == 'superquan'}">
                        <g:each in="${session.loginPOJO.menuList}" status="i" var="menu">

                            <g:if test="${menu.menuPath}">
                                <li>
                                    <a href="<%=request.getContextPath()%>/${menu.menuPath}">${menu.menuName}<b
                                            class="caret"></b></a>
                                </li>
                            </g:if><g:else>

                            <li class="dropdown">
                                <a href="<%=request.getContextPath()%>/${menu.menuPath}" class="dropdown-toggle"
                                   data-toggle="dropdown">${menu.menuName}<b class="caret"></b></a>
                                <ul class="dropdown-menu">
                                    <g:each in="${menu.childrenMenu}" var="childrenMenu">
                                        <li><a href="<%=request.getContextPath()%>/${childrenMenu.menuPath}">${childrenMenu.menuName}</a>
                                        </li>
                                    </g:each>
                                </ul>
                            </li>
                        </g:else>
                        </g:each>
                    </g:if>
                    <g:if test="${session.loginPOJO.user.role == 'wuliu'}">
                        <li>
                            <g:link controller="adminOrderShip" action="list">物流单打印</g:link>
                        </li>
                    </g:if>
                </ul>



                <ul class="nav pull-right">

                    <g:if test="${session?.loginPOJO?.user}">
                        <li class="active"><a>欢迎您，${session.loginPOJO.user.email}!</a></li>
                        <li class="active">
                            <g:link controller="login" action="logout">退出</g:link>
                        </li>
                    </g:if>
                    <g:else>
                        <li class="active"><a href="#loginModal" data-toggle="modal">登录</a></li>
                        <li class="active"><a href="#reigsterModal" data-toggle="modal">注册</a></li>
                    </g:else>

                </ul>

            </div><!--/.nav-collapse -->
        </div>
    </div>
</div>



<g:layoutBody/>
<r:layoutResources/>
</body>
</html>
