package ship

import grails.converters.JSON

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

}
