package com.spboot.app.service.impl;

import cn.hutool.core.io.FileUtil;
import com.jntoo.db.utils.StringUtil;
import com.spboot.app.config.Configure;
import com.spboot.app.service.UploadService;
import com.spboot.app.utils.R;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * 保存在本地文件上传处理类
 */
@Service("UploadService")
public class LocalUploadService implements UploadService {

    @Override
    public R<Object> save(MultipartFile fujian) throws IOException {
        // 获取文件名
        String fileName = fujian.getOriginalFilename();
        // 获取文件后缀名
        String suffix = FileUtil.getSuffix(fileName);
        // 获取文件的md5值
        String md5 = DigestUtils.md5DigestAsHex(fujian.getInputStream());
        // 没有后缀名，默认为tmp
        if (StringUtil.isNullOrEmpty(suffix)) {
            suffix = "tmp";
        }
        // 新的文件名为：MD5.后缀
        String newName = md5 + "." + suffix;
        // 保存文件的路径
        String uploadPath = Configure.UPLOAD_DIR + newName;
        // 创建File类
        File file = new File(uploadPath);
        // 强制创建文件夹目录
        FileUtils.forceMkdirParent(file);
        // 将文件保存
        FileUtils.copyInputStreamToFile(fujian.getInputStream(), file);
        // 返回前端访问的url路径
        String url = Configure.FILE_PREFIX + newName;
        return R.success(url);
    }
}
