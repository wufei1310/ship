package ship

import grails.converters.JSON

import javax.servlet.ServletOutputStream

class LoginController {
    def jcaptchaService

    def index() {}

    def doLogin() {
        //验证码不正确0，登录成功1,用户名密码不正确2
        try {
            if (!jcaptchaService.validateResponse("imageCaptcha", session.id, params.loginValidCode)) {

                flash.message = "验证码不正确";
                redirect(controller: 'index', action: "index", params: [reqAction: "reqLogin"])
                return;
            }
        } catch (Exception e) {
            flash.message = "验证码不正确";
            redirect(controller: 'index', action: "index", params: [reqAction: "reqLogin"])
            return;
        }

        def user = User.findByEmailAndPassword(params.email, params.loginPassword.encodeAsPassword())
        if (!user) {
            flash.message = "用户名密码不正确"
            redirect(controller: 'index', action: "index", params: [reqAction: "reqLogin"])
            return;
        } else {

            if (user.status == '1') {
                flash.message = "用户被冻结"
                redirect(controller: 'index', action: "index", params: [reqAction: "reqLogin"])
                return;
            }

            def loginPOJO = new LoginPOJO();
            loginPOJO.user = user;
            session.loginPOJO = loginPOJO;
//            redirect(controller: 'memberDaiFaOrder',action:"list")
            redirect(controller: 'memberNewProduct', action: "list")
            return
        }
    }

    def logout() {
        session.loginPOJO = null
        redirect(controller: "/")
    }


    def selError() {
        def tranloglist = TranLog.findAllByTypeAndDateCreatedBetween("3", Date.parse("yyyy-MM-dd HH:mm:ss", "2014-12-10" + " 00:00:00"), Date.parse("yyyy-MM-dd HH:mm:ss", "2014-12-25" + " 00:00:00"))
        tranloglist.each {
            def daifaorder = DaiFaOrder.findByOrderSN(it.orderSN)
            daifaorder.daiFaGoods.each {
                if(it.status=="1"){
                    render it.id + ":" + it.status
                    render "<br/>"
                }

            }
        }


    }



    def testData(){

        def ujson =   User.last() as JSON

        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        //下面那句是核心
        response.setHeader("Access-Control-Allow-Origin", "http://127.0.0.1/*");
        response.setHeader("Access-Control-Allow-Origin", "chrome-extension://dkdpelmhmoaandppkgjfllcdllkmojac");

        response.setDateHeader("Expires", 0);
        ServletOutputStream sos = response.getOutputStream();
        try {
            sos.write(ujson.toString().getBytes("GBK"));
        } catch (Exception e) {
            System.out.println(e)
        } finally {
            try {
                sos.close();
            } catch (Exception e) {
            }
        }

//
//        def ujson =   User.last() as JSON
//        def jsonp = params.jsonpcallback
//
//        def str =  jsonp +"("+ ujson.toString()+ ")"
//        println "返回jsonp========="
//        println str
//        render str
    }


}
