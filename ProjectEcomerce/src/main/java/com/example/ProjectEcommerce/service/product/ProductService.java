package com.example.ProjectEcommerce.service.product;

import com.example.ProjectEcommerce.dto.ImageDto;
import com.example.ProjectEcommerce.dto.ProductDto;
import com.example.ProjectEcommerce.exceptions.AlreadyExistsException;
import com.example.ProjectEcommerce.model.Image;
import com.example.ProjectEcommerce.repository.ImageRepository;
import com.example.ProjectEcommerce.repository.ProductRepository;
import com.example.ProjectEcommerce.exceptions.ProductNotFoundException;
import com.example.ProjectEcommerce.model.Category;
import com.example.ProjectEcommerce.repository.CategoryRepository;
import com.example.ProjectEcommerce.model.Product;
import com.example.ProjectEcommerce.request.AddProductRequest;
import com.example.ProjectEcommerce.request.ProductUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {
    final private ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final ImageRepository imageRepository;


    @Override
    public Product addProduct(AddProductRequest request) {
        //check if the category is found in the DB
        // IF yes, set it as the new product
        //If no, then save it as a new cate
        //Then set as new product category.

        if(productExists(request.getName(), request.getBrand())){
            throw new AlreadyExistsException(request.getBrand()+" "
                    +request.getName()+" already exists, you may update product instead!!!");
        }

        Category category = Optional.ofNullable(categoryRepository.findByName(request.getCategory().getName()))
                .orElseGet(()->{
                    Category newCategory =  new Category(request.getCategory().getName());
                    return  categoryRepository.save(newCategory);
                });
        request.setCategory(category);
        return productRepository.save(createProduct(request, category));
    }

    private boolean productExists(String name, String brand){
        return productRepository.existsByNameAndBrand(name,brand);
    }
    private Product createProduct(AddProductRequest request, Category category){
        return new Product(
                request.getName(),
                request.getBrand(),
                request.getPrice(),
                request.getInventory(),
                request.getDescription(),
                category
        );
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(()-> new ProductNotFoundException("Product not found!"));
    }

    @Override
    public void deleteProductById(Long id) {
        productRepository.findById(id)
                .ifPresentOrElse(productRepository::delete,
                ()->{throw new ProductNotFoundException("Product not found");});
    }



    @Override
    public Product updateProduct(ProductUpdateRequest product, Long productId) {

            return productRepository.findById(productId)
                    .map(existingProduct->updateExistingProduct(existingProduct, product))
                    .map(productRepository::save)
                    .orElseThrow(()->new ProductNotFoundException("Product not found"));
    }
    private Product updateExistingProduct(Product existProduct, ProductUpdateRequest request){
        existProduct.setName(request.getName());
        existProduct.setBrand(request.getBrand());
        existProduct.setPrice(request.getPrice());
        existProduct.setInventory(request.getInventory());
        existProduct.setDescription(request.getDescription());
        Category category = categoryRepository.findByName(request.getCategory().getName());
        existProduct.setCategory(category);
        return existProduct;
    }
    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {

        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
        return productRepository.findByCategoryNameAndBrand(category, brand);
    }

    @Override
    public List<Product> getProductsByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public List<Product> getProductsByBrandAndName(String brand, String name) {
        return productRepository.findByBrandAndName(brand, name);
    }

    @Override
    public Long countProductsByBrandAndName(String brand, String name) {
        return productRepository.countByBrandAndName(brand, name);
    }
    @Override
    public List<ProductDto> getConvertedProducts(List<Product> products){
        return products.stream().map(this::convertToDto).toList();

    }

    @Override
    public ProductDto convertToDto(Product product){
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        List<Image> images = imageRepository.findByProductId(product.getId());
        List<ImageDto> imageDtos = images.stream()
                .map(image-> modelMapper.map(image, ImageDto.class))
                .toList();
        productDto.setImages(imageDtos);
        return productDto;
    }
}
