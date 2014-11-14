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
            热门批发
            <g:link controller="memberDaiFaOrder" action="add" class="btn btn-primary btn-large pull-right">创建新订单</g:link>
            <g:link style="margin-right:10px;" controller="memberDaiFaOrder" action="list" class="btn btn-primary btn-large pull-right">代发动态</g:link>

        </h3>
    </div>
    <script>

        function isMarketSelect() {
            return true;
            if ($("#floor").val() != "") {
                if ($("#market").val() == "") {
                    alert("请先选择市场再选择楼层！")
                    return false;
                }
            }
        }

    </script>

    <div class="well">

        <g:form onsubmit="return isMarketSelect()" controller="memberNewProduct" action="list" class="form-inline">

            <g:select id="market" noSelection="['': '请选择']" class="input-small" value="${params.market}" name="market"
                      from="${market}"/>


            <input value="${params.stall}" name="stall" type="text" class="input-medium"
                   placeholder="按档口号查询">
            <input value="${params.goods_sn}" name="goods_sn" type="text" class="input-medium"
                   placeholder="按货口号查询">
            <input value="${params.store_name}" name="store_name" type="text" class="input-medium"
                   placeholder="按店铺名称查询">
            <input class="btn btn-primary" type="submit" value="筛选"/>
        </g:form>

    </div>




<div class="row-fluid">
<ul class="thumbnails">

    <g:set var="j" value="${0}" />
    <g:each in="${xmlGoodsList}" status="i" var="goods">
        <g:if test="${goods!=null}">


            <li class="span3">
                <div class="thumbnail">

                    <g:link controller="memberNewProduct" action="goods" id="${goods.goods_id}"
                            class="areafancybox fancybox.ajax">

                        <img title="${goods.goods_name}" style="width: 300px; height: 200px;" src="${goods.pic}">
                    </g:link>
                    <div class="caption">
                        <h4>${goods.goods_name.size() > 18 ? goods.goods_name[0..18] : goods.goods_name[0..10]}</h4>
                        <p>价格：${goods.price}</p>

                        <p>货号：${goods.goods_sn}</p>

                        <p>尺码：${goods.skusize}</p>

                        <p>颜色：${goods.skucolor}</p>

                        <p>
                            <g:link controller="memberNewProduct" action="list"
                                    params="[store_name: goods.store_name]">${goods.store_name}</g:link>

                        &nbsp;${goods.market}&nbsp;${goods.floor}&nbsp;${goods.stall}</p>

                        <p>手机：${goods.tel}&nbsp;&nbsp;&nbsp;</p>
                        %{--<p>QQ：${goods.qq}</p>--}%
                    <p>

                        <g:link controller="memberNewProduct" action="goods" id="${goods.goods_id}"
                                class="btn btn-info areafancybox fancybox.ajax">瞅瞅</g:link>

                        %{--<g:remoteLink after="layer_load();"  onSuccess="addTaobaoSuccess(data)" action="addTaobaoGoods" id="${goods.taobao_id}" class="btn btn-primary">上传淘宝</g:remoteLink>--}%
                        %{--<g:link target="_blank" class="btn btn-primary" controller="memberDaiFaOrder" action="add"--}%
                        %{--params="[hotId: goods.goods_id]">代我发货</g:link>--}%


                        <a href="#myModal${goods.goods_id}" role="button" class="btn btn-primary" data-toggle="modal">代我发货</a>
                    <!-- Modal -->
                        <g:form method="post" controller="memberDaiFaOrder" action="add" name="addNewOrder" target="_blank">
                            <div id="myModal${goods.goods_id}" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
                                 aria-hidden="true">
                                <div class="modal-header">
                                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>

                                    <h3 id="myModalLabel">选择颜色尺码</h3>
                                </div>

                                <input type="hidden" name="hotId" value="${goods.goods_id}">
                                <div class="modal-body">
                                    <p>尺码：

                                        <g:each var="size" in="${goods.skusize.split(",")}">
                                            <input type="checkbox" name="size" value="${size}"> ${size}
                                        </g:each>

                                    </p>

                                    <p>颜色：
                                        <g:each var="color" in="${goods.skucolor.split(",")}">

                                            <input type="checkbox" name="color" value="${color}"> ${color}
                                        </g:each>
                                    </p>
                                </div>

                                <div class="modal-footer">
                                    <button class="btn btn-primary btn-large">确定</button>
                                </div>

                            </div>
                        </g:form>
                    </p>
                    </div>
                </div>
            </li>

        </g:if>


        <g:if test="${j % 4 == 3}">
            </ul>
        </div>
        <div class="row-fluid">
         <ul class="thumbnails">
        </g:if>
        <g:set var="j" value="${j + 1}"/>
    </g:each>

</ul>
</div>
    <ul class="pager">
        <g:set var="params" value="${params.put('p', (p as int) - 1)}"/>
        <li><g:link controller="memberNewProduct" action="list" params="${params}">上一页</g:link></li>
        <g:set var="params" value="${params.put('p', (p as int) + 1)}"/>
        <li><g:link controller="memberNewProduct" action="list" params="${params}">下一页</g:link></li>
    </ul>

    <g:render template="/layouts/footer"/>

    <g:form target="_blank" class="reqTaobaoForm" name="reqTaobaoForm" action="reqTaobao"></g:form>

</div> <!-- /container -->
<script>
    $(document).ready(function () {
        $(".areafancybox").fancybox({
            'width': '300px'
        });
    });


    function addTaobaoSuccess(data) {
        if (data == "0") {
            alert("金士代发需要您的授权才能向您的店铺一键上传操作，去授权?")
            $(".reqTaobaoForm").submit();
        }

        if (data == "1") {

            layer_close();
            alert("上传商品成功！")
        }
        if (data == "2") {

            layer_close();
            alert("您已上传过该商品，淘宝不允许重复上传！")
        }
    }

</script>
</body>
</html>
