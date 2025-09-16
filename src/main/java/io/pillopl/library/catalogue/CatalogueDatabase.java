package io.pillopl.library.catalogue;

import io.vavr.control.Option;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

@AllArgsConstructor(access = AccessLevel.PACKAGE)
class CatalogueDatabase {

    private final JdbcTemplate jdbcTemplate;

    Book saveNew(Book book) {
        jdbcTemplate.update("" +
                        "INSERT INTO catalogue_book " +
                        "(id, isbn, title, author) VALUES " +
                        "(catalogue_book_seq.nextval, ?, ?, ?)",
                book.getBookIsbn().getIsbn(), book.getTitle().getTitle(), book.getAuthor().getName());
        return book;
    }

    BookInstance saveNew(BookInstance bookInstance) {
        jdbcTemplate.update("" +
                        "INSERT INTO catalogue_book_instance " +
                        "(id, isbn, book_id) VALUES " +
                        "(catalogue_book_instance_seq.nextval, ?, ?)",
                bookInstance.getBookIsbn().getIsbn(), bookInstance.getBookId().getBookId());
        return bookInstance;
    }

    Option<Book> findBy(ISBN isbn) {
        List<BookDatabaseRow> results = jdbcTemplate.query(
                "SELECT b.* FROM catalogue_book b WHERE b.isbn = ?",
                new BeanPropertyRowMapper<>(BookDatabaseRow.class),
                isbn.getIsbn());
        
        return results.isEmpty() ? Option.none() : Option.of(results.get(0).toBook());
    }

}

@Data
@NoArgsConstructor(access = AccessLevel.PACKAGE)
class BookDatabaseRow {
    String isbn;
    String author;
    String title;

    Book toBook() {
        return new Book(isbn, author, title);
    }
}
