package com.spboot.app.controller;

import com.alibaba.fastjson.JSON;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.jntoo.db.DB;
import com.jntoo.db.utils.Convert;
import com.spboot.app.pojo.*;
import com.spboot.app.utils.Base64Utils;
import com.spboot.app.utils.JwtTokenUtils;
import com.spboot.app.utils.R;
import com.spboot.app.utils.SessionFactory;
import io.swagger.annotations.Api;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = { "支付" })
@RestController
@RequestMapping("/api")
public class PaymentController {

    @Resource
    HttpServletRequest request;

    /**
     * 处理支付
     * @return String
     */
    @Transactional(rollbackFor = Exception.class)
    @RequestMapping("/payment")
    public String payment() {
        String id, biao;
        if (request.getParameter("out_trade_no") != null) {
            String[] out_trade_no = request.getParameter("out_trade_no").split("\\-");
            if (out_trade_no.length < 3) {
                return JSON.toJSONString(R.error("订单号格式错误"));
            }
            id = out_trade_no[2];
            biao = out_trade_no[1];
        } else {
            id = request.getParameter("id");
            biao = request.getParameter("biao");
        }

        // 白名单校验，避免任意表更新
        if (!"jingpaidingdan".equals(biao)) {
            return JSON.toJSONString(R.error("不支持的支付业务类型"));
        }

        if("bank".equals(request.getParameter("payType"))){
            Yonghu yonghu = DB.name(Yonghu.class).find(SessionFactory.getId());
            double zonge = Convert.toDouble(request.getParameter("zongji"));
            if(yonghu.getQianbaoyue() < zonge){
                return JSON.toJSONString(R.error("账户余额不足"));
            }
            yonghu.setQianbaoyue(yonghu.getQianbaoyue() - zonge);
            DB.name(yonghu).update(yonghu);

        }
        Jingpaidingdan order = DB.name(Jingpaidingdan.class).find(id);
        if (order == null || order.getId() == null) {
            return JSON.toJSONString(R.error("订单不存在"));
        }
        if (!"是".equals(order.getIszf())) {
            DB.name("jingpaidingdan").where("id", order.getId()).setField("iszf", "是");
            DB.name("jingpaidingdan").where("id", order.getId()).setField("dingdanzhuangtai", "待发货");
            DB.name("shangpinxinxi").where("shangpinbianhao", order.getShangpinbianhao()).setField("shangpinzhuangtai", "已结束");

            Shangjia shangjia = DB.name(Shangjia.class).where("zhanghao", order.getFaburen()).find();
            if (shangjia != null) {
                shangjia.setQianbaoyue((shangjia.getQianbaoyue() == null ? 0D : shangjia.getQianbaoyue()) + (order.getZhifujine() == null ? 0D : order.getZhifujine()));
                DB.name(shangjia).update(shangjia);
            }
        }



        if (request.getParameter("out_trade_no") != null) {
            return "<script>\n" + "    window.opener.postMessage(\"payment-success\" , \"*\");\n" + "    window.close();\n" + "</script>";
        }

        return JSON.toJSONString(R.success("ok"));
    }
}
