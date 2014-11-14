<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>金士代发</title>
</head>

<body>
<div class="container body" id="page">

<g:if test="${flash.message}">
    <script>
        alert('${flash.message}')
    </script>
</g:if>

<div id="beizhuModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true" style="display: none;">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>

        <h3 id="myModalLabel">修改订单备注
            <br/>
            <span style="color:red;font-size:0.6em;line-height: 5px;">下单后无法更换档口，请不要在备注栏填写更换档口信息，会耽误发货速度。当缺货时，商品列表中将提供更换档口操作!</span>
        </h3>
    </div>

    <div class="modal-body">
        <g:form class="beizhuForm form-horizontal" data-validate="parsley" action="updateBeiZhu" target="innerFrame">
            <input type='hidden' name='id'>

            <div class="control-group">
                <label class="control-label">订单备注

                </label>

                <div class="controls">
                    <textarea maxlength="200" name="beizhu" style="width: 250px;height: 100px;"></textarea>
                </div>
            </div>

            <div class="control-group">
                <div class="controls">
                    <button type="button" class="btn btn-primary btn-large"
                            onclick="toActionFormCom('beizhuForm')">确定</button>
                </div>
            </div>
        </g:form>
    </div>
</div>

<div id="taobaoModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true" style="display: none;">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>

        <h3 id="myModalLabel">填写淘宝订单号</h3>
    </div>

    <div class="modal-body">
        <g:form class="taobaoForm form-horizontal" data-validate="parsley" action="toAdd"
                controller="memberTaoBaoOrder">
            <div class="control-group">
                <label class="control-label">淘宝订单号</label>

                <div class="controls">
                    <input data-required-message="淘宝订单号不能为空" data-required="true" type="text" name="tid">
                </div>
            </div>

            <div class="control-group">
                <div class="controls">
                    <button type="submit" class="btn btn-primary btn-large">提交</button>
                </div>
            </div>
        </g:form>
    </div>

</div>

<div class="page-header">
    <h3>${session.loginPOJO.user.email}的代发订单（点击查看<g:link controller="memberTranLog" target="_blank"
                                                         action="list">订单金额流水</g:link>）
    <!--          <a href="#taobaoModal" data-toggle="modal" class="btn btn-primary btn-large pull-right">同步淘宝订单</a>-->
    <g:link controller="memberDaiFaOrder" action="add" class="btn btn-primary btn-large pull-right">创建新订单</g:link>

    </h3>
</div>

<div class="well well-large">
    <form class="form-inline">
        订单号：
        <input value="${params.orderSN}" name="orderSN" type="text" class="input-small" placeholder="">
        收件人：
        <input value="${params.reperson}" name="reperson" type="text" class="input-small" placeholder="">
        收货人电话：
        <input name="contphone" type="text" class="input-small" placeholder="">
        %{--状态：--}%
        <g:select style="display:none;" class="input-medium" value="${params.status}" name="status" optionKey="status"
                  optionValue="queryShow"
                  from="${[[status: "", queryShow: "全部"], [status: "waitpay", queryShow: "未付款"], [status: "haspay", queryShow: "已付款，等待发货"], [status: "shipped", queryShow: "已发货"], [status: "error", queryShow: "等待补款/缺货"], [status: "close", queryShow: "已取消"], [status: "diffship", queryShow: "需要补运费"]]}"/>

        <button type="submit" class="btn ">查询</button>
        <g:link class="btn ${params.status == 'waitpay' ? 'btn-primary' : ''}" controller="memberDaiFaOrder"
                action="list" params="[status: 'waitpay']">未付款(${waitpay})</g:link>
        <g:link class="btn ${params.status == 'error' ? 'btn-primary' : ''}" controller="memberDaiFaOrder" action="list"
                params="[status: 'error']">补款/缺货(${error})</g:link>
        <g:link class="btn ${params.status == 'diffship' ? 'btn-primary' : ''}" controller="memberDaiFaOrder"
                action="list" params="[status: 'diffship']">补运费(${diffship})</g:link>
        <g:link class="btn ${params.status == null ? 'btn-primary' : ''}" controller="memberDaiFaOrder"
                action="list">全部</g:link>
    </form>

</div>


<div class="bs-docs-example">
    <table class="table table-bordered">
        <thead>
        <tr>
            <th> 全选　<input  type="checkbox" onclick="checkAll(this)"/>　</th>
            <th style="">订单号</th>
            <th>收货人信息</th>
            <th>总费用</th>
            <th>补款费</th>
            <th>快递公司</th>
            <th>物流单号</th>
            <th>点击查看取货详情</th>
            <th>支付时间</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>

        <g:each in="${list}" status="i" var="daiFaOrder">
            <tr>
                <td><input <g:if test="${daiFaOrder.status != 'waitpay'}">disabled</g:if>  onclick="waitpayorderid()" type="checkbox" class="waitpayorderid" name="waitpayorderid" value="${daiFaOrder.id}" amount="${daiFaOrder.totalFee}"></td>
                <td>${daiFaOrder.orderSN}

                </td>
                <td>${daiFaOrder.reperson}</td>
                <td>${daiFaOrder.totalFee}</td>
                <td>${daiFaOrder.diffFee}</td>
                <td><order:wuliu wuliu="${daiFaOrder.wuliu}"/></td>
                <td>${daiFaOrder.wuliu_no}</td>
                <td>
                    <!--                  <g:link controller="memberDaiFaOrder" action="show" id="${daiFaOrder.id}"
                                                  params="${params}">点击查看取货进展</g:link>-->
                    <!--                  <font<g:if
                            test="${daiFaOrder.status == 'error' || daiFaOrder.status == 'diffship'}">color="red"</g:if>><order:orderStatusDic
                            status="${daiFaOrder.status}"/></font>-->

                    <g:if test="${daiFaOrder.status == 'waitpay'}">
                        <g:link controller="memberDaiFaOrder" action="show" id="${daiFaOrder.id}" params="${params}"
                                class="btn btn-danger">该订单还未付款</g:link>
                    </g:if>
                    <g:if test="${daiFaOrder.status == 'waitaccept'}">

                        <g:if test="${daiFaOrder.isCanExport == '3'}">
                            <g:link controller="memberDaiFaOrder" action="show" id="${daiFaOrder.id}" params="${params}"
                                    class="">正在移交物流公司</g:link>
                        </g:if>
                        <g:else>
                            <g:link controller="memberDaiFaOrder" action="show" id="${daiFaOrder.id}" params="${params}"
                                    class=" ">已分配专人处理</g:link>
                        </g:else>




                    </g:if>
                    <g:if test="${daiFaOrder.status == 'partaccept'}">
                        <g:link controller="memberDaiFaOrder" action="show" id="${daiFaOrder.id}" params="${params}"
                                class="">部分商品已完成拿货</g:link>


                    </g:if>
                    <g:if test="${daiFaOrder.status == 'allaccept'}">
                        <g:link controller="memberDaiFaOrder" action="show" id="${daiFaOrder.id}" params="${params}"
                                class="">已完成验货，马上发货</g:link>

                    </g:if>
                    <g:if test="${daiFaOrder.status == 'shipped'}">
                        <g:link controller="memberDaiFaOrder" action="show" id="${daiFaOrder.id}" params="${params}"
                                class="">已为您发货</g:link>
                    </g:if>
                    <g:if test="${daiFaOrder.status == 'error'}">
                        <g:link controller="memberDaiFaOrder" action="show" id="${daiFaOrder.id}" params="${params}"
                                class="btn btn-danger">等待补款/缺货</g:link>

                    </g:if>
                    <g:if test="${daiFaOrder.status == 'diffship'}">
                        <g:link controller="memberDaiFaOrder" action="show" id="${daiFaOrder.id}" params="${params}"
                                class="btn btn-danger">需要补运费</g:link>

                    </g:if>
                    <g:if test="${daiFaOrder.status == 'close'}">
                        <g:link controller="memberDaiFaOrder" action="show" id="${daiFaOrder.id}" params="${params}"
                                class="">您已取消该订单</g:link>


                    </g:if>
                    <g:if test="${daiFaOrder.status == 'problem'}">
                        <g:link controller="memberDaiFaOrder" action="show" id="${daiFaOrder.id}" params="${params}"
                                class="">已分配专人处理</g:link>

                    </g:if>

                    <g:if test="${daiFaOrder.status == 'kill'}">

                        订单已中止发货
                    </g:if>

                    <g:if test="${daiFaOrder.type == '1'}">
                        <br/>
                        <g:link controller="memberDaiFaOrder" action="saleReturnList"
                                                                   target="_blank"
                                                                   params="[orderSN: daiFaOrder.orderSN]">查看退换货信息</g:link></g:if>


                </td>
                <td>

                    <g:if test="${daiFaOrder.payTime}">
                        ${daiFaOrder.payTime.toString()[0..18]}
                    </g:if>

                </td>
                <td>
                <!--                  <g:link controller="memberDaiFaOrder" action="show" id="${daiFaOrder.id}"
                                              params="${params}" class="btn">查看</g:link>-->
                    <g:if test="${daiFaOrder.status == 'waitpay'}">
                        <g:link controller="memberDaiFaOrder" action="update" id="${daiFaOrder.id}" params="${params}"
                                class="btn btn-small">修改</g:link>
                        <a class="btn btn-small" href="javascript:void(0)"
                           onclick=" toActionCom({id: '${daiFaOrder.id}'}, '<%=request.getContextPath()%>/memberDaiFaOrder/delete', '确定删除该订单吗？')">删除</a>
                    </g:if>
                    %{--<g:if test="${daiFaOrder.status != 'waitpay' && daiFaOrder.status != 'shipped' && daiFaOrder.status != 'close' && daiFaOrder.status != 'kill' }">--}%
                        %{--<a class="btn" href="#beizhuModal" data-toggle="modal"--}%
                           %{--onclick="updateBeiZhu('${daiFaOrder.id}', '${daiFaOrder.senddesc}')">修改订单备注</a>--}%
                    %{--</g:if>--}%
                    <g:if test="${daiFaOrder.status == 'shipped'}">

                        <g:if test="${daiFaOrder.type == '0'}">
                        <g:link controller="memberDaiFaOrder" action="toSaleExchange" id="${daiFaOrder.id}"
                                params="${params}" class="btn btn-small">申请退换货</g:link>
                            </g:if>

                    </g:if>


                    <g:if test="${daiFaOrder.status == 'problem'||daiFaOrder.status == 'diffship'||daiFaOrder.status == 'error'||daiFaOrder.status == 'allaccept'||daiFaOrder.status == 'partaccept'||daiFaOrder.status == 'waitaccept'}">

                        <button onclick="beforeKill('${daiFaOrder.id}')" class="btn btn-small" type="button">紧急中止发货</button>
                    </g:if>

                </td>
            </tr>
        </g:each>
        <tr>
            <td colspan="10">

                合并订单待支付金额<span style="color: red;font-size:1.2em;" class="waitpayamount">0</span>元。
                <a class="btn btn-success" href="#payAll" data-toggle="modal"  >合并付款</a>
            </td>

        </tr>

        <div id="payAll" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
             aria-hidden="true" style="display: none;">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>

                <h3 id="myModalLabel"> 合并订单待支付金额<span style="color: red;font-size:1.2em;" class="waitpayamount">0</span>元。</h3>
            </div>

            <div class="modal-body">
                <g:form style="margin:0" onsubmit="return isEnterPass();"  id="loginForm" name="loginForm" data-validate="parsley"
                        url="[controller: 'memberDaiFaOrder', action: 'payDaiFaAllOrder']"
                        method="post" class="form-horizontal">
                    <input type="hidden" id="waitpayorderidstr" name="waitpayorderidstr" value="">


                    %{--<div class="control-group">--}%
                        %{--<label class="control-label"></label>--}%

                        %{--<div class="controls">--}%
                            %{--<input type="radio" id="yuePay" name="pay_type" value="0"--}%
                                   %{--onclick="$('#safepassSpan').show();--}%
                                   %{--$('#zfbSpan').hide()" checked/>余额支付 &nbsp;&nbsp;--}%
                            %{--<input type="radio" id="zfbPay" name="pay_type" value="1"--}%
                                   %{--onclick="$('#safepassSpan').hide();--}%
                                   %{--$('#zfbSpan').show()"/>支付宝支付--}%
                        %{--</div>--}%
                    %{--</div>--}%
                    <div class="control-group">
                         <div class="controls">
                             您的账户余额:<span id="yuelast" style="color: red"></span>
                             <g:link target="_blank" controller="memberAlipayRemit" action="toAdd">去充值</g:link>
                             <script>

                                 <g:if test="${session.loginPOJO?.user && session.loginPOJO.user.user_type=='member'}">
                                 $.ajax(
                                         { url: "<%=request.getContextPath()%>/memberUser/selAccount",
                                             cache: false,
                                             success: function (msg) {
                                                 $("#yuelast").html(msg)
                                             }
                                         });
                                 </g:if>


                             </script>
                         </div>
                        <div class="controls" style="margin-top: 10px;">
                             <div id="safepassSpan" >
                                 请输入支付密码：
                                 <br/>
                                 <input class="input-medium"  name="safepass" id="safepass_text" type="password" >
                                 <g:if test="${!session.loginPOJO.user.safepass}"><a target="_blank" style="color: red" href="<%=request.getContextPath()%>/memberUser/toSetSafepass">您还没有设置支付密码，点此设置</a></g:if>
                                 <br/>
                                 <button type="submit" class="btn btn-large btn-primary">使用余额合并支付</button>
                            </div>
                            <div id="zfbSpan" style="display: none">
                                <g:link target="_blank" action="reqPay" controller="memberAlipay"  onclick="daiFaPay()"  class=" btn btn-large btn-primary">支付</g:link>
                            </div>

                        </div>
                    </div>

                </g:form>
            </div>
                <!--    <div class="modal-footer">
          <button class="btn" data-dismiss="modal">关闭</button>
          <button class="btn btn-primary">Save changes</button>
        </div>-->
            </div>
          </div>
        </tbody>
    </table>
</div>

<div class="row-fluid">
    <div class="span12">
        <div class="pagination pagination-right new-pagination">

            <g:if test="${total != 0}">
                <g:paginate total="${total}" params="${params}" action="list" update="page"/>
            </g:if>

            <span class="currentStep">共${total}条数据</span>
        </div>

    </div>
</div>

<form method="post" id="commonActionForm" target="innerFrame">
    <input type='hidden' name='id' id='id' class='key'>
</form>
<iframe id="innerFrame" name="innerFrame" height="0" frameborder="0"></iframe>
<g:form action="${actionName}" params="${params}" method="post" class="commonListForm">
</g:form>
<g:render template="/layouts/footer"/>
<%
    String baseUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath()
%>
<script>

    function beforeKill(orderId){
        if(confirm("确认强制停止订单吗？我们将退回您的货款及物流费，但物流公司会收取3.5元单据费，且您已缴纳的1元/件代发费将不会退回!")){

            $.ajax({
                url: '${baseUrl}/memberDaiFaOrder/killOrder',
                data: 'orderId='+orderId,
                success:function(data){
                    if(data=="1"){
                        window.location.reload();
                    }

                }
            });

        } else{
            return false;
        }
    }


    function waitpayorderid(){

        var amount = 0
        var waitpayorderid = ""
        $(".waitpayorderid").each(function(){
            if($(this).attr("checked")=="checked"){
                waitpayorderid = waitpayorderid + "," +  $(this).val()
                amount = amount + parseFloat($(this).attr("amount"));
            }
        });
        $(".waitpayamount").html(amount)
        $("#waitpayorderidstr").val(waitpayorderid)
    }

    function isEnterPass(){
        var safepass_text = $("#safepass_text").val();
        if($.trim(safepass_text)==""){
            alert("支付密码不能为空！")
            return false;
        }
    }

    function updateBeiZhu(order_id, beizhu) {
        $('#beizhuModal').find("input[name='id']").val(order_id)
        $('#beizhuModal').find("textarea[name='beizhu']").val(beizhu)
    }


    function checkAll(o){
        var table = $(o).parents("table");
        if($(o).attr("checked")=="checked"){

            table.find(".waitpayorderid").each(function(){
                if($(this).attr("disabled")!="disabled"){
                   $(this).attr("checked","checked")
                }
            });

        }else{
            table.find(".waitpayorderid").removeAttr("checked")
        }
        waitpayorderid();
    }
</script>
</div> <!-- /container -->

</body>
</html>
