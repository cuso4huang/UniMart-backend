package com.example.unimart.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    
    @PostMapping("/publish")
    public ResponseEntity<?> publishProduct(@RequestBody ProductDTO product) {
        try {
            // 处理商品发布逻辑
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("发布失败：" + e.getMessage());
        }
    }
} 