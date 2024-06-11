package com.mylibrary.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mylibrary.entity.ApiResponseBody;
import com.mylibrary.entity.Borrower;
import com.mylibrary.entity.ErrorResponse;
import com.mylibrary.repositories.lmsDs.BorrowerRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Tag(name = "BorrowerController")
@RequestMapping("/borrower")
public class BorrowerController {
	
	@Autowired
	BorrowerRepository borrowerRepo;
	
	static String successMessage = "Data retrieved successfully.";
	static String genericErrorMessage = "Unable to retrieve data.";
	
	@Operation(summary = "Return List of all borrower")
	@GetMapping(value = "")
	public ResponseEntity<?> getBorrowerList(
			HttpServletResponse response) {

		ResponseEntity<?> res = null;

		try {
			ApiResponseBody<List<?>> successBody = new ApiResponseBody<>();
			List<Borrower> borrower = borrowerRepo.findAll();
			response.setStatus(HttpServletResponse.SC_OK);

			successBody.setCode(HttpServletResponse.SC_OK);
			successBody.setMessage(successMessage);
			successBody.setStatus(true);
			successBody.setData(borrower);

			res = new ResponseEntity<>(successBody, HttpStatus.OK);

		} catch (Exception e) {
			ErrorResponse errBody = new ErrorResponse();
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

			errBody.setCode(HttpServletResponse.SC_BAD_REQUEST);
			errBody.setError(genericErrorMessage);
			errBody.setStatus(false);

			res = new ResponseEntity<>(errBody, HttpStatus.BAD_REQUEST);
		}
		return res;
	}
	
	@Operation(summary = "Register New Borrower")
	@PostMapping(value = "")
	public ResponseEntity<?> registerNewBook(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestBody Borrower borrower) {

		ResponseEntity<?> res = null;

		try {

			Optional<Borrower> borrowerDetails = borrowerRepo.findByEmail(borrower.getEmail());
			
			if(!borrowerDetails.isPresent())
			{
				// create new borrower
				borrower.setCreatedDate(LocalDateTime.now());
				borrower.setUpdatedDate(LocalDateTime.now());
				borrowerRepo.save(borrower);
				
				res = new ResponseEntity<>(borrower, HttpStatus.OK);
			}
			else
			{
				ErrorResponse errBody = new ErrorResponse();
				errBody.setCode(HttpServletResponse.SC_ACCEPTED);
				errBody.setError("Record already exist!");
				errBody.setStatus(false);
				
				res = new ResponseEntity<>(errBody, HttpStatus.BAD_REQUEST);

			}

		} catch (Exception e) {
			ErrorResponse errBody = new ErrorResponse();
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

			errBody.setCode(HttpServletResponse.SC_BAD_REQUEST);
			errBody.setError("Error Register New Book");
			errBody.setStatus(false);

			res = new ResponseEntity<>(errBody, HttpStatus.BAD_REQUEST);
		}
		return res;
	}
}
