package com.edu.ulab.app.storage.impl;

import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.storage.Storage;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BookStorage extends Storage<Book> {

    @Override
    public Book save(Book entity) {
        return super.save(entity);
    }

    @Override
    public Book update(Book entity) {
        return super.update(entity);
    }

    @Override
    public Book findById(Long id) {
        return super.findById(id);
    }

    @Override
    public List<Book> findAll() {
        return super.findAll();
    }

    @Override
    public void deleteById(Long id) {
        super.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return super.existsById(id);
    }
}
