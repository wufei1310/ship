<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>金士代发</title>
</head>

<body>
<div class="container body" id="page">

    <div class="page-header">
        <h3>
            如果您需要为您的买家提供退货、换货服务，只需2元，我们为您代劳。
        </h3>
    </div>

    <div class="row">
        <div class="span12">
            <div class="hero-unit">
                <div class="row">
                    <div class="span6 pull-left">
                        <h2>金士代发平台订单<span style="font-size: 0.4em;"></span></h2>

                        <p>商品是在本平台代发，请输入退回商品物流单号。</p>
                        <g:form class="form-inline" controller="memberDaiFaOrder" action="goToReturn2" method="post">
                            <input style="height: 30px;" value="${params.orderSN}" name="wuliu_sn" type="text"
                                   class="input-medium span4" placeholder="请输入退回商品物流单号">

                            <button type="submit" class="btn btn-primary btn-large">下一步</button>
                            %{--<g:link controller="memberDaiFaOrder" action="list"--}%
                                    %{--class="btn btn-success btn-large">我的订单</g:link>--}%
                        </g:form>

                    </div>
                    <div class="span4 pull-right">
                        <h4>退货下单前准备</h4>
                        <ol>
                            <li>在委托我们帮您退、换货前，请先跟档口联系该商品是否可以退、换以及价格</li>
                            <li>通知您的买家将要退、换的货物寄回至<strong>广州市天河区先烈东路虚地街５号３０２</strong>（不收到付货品）</li>
                            <li>当您的买家按上述地址发货后，请向您的买家取得退回的快递单照片</li>
                            <li>下单并正确填写信息（填写错误的退货信息会拖延您的最佳退、换货时机，也有可能造成您的货物损失）</li>
                        </ol>
                    </div>
                </div>

            </div>
        </div>

        %{--<div class="span6">--}%
        %{--<div class="hero-unit">--}%
        %{--<h2>非本平台订单</h2>--}%
        %{--<p>商品不是我们代发的，请在这里下单。</p>--}%
        %{--<g:link controller="memberDaiFaOrder" action="toSaleReturnAdd"  class="btn btn-primary btn-large">我要退货</g:link>--}%
        %{--</div>--}%
        %{--</div>--}%

    </div>





    <g:render template="/layouts/footer"/>

</div> <!-- /container -->

</body>
</html>
