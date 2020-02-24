package com.example.springwebcrawler.repository;

import com.example.springwebcrawler.model.RestaurantReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantReportRepository extends JpaRepository<RestaurantReport, Long> {

}