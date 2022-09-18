package com.edu.ulab.app.facade;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.exception.NotFoundException;
import com.edu.ulab.app.mapper.BookMapper;
import com.edu.ulab.app.mapper.UserMapper;
import com.edu.ulab.app.service.BookService;
import com.edu.ulab.app.service.UserService;
import com.edu.ulab.app.web.request.UserBookRequest;
import com.edu.ulab.app.web.request.UserBookUpdateRequest;
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
        log.debug("Mapped user request: {}", userDto);

        UserDto createdUser = userService.createUser(userDto);
        log.debug("Created user: {}", createdUser);

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

    public UserBookResponse updateUserWithBooks(UserBookUpdateRequest userBookUpdateRequest) {
        log.info("Got user book update request: {}", userBookUpdateRequest);
        UserDto userDto = userMapper.userUpdateRequestToUserDto(userBookUpdateRequest.getUserUpdateRequest());
        log.debug("Mapped user request: {}", userDto);

        log.debug("Check user in database: {}", userDto);
        getUserWithBooks(userDto.getId());
        log.debug("The user is in the database: {}", userDto);

        List<BookDto> bookDtoList = bookMapper.bookUpdateRequestToBookDto(userBookUpdateRequest.getBookUpdateRequests());
        log.debug("Mapped book request: {}", bookDtoList);

        UserDto updateUser = userService.updateUser(userDto);
        log.debug("Update user: {}", updateUser);

        Map<Long, BookDto> bookDtoMap = bookService.getAllBooks()
                .stream()
                .filter(Objects::nonNull)
                .filter(b -> b.getUserId().equals(updateUser.getId()))
                .collect(Collectors.toMap(BookDto::getId, book -> book));
        log.debug("Collected book ids: {}", bookDtoMap);

        bookDtoList.forEach(bookRequest -> {
            if (bookRequest.getId() == null) {
                bookRequest.setUserId(updateUser.getId());
                bookService.createBook(bookRequest);
                log.debug("Created book: {}", bookRequest);
            }
            if (bookDtoMap.containsKey(bookRequest.getId())) {
                bookRequest.setUserId(updateUser.getId());
                bookService.updateBook(bookRequest);
                log.debug("Update book: {}", bookRequest);
            }
        });

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
        log.debug("Got a user: {}", userDto);

        if (userDto == null) {
            throw new NotFoundException("No user with id: " + userId);
        }

        List<Long> bookIdList = getAllBooksUser(userId);
        log.info("Collected book ids: {}", bookIdList);

        return UserBookResponse.builder()
                .userId(userDto.getId())
                .booksIdList(bookIdList)
                .build();
    }

    public void deleteUserWithBooks(Long userId) {
        log.info("Got user and book delete request: {}", userId);

        log.debug("Check user in database: {}", userId);
        getUserWithBooks(userId);
        log.debug("The user is in the database: {}", userId);

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
