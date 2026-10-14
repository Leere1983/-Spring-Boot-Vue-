package com.spboot.app.controller;

import com.jntoo.db.utils.StringUtil;
import com.spboot.app.service.UploadService;
import com.spboot.app.utils.FileUnitUtils;
import com.spboot.app.utils.R;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

// 上传控制器
@RestController
@RequestMapping("/api/upload")
/**
 * 上传文件控制器
 */
public class UploadController {
    /**
     * 允许上传最大限制
     */
    private static final String ALLOW_FILE_LENGTH = "10GB";

    /**
     * 允许上传的类型
     */
    private static final String[] ALLOW_EXTENSIONS = new String[]{
            // 图片
            "jpg", "jpeg", "png", "gif", "bmp","webp",
            // 压缩包
            "zip", "rar", "gz", "7z", "cab","tmp",
            // 音视频,
            "wav", "avi", "mp4", "mp3", "m3u8", "ogg", "wma", "wmv", "rm", "rmvb", "aac", "mov", "asf", "flv","m4a",
            // 文档
            "doc", "docx", "xls", "xlsx", "ppt", "pptx", "pot", "txt", "csv", "md", "pdf"
    };

    // 上传文件逻辑处理类
    @Autowired
    private UploadService uploadService;

    /**
     * 判断文件名是否允许上传
     * @param fileName 文件名
     * @return
     */
    public boolean checkAllow(String fileName) {
        String ext = FilenameUtils.getExtension(fileName).toLowerCase();
        if(StringUtil.isNullOrEmpty(ext)){
            ext = "tmp";
        }
        // 循环比对允许的上传文件后缀
        for (String allowExtension : ALLOW_EXTENSIONS) {
            if (allowExtension.toLowerCase().equals(ext)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检测文件长度是否超出设置的值
     * @param size
     * @return
     */
    public boolean checkFileSizeLength(long size){
        if(size <= FileUnitUtils.unitToLong(ALLOW_FILE_LENGTH)){
            return true;
        }
        return false;
    }

    /**
     * 保存文件到本地服务器
     * @param fujian
     * @return
     * @throws IOException
     */
    @PostMapping("save")
    public R<Object> upload(@RequestPart(value = "fujian") MultipartFile fujian)  throws IOException
    {
        // 获取文件名
        String fileName = fujian.getOriginalFilename();
        // 检测是否允许这个格式的文件上传
        if(!checkAllow(fileName)){
            return R.error("文件类型不匹配，无法上传");
        }
        // 检测文件长度是否超出设置的值
        if(!checkFileSizeLength(fujian.getSize())){
            return R.error("文件长度超出："+ALLOW_FILE_LENGTH+"，无法上传");
        }
        return uploadService.save(fujian);
    }
}
