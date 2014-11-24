<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="layout" content="main"/>
    <title>${grailsApplication.config.seo.title}</title>
    <meta name="description" content="${grailsApplication.config.seo.description}"/>
    <meta name="keywords" content="${grailsApplication.config.seo.keywords}"/>
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
            <h2>退货服务</h2>

            <p class="lead">当您需要为您的买家提供退货服务时，我们可以为您效劳！</p>
        </div><!-- /.span4 -->
    </div><!-- /.row -->



<!-- START THE FEATURETTES -->

    <hr>

    <div class="row">
        <div class="span7" >

            <h3>代发服务区域</h3>

            <p class="lead">机筑巷、国大、大时代、宝华、女人街、大西豪、西街、富丽、金马、新潮都、非凡、柏美、佰润、益民</p>

            <h3>支持的快递公司</h3>

            <p class="lead">中通、申通、圆通、顺丰、天天、韵达、EMS</p>

            <h3>拿货发货时间</h3>

            <p class="lead" width="100px;">
                11-14点拿货一次 处理11点之前的订单;
                <br/>
                15-17点拿货一次 处理11-16:30之间的订单
                <br/>
                特别说明：16:30前订单当天发出，之后的订单第二天发出</p>
        </div>

        <div class="span5">

            <style>
                .baokuan li{
                    width: 100px;
                    height: 150px;
                    float:left;
                    margin-bottom: 10px;
                }
                .dangkou li{
                    width: 100px;
                    height: 130px;
                    float:left;
                    margin-bottom: 10px;
                }
            </style>

            <div class="bs-docs-example">
                <ul id="myTab" class="nav nav-pills">
                    <li class="active"><a href="#home" data-toggle="tab">爆款追踪</a></li>
                    <li class=""><a href="#profile" data-toggle="tab">店铺推荐</a></li>
                </ul>
                <div id="myTabContent" class="tab-content">
                    <div class="tab-pane fade active in" id="home">


                        <ul class="inline baokuan" >
                            <li >
                                <img src="http://img04.taobaocdn.com/bao/uploaded/i4/T1dNCwFuxeXXXXXXXX_!!0-item_pic.jpg_160x160.jpg" alt="">
                                <p>2014新款夏装短袖T恤套
                                    <br/>
                                    <span style="color: red;">33.00元</span>
                                </p>
                            </li>
                            <li >
                                <img src="http://img04.taobaocdn.com/bao/uploaded/i4/T1dNCwFuxeXXXXXXXX_!!0-item_pic.jpg_160x160.jpg" alt="">
                                <p>2014新款夏装短袖T恤套
                                    <br/>
                                    <span style="color: red;">33.00元</span>
                                </p>
                            </li>
                            <li >
                                <img src="http://img04.taobaocdn.com/bao/uploaded/i4/T1dNCwFuxeXXXXXXXX_!!0-item_pic.jpg_160x160.jpg" alt="">
                                <p>2014新款夏装短袖T恤套
                                    <br/>
                                    <span style="color: red;">33.00元</span>
                                </p>
                            </li>
                            <li >
                                <img src="http://img04.taobaocdn.com/bao/uploaded/i4/T1dNCwFuxeXXXXXXXX_!!0-item_pic.jpg_160x160.jpg" alt="">
                                <p>2014新款夏装短袖T恤套
                                    <br/>
                                    <span style="color: red;">33.00元</span>
                                </p>
                            </li>
                            <li >
                                <img src="http://img04.taobaocdn.com/bao/uploaded/i4/T1dNCwFuxeXXXXXXXX_!!0-item_pic.jpg_160x160.jpg" alt="">
                                <p>2014新款夏装短袖T恤套
                                    <br/>
                                    <span style="color: red;">33.00元</span>
                                </p>
                            </li>
                        </ul>

                    </div>
                    <div class="tab-pane fade" id="profile">


                        <ul class="inline dangkou" >
                            <li >
                                <img src="http://img04.taobaocdn.com/bao/uploaded/i4/T1dNCwFuxeXXXXXXXX_!!0-item_pic.jpg_160x160.jpg" alt="">
                                <p>大时代2楼B1档  </p>
                            </li>
                            <li >
                                <img src="http://img04.taobaocdn.com/bao/uploaded/i4/T1dNCwFuxeXXXXXXXX_!!0-item_pic.jpg_160x160.jpg" alt="">
                                <p>女人街5楼A02
                                </p>
                            </li>
                            <li >
                                <img src="http://img04.taobaocdn.com/bao/uploaded/i4/T1dNCwFuxeXXXXXXXX_!!0-item_pic.jpg_160x160.jpg" alt="">
                                <p>女人街3楼C9档
                                </p>
                            </li>
                            <li >
                                <img src="http://img04.taobaocdn.com/bao/uploaded/i4/T1dNCwFuxeXXXXXXXX_!!0-item_pic.jpg_160x160.jpg" alt="">
                                <p>女人街3楼C9档
                                </p>
                            </li>
                            <li >
                                <img src="http://img04.taobaocdn.com/bao/uploaded/i4/T1dNCwFuxeXXXXXXXX_!!0-item_pic.jpg_160x160.jpg" alt="">
                                <p>女人街3楼C9档
                                </p>
                            </li>
                            <li >
                                <img src="http://img04.taobaocdn.com/bao/uploaded/i4/T1dNCwFuxeXXXXXXXX_!!0-item_pic.jpg_160x160.jpg" alt="">
                                <p>女人街3楼C9档
                                </p>
                            </li>

                        </ul>

                    </div>
                </div>
            </div>


        </div>

    </div>







    <!-- /END THE FEATURETTES -->


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



</body>
</html>
