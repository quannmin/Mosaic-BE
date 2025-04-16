package com.mosaic.service.impl;

import com.mosaic.entity.Image;
import com.mosaic.repository.ImageRepository;
import com.mosaic.service.spec.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;

    @Override
    public Image createImage(Image image) {
        return imageRepository.save(image);
    }
}
