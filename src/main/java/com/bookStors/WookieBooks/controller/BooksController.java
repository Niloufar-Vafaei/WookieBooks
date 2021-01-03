package com.bookStors.WookieBooks.controller;

import com.bookStors.WookieBooks.model.Book;
import com.bookStors.WookieBooks.service.BooksServices;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;



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
    private ResponseEntity<Book> getBook(@PathVariable("bookid") Long bookid) {
        return ResponseEntity.ok().body(booksServices.findById(bookid));
    }

    //create a delete mapping that deletes a specific book
    @DeleteMapping("/books/{bookid}")
    private void deleteBook(@PathVariable("bookid") Long bookid) {
        booksServices.deleteBook(bookid);
    }

    //creating post mapping that post the book detail in the database
    @PostMapping("/books")
    private ResponseEntity<Book> saveBook(@RequestPart String book,
                                          @RequestParam(value = "coverImage", required = false) MultipartFile coverImage) throws IOException {
        return ResponseEntity.ok().body(booksServices.addBook(book, coverImage));
    }

    //creating put mapping that updates the book detail, get whole object of book
    @PutMapping("/book/{id}")
    private ResponseEntity<Book> updateFull(@RequestParam Map<String, String> book,
                                            @RequestParam(value = "coverImage", required = false) MultipartFile coverImage,
                                            @PathVariable Long id) {
        ObjectMapper bookMapper = new ObjectMapper();
        Book bookPojo = bookMapper.convertValue(book, Book.class);
        bookPojo.setBookId(id);
        return ResponseEntity.ok().body(booksServices.update(bookPojo, coverImage));
    }

    //creating patch mapping that updates the book detail
    @PatchMapping("/book/{id}")
    private ResponseEntity<Book> updateDetails(@RequestParam Map<String, String> book,
                                               @RequestParam(value = "coverImage", required = false) MultipartFile coverImage,
                                               @PathVariable Long id) {
        Book bookPojo = booksServices.findById(id);
        if (book.get("id") != null) {
            bookPojo.setBookId(id);
        }
        if (book.get("title") != null) {
            bookPojo.setTitle(book.get("title"));
        }
        if (book.get("description") != null) {
            bookPojo.setDescription(book.get("description"));
        }
        if (book.get("author") != null) {
            bookPojo.setAuthor(book.get("author"));
        }
        if (book.get("price") != null) {
            bookPojo.setPrice(Float.parseFloat(book.get("price")));
        }

        return ResponseEntity.ok().body(booksServices.update(bookPojo, coverImage));
    }

    //creating Get mapping that download the book coverImage
    @GetMapping("/book/download/{coverImageName}")
    private ResponseEntity<Resource> download(@PathVariable("coverImageName") String coverImageName, HttpServletRequest request) {
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
