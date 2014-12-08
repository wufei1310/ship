<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
    <title>金士代发</title>
  </head>
  <body>
    
    <script>


        function confirmActualPrice(price,o,changeStallsNum){
            var actual_price =  $(o).find(".actual_price").val();
            var goods_sn =   $(o).find(".goods_sn").val();
            var stalls =   $(o).find(".stalls").val();




            if(changeStallsNum>=2){
                var safepass  =   $(o).find(".safepass").val();
                if($.trim(safepass)==""){
                    alert("支付密码不能为空！")
                    return false;
                }
            }


            if($.trim(actual_price)==""){
                alert("单件价格不能为空！")
                return false;
            }

            if($.trim(goods_sn)==""){
                alert("货号不能为空！")
                return false;
            }

            if($.trim(stalls)==""){
                alert("档口不能为空！")
                return false;
            }


            if(actual_price>price){
                if(confirm("新档口拿货价格比原档口价格高，更换档口后需要补交货款，确认提交吗?")){
                    return true;
                }else{
                    return false;
                }
            }
            if(actual_price<price){
                alert("新档口拿货价格比原档口价格低，更换档口后系统将退还差额到您的会员账户！");
                return true;
            }
            return true;


        }

        function daiFaPay(){
          $('#daiFaPayModal').modal({
                keyboard: false
               })
        }
        function yuePay(){
             if($("#safepass_text").val()==""){
                  alert("支付密码不能为空")
                  return false;
              }else{           
                toActionCom({id:'${order.id}',safepass:$("#safepass_text").val()},'<%=request.getContextPath()%>/memberDaiFaOrder/payDaiFaOrder')
              }
        }
        
        function chaPay(){
             if($("#safepass_text").val()==""){
                  alert("支付密码不能为空")
                  return false;
              }else{           
                toActionCom({id:'${order.id}',safepass:$("#safepass_text").val()},'<%=request.getContextPath()%>/memberDaiFaOrder/chaPay')
              }
        }
        
        function shipChaPay(){
          if($("#safepass_text").val()==""){
                  alert("支付密码不能为空")
                  return false;
              }else{           
                toActionCom({id:'${order.id}',safepass:$("#safepass_text").val()},'<%=request.getContextPath()%>/memberDaiFaOrder/chaShipPay')
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
        <h3>
<g:if test="${order.orderSN.startsWith('T')}">
    寄走退货不成功商品
</g:if> <g:else>订单详细</g:else>(<small>订单号：</small>${order.orderSN})</h3>
      </div>
        <h4>1.商品信息</h4>
        <div class="bs-docs-example">
          <table class="table table-bordered">
            <thead>
              <tr>
                <th style="width: 100px;">市场位置</th>
                <th style="width: 180px;">档口号</th>
                <th >商品货号</th>
                <th>规格（颜色/尺码）</th>
                <th>件数</th>
                <th>单件价格</th>
                <th>单件差价</th>
                <th>单件退货退款</th>
                <th>退货件数</th>
                <th>图片</th>
                <th width="190px;">操作</th>
              </tr>
            </thead>
            <tbody>
                <g:set var="totalDiff" value="0"/>
                <g:set var="bukuanFlat" value="0"/>
            
              <g:each in="${order.daiFaGoods}" status="i" var="daiFaGoods">
                
                  <g:set var="c" value="${i % 2 == 0 ? '' : ''}"/>
                
                
                <tr class="daiFaGoods ${c}">
                  <td rowspan="2">
                    ${daiFaGoods.market} - ${daiFaGoods.floor}
                  </td>
                  <td rowspan="2">${daiFaGoods.stalls}</td>
                  <td rowspan="2">${daiFaGoods.goods_sn}</td>
                  <td>${daiFaGoods.spec}</td>
                  <td>${daiFaGoods.num} ${daiFaGoods.status}</td>
                  <td>${daiFaGoods.price}</td>
                  <td class="diff" style="color:red;"><g:if test="${daiFaGoods.status == '4'}">${daiFaGoods.diffFee}<g:set var="bukuanFlat" value="1"/><g:set var="totalDiff" value="${Double.valueOf(totalDiff) + daiFaGoods.diffFee*daiFaGoods.num}"/></g:if></td>
                  
                  <td  style="color:red;">${daiFaGoods.tuihuo_price}</td>
                  <td  style="color:red;">${daiFaGoods.tuihuo_num}</td>
                  <td rowspan="2" >
                    <g:if test="${daiFaGoods.attach_id}">
                        <a href="${daiFaGoods.attach_id}" target="blank">
                        <img src="${daiFaGoods.attach_id}?imageView/2/w/100"  style="display: inline; max-width: 100px"/></a>
                    </g:if>
                  </td>
                  <td rowspan="2" >
                    <g:if test="${daiFaGoods.status == '4' || daiFaGoods.status == '5'  }">
                     <a class="btn" href="javascript:void(0)"  onclick=" toActionCom({id:'${daiFaGoods.id}'},'<%=request.getContextPath()%>/memberDaiFaOrder/closeGoods','确定取消该商品吗？')">取消并退款</a>
                        <g:if test="${daiFaGoods.status == '5'  }">
                          <a  class="btn btn-success pull-right" href="#changeModal${daiFaGoods.id}" data-toggle="modal">更换档口</a>

                        <div id="changeModal${daiFaGoods.id}" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
                             aria-hidden="true" style="display: none;">



                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>

                                <h3 id="myModalLabel">缺货商品可更换档口，让我们继续为您服务!<br/>
                            <g:if test="${2-(daiFaGoods.changeStallsNum!=null?daiFaGoods.changeStallsNum:0)<=0}">

                                <small>您已免费更换档口2次，现在更换档口将按1元/件次收费</small>
                            </g:if>
                                    <g:else>

                                        <small>您可免费更换档口${2-(daiFaGoods.changeStallsNum!=null?daiFaGoods.changeStallsNum:0)}次</small>
                                    </g:else>


                                </h3>
                            </div>

                            <div class="modal-body">


                                <g:form onsubmit="return confirmActualPrice(${daiFaGoods.price},this,${daiFaGoods.changeStallsNum});"  name="changeStall" data-validate="parsley"
                                        url="[controller: 'memberDaiFaOrder', action: 'doChangeStall']" method="post" class="form-horizontal">
                                    <input name="goodsId" type="hidden" value="${daiFaGoods.id}">
                                    <input name="orderId" type="hidden" value="${daiFaGoods.daiFaOrder.id}">
                                    <div class="control-group">
                                        <label class="control-label" >市场:</label>
                                        <g:select  class="input-small" value="${daiFaGoods.market}"  name="market"  from="${market}" />
                                        <select name="floor" class="input-small">
                                            <option value="1楼"<g:if test="${daiFaGoods.floor== '1楼'}">selected</g:if> >1楼</option>
                                            <option value="2楼" <g:if test="${daiFaGoods.floor== '2楼'}">selected</g:if>>2楼</option>
                                            <option value="3楼" <g:if test="${daiFaGoods.floor== '3楼'}">selected</g:if>>3楼</option>
                                            <option value="4楼" <g:if test="${daiFaGoods.floor== '4楼'}">selected</g:if>>4楼</option>
                                            <option value="5楼" <g:if test="${daiFaGoods.floor== '5楼'}">selected</g:if>>5楼</option>
                                            <option value="6楼" <g:if test="${daiFaGoods.floor== '6楼'}">selected</g:if>>6楼</option>
                                            <option value="7楼" <g:if test="${daiFaGoods.floor== '7楼'}">selected</g:if>>7楼</option>
                                            <option value="8楼" <g:if test="${daiFaGoods.floor== '8楼'}">selected</g:if>>8楼</option>
                                            <option value="9楼" <g:if test="${daiFaGoods.floor== '9楼'}">selected</g:if>>9楼</option>
                                        </select>
                                    </div>


                                    <div class="control-group">
                                        <label class="control-label" >档口号:</label>
                                        <input  name="stalls" class="input-medium stalls" type="text" placeholder="" value="${daiFaGoods.stalls}">
                                    </div>

                                    <div class="control-group">
                                        <label class="control-label" >货号:</label>
                                        <input  name="goods_sn" class="input-medium goods_sn" type="text" placeholder="" value="${daiFaGoods.goods_sn}">
                                    </div>

                                    <div class="control-group">
                                        <label class="control-label" >规格（颜色/尺码）:</label>
                                        <input  name="spec" class="input-medium spec" type="text" placeholder="" value="${daiFaGoods.spec}">
                                    </div>

                                    <div class="control-group">
                                        <label class="control-label" >单件价格:</label>
                                        <input name="actual_price" class="input-medium actual_price" type="text" placeholder="" value="${daiFaGoods.price}">
                                    </div>

                                    <div class="control-group">
                                        <label class="control-label" >订单备注:</label>
                                        <textarea maxlength="200" name="beizhu" style="width: 250px;height: 50px;"></textarea>
                                    </div>

                                    <g:if test="${2-(daiFaGoods.changeStallsNum!=null?daiFaGoods.changeStallsNum:0)<=0}">
                                        <div class="control-group">

                                            <label class="control-label" >使用余额支付，支付密码:</label>
                                            <input class="safepass" name="safepass" id="safepass_text" type="password" >
                                            <br/>
                                            <g:if test="${!session.loginPOJO.user.safepass}"><a target="_blank" style="color: red" href="<%=request.getContextPath()%>/memberUser/toSetSafepass">您还没有设置支付密码，点此设置</a></g:if>


                                        </div>


                                    </g:if>


                                    <g:if test="${2-(daiFaGoods.changeStallsNum!=null?daiFaGoods.changeStallsNum:0)>0}">


                                        <div class="control-group">
                                            <div class="controls">
                                                <button type="submit" class="btn btn-success btn-large" >确认更换档口</button>

                                            </div>
                                        </div>

                                    </g:if>

                                    <g:else>
                                        <div class="control-group">
                                            <label class="control-label" >商品数量:${daiFaGoods.num}件,需支付${daiFaGoods.num}元</label>
                                            <div class="controls">

                                                <button type="submit" class="btn btn-success btn-large" >确认支付以更换档口</button>


                                            </div>
                                        </div>

                                    </g:else>

                                </g:form>
                            </div>
                            <!--    <div class="modal-footer">
          <button class="btn" data-dismiss="modal">关闭</button>
          <button class="btn btn-primary">Save changes</button>
        </div>-->
                        </div>
                        </g:if>
                        
                  </g:if>
                  
                  </td>
                </tr>
                
                <tr class="${c}">
                  <td colspan="6">
                    
                    <g:if test="${daiFaGoods.status == '4'}"><font color="red">等待补款</font> 该商品现在市场单价高于您当时支付价，请点击列表下方蓝色补款按钮补足相应差额，我们继续为您拿货发货。或者点击右方按钮取消该商品。</g:if>
                      <g:elseif test="${daiFaGoods.status == '5'}"><font color="red">缺货</font> ${daiFaGoods.shuoming} </g:elseif>
                      <g:elseif test="${daiFaGoods.status == '6'}"><font color="red">已取消</font> 商品已取消</g:elseif>
                      <g:elseif test="${daiFaGoods.status == '10'}"><font color="red">已退货退款</font> 您的退货我们已处理，相应退款已退回到您的余额中</g:elseif>
                      <g:elseif test="${daiFaGoods.status == '9'}"><font color="red">已发货</font> 您的货物已发出</g:elseif>
                      <g:elseif test="${daiFaGoods.status == '2'}"><font color="red">已拿货</font>


                          商品已成功拿货，待我们验收合格后将立即为您发货
                          <br/>
                          ${daiFaGoods.yanshoudesc}

                      </g:elseif>
                      <g:elseif test="${daiFaGoods.status == '7'}"><font color="red">已验收</font> 该商品已完成验收</g:elseif>
                      <g:else><font color="red">等待处理</font> 我们将尽快按流程为您处理</g:else>
                      
                  </td>
                </tr>
              </g:each>
            </tbody>
          </table>
          <g:if test="${bukuanFlat == '1'}">
           选择支付方式： <input type="radio" id="yuePay" name="pay_type" value="0" onclick="$('#safepassSpan').show();$('#zfbSpan').hide()" checked/>余额支付 &nbsp;&nbsp;
                                <input type="radio" id="zfbPay" name="pay_type" value="1" onclick="$('#safepassSpan').hide();$('#zfbSpan').show()" />支付宝支付 
                                <br/>
                                <br/>
                               <div id="safepassSpan" >支&nbsp;&nbsp;付&nbsp;&nbsp;&nbsp;密&nbsp;&nbsp;码：<input  name="safepass" id="safepass_text" type="password" >
                                  <g:if test="${!session.loginPOJO.user.safepass}"><a target="_blank" style="color: red" href="<%=request.getContextPath()%>/memberUser/toSetSafepass">您还没有设置支付密码，点此设置</a></g:if>
                                <div>
                                  <a  href="javascript:void(0)" onclick="chaPay()"  class=" btn btn-large btn-primary">补款（金额：${totalDiff}）</a>
                                  <g:link controller="memberDaiFaOrder" action="list"  params="${params}" class=" btn btn-large ">返回</g:link>
                                </div>
                              </div>
                                <div id="zfbSpan" style="display: none">
                                    <g:link target="_blank" action="reqPay" controller="memberAlipay" params="[orderSN:order.orderSN,payType:'1']" onclick="daiFaPay()"  class=" btn btn-large btn-primary">补款（金额：${totalDiff}）</g:link>
                                    <g:link controller="memberDaiFaOrder" action="list"  params="${params}" class=" btn btn-large ">返回</g:link>
                                </div>
         
        </g:if>
          <g:if test="${order.status == 'diffship'}">

            包裹重量：${order.weight}
              <br/>

           选择支付方式： <input type="radio" id="yuePay" name="pay_type" value="0" onclick="$('#safepassSpan').show();$('#zfbSpan').hide()" checked/>余额支付 &nbsp;&nbsp;
                                <input type="radio" id="zfbPay" name="pay_type" value="1" onclick="$('#safepassSpan').hide();$('#zfbSpan').show()" />支付宝支付 
                                <br/>
                                <br/>
                               <div id="safepassSpan" >支&nbsp;&nbsp;付&nbsp;&nbsp;&nbsp;密&nbsp;&nbsp;码：<input  name="safepass" id="safepass_text" type="password" >
                                  <g:if test="${!session.loginPOJO.user.safepass}"><a target="_blank" style="color: red" href="<%=request.getContextPath()%>/memberUser/toSetSafepass">您还没有设置支付密码，点此设置</a></g:if>
                                <div>
                                  <a  href="javascript:void(0)" onclick="shipChaPay()"  class=" btn btn-large btn-primary">运费补款（金额：${order.diffShip}）</a>
                                </div>
                              </div>
                                <div id="zfbSpan" style="display: none">
                                    <g:link target="_blank" action="reqPay" controller="memberAlipay" params="[orderSN:order.orderSN,payType:'2']" onclick="daiFaPay()"  class=" btn btn-large btn-primary">运费补款（金额：${order.diffShip}）</g:link>
                                </div>
         
        </g:if>
         
        </div>
        
        
        

        <h4>2.收货地址</h4>
        <div class="bs-docs-example">
          <table class="table table-bordered">

            <tbody>

              <tr>
                <td >

                  收&nbsp;&nbsp;件&nbsp;&nbsp;人：
                  ${order.reperson}
                  &nbsp;&nbsp;
                  联系手机/电话：
                  ${order.contphone}
                  <br/>
                  收件地区：
                  <g:areaName area_id="${order.area_id}" />
                  <br/>
                  收件地址：               
                  ${order.address}
                  <br/>
                  快递：
                  <order:wuliu wuliu="${order.wuliu}" /> 
                  <br/>
                  <g:if test="${order.status == 'shipped'}">
                    发货时间：
                    ${order.ship_time.toString()[0..18]}
                   <br/>
                    发货单号：
                    ${order.wuliu_no}
                   <br/>
                  </g:if>
                </td>
              </tr>

            </tbody>
          </table>
        </div>
        
        <h4>3.下单人信息</h4>
        <div class="bs-docs-example">
          <table class="table table-bordered">

            <tbody>

              <tr>
                <td >
                  下&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;单&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;人：
                  ${order.sendperson}
                    <br/>
                您&nbsp;的&nbsp;网&nbsp;店&nbsp;名：${order.wangdianming}
                  <br/>
                  发&nbsp;&nbsp;&nbsp;件&nbsp;&nbsp;地&nbsp;&nbsp;&nbsp;址：
                  ${order.sendaddress}
                  <br/>
                  联系手机/电话：
                  ${order.sendcontphone}
                  <br/>
                  备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注：
                  ${order.senddesc}
                  <br/>
                  <g:if test="${order.h_senddesc}">
                    历&nbsp;&nbsp;&nbsp;史&nbsp;&nbsp;备&nbsp;&nbsp;&nbsp;注：
                    <ul>
                      <g:each in="${order.h_senddesc.split("\\|")}" var="item">
                        <li>${item}</li>
                       </g:each>
                    </ul>
                    
                </g:if>
              </tr>

            </tbody>
          </table>
        </div>
        
        <h4>4.礼品</h4>
        <div class="bs-docs-example">
          <table class="table table-bordered">

            <tbody>

              <tr>
                <td >

                  选择礼品：<order:orderRegards status="${order.regards}" />
                  
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
                <td >
                  商品总价：
                  <strong id="goodsFee">${order.goodsFee}</strong>
                  
                  <br/>
                  运&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;费：&nbsp;<strong id="shipFee">${order.shipFee}</strong>
                  
                  <br/>
                  代&nbsp; 发&nbsp; 费：&nbsp;<strong id="serviceFee">${order.serviceFee}(代发每件1元)</strong>
                  
                  <br/>
                  礼&nbsp; 品&nbsp; 费：&nbsp;<strong id="regardsFee">${order.regardsFee}(好评卡+小礼物1元/件，包装盒1.5元/件，包装盒+小礼物2元/件，好评卡+包装盒+小礼物3元/件)</strong>

                  <br/>
                  订单总价：&nbsp;<strong id="totalFee">${order.totalFee}</strong>
                  <br/>
                  <br/>
                </td>
              </tr>

            </tbody>
          </table>
        </div>
        
        <h4>5.状态</h4>
        <div class="bs-docs-example">
          <table class="table table-bordered">
 
            <tbody>

              <tr>
                <td >
                  订单状态：
                  <strong ><order:orderStatusDic status="${order.status}"/></strong>
                  
                  <br/>
                  <g:if test="${order.status == 'processing'}">
                    受理时间：
                    <strong >${order.processing_time.toString()[0..18]}</strong>
                   <br/>                  
                  </g:if>
                  
                  <br/>
                </td>
              </tr>

            </tbody>
          </table>
            
        </div>
        


      
      
      

       <g:if test="${order.status == 'waitpay'}">
        选择支付方式： <input type="radio" id="yuePay" name="pay_type" value="0" onclick="$('#safepassSpan').show();$('#zfbSpan').hide()" checked/>余额支付 &nbsp;&nbsp;
                                <input type="radio" id="zfbPay" name="pay_type" value="1" onclick="$('#safepassSpan').hide();$('#zfbSpan').show()" />支付宝支付 
                                <br/>
                                <br/>
                               <div id="safepassSpan" >支&nbsp;&nbsp;付&nbsp;&nbsp;&nbsp;密&nbsp;&nbsp;码：<input  name="safepass" id="safepass_text" type="password" >
                                  <g:if test="${!session.loginPOJO.user.safepass}"><a target="_blank" style="color: red" href="<%=request.getContextPath()%>/memberUser/toSetSafepass">您还没有设置支付密码，点此设置</a></g:if>
                                <div>
                                  <a  href="javascript:void(0)" onclick="yuePay()"  class=" btn btn-large btn-primary">支付</a>
                                  <g:link controller="memberDaiFaOrder" action="list"  params="${params}" class=" btn btn-large ">返回</g:link>
                                </div>
                              </div>
                                <div id="zfbSpan" style="display: none">
                                    <g:link target="_blank" action="reqPay" controller="memberAlipay" params="[orderSN:order.orderSN,payType:'0']" onclick="daiFaPay()"  class=" btn btn-large btn-primary">支付</g:link>
                                    <g:link controller="memberDaiFaOrder" action="list"  params="${params}" class=" btn btn-large ">返回</g:link>
                                </div>
      </g:if>
      <g:if test="${order.status != 'waitpay' && bukuanFlat != '1'}">
        <g:link controller="memberDaiFaOrder" action="list"  params="${params}" class=" btn btn-large ">返回</g:link>
      </g:if>
      <div id="daiFaPayModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="display: none;">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
          <h3 id="myModalLabel">支付代发订单</h3>
        </div>




        <div class="modal-footer">
          <g:link class="btn btn-primary" controller="memberDaiFaOrder" params="${params}" action="show">支付完成</g:link>
        </div>
      </div>
      <form     method="post" id="commonActionForm" target="innerFrame">
                  <input type='hidden' name='id' id='id' class='key'>
                  <input type='hidden' name='safepass' id='safepass' class='key'>
        </form>
        <iframe id="innerFrame" name="innerFrame" height="0" frameborder="0"></iframe>
         <g:form  action="${actionName}" params="${params}"  method="post" class="commonListForm">
        </g:form >
      <g:render template="/layouts/footer"/>
  </body>
</html>
