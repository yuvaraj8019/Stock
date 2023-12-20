package com.stock.whishlist.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stock.whishlist.dto.WhishList;

@Repository
public interface WhishListRepo extends JpaRepository<WhishList, Long>{
	
	List<WhishList> findByUserId(String userId);
	
	Optional<WhishList> findBySymbol(String symbol);
	
	boolean existsBySymbol(String symbol);
}
