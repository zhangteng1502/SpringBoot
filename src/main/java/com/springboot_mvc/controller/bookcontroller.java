package com.springboot_mvc.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.springboot_mvc.controller.utils.R;
import com.springboot_mvc.domain.Book;
import com.springboot_mvc.service.IBookService;
import com.springboot_mvc.service.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/books")
public class bookcontroller {

    @Autowired
    private IBookService bookService;

    @Log(name = "bookcontroller日志")
    @GetMapping
    public R getAll() {
        return new R(true, bookService.list());
    }

    @Log(name = "bookcontroller日志")
    @PostMapping
    public R save(@RequestBody Book book) throws IOException {
        if (book.getName().equals("123")) throw new IOException();
        boolean flag = bookService.save(book);
        return new R(flag, flag ? "添加成功^_^" : "添加失败-_-!");
    }

    @Log(name = "bookcontroller日志")
    @PutMapping
    public R update(@RequestBody Book book) throws IOException {
        if (book.getName().equals("123")) throw new IOException();
        boolean flag = bookService.modify(book);
        return new R(flag, flag ? "修改成功^_^" : "修改失败-_-!");
    }

    @Log(name = "bookcontroller日志")
    @DeleteMapping("{id}")
    public R delete(@PathVariable Integer id) {
        return new R(bookService.delete(id));
    }

    @Log(name = "bookcontroller日志")
    @GetMapping("{id}")
    public R getById(@PathVariable Integer id) {
        return new R(true, bookService.getById(id));
    }

    @Log(name = "bookcontroller日志")
    @GetMapping("{currentPage}/{pageSize}")
    public R getPage(@PathVariable int currentPage, @PathVariable int pageSize, Book book) {
        try{
            int a =1/0;
        }catch (Exception e){
            throw new RuntimeException();
        }
        IPage<Book> page = bookService.getPage(currentPage, pageSize, book);
        //如果当前页码值大于了总页码值，那么重新执行查询操作，使用最大页码值作为当前页码值
        if (currentPage > page.getPages()) {
            page = bookService.getPage((int) page.getPages(), pageSize, book);
        }
        return new R(true, page);
    }
}
