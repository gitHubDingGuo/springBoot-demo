package top.javahouse.page.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.javahouse.page.entity.Book;

import java.util.List;

@Mapper
public interface BookMapper {
    int addBook(Book book);

    int deleteBookById(Integer id);

    int updateBookById(Book book);

    Book getBookById(Integer id);

    List<Book> getAllBooks();
}
