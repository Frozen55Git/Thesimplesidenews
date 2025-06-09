package com.thesimpleside.news.controller;

import com.thesimpleside.news.model.Stock;
import com.thesimpleside.news.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/stocks")
@RequiredArgsConstructor
public class StockController {
    
    private final StockService stockService;
    
    @GetMapping
    public ResponseEntity<List<Stock>> getAllStocks() {
        return ResponseEntity.ok(stockService.findAll());
    }
    
    @GetMapping("/{ticker}")
    public ResponseEntity<Stock> getStockByTicker(@PathVariable String ticker) {
        return stockService.findByTicker(ticker)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<Stock>> searchStocks(
            @RequestParam(required = false) String ticker,
            @RequestParam(required = false) String companyName) {
        if (ticker != null) {
            return ResponseEntity.ok(stockService.findByTickerContainingIgnoreCase(ticker));
        } else if (companyName != null) {
            return ResponseEntity.ok(stockService.findByCompanyNameContainingIgnoreCase(companyName));
        }
        return ResponseEntity.ok(stockService.findAll());
    }
    
    @GetMapping("/industry/{industry}")
    public ResponseEntity<List<Stock>> getStocksByIndustry(@PathVariable String industry) {
        return ResponseEntity.ok(stockService.findByIndustry(industry));
    }
    
    @GetMapping("/sector/{sector}")
    public ResponseEntity<List<Stock>> getStocksBySector(@PathVariable String sector) {
        return ResponseEntity.ok(stockService.findBySector(sector));
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Stock> createStock(@RequestBody Stock stock) {
        return ResponseEntity.ok(stockService.save(stock));
    }
    
    @PutMapping("/{ticker}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Stock> updateStock(
            @PathVariable String ticker,
            @RequestBody Stock updatedStock) {
        return stockService.findByTicker(ticker)
                .map(existingStock -> {
                    updatedStock.setId(existingStock.getId());
                    return ResponseEntity.ok(stockService.save(updatedStock));
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{ticker}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteStock(@PathVariable String ticker) {
        stockService.delete(ticker);
        return ResponseEntity.ok().build();
    }
} 