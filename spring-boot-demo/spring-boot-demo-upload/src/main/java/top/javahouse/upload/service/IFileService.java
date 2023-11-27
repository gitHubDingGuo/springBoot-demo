package top.javahouse.upload.service;

import java.io.InputStream;

public interface IFileService {

    //上传
    String upload(InputStream inputStream, String module, String originalFilename);

    //删除
    void remove(String url) throws Exception;

}
