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
      <h3>支出统计(${startDate}~${endDate})</h3>
    </div>



    
    <table class="table table-bordered">

      <thead>
        <tr>
          <td >#</td>
          <td>金额</td>
          <td>商品数量</td>
          <td>合计</td>
        </tr>


      </thead>
      <tbody>

        <tr>
          <td>验收商品</td>
          <td>${yan_goods}</td>
          <td>${yanshougoodsnum}</td>
          <td rowspan="12">${killchu+changeStallchu+cancle_order_giftfee + alichu+yan_goods + huan_ship+kong+tui_goods+cancle_allgoods+cancle_goods+fa_ship+cha_goods}</td>
        </tr>

        <tr>
          <td>发货运费</td>
          <td>${fa_ship}</td>
          <td>${shipgoodsnum}</td>
        </tr>

        <tr>
          <td>会员取消商品退款</td>
          <td>${cancle_goods}</td>
          <td>${canclegoodsnum}</td>
        </tr>

        <tr>
          <td>会员取消商品(订单)退运费</td>
          <td>${cancle_allgoods}</td>
          <td>${canclegoodsnumship}</td>
        </tr>


      <tr>
          <td>会员取消商品(订单)退礼品费</td>
          <td>${cancle_order_giftfee}</td>
          <td></td>
      </tr>


        <tr>
          <td>会员退货退款至会员账户</td>
          <td>${tui_goods}</td>
          <td>${tuigoodsnum}</td>
        </tr>
        <tr>
          <td>空包退还代发费用</td>
          <td>${kong}</td>
          <td>${konggoodsnum}</td>
        </tr>
        <tr>
          <td>发货运费(换货)</td>
          <td>${huan_ship}</td>
          <td>${huanshipgoodsnum}</td>
        </tr>
        <tr>
          <td>换货差价支出</td>
          <td>${cha_goods}</td>
          <td>${chagoodsnum}</td>
        </tr>

      <tr>
          <td>换档口退货款</td>
          <td>${changeStallchu}</td>
          <td></td>
      </tr>

        <tr>
          <td>支付宝充值</td>
          
          <td style="color: red">${alichu}</td>
          <td>&nbsp;</td>
        </tr>
      </tr>

      <tr>
          <td>会员紧急中止发货，退货款运费</td>

          <td style="color: red">${killchu}</td>
          <td>&nbsp;</td>
      </tr>

      </tbody>

    </table>

    
    
    
    <div class="page-header">
      <h3>收入统计(${startDate}~${endDate})</h3>
    </div>
    
    <table class="table table-bordered">
      <thead>
        <tr>
          <td >#</td>
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
          <td>${paygoodsnum}</td>
          <td>${t_ali_goods}</td>
          <td>${t_yu_goods}</td>
          <td>${t_yu_goods+t_ali_goods}</td>
          <td rowspan="13">${killru + ali_changestall+yu_changestall + t_yu_gift+t_ali_gift + aliru+tui_huikuan+tui_yu_ship+tui_ali_ship+tui_yu_service+tui_ali_service+tui_yu_goods+tui_ali_goods+t_yu_buship+t_ali_buship+t_yu_bugoods+t_ali_bugoods+t_yu_service+t_ali_service+t_yu_ship+t_ali_ship+t_yu_goods+t_ali_goods}</td>
        </tr>
        <tr>
          <td>会员支付拿货运费</td>
          <td>${payshipgoodsnum}</td>
          <td>${t_ali_ship}</td>
          <td>${t_yu_ship}</td>
          <td>${t_yu_ship+t_ali_ship}</td>
        </tr>
        <tr>
          <td>会员支付服务费</td>
          <td>${payservicegoodsnum}</td>
          <td>${t_ali_service}</td>
          <td>${t_yu_service}</td>
          <td>${t_yu_service+t_ali_service}</td>
        </tr>


      <tr>
          <td>会员支付礼品费</td>
          <td></td>
          <td>${t_ali_gift}</td>
          <td>${t_yu_gift}</td>
          <td>${t_yu_gift+t_ali_gift}</td>
      </tr>

        <tr>
          <td>会员对商品补款</td>
          <td>${paybugoodsnum}</td>
          <td>${t_ali_bugoods}</td>
          <td>${t_yu_bugoods}</td>
          <td>${t_yu_bugoods+t_ali_bugoods}</td>
        </tr>

        <tr>
          <td>会员对运费补款</td>
          <td>${paybushipgoodsnum}</td>
          <td>${t_ali_buship}</td>
          <td>${t_yu_buship}</td>
          <td>${t_yu_buship+t_ali_buship}</td>
        </tr>



        <tr>
          <td>会员支付换货差价</td>
          <td>${payhuangoodsnum}</td>
          <td>${tui_ali_goods}</td>
          <td>${tui_yu_goods}</td>
          <td>${tui_yu_goods+tui_ali_goods}</td>
        </tr>

        <tr>
          <td>会员支付退货服务费</td>
          <td>${paytuiservicegoodsnum}</td>
          <td>${tui_ali_service}</td>
          <td>${tui_yu_service}</td>
          <td>${tui_yu_service+tui_ali_service}</td>
        </tr>



        <tr>
          <td>会员支付换货运费</td>
          <td>${payhuanshipgoodsnum}</td>
          <td>${tui_ali_ship}</td>
          <td>${tui_yu_ship}</td>
          <td>${tui_yu_ship+tui_ali_ship}</td>
        </tr>


      <tr>
          <td>会员更换档口超过2次支付费用</td>
          <td>${changestallgoodsnum}</td>
          <td>${ali_changestall}</td>
          <td>${yu_changestall}</td>
          <td>${ali_changestall+yu_changestall}</td>
      </tr>

        <tr>
          <td>退货档口回款</td>
          <td>${huikangoodsnum}</td>
          <td colspan="3" style="color: red">${tui_huikuan}</td>
        </tr>
        
        <tr>
          <td>支付宝充值</td>
          <td>&nbsp;</td>
          <td colspan="3" style="color: red">${aliru}</td>
        </tr>

      <tr>
          <td>会员紧急中止发货,收服务费</td>
          <td>&nbsp;</td>
          <td colspan="3" style="color: red">${killru}</td>
      </tr>

      </tbody>


    </table>
    
    
     <div class="page-header">
      <h3>会员账户余额报表</h3>
    </div>



    
    <table class="table table-bordered">

      <thead>
        <tr>
          <td>会员账户</td>
          <td>余额</td>
          <td>账户变动时间</td>
          <td>联系方式</td>
        </tr>


      </thead>
      <tbody>
        <g:set var="total" value="0" />
      <g:each in="${user}" status="i" var="u">
        <g:set var="total" value="${total + u.account.amount as float}" />

        <tr>
          <td >${u.email}</td>
          <td>${u.account.amount}</td>
          <td>${u.account.lastUpdated}</td>
          <td>${u.phone}</td>
        </tr>
      </g:each>
        <tr>
          <td>总计</td>
          <td>${total}</td>
          <td></td>
          <td></td>
        </tr>


      </tbody>

    </table>

  </body>
</html>

