package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.mapper.BookMapper;
import com.edu.ulab.app.service.BookService;
import com.edu.ulab.app.storage.BookStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookStorage bookStorage;
    private final BookMapper bookMapper;

    @Override
    public BookDto createBook(BookDto bookDto) {
        Book book = new Book();

        book.setTitle(bookDto.getTitle());
        book.setAuthor(bookDto.getAuthor());
        book.setPageCount(bookDto.getPageCount());
        book.setUserId(bookDto.getUserId());

        return bookMapper.bookToBookDto(bookStorage.save(book));
    }

    @Override
    public BookDto updateBook(BookDto bookDto) {
        Book book = new Book();

        book.setId(bookDto.getId());
        book.setTitle(bookDto.getTitle());
        book.setAuthor(bookDto.getAuthor());
        book.setPageCount(bookDto.getPageCount());
        book.setUserId(bookDto.getUserId());

        return bookMapper.bookToBookDto(bookStorage.update(book));
    }

    @Override
    public BookDto getBookById(Long id) {
        return bookMapper.bookToBookDto(bookStorage.findById(id));
    }

    @Override
    public List<BookDto> getAllBooks() {
        return bookStorage.findAll()
                .stream()
                .map(bookMapper::bookToBookDto)
                .toList();
    }

    @Override
    public void deleteBookById(Long id) {
        bookStorage.deleteById(id);
    }
}
