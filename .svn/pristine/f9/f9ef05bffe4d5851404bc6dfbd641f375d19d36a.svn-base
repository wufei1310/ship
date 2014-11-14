<!--
  To change this template, choose Tools | Templates
  and open the template in the editor.
-->


<!--
To change this template, choose Tools | Templates
and open the template in the editor.
-->
<!DOCTYPE html>
<html>
  <head>
    <title></title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <!-- 最新 Bootstrap 核心 CSS 文件 -->
    <style type="text/css">


      .head table{
        width:100%;
      }

      .content{
        width:100%;
      }

      table
      {
        border-collapse: collapse;
        border: none;
        width: 100%;
      }
      table    td
      {
        border: solid #000 1px;
      }
    </style>

  </head>
  <body>

    <div class="page-header">
      <h3>
        已发货拿货订单数量:<span style="color:red;">${shippedmap.orderCount}</span>
       <!-- &nbsp;&nbsp;已退款退货订单数量:<span style="color:red;">${tuimap.orderCount}</span>
        &nbsp;&nbsp;已发货换货订单数量:<span style="color:red;">${huanmap.orderCount}</span>    -->
      </h3>
    </div>
   
    
    实际拿货款：${shippedmap.t_shipped_actual_price}<br/>
    实际发货运费：${shippedmap.t_shipped_actual_shipfee}<br/>
    换货发货运费：${shippedmap.t_shipped_actualhuanhuo_shipfee}<br/>
    档口退货回款：${shippedmap.t_return_goodsFee}<br/>



    凡衣拨款：${shippedmap.t_shipped_actualhuanhuo_shipfee+shippedmap.t_shipped_actual_price+shippedmap.t_shipped_actual_shipfee-shippedmap.t_return_goodsFee}<br/>

    <hr/>

    
    <table class="table table-bordered">

      <thead>
        <tr>
          <td ><h3>支出</h3></td>
          <td>金额</td>
          <td>商品数量</td>
          <td>合计</td>
        </tr>


      </thead>
      <tbody>

        <tr>
          <td>验收商品</td>
          <td>${shippedmap.yan_goods}</td>
          <td>${shippedmap.yanshougoodsnum}</td>
          <td rowspan="11">${huanmap.changeStallchu+shippedmap.cancle_order_giftfee + huanmap.cha_goods+huanmap.huan_ship+shippedmap.kong+tuimap.tui_goods+shippedmap.cancle_allgoods+shippedmap.cancle_goods+shippedmap.fa_ship+shippedmap.yan_goods}</td>
        </tr>

        <tr>
          <td>发货运费</td>
          <td>${shippedmap.fa_ship}</td>
          <td>${shippedmap.shipgoodsnum}</td>
        </tr>

        <tr>
          <td>会员取消商品退款</td>
          <td>${shippedmap.cancle_goods}</td>
          <td>${shippedmap.canclegoodsnum}</td>
        </tr>

        <tr>
          <td>会员取消商品退运费</td>
          <td>${shippedmap.cancle_allgoods}</td>
          <td>${shippedmap.canclegoodsnumship}</td>
        </tr>

      <tr>
          <td>会员取消商品(订单)退礼品费</td>
          <td>${shippedmap.cancle_order_giftfee}</td>
          <td></td>
      </tr>

      <tr>
          <td>会员退货退款至会员账户</td>
          <td>${tuimap.tui_goods}</td>
          <td>${tuimap.tuigoodsnum}</td>
        </tr>
        <tr>
          <td>空包退还代发费用</td>
          <td>${shippedmap.kong}</td>
          <td>${shippedmap.konggoodsnum}</td>
        </tr>
        <tr>
          <td>发货运费(换货)</td>
          <td>${huanmap.huan_ship}</td>
          <td>${huanmap.huanshipgoodsnum}</td>
        </tr>
        <tr>
          <td>换货差价支出</td>
          <td>${huanmap.cha_goods}</td>
          <td>${huanmap.chagoodsnum}</td>
        </tr>
      <tr>
          <td>换档口退货款</td>
          <td>${shippedmap.changeStallchu}</td>
          <td></td>
      </tr>

      </tbody>

    </table>

    <br/>
    <br/>
    <br/>
    
    
    
    <table class="table table-bordered">
      <thead>
        <tr>
          <td ><h3>收入</h3></td>
          <td>商品数量</td>
          <td>支付宝收入</td>
          <td>会员账户余额收入</td>
          
          <td>小计</td>
          <td >合计</td>
        </tr>


      </thead>
      <tbody>

        <tr>
          <td>会员支付商品</td>
          <td>${shippedmap.paygoodsnum}</td>
          <td>${shippedmap.t_ali_goods}</td>
          <td>${shippedmap.t_yu_goods}</td>
          <td>${shippedmap.t_yu_goods+shippedmap.t_ali_goods}</td>
          <td rowspan="11">${shippedmap.t_yu_gift+shippedmap.t_ali_gift + tuimap.tui_huikuan+huanmap.tui_yu_ship+huanmap.tui_ali_ship+huanmap.tui_yu_service+huanmap.tui_ali_service+tuimap.tui_yu_service+tuimap.tui_ali_service+huanmap.tui_yu_goods+huanmap.tui_ali_goods+shippedmap.t_yu_buship+shippedmap.t_ali_buship+shippedmap.t_yu_bugoods+shippedmap.t_ali_bugoods+shippedmap.t_yu_service+shippedmap.t_ali_service+shippedmap.t_yu_ship+shippedmap.t_ali_ship+shippedmap.t_yu_goods+shippedmap.t_ali_goods}</td>
        </tr>
        <tr>
          <td>会员支付拿货运费</td>
          <td>${shippedmap.payshipgoodsnum}</td>
          <td>${shippedmap.t_ali_ship}</td>
          <td>${shippedmap.t_yu_ship}</td>
          <td>${shippedmap.t_yu_ship+shippedmap.t_ali_ship}</td>
        </tr>
        <tr>
          <td>会员支付服务费</td>
          <td>${shippedmap.payservicegoodsnum}</td>
          <td>${shippedmap.t_ali_service}</td>
          <td>${shippedmap.t_yu_service}</td>
          <td>${shippedmap.t_yu_service+shippedmap.t_ali_service}</td>
        </tr>

      <tr>
          <td>会员支付礼品费</td>
          <td></td>
          <td>${shippedmap.t_ali_gift}</td>
          <td>${shippedmap.t_yu_gift}</td>
          <td>${shippedmap.t_yu_gift+shippedmap.t_ali_gift}</td>
      </tr>



        <tr>
          <td>会员对商品补款</td>
          <td>${shippedmap.paybugoodsnum}</td>
          <td>${shippedmap.t_ali_bugoods}</td>
          <td>${shippedmap.t_yu_bugoods}</td>
          <td>${shippedmap.t_yu_bugoods+shippedmap.t_ali_bugoods}</td>
        </tr>

        <tr>
          <td>会员对运费补款</td>
          <td>${shippedmap.paybushipgoodsnum}</td>
          <td>${shippedmap.t_ali_buship}</td>
          <td>${shippedmap.t_yu_buship}</td>
          <td>${shippedmap.t_yu_buship+shippedmap.t_ali_buship}</td>
        </tr>



        <tr>
          <td>会员支付换货差价</td>
          <td>${huanmap.payhuangoodsnum}</td>
          <td>${huanmap.tui_ali_goods}</td>
          <td>${huanmap.tui_yu_goods}</td>
          <td>${huanmap.tui_yu_goods+huanmap.tui_ali_goods}</td>
        </tr>


      <tr>
          <td>会员更换档口超过2次支付费用</td>
          <td>${shippedmap.changestallgoodsnum}</td>
          <td>${shippedmap.ali_changestall}</td>
          <td>${shippedmap.yu_changestall}</td>
          <td>${shippedmap.ali_changestall+shippedmap.yu_changestall}</td>
      </tr>


        <tr>
          <td>会员支付退换货服务费</td>
          <td>退货:${tuimap.paytuiservicegoodsnum}<br/>
              换货:${huanmap.paytuiservicegoodsnum}
          </td>
          <td>退货:${tuimap.tui_ali_service}<br/>
              换货:${huanmap.tui_ali_service}
          </td>
          <td>退货:${tuimap.tui_yu_service}<br/>
              换货:${huanmap.tui_yu_service}
          </td>
          <td>${huanmap.tui_yu_service+huanmap.tui_ali_service+tuimap.tui_yu_service+tuimap.tui_ali_service}</td>
        </tr>



        <tr>
          <td>会员支付换货运费</td>
          <td>${huanmap.payhuanshipgoodsnum}</td>
          <td>${huanmap.tui_ali_ship}</td>
          <td>${huanmap.tui_yu_ship}</td>
          <td>${huanmap.tui_yu_ship+huanmap.tui_ali_ship}</td>
        </tr>

        <tr>
          <td>退货档口回款</td>
          <td>${tuimap.huikangoodsnum}</td>
          <td colspan="3" style="color: red">${tuimap.tui_huikuan}</td>
        </tr>

      </tbody>


    </table>

  </body>
</html>

