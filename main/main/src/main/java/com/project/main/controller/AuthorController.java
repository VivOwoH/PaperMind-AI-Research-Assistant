package com.project.main.controller;

import com.project.main.entity.Author;
import com.project.main.service.AuthorService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// @RestController
// @RequestMapping("/authors")
// public class AuthorController {

//     private final AuthorService authorService;

//     public AuthorController(AuthorService authorService) {
//         this.authorService = authorService;
//     }

//     @GetMapping("/")
//     public ResponseEntity<List<Author>> getAllAuthors() {
//         return ResponseEntity.ok().body(authorService.getAllAuthors());
//     }

//     @GetMapping("/{id}")
//     public ResponseEntity<Author> getAuthorById(@PathVariable Integer id) {
//         return ResponseEntity.ok().body(authorService.getAuthorById(id));
//     }

//     @PostMapping("/")
//     public ResponseEntity<Author> saveAuthor(@RequestBody Author author) {
//         return ResponseEntity.ok().body(authorService.saveAuthor(author));
//     }

//     @DeleteMapping("/{id}")
//     public ResponseEntity<String> deleteAuthorById(@PathVariable Integer id) {
//         authorService.deleteAuthorById(id);
//         return ResponseEntity.ok().body("Deleted author successfully");
//     }
// }


@RestController
@RequestMapping("/authors")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping("/")
    public ResponseEntity<List<Author>> getAllAuthors() {
        try {
            List<Author> authors = authorService.getAllAuthors();
            return ResponseEntity.ok().body(authors);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Author> getAuthorById(@PathVariable Integer id) {
        try {
            Author author = authorService.getAuthorById(id);
            if (author != null) {
                return ResponseEntity.ok().body(author);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/")
    public ResponseEntity<Author> saveAuthor(@RequestBody Author author) {
        try {
            Author savedAuthor = authorService.saveAuthor(author);
            return ResponseEntity.ok().body(savedAuthor);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    // @PostMapping("/")
    // public ResponseEntity<Author> saveAuthor(@RequestBody Author author) {
    //     return ResponseEntity.ok().body(authorService.saveAuthor(author));
    // }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAuthorById(@PathVariable Integer id) {
        try {
            authorService.deleteAuthorById(id);
            return ResponseEntity.ok().body("Deleted author successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
