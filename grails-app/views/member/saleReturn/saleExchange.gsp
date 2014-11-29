<r:require modules="uploadify"/>
<%@ page import="common.CommonParams" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>金士代发</title>
    <style>
    .uploadifyQueue {
        text-align: center;
        left: 50%; /*FF IE7*/
        top: 50%; /*FF IE7*/
        margin-left: -130px !important; /*FF IE7 该值为本身宽的一半 */
        margin-top: -30px !important; /*FF IE7 该值为本身高的一半*/
        margin-left: 0px;
        margin-top: 0px;
        z-index: 99;
        position: fixed !important; /*FF IE7*/
        position: absolute; /*IE6*/

    }

    </style>
</head>

<body>

<script>
    function daiFaPay() {
        $('#daiFaPayModal').modal({
            keyboard: false
        })
    }
    function yuePay1() {

        if (!validGoods()) {
            return;
        }

        var wuliu = $("#wuliu").val()
        if (wuliu == "") {
            alert("请选择物流公司");
            return;
        }

        if (!$("#saleExchangeForm").parsley('validate')) {
            return false
        }
        if ($('input[type="checkbox"]:checked').length == 0) {
            alert("请选择需要换货的商品")
            return false
        }

        var wuliupic = $("#imgInput").val();
//            if($.trim(wuliupic)==""){
//                alert("请上传退回商品的物流单拍照图片，这将做为我们审核换货申请的重要凭据！")
//                return false
//            }

        if ($("#yuePay").is(":checked")) {
            if ($("#safepass_text").val() == "") {
                alert("支付密码不能为空")
                return false;
            }

            $(".saleExchangeForm").attr("target", "innerFrame")
            toActionFormCom('saleExchangeForm')
        } else {
            $(".saleExchangeForm").attr("target", "_blank")
            daiFaPay()


            $(".saleExchangeForm").submit()
        }

    }


    function checkCheckBox(obj) {
        var o = $(obj).closest('tr').next().find('input[type="text"]')
        var a = $(obj).closest('tr').next().find('select')
        if ($(obj).is(":checked")) {
            o.removeAttr("disabled")
            a.removeAttr("disabled")
        } else {
            o.attr("disabled", "disabled")
            a.attr("disabled", "disabled")
        }
    }

    function fee() {
        var serviceFee = 0.00;
        var chajia = $("#chajia").val() * 1

        if (isNaN(chajia)) {
            chajia = 0;
            $("#chajia").val(0);
        }

        var shipFee = $("#shipFee").html() * 1
        var totalFee = 0.00;
        var num = 0
        $(".daiFaGoods").find("input[type='checkbox']").each(function () {
            if ($(this).is(":checked")) {
                num = num + $(this).closest('tr').next().find('input[name="return_num"]').val() * 1
            }
        })
        serviceFee = num * <%=CommonParams.return_free%>
                totalFee = serviceFee + chajia + shipFee
        $("#serviceFee").html(serviceFee)
        $("#totalFee").html(totalFee)
    }

    function checkShip() {
        var num = 0
        $(".daiFaGoods").find("input[type='checkbox']").each(function () {

            if ($(this).is(":checked")) {

                var returnGoodsType = $(this).next().val()
                if (returnGoodsType == "1") {
                    num = num + $(this).closest('tr').next().find('input[name="return_num"]').val() * 1
                }


            }
        })
        //计算运费
        $.ajax({
            type: "POST",
            url: '<%=request.getContextPath()%>/memberDaiFaOrder/checkShipAjax',
            data: {area_id: '${order.area_id}', wuliu: '${order.wuliu}', num: num},
            success: function (msg) {
                $("#shipFee").html(msg)
                fee()
            }
        });
    }


    function selReturnType(o) {
        var t = $(o).parents('tr').next().find('input[type="text"]')
        var s = $(o).parents('tr').next().find('select')
        var font = $(o).parents('tr').next().find('font')
        var type = $(o).val()

        var checkGoodsId = $(o).parents('tr').find('.checkGoodsId')
        var returnGoodsType = $(o).parents('tr').find('.returnGoodsType')
        if (type == "0") {
            checkGoodsId.attr("checked", 'true')
            returnGoodsType.removeAttr("disabled")
            returnGoodsType.val("0")

            t.removeAttr("disabled")
            s.removeAttr("disabled")

            $(o).parents('tr').next().find(".goods_sn").attr("disabled", "disabled")
            $(o).parents('tr').next().find(".spec").attr("disabled", "disabled")

            $(o).parents('tr').next().find(".returnReason").show()

            font.html("退货信息")
            $(o).parents('tr').next().show()
        }
        if (type == "1") {
            checkGoodsId.attr("checked", 'true')
            returnGoodsType.removeAttr("disabled")
            returnGoodsType.val("1")
            t.removeAttr("disabled")
            s.removeAttr("disabled")

            $(o).parents('tr').next().find(".price").attr("disabled", "disabled")
            $(o).parents('tr').next().find(".returnReason").hide()

            font.html("换货信息")
            $(o).parents('tr').next().show()
        }

        if (type == "") {
            checkGoodsId.removeAttr("checked")
            t.attr("disabled", "disabled")
            s.attr("disabled", "disabled")
            returnGoodsType.attr("disabled", "disabled")
            $(o).parents('tr').next().hide()
        }

        checkShip();

    }


    function validGoods() {
        var isOk = true;
        var validGoods = $("#validGoods")

        if (validGoods.attr("val") != "") {
            $('input[type="checkbox"]:checked').each(function () {
                validGoodsSN = $(this).parents('tr').find(".oldGoods_sn").html();
                var goodsId = $(this).val();

                var validGoodsNum = validGoods.find("#" + goodsId).val()
                if (validGoodsNum == undefined) {

                    alert("我们收到的包裹中没有货号：" + validGoodsSN)
                    isOk = false;
                } else {
                    var return_num = $(this).parents('tr').next().find(".return_num_tip").val();
                    if (validGoodsNum < return_num) {
                        alert("包裹中收到货号：" + validGoodsSN + "商品" + validGoodsNum + "件，与您输入的数量不符")
                        isOk = false;
                    }
                }

            });
        }
        return isOk
    }


    function setWuliu(o) {
        var wuliu = $(o).val();
        if (wuliu == "") {
            alert("请选择物流公司");
            $("#wuliu").hide();
            return false;
        } else if (wuliu == "other") {
            $("#wuliu").val("")
            $("#wuliu").show();
        } else {
            $("#wuliu").val(wuliu)
            $("#wuliu").hide();
        }
    }
</script>


<div class="container body">
<span id="validGoods" val="${returnOrder}">
    <g:if test="${returnOrder}">
        <g:each in="${returnOrder.returnGoods}" var="returnGoods">
            <input type="hidden" id="${returnGoods.daiFaGoods.id}" value="${returnGoods.return_num}">
        </g:each>
    </g:if>
</span>



<g:if test="${flash.message}">
    <script>
        alert('${flash.message}')
    </script>
</g:if>

<div class="page-header">
    <h3>申请退货(<small>订单号：</small>${order.orderSN})</h3>
</div>
<g:form name="saleExchangeForm" class="saleExchangeForm" action="doSaleExchange" method="post" target="innerFrame"
        data-validate="parsley">
<input type="hidden" name="id" value="${order.id}">
<h4>商品信息（<font color="red">请选择需要退货的商品</font>）</h4>

<div class="bs-docs-example">
    <table class="table table-bordered">
        <thead>
        <tr>
            <th></th>
            <th></th>
            <th style="width: 200px;">市场位置</th>
            <th style="width: 200px;">档口号</th>
            <th>商品货号</th>
            <th>规格（颜色/尺码）</th>
            <th>退件数</th>
            <th>退货价格</th>
            %{--<th>换货件数</th>--}%
        </tr>
        </thead>
        <tbody>
        <g:each in="${order.daiFaGoods}" status="i" var="daiFaGoods">
            <g:if test="${daiFaGoods.status != '6'}">
                <tr class="daiFaGoods">
                    <td rowspan="1">

                        <input style="display: none;" class="checkGoodsId" type="checkbox" name="goods_id"
                               value="${daiFaGoods.id}" onclick="checkCheckBox(this);
                        checkShip()"/>
                        <input disabled="disabled" style="display: none;" name="returnGoodsType" class="returnGoodsType"
                               type="text"/>
                        <select name="" class="span1" onchange="selReturnType(this);
                        validGoods();">
                            <option value="">选择</option>
                            <option value="0">退货</option>
                            %{--<option value="1">换货</option>--}%
                        </select>
                    </td>
                    <td><font color="red">订单商品</font></td>
                    <td>
                        ${daiFaGoods.market} - ${daiFaGoods.floor}
                    </td>
                    <td>${daiFaGoods.stalls}</td>
                    <td class="oldGoods_sn">${daiFaGoods.goods_sn}</td>
                    <td>${daiFaGoods.spec}</td>
                    <td>${daiFaGoods.num}</td>
                    <td>${daiFaGoods.actual_price}</td>
                    %{--<td>--}%
                    %{----}%
                    %{--</td>--}%
                </tr>
                <tr style="display: none;" class="daiFaGoods success">
                    <td colspan="2"><font color="red">换货商品</font>

                        <br/>
                        <select class="span2 returnReason" name="returnReason" disabled="disabled">
                            <option value="无理由退货">无理由退货</option>
                            <option value="衣服不合适">衣服不合适</option>
                            <option value="款式不喜欢">款式不喜欢</option>
                            <option value="发错尺码">发错尺码</option>
                            <option value="衣服有破洞或有污渍（必须拒签）">衣服有破洞或有污渍（必须拒签）</option>
                            <option value="发错款式（必须核对下单照片）">发错款式（必须核对下单照片）</option>

                        </select>
                    </td>
                    <td>${daiFaGoods.market} - ${daiFaGoods.floor}
                    <g:select style="display:none;" class="input-small" name="market" from="${market}"
                              disabled="disabled" value="${daiFaGoods.market}"/>
                        <select style="display:none;" name="floor" class="input-small" disabled="disabled">
                            <option value="1楼" <g:if test="${daiFaGoods.floor == '1楼'}">selected</g:if>>1楼</option>
                            <option value="2楼" <g:if test="${daiFaGoods.floor == '2楼'}">selected</g:if>>2楼</option>
                            <option value="3楼" <g:if test="${daiFaGoods.floor == '3楼'}">selected</g:if>>3楼</option>
                            <option value="4楼" <g:if test="${daiFaGoods.floor == '4楼'}">selected</g:if>>4楼</option>
                            <option value="5楼" <g:if test="${daiFaGoods.floor == '5楼'}">selected</g:if>>5楼</option>
                            <option value="6楼" <g:if test="${daiFaGoods.floor == '6楼'}">selected</g:if>>6楼</option>
                            <option value="7楼" <g:if test="${daiFaGoods.floor == '7楼'}">selected</g:if>>7楼</option>
                            <option value="8楼" <g:if test="${daiFaGoods.floor == '8楼'}">selected</g:if>>8楼</option>
                            <option value="9楼" <g:if test="${daiFaGoods.floor == '9楼'}">selected</g:if>>9楼</option>
                        </select>
                    </td>
                    <td>
                        ${daiFaGoods.stalls}
                        <input style="display:none;" name="stalls" class="input-medium" type="text" placeholder=""
                               disabled="disabled" value="${daiFaGoods.stalls}"></td>
                    <td><input onchange="$(this).next().val($(this).val())" name="goods_sn_tip"
                               class="input-small goods_sn" type="text" placeholder="" disabled="disabled"
                               value="${daiFaGoods.goods_sn}">

                        <input style="display:none;" name="goods_sn" type="text" disabled="disabled"
                               value="${daiFaGoods.goods_sn}">
                    </td>
                    <td>
                        <input onchange="$(this).next().val($(this).val())" name="spec_tip" class="input-small spec"
                               type="text" placeholder="" disabled="disabled" value="${daiFaGoods.spec}">

                        <input style="display:none;" disabled="disabled" name="spec" type="text"
                               value="${daiFaGoods.spec}">
                    </td>
                    <td>
                        <input onchange="$(this).next().val($(this).val())" disabled="disabled"
                               onkeyup="if (value >${daiFaGoods.num})this.value =${daiFaGoods.num};
                               if (isNaN(value) || value <= 0)execCommand('undo')"
                               onafterpaste="if(value>${daiFaGoods.num})this.value=${daiFaGoods.num};if(isNaN(value) || value<=0)execCommand('undo')"
                               name="return_num_tip" class="input-small return_num_tip" type="text"
                               value="${daiFaGoods.num}"/>


                        <input style="display:none;" disabled="disabled" name="return_num" type="text"
                               value="${daiFaGoods.num}">

                    </td>
                    <td>
                        <input onchange="$(this).next().val($(this).val())" disabled="disabled"
                               onkeyup="if (value >${daiFaGoods.actual_price})this.value =${daiFaGoods.actual_price};
                               if (isNaN(value) || value <= 0)execCommand('undo')"
                               onafterpaste="if(value>${daiFaGoods.actual_price})this.value=${daiFaGoods.actual_price};if(isNaN(value) || value<=0)execCommand('undo')"
                               name="price_tip" class="input-small price" type="text"
                               value="${daiFaGoods.actual_price}"/>

                        <input style="display:none;" disabled="disabled" name="price" type="text"
                               value="${daiFaGoods.actual_price}">

                    </td>
                    %{--<td><input onkeyup="if(value>${daiFaGoods.num})this.value=${daiFaGoods.num};if(isNaN(value) || value<=0)execCommand('undo')" onafterpaste="if(value>${daiFaGoods.num})this.value=${daiFaGoods.num};if(isNaN(value) || value<=0)execCommand('undo')" data-error-message="换货件数不能为空" data-required="true"   name="return_num" class="input-small" type="text" placeholder="" value="${daiFaGoods.num}" disabled="disabled" onblur="if(value>${daiFaGoods.num})this.value=${daiFaGoods.num};if(isNaN(value) || value<=0)execCommand('undo');checkShip();">--}%
                    %{--</td>--}%
                </tr>
            </g:if>
        </g:each>
        </tbody>
    </table>

</div>

<div class="row">
    <div class="span6">


        <h4>下单人信息</h4>

        <div class="bs-docs-example">
            <table class="table table-bordered">

                <tbody>

                <tr>
                    <td>
                        下&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;单&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;人：
                        ${order.sendperson}
                        <br/>
                        发&nbsp;&nbsp;&nbsp;件&nbsp;&nbsp;地&nbsp;&nbsp;&nbsp;址：
                        ${order.sendaddress}
                        <br/>
                        联系手机/电话：
                        ${order.sendcontphone}
                        <br/>
                    </td>
                </tr>

                </tbody>
            </table>
        </div>

        <h4>退回商品物流信息</h4>

        <div class="bs-docs-example">
            <table class="table table-bordered">

                <tbody>

                <tr>
                    <td>

                        物流单号：<input value="${params.wuliu_sn}" data-error-message="物流单号不能为空" data-required="true"
                                    name="wuliu_sn" type="text" placeholder="">
                        <br>
                        物流公司：
                        <select onchange="setWuliu(this)">
                            <option value="">请选择</option>
                            <option value="中通">中通</option>
                            <option value="天天">天天</option>
                            <option value="顺丰">顺丰</option>
                            <option value="圆通">圆通</option>
                            <option value="申通">申通</option>
                            <option value="韵达">韵达</option>
                            <option value="EMS">EMS</option>
                            <option value="other">其它</option>
                        </select>

                        <input id="wuliu" name="wuliu" type="text" style="display: none;">
                        <br>
                        上传快递单图片：
                        <div id="imgFileDiv">
                            <input type="file" class="imgFile" id="imgFile" style="display:none">
                        </div>

                        <div class="imgDiv" id="imgDiv" style="position: relative;width:100px"></div>
                        <input type="hidden" id="imgInput" class="imgInput" name="wuliupic" value="">

                    </td>
                </tr>

                </tbody>
            </table>
        </div>

        <h4>买家收货地址</h4>

        <div class="bs-docs-example">
            <table class="table table-bordered">

                <tbody>

                <tr>
                    <td>

                        收&nbsp;&nbsp;件&nbsp;&nbsp;人：
                        ${order.reperson}
                        &nbsp;&nbsp;
                        联系手机/电话：
                        ${order.contphone}
                        <br/>
                        收件地区：
                        <g:areaName area_id="${order.area_id}"/>
                        <br/>
                        收件地址：
                        ${order.address}
                        <br/>
                        快递：
                        ${order.wuliu}
                        <br/>
                    </td>
                </tr>

                </tbody>
            </table>
        </div>

        <h4>退货寄达地址</h4>

        <div class="bs-docs-example">
            <table class="table table-bordered">

                <tbody>

                <tr>
                    <td>
                        收件地址：
                        广州市天河区先烈东路虚地街５号３０２（不收到付货品）
                        <br/>
                        联系电话：
                        18677050002
                    </td>
                </tr>

                </tbody>
            </table>
        </div>

    </div>

    <div class="span6">

        <div class="hero-unit">
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


<h4>支付款项</h4>

<div class="bs-docs-example">
    <table class="table table-bordered">

        <tbody>

        <tr>
            <td>
                %{--补&nbsp;&nbsp;差&nbsp;&nbsp;价：--}%
                <input style="display: none;" id="chajia" name="chajia" class="input-small" type="text" placeholder=""
                       onkeyup="if (isNaN(value))execCommand('undo');
                       fee()" onafterpaste="if(isNaN(value))execCommand('undo')" value="0" onblur="fee()">
                <font style="display: none;" color="red">如果换货商品价格大于原商品价格，请补上差价</font>
                <br/>
                手&nbsp;&nbsp;续&nbsp;&nbsp;费：<strong id="serviceFee">0</strong> （手续费每件2元）
                <br/>
                运&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;费：<strong
                    id="shipFee">0</strong> （运费根据收件地区、快递、商品总数量自动计算，<g:link controller="index" action="ship"
                                                                           target="_blank">点击查看最新运费</g:link>）
                <br/>
                支付总计：<strong id="totalFee">0</strong>
            </td>
        </tr>

        </tbody>
    </table>
</div>









支付方式： <input type="radio" id="yuePay" name="pay_type" value="0" onclick="$('#safepassSpan').show()"
             checked/>余额支付 &nbsp;&nbsp;
%{--<input type="radio" id="zfbPay" name="pay_type" value="1" onclick="$('#safepassSpan').hide()" />支付宝支付--}%
<br/>
<br/>

<div id="safepassSpan">支&nbsp;&nbsp;付&nbsp;&nbsp;&nbsp;密&nbsp;&nbsp;码：<input name="safepass" id="safepass_text"
                                                                             type="password">
    <g:if test="${!session.loginPOJO.user.safepass}"><a target="_blank" style="color: red"
                                                        href="<%=request.getContextPath()%>/memberUser/toSetSafepass">您还没有设置支付密码，点此设置</a></g:if>

</div>

<div>
    <a href="javascript:void(0)" onclick="yuePay1()" class=" btn btn-large btn-primary">支付退货费</a>
    <g:link controller="memberDaiFaOrder" action="list" params="${params}" class=" btn btn-large ">返回</g:link>
</div>

</g:form>
<div id="daiFaPayModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true" style="display: none;">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>

        <h3 id="myModalLabel">支付代发订单</h3>
    </div>


    <div class="modal-footer">
        <g:link class="btn btn-primary" controller="memberDaiFaOrder" action="saleReturnList">支付完成</g:link>
    </div>
</div>

<form method="post" id="commonActionForm" target="innerFrame">
    <input type='hidden' name='id' id='id' class='key'>
    <input type='hidden' name='safepass' id='safepass' class='key'>
</form>
<iframe id="innerFrame" name="innerFrame" height="0" frameborder="0"></iframe>
<g:form action="saleReturnList" method="post" class="commonListForm">
</g:form>
<g:render template="/layouts/footer"/>

<script>

    var $uploadifyImg = {
        'auto': true,
        'debug': false,
        'multi': false,
        'sizeLimit': '2100000',
        'fileDataName': 'imgFile',
        'fileDesc': '上传图片',
        'fileExt': '*.gif;*.jpg;*.jpeg;*.png;*.bmp;',
        'buttonImg': '<%=request.getContextPath()%>/images/upload.png',
        'cancelImg': '<%=request.getContextPath()%>/resource/uploadify/cancel.png',
        'uploader': '<%=request.getContextPath()%>/resource/uploadify/uploadify.swf',
        'script': '<%=request.getContextPath()%>/image/doImgUpload;jsessionid=<%=session.getId()%>'
    };

    function initImg() {
        $uploadifyImg.onComplete = function (event, queueId, fileObj, response, data) {
            var msg = eval('(' + response + ')');
            if (msg.error == '0') {


                var _html = '<a href="<%=request.getContextPath()%>/' + msg.url + '" target="blank"><img src="<%=request.getContextPath()%>/' + msg.url + '?imageView/2/w/100"  style="display: inline;max-width: 100px"/></a>' +
                        '<span class="closeimg" style="position: absolute; top:0px; right:0px; display:inline-block;*display:inline;zoom:1; width:16px; height:18px; z-index:100;cursor: pointer; " onClick="removeImg()"><img src="<%=request.getContextPath()%>/resource/uploadify/cancel.png"/>'
                $('#imgDiv').html(_html)
                $("#imgFileDiv").css("height", "0px");
                $("#imgInput").val(msg.url)
            } else {
                alert(msg.message)
            }
        }
        $('#imgFile').uploadify($uploadifyImg)
    }
    $(document).ready(function () {

        initImg();
    });

    function removeImg() {
        $('#imgDiv').html("")
        $("#imgFileDiv").css("height", "auto");
        $("#imgInput").val("")
    }

</script>
</body>
</html>
