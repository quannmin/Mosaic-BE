package com.mosaic.service.impl;

import com.mosaic.entity.Image;
import com.mosaic.exception.ElementNotFoundException;
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
                () -> new ElementNotFoundException("Image not found with id: " + imageId));
        s3Service.deleteImage(image.getUrlDownload());
        String newImageUrlDownload = s3Service.uploadProductVariantImage(newImage);
        image.setUrlDownload(newImageUrlDownload);
        return imageRepository.save(image);
    }

    @Override
    public Image findImageById(Long id) {
        return imageRepository.findById(id).orElseThrow(
                () -> new ElementNotFoundException("Image not found with id: " + id));
    }

    @Override
    public List<Image> findAllImages() {
        return imageRepository.findAll();
    }

    @Override
    public void deleteImage(Long imageId) {
        Image image = imageRepository.findById(imageId).orElseThrow(
                () -> new ElementNotFoundException("Image not found with id: " + imageId));
        s3Service.deleteImage(image.getUrlDownload());
        imageRepository.delete(image);
    }

    @Override
    public List<Image> findAllByProductVariantId(Long id) {
        return imageRepository.findAllByProductVariantId(id);
    }
}
