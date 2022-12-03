package com.edu.ulab.app.mapper;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.web.request.BookRequest;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookMapper {

    BookDto bookRequestToBookDto(BookRequest bookRequest);

    List<BookDto> bookRequestToBookDto(List<BookRequest> bookUpdateRequest);

    BookDto bookToBookDto(Book book);

    Book bookDtoToBook(BookDto bookDto);
}
