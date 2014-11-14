<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="layout" content="main"/>
    <title>${grailsApplication.config.seo.title}</title>
    <meta name="description" content="${grailsApplication.config.seo.description}"/>
    <meta name="keywords" content="${grailsApplication.config.seo.keywords}"/>
    <link rel="shortcut icon" href="<g:resource dir='images' file='kingsdf.ico'/>">
    <r:require modules="application"/>
    <r:require modules="jquery"/>
    <r:require modules="bootstrap"/>
    <r:require modules="parsley"/>
    <r:require modules="fancyBox"/>
    <r:require modules="layer"/>

    <r:layoutResources/>
    <g:javascript src="respond.src.js"/>
    <r:layoutResources/>

    <!-- Le styles -->

</head>

<body>

<!-- Carousel
        ================================================== -->
<div id="myCarousel" class="carousel slide">
    <div class="carousel-inner">
        <div class="item active">
            <g:img uri="/images/slide-01.jpg"/>
            <div class="container">
                <div class="carousel-caption">
                    %{--<h1>Example headline.</h1>--}%
                    <p class="lead">Kingsdf.cn(金士代发)是广州最专业的代发服务团队，活跃于广州各大型服装批发市场，帮淘宝店、天猫、拍拍等c2c和b2c卖家到服装批发档口直接取货，并立即以卖家的名义发货到指定的买家。</p>
                    <a class="btn btn-large btn-primary" href="#registerModal" data-toggle="modal">去注册</a>
                </div>
            </div>
        </div>

        <div class="item">
            <g:img uri="/images/slide-02.jpg"/>
            <div class="container">
                <div class="carousel-caption">
                    <h1>我们的服务</h1>

                    <p class="lead">1.为你轻松的在产地拿一手货源，且不用压货！</p>

                    <p class="lead">2.提供发货前质量检验服务，解除您的后顾之忧！</p>

                    <p class="lead">3.为您提供个性包装、促销礼品、换吊牌、服务，彰显您的网店实力！，让您网店生意更好做！</p>
                    <g:link controller="index" class="btn btn-large btn-primary"
                            action="liucheng">产品&服务</g:link>
                </div>
            </div>
        </div>
        <!--  <div class="item">
          <g:img uri="/images/slide-03.jpg"/>
          <div class="container">
            <div class="carousel-caption">
              <h1>One more for good measure.</h1>
              <p class="lead">Cras justo odio, dapibus ac facilisis in, egestas eget quam. Donec id elit non mi porta gravida at eget metus. Nullam id dolor id nibh ultricies vehicula ut id elit.</p>
              <a class="btn btn-large btn-primary" href="#">Browse gallery</a>
            </div>
          </div>
        </div>-->
    </div>
    <a class="left carousel-control" href="#myCarousel" data-slide="prev">&lsaquo;</a>
    <a class="right carousel-control" href="#myCarousel" data-slide="next">&rsaquo;</a>
</div><!-- /.carousel -->



<!-- Marketing messaging and featurettes
        ================================================== -->
<!-- Wrap the rest of the page in another container to center all the content. -->

<div class="container marketing">
    <!-- Three columns of text below the carousel -->
    <div class="row">
        <div class="span4">
            <!--          <img class="img-circle" data-src="holder.js/140x140">-->
            <h2>1元代发</h2>

            <p class="lead" style="align:right;">
                1元钱能买什么？只需1元您就可以享受取货、质量检验、发货等强大后勤服务。
                详见<g:link controller="index" action="liucheng">《产品&服务》</g:link>
            </p>
        </div><!-- /.span4 -->
        <div class="span4">
            <!--          <img class="img-circle" data-src="holder.js/140x140">-->
            <h2>高效快捷</h2>

            <p class="lead">
                企业化管理，负责的态度，令我们能高速且不紊的奔跑于各大服装货源中心，为您提供可靠的服务！
            </p>
        </div><!-- /.span4 -->
        <div class="span4">
            <!--          <img class="img-circle" data-src="holder.js/140x140">-->
            <h2>退换货服务</h2>

            <p class="lead">当您需要为您的买家提供退换货服务时，我们可以为您效劳！<br/>详见<a href="<%=request.getContextPath()%>/index/policy#huanhuo" >《退换货规则》</a></p>
        </div><!-- /.span4 -->
    </div><!-- /.row -->





    <hr class="featurette-divider">



    <div class="row">


        <div class="span4" style="border-right: 1px solid #eeeeee;">
            <h4 style="color: #0088cc;margin-left:20px; text-align: right;padding-right: 50px;">热门店铺</h4>
            <ul class="unstyled" style="font-size: 1.0em;">
                <g:each in="${stalls}">
                    <li  style="margin: 5px;text-align: right;padding-right: 30px;">${it[1]}-${it[2]}-${it[3]}</li>
                </g:each>
            </ul>


            %{--<h4 style="color: #0088cc;">实时订单动态</h4>--}%
            %{--<div class="ranklist" style="height: 400px;--}%
            %{--overflow: hidden;">--}%
                %{--<ul  style="font-size: 1.0em;" class="unstyled">--}%
                    %{--<g:each in="${daifaGoods}">--}%
                        %{--<li style="margin: 5px;">--}%
                            %{--${it.market}-${it.floor}-${it.stalls}-${it.goods_sn}&nbsp;--}%
                            %{--<g:if test="${it.status=='1'}">已受理</g:if>--}%
                            %{--<g:if test="${it.status=='2'}">已拿货</g:if>--}%
                            %{--<g:if test="${it.status=='7'}">已验收</g:if>--}%
                            %{--<g:if test="${it.status=='9'}">已发货</g:if>--}%
                        %{--</li>--}%
                    %{--</g:each>--}%

                %{--</ul>--}%
            %{--</div>--}%

        </div>

        <div class="span7">
            <div class="featurette">
                %{--<g:img class="featurette-image pull-right" uri="/images/browser-icon-chrome.png"/>--}%
                <h4 style="color: #0088cc;">代发服务区域</h4>

                <p class="lead">机筑巷、国大、大时代、宝华、女人街、大西豪、西街、富丽、金马、新潮都、非凡、柏美、佰润、益民</p>

                <h4 style="color: #0088cc;">支持的快递公司</h4>

                <p class="lead">中通、申通、圆通、顺丰、天天、韵达、EMS</p>

                <h4 style="color: #0088cc;">拿货发货时间</h4>

                <p class="lead" width="100px;">
                    11:00-14:00第一次取货发货
                    <br/>
                    15:00-17:00第二次取货发货
                    <br/>
                    特别说明：16:00后的订单第二天取

                </p>
                <h4 style="color: #0088cc;">快递单号公布时间</h4>
                <p class="lead"> 14:00 - 22:00完成质量检查后发货取得快递单号</p>
            </div>
            <div class="alert alert-info">
                <h4>金士代发，助您生意越来越兴隆</h4>
            </div>
        </div>
        %{--<div class="span3 well" style="border: 0px;border-radius: 0px;box-shadow: inset 0 0px 0px rgba(0, 0, 0, 0.05);--}%
        %{--} ">--}%
            %{--<h4>热门店铺</h4>--}%
            %{--<ol style="font-size: 1.0em;">--}%
                %{--<g:each in="${stalls}">--}%
                    %{--<li style="margin: 5px;">${it[1]}-${it[2]}-${it[3]}</li>--}%
                %{--</g:each>--}%
            %{--</ol>--}%
        %{--</div>--}%
        %{--<div class="span3 well"  style="border: 0px;border-radius: 0px;box-shadow: inset 0 0px 0px rgba(0, 0, 0, 0.05);--}%
        %{--} ">--}%
            %{--<h4>实时订单动态</h4>--}%
            %{--<div class="ranklist" style="height: 400px;--}%
            %{--overflow: hidden;">--}%
            %{--<ul  style="font-size: 1.0em;" class="unstyled">--}%
                %{--<g:each in="${daifaGoods}">--}%
                    %{--<li style="margin: 5px;">--}%
                        %{--${it.market}-${it.floor}-${it.stalls}-${it.goods_sn}&nbsp;--}%
                        %{--<g:if test="${it.status=='1'}">已受理</g:if>--}%
                        %{--<g:if test="${it.status=='2'}">已拿货</g:if>--}%
                        %{--<g:if test="${it.status=='7'}">已验收</g:if>--}%
                        %{--<g:if test="${it.status=='9'}">已发货</g:if>--}%
                    %{--</li>--}%
                %{--</g:each>--}%

            %{--</ul>--}%
            %{--</div>--}%
        %{--</div>--}%
    </div>


    <!-- FOOTER -->
    <g:render template="/layouts/footer"/>

</div><!-- /.container -->



<!-- Le javascript
        ================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script>
    !function ($) {
        $(function () {
// carousel demo
            $('#myCarousel').carousel()
        })
    }(window.jQuery)




</script>


<script type="text/javascript">
    (function($){

        $.fn.myScroll = function(options){
            //默认配置
            var defaults = {
                speed:40,  //滚动速度,值越大速度越慢
                rowHeight:24 //每行的高度
            };

            var opts = $.extend({}, defaults, options),intId = [];

            function marquee(obj, step){

                obj.find("ul").animate({
                    marginTop: '-=1'
                },0,function(){
                    var s = Math.abs(parseInt($(this).css("margin-top")));
                    if(s >= step){
                        $(this).find("li").slice(0, 1).appendTo($(this));
                        $(this).css("margin-top", 0);
                    }
                });
            }

            this.each(function(i){
                var sh = opts["rowHeight"],speed = opts["speed"],_this = $(this);
                intId[i] = setInterval(function(){
//                    if(_this.find("ul").height()<=_this.height()){
//                        clearInterval(intId[i]);
//                    }else{
                        marquee(_this, sh);
//                    }
                }, speed);


            });

        }

    })(jQuery);

    $(function(){

        $(".ranklist").myScroll({
            speed:100,
            rowHeight:20
        });

    });
</script>


</body>
</html>
