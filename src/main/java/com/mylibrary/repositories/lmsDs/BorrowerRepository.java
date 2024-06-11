package com.mylibrary.repositories.lmsDs;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mylibrary.entity.Borrower;

public interface BorrowerRepository extends JpaRepository<Borrower, Long> {
	
	 Optional<Borrower> findByEmail(String email);

}
