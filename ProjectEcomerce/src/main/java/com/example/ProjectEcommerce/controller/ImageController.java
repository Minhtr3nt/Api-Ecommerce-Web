package com.example.ProjectEcommerce.controller;

import com.example.ProjectEcommerce.dto.ImageDto;
import com.example.ProjectEcommerce.exceptions.ResourceNotFoundException;
import com.example.ProjectEcommerce.model.Image;
import com.example.ProjectEcommerce.reponse.ApiResponse;
import com.example.ProjectEcommerce.service.image.IImageService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/images")
public class    ImageController {
    private final IImageService imageService;
    private final ModelMapper modelMapper;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse> saveImage(@RequestParam List<MultipartFile> files,@RequestParam Long productId){
        try {
            List<ImageDto> imageDtos = imageService.saveImage(files, productId);
            return ResponseEntity.ok(new ApiResponse("Upload Success", imageDtos));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Upload failed", e.getMessage()));
        }
    }
    @GetMapping("/image/download/{imageId}")
    public ResponseEntity<Resource> downloadImage(@PathVariable Long imageId) throws SQLException {
        Image image = imageService.getImageById(imageId);
        ByteArrayResource resource = new ByteArrayResource(image.getImage()
                .getBytes(1, (int) image.getImage().length()));
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(image.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+image.getFileName()+ "\"")
                .body(resource);
    }
    @GetMapping("/get-img-by-product/{productId}")
    public ResponseEntity<ApiResponse> getImgByProduct(@PathVariable Long productId){
        List<Image> images = imageService.getImageByProduct(productId);
        List<ImageDto> imgs =  images.stream().map((element) -> modelMapper.map(element, ImageDto.class)).collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponse("List image", imgs));
    }

    @PutMapping("/image/{imageId}/update")
    public ResponseEntity<ApiResponse> updateImage(@PathVariable Long imageId, @RequestBody MultipartFile file){
        try {
            Image image = imageService.getImageById(imageId);
            if(image != null){
                imageService.updateImage(file, imageId);
                return  ResponseEntity.ok(new ApiResponse("Update Success!", null));
            }
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Update failed!!", null));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Update failed!!", HttpStatus.INTERNAL_SERVER_ERROR));
    }
    @DeleteMapping("/image/{imageId}/delete")
    public ResponseEntity<ApiResponse> deleteImage(@PathVariable Long imageId){
        try {
            Image image = imageService.getImageById(imageId);
            if(image != null){
                imageService.deleteImageById( imageId);
                return  ResponseEntity.ok(new ApiResponse("Delete Success!", null));
            }
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Delete failed!!", null));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Delete failed!!", HttpStatus.INTERNAL_SERVER_ERROR));
    }

}
