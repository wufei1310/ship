<r:require modules="uploadify"/>
<%@ page import="common.CommonParams"%>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
    <title>金士代发</title>
      <style>
      .uploadifyQueue{
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
        function daiFaPay(){
          $('#daiFaPayModal').modal({
                keyboard: false
               })
        }
        function yuePay1(){
          if(!$("#doSaleReturnForm").parsley( 'validate' )){
                   return false
          }
          if($('input[type="checkbox"]:checked').length==0){
              alert("请选择需要退货的商品")
              return false
          }


          var wuliupic = $("#imgInput").val();
          if($.trim(wuliupic)==""){
              alert("请上传退回商品的物流单拍照图片，这将做为我们审核退货申请的重要凭据！")
              return false
          }


          if($("#yuePay").is(":checked")){
            if($("#safepass_text").val()==""){
                  alert("支付密码不能为空")
                  return false;
              }
             
              $(".doSaleReturnForm").attr("target","innerFrame")
              toActionFormCom('doSaleReturnForm')   
          }else{
              $(".doSaleReturnForm").attr("target","_blank")
              daiFaPay()
              $("#doSaleReturnForm").submit()
          }
          
        }

        
        function checkCheckBox(obj){
           var o = $(obj).closest('tr').find('input[type="text"]')
           if($(obj).is(":checked")){
             o.removeAttr("disabled")
           }else{
             o.attr("disabled","disabled")
           }
        }
        
        function fee(){
          var serviceFee = 0.00;
          var num = 0
          $(".daiFaGoods").find("input[type='checkbox']").each(function(){
             if($(this).is(":checked")){
               num = num + $(this).closest('tr').find('input[name="return_num"]').val()*1
             }
          })
          serviceFee = num * <%=CommonParams.return_free%>
          $("#serviceFee").html(serviceFee)
        }
    </script>
    
    
    <div class="container body">
      <g:if test="${flash.message}">
         <script>
         alert('${flash.message}')
        </script>
      </g:if>

      <div class="page-header">
        <h3>申请退货(<small>订单号：</small>${order.orderSN})</h3>
      </div>
      <g:form name="doSaleReturnForm"   class="doSaleReturnForm" action="doSaleReturn" method="post" target="innerFrame" data-validate="parsley" >
        <input type="hidden" name="id" value="${order.id}">
        <h4>商品信息<span  style="font-size: 0.8em;;color:red;">（请选择需要退货的商品）</span></h4>
        <div class="bs-docs-example">
          <table class="table table-bordered">
            <thead>
              <tr>
                <th></th>
                <th style="width: 200px;">市场位置</th>
                <th style="width: 200px;">档口号</th>
                <th >商品货号</th>
                <th>规格（颜色/尺码）</th>
                <th>拿货件数</th>
                <th>单件商品价格</th>
                <th>单件退货价格</th>
                <th>退货件数</th>
              </tr>
            </thead>
            <tbody>
              <g:each in="${order.daiFaGoods}" status="i" var="daiFaGoods">
                <g:if test="${daiFaGoods.status != '6'}">
                  <tr class="daiFaGoods">
                    <td><input type="checkbox" name="goods_id" value="${daiFaGoods.id}" onclick="checkCheckBox(this);fee()" /></td>
                    <td >
                      ${daiFaGoods.market} - ${daiFaGoods.floor}
                    </td>
                    <td>${daiFaGoods.stalls}</td>
                    <td>${daiFaGoods.goods_sn}</td>
                    <td>${daiFaGoods.spec}</td>
                    <td>${daiFaGoods.num}</td>
                    <td>${daiFaGoods.price}</td>
                    <td><input onkeyup="if(isNaN(value) || value<=0)execCommand('undo');if(value>${daiFaGoods.price})value=${daiFaGoods.price}" onafterpaste="if(isNaN(value) || value<=0)execCommand('undo');if(value>${daiFaGoods.price})value=${daiFaGoods.price}" data-error-message="单件退货价格不能为空" data-required="true"   name="price" class="input-small" type="text" placeholder="" value="${daiFaGoods.price}" disabled="disabled" onblur="if(isNaN(value) || value<=0)execCommand('undo');if(value>${daiFaGoods.price})value=${daiFaGoods.price}"></td>
                    <td><input onkeyup="if(value>${daiFaGoods.num})this.value=${daiFaGoods.num};if(isNaN(value) || value<=0)execCommand('undo')" onafterpaste="if(value>${daiFaGoods.num})this.value=${daiFaGoods.num};if(isNaN(value) || value<=0)execCommand('undo')" data-error-message="退货件数不能为空" data-required="true"   name="return_num" class="input-small" type="text" placeholder="" value="${daiFaGoods.num}" disabled="disabled" onblur="if(value>${daiFaGoods.num})this.value=${daiFaGoods.num};if(isNaN(value) || value<=0)execCommand('undo');fee();">                       
                    </td>
                  </tr>
                 </g:if>
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
                  ${order.sendperson}
                  <br/>
                  发&nbsp;&nbsp;&nbsp;件&nbsp;&nbsp;地&nbsp;&nbsp;&nbsp;址：
                  ${order.sendaddress}
                  <br/>
                  联系手机/电话：
                  ${order.sendcontphone}
                  <br/>
              </tr>

            </tbody>
          </table>
        </div>
        
        <h4>退回商品物流信息<span  style="font-size: 0.8em;color:red;">（请如实填写物流信息，若因物流信息不正确，不能正常退换货，手续费不予退换）</span></h4>
        <div class="bs-docs-example">
          <table class="table table-bordered">

            <tbody>

              <tr>
                <td >

                  退回物流单号：<input   data-error-message="物流单号不能为空" data-required="true"   name="wuliu_sn"  type="text" placeholder=""  >
                  <br>
                  退回物流公司：<input  data-error-message="物流公司不能为空" data-required="true"   name="wuliu"  type="text" placeholder=""  >
                    <br>
                    上传快递单图片：
                    <div id="imgFileDiv" >
                    <input type="file" class="imgFile" id="imgFile" style="display:none">
                    </div>
                    <div class="imgDiv" id="imgDiv" style="position: relative;width:100px"> </div>
                    <input  type="hidden" id="imgInput" class="imgInput"  name="wuliupic" value=" " >
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

                  手&nbsp; 续&nbsp; 费：&nbsp;<strong id="serviceFee">0</strong>(手续费每件2元)

                </td>
              </tr>

            </tbody>
          </table>
        </div>
        
      


      
      
      

 
        选择支付方式： <input type="radio" id="yuePay" name="pay_type" value="0" onclick="$('#safepassSpan').show()" checked/>余额支付 &nbsp;&nbsp;
                                <input type="radio" id="zfbPay" name="pay_type" value="1" onclick="$('#safepassSpan').hide()" />支付宝支付 
                                <br/>
                                <br/>
                               <div id="safepassSpan" >支&nbsp;&nbsp;付&nbsp;&nbsp;&nbsp;密&nbsp;&nbsp;码：<input  name="safepass" id="safepass_text" type="password" >
                                  <g:if test="${!session.loginPOJO.user.safepass}"><a target="_blank" style="color: red" href="<%=request.getContextPath()%>/memberUser/toSetSafepass">您还没有设置支付密码，点此设置</a></g:if>
                                
                              </div>
                                <div>
                                  <a  href="javascript:void(0)" onclick="yuePay1()"  class=" btn btn-large btn-primary">支付</a>
                                  <g:link controller="memberDaiFaOrder" action="list"  params="${params}" class=" btn btn-large ">返回</g:link>
                                </div>

    </g:form>
      <div id="daiFaPayModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="display: none;">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
          <h3 id="myModalLabel">支付代发订单</h3>
        </div>




        <div class="modal-footer">
          <g:link class="btn btn-primary" controller="memberDaiFaOrder"  action="saleReturnList">支付完成</g:link>
        </div>
      </div>
      <form     method="post" id="commonActionForm" target="innerFrame">
                  <input type='hidden' name='id' id='id' class='key'>
                  <input type='hidden' name='safepass' id='safepass' class='key'>
        </form>
        <iframe id="innerFrame" name="innerFrame" height="0" frameborder="0"></iframe>
         <g:form  action="saleReturnList"   method="post" class="commonListForm">
        </g:form >
      <g:render template="/layouts/footer"/>



        <script>

        var $uploadifyImg = {
            'auto':true,
            'debug':false,
            'multi': false,
            'sizeLimit' : '2100000',
            'fileDataName' : 'imgFile',
            'fileDesc' : '上传图片',
            'fileExt' : '*.gif;*.jpg;*.jpeg;*.png;*.bmp;',
            'buttonImg' : '<%=request.getContextPath()%>/images/upload.png',
            'cancelImg': '<%=request.getContextPath()%>/resource/uploadify/cancel.png',
            'uploader': '<%=request.getContextPath()%>/resource/uploadify/uploadify.swf',
            'script' : '<%=request.getContextPath()%>/image/doImgUpload;jsessionid=<%=session.getId()%>'
        };

        function initImg(){
            $uploadifyImg.onComplete = function(event,queueId,fileObj,response,data) {
                var msg = eval('('+response+')');
                if(msg.error == '0'){


                    var _html = '<a href="<%=request.getContextPath()%>/'+msg.url+'" target="blank"><img src="<%=request.getContextPath()%>/'+msg.url+'?imageView/2/w/100"  style="display: inline;max-width: 100px"/></a>'+
                            '<span class="closeimg" style="position: absolute; top:0px; right:0px; display:inline-block;*display:inline;zoom:1; width:16px; height:18px; z-index:100;cursor: pointer; " onClick="removeImg()"><img src="<%=request.getContextPath()%>/resource/uploadify/cancel.png"/>'
                    $('#imgDiv').html(_html)
                    $("#imgFileDiv").css("height","0px");
                    $("#imgInput").val(msg.url)
                }else{
                    alert(msg.message)
                }
            }
            $('#imgFile').uploadify($uploadifyImg)
        }
        $(document).ready(function() {

            initImg();
        });

        function removeImg(){
            $('#imgDiv').html("")
            $("#imgFileDiv").css("height","auto");
            $("#imgInput").val("")
        }

        </script>


  </body>
</html>
