package com.stock.userprofile.repo;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.stock.userprofile.model.UserProfile;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserProfileRepositoryTest {

	@Autowired
	private UserProfileRepo userProfileRepository;
	UserProfile userProfile;
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	@BeforeEach
	void setUp() throws ParseException {
		userProfile = new UserProfile();
		userProfile = new UserProfile();
		userProfileRepository.save(userProfile);
	}

	@AfterEach
	void tearDown() {
		userProfile = null;
		userProfileRepository.deleteAll();
	}

	@Test
	void testFindAll() {

		System.out.println(userProfileRepository.findAll());

	}
}