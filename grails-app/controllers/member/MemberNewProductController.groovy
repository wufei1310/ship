package member

import com.taobao.api.DefaultTaobaoClient
import com.taobao.api.FileItem
import com.taobao.api.TaobaoClient
import com.taobao.api.internal.util.WebUtils
import com.taobao.api.request.ItemAddRequest
import com.taobao.api.request.ItemGetRequest
import com.taobao.api.request.ItemImgUploadRequest
import com.taobao.api.request.ItemSkuAddRequest
import com.taobao.api.request.PictureCategoryAddRequest
import com.taobao.api.request.PictureCategoryGetRequest
import com.taobao.api.request.PictureUploadRequest
import com.taobao.api.response.ItemAddResponse
import com.taobao.api.response.ItemGetResponse
import com.taobao.api.response.ItemImgUploadResponse
import com.taobao.api.response.ItemSkuAddResponse
import com.taobao.api.response.PictureCategoryAddResponse
import com.taobao.api.response.PictureCategoryGetResponse
import com.taobao.api.response.PictureUploadResponse
import grails.converters.JSON
import groovy.json.JsonSlurper
import groovyx.net.http.HTTPBuilder
import org.apache.http.client.HttpClient
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.support.PropertiesLoaderUtils
import ship.Market
import sysinit.SysInitParams

import java.awt.image.BufferedImage
import javax.imageio.ImageIO;


class MemberNewProductController {

    def memberXmlGoodsService;
    def isDev = false;

    def properties = PropertiesLoaderUtils.loadProperties(new ClassPathResource("sysSetting.properties"))

    TaobaoClient client = new DefaultTaobaoClient(getSdkUrl(), getAppKey(), getAppSecret());

    def getAppKey() {
        if (isDev) {
            return "1023024144"
        } else {
            return "23024144"
        }
    }

    def getAppSecret() {
        if (isDev) {
            return "sandbox138959a9ec941f6ed044dc41b"
        } else {
            return "576761b138959a9ec941f6ed044dc41b"
        }
    }

    def getAuthCodeUrl() {
        if (isDev) {
            return "https://oauth.tbsandbox.com/authorize"
        } else {
            return "https://oauth.taobao.com/authorize"
        }
    }


    def getTokenUrl() {
        if (isDev) {
            return "https://oauth.tbsandbox.com/token"
        } else {
            return "https://oauth.taobao.com/token"
        }
    }

    def getSdkUrl() {
        if (isDev) {
            return "http://gw.api.tbsandbox.com/router/rest"
        } else {
            return "http://gw.api.taobao.com/router/rest"
        }
    }




    def index() {}

    def list() {


        def p = params.p
        if (!p) {
            p = 1
        } else {

        }
        def market = Market.list().market_name
        def xmlGoodsList = memberXmlGoodsService.getXmlGoodsList(params,p as int);
        def map = [xmlGoodsList: xmlGoodsList, p: p,market:market]

        render(view: "/member/newProduct/list", model: map)
    }

    def goods() {
        def xmlGoods = memberXmlGoodsService.getXmlGoods(params.id as String)
        def map = [xmlGoods: xmlGoods]

        render(view: "/member/newProduct/detail", model: map)
    }


    def reqTaobao() {
        String baseUrl = "http://" + request.getServerName() + ":" + request.getServerPort()
        String return_url = baseUrl + "${createLink(action: 'taobaoapiredirect_uri')}";
        def url = getAuthCodeUrl() + "?client_id=" + getAppKey() + "&response_type=code&redirect_uri=" + return_url
        redirect(url: url);
    }


    def taobaoapiredirect_uri() {


        if (isDev) {
            println "获得淘宝授权码:" + "61009129723a5dcf3258102ae2b5cb90b3c73dc5168ebd22074082787"
            session.access_token = "61009129723a5dcf3258102ae2b5cb90b3c73dc5168ebd22074082787"
        } else {
            String baseUrl = "http://" + request.getServerName() + ":" + request.getServerPort()
            String return_url = baseUrl + "${createLink(action: 'taobaoapiredirect_uri')}";

            String url = getTokenUrl();
            Map<String, String> props = new HashMap<String, String>();
            props.put("grant_type", "authorization_code");
            /*测试时，需把test参数换成自己应用对应的值*/
            props.put("code", params.code);
            props.put("client_id", getAppKey());
            props.put("client_secret", getAppSecret());
            props.put("redirect_uri", return_url);
            props.put("view", "web");
            String s = "";
            try {
                s = WebUtils.doPost(url, props, 30000, 30000);
                def sessionjson = new JsonSlurper().parseText(s)
                System.out.println("当前access_token：" + sessionjson.access_token);
                session.access_token = sessionjson.access_token
                // render s
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        def map = []
        render(view: "/member/newProduct/oathsucc", model: map)
    }

//    def getTaobaoGoods2() {
//        println "开始读取商品信息"
//
//        TaobaoClient client = new DefaultTaobaoClient(getSdkUrl(), getAppKey(), getAppSecret());
//        ItemGetRequest req = new ItemGetRequest();
//        req.setFields("detail_url,num_iid,title,nick,type,cid,seller_cids,props,input_pids,input_str,desc,pic_url,num,valid_thru,list_time,delist_time,stuff_status,location,price,post_fee,express_fee,ems_fee,has_discount,freight_payer,has_invoice,has_warranty,has_showcase,modified,increment,approve_status,postage_id,product_id,auction_point,property_alias,item_img,prop_img,sku,video,outer_id,is_virtual");
//        req.setNumIid(params.id);
//        ItemGetResponse response = client.execute(req, session.access_token);
//        return response
//    }


    def addTaobaoGoods() {
        println "开始添加商品了,当前access_token：" + session.access_token


        if (session.access_token == null) {
            render "0";
            return;
        }

        if (isDev) {
            params.id = 2100554390350;
        }

        println "开始读取商品信息:"  +  params.id






        ItemGetRequest readreq = new ItemGetRequest();
        readreq.setFields("detail_url,num_iid,title,nick,type,cid,seller_cids,props,input_pids,input_str,desc,pic_url,num,valid_thru,list_time,delist_time,stuff_status,location,price,post_fee,express_fee,ems_fee,has_discount,freight_payer,has_invoice,has_warranty,has_showcase,modified,increment,approve_status,postage_id,product_id,auction_point,property_alias,item_img,prop_img,sku,video,outer_id,is_virtual");
        readreq.setNumIid(params.id as Long);
        ItemGetResponse response = client.execute(readreq, session.access_token);







        println "获取商品信息" + (response as JSON)


        ItemAddRequest req = new ItemAddRequest();
        req.setNum(response.getItem().getNum());
        req.setPrice(response.getItem().getPrice());
        req.setType(response.getItem().getType());
        req.setStuffStatus(response.getItem().getStuffStatus());

        if (isDev) {
            req.setTitle("沙箱测试"+response.getItem().getTitle());
        }else{
            req.setTitle(response.getItem().getTitle());
        }



        String descs = response.getItem().getDesc()
        //图片搬家
        tupianbangjia(descs)


        req.setDesc(response.getItem().getDesc());
        req.setLocationState(response.getItem().getLocation().getState());
        req.setLocationCity(response.getItem().getLocation().getCity());
        req.setCid(response.getItem().getCid());
        req.setHasInvoice(response.getItem().getHasInvoice())
        req.setHasWarranty(response.getItem().getHasWarranty())
        req.setProps(response.getItem().getProps())
        ItemAddResponse addresponse = client.execute(req, session.access_token);

        if(addresponse.getSubMsg()&&addresponse.getSubMsg().indexOf("您已发布过同类宝贝")>-1){
            render "2"
            return
        }


//        println "response.getItem().getSkus():" + response.getItem().getSkus()

        response.getItem().getSkus().each {
            ItemSkuAddRequest skureq = new ItemSkuAddRequest();
            skureq.setNumIid(addresponse.getItem().getNumIid());
            skureq.setProperties(it.properties);
            skureq.setQuantity(it.quantity);
            skureq.setPrice(it.price);
            skureq.setOuterId(it.outerId);
            ItemSkuAddResponse skurep = client.execute(skureq, session.access_token);
        }

        response.getItem().getItemImgs().eachWithIndex { it, i ->
//            println it.position
//            println it.url
            def filepath = download(it.url)
//            println  filepath
            FileItem fitem = new FileItem(new File(filepath));
            ItemImgUploadRequest imgreq = new ItemImgUploadRequest();
            imgreq.setNumIid(addresponse.getItem().getNumIid());
            imgreq.setPosition(it.position);
            imgreq.setImage(fitem);
            if (i == 0) {
                imgreq.setIsMajor(true)
            }


            ItemImgUploadResponse imgrep = client.execute(imgreq, session.access_token);
           // println imgrep as JSON;

        }


        println "=====================添加商品结束"
        println addresponse as JSON;
        render "1"
        return
    }


    public tihuanpic(descs,j,map){
        int s = descs.indexOf("img src=\"http:")
        if(s>-1){
            int e = descs.indexOf("webp")
            def taobaopic = descs[s+9..e+3];
            def downpath = download(taobaopic)
            map.put("P"+j,downpath)
            descs=descs.replaceAll(descs[s+9..e+3],"P"+j)
            j++;
            tihuanpic(descs,j,map)
        }else{
//            println map
//            println descs
//


            PictureCategoryGetRequest pictureCategoryGetRequest=new PictureCategoryGetRequest();
            pictureCategoryGetRequest.setPictureCategoryName("金士代发");
            PictureCategoryGetResponse pictureCategoryGetResponse = client.execute(pictureCategoryGetRequest , session.access_token);

            def picCategoryId;
            if(pictureCategoryGetResponse.getPictureCategories()==null){
                PictureCategoryAddRequest piccategoryreq=new PictureCategoryAddRequest();
                piccategoryreq.setPictureCategoryName("金士代发");
                piccategoryreq.setParentId(0L);
                PictureCategoryAddResponse picategoryresponse = client.execute(piccategoryreq , session.access_token);
                println "picategoryresponse:"+(picategoryresponse as JSON)
                picCategoryId =  picategoryresponse.getPictureCategory().pictureCategoryId
            }else{
                picCategoryId =  pictureCategoryGetResponse.getPictureCategories()[0].getPictureCategoryId();
            }

            map.each{k,v->
                if(v){



                    PictureUploadRequest req=new PictureUploadRequest();
                    req.setPictureCategoryId(picCategoryId);
                    FileItem fItem = new FileItem(new File(v));
                    req.setImg(fItem);

                    String fileName = v.substring(v.lastIndexOf("/") + 1);

                    req.setImageInputTitle(fileName);
                    req.setTitle(k);
                    req.setClientType("client:computer");
                    PictureUploadResponse response = client.execute(req , session.access_token);
                    descs = descs.replaceAll(k,response.getPicture().getPicturePath())
                }

            }
            return descs
        }

    }

    public tupianbangjia(descs){
        descs = descs + '<p><img src="http://img03.taobaocdn.com/imgextra/i3/255689516/1223.jpg_.webp"></p>' + descs
        int s = descs.indexOf("img")
        int e = descs.indexOf("webp")
        descs = tihuanpic(descs,1,[:])

    }


    public download(String strUrl) {
        String fileName = strUrl.substring(strUrl.lastIndexOf("/") + 1);
        fileName = fileName.replace("_.webp","")
        String path = properties.getProperty("taobaocache")
        File dir = new File(path);
        if (!dir.exists())
            dir.mkdir();
        path += fileName
        if(new File(path).exists()){
            return path;
        }
        URL url = null;
        try {
            url = new URL(strUrl.replace("_.webp",""));
        } catch (MalformedURLException e2) {
            e2.printStackTrace();
            return;
        }

        InputStream is = null;
        try {
            is = url.openStream();
        } catch (IOException e1) {
            e1.printStackTrace();
            return;
        }

        OutputStream os = null;
        try {
            os = new FileOutputStream(path);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = is.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            return path;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }

}

//

