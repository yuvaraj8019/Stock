package com.stock.whishlist.service;

import org.springframework.http.ResponseEntity;

import com.stock.whishlist.dto.WhishList;

public interface WhishListService {
	
	public ResponseEntity<?> getWhishlistByUserId(String userId);
	
	public ResponseEntity<?> addStockToWhishlist(WhishList whishlist);
	
	public ResponseEntity<?> deleteFromWhishList(Long id);
	
	public ResponseEntity<?> updateWhishList(Long id,WhishList whishlist);
	
	public ResponseEntity<?> getAllStocksWhishlist();

}
