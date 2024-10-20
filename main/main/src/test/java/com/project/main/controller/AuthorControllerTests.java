package com.project.main.controller;

import com.project.main.entity.Author;
import com.project.main.service.AuthorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;

class AuthorControllerTests {

    @Mock
    private AuthorService authorService;

    @InjectMocks
    private AuthorController authorController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllAuthors() {
        Author author1 = new Author("John Doe");
        Author author2 = new Author("Jane Doe");
        List<Author> authors = Arrays.asList(author1, author2);

        when(authorService.getAllAuthors()).thenReturn(authors);

        ResponseEntity<List<Author>> response = authorController.getAllAuthors();

        assertEquals(OK, response.getStatusCode());
        assertEquals(authors, response.getBody());
        verify(authorService, times(1)).getAllAuthors();
    }

    @Test
    void testGetAuthorById_Success() {
        Author author = new Author("John Doe");
        when(authorService.getAuthorById(1)).thenReturn(author);

        ResponseEntity<Author> response = authorController.getAuthorById(1);

        assertEquals(OK, response.getStatusCode());
        assertEquals(author, response.getBody());
        verify(authorService, times(1)).getAuthorById(1);
    }

    @Test
    void testGetAuthorById_NotFound() {
        when(authorService.getAuthorById(1)).thenReturn(null);

        ResponseEntity<Author> response = authorController.getAuthorById(1);

        assertEquals(NOT_FOUND, response.getStatusCode());
        verify(authorService, times(1)).getAuthorById(1);
    }

    @Test
    void testSaveAuthor() {
        Author author = new Author("John Doe");
        when(authorService.saveAuthor(author)).thenReturn(author);

        ResponseEntity<Author> response = authorController.saveAuthor(author);

        assertEquals(OK, response.getStatusCode());
        assertEquals(author, response.getBody());
        verify(authorService, times(1)).saveAuthor(author);
    }

    @Test
    void testDeleteAuthorById() {
        doNothing().when(authorService).deleteAuthorById(1);

        ResponseEntity<String> response = authorController.deleteAuthorById(1);

        assertEquals(OK, response.getStatusCode());
        assertEquals("Deleted author successfully", response.getBody());
        verify(authorService, times(1)).deleteAuthorById(1);
    }

        @Test
    void testGetAllAuthors_InternalServerError() {
        // Arrange: Mock an exception thrown by the service
        when(authorService.getAllAuthors()).thenThrow(new RuntimeException("Service Error"));

        // Act: Call the controller method
        ResponseEntity<List<Author>> response = authorController.getAllAuthors();

        // Assert: Check the response status is 500 Internal Server Error
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        verify(authorService, times(1)).getAllAuthors();
    }

    @Test
    void testGetAuthorById_InternalServerError() {
        // Arrange: Mock an exception thrown by the service
        when(authorService.getAuthorById(1)).thenThrow(new RuntimeException("Service Error"));

        // Act: Call the controller method
        ResponseEntity<Author> response = authorController.getAuthorById(1);

        // Assert: Check the response status is 500 Internal Server Error
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        verify(authorService, times(1)).getAuthorById(1);
    }

    @Test
    void testSaveAuthor_InternalServerError() {
        // Arrange: Mock an exception thrown by the service
        Author author = new Author("John Doe");
        when(authorService.saveAuthor(author)).thenThrow(new RuntimeException("Service Error"));

        // Act: Call the controller method
        ResponseEntity<Author> response = authorController.saveAuthor(author);

        // Assert: Check the response status is 500 Internal Server Error
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        verify(authorService, times(1)).saveAuthor(author);
    }

    @Test
    void testDeleteAuthorById_InternalServerError() {
        // Arrange: Mock an exception thrown by the service
        doThrow(new RuntimeException("Service Error")).when(authorService).deleteAuthorById(1);

        // Act: Call the controller method
        ResponseEntity<String> response = authorController.deleteAuthorById(1);

        // Assert: Check the response status is 500 Internal Server Error
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        verify(authorService, times(1)).deleteAuthorById(1);
    }

}
