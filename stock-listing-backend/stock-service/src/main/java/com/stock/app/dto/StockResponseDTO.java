package com.stock.app.dto;

import java.util.List;

import com.stock.app.model.Stock;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StockResponseDTO {

	private List<Stock> data;

	public StockResponseDTO(List<Stock> data) {
		super();
		this.data = data;
	}
}