package com.thesimpleside.news.repository;

import com.thesimpleside.news.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    Optional<Stock> findByTickerIgnoreCase(String ticker);
    List<Stock> findByTickerContainingIgnoreCase(String ticker);
    List<Stock> findByCompanyNameContainingIgnoreCase(String companyName);
    List<Stock> findByIndustryIgnoreCase(String industry);
    List<Stock> findBySectorIgnoreCase(String sector);
    void deleteByTickerIgnoreCase(String ticker);
} 