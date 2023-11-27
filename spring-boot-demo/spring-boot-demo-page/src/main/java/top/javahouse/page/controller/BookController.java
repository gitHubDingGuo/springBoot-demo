package top.javahouse.page.controller;

import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.javahouse.page.entity.Book;
import top.javahouse.page.service.BookService;


import java.util.List;

@RestController
public class BookController {
    @Autowired
    BookService bookService;

    @GetMapping("/bookOps")
    public void bookOps() {
        System.out.println("getAllBooks>>>" );
    }

    @GetMapping("page")
    public ResponseEntity<PageInfo<Book>> bookOps(@PageableDefault(value = 10, direction = Sort.Direction.DESC) Pageable pageable) {
        PageInfo<Book> list = bookService.getAllBooks(pageable);
        return ResponseEntity.ok(list);
    }


}