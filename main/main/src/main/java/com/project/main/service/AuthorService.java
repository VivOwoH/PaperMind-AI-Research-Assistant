package com.project.main.service;

import com.project.main.entity.Author;
import com.project.main.repository.AuthorRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorService {

    private final AuthorRepo authorRepo;

    public AuthorService(AuthorRepo authorRepo) {
        this.authorRepo = authorRepo;
    }

    public List<Author> getAllAuthors() {
        return authorRepo.findAll();
    }

    public Author getAuthorById(Integer id) {
        Optional<Author> author = authorRepo.findById(id);
        return author.orElse(null);
    }

    public Author saveAuthor(Author author) {
        return authorRepo.save(author);
    }

    public void deleteAuthorById(Integer id) {
        authorRepo.deleteById(id);
    }
}
