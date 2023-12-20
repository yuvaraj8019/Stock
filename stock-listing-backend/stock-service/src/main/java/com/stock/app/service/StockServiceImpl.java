package com.stock.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.stock.app.dto.StockResponseDTO;
import com.stock.app.model.Stock;

@Service
public class StockServiceImpl implements StockService {

	@Value("${twelvedata.api.key}")
	private String apiKey;

	private static final String API_URL = "https://api.twelvedata.com/stocks?apikey=090bda1dd6134cc5aced2f316b9b5b31&country={country}";
   
	private final RestTemplate restTemplate;
    @Autowired
    public StockServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

	@Override
	public ResponseEntity<?> getStocksByCountryName(String countryName) {
		String apiUrl = API_URL.replace("{country}", countryName);
		StockResponseDTO forObject = restTemplate.getForObject(apiUrl, StockResponseDTO.class);
		List<Stock> stocks = forObject.getData();
		return new ResponseEntity<>(stocks, HttpStatus.BAD_REQUEST);
	}

}
