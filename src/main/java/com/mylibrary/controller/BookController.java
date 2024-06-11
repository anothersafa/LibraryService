package com.mylibrary.controller;

import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mylibrary.entity.ApiResponseBody;
import com.mylibrary.entity.Author;
import com.mylibrary.entity.Book;
import com.mylibrary.entity.ErrorResponse;
import com.mylibrary.repositories.lmsDs.AuthorRepository;
import com.mylibrary.repositories.lmsDs.BookRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Tag(name = "BookController")
@RequestMapping("/book")
public class BookController {
	
	@Autowired
	BookRepository bookRepo;
	
	@Autowired
	AuthorRepository authorRepo;
	
	static String successMessage = "Data retrieved successfully.";
	static String genericErrorMessage = "Unable to retrieve data.";
	
	@Operation(summary = "Return List of all book")
	@GetMapping(value = "")
	public ResponseEntity<?> bookList(
			HttpServletResponse response) {

		ResponseEntity<?> res = null;

		try {
			ApiResponseBody<List<?>> successBody = new ApiResponseBody<>();
			List<Book> book = bookRepo.findAll();
			response.setStatus(HttpServletResponse.SC_OK);

			successBody.setCode(HttpServletResponse.SC_OK);
			successBody.setMessage(successMessage);
			successBody.setStatus(true);
			successBody.setData(book);

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
	
	@Operation(summary = "Return List by Isbn No")
	@GetMapping(value = "/{isbnNo}")
	public ResponseEntity<?> bookListByIsbnNo(
			HttpServletResponse response,
			@PathVariable String isbnNo) {

		ResponseEntity<?> res = null;

		try {
			ApiResponseBody<List<?>> successBody = new ApiResponseBody<>();
			List<Book> book = bookRepo.findByIsbnNo(isbnNo);
			response.setStatus(HttpServletResponse.SC_OK);

			successBody.setCode(HttpServletResponse.SC_OK);
			successBody.setMessage(successMessage);
			successBody.setStatus(true);
			successBody.setData(book);

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
	
	@Operation(summary = "Register New Book")
	@PostMapping(value = "")
	public ResponseEntity<?> registerNewBook(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestBody Book book) {

		ResponseEntity<?> res = null;

		try {

			Author bookAuthor = authorRepo.findById(book.getAuthorId()).get();
			
			book.setAuthor(bookAuthor);
			book.setCreatedDate(LocalDateTime.now());
			book.setUpdatedDate(LocalDateTime.now());
			book.setStatus(true); // true - available, false - not available
			
			bookRepo.save(book);

			res = new ResponseEntity<>(book, HttpStatus.OK);

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
