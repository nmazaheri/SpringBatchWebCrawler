package com.example.springwebcrawler.repository;

import com.example.springwebcrawler.model.Restaurant;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {

	@Query("SELECT r FROM Restaurant r WHERE r.jobId = :jobId")
	List<Restaurant> findByJobId(Long jobId);
}