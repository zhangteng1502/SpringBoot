package com.springboot_mvc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.springboot_mvc.dao.BookDao;
import com.springboot_mvc.domain.Book;
import com.springboot_mvc.service.IBookService;
import com.springboot_mvc.service.Log;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl extends ServiceImpl<BookDao, Book> implements IBookService {

    @Autowired
    BookDao bookDao;

    @Log(name = "Service日志")
    @Override
    public boolean saveBook(Book book) {
        return bookDao.insert(book) > 0;
    }

    @Log(name = "Service日志")
    @Override
    public boolean modify(Book book) {
        return bookDao.updateById(book) > 0;
    }

    @Log(name = "Service日志")
    @Override
    public boolean delete(Integer id) {
        return bookDao.deleteById(id) > 0;
    }

    @Log(name = "Service日志")
    @Override
    public IPage<Book> getPage(int currentPage, int pageSize) {
        IPage page = new Page(currentPage, pageSize);
        bookDao.selectPage(page, null);
        return page;
    }

    @Log(name = "Service日志")
    @Override
    public IPage<Book> getPage(int currentPage, int pageSize, Book book) {
        LambdaQueryWrapper<Book> lqw = new LambdaQueryWrapper<Book>();
        lqw.like(Strings.isNotEmpty(book.getType()), Book::getType, book.getType());
        lqw.like(Strings.isNotEmpty(book.getName()), Book::getName, book.getName());
        lqw.like(Strings.isNotEmpty(book.getDescription()), Book::getDescription, book.getDescription());
        IPage page = new Page(currentPage, pageSize);
        bookDao.selectPage(page, lqw);
        return page;
    }
}
