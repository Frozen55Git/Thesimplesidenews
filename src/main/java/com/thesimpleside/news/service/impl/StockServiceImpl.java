package com.thesimpleside.news.service.impl;

import com.thesimpleside.news.model.Stock;
import com.thesimpleside.news.repository.StockRepository;
import com.thesimpleside.news.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class StockServiceImpl implements StockService {
    
    private final StockRepository stockRepository;
    
    @Override
    public Stock save(Stock stock) {
        return stockRepository.save(stock);
    }
    
    @Override
    public Optional<Stock> findByTicker(String ticker) {
        return stockRepository.findByTickerIgnoreCase(ticker);
    }
    
    @Override
    public List<Stock> findAll() {
        return stockRepository.findAll();
    }
    
    @Override
    public List<Stock> findByTickerContainingIgnoreCase(String ticker) {
        return stockRepository.findByTickerContainingIgnoreCase(ticker);
    }
    
    @Override
    public List<Stock> findByCompanyNameContainingIgnoreCase(String companyName) {
        return stockRepository.findByCompanyNameContainingIgnoreCase(companyName);
    }
    
    @Override
    public List<Stock> findByIndustry(String industry) {
        return stockRepository.findByIndustryIgnoreCase(industry);
    }
    
    @Override
    public List<Stock> findBySector(String sector) {
        return stockRepository.findBySectorIgnoreCase(sector);
    }
    
    @Override
    @Transactional
    public void updateStockPrice(String ticker, double price) {
        stockRepository.findByTickerIgnoreCase(ticker).ifPresent(stock -> {
            stock.setCurrentPrice(BigDecimal.valueOf(price));
            stockRepository.save(stock);
        });
    }
    
    @Override
    public void delete(String ticker) {
        stockRepository.deleteByTickerIgnoreCase(ticker);
    }
} 