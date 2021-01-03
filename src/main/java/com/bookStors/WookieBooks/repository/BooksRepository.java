package com.bookStors.WookieBooks.repository;

import com.bookStors.WookieBooks.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BooksRepository extends JpaRepository<Book,Long> {
}
