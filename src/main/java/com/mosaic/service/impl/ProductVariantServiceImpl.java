package com.mosaic.service.impl;

import com.mosaic.domain.request.ProductVariantCreateRequest;
import com.mosaic.domain.request.ProductVariantUpdateRequest;
import com.mosaic.domain.response.ProductVariantResponse;
import com.mosaic.entity.Image;
import com.mosaic.entity.ProductVariant;
import com.mosaic.exception.ElementNotFoundException;
import com.mosaic.mapper.ProductVariantMapper;
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

    @Override
    @Transactional
    public ProductVariantResponse createProductVariant(ProductVariantCreateRequest productVariantCreateRequest, MultipartFile[] image) {
        ProductVariant productVariant = productVariantMapper.toProductVariantCreate(productVariantCreateRequest);
        productVariant.setCreatedBy("");
        productVariantRepository.save(productVariant);

        return getProductVariantResponse(image, productVariant);
    }

    private ProductVariantResponse getProductVariantResponse(MultipartFile[] image, ProductVariant productVariant) {
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
                                                       MultipartFile[] newImages) {
        ProductVariant existingProductVariant = productVariantRepository.findById(productVariantId)
                .orElseThrow(() -> new ElementNotFoundException("Product variant not found"));

        productVariantMapper.toProductVariantUpdate(productVariantUpdateRequest, existingProductVariant);
        existingProductVariant.setUpdatedBy("");
        productVariantRepository.save(existingProductVariant);

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

        ProductVariantResponse productVariantResponse = productVariantMapper.toProductVariantResponse(existingProductVariant);
        productVariantResponse.setImages(allImages);
        return productVariantResponse;
    }

    @Override
    public ProductVariantResponse findProductVariantById(Long id) {
        ProductVariant productVariant = productVariantRepository.findById(id).orElseThrow(() -> new ElementNotFoundException("Product variant not found!"));
        return productVariantMapper.toProductVariantResponse(productVariant);
    }

    @Override
    @Transactional
    public void deleteProductVariant(Long id) {
        ProductVariant productVariant = productVariantRepository.findById(id).orElseThrow(() -> new ElementNotFoundException("Product variant not found!"));
        productVariant.getImages().forEach(item -> s3Service.deleteImage(item.getUrlDownload()));
        productVariantRepository.delete(productVariant);
    }

    @Override
    public List<ProductVariantResponse> findAllProductVariants() {
        return productVariantRepository.findAll().stream().map(productVariantMapper::toProductVariantResponse).collect(Collectors.toList());
    }
}
