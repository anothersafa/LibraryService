package com.mylibrary.repositories.lmsDs;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mylibrary.entity.Book;

public interface BookRepository extends JpaRepository<Book, Long> {

	 List<Book> findByIsbnNo(String isbnNo);
}
