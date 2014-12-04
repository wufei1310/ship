<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="adminMain"/>
    <title>金士代发</title>
</head>

<body>
<div class="container body" id="page">

    <g:if test="${flash.message}">
        <script>
            alert('${flash.message}')
        </script>
    </g:if>



    <div class="page-header">
        <h3>

            <g:if test="${params.orderfrom == 'kings'}">系统包裹退货申请</g:if>
            <g:if test="${params.orderfrom == 'member'}">会员提交退货申请</g:if>
            <g:link controller="adminEmail" action="updateReturnOrderKM" target="_blank" class="btn btn-primary btn-large pull-right">同步退货数据</g:link>
        </h3>
    </div>

    <div class="well well-large">
        <form class="form-inline">
            <input type="hidden" name="orderfrom" value="${params.orderfrom}">
            订单号：
            <input name="orderSN" type="text" class="" placeholder="" value="${params.orderSN}">
            状态：
            <g:select class="input-medium" value="${params.status}" name="status" optionKey="status"
                      optionValue="queryShow"
                      from="${[[status: "", queryShow: "全部"], [status: "0", queryShow: "未支付"], [status: "1", queryShow: "处理中"], [status: "2", queryShow: "处理结束"]]}"/>
            是否退款：
            <g:select class="input-medium" value="${params.needTui}" name="needTui" optionKey="status"
                      optionValue="queryShow"
                      from="${[[status: "", queryShow: "全部"], [status: "1", queryShow: "等待退款"], [status: "2", queryShow: "已退款"]]}"/>
            会员是否下单：
            <g:select class="input-medium" value="${params.ishuiyuanxiadan}" name="ishuiyuanxiadan" optionKey="status"
                      optionValue="queryShow"
                      from="${[[status: "", queryShow: "全部"], [status: "0", queryShow: "否"], [status: "1", queryShow: "是"]]}"/>

            <button type="submit" class="btn btn-primary">查询</button>
        </form>
    </div>


    <div class="bs-docs-example">
        <table class="table table-bordered">
            <thead>
            <tr>
                <th>订单号</th>
                <!--              <th>物流单号</th>
              <th>物流公司</th>-->
                <th>状态</th>
                %{--<th>是否退款</th>--}%
                <th>会员是否下单</th>

                <th>创建时间</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody>

            <g:each in="${list}" status="i" var="returnOrder">
                <tr>
                    <td>${returnOrder.orderSN}<g:if test="${returnOrder.flat == '1'}">(非平台订单)</g:if>
                    </td>
                    <!--              <td>${returnOrder.wuliu_sn}</td>
              <td>${returnOrder.wuliu}</td>-->
                    <td>
                    %{--status: ${returnOrder.status}--}%

                    %{--needTui: ${returnOrder.needTui}--}%

                    %{--isScan: ${returnOrder.isScan}--}%

                        %{--<br/>--}%

                        <g:if test="${params.orderfrom == 'kings'}">
                            <g:if test="${returnOrder.status == '0'}">未支付</g:if>

                            <g:else>
                                <g:if test="${returnOrder.needTui == '1'}">等待退款</g:if>
                                <g:elseif test="${returnOrder.needTui == '2'}">已退款</g:elseif>
                                <g:elseif test="${returnOrder.status == '2'}">处理结束</g:elseif>
                                <g:else>
                                    处理中
                                </g:else>

                            </g:else>
                        </g:if>
                        <g:if test="${params.orderfrom == 'member'}">
                            <g:if test="${returnOrder.status=='0'}">未支付</g:if>

                            <g:else>
                                <g:if test="${returnOrder.needTui=='2'}">已退款</g:if>
                                <g:elseif test="${returnOrder.status=='2'}">处理结束</g:elseif>
                                <g:else>
                                    <g:if test="${returnOrder.isScan!='1'}">没收到包裹</g:if>
                                    <g:else>处理中</g:else>

                                </g:else>


                            </g:else>
                        </g:if>
                    </td>
                    %{--<td>--}%
                    %{--<g:if test="${returnOrder.needTui=='1'}">等待退款</g:if>--}%
                    %{--<g:if test="${returnOrder.needTui=='2'}">已退款</g:if>--}%


                    %{--</td>--}%



                    <td>
                        <g:if test="${returnOrder.ishuiyuanxiadan == '0'}">否</g:if>
                        <g:if test="${returnOrder.ishuiyuanxiadan == '1'}">会员已经下单</g:if>

                    </td>

                    <td>${returnOrder.dateCreated.toString()[0..18]}</td>
                    <td>

                        <g:if test="${params.orderfrom == 'kings'}">

                            <g:link controller="adminDaiFaOrder" action="saleReturnAmountShow" id="${returnOrder.id}"
                                    params="${params}" class="btn">查看</g:link>

                        </g:if>
                        <g:if test="${params.orderfrom == 'member'}">
                            <g:link controller="adminDaiFaOrder" action="saleReturnShow" id="${returnOrder.id}"
                                    params="${params}" class="btn">查看</g:link>

                        </g:if>

                    </td>
                </tr>
            </g:each>

            </tbody>
        </table>
    </div>

    <div class="row-fluid">
        <div class="span12">
            <div class="pagination pagination-right new-pagination">

                <g:if test="${total != 0}">
                    <g:paginate total="${total}" params="${params}" action="saleReturnList" update="page"/>
                </g:if>

                <span class="currentStep">共${total}条数据</span>
            </div>

        </div>
    </div>

    <g:render template="/layouts/footer"/>

</div> <!-- /container -->

</body>
</html>
