package com.edu.ulab.app.facade;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.exception.NotFoundException;
import com.edu.ulab.app.mapper.BookMapper;
import com.edu.ulab.app.mapper.UserMapper;
import com.edu.ulab.app.service.BookService;
import com.edu.ulab.app.service.UserService;
import com.edu.ulab.app.web.request.UserBookRequest;
import com.edu.ulab.app.web.response.UserBookResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
public class UserDataFacade {
    private final UserService userService;
    private final BookService bookService;
    private final UserMapper userMapper;
    private final BookMapper bookMapper;

    public UserDataFacade(UserService userService,
                          BookService bookService,
                          UserMapper userMapper,
                          BookMapper bookMapper) {
        this.userService = userService;
        this.bookService = bookService;
        this.userMapper = userMapper;
        this.bookMapper = bookMapper;
    }

    public UserBookResponse createUserWithBooks(UserBookRequest userBookRequest) {
        log.info("Got user book create request: {}", userBookRequest);
        UserDto userDto = userMapper.userRequestToUserDto(userBookRequest.getUserRequest());
        log.info("Mapped user request: {}", userDto);

        UserDto createdUser = userService.createUser(userDto);
        log.info("Created user: {}", createdUser);

        List<Long> bookIdList = userBookRequest.getBookRequests()
                .stream()
                .filter(Objects::nonNull)
                .map(bookMapper::bookRequestToBookDto)
                .peek(bookDto -> bookDto.setUserId(createdUser.getId()))
                .peek(mappedBookDto -> log.debug("mapped book: {}", mappedBookDto))
                .map(bookService::createBook)
                .peek(createdBook -> log.debug("Created book: {}", createdBook))
                .map(BookDto::getId)
                .toList();
        log.info("Collected book ids: {}", bookIdList);

        return UserBookResponse.builder()
                .userId(createdUser.getId())
                .booksIdList(bookIdList)
                .build();
    }

    public UserBookResponse updateUserWithBooks(UserBookRequest userBookRequest) {
        log.info("Got user book update request: {}", userBookRequest);
        UserDto userDto = userMapper.userRequestToUserDto(userBookRequest.getUserRequest());
        log.info("Mapped user request: {}", userDto);

        log.info("Check user in database: {}", userDto);
        userService.getUserById(userDto.getId());

        List<BookDto> bookDtoList = bookMapper.bookRequestToBookDto(userBookRequest.getBookRequests());
        log.info("Mapped book request: {}", bookDtoList);

        log.info("Check books user in database which id: {}", bookDtoList);
        bookDtoList.stream()
                .filter(Objects::nonNull)
                .filter(book -> book.getId() != null)
                .forEach(book -> {
                    BookDto bookDto = bookService.getBookById(book.getId());
                    if (!Objects.equals(bookDto.getUserId(), userDto.getId())) {
                        throw new NotFoundException("The user does not have a book with id: " + bookDto.getId());
                    }
                });

        UserDto updateUser = userService.updateUser(userDto);
        log.info("Update user: {}", updateUser);

        bookDtoList.stream()
                .filter(Objects::nonNull)
                .forEach(bookDto -> {
                    if(bookDto.getId() == null){
                        bookDto.setUserId(updateUser.getId());
                        bookService.createBook(bookDto);
                        log.info("Created book: {}", bookDto);
                    } else {
                        bookDto.setUserId(updateUser.getId());
                        bookService.updateBook(bookDto);
                        log.info("Update book: {}", bookDto);
                    }
                });
        log.info("Update book: {}", bookDtoList);

        List<Long> allBooksUser = getAllBooksUser(updateUser.getId());
        log.info("Collected update book ids: {}", allBooksUser);

        return UserBookResponse.builder()
                .userId(updateUser.getId())
                .booksIdList(allBooksUser)
                .build();
    }

    public UserBookResponse getUserWithBooks(Long userId) {
        log.info("Got user book get request: {}", userId);

        UserDto userDto = userService.getUserById(userId);
        log.info("Got a user: {}", userDto);

        List<Long> allBooksUser = getAllBooksUser(userId);
        log.info("Collected book ids: {}", allBooksUser);

        return UserBookResponse.builder()
                .userId(userDto.getId())
                .booksIdList(allBooksUser)
                .build();
    }

    public void deleteUserWithBooks(Long userId) {
        log.info("Got user and book delete request: {}", userId);

        userService.deleteUserById(userId);
        log.info("Deleted user");

        bookService.getAllBooks()
                .stream()
                .filter(Objects::nonNull)
                .filter(b -> b.getUserId().equals(userId))
                .forEach(b -> bookService.deleteBookById(b.getId()));
        log.info("Deleted user's books.");
    }

    private List<Long> getAllBooksUser(Long id) {
        return bookService.getAllBooks()
                .stream()
                .filter(Objects::nonNull)
                .filter(b -> b.getUserId().equals(id))
                .map(BookDto::getId)
                .toList();
    }
}
