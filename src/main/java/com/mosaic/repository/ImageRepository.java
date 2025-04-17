package com.mosaic.repository;

import com.mosaic.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findAllByProductVariantId(Long id);
}
