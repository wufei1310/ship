<%@ page import="org.springframework.core.io.support.PropertiesLoaderUtils; org.springframework.core.io.ClassPathResource" %>
<%
    def properties = PropertiesLoaderUtils.loadProperties(new ClassPathResource("sysSetting.properties"))
%>

<r:require modules="uploadify"/>
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
<div class="container body">
<g:if test="${flash.message}">
    <div class="alert alert-error">
        <button type="button" class="close" data-dismiss="alert">&times;</button>
        <strong>${flash.message}</strong>
    </div>
</g:if>

<div class="page-header">
    <h3>创建新订单</h3>
</div>
<g:form name="addDaiFaForm" id="addDaiFaForm" data-validate="parsley" class="form-signin addDaiFaForm"
        controller="memberDaiFaOrder" action="doAdd" method="post">
<h4>1.填写商品信息
<span style="font-size: 0.8em;">
    <g:link controller="memberDaiFaOrder" action="add" params="[isCopy:'last']">复制上次订单商品</g:link>
</span>
</h4>

<div class="bs-docs-example">
    <table class="table table-bordered">
        <thead>
        <tr>
            <th style="width: 200px;">市场位置</th>
            <th style="width: 200px;">档口号</th>
            <th>商品货号</th>
            <th>规格（颜色/尺码）</th>
            <th>件数</th>
            <th>单件价格</th>
            <th>图片</th>
            <th><button onclick="addDaiFaGoods()" type="button" class="btn btn-primary">增加</button></th>
        </tr>
        </thead>
        <tbody>
        <g:if test="${goodsList}">
            <g:each in="${goodsList}" status="i" var="daiFaGoods">
                <tr class="daiFaGoods">
                    <td>
                        <g:select class="input-small marketselect" value="${daiFaGoods.market}" name="market" from="${market}"/>
                        <select name="floor" class="input-small floorselect">
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
                    <td><input name="stalls" class="input-medium stalls" type="text" placeholder=""
                               value="${daiFaGoods.stalls}"></td>
                    <td><input name="goods_sn" class="input-small goods_sn" type="text" placeholder=""
                               value="${daiFaGoods.goods_sn}"></td>
                    <td><input name="spec" class="input-small spec" type="text" placeholder=""
                               value="${daiFaGoods.spec}"></td>
                    <td><input onkeyup="if (isNaN(value))execCommand('undo')"
                               onafterpaste="if(isNaN(value))execCommand('undo')"
                               onblur="if (isNaN(value))execCommand('undo');
                               checkShip()" name="num" class="input-small num" type="text" placeholder=""
                               value="${daiFaGoods.num}"></td>
                    <td><input onkeyup="if (isNaN(value))execCommand('undo')"
                               onafterpaste="if(isNaN(value))execCommand('undo')"
                               onblur="if (isNaN(value))execCommand('undo');
                               checkShip()" name="price" class="input-small price" type="text" placeholder=""
                               value="${daiFaGoods.price}"></td>
                    <td class="imgTd">
                        <div id="imgFileDiv${i}"><input type="file" class="imgFile" id="imgFile${i}"
                                                        style="display:none"></div>

                        <div class="imgDiv" id="imgDiv${i}" style="position: relative;width:100px">
                            <g:if test="${daiFaGoods.attach_id}">
                                <a href="#" >
                                    <img src="${daiFaGoods.attach_id}" style="display: inline;max-width: 100px"/></a>
                                <span class="closeimg" style="position: absolute; top:0px; right:0px; display:inline-block;*display:inline;zoom:1; width:16px; height:18px; z-index:100;cursor: pointer; " onClick="removeImg(${i})"><img
                                    src="<%=request.getContextPath()%>/resource/uploadify/cancel.png"/>
                            </g:if>
                        </div>
                        <input type="hidden" id="imgInput${i}" class="imgInput" name="attach_id"
                               value="${daiFaGoods.attach_id}">
                    </td>
                    <td><button onclick="removeDaiFaGoods(this)" type="button" class="btn btn-danger">删除</button>
                    <br/>
                        <a style="margin-top: 2px;" href="javascript:void(0)" onclick="toHot(this)" class="btn btn-info">热门货源</a>

                    </td>
                </tr>
            </g:each>
        </g:if><g:else>
            <tr class="daiFaGoods">
                <td>
                    <g:select class="input-small marketselect" name="market" from="${market}"/>
                    <select name="floor" class="input-small floorselect">
                        <option value="1楼">1楼</option>
                        <option value="2楼">2楼</option>
                        <option value="3楼">3楼</option>
                        <option value="4楼">4楼</option>
                        <option value="5楼">5楼</option>
                        <option value="6楼">6楼</option>
                        <option value="7楼">7楼</option>
                        <option value="8楼">8楼</option>
                        <option value="9楼">9楼</option>
                    </select>
                </td>
                <td><input name="stalls" class="input-medium stalls" type="text" placeholder="请不要输入市场和楼层"></td>
                <td><input name="goods_sn" class="input-small goods_sn" type="text" placeholder=""></td>
                <td><input name="spec" class="input-small spec" type="text" placeholder=""></td>
                <td><input onkeyup="if (isNaN(value))execCommand('undo')"
                           onafterpaste="if(isNaN(value))execCommand('undo')"
                           onblur="if (isNaN(value))execCommand('undo');
                           checkShip();" name="num" class="input-small num" type="text" placeholder=""></td>
                <td><input onkeyup="if (isNaN(value))execCommand('undo')"
                           onafterpaste="if(isNaN(value))execCommand('undo')"
                           onblur="if (isNaN(value))execCommand('undo');
                           checkShip();" name="price" class="input-small price" type="text" placeholder=""></td>
                <td class="imgTd"><div id="imgFileDiv0"><input type="file" class="imgFile" id="imgFile0"
                                                               style="display:none"></div>

                    <div class="imgDiv" id="imgDiv0" style="position: relative;width:100px"></div><input type="hidden"
                                                                                                         id="imgInput0"
                                                                                                         class="imgInput"
                                                                                                         name="attach_id">
                </td>
                <td><button onclick="removeDaiFaGoods(this)" type="button" class="btn btn-danger">删除</button>
                    <br/>
                    <a style="margin-top: 2px;" href="javascript:void(0)" onclick="toHot(this)" class="btn btn-info">热门货源</a></td>
            </tr>
        </g:else>

        </tbody>
    </table>
</div>



<h4>2.填写收货地址</h4>

<div class="bs-docs-example">
    <table class="table table-bordered">

        <tbody>

        <tr>
            <td>

                收&nbsp;&nbsp;件&nbsp;&nbsp;人：
                <input data-error-message="收件人不能为空" data-required="true" id="reperson" name="reperson" type="text"
                       class="" placeholder="">
                &nbsp;&nbsp;
                联系手机/电话：
                <input data-error-message="联系手机/电话不能为空" data-required="true" id="contphone" name="contphone" type="text"
                       class="" placeholder="">
                <br/>
                收货地址：
                <select class="span2" name="area_id" id="area_id" onchange="afterArea()">
                    <option value="">请选择省份</option>
                     <g:each in="${areaChild}" var="area">
                         <option value="${area.id}">${area.name}</option>
                     </g:each>
                </select>
                %{--<g:include controller="area" action="reqAreaField"/>--}%
                %{--<br/>--}%
                %{--<br/>--}%
                %{--收件地址：--}%
                <input data-error-message="收件地址不能为空" data-required="true" id="address" name="address" type="text"
                       class="input-xxlarge" placeholder="请填写市，县，区，街道，门牌等详细资料">

                <br/>
                选择快递：
                <select id="wuliu" name="wuliu" class="input-small" onchange="checkShip()">
                    <option value="中通">中通</option>
                    <option value="韵达">韵达</option>
                    <option value="申通">申通</option>
                    <option value="天天">天天</option>

                    <option value="顺丰">顺丰</option>
                    <option value="圆通">圆通</option>

                    <!--<option value="汇通">汇通</option>-->


                    <!--                    <option value="电邮小包">电邮小包</option>-->
                    <option value="EMS">EMS</option>
                    <option value="大包">大包</option>

                </select>
                <span id="wuliuxuanze" style="color: red;">请向快递公司核实您的买家地址是否在派件范围内，避免退件</span>
                <span id="dabao" style="color: red;display:none;">大包服务，将发大包公路运输，运费到付，增加1元/件短途打包转运费</span>
                <br/>
                预计运费：
                <input onkeyup="if (isNaN(value))execCommand('undo')" onafterpaste="if(isNaN(value))execCommand('undo')"
                       readonly="readonly" value="0" data-required-message="运费不能为空" data-required="true"
                       data-min-message="运费至少大于0" data-min="0" id="addShipFee" type="text" class="">
                <span style="color: red" id="wuliuSpan">运费根据收件地区、快递、商品总数量自动计算，<g:link controller="index" action="ship"
                                                                                      target="_blank">点击查看最新运费</g:link></span>

            </td>
        </tr>

        </tbody>
    </table>
</div>

<h4>3.填写下单人信息</h4>

<div class="bs-docs-example">
    <table class="table table-bordered">

        <tbody>

        <tr>
            <td>
                下&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;单&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;人：
                <input data-error-message="下单人不能为空" data-required="true" name="sendperson" type="text" class=""
                       placeholder="" value="${daiFaOrder.sendperson}">


                &nbsp;&nbsp;联系手机/电话：
                <input data-error-message="联系手机/电话不能为空" data-required="true" name="sendcontphone" type="text" class=""
                       placeholder="" value="${daiFaOrder.sendcontphone}">
                <br/>
                下&nbsp;&nbsp;单&nbsp;人&nbsp;地&nbsp;址：
                %{--<select onchange="checkAddress(this)" onclick="checkAddress(this)">--}%
                    %{--<option value="0">以我的真实地址做为发货地址</option>--}%
                    %{--<option value="1">借用金士地址做为我的发货地址</option>--}%
                %{--</select>--}%
                <input data-error-message="下单人地址不能为空" data-required="true" name="sendaddress" id="sendaddress"
                       type="text" class="input-xxlarge" placeholder="" value="${daiFaOrder.sendaddress}">

                <br/>

                备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注：
                <input name="senddesc" type="text" class="input-xxlarge" value="&nbsp;">
        </tr>

        </tbody>
    </table>
</div>

<h4>4.增值服务</h4>

<div class="bs-docs-example">
    <table class="table table-bordered">

        <tbody>

        <tr>
            <td>

                选择礼品：<select id="regards" name="regards" class="input-large" onchange="fee()">
                <option value="">请选择</option>
                <option value="1">好评卡+小礼物(1元/件)</option>
                <option value="5">换吊牌　更换包装袋(1元/件)</option>
                <option value="4">包装盒(1.5元/件)</option>
                <option value="2">包装盒+小礼物(2元/件)</option>
                <option value="3">好评卡+包装盒+小礼物(3元/件)</option>
            </select>

            </td>
        </tr>

        </tbody>
    </table>
</div>

<h4>5.支付款项</h4>

<div class="bs-docs-example">
    <table class="table table-bordered">

        <tbody>

        <tr>
            <td>
                商品总价：
                <strong id="goodsFee">0</strong>
                <input type="hidden" name="goodsFee">
                <br/>
                运&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;费：&nbsp;<strong id="shipFee">0</strong>
                <input type="hidden" name="shipFee">
                <br/>
                代&nbsp; 发&nbsp; 费：&nbsp;<strong id="serviceFee">0</strong>
                <input type="hidden" name="serviceFee">(代发每件1元,大包每件２元)
                <br/>
                礼&nbsp; 品&nbsp; 费：&nbsp;<strong id="regardsFee">0</strong>
                <input type="hidden" name="regardsFee">
                <br/>
                订单总价：&nbsp;<strong id="totalFee">0</strong>
                <input type="hidden" name="totalFee">
                <!--                <br/>
                                  账户余额：<strong style="color: red;">0</strong>(余额不足，去充值)-->
                <br/>
                <br/>
                选择支付方式： <input type="radio" id="zfbPay" name="pay_type" value="1" onclick="$('#safepassSpan').hide()"
                               checked/>支付宝支付
                <input type="radio" id="yuePay" name="pay_type" value="0"
                       onclick="$('#safepassSpan').show()"/>余额支付 &nbsp;&nbsp;
                <input style="display: none;" type="radio" id="saveOrder" name="pay_type" value="2"/>
                <br/>
                <br/>

                <div id="safepassSpan" style="display: none;">支&nbsp;&nbsp;付&nbsp;&nbsp;&nbsp;密&nbsp;&nbsp;码：<input
                        name="safepass" id="safepass" type="password">



                    <g:if test="${!session.loginPOJO.user.safepass}">

                        <a target="_blank" style="color: red"
                                                                        href="<%=request.getContextPath()%>/memberUser/toSetSafepass">您还没有设置支付密码，点此设置</a></g:if>
                    <g:else>
                        <a target="_blank" style="color: red"
                           href="<%=request.getContextPath()%>/memberUser/toSetSafepass">忘记支付密码？</a>

                    </g:else>


                </div>

                <div>
                    <button id="addDaiFaButt" type="button" class=" btn btn-large btn-primary"
                            onclick="beforAddDaiFaForm(this)">提交订单并支付</button>
                    <a class=" pull-right" style="margin-right: 20px;"  href="javascript:void(0);"
                            onclick="beforSaveDaiFaForm(this)">以后集中支付</a>
                </div>
            </td>
        </tr>

        </tbody>
    </table>
</div>
</g:form>


<div id="daiFaPayModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true" style="display: none;">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>

        <h3 id="myModalLabel">支付代发订单</h3>
    </div>


    <div class="modal-footer">
        <g:link class="btn btn-primary" controller="memberDaiFaOrder" action="list">支付完成</g:link>
    </div>
</div>
<iframe id="innerFrame" name="innerFrame" height="0" frameborder="0"></iframe>
<g:form action="list" controller="memberDaiFaOrder" method="post" class="commonListForm">
</g:form>
<g:render template="/layouts/footer"/>
</div> <!-- /container -->


<script>


    var isPro = '<%=properties.getProperty("isPro")%>'

    var newaddress = "广州市天河区先烈东路虚地街5号302"
    var my_address = "${daiFaOrder.sendaddress}"

    function checkAddress(obj) {
        if (obj.value == '0') {
            if(my_address=="广州市白云区沙太南路318号橡树园小区A2栋310号"){
                my_address = newaddress;
            }

            $("#sendaddress").val(my_address)
            $("#sendaddress").removeAttr('readOnly')
        }
        if (obj.value == '1') {
            $("#sendaddress").val(newaddress)
            $("#sendaddress").attr('readOnly', 'readOnly')
        }
    }

    <g:if test="${goodsList}">
    var imgFileId = ${goodsList.size()}
            fee()
    </g:if><g:else>
    var imgFileId = 0
    </g:else>


    var priceMap = {}
    priceMap["中通"] = 8
    priceMap["顺丰"] = 13
    priceMap["圆通"] = 8
    priceMap["申通"] = 8
    priceMap["韵达"] = 8
    priceMap["电邮小包"] = 11
    priceMap["天天"] = 6

    //        checkWuliu(document.getElementById("wuliu"))
    //        function checkWuliu(obj){
    //          $("#wuliuSpan").html('‘'+obj.value+'’最低首重'+'‘'+priceMap[obj.value]+'’元    <g:link controller="index" action="ship" target="_blank">点击查看续重详情</g:link>')
    //        }

    function pageProcess() {
        $("#addDaiFaButt").removeAttr("disabled")
    }


    function beforSaveDaiFaForm(obj){
         $("#saveOrder").attr("checked","checked")
        beforAddDaiFaForm(obj)

    }


    function beforAddDaiFaForm(obj) {




        var msg = ''
        %{----}%
          %{--var lot = '${lot}'--}%

        %{--if(isPro=="true"){--}%
                    %{--if(lot!="lot"){--}%
                        %{--$(".imgInput").each(function(){--}%
                            %{--if ($(this).val() == "") {--}%
                                %{--msg = msg + "商品图片不能为空" + '\n'--}%
                                %{--return false;--}%
                            %{--}--}%
                    %{--})--}%
        %{--}--}%
        %{--}--}%





        $("input[name='stalls']").each(function () {
            if ($(this).val() == "") {
                msg = msg + "档口号不能为空" + '\n'
//              alert("档口号不能为空")
                return false;
            }
        })

        $("input[name='goods_sn']").each(function () {
            if ($(this).val() == "") {
                msg = msg + "商品货号不能为空" + '\n'
//              alert("商品货号不能为空")
                return false;
            }
        })

        $("input[name='spec']").each(function () {
            if ($(this).val() == "") {
                msg = msg + "规格（颜色/尺码）不能为空" + '\n'
//              alert("规格（颜色/尺码）不能为空")
                return false;
            }
        })

        $("input[name='num']").each(function () {


            if ($(this).val() == "") {
                msg = msg + "件数不能为空" + '\n'
//              alert("件数不能为空")
                return false;
            }
            if ($(this).val() <= 0) {
                msg = msg + "件数必须大于0" + '\n'
//              alert("件数必须大于0")
                return false;
            }
        })


        $("input[name='price']").each(function () {

            if ($(this).val() == "") {
                msg = msg + "单件价格不能为空" + '\n'
//              alert("单件价格不能为空")
                return false;
            }
            if ($(this).val() <= 0) {
                msg = msg + "单件价格必须大于0" + '\n'
//              alert("单件价格必须大于0")
                return false;
            }

        })


        if (msg != '') {
            alert(msg)
            return false;//取消提交
        } else {

        }
        if ($("#area_id").val() == '') {
            alert("请选择收件地区")
            return false
        }
//          var addShipFee = $("#addShipFee").val()
//          var wuliuPrice = priceMap[$("#wuliu").val()]
//          if(addShipFee*1 < wuliuPrice*1){
//            alert("‘"+$("#wuliu").val()+"’最低首重"+"‘"+priceMap[$("#wuliu").val()]+"’元,请重新填写运费")
//            $("#addShipFee").focus()
//            return false
//          }

        fee()


        if (($("#reperson").val() != '')
                && ($("#contphone").val() != '')
                && ($("#address").val() != '')) {

        }
        if ($("#yuePay").is(":checked")) {
            if ($("#safepass").val() == "") {
                alert("支付密码不能为空")
                return false;
            } else {
                if (!$("#addDaiFaForm").parsley('validate')) {
                    return false
                } else {


                    if (confirm("由于沙河档口销货很快，容易缺货，请在下单付款前，确认好档口是否开档有货。如代发人员去到缺货，将只能由您取取消订单并退款，货款金额将会退还到您的金士代发官网账户上，可下次下单时使用。如订单中所有的商品都取消，所支付的物流费也将退还。")) {
                    } else {
                        return false;
                    }


                    $(obj).attr("disabled", "disabled")
                    $("#addDaiFaForm").attr("target", "innerFrame")
                    toActionFormCom('addDaiFaForm')
                }
            }
        } else {
            if (!$("#addDaiFaForm").parsley('validate')) {
                return false
            } else {

                if($("#zfbPay").attr("checked")=="checked"){
                    $('#daiFaPayModal').modal({
                        keyboard: false
                    })
                    $("#addDaiFaForm").attr("target", "_blank")
                }


                $("#addDaiFaForm").submit()
            }
        }


    }

    function fee() {

        var goodsFee = 0.00;
        var serviceFee = 0.00;
        var shipFee = 0.00;
        var regardsFee = 0.00;
        var totalFee = 0.00;
        $(".daiFaGoods").each(function () {
            var num = $(this).find("input[name='num']").val();
            var price = $(this).find("input[name='price']").val();
            goodsFee = parseFloat(goodsFee) + parseFloat(price * num);
            serviceFee = parseFloat(serviceFee) + parseFloat(1 * num);
            shipFee = parseFloat($("#addShipFee").val());
        })
        if ($("#regards").val() != '') {

            if($("#regards").val()=="4"){
                regardsFee = 1.5
            }else if($("#regards").val()=="5"){
                regardsFee = 1
            }else{
                regardsFee = parseFloat($("#regards").val());
            }


        }




        $("#goodsFee").html(goodsFee.toFixed(2));
        $("#goodsFee").next().val(goodsFee.toFixed(2))


        var wuliu = $("#wuliu").val();
        if(wuliu=="大包"){
            $("#serviceFee").html(serviceFee.toFixed(2)*2);
            $("#serviceFee").next().val(serviceFee.toFixed(2)*2)
            serviceFee = serviceFee.toFixed(2)*2
        }else{
            $("#serviceFee").html(serviceFee.toFixed(2));
            $("#serviceFee").next().val(serviceFee.toFixed(2))
        }




        $("#shipFee").html(shipFee.toFixed(2));
        $("#shipFee").next().val(shipFee.toFixed(2))
        $("#regardsFee").html(regardsFee.toFixed(2));
        $("#regardsFee").next().val(regardsFee.toFixed(2))

        totalFee = parseFloat(goodsFee) + parseFloat(serviceFee) + parseFloat(shipFee) + parseFloat(regardsFee);
        $("#totalFee").html(totalFee.toFixed(2));
        $("#totalFee").next().val(totalFee.toFixed(2))
    }


    function removeDaiFaGoods(o) {

        $(o).parents("tr").remove();
        checkShip()
    }

    function addDaiFaGoods() {
        var lastTR = jQuery(".daiFaGoods:last")
        var cloneTR = lastTR.clone();
        cloneTR.show();
        cloneTR.find("input").val('')

        imgFileId++
        cloneTR.find(".imgTd").html('<div id="imgFileDiv' + imgFileId + '"><input type="file" class="imgFile" id="imgFile' + imgFileId + '" style="display:none"></div><div class="imgDiv" id="imgDiv' + imgFileId + '" style="position: relative;width:100px"></div><input type="hidden" id="imgInput' + imgFileId + '" class="imgInput"  name="attach_id" >')


        jQuery(".daiFaGoods:last").after(cloneTR)
        initImg(imgFileId)
    }

    var timeRefresh;
    timer()
    function timer() {
        timeRefresh = setInterval(function () {
                    refreshAjax()
                },
                600000);
    }

    function refreshAjax() {
        $.ajax({
            type: "POST",
            url: '<%=request.getContextPath()%>/memberDaiFaOrder/acceptAjax',
            success: function (msg) {

            }
        });

    }
    function removeImg(id) {
        $('#imgDiv' + id).html("")
        $("#imgFileDiv" + id).css("height", "auto");
        $("#imgInput" + id).val("")
    }


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

    function initImg(id) {
        $uploadifyImg.onComplete = function (event, queueId, fileObj, response, data) {
            var msg = eval('(' + response + ')');
            if (msg.error == '0') {


                var _html = '<a href="#" ><img src="<%=request.getContextPath()%>/' + msg.url + '?imageView/2/w/100"  style="display: inline;max-width: 100px"/></a>' +
                        '<span class="closeimg" style="position: absolute; top:0px; right:0px; display:inline-block;*display:inline;zoom:1; width:16px; height:18px; z-index:100;cursor: pointer; " onClick="removeImg(' + id + ')"><img src="<%=request.getContextPath()%>/resource/uploadify/cancel.png"/>'
                $('#imgDiv' + id).html(_html)
                $("#imgFileDiv" + id).css("height", "0px");
                $("#imgInput" + id).val(msg.url)
            } else {
                alert(msg.message)
            }
        }
        $('#imgFile' + id).uploadify($uploadifyImg)
    }
    $(document).ready(function () {

        <g:if test="${goodsList}">
        <g:if test="${goodsList.size()<100}">
        <g:each in="${goodsList}" status="i" var="daiFaGoods">
        <g:if test="${daiFaGoods.attach_id}">
        $("#imgFileDiv${i}").css("height", "0px");
        </g:if>
        initImg(${i})
        </g:each>
        </g:if>
        </g:if><g:else>
        initImg(0)
        </g:else>
    });

    function checkShip() {
        $("#wuliuxuanze").show();
        $("#dabao").hide();
        var num = 0
        var area_id = $("#area_id").val()
        var wuliu = $("#wuliu").val()
        $(".daiFaGoods").each(function () {
            num = num + $(this).find("input[name='num']").val() * 1;
        })
        if (wuliu == "大包") {
            $("#dabao").show();
            $("#wuliuxuanze").hide();
            $("#addShipFee").val("0")
            fee()
        } else if (area_id != "" && wuliu != "") {
            $.ajax({
                type: "POST",
                url: '<%=request.getContextPath()%>/memberDaiFaOrder/checkShipAjax',
                data: {area_id: area_id, wuliu: wuliu, num: num},
                success: function (msg) {

                    if(msg.indexOf("DOCTYPE html")>-1){
                        alert("您已经太久没有操作，系统将引导您重新登录！");
                        window.href = "<%=request.getContextPath()%>?reqAction=reqLogin"
                    }

                    $("#addShipFee").val(msg)
                    fee()
                }
            });
        } else {
            fee()
            // alert("请先选择收件地区和快递")
        }
    }

    function afterArea() {
        checkShip()
    }


    function toHot(o){
        var hotlink = '<%=request.getContextPath()%>/memberNewProduct/list'
        var market=$(o).parents("tr").find(".marketselect").val();
        var floor=$(o).parents("tr").find(".floorselect").val();
        floor=floor.replace("楼","F");
        hotlink = hotlink + "?market="+market+"&floor="+floor;
        window.open(hotlink,'hotlink')
    }
</script>
</body>
</html>
