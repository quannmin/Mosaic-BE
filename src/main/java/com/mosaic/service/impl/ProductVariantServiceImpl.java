package com.mosaic.service.impl;

import com.mosaic.domain.request.ProductVariantCreateRequest;
import com.mosaic.domain.response.ProductVariantResponse;
import com.mosaic.entity.Image;
import com.mosaic.entity.ProductVariant;
import com.mosaic.mapper.ProductVariantMapper;
import com.mosaic.repository.ProductVariantRepository;
import com.mosaic.service.spec.ImageService;
import com.mosaic.service.spec.ProductVariantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductVariantServiceImpl implements ProductVariantService {

    private final ProductVariantRepository productVariantRepository;
    private final S3Service s3Service;
    private final ProductVariantMapper productVariantMapper;
    private final ImageService imageService;

    @Override
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
}
