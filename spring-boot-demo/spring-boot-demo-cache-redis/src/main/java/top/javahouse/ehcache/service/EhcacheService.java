package top.javahouse.ehcache.service;


import top.javahouse.ehcache.entity.Book;

public interface EhcacheService {

    Book getById(Long l);

    Book getByIdCondition(Long id,boolean isTrue);

    Book getByIdUnless(Long l,boolean isTrue);

    Book add(Long id, String content);

    void delete(Long id);

}