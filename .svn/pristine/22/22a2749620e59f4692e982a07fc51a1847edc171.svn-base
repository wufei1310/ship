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
    <h4>
        您退回的物流单号是：${params.wuliu_sn}
        <br/> <br/>
        <g:if test="${list.size()>0}">
            该物流单号包裹我们已经收到，根据包裹已为您查出可能你要退货的是如下订单，请进入订单核实并提交
        </g:if>
        <g:else>
            还未收到该物流单号包裹，请进入<g:link controller="memberDaiFaOrder" action="list"
                                   class="btn btn-success ">我的订单</g:link>直接提交退换货申请

        </g:else>

    </h4>
</div>



<div class="bs-docs-example">
    <table class="table table-bordered">
        <thead>
        <tr>
            <th style="">订单号</th>
            <th>收货人信息</th>
            <th>总费用</th>
            <th>补款费</th>
            <th>快递公司</th>
            <th>物流单号</th>
            <th>创建时间</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>

        <g:each in="${list}" status="i" var="daiFaOrder">
            <tr>
                <td>

                    <g:link target="_blank" controller="memberDaiFaOrder" action="show" id="${daiFaOrder.id}">${daiFaOrder.orderSN}</g:link>
                    <br/>
                </td>
                <td>${daiFaOrder.reperson}</td>
                <td>${daiFaOrder.totalFee}</td>
                <td>${daiFaOrder.diffFee}</td>
                <td><order:wuliu wuliu="${daiFaOrder.wuliu}"/></td>
                <td>${daiFaOrder.wuliu_no}</td>
                <td>${daiFaOrder.dateCreated.toString()[0..18]}</td>
                <td>
                <!--                  <g:link controller="memberDaiFaOrder" action="show" id="${daiFaOrder.id}"
                                              params="${params}" class="btn">查看</g:link>-->
                    <g:if test="${daiFaOrder.status == 'waitpay'}">
                        <g:link controller="memberDaiFaOrder" action="update" id="${daiFaOrder.id}" params="${params}"
                                class="btn">修改</g:link>
                        <a class="btn" href="javascript:void(0)"
                           onclick=" toActionCom({id: '${daiFaOrder.id}'}, '<%=request.getContextPath()%>/memberDaiFaOrder/delete', '确定删除该订单吗？')">删除</a>
                    </g:if>
                    <g:if test="${daiFaOrder.status == 'shipped'}">

                        <g:link controller="memberDaiFaOrder" action="toSaleExchange" id="${daiFaOrder.id}"
                                params="${params}" class="btn">申请退换货</g:link>

                    </g:if>
                </td>
            </tr>
        </g:each>

</div>
</tbody>
</table>
</div>

<g:render template="/layouts/footer"/>
</div> <!-- /container -->

</body>
</html>
