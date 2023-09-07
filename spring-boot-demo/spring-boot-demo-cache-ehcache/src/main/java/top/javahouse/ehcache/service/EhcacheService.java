package top.javahouse.ehcache.service;

import java.util.List;

public interface EhcacheService {

    public List<String> getList();

    public String getPage(int page, int pageSize);

    String getById(Long l, boolean b);

    String findById(Long l);

    public String add(Long id, String content);

    public void delete(Long id);

}