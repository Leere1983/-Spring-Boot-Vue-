package com.spboot.app.service;

import com.spboot.app.utils.R;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 *   上传处理的逻辑接口类
 */
public interface UploadService {
    public R<Object> save(MultipartFile fujian) throws IOException;
}
