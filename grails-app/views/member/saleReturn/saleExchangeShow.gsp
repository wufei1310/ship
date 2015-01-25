<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>金士代发</title>
</head>

<body>

<script>
    function daiFaPay() {
        $('#daiFaPayModal').modal({
            keyboard: false
        })
    }
    function yuePay1() {
        if ($("#safepass_text").val() == "") {
            alert("支付密码不能为空")
            return false;
        }
        toActionCom({id: '${returnOrder.id}', safepass: $("#safepass_text").val()}, '<%=request.getContextPath()%>/memberDaiFaOrder/paySaleReturn')
    }


    function returnGoOn(){
        if($("#safepass_text1").val()==""){
            alert("支付密码不能为空")
            return false;
        }else{
            $(".returnGoOn #safepass").val($("#safepass_text1").val())
           $(".returnGoOn").submit()
        }
    }

</script>

<g:form controller="memberDaiFaOrder" action="returnGoOn" method="post" class="returnGoOn">
    <input name="id" type="hidden" value="${kingsReturnOrder?.id}">
    <input id="safepass" name="safepass" type="hidden">
    <input name="mid" type="hidden" value="${returnOrder.id}">
</g:form>


<div class="container body">
<g:if test="${flash.message}">
    <script>
        alert('${flash.message}')
    </script>
</g:if>

<div class="page-header">
    <h5 class=" alert alert-danger">退货不成的商品我们将为您免费保管十天，十天后将会收取10元每天的保管费，二十八天后将默您已放弃该商品的所有权并清理出我们的仓库。</h5>
    <input id="isfail" value="0" type="hidden">
    <h3>退货申请(<small>订单号：</small>${returnOrder.daiFaOrder.orderSN})
    <g:if test="${kingsReturnOrder && kingsReturnOrder.status == '2'}">
        <g:link style="margin-left: 10px;display: none;" params="[mid:returnOrder.id]" id="${kingsReturnOrder.id}" controller="memberDaiFaOrder"
                action="giveup" class="btn btn-danger btn-large pull-right jihui">不要了</g:link>



                <g:if test="${new Date() - returnOrder.dateCreated<30}">

                    <g:link style="display: none;" params="[mid:returnOrder.id]" id="${kingsReturnOrder.id}" controller="memberDaiFaOrder" action="addBack"
                            class="btn btn-primary btn-large pull-right jihui">帮我寄回退不了的商品</g:link>

                </g:if>



    </g:if>


    </h3>
    </div>
<g:form>
<g:if test="${kingsReturnOrder && kingsReturnOrder.status == '2'}">
    <h4>收到包裹情况</h4>

    <div class="bs-docs-example">
        <table class="table table-bordered">
            <thead>
            <tr>
                <th colspan="2" style="color: red"></th>
                <th style="width: 200px;">市场位置</th>
                <th style="width: 200px;">档口号</th>
                <th>商品货号</th>
                <th>规格（颜色/尺码）</th>
                <th>收到件数</th>
                <th>单件拿货价格</th>
                <th>退货信息</th>
            </tr>
            </thead>
            <tbody>
            <g:each in="${kingsReturnOrder.returnGoods}" status="i" var="returnGoods">
            %{--<tr class="daiFaGoods" >--}%
            %{--<td rowspan="2"></td>--}%
            %{--<td><font color="red">原商品</font></td>--}%
            %{--<td >--}%
            %{--${returnGoods.daiFaGoods.market} - ${returnGoods.daiFaGoods.floor}--}%
            %{--</td>--}%
            %{--<td>${returnGoods.daiFaGoods.stalls}</td>--}%
            %{--<td>${returnGoods.daiFaGoods.goods_sn}</td>--}%
            %{--<td>${returnGoods.daiFaGoods.spec}</td>--}%
            %{--<td>${returnGoods.daiFaGoods.num}</td>--}%
            %{--<td>${returnGoods.daiFaGoods.actual_price}</td>--}%
            %{--<td>--}%
            %{--<g:returnGoods status="${returnGoods.status}" />--}%


            %{--</td>--}%
            %{--</tr>--}%
                <tr class="daiFaGoods" style="background-color: lavender">
                    <td colspan="2"><font color="red">

                        <g:if test="${returnGoods.type == '0'}">退货</g:if>
                        <g:if test="${returnGoods.type == '1'}">换货</g:if>

                    </font></td>
                    <td>
                        ${returnGoods.market} - ${returnGoods.floor}
                    </td>
                    <td>${returnGoods.stalls}</td>
                    <td>${returnGoods.goods_sn}</td>
                    <td>${returnGoods.spec}</td>
                    <td>${returnGoods.return_num}</td>
                    <td>
                        会员期望退回：<g:getMemberReturnFee id="${returnGoods.id}"/>
                        <br/>
                        %{--根据M表申请的状态来决定是否显示实际退回的价格给会员看到--}%
                        %{--<g:if test="${returnOrder.status == '2'}">--}%
                            %{--档口实际退回：<span style="color: red">${returnGoods.actual_return_fee}</span>--}%
                        %{--</g:if>--}%



                        <g:if test="${(g.getMemberReturnFee([id:returnGoods.id]) as BigDecimal)>returnGoods.actual_return_fee}">

                            <g:if test="${returnGoods.actual_return_fee!=0}">
                                <font style="color: red;">无法按您指定价格退货,档口允许退货价为${returnGoods.actual_return_fee}</font>
                                <script>
                                    $("#isfail").val("1")
                                </script>
                            </g:if>
                            <g:if test="${returnGoods.actual_return_fee!=0}">
                                <font style="color: red;">无法按您指定价格退货</font>
                            </g:if>


                        </g:if>


                        <br/>
                        <g:if test="${returnGoods.status == '10'}">
                            <span style="color: red">不要了</span>

                        </g:if>
                        <g:if test="${returnGoods.status == '9'}">
                            <span style="color: red">已寄回</span>

                        </g:if>
                    </td>
                    <input type="hidden" name="returnGoods_id" value="${returnGoods.id}"/>
                    <td>
            <g:if test="${returnGoods.status != '4'}">

                ${returnGoods.reason}
            </g:if>


                    </td>
                    <g:if test="${returnGoods.status == '6'}">
                        <script>
                            $("#isfail").val("1")
                            $('.jihui').show();
                        </script>
                    </g:if>
                </tr>
            </g:each>
            </tbody>
        </table>
        <br/>
<g:if test="${kingsReturnOrder  }">
    <div id="returnGoOnDiv" style="display:none;">
        <h4>退货不成商品降低5元继续退货,支付2元手续费</h4>
        <div id="safepassSpan" >支付密码：<input  name="safepass" id="safepass_text1" type="password" >
            <g:if test="${!session.loginPOJO.user.safepass}"><a target="_blank" style="color: red" href="<%=request.getContextPath()%>/memberUser/toSetSafepass">您还没有设置支付密码，点此设置</a></g:if>
                <a  href="javascript:void(0)" onclick="returnGoOn()"  class=" btn btn-large btn-primary">提交继续退货</a>
        </div>

    </div>


    <script>
        if($("#isfail").val()=="1"){
            $("#returnGoOnDiv").show()
        }else{
            $("#returnGoOnDiv").hide()
        }

    </script>

    </g:if>

    </div>

</g:if>



<h4>会员下单情况
</h4>

<div class="bs-docs-example">
    <table class="table table-bordered">
        <thead>
        <tr>
            <th rowspan="2">申请原因</th>
            <th></th>
            <th style="width: 200px;">市场-档口</th>
            <th>商品货号</th>
            <th>规格（颜色/尺码）</th>
            <th>退货件数</th>
            <th>价格</th>
            <th></th>
        </tr>
        </thead>
        <tbody>
        <g:each in="${returnOrder.returnGoods}" status="i" var="returnGoods">
            <tr class="daiFaGoods">
                <td rowspan="2">

                    ${returnGoods.returnReason}
                </td>
                <td><font color="red">原商品</font></td>
                <td>
                    ${returnGoods.daiFaGoods.market} - ${returnGoods.daiFaGoods.floor} -${returnGoods.daiFaGoods.stalls}
                </td>
                <td>${returnGoods.daiFaGoods.goods_sn}</td>
                <td>${returnGoods.daiFaGoods.spec}</td>
                <td>${returnGoods.daiFaGoods.num}</td>
                <td>${returnGoods.daiFaGoods.actual_price}</td>
                <td></td>
            </tr>
            <tr class="daiFaGoods" style="background-color: lavender">

                <td><font color="red">
                    <g:if test="${returnGoods.type == '0'}">退货</g:if>
                    <g:if test="${returnGoods.type == '1'}">换货</g:if>
                </font></td>
                <td>
                    ${returnGoods.market} - ${returnGoods.floor} - ${returnGoods.stalls}
                </td>
                <td>${returnGoods.goods_sn}</td>
                <td>${returnGoods.spec}</td>
                <td>${returnGoods.return_num}</td>
                <td>

                    会员期望退回：${returnGoods.return_fee}
                <td></td>
            </tr>
        </g:each>
        </tbody>
    </table>

</div>


<h4>下单人信息</h4>

<div class="bs-docs-example">
    <table class="table table-bordered">

        <tbody>

        <tr>
            <td>
                下&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;单&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;人：
                ${returnOrder.sendperson}
                <br/>
                发&nbsp;&nbsp;&nbsp;件&nbsp;&nbsp;地&nbsp;&nbsp;&nbsp;址：
                ${returnOrder.sendaddress}
                <br/>
                联系手机/电话：
                ${returnOrder.sendcontphone}
                <br/>
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

                物流单号：${returnOrder.wuliu_sn}
                <br>
                物流公司：${returnOrder.wuliu}
                <br>
                快递单图片：
                <g:if test="${returnOrder.wuliupic}">
                    <a href="${returnOrder.wuliupic}" target="blank">
                        <img src="${returnOrder.wuliupic}" style="display: inline;max-width: 100px"/>
                    </a>
                </g:if>
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
                ${returnOrder.daiFaOrder.reperson}
                &nbsp;&nbsp;
                联系手机/电话：
                ${returnOrder.daiFaOrder.contphone}
                <br/>
                收件地区：
                <g:areaName area_id="${returnOrder.daiFaOrder.area_id}"/>
                <br/>
                收件地址：
                ${returnOrder.daiFaOrder.address}
                <br/>
                快&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;递：
                ${returnOrder.daiFaOrder.wuliu}
                <g:if test="${returnOrder.shipTime}">
                    <br/>
                    发货时间：
                    ${returnOrder.shipTime.toString()[0..18]}
                    <br/>
                    发货单号：
                    ${returnOrder.h_wuliu_sn}
                </g:if>
                <br/>
            </td>
        </tr>

        </tbody>
    </table>
</div>

<h4>状态</h4>

<div class="bs-docs-example">
    <table class="table table-bordered">

        <tbody>

        <tr>
            <td>
                申请状态：
                <strong><g:if test="${returnOrder.status == '0'}">未支付</g:if>
                    <g:elseif test="${returnOrder.status == '2'}">处理结束</g:elseif>
                    <g:else>
                        <g:if test="${returnOrder.needTui == '2'}">已退款</g:if>
                        <g:else>
                            <g:if test="${returnOrder.isScan != '1'}">没收到包裹</g:if>
                            <g:else>处理中</g:else>

                        </g:else>

                    </g:else></strong>

                <br/>
            </td>
        </tr>

        </tbody>
    </table>

</div>

<h4>支付款项</h4>

<div class="bs-docs-example">
    <table class="table table-bordered">

        <tbody>

        <tr>
            <td>
                补&nbsp;&nbsp;差&nbsp;&nbsp;价：${returnOrder.chajia}
                <br/>
                手&nbsp;&nbsp;续&nbsp;&nbsp;费：${returnOrder.serviceFee} （手续费每件2元）
                <br/>
                运&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;费：${returnOrder.shipFee}
                <br/>
                支付总计：${returnOrder.totalFee}
            </td>
        </tr>

        </tbody>
    </table>
</div>









<g:if test="${returnOrder.status == '0'}">
    选择支付方式： <input type="radio" id="yuePay" name="pay_type" value="0" onclick="$('#safepassSpan').show();
$('#zfbSpan').hide()" checked/>余额支付 &nbsp;&nbsp;
    %{--<input type="radio" id="zfbPay" name="pay_type" value="1" onclick="$('#safepassSpan').hide();--}%
    %{--$('#zfbSpan').show()"/>--}%
    %{--支付宝支付--}%
    <br/>
    <br/>

    <div id="safepassSpan">支&nbsp;&nbsp;付&nbsp;&nbsp;&nbsp;密&nbsp;&nbsp;码：<input name="safepass" id="safepass_text"
                                                                                 type="password">
        <g:if test="${!session.loginPOJO.user.safepass}"><a target="_blank" style="color: red"
                                                            href="<%=request.getContextPath()%>/memberUser/toSetSafepass">您还没有设置支付密码，点此设置</a></g:if>
        <div>
            <a href="javascript:void(0)" onclick="yuePay1()" class=" btn btn-large btn-primary">支付</a>
            <g:link controller="memberDaiFaOrder" action="saleReturnList" params="${params}"
                    class=" btn btn-large ">返回</g:link>
        </div>
    </div>

    <div id="zfbSpan" style="display: none">
        <g:link target="_blank" action="reqPay" controller="memberAlipay"
                params="[orderSN: returnOrder.orderSN, payType: '3', body: returnOrder.id]" onclick="daiFaPay()"
                class=" btn btn-large btn-primary">支付</g:link>
        <g:link controller="memberDaiFaOrder" action="saleReturnList" params="${params}"
                class=" btn btn-large ">返回</g:link>
    </div>
</g:if>
<g:else>
    <g:link controller="memberDaiFaOrder" action="saleReturnList" params="${params}" class=" btn btn-large ">返回</g:link>
</g:else>

</g:form>
<div id="daiFaPayModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true" style="display: none;">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>

        <h3 id="myModalLabel">支付代发订单</h3>
    </div>


    <div class="modal-footer">
        <g:link class="btn btn-primary" controller="memberDaiFaOrder" params="${params}"
                action="saleReturnShow">支付完成</g:link>
    </div>
</div>

<form method="post" id="commonActionForm" target="innerFrame">
    <input type='hidden' name='id' id='id' class='key'>
    <input type='hidden' name='safepass' id='safepass' class='key'>
</form>
<iframe id="innerFrame" name="innerFrame" height="0" frameborder="0"></iframe>
<g:form action="saleReturnShow" params="${params}" method="post" class="commonListForm">
</g:form>
<g:render template="/layouts/footer"/>
</body>
</html>