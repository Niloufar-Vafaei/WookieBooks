package com.bookStors.WookieBooks.service;

import com.bookStors.WookieBooks.model.Book;
import com.bookStors.WookieBooks.repository.BooksRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class BooksServices {

    //store uploaded a image
    @Value("${app.upload.dir:${user.dir}}")
    public String uploadDir;

    @Autowired
    BooksRepository booksRepository;

    //getting all books record by using the method findaAll() of CrudRepository
    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<Book>();
        booksRepository.findAll().forEach(book1 -> books.add(book1));
        return books;
    }

    //getting a book record by using the method findById() of CrudRepository
    public Book getBooksById(int bookid) {
        Book book = new Book();
        return  booksRepository.findById(bookid).get();
    }

    //delete a book by using the method deleteById() of CrudRepository
    public void deleteBook(int bookid) {
        booksRepository.deleteById(bookid);
    }

    //saving a specific record by using the method save() of CrudRepository
    public Book addBook(String book,MultipartFile coverImage) throws IOException {
        String newFileName=storeFile(coverImage);
        Book bookJson=mappingToJson(book, newFileName);
        booksRepository.save(bookJson);
        return bookJson;
    }

    public String storeFile(MultipartFile coverImage) throws IOException {
        UUID uuid = UUID.randomUUID();
        String originalFileName = StringUtils.cleanPath(coverImage.getOriginalFilename()).toLowerCase();
        String[] splitFileName = originalFileName.split("\\.", 2);
        String newFileName = uuid.toString() + "." + splitFileName[1];

        Path copyLocation = Paths
                .get(uploadDir + File.separator + newFileName);
        Files.copy(coverImage.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);
        return newFileName;
    }


    //update a record
    public void update(Book book) {
        booksRepository.save(book);
    }

    //get uploaded image
    public void getImage() {
    }

    //mapping String To JSON Book object
    public Book mappingToJson(String book, String coverImageName) {
        Book bookJson = new Book();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            bookJson = objectMapper.readValue(book, Book.class);
            bookJson.setCoverImageName(coverImageName);
        } catch (IOException err) {
            System.out.printf("Error", err.toString());
        }
        return bookJson;
    }

    //download coverImage
    public  Resource download(String coverImageName) throws MalformedURLException {
        Path copyLocation = Paths
                .get(uploadDir + File.separator + coverImageName);
        Resource resource = new UrlResource(copyLocation.toUri());
        return resource;
    }
}
