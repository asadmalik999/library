package com.aeon.library.service;

import com.aeon.library.dto.BookDto;
import com.aeon.library.entities.Book;
import com.aeon.library.entities.Borrower;
import com.aeon.library.exception.ArgumentNotValidException;
import com.aeon.library.exception.BorrowerNotFoundException;
import com.aeon.library.repository.BookRepository;
import com.aeon.library.repository.BorrowerRepository;
import com.aeon.library.service.Impl.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;
    @Mock
    private BorrowerRepository borrowerRepository;
    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private BookServiceImpl bookService;

    private BookDto createBookDto(String isbn, String title, String author) {
        return new BookDto(null, isbn, title, author, null);
    }

    @BeforeEach
    void setUp() {
    }

    @Test
    void testRegisterBook() {
        // Given
        BookDto bookDto = createBookDto("1234567890", "Test Book", "Test Author");
        Book book = new Book(1L, "1234567890", "Test Book", "Test Author", null);

        // Mocking
        when(modelMapper.map(bookDto, Book.class)).thenReturn(book);
        when(modelMapper.map(book, BookDto.class)).thenReturn(bookDto);
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        // When
        BookDto savedBook = bookService.registerBook(bookDto);

        // Then
        assertEquals(bookDto.getIsbn(), savedBook.getIsbn());
        assertEquals(bookDto.getTitle(), savedBook.getTitle());
        assertEquals(bookDto.getAuthor(), savedBook.getAuthor());
    }

    @Test
    void testRegisterBookWithExistingISBNAndSameTitleAuthor() {
        // Given
        BookDto bookDto = createBookDto("1234567890", "Test Book", "Test Author");
        Book existingBook = new Book(1L, "1234567890", "Test Book", "Test Author", null);
        List<Book> existingBooks = List.of(existingBook);

        // Mocking
        when(bookRepository.findByIsbn(bookDto.getIsbn())).thenReturn(existingBooks);
        when(modelMapper.map(bookDto, Book.class)).thenReturn(existingBook);


        // When / Then
        assertDoesNotThrow(() -> bookService.registerBook(bookDto));
    }

    @Test
    void testRegisterBookWithExistingISBNAndDifferentTitle() {
        // Given
        BookDto bookDTO = createBookDto("1234567890", "Different Title", "Test Author");
        Book existingBook = new Book(1L, "1234567890", "Test Book", "Test Author", null);
        List<Book> existingBooks = List.of(existingBook);

        // Mocking
        when(bookRepository.findByIsbn(bookDTO.getIsbn())).thenReturn(existingBooks);

        // When / Then
        assertThrows(IllegalArgumentException.class, () -> bookService.registerBook(bookDTO));
    }

    @Test
    void testRegisterBookWithExistingISBNAndDifferentAuthor() {
        // Given
        BookDto bookDTO = createBookDto("1234567890", "Test Book", "Different Author");
        Book existingBook = new Book(1L, "1234567890", "Test Book", "Test Author", null);
        List<Book> existingBooks = List.of(existingBook);

        // Mocking
        when(bookRepository.findByIsbn(bookDTO.getIsbn())).thenReturn(existingBooks);

        // When / Then
        assertThrows(IllegalArgumentException.class, () -> bookService.registerBook(bookDTO));
    }

    @Test
    void testGetAllBooks() {
        // Given
        Book book1 = new Book(1L, "1234567890", "Book 1", "Author 1", null);
        Book book2 = new Book(2L, "0987654321", "Book 2", "Author 2", null);
        List<Book> mockBooks = Arrays.asList(book1, book2);

        // Mocking
        when(bookRepository.findAll()).thenReturn(mockBooks);
        when(modelMapper.map(book1, BookDto.class)).thenReturn(new BookDto(book1.getId(), book1.getIsbn(), book1.getTitle(), book1.getAuthor(), null));
        when(modelMapper.map(book2, BookDto.class)).thenReturn(new BookDto(book2.getId(), book2.getIsbn(), book2.getTitle(), book2.getAuthor(), null));

        // When
        List<BookDto> books = bookService.getAllBooks();

        // Then
        assertEquals(2, books.size());
        assertEquals(book1.getIsbn(), books.get(0).getIsbn());
        assertEquals(book2.getIsbn(), books.get(1).getIsbn());
    }


    @Test
    void testRegisterBookWithValidBorrowerId() {
        // Given
        BookDto bookDto = createBookDto("1234567890", "Test Book", "Test Author");
        bookDto.setBorrowerId(1L);
        Borrower borrower = new Borrower(1L, "doe@john.com", "John Doe");

        // Mocking
        when(modelMapper.map(bookDto, Book.class)).thenReturn(new Book());
        when(modelMapper.map(new Book(), BookDto.class)).thenReturn(bookDto);
        when(borrowerRepository.findById(1L)).thenReturn(Optional.of(borrower));
        when(bookRepository.save(any(Book.class))).thenReturn(new Book());

        // When
        BookDto savedBook = bookService.registerBook(bookDto);

        // Then
        assertNotNull(savedBook);
        assertEquals(bookDto.getBorrowerId(), savedBook.getBorrowerId());
    }

    @Test
    void testRegisterBookWithInvalidBorrowerId() {
        // Given
        BookDto bookDTO = createBookDto("1234567890", "Test Book", "Test Author");
        bookDTO.setBorrowerId(999L);

        // Mocking
        when(modelMapper.map(bookDTO, Book.class)).thenReturn(new Book());
        when(borrowerRepository.findById(999L)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(BorrowerNotFoundException.class, () -> bookService.registerBook(bookDTO));
    }

    @Test
    void testRegisterBookWithNullBorrowerId() {
        // Given
        BookDto bookDTO = createBookDto("1234567890", "Test Book", "Test Author");
        bookDTO.setBorrowerId(null);

        // Mocking
        when(modelMapper.map(bookDTO, Book.class)).thenReturn(new Book());
        when(modelMapper.map(new Book(), BookDto.class)).thenReturn(bookDTO);
        when(bookRepository.save(any(Book.class))).thenReturn(new Book());

        // When
        BookDto savedBook = bookService.registerBook(bookDTO);

        // Then
        assertNotNull(savedBook);
        assertNull(savedBook.getBorrowerId());
    }

    @Test
    void testRegisterBookWithZeroBorrowerId() {
        // Given
        BookDto bookDto = createBookDto("1234567890", "Test Book", "Test Author");
        bookDto.setBorrowerId(0L);

        // Mocking
        when(modelMapper.map(bookDto, Book.class)).thenReturn(new Book());
        when(modelMapper.map(new Book(), BookDto.class)).thenReturn(bookDto);
        when(bookRepository.save(any(Book.class))).thenReturn(new Book());

        // When
        BookDto savedBook = bookService.registerBook(bookDto);

        // Then
        assertNotNull(savedBook);
        assertEquals(0L, savedBook.getBorrowerId());
    }

}
