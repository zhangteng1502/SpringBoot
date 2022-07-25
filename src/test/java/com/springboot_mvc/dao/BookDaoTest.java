package com.springboot_mvc.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.springboot_mvc.domain.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BookDaoTest {

    @Autowired
    private BookDao bookDao;

    @Test
    void testGetByid(){
        System.out.println(bookDao.selectById(1));
    }

    @Test
    void testSave(){
        System.out.println(bookDao.selectById(1));
    }

    @Test
    void testDelete(){
        Book book = new Book();
        book.setType("文学");
        book.setName("红楼梦");
        book.setDescription("红楼梦牛逼");
        System.out.println(bookDao.insert(book));
    }

    @Test
    void testUpdate(){
        Book book = new Book();
        book.setId(2);
        book.setType("文学");
        book.setName("红楼梦");
        book.setDescription("红楼梦第一");
        System.out.println(bookDao.updateById(book));
    }

    @Test
    void testGetAll(){
        bookDao.selectList(null);
    }

    @Test
    void testGetPage(){
        IPage page = new Page(1,2);
        bookDao.selectPage(page,null);
    }

    @Test
    void testGetBy(){
        LambdaQueryWrapper<Book> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like( Book::getName,"Spring");
        bookDao.selectList(queryWrapper);
    }
}
