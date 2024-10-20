package com.project.main.service;

import com.project.main.entity.Author;
import com.project.main.repository.AuthorRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthorServiceTests {

    @Mock
    private AuthorRepo authorRepo;

    @InjectMocks
    private AuthorService authorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllAuthors_Success() {
        Author author1 = new Author();
        Author author2 = new Author();
        when(authorRepo.findAll()).thenReturn(Arrays.asList(author1, author2));

        List<Author> authors = authorService.getAllAuthors();

        assertEquals(2, authors.size());
        verify(authorRepo, times(1)).findAll();
    }

    @Test
    void testGetAllAuthors_EmptyList() {
        when(authorRepo.findAll()).thenReturn(Collections.emptyList());

        List<Author> authors = authorService.getAllAuthors();

        assertTrue(authors.isEmpty());
        verify(authorRepo, times(1)).findAll();
    }

    @Test
    void testGetAuthorById_Success() {
        Author author = new Author();
        when(authorRepo.findById(1)).thenReturn(Optional.of(author));

        Author result = authorService.getAuthorById(1);

        assertNotNull(result);
        assertEquals(author, result);
        verify(authorRepo, times(1)).findById(1);
    }

    @Test
    void testGetAuthorById_NotFound() {
        when(authorRepo.findById(1)).thenReturn(Optional.empty());

        Author result = authorService.getAuthorById(1);

        assertNull(result);
        verify(authorRepo, times(1)).findById(1);
    }

    @Test
    void testSaveAuthor_Success() {
        Author author = new Author();
        when(authorRepo.save(author)).thenReturn(author);

        Author result = authorService.saveAuthor(author);

        assertNotNull(result);
        assertEquals(author, result);
        verify(authorRepo, times(1)).save(author);
    }

    @Test
    void testDeleteAuthorById_Success() {
        doNothing().when(authorRepo).deleteById(1);

        authorService.deleteAuthorById(1);

        verify(authorRepo, times(1)).deleteById(1);
    }
}
