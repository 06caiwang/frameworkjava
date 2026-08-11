package com.zyh.fileservice.service.impl;

import com.zyh.fileservice.domain.dto.FileDTO;
import com.zyh.fileservice.domain.dto.SignDTO;
import com.zyh.fileservice.service.IFileService;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author zhangyuheng
 */
public class OSSFileServiceImpl implements IFileService {

    @Override
    public FileDTO upload(MultipartFile file) {
        return null;
    }

    @Override
    public SignDTO getSign() {
        return null;
    }
}
