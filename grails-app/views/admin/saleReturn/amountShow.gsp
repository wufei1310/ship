
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="adminMain"/>
    <title>金士代发</title>
  </head>
  <body>
    <script>
        function beforeTuikuan(){
            if(window.confirm("将会退给会员${returnFee}元退款，确认吗？")){
                toActionFormCom('checkSaleReturnForm')  
            }
        }
    </script>

    
    
    <div class="container body">
      <g:if test="${flash.message}">
         <script>
         alert('${flash.message}')
        </script>
      </g:if>

      <div class="page-header">
        <h3>退款审核(<small>订单号：</small>${returnOrder.orderSN}) </h3>
      </div>
      <g:form name="checkSaleReturnForm"  data-validate="parsley" class="form-signin checkSaleReturnForm" action="saleReturnAmount" method="post" target="innerFrame">
        <input type="hidden" name="id" value="${returnOrder.id}">

          <g:if test="${memberReturnOrder}">
              <h4>会员提交退货申请信息(退款账户：<g:link target="_blank" controller="adminTranLog" action="list" params="[userid:memberReturnOrder.add_user]">${memberReturnOrder.add_user}</g:link>)</h4>
              <div class="bs-docs-example">
                  <table class="table table-bordered">
                      <thead>
                      <tr>
                          <th colspan="2" style="color: red" ></th>
                          <th style="width: 200px;">市场位置</th>
                          <th style="width: 200px;">档口号</th>
                          <th >商品货号</th>
                          <th>规格（颜色/尺码）</th>
                          <th>退货货件数</th>
                          <th>单件拿货价格</th>
                          <th>退货信息</th>
                      </tr>
                      </thead>
                      <tbody>
                      <g:each in="${memberReturnOrder.returnGoods}" status="i" var="returnGoods">
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

                                  <g:if test="${returnGoods.type=='0'}">退货</g:if>
                                  <g:if test="${returnGoods.type=='1'}">换货</g:if>

                              </font></td>
                              <td >
                                  ${returnGoods.market} - ${returnGoods.floor}
                              </td>
                              <td>${returnGoods.stalls}</td>
                              <td>${returnGoods.goods_sn}</td>
                              <td>${returnGoods.spec}</td>
                              <td>${returnGoods.return_num}  </td>
                              <td>
                                  商品拿货价格：${returnGoods.daiFaGoods.actual_price}
                                  <br/>
                                  会员期望退回：${returnGoods.return_fee}
                              </td>
                              <input type="hidden" name="returnGoods_id" value="${returnGoods.id}"/>
                              <td>${returnGoods.reason}</td>
                          </tr>
                      </g:each>
                      </tbody>
                  </table>


              </div>

          </g:if>




          <h4>商品信息</h4>
          <div class="bs-docs-example">
              <table class="table table-bordered">
                  <thead>
                  <tr>
                      <th colspan="2" style="color: red" >收货的包裹退货情况</th>
                      <th style="width: 200px;">市场位置</th>
                      <th style="width: 200px;">档口号</th>
                      <th >商品货号</th>
                      <th>规格（颜色/尺码）</th>
                      <th>退货货件数</th>
                      <th>单件拿货价格</th>
                      <th>退货信息</th>
                  </tr>
                  </thead>
                  <tbody>
                  <g:each in="${returnOrder.returnGoods}" status="i" var="returnGoods">
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

                              <g:if test="${returnGoods.type=='0'}">退货</g:if>
                              <g:if test="${returnGoods.type=='1'}">换货</g:if>

                          </font></td>
                          <td >
                              ${returnGoods.market} - ${returnGoods.floor}
                          </td>
                          <td>${returnGoods.stalls}</td>
                          <td>${returnGoods.goods_sn}</td>
                          <td>${returnGoods.spec}</td>
                          <td>${returnGoods.return_num}  </td>
                          <td>
                              商品拿货价格：${returnGoods.daiFaGoods.actual_price}
                              <br/>
                              会员期望退回：<g:getMemberReturnFee id="${returnGoods.id}"/>
                              <br/>
                                档口实际退回：<span style="color: red">${returnGoods.actual_return_fee}</span>
                              <br/>
                              档口退货时间：${returnGoods.actual_returnTime}
                              <g:if test="${g.getMemberReturnFee([id:returnGoods.id])}">

                                      <g:if test="${(g.getMemberReturnFee([id:returnGoods.id]) as BigDecimal)>returnGoods.actual_return_fee}">
                                          <br/>
                                          <font style="color: red;">会员期望退回价格高于实际退回价</font>
                                         <g:set var="isHigh" value="1"/>
                                      </g:if>
                              </g:if>

                          </td>
                          <input type="hidden" name="returnGoods_id" value="${returnGoods.id}"/>
                          <td>${returnGoods.reason}</td>
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
                <td >
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
                <td >

                  物流单号：${returnOrder.wuliu_sn}
                  <br>
                  物流公司：${returnOrder.wuliu}
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
                <td >
                  申请状态：
                  <strong >
                    <g:if test="${returnOrder.status == '0'}">未支付</g:if>

                    <g:else>
                        <g:if test="${returnOrder.needTui == '1'}">等待退款</g:if>
                        <g:elseif test="${returnOrder.needTui == '2'}">已退款</g:elseif>
                        <g:elseif test="${returnOrder.status == '2'}">处理结束</g:elseif>
                        <g:else>
                            处理中
                        </g:else>

                    </g:else>
                </strong>
                  
                  <br/>

                    会员下单时间：<strong ><g:memberReturnOrderTime orderSN="${returnOrder.orderSN}"></g:memberReturnOrderTime></strong>
                    <br/>
                    包裹扫描时间：<strong ><shipSN:scanTime wuliu_sn="${returnOrder.wuliu_sn}"></shipSN:scanTime> </strong>
                    <br/>
                    K数据生成时间：<strong >${returnOrder.dateCreated} </strong>
                    <br/>
                    包裹关联单号：<strong ><shipSN:packOrderSN wuliu_sn="${returnOrder.wuliu_sn}"></shipSN:packOrderSN> </strong>
                    <br/>
                    M数据物流单号：
                    <strong >${memberReturnOrder?.wuliu_sn}</strong>
                    <g:if test="${returnOrder.check_time}">
                    退款人：<strong >${returnOrder.check_user.email}</strong>
                    <br/>
                    退款审核时间：
                    <strong >${returnOrder.check_time.toString()[0..18]}</strong>
                   <br/>
                      M数据物流单号：
                      <strong >${memberReturnOrder.wuliu_sn}</strong>
                  </g:if>
                </td>
              </tr>

            </tbody>
          </table>
            
        </div>
        
        <h4>款项</h4>
        <div class="bs-docs-example">
          <table class="table table-bordered">

            <tbody>

              <tr>
                <td >
                    <span style="color:red;font-size: 1.2em;">
                    <g:if test="${memberReturnOrder?.wuliu_sn==returnOrder?.wuliu_sn}">物流单号相同</g:if>
                    <g:else>物流单号不相同</g:else>
                    </span>
                    <br/>

                  退货退款：<strong id="goodsFee">${returnFee}</strong><br/>

                    手&nbsp;&nbsp;续&nbsp;&nbsp;费：${returnOrder.serviceFee} （手续费每件2元）
                    <br/>
                    运&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;费：${returnOrder.shipFee}
                    <br/>

                </td>
              </tr>

            </tbody>
          </table>
        </div>





          <input type='hidden' name='returnOrderOperType' id='returnOrderOperType' value='tui'>
          <g:if test="${returnOrder.ishuiyuanxiadan=='1'}">会员已经认领</g:if>



         <g:if test="${returnOrder.ishuiyuanxiadan=='1'&&returnOrder.needTui=='1'}">


             <g:if test="${isHigh=='1'}">
                 有商品退回价格低于会员期望价格，暂不能退款。等待会员降价退货处理
             </g:if>
             <g:else>
                 <a  href="javascript:void(0)" onclick="beforeTuikuan()"  class=" btn btn-large btn-primary">确认退款</a>
             </g:else>


         </g:if>
 <g:link controller="adminDaiFaOrder" action="saleReturnList"  params="${params}" class=" btn btn-large ">返回</g:link>
      
    </g:form>
      
      <form     method="post" id="commonActionForm" target="innerFrame">

                  <input type='hidden' name='id' id='id' class='key'>
                  <input type='hidden' name='safepass' id='safepass' class='key'>
        </form>
        <iframe id="innerFrame" name="innerFrame" height="0" frameborder="0"></iframe>
         <g:form  action="saleReturnList" params="${params}"   method="post" class="commonListForm">
        </g:form >
      <g:render template="/layouts/footer"/>
  </body>
</html>
