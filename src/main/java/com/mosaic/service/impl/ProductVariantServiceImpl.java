package com.mosaic.service.impl;

import com.mosaic.domain.request.ProductVariantCreateRequest;
import com.mosaic.domain.request.ProductVariantUpdateRequest;
import com.mosaic.domain.response.ProductVariantResponse;
import com.mosaic.entity.Image;
import com.mosaic.entity.ProductVariant;
import com.mosaic.exception.custom.ResourceNotFoundException;
import com.mosaic.mapper.ProductVariantMapper;
import com.mosaic.repository.ImageRepository;
import com.mosaic.repository.ProductVariantRepository;
import com.mosaic.service.spec.ImageService;
import com.mosaic.service.spec.ProductVariantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductVariantServiceImpl implements ProductVariantService {

    private final ProductVariantRepository productVariantRepository;
    private final S3Service s3Service;
    private final ProductVariantMapper productVariantMapper;
    private final ImageService imageService;
    private final ImageRepository imageRepository;

    @Override
    @Transactional
    public ProductVariantResponse createProductVariant(ProductVariantCreateRequest productVariantCreateRequest, MultipartFile[] image) {
        ProductVariant productVariant = productVariantMapper.toProductVariantCreate(productVariantCreateRequest);
        productVariant.setCreatedBy("");
        productVariantRepository.save(productVariant);
        List<Image> savedImages = new ArrayList<>();

        if(image != null && image.length > 0) {
            Arrays.stream(image).forEach(item -> {
                String urlImage = s3Service.uploadProductVariantImage(item);
                if(urlImage != null) {
                    Image entity = new Image();
                    entity.setUrlDownload(urlImage);
                    entity.setProductVariant(productVariant);
                    Image savedImage = imageService.createImage(entity);
                    savedImages.add(savedImage);
                }
            });
        }
        ProductVariantResponse productVariantResponse = productVariantMapper.toProductVariantResponse(productVariant);
        productVariantResponse.setImages(savedImages);
        return productVariantResponse;
    }

    @Override
    @Transactional
    public ProductVariantResponse updateProductVariant(Long productVariantId,
                                                       ProductVariantUpdateRequest productVariantUpdateRequest,
                                                       MultipartFile newImage,
                                                       MultipartFile[] newImages) {
        ProductVariant existingProductVariant = findProductVariantById(productVariantId);

        productVariantMapper.toProductVariantUpdate(productVariantUpdateRequest, existingProductVariant);
        existingProductVariant.setUpdatedBy("");

        if(productVariantUpdateRequest.getImageId() != null) {
            Image oldImage = imageService.findImageById(productVariantUpdateRequest.getImageId());
            if(newImage != null) {
                String urlImage = s3Service.uploadProductVariantImage(newImage);
                s3Service.deleteImage(oldImage.getUrlDownload());
                if(urlImage != null) {
                    oldImage.setUrlDownload(urlImage);
                    oldImage.setMainUrlImage(productVariantUpdateRequest.isMainUrlImage());
                }
            } else if(productVariantUpdateRequest.isMainUrlImage()) {
                Image currentImage = imageService.findByMainUrlImageTrue();
                currentImage.setMainUrlImage(false);
                imageRepository.save(currentImage);
                oldImage.setMainUrlImage(true);
            }
            imageRepository.save(oldImage);
        }

        if(newImages != null && newImages.length > 0) {
            Arrays.stream(newImages).forEach(item -> {
                if(item != null && item.getOriginalFilename() != null && !item.getOriginalFilename().isBlank()) {
                    String urlImage = s3Service.uploadProductVariantImage(item);
                    if (urlImage != null) {
                        Image entity = new Image();
                        entity.setUrlDownload(urlImage);
                        entity.setProductVariant(existingProductVariant);
                        imageService.createImage(entity);
                    }
                }
            });
        }

        List<Image> allImages = imageService.findAllByProductVariantId(productVariantId);
        Image mainImage = allImages.stream().filter(Image::isMainUrlImage).findFirst().orElseThrow(
                () -> new ResourceNotFoundException("Main image in product variant not found!"));
        if(mainImage != null) {
            existingProductVariant.setMainUrlImage(mainImage.getUrlDownload());
        }
        productVariantRepository.save(existingProductVariant);
        ProductVariantResponse productVariantResponse = productVariantMapper.toProductVariantResponse(existingProductVariant);
        productVariantResponse.setImages(allImages);
        return productVariantResponse;
    }

    @Override
    public ProductVariant findProductVariantById(Long id) {
        return productVariantRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(
                "Product variant!",
                "id",
                id.toString())
        );
    }

    @Override
    public ProductVariantResponse findProductVariantResponseById(Long id) {
        ProductVariant productVariant = productVariantRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(
                "Product variant!",
                "id",
                id.toString())
        );
        return productVariantMapper.toProductVariantResponse(productVariant);
    }

    @Override
    @Transactional
    public void deleteProductVariant(Long id) {
        ProductVariant productVariant = findProductVariantById(id);
        productVariant.getImages().forEach(item -> s3Service.deleteImage(item.getUrlDownload()));
        productVariantRepository.delete(productVariant);
    }

    @Override
    public List<ProductVariantResponse> findAllProductVariants() {
        return productVariantRepository.findAll().stream().map(productVariantMapper::toProductVariantResponse).collect(Collectors.toList());
    }
}
