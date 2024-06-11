package com.mylibrary.repositories.lmsDs;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mylibrary.entity.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {

}
