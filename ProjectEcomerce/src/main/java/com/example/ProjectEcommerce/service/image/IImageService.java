package com.example.ProjectEcommerce.service.image;


import com.example.ProjectEcommerce.dto.ImageDto;
import com.example.ProjectEcommerce.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageService {
    Image getImageById(Long id);
    void deleteImageById(Long id);
    List<ImageDto> saveImage(List<MultipartFile> files, Long productId);
    void updateImage(MultipartFile file, Long imageId);
    List<Image> getImageByProduct(Long product_id);
}
