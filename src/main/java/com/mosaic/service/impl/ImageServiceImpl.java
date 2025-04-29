package com.mosaic.service.impl;

import com.mosaic.entity.Image;
import com.mosaic.exception.custom.ResourceNotFoundException;
import com.mosaic.repository.ImageRepository;
import com.mosaic.service.spec.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final S3Service s3Service;

    @Override
    public Image createImage(Image image) {
        return imageRepository.save(image);
    }

    @Override
    public Image updateImage(Long imageId, MultipartFile newImage) {
        Image image = imageRepository.findById(imageId).orElseThrow(
                () -> new ResourceNotFoundException("Image", "id", imageId));
        s3Service.deleteImage(image.getUrlDownload());
        String newImageUrlDownload = s3Service.uploadProductVariantImage(newImage);
        image.setUrlDownload(newImageUrlDownload);
        return imageRepository.save(image);
    }

    @Override
    public Image findImageById(Long id) {
        return imageRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Image", "id", id));
    }

    @Override
    public Image findByMainUrlImageTrue() {
        return imageRepository.findByMainUrlImageTrue()
                .orElseThrow(() -> new ResourceNotFoundException("Main image not found"));
    }

    @Override
    public List<Image> findAllImages() {
        return imageRepository.findAll();
    }

    @Override
    public void deleteImage(Long imageId) {
        Image image = findImageById(imageId);
        s3Service.deleteImage(image.getUrlDownload());
        imageRepository.delete(image);
    }

    @Override
    public List<Image> findAllByProductVariantId(Long id) {
        return imageRepository.findAllByProductVariantId(id);
    }
}
