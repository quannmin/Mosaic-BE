package com.mosaic.service.spec;

import com.mosaic.entity.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageService {
    Image createImage(Image image);
    Image updateImage(Long id, MultipartFile file);
    Image findImageById(Long id);
    Image findByMainUrlImageTrue();
    List<Image> findAllImages();
    void deleteImage(Long imageId);
    List<Image> findAllByProductVariantId(Long id);
}
