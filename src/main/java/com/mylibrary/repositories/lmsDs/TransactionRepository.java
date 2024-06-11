package com.mylibrary.repositories.lmsDs;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mylibrary.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

}
