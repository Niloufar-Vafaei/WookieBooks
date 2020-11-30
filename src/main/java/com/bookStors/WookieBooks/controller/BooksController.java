package com.bookStors.WookieBooks.controller;

import com.bookStors.WookieBooks.model.Book;
import com.bookStors.WookieBooks.service.BooksServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;


//Mark class as controller
@RestController
public class BooksController {
    private static final Logger logger = LoggerFactory.getLogger(BooksController.class);

    //Autowired BooksService class
    @Autowired
    BooksServices booksServices;

    //create a get mapping that retrieves all the books detail from database
    @GetMapping("/books")
    private ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok().body(booksServices.getAllBooks());
    }

    //create a get mapping that retrieves  the detail of a specific book
    // @RequestParams extract values from the query string, @PathVariables extract values from the URI path
    @GetMapping("/books/{bookid}")
    private ResponseEntity<Book> getBook(@PathVariable("bookid") int bookid) {
        return ResponseEntity.ok().body( booksServices.getBooksById(bookid));
    }

    //create a delete mapping that deletes a specific book
    @DeleteMapping("/books/{bookid}")
    private void deleteBook(@PathVariable("bookid") int bookid) {
        booksServices.deleteBook(bookid);
    }

    //creating post mapping that post the book detail in the database
    @PostMapping("/books")
    private ResponseEntity<Book> saveBook(@RequestPart String book, @RequestParam(value = "coverImage", required = false) MultipartFile coverImage) throws IOException {
        return ResponseEntity.ok().body(booksServices.addBook(book, coverImage));
    }

    //creating put mapping that updates the book detail
    @PutMapping("/book")
    private Book update(@RequestBody Book book) {
        booksServices.update(book);
        return book;
    }

    //creating Get mapping that download the book coverImage
    @GetMapping("/book/download/{coverImageName}")
    private ResponseEntity<Resource> download(@PathVariable("coverImageName") String coverImageName, HttpServletRequest request) throws MalformedURLException {
        Resource resource = booksServices.download(coverImageName);
        String contentType = null;
        try {
           contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
                //.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
