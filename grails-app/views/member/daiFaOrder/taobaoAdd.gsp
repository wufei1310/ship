<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
    <title>金士代发</title>
  </head>
  <body>
    <div class="container body">
      <g:if test="${flash.message}">
         <script>
         alert('${flash.message}')
        </script>
      </g:if>

      <div class="page-header">
        <h3>创建新订单</h3>
      </div>
      <g:form   name="addDaiFaForm" id="addDaiFaForm" data-validate="parsley" class="form-signin addDaiFaForm" controller="memberDaiFaOrder" action="doAdd" method="post">
        <h4>1.填写商品信息</h4>
        <div class="bs-docs-example">
          <table class="table table-bordered">
            <thead>
              <tr>
                <th style="width: 200px;">市场位置</th>
                <th style="width: 200px;">档口号</th>
                <th >商品货号</th>
                <th>规格（颜色/尺码）</th>
                <th>件数</th>
                <th>单件价格</th>
                <th><button onclick="addDaiFaGoods()" type="button" class="btn btn-primary">增加</button></th>
              </tr>
            </thead>
            <tbody>
             <g:each in="${goodsList}" status="i" var="daiFaGoods">
              <tr class="daiFaGoods">
                <td >
                  <select name="market" class="input-small">
                    <option value="大时代">大时代</option>
                    <option value="宝华">宝华</option>
                    <option value="女人街">女人街</option>
                    <option value="大西豪">大西豪</option>
                    <option value="西街">西街</option>
                    <option value="富丽">富丽</option>
                    <option value="金马">金马</option>
                    <option value="机筑巷">机筑巷</option>
                    <option value="国大">国大</option>
                    <option value="广州站">广州站</option>
                    <option value="新潮都">新潮都</option>
                    
                    <option value="柏美">柏美</option>
                    <option value="非凡">非凡</option>
                    <option value="佰润">佰润</option>
                    <option value="益民">益民</option>
                  </select>
                  <select name="floor" class="input-small">
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
                <td><input  name="stalls" class="input-medium" type="text" placeholder=""></td>
                <td><input  name="goods_sn" class="input-small" type="text" placeholder="" value="${daiFaGoods.goods_sn}"></td>
                <td><input  name="spec" class="input-small" type="text" placeholder="" value="${daiFaGoods.spec}"></td>
                <td><input onkeyup="if(isNaN(value))execCommand('undo')" onafterpaste="if(isNaN(value))execCommand('undo')" onblur="fee()" name="num" class="input-small" type="text" placeholder="" value="${daiFaGoods.num}"></td>
                <td><input onkeyup="if(isNaN(value))execCommand('undo')" onafterpaste="if(isNaN(value))execCommand('undo')"  onblur="fee()" name="price" class="input-small" type="text" placeholder="" value="${daiFaGoods.price}"></td>
                <td><button onclick="removeDaiFaGoods(this)" type="button" class="btn btn-danger">删除</button></td>
              </tr>
             </g:each>

            </tbody>
          </table>
        </div>

        <h4>2.填写下单人信息</h4>
        <div class="bs-docs-example">
          <table class="table table-bordered">

            <tbody>

              <tr>
                <td >
                  下&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;单&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;人：
                  <input data-error-message="下单人不能为空" data-required="true"  name="sendperson" type="text" class="" placeholder="">
                  <br/>
                  发&nbsp;&nbsp;&nbsp;件&nbsp;&nbsp;地&nbsp;&nbsp;&nbsp;址：
                  <input data-error-message="下单人地址不能为空" data-required="true"  name="sendaddress" type="text" class="input-xxlarge" placeholder="">
                  <br/>
                  联系手机/电话：
                  <input data-error-message="联系手机/电话不能为空" data-required="true"   name="sendcontphone" type="text" class="" placeholder="">
                  <br/>
                  备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注：
                  <input   name="senddesc" type="text" class="input-xxlarge" placeholder="">
              </tr>

            </tbody>
          </table>
        </div>
        
        <h4>3.填写收货地址</h4>
        <div class="bs-docs-example">
          <table class="table table-bordered">

            <tbody>

              <tr>
                <td >
               
                  &nbsp;&nbsp;
                  收&nbsp;&nbsp;件&nbsp;&nbsp;人：
                  <input data-error-message="收件人不能为空" data-required="true" id="reperson" name="reperson" type="text" class="" placeholder="" value="${reperson}">
                  &nbsp;&nbsp;
                  联系手机/电话：
                  <input data-error-message="联系手机/电话不能为空" data-required="true"  id="contphone" name="contphone" type="text" class="" placeholder="" value="${contphone}">

                  <br/>
                  收件地址：
                  <input data-error-message="收件地址不能为空" data-required="true"  id="address" name="address" type="text" class="input-xxlarge" placeholder="" value="${address}">

                  <br/>
                  选择快递：
                  <select name="wuliu" class="input-small">
                    <option value="中通">中通</option>
                    <option value="顺丰">顺丰</option>
                    <option value="圆通">圆通</option>
                    
                    <option value="汇通">汇通</option>
                    <option value="申通">申通</option>
                    <option value="韵达">韵达</option>
                    <option value="宅急送">宅急送</option>
                    <option value="EMS">EMS</option>
                    <option value="天天">天天</option>
                  </select>
                  <br/>
                  填写运费：
                  <input onkeyup="if(isNaN(value))execCommand('undo')" onafterpaste="if(isNaN(value))execCommand('undo')" onblur="fee()"  data-required-message="运费不能为空" data-required="true" data-min-message="运费至少大于0" data-min="0"  id="addShipFee" type="text" class="" placeholder="请输入运费" value="${shipFee}">


                </td>
              </tr>

            </tbody>
          </table>
        </div>

        <h4>4.支付款项</h4>
        <div class="bs-docs-example">
          <table class="table table-bordered">

            <tbody>

              <tr>
                <td >
                  商品总价：
                  <strong id="goodsFee">0</strong>
                  <input type="hidden" name="goodsFee">
                  <br/>
                  运&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;费：&nbsp;<strong id="shipFee">0</strong>
                  <input type="hidden" name="shipFee">
                  <br/>
                  代&nbsp; 发&nbsp; 费：&nbsp;<strong id="serviceFee">0</strong>
                  <input type="hidden" name="serviceFee">(代发每件1元)
                  <br/>
                  订单总价：&nbsp;<strong id="totalFee">0</strong>
                  <input type="hidden" name="totalFee">
                  <!--                <br/>
                                  账户余额：<strong style="color: red;">0</strong>(余额不足，去充值)-->
                  <br/>
                  <br/>
                  选择支付方式： <input type="radio" id="yuePay" name="pay_type" value="0" onclick="$('#safepassSpan').show()" checked/>余额支付 &nbsp;&nbsp;
                                <input type="radio" id="zfbPay" name="pay_type" value="1" onclick="$('#safepassSpan').hide()" />支付宝支付 
                                <br/>
                                <br/>
                                <div id="safepassSpan" >支&nbsp;&nbsp;付&nbsp;&nbsp;&nbsp;密&nbsp;&nbsp;码：<input  name="safepass" id="safepass" type="password" >
                                  <g:if test="${!session.loginPOJO.user.safepass}"><a target="_blank" style="color: red" href="<%=request.getContextPath()%>/memberUser/toSetSafepass">您还没有设置支付密码，点此设置</a></g:if></div>
                  <div>
                                <button id="addDaiFaButt" type="button" class=" btn btn-large btn-primary" onclick="beforAddDaiFaForm(this)">提交订单并支付</button>
                  </div>

                </td>
              </tr>

            </tbody>
          </table>
        </div>
      </g:form>


      <div id="daiFaPayModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="display: none;">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
          <h3 id="myModalLabel">支付代发订单</h3>
        </div>




        <div class="modal-footer">
          <g:link class="btn btn-primary" controller="memberDaiFaOrder" action="list">支付完成</g:link>
        </div>
      </div>
      <iframe id="innerFrame" name="innerFrame" height="0" frameborder="0"></iframe>
         <g:form  action="list"   method="post" class="commonListForm">
        </g:form >
       <g:render template="/layouts/footer"/>
      </div> <!-- /container -->
      <script>
       function pageProcess(){
          $("#addDaiFaButt").removeAttr("disabled")
        }
        fee()
      
        function beforAddDaiFaForm(obj){
          
          
          var msg = ''
          
          $("input[name='stalls']").each(function(){
            if($(this).val()==""){
              msg = msg + "档口号不能为空" + '\n'
//              alert("档口号不能为空")
              return false;
            }
          })
          
          $("input[name='goods_sn']").each(function(){
            if($(this).val()==""){
              msg = msg + "商品货号不能为空" + '\n'
//              alert("商品货号不能为空")
              return false;
            }
          })
          
          $("input[name='spec']").each(function(){
            if($(this).val()==""){
              msg = msg + "规格（颜色/尺码）不能为空" + '\n'
//              alert("规格（颜色/尺码）不能为空")
              return false;
            }
          })
          
          $("input[name='num']").each(function(){
            
            
          
            if($(this).val()==""){
              msg = msg + "件数不能为空" + '\n'
//              alert("件数不能为空")
              return false;
            }
            if($(this).val()<=0){
              msg = msg + "件数必须大于0" + '\n'
//              alert("件数必须大于0")
              return false;
            }
          })
          
          
          $("input[name='price']").each(function(){
            
            if($(this).val()==""){
              msg = msg + "单件价格不能为空" + '\n'
//              alert("单件价格不能为空")
              return false;
            }
            if($(this).val()<=0){
              msg = msg + "单件价格必须大于0" + '\n'
//              alert("单件价格必须大于0")
              return false;
            }
          
          })
          
          
          if(msg!=''){
            alert(msg)
            return false;//取消提交
          }else{
            
          }
            
          
          fee()
          
          
        
          if(($("#reperson").val()!='')
            &&($("#contphone").val()!='')
            &&($("#address").val()!='')){
              
            }
            if($("#yuePay").is(":checked")){
             if($("#safepass").val()==""){
                  alert("支付密码不能为空")
                  return false;
              }else{
                if(!$("#addDaiFaForm").parsley( 'validate' )){
                   return false
                 }else{
                  $(obj).attr("disabled","disabled")
                  $("#addDaiFaForm").attr("target","innerFrame")
                  toActionFormCom('addDaiFaForm')
                 }
              }
          }else{
            if(!$("#addDaiFaForm").parsley( 'validate' )){
             return false
             }else{
               $('#daiFaPayModal').modal({
                keyboard: false
               })
               $("#addDaiFaForm").attr("target","_blank")
               $("#addDaiFaForm").submit()
             }
          }
        }
      
        function fee(){
        
          var goodsFee = 0.00 ;
          var serviceFee = 0.00;
          var shipFee = 0.00;
          var totalFee = 0.00;
          $(".daiFaGoods").each(function(){
            var num = $(this).find("input[name='num']").val();
            var price = $(this).find("input[name='price']").val();
            goodsFee = parseFloat(goodsFee) + parseFloat(price*num);
            serviceFee = parseFloat(serviceFee) + parseFloat(1*num);
            shipFee = parseFloat($("#addShipFee").val());
          })
          $("#goodsFee").html(goodsFee.toFixed(2));
          $("#goodsFee").next().val(goodsFee.toFixed(2))
          $("#serviceFee").html(serviceFee.toFixed(2));
          $("#serviceFee").next().val(serviceFee.toFixed(2))
          $("#shipFee").html(shipFee.toFixed(2));
          $("#shipFee").next().val(shipFee.toFixed(2))
        
          totalFee = parseFloat(goodsFee) + parseFloat(serviceFee) + parseFloat(shipFee);
          $("#totalFee").html(totalFee.toFixed(2));
          $("#totalFee").next().val(totalFee.toFixed(2))
        }
      
      
        function removeDaiFaGoods(o){
        
          $(o).parents("tr").remove();
          fee()
        }
      
        function addDaiFaGoods(){
          fee()
          var lastTR = jQuery(".daiFaGoods:last")
          var cloneTR = lastTR.clone();
          cloneTR.show();
          cloneTR.find("input").val('')
          jQuery(".daiFaGoods:last").after(cloneTR)
        }
      </script>
  </body>
</html>
