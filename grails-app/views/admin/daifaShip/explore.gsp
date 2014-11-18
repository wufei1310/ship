<%@page contentType="application/vnd.ms-excel;charset=UTF-8" language="java"%>

<%
   response.addHeader("Content-Disposition", "attachment;filename="+new String( file.getBytes("gb2312"), "ISO8859-1" ));
   %>
<html>
  <head>
    <title>金士代发</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <style>
      table td{mso-number-format:"\@";}
　　</style>
  </head>
  <body>


    <table width="100%" border="1" cellpadding="2" cellspacing="0">
          <thead>
            <tr>
              <th>序号</th>
              <th>备注</th>
              <th>寄件人姓名</th>
              <th>始发地</th>
              <th>寄件人详细地址</th>
              <th>寄件人电话</th>
              <th>收件人姓名</th>
              <th>目的地</th>
              <th>收件人详细地址</th>
              <th>收件人电话</th>
              %{--<th>拿齐货时间</th>--}%
            </tr>
          </thead>
          <tbody>

           <g:each status="i" in="${list}" var="daifa">
             <tr>
                  <td>${i+1}</td>
                  <td>${daifa.orderSN} 件数：${num_map[daifa.id]}</td>
                  <td>${daifa.sendperson}</td>
                  <td>广州</td>
                  <td>广州市天河区先烈东路虚地街５号３０２</td>
                  <td> 18677050002
                      %{--${daifa.sendcontphone}--}%
                  </td>
                  <td>${daifa.reperson}</td>
                  <td><g:areaProvice area_id="${daifa.area_id}" /></td>
                  <td><g:areaName area_id="${daifa.area_id}" />&nbsp;${daifa.address}</td>
                  <td>${daifa.contphone}</td>
                  %{--<td>${daifa.canExport_date}</td>--}%
             </tr>
            </g:each>

          </tbody>
        </table>

  </body>
</html>

