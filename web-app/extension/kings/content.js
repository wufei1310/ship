var curURL = window.location.href;
var taobaoURL = "taobao.com/trade/itemlist"
var kingsdfURL = "memberDaiFaOrder/add";



function goodsObj(name,goods_sn,price,num,spec){
    var obj=new Object(); 
    obj.name = name;
    obj.goods_sn = goods_sn;
    obj.price=price;
    obj.num= num;
    obj.spec = spec;
    return obj
}

function addressObj(person,phone,address){
    var obj=new Object(); 
    obj.person = person;
    obj.phone=phone;
    obj.address= address;
    return obj
}


function kingsObj(goodsArr,address){
    var obj=new Object(); 
    obj.goodsArr = goodsArr;
    obj.address= address;
    return obj
}


var getOrderInfo = function(){
	var parentsTR = $(this).parents("tr");
	var detailTR = parentsTR.next(); 
	var detailLink = detailTR.find(".detail-link");

	$.ajax({ 
		url: detailLink.attr("href"), 
		dataType: "html",
		success:function(data) {    
			var logistics = $(data).find(".logistics-info tr:first-child");
			var logisticsinfo = logistics.find("td").html();
			var infoarr = logisticsinfo.split("，");
			var address = addressObj($.trim(infoarr[0]),$.trim(infoarr[1]),$.trim(infoarr[3]));
			//alert(JSON.stringify(address));

			var goodsArr = new Array();
			var items = $(data).find(".order-item");
			items.each(function(){
				var sku = $(this).find(".sku .props")
				var spec = sku.html().replace("<span>","").replace("<span>","").replace("</span>","").replace("</span>","");
				var price = $(this).find(".price").html();
				var num = $(this).find(".num").html();

				var goods = goodsObj("","",price,num,spec);
				goodsArr.push(goods);
				

				
			});
			//alert(JSON.stringify(goodsArr));


			var kings = kingsObj(goodsArr,address);
			var paramsjson = JSON.stringify(kings);
			//chrome.extension.sendRequest(kings); 
			//alert(JSON.stringify(kings));
			window.open("http://kingsdf.com/memberDaiFaOrder/add?paramsjson="+encodeURI(encodeURI(paramsjson.replace("#","号")))); 
		}
	});

}

if(curURL.indexOf(taobaoURL)>-1){
	var orderTD = $("#J_ListTable .order-hd td");
	var imgURL = chrome.extension.getURL("kings.png");
	var button = '<a class="kingsButton" href="javascript:void(0);" >委托金士代发</a>'
	orderTD.append(button);
	$(".kingsButton").bind("click",getOrderInfo);
}
if(curURL.indexOf(kingsdfURL)>-1){
	var kingsJSON = decodeURI(getQueryString('paramsjson'));
	var kingsOBJ =  eval('(' + kingsJSON + ')');


	$("#contphone").val(kingsOBJ.address.phone);
	$("#reperson").val(kingsOBJ.address.person);
	$("#address").val(kingsOBJ.address.address);


	$.each(kingsOBJ.goodsArr,function(n,value) {  
           if(n==0){
		var daiFaGoods = $(".daiFaGoods");
		daiFaGoods.find(".spec").val(value.spec);
		daiFaGoods.find(".price").val(value.price);
		daiFaGoods.find(".num").val(value.num);		
	   }else{
		var daiFaGoodsClone = $(".daiFaGoods").clone();
		daiFaGoodsClone.find(".spec").val(value.spec);
		daiFaGoodsClone.find(".price").val(value.price);
		daiFaGoodsClone.find(".num").val(value.num); 
		$(".daiFaGoods:last").after(daiFaGoodsClone)

		}

		
 	});  


}


function getQueryString(name) {

	
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");

    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]); return null;
}







