package com.example.ProjectEcommerce.repository;

import com.example.ProjectEcommerce.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {


    List<Image> findByProductId(Long id);
}