package com.stock.app.service;

import org.springframework.http.ResponseEntity;

public interface StockService {

	public ResponseEntity<?> getStocksByCountryName(String countryName);
}
