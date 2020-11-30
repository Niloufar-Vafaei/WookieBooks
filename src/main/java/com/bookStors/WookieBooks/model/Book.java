package com.bookStors.WookieBooks.model;


import javax.persistence.*;

//mark class as Entity
@Entity
//define class name as Table name
@Table
public class Book {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column
    private int bookId;
    @Column
    private String title;
    @Column
    private String description;
    @Column
    private String author;
    @Column
    private String coverImageName;
    @Column
    private float price;

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCoverImageName() {
        return coverImageName;
    }

    public void setCoverImageName(String coverImage) {
        this.coverImageName = coverImage;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
