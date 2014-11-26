
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
    <title>金士代发</title>
  </head>
  <body>
    
<script>
        function daiFaPay(){
          $('#daiFaPayModal').modal({
                keyboard: false
               })
        }
        function yuePay1(){
            if($("#safepass_text").val()==""){
                  alert("支付密码不能为空")
                  return false;
              }
             toActionCom({id:'${returnOrder.id}',safepass:$("#safepass_text").val()},'<%=request.getContextPath()%>/memberDaiFaOrder/paySaleReturn')
        }

 
    </script>
    
    
    <div class="container body">
      <g:if test="${flash.message}">
         <script>
         alert('${flash.message}')
        </script>
      </g:if>

      <div class="page-header">
        <h3>退货申请(<small>订单号：</small>${returnOrder.daiFaOrder.orderSN})</h3>
      </div>
      <g:form >


      <g:if test="${kingsReturnOrder&&kingsReturnOrder.status=='2'}">
      <h4>真实退货情况</h4>
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
                      <td>会员期望退回：${returnGoods.actual_price}
                          <br/>
                          <g:if test="${kingsReturnOrder.status=='2'}">
                              实际退回：<span style="color: red">${returnGoods.actual_return_fee}</span>
                          </g:if>

                      </td>
                      <input type="hidden" name="returnGoods_id" value="${returnGoods.id}"/>
                      <td>${returnGoods.reason}</td>
                  </tr>
              </g:each>
              </tbody>
          </table>


      </div>

      </g:if>



        <h4>商品信息(<span style="color: red;">
        <g:if test="${returnOrder.isScan=='1'}">收到包裹</g:if><g:else>没收到包裹</g:else></span>)
        </h4>
        <div class="bs-docs-example">
          <table class="table table-bordered">
            <thead>
              <tr>
                <th rowspan="2">申请原因</th>
                  <th></th>
                <th style="width: 200px;">市场-档口</th>
                <th >商品货号</th>
                <th>规格（颜色/尺码）</th>
                <th>退货件数</th>
                <th>价格</th>
                <th>退货信息</th>
              </tr>
            </thead>
            <tbody>
              <g:each in="${returnOrder.returnGoods}" status="i" var="returnGoods">
                  <tr class="daiFaGoods" >
                    <td rowspan="2">

                       ${returnGoods.returnReason}
                    </td>
                    <td><font color="red">原商品</font></td>
                    <td >
                      ${returnGoods.daiFaGoods.market} - ${returnGoods.daiFaGoods.floor} -${returnGoods.daiFaGoods.stalls}
                    </td>
                    <td>${returnGoods.daiFaGoods.goods_sn}</td>
                    <td>${returnGoods.daiFaGoods.spec}</td>
                    <td>${returnGoods.daiFaGoods.num}</td>
                    <td>${returnGoods.daiFaGoods.actual_price}</td>
                      <td><g:returnGoods status="${returnGoods.status}" /></td>
                  </tr>
                  <tr class="daiFaGoods" style="background-color: lavender">
                    
                    <td><font color="red">
                        <g:if test="${returnGoods.type=='0'}">退货</g:if>
                        <g:if test="${returnGoods.type=='1'}">换货</g:if>
                    </font></td>
                    <td >
                      ${returnGoods.market} - ${returnGoods.floor} - ${returnGoods.stalls}
                    </td>
                    <td>${returnGoods.goods_sn}</td>
                    <td>${returnGoods.spec}</td>
                    <td>${returnGoods.return_num}</td>
                    <td>${returnGoods.return_fee}
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
                    <br>
                    快递单图片：
                    <g:if test="${returnOrder.wuliupic}">
                        <a href="${returnOrder.wuliupic}" target="blank">
                            <img src="${returnOrder.wuliupic}"  style="display: inline;max-width: 100px"/>
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
                <td >

                  收&nbsp;&nbsp;件&nbsp;&nbsp;人：
                  ${returnOrder.daiFaOrder.reperson}
                  &nbsp;&nbsp;
                  联系手机/电话：
                  ${returnOrder.daiFaOrder.contphone}
                  <br/>
                  收件地区：
                  <g:areaName area_id="${returnOrder.daiFaOrder.area_id}" />
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
                <td >
                  申请状态：
                  <strong ><g:returnOrder status="${returnOrder.status}" /></strong>
                  
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
                <td >
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
        选择支付方式： <input type="radio" id="yuePay" name="pay_type" value="0" onclick="$('#safepassSpan').show();$('#zfbSpan').hide()" checked/>余额支付 &nbsp;&nbsp;
                                <input type="radio" id="zfbPay" name="pay_type" value="1" onclick="$('#safepassSpan').hide();$('#zfbSpan').show()" />支付宝支付 
                                <br/>
                                <br/>
                               <div id="safepassSpan" >支&nbsp;&nbsp;付&nbsp;&nbsp;&nbsp;密&nbsp;&nbsp;码：<input  name="safepass" id="safepass_text" type="password" >
                                  <g:if test="${!session.loginPOJO.user.safepass}"><a target="_blank" style="color: red" href="<%=request.getContextPath()%>/memberUser/toSetSafepass">您还没有设置支付密码，点此设置</a></g:if>
                                <div>
                                  <a  href="javascript:void(0)" onclick="yuePay1()"  class=" btn btn-large btn-primary">支付</a>
                                  <g:link controller="memberDaiFaOrder" action="saleReturnList"  params="${params}" class=" btn btn-large ">返回</g:link>
                                </div>
                              </div>
                                <div id="zfbSpan" style="display: none">
                                    <g:link target="_blank" action="reqPay" controller="memberAlipay" params="[orderSN:returnOrder.orderSN,payType:'3',body:returnOrder.id]" onclick="daiFaPay()"  class=" btn btn-large btn-primary">支付</g:link>
                                    <g:link controller="memberDaiFaOrder" action="saleReturnList"  params="${params}" class=" btn btn-large ">返回</g:link>
                                </div>
        </g:if>
        <g:else>
            <g:link controller="memberDaiFaOrder" action="saleReturnList"  params="${params}" class=" btn btn-large ">返回</g:link>
        </g:else>

    </g:form>
      <div id="daiFaPayModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="display: none;">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
          <h3 id="myModalLabel">支付代发订单</h3>
        </div>




        <div class="modal-footer">
          <g:link class="btn btn-primary" controller="memberDaiFaOrder" params="${params}"  action="saleReturnShow">支付完成</g:link>
        </div>
      </div>
      <form     method="post" id="commonActionForm" target="innerFrame">
                  <input type='hidden' name='id' id='id' class='key'>
                  <input type='hidden' name='safepass' id='safepass' class='key'>
        </form>
        <iframe id="innerFrame" name="innerFrame" height="0" frameborder="0"></iframe>
         <g:form  action="saleReturnShow" params="${params}"   method="post" class="commonListForm">
        </g:form >
      <g:render template="/layouts/footer"/>
  </body>
</html>