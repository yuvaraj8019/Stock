package com.stock.whishlist.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.stock.whishlist.dto.WhishList;
import com.stock.whishlist.exception.ResourceAlreadyExistsException;
import com.stock.whishlist.exception.ResourceNotFoundException;
import com.stock.whishlist.repo.WhishListRepo;

@Service
public class WhishListServiceImpl implements WhishListService{

	@Autowired
	WhishListRepo whishListRepo;
	
	@Override
	public ResponseEntity<?> getWhishlistByUserId(String userId) {
		List<WhishList> whishlist = whishListRepo.findByUserId(userId);
		if(whishlist.isEmpty()) {
			throw new ResourceNotFoundException("item with given id not found");
		}
		return new ResponseEntity<>(whishlist, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> addStockToWhishlist(WhishList whishlist) {
		if(whishListRepo.existsBySymbol(whishlist.getSymbol())) {
			throw new ResourceAlreadyExistsException("item in whishlist already exists");
		}
		return new ResponseEntity<>(whishListRepo.save(whishlist), HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<?> deleteFromWhishList(Long id) {
		Optional<WhishList> whishlist = whishListRepo.findById(id);
		if(whishlist.isEmpty()) {
			throw new ResourceNotFoundException("item with given id not found");
		}
		whishListRepo.deleteById(id);
		return new ResponseEntity<>("item deleted successfully", HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> updateWhishList(Long id, WhishList whishlist) {
		Optional<WhishList> whishlistdb = whishListRepo.findById(id);
		if(whishlistdb.isEmpty()) {
			whishListRepo.save(whishlist);
		}
		whishlistdb.get().setCountry(whishlist.getCountry());
		whishlistdb.get().setCurrency(whishlist.getCurrency());
		whishlistdb.get().setExchange(whishlist.getExchange());
		whishlistdb.get().setMic_code(whishlist.getMic_code());
		whishlistdb.get().setExchange(whishlist.getExchange());
		whishlistdb.get().setSymbol(whishlist.getSymbol());
		whishlistdb.get().setType(whishlist.getType());
		whishlistdb.get().setName(whishlist.getName());
		WhishList updated = whishListRepo.save(whishlistdb.get());
		return new ResponseEntity<>(updated, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> getAllStocksWhishlist() {
		return new ResponseEntity<>(whishListRepo.findAll(), HttpStatus.OK);
	}

}
