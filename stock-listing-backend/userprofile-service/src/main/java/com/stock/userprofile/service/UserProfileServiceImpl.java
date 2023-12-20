package com.stock.userprofile.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.stock.userprofile.dto.UserProfileDto;
import com.stock.userprofile.exception.ResourceAlreadyExistsException;
import com.stock.userprofile.exception.ResourceNotFoundException;
import com.stock.userprofile.model.UserProfile;
import com.stock.userprofile.repo.UserProfileRepo;

@Service
public class UserProfileServiceImpl implements UserProfileService {

	private final UserProfileRepo usersProfileRepository;

	private final ModelMapper modelMapper;

	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	public UserProfileServiceImpl(UserProfileRepo usersProfileRepository, ModelMapper modelMapper,
			BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.usersProfileRepository = usersProfileRepository;
		this.modelMapper = modelMapper;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	@Override
	public List<UserProfileDto> getAllUsers() {

		return Stream.of(usersProfileRepository.findAll())
				.flatMap(entityList -> entityList.stream().map(entity -> modelMapper.map(entity, UserProfileDto.class)))
				.toList();

	}

	@Override
	public UserProfileDto getUserProfileById(long id) {
		UserProfile entity = usersProfileRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Entity not found with ID: " + id));

		return modelMapper.map(entity, UserProfileDto.class);
	}

	@Override
	public UserProfileDto saveUserProfile(UserProfile userProfile) {

		if (usersProfileRepository.existsByUsername(userProfile.getUsername())
				|| usersProfileRepository.existsByEmail(userProfile.getEmail())) {
			throw new ResourceAlreadyExistsException("UserProfile already exists");
		}

		userProfile.setPassword(bCryptPasswordEncoder.encode(userProfile.getPassword()));
		return modelMapper.map(usersProfileRepository.save(userProfile), UserProfileDto.class);
	}

	@Override
	public UserProfileDto updateUserProfile(UserProfileDto userProfileDto, long id) {
		UserProfile entity = usersProfileRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("UserProfile not found with ID: " + id));

		entity.setEmail(userProfileDto.getEmail());
		entity.setFirstName(userProfileDto.getFirstName());
		entity.setLastName(userProfileDto.getLastName());
		entity.setNumber(userProfileDto.getNumber());
		entity.setDateOfBirth(userProfileDto.getDateOfBirth());

		usersProfileRepository.save(entity);

		return modelMapper.map(entity, UserProfileDto.class);
	}

	@Override
	public Optional<UserProfile> getUserByUsername(String username) {
		Optional<UserProfile> user = usersProfileRepository.findByUsername(username);
		if (user.isEmpty()) {
			throw new ResourceNotFoundException("UserProfile not found with username: " + username);
		}
		return user;
	}

}