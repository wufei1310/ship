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
        <h3>今日收入明细(${startDate}~${endDate})</h3>
      </div>
        
        <table class="table table-bordered">
        <thead>
          <tr>
              <td colspan="2">#</td>
            <td>支付宝收入</td>
            <td>会员账户余额收入</td>
            <td>小计</td>
            <td>合计</td>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td rowspan="5">今日新订单</td>
            <td>支付商品</td>
            <td>${t_ali_goods}</td>
            <td>${t_yu_goods}</td>
            <td>${t_yu_goods+t_ali_goods}</td>
            <td rowspan="5">${t_ali_goods+t_ali_ship+t_ali_service+t_ali_bugoods+t_ali_buship+t_yu_goods+t_yu_ship+t_yu_service+t_yu_bugoods+t_yu_buship}</td>
          </tr>
          <tr>
            <td>支付运费</td>
            <td>${t_ali_ship}</td>
            <td>${t_yu_ship}</td>
            <td>${t_yu_ship+t_ali_ship}</td>
          </tr>
          <tr>
            <td>支付服务费</td>
            <td>${t_ali_service}</td>
            <td>${t_yu_service}</td>
            <td>${t_yu_service+t_ali_service}</td>
          </tr>
          <tr>
            <td>商品补款</td>
            <td>${t_ali_bugoods}</td>
            <td>${t_yu_bugoods}</td>
            <td>${t_yu_bugoods+t_ali_bugoods}</td>
          </tr>
          <tr>
            <td>运费补款</td>
            <td>${t_ali_buship}</td>
            <td>${t_yu_buship}</td>
            <td>${t_yu_buship+t_ali_buship}</td>
          </tr>
          <tr>
            <td rowspan="5">非今日订单</td>
            <td>支付商品</td>
            <td>${h_ali_goods}</td>
            <td>${h_yu_goods}</td>
            <td>${h_yu_goods+h_ali_goods}</td>
            <td rowspan="5">${h_ali_goods+h_ali_ship+h_ali_service+h_ali_bugoods+h_ali_buship+h_yu_goods+h_yu_ship+h_yu_service+h_yu_bugoods+h_yu_buship}</td>
          </tr>
          <tr>
            <td>支付运费</td>
            <td>${h_ali_ship}</td>
            <td>${h_yu_ship}</td>
            <td>${h_yu_ship+h_ali_ship}</td>
          </tr>
          <tr>
            <td>支付服务费</td>
            <td>${h_ali_service}</td>
            <td>${h_yu_service}</td>
            <td>${h_yu_service+h_ali_service}</td>
          </tr>
          <tr>
            <td>商品补款</td>
            <td>${h_ali_bugoods}</td>
            <td>${h_yu_bugoods}</td>
            <td>${h_yu_bugoods+h_ali_bugoods}</td>
          </tr>
          <tr>
            <td>运费补款</td>
            <td>${h_ali_buship}</td>
            <td>${h_yu_buship}</td>
            <td>${h_yu_buship+h_ali_buship}</td>
          </tr>
          
          
          
          <tr>
            <td rowspan="4">退换货</td>
            <td>支付换货差价</td>
            <td>${tui_ali_goods}</td>
            <td>${tui_yu_goods}</td>
            <td>${tui_yu_goods+tui_ali_goods}</td>
            <td rowspan="4">${tui_yu_goods+tui_ali_goods+tui_ali_ship+tui_yu_ship+tui_yu_service+tui_ali_service+tui_huikuan}</td>
          </tr>
          <tr>
            <td>支付换货运费</td>
            <td>${tui_ali_ship}</td>
            <td>${tui_yu_ship}</td>
            <td>${tui_ali_ship+tui_yu_ship}</td>
          </tr>
          <tr>
            <td>支付服务费</td>
            <td>${tui_ali_service}</td>
            <td>${tui_yu_service}</td>
            <td>${tui_yu_service+tui_ali_service}</td>
          </tr>
          <tr>
            <td>已退货档口回款</td>
            <td colspan="3">${tui_huikuan}</td>
          </tr>
          
          <tr>
            <td>总计</td>
            <td></td>
            <td>${tui_ali_service+tui_ali_ship+tui_ali_goods+t_ali_goods+t_ali_ship+t_ali_service+t_ali_bugoods+t_ali_buship+h_ali_goods+h_ali_ship+h_ali_service+h_ali_bugoods+h_ali_buship}<span style="color: red">(不含档口回款)</span></td>
            <td>${tui_yu_service+tui_yu_ship+tui_yu_goods+t_yu_goods+t_yu_ship+t_yu_service+t_yu_bugoods+t_yu_buship+h_yu_goods+h_yu_ship+h_yu_service+h_yu_bugoods+h_yu_buship}<span style="color: red">(不含档口回款)</span></td>
            <td>${tui_yu_service+tui_yu_ship+tui_yu_goods+tui_ali_service+tui_ali_ship+tui_ali_goods+t_ali_goods+t_ali_ship+t_ali_service+t_ali_bugoods+t_ali_buship+h_ali_goods+h_ali_ship+h_ali_service+h_ali_bugoods+h_ali_buship+t_yu_goods+t_yu_ship+t_yu_service+t_yu_bugoods+t_yu_buship+h_yu_goods+h_yu_ship+h_yu_service+h_yu_bugoods+h_yu_buship}<span style="color: red">(不含档口回款)</span></td>
            <td>&nbsp;</td>
          </tr>
        </tbody>
      </table>
        ${yue_str}
    </body>
</html>

