package com.edu.ulab.app.storage;

import com.edu.ulab.app.entity.Book;
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
}
