package com.example.productservice.service;
import com.example.productservice.config.ApplicationException;
import com.example.productservice.dto.ProductRequest;
import com.example.productservice.dto.ProductResponse;
import com.example.productservice.dto.request.OrderStockLineItem;
import com.example.productservice.dto.request.OrderStockNotifyRequest;
import com.example.productservice.dto.request.ProductFilter;
import com.example.productservice.entity.Product;
import com.example.productservice.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductResponse create(ProductRequest request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setIsDeleted(false);

        return toResponse(productRepository.save(product));
    }
    @CacheEvict(
            value = "product_detail",
            key = "#id",
            cacheManager = "redisCacheManager"
    )
    public ProductResponse update(String id, ProductRequest request) {
        Product product = getActiveEntityOrThrow(id);
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());

        return toResponse(productRepository.save(product));
    }

    public void softDelete(String id) {
        Product product = getActiveEntityOrThrow(id);
        product.setIsDeleted(true);
        productRepository.save(product);
    }



    @Cacheable(value = "product_detail" , key = "#id" )
    public Product getById(String id) {
    try

    {
        log.info("dfsdf");
        Thread.sleep(2000);
    }catch(
    InterruptedException e )

    {
        throw new RuntimeException(e);
    }

       return productRepository.findById(id)
               .orElseThrow(() -> new ApplicationException("product not found"));
    }



    public java.util.List<ProductResponse> search(ProductFilter filter) {
        if (filter == null || filter.getIds() == null || filter.getIds().isEmpty()) {
            return java.util.List.of();
        }

        return productRepository.findAllById(filter.getIds()).stream()
                .filter(p -> !Boolean.TRUE.equals(p.getIsDeleted()))
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public void applyOrderStock(OrderStockNotifyRequest request) {
        if (request == null || request.getItems() == null || request.getItems().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "items is required");
        }
        for (OrderStockLineItem line : request.getItems()) {
            if (line.getProductId() == null || line.getQuantity() == null || line.getQuantity() <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid line item");
            }
            Product product = productRepository.findById(line.getProductId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found: " + line.getProductId()));
            if (Boolean.TRUE.equals(product.getIsDeleted())) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found: " + line.getProductId());
            }
            if (product.getStock() < line.getQuantity()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient stock: " + line.getProductId());
            }
            product.setStock(product.getStock() - line.getQuantity());
            productRepository.save(product);
        }
    }

    private Product getActiveEntityOrThrow(String id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

        if (Boolean.TRUE.equals(product.getIsDeleted())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }

        return product;
    }

    private String safeSortBy(String sortBy) {
        if ("name".equals(sortBy) || "price".equals(sortBy) || "stock".equals(sortBy) || "id".equals(sortBy)) {
            return sortBy;
        }
        return "id";
    }

    private ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getStock()
        );
    }

    @Transactional // Rất quan trọng: Nếu lỗi 1 sản phẩm thì cả đơn hàng sẽ không bị trừ kho
    public void decreaseStock(OrderStockNotifyRequest request) {
        for (var item : request.getItems()) {
            // 1. Tìm sản phẩm trong DB
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm ID: " + item.getProductId()));

            // 2. Kiểm tra xem có đủ hàng không (Optional nhưng nên có)
            if (product.getStock() < item.getQuantity()) {
                throw new RuntimeException("Sản phẩm " + product.getName() + " đã hết hàng!");
            }

            // 3. Trừ kho
            int updatedStock = product.getStock() - item.getQuantity();
            product.setStock(updatedStock);

            // 4. LƯU XUỐNG DATABASE
            productRepository.save(product);

            System.out.println(">>> [DB Update] Sản phẩm: " + product.getName() + " | Kho mới: " + updatedStock);
        }
}
}

