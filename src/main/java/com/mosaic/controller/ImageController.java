package com.mosaic.controller;

import com.mosaic.domain.response.ApiResponse;
import com.mosaic.entity.Image;
import com.mosaic.service.spec.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @GetMapping()
    public ResponseEntity<ApiResponse<List<Image>>> getAllImages() {
        return ResponseEntity.ok(ApiResponse.<List<Image>>builder()
                .code(HttpStatus.OK.value())
                .message("Get all images successfully!")
                .data(imageService.findAllImages())
                .success(true)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Image>> getImage(@PathVariable("id") Long id) {
        return ResponseEntity.ok(ApiResponse.<Image>builder()
                .code(HttpStatus.OK.value())
                .message("Get image successfully!")
                .data(imageService.findImageById(id))
                .success(true)
                .build());
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Image>> updateImage(@PathVariable("id") Long id, @RequestPart("image") MultipartFile file) {
        return ResponseEntity.ok(ApiResponse.<Image>builder()
                .code(HttpStatus.OK.value())
                .message("Update image successfully!")
                .data(imageService.updateImage(id, file))
                .success(true)
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteImage(@PathVariable("id") Long id) {
        imageService.deleteImage(id);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                        .message("Delete image successfully!")
                        .code(HttpStatus.OK.value())
                        .success(true)
                .build());
    }
}
