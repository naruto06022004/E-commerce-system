package com.example.productservice.repository;

import com.example.productservice.entity.Product;
import jakarta.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, String> {
    List<Product> findByIdIn(List<String> ids);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query ("SELECT p from Product p where p.id in :in")
    List<Product> finByIdInForUpdate(@Param("ids") List<String> ids);
}

