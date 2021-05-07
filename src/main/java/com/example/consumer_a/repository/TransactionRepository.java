package com.example.consumer_a.repository;

import com.example.consumer_a.entity.Account;
import com.example.consumer_a.entity.Transaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Integer> {
}
