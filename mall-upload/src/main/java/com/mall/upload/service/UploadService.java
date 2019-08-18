package com.mall.upload.service;

import com.mall.common.enums.ExceptionEnum;
import com.mall.common.exception.MallException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class UploadService {
    private static final List<String> ALLOW_TYPES = Arrays.asList("image/png","image/jpng");
    public String uploadImage(MultipartFile file) {
        try {
            //文件类型校验
            String contentType = file.getContentType();
            if (!ALLOW_TYPES.contains(contentType)) {
                throw new MallException(ExceptionEnum.INVALID_FILE_TYPE);
            }

            //文件类型校验
            BufferedImage image = ImageIO.read(file.getInputStream());
            if (image == null) {
                throw new MallException(ExceptionEnum.INVALID_FILE_TYPE);
            }


            File dest = new File("F:\\githubpro\\mall-demo\\upload",file.getOriginalFilename());
            file.transferTo(dest);
            return "http://image.mall.com"+file.getOriginalFilename();
        } catch (IOException e) {
           log.error("上传文件失败",e);
            throw new MallException(ExceptionEnum.UPLOAD_ERROR);

        }

    }
}
