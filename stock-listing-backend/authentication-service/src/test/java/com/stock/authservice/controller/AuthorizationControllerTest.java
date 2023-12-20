package com.stock.authservice.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stock.authservice.dto.LoginDto;
import com.stock.authservice.dto.ResetDto;
import com.stock.authservice.dto.SignupDto;
import com.stock.authservice.model.User;
import com.stock.authservice.repo.RoleRepository;
import com.stock.authservice.repo.UserRepository;
import com.stock.authservice.security.JwtTokenUtil;
import com.stock.authservice.service.UserService;

@SpringBootTest
@AutoConfigureMockMvc
class AuthorizationControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private RoleRepository roleRepo;

	@MockBean
	private UserRepository userRepo;

	@MockBean
	private UserService userService;

	@MockBean
	private JwtTokenUtil jwtTokenUtil;

	@Test
	void testRegisterUser() throws Exception {
		// Role roleuser = new Role("101", "ROLE_ADMIN");
		Set<String> roles = new HashSet<>();
		roles.add("ROLE_ADMIN");
		SignupDto signUpObj = new SignupDto("test", "test@gmail.com", roles, "testpsk", "pet", "testpet");
		when(userService.addUser(signUpObj)).thenReturn(new ResponseEntity<>(HttpStatus.OK));
		mockMvc.perform(post("/api/v1.0/auth/signup").content(toJson(signUpObj)).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());

	}

	@Test
	void testLoginUser() throws Exception {

		LoginDto loginObj = new LoginDto("admin", "hritik@123");
		User userObj = new User("admin", "test@gmail.com", "hritik@123", "pet", "testpet");
		when(userService.getUserByUsername(anyString())).thenReturn(Optional.of(userObj));
		when(jwtTokenUtil.generatToken(loginObj)).thenReturn("testToken");

		mockMvc.perform(post("/api/v1.0/auth/login").content(toJson(loginObj)).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());

	}

	@Test
	public void testLoginUserInvalidCredentials() throws Exception {
		LoginDto loginObj = new LoginDto("admin", "hritik@123");

		when(userService.getUserByUsername(loginObj.getUsername())).thenReturn(Optional.empty());

		mockMvc.perform(post("/api/v1.0/auth/login").content(toJson(loginObj)).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
	}

	@Test
	public void testValidateTokenInvalid() throws Exception {
		String token = "Bearer test-token";

		when(jwtTokenUtil.validateJwtToken("test-token")).thenReturn(false);

		mockMvc.perform(post("/api/v1.0/auth/validate").header("Authorization", token))
				.andExpect(status().isNotFound());
	}

	@Test
	void testUpdatePassword() throws Exception {
		String username = "testusername";
		ResetDto resetobj = new ResetDto(username, "newpass", "secquestion", "secanswer");
		when(userService.updatePassword(any(ResetDto.class))).thenReturn(new ResponseEntity<>(HttpStatus.OK));

		mockMvc.perform(patch("/api/v1.0/auth/forgot").content(toJson(resetobj)).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());

	}

	@Test
	void testGetAllUsers() throws Exception {
		String token = "Bearer test-token";
		String authToken = token.substring(7);
		when(jwtTokenUtil.getRoleFromToken(authToken)).thenReturn("ROLE_ADMIN");
		when(userService.getAllUsers()).thenReturn(new ResponseEntity<>(HttpStatus.OK));
		mockMvc.perform(get("/api/v1.0/auth/getAllUsers").header("Authorization", token)).andExpect(status().isNotFound());
	}

	@Test
	void testGetAllUsersInvalidUser() throws Exception {
		String token = "Bearer test-token";
		String authToken = token.substring(7);
		when(jwtTokenUtil.getRoleFromToken(authToken)).thenReturn("ROLE_CUSTOMER");
		when(userService.getAllUsers()).thenReturn(new ResponseEntity<>(HttpStatus.FORBIDDEN));
		mockMvc.perform(get("/api/v1.0/auth/getAllUsers").header("Authorization", token))
				.andExpect(status().isNotFound());
	}

	public static String toJson(Object obj) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		String jsonObj = mapper.writeValueAsString(obj);
		return jsonObj;

	}

}
