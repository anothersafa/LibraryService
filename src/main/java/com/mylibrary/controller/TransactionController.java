package com.mylibrary.controller;

import java.time.LocalDateTime;
import java.util.List;

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
import com.mylibrary.entity.Book;
import com.mylibrary.entity.Borrower;
import com.mylibrary.entity.ErrorResponse;
import com.mylibrary.entity.Transaction;
import com.mylibrary.logger.Logger;
import com.mylibrary.repositories.lmsDs.BookRepository;
import com.mylibrary.repositories.lmsDs.BorrowerRepository;
import com.mylibrary.repositories.lmsDs.TransactionRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Tag(name = "TransactionController")
@RequestMapping("/transaction")
public class TransactionController {
	
	private static final String LOG_BASE_NAME = "TransactionController";
	
	@Autowired
	TransactionRepository transRepo;
	
	@Autowired
	BookRepository bookRepo;
	
	@Autowired
	BorrowerRepository borrowerRepo;
	
	static String successMessage = "Transaction success.";
	static String genericErrorMessage = "Unable to create new transaction.";
	
	@Operation(summary = "Return List of all transaction")
	@GetMapping(value = "")
	public ResponseEntity<?> getTransList(
			HttpServletRequest request,
			HttpServletResponse response) {

		ResponseEntity<?> res = null;

		try {
			ApiResponseBody<List<?>> successBody = new ApiResponseBody<>();
			List<Transaction> trans = transRepo.findAll();
			
			response.setStatus(HttpServletResponse.SC_OK);

			successBody.setCode(HttpServletResponse.SC_OK);
			successBody.setMessage(successMessage);
			successBody.setStatus(true);
			successBody.setData(trans);

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
	
	@Operation(summary = "Borrow Book")
	@PostMapping(value = "/borrow")
	public ResponseEntity<?> borrowBook(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestBody Transaction trans) {

		ResponseEntity<?> res = null;
		
		Logger logger = new Logger(LOG_BASE_NAME);
		
		try {
			

			logger.writeLog("-------------------- START BORROW TRANSACTION ------------------");

			Book bookDetails = bookRepo.findById(trans.getBookId()).get();
			
			if(bookDetails.getStatus().booleanValue() == true)
			{
				Borrower borrower = borrowerRepo.findById(trans.getBorrowerId()).get();
				
				trans.setBook(bookDetails);
				trans.setBorrower(borrower);
				trans.setBorrowDate(LocalDateTime.now());
				
				transRepo.save(trans);
				
				//update book status
				bookDetails.setStatus(false);
				bookRepo.save(bookDetails);
				
				res = new ResponseEntity<>(trans, HttpStatus.OK);
			}
			else
			{
				ErrorResponse errBody = new ErrorResponse();
				errBody.setCode(HttpServletResponse.SC_ACCEPTED);
				errBody.setError("Book Not Available");
				errBody.setStatus(false);
				res = new ResponseEntity<>(errBody, HttpStatus.OK);
			}

		} catch (Exception e) {
			
			ErrorResponse errBody = new ErrorResponse();
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

			errBody.setCode(HttpServletResponse.SC_BAD_REQUEST);
			errBody.setError("Error Register New Book");
			errBody.setStatus(false);

			res = new ResponseEntity<>(errBody, HttpStatus.BAD_REQUEST);
		}
		
		logger.writeLog("Request Data" + res);
		logger.writeLog("-------------------- END BORROW TRANSACTION ------------------\n");
		logger.flushLog();
		
		return res;
	}
	
	@Operation(summary = "Return Book")
	@PostMapping(value = "/return")
	public ResponseEntity<?> returnBook(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestBody Transaction trans) {

		ResponseEntity<?> res = null;
		
		Logger logger = new Logger(LOG_BASE_NAME);
		
		try {
			
			logger.writeLog("-------------------- START BORROW TRANSACTION ------------------");
			
			Long transactionId = trans.getId();
			Transaction transDetails = transRepo.findById(transactionId).get();
			
			Long bookId = transDetails.getBook().getId();
			Book bookDetails = bookRepo.findById(bookId).get();
			
			if(transDetails.getReturnDate() == null)
			{				
				//update return date for selected trans id
				transDetails.setReturnDate(LocalDateTime.now()) ;
				transRepo.save(transDetails);
				
				//update book status
				bookDetails.setStatus(true);
				bookRepo.save(bookDetails);
				
				res = new ResponseEntity<>(transDetails, HttpStatus.OK);
			}
			else
			{
				ErrorResponse errBody = new ErrorResponse();
				errBody.setCode(HttpServletResponse.SC_ACCEPTED);
				errBody.setError("Book already return");
				errBody.setStatus(false);
				res = new ResponseEntity<>(errBody, HttpStatus.OK);
			}

		} catch (Exception e) {
			
			ErrorResponse errBody = new ErrorResponse();
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

			errBody.setCode(HttpServletResponse.SC_BAD_REQUEST);
			errBody.setError("Error return book");
			errBody.setStatus(false);

			res = new ResponseEntity<>(errBody, HttpStatus.BAD_REQUEST);
		}
		
		logger.writeLog("Request Data" + res);
		logger.writeLog("-------------------- END BORROW TRANSACTION ------------------\n");
		logger.flushLog();
		
		return res;
	}
	
}
