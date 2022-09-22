package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.exception.NotFoundException;
import com.edu.ulab.app.mapper.BookMapper;
import com.edu.ulab.app.service.BookService;
import com.edu.ulab.app.storage.impl.BookStorage;
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
        log.info("Got book create request: {}", bookDto);

        Book book = bookMapper.bookDtoToBook(bookDto);
        log.info("Mapped bookDto: {}", bookDto);

        Book saveBook = bookStorage.save(book);
        log.info("Save book: {}", saveBook);

        return bookMapper.bookToBookDto(saveBook);
    }

    @Override
    public BookDto updateBook(BookDto bookDto) {
        log.info("Got book update request: {}", bookDto);

        Book book = bookMapper.bookDtoToBook(bookDto);
        log.info("Mapped bookDto: {}", bookDto);

        Book updateBook = bookStorage.update(book);
        log.info("Update book: {}", updateBook);

        return bookMapper.bookToBookDto(updateBook);
    }

    @Override
    public BookDto getBookById(Long id) {
        log.info("Got book get book with id: {}", id);

        Book book;
        log.info("Check book in database which  id: {}", id);
        if (bookStorage.existsById(id)) {
            book = bookStorage.findById(id);
            log.info("Get book: {}", book);
        } else {
            throw new NotFoundException("No book with id: " + id);
        }

        return bookMapper.bookToBookDto(book);
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
        log.info("Got user delete book with id : {}", id);

        log.info("Check book in database which  id: {}", id);
        if (bookStorage.existsById(id)) {
            bookStorage.deleteById(id);
            log.info("Delete book with id: {}", id);
        } else {
            throw new NotFoundException("No book with id: " + id);
        }
    }
}
