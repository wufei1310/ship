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
        <h3>今日支出统计(${startDate}~${endDate})</h3>
      </div>
        
        <table class="table table-bordered">
        <thead>
          <tr>
              <td colspan="1">#</td>
            <td>今日新订单(${newCount})</td>
            <td>非今日订单(${oldCount})</td>
            <td>总计</td>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td>验收商品</td>
            <td>${t_yan_goods}/商品数量：${t_yan_goods_count}</td>
            <td>${h_yan_goods}/商品数量：${h_yan_goods_count}</td>
            <td>${t_yan_goods+h_yan_goods}</td>
          </tr>
          <tr>
            <td>发货运费</td>
            <td>${t_fa_ship}</td>
            <td>${h_fa_ship}</td>
            <td>${t_fa_ship+h_fa_ship}</td>
          </tr>
          <tr>
            <td>客户取消商品退款</td>
            <td>${t_cancle_goods}/商品数量：${t_cancle_goods_count}</td>
            <td>${h_cancle_goods}/商品数量：${h_cancle_goods_count}</td>
            <td>${t_cancle_goods+h_cancle_goods}</td>
          </tr>
          <tr>
            <td>客户取消订单全部商品退运费</td>
            <td>${t_cancle_allgoods}</td>
            <td>${h_cancle_allgoods}</td>
            <td>${t_cancle_allgoods+h_cancle_allgoods}</td>
          </tr>
          <tr>
            <td>退货退款</td>
            <td>${t_tui}</td>
            <td>${h_tui}</td>
            <td>${t_tui+h_tui}</td>
          </tr>
          <tr>
            <td>空包退还代发费</td>
            <td>${t_kong}</td>
            <td>${h_kong}</td>
            <td>${t_kong+h_kong}</td>
          </tr>
          <tr>
            <td>总计</td>
            <td>${t_yan_goods+t_fa_ship+t_cancle_goods+t_cancle_allgoods+t_tui+t_kong}</td>
            <td>${h_yan_goods+h_fa_ship+h_cancle_goods+h_cancle_allgoods+h_tui+h_kong}</td>
            
            <td>${t_yan_goods+t_fa_ship+t_cancle_goods+t_cancle_allgoods+t_tui+t_kong+h_yan_goods+h_fa_ship+h_cancle_goods+h_cancle_allgoods+h_tui+h_kong}</td>
            
          </tr>
        </tbody>
      </table>
        
    </body>
</html>
