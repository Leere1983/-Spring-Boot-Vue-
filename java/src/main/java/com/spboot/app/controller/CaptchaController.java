package com.spboot.app.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.spboot.app.utils.Base64Utils;
import com.spboot.app.utils.JwtTokenUtils;
import com.spboot.app.utils.R;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javax.imageio.ImageIO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 验证码控制器
 */
@RestController
@RequestMapping("/api/user")
public class CaptchaController {

    // jwt token 的工具类
    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    // 获取kaptcha 类库
    @Autowired
    private DefaultKaptcha kaptcha;

    /**
     * 获取验证码的数据，验证码数据为base64的加密格式，就是将bytes 结构数据转为了字符串的形式
     */
    @GetMapping("/captch")
    public R createCaptch() {
        // 获取验证码的数据
        String code = createRandomString(4);
        // 生成验证码的图片数据,使用了kaptcha 库生成验证码
        BufferedImage image = kaptcha.createImage(code);
        // 将验证码数据转化为字符串格式，方便返回给前端
        String base64 = createBase64Png(image);
        // 创建返回数据
        Map result = new HashMap();
        // 将code 验证码值加密成jwttoken 形式返回给前端，验证的时候进行解密，在进行比对
        result.put("token", jwtTokenUtils.generateToken(code, new HashMap()));
        // 数据库图片的连接路径
        result.put("url", base64);

        return R.success(result);
    }

    /**
     * 生成验证码字符串
     *
     * @param len
     * @return
     */
    private String createRandomString(int len) {
        // 生成随机数类
        Random random = new Random();
        // 返回的值
        String result = "";
        // 循环获取数据
        for (int i = 0; i < len; i++) {
            String rand = String.valueOf(random.nextInt(10));
            result += rand;
        }
        return result;
    }

    /**
     *  将验证码数据转化为字符串格式
     *  @return 返回base64数据
     */
    private String createBase64Png(BufferedImage bufferedImage) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(); //io流
            ImageIO.write(bufferedImage, "png", baos); //写入流中
            byte[] bytes = baos.toByteArray(); //转换成字节
            //BASE64Encoder encoder = new BASE64Encoder();
            String png_base64 = Base64Utils.encode(bytes); //encoder.encodeBuffer(bytes).trim(); //转换成base64串
            png_base64 = png_base64.replaceAll("\n", "").replaceAll("\r", ""); //删除 \r\n

            //        ImageIO.write(bufferedImage, "png", new File("D:/qrcode1.png"));
            //System.out.println("值为："+"data:image/jpg;base64,"+png_base64);
            return "data:image/png;base64," + png_base64;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
