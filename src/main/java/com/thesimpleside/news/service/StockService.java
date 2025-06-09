package com.thesimpleside.news.service;

import com.thesimpleside.news.model.Stock;
import java.util.List;
import java.util.Optional;

public interface StockService {
    Stock save(Stock stock);
    Optional<Stock> findByTicker(String ticker);
    List<Stock> findAll();
    List<Stock> findByTickerContainingIgnoreCase(String ticker);
    List<Stock> findByCompanyNameContainingIgnoreCase(String companyName);
    List<Stock> findByIndustry(String industry);
    List<Stock> findBySector(String sector);
    void updateStockPrice(String ticker, double price);
    void delete(String ticker);
} 