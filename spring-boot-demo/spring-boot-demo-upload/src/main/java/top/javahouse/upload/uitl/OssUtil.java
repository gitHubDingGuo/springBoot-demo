package top.javahouse.upload.uitl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import top.javahouse.upload.config.OssProperties;

public class OssUtil {

    public static OSS getOssClient(){
        OSS ossClient = new OSSClientBuilder().build(OssProperties.ENDPOINT, OssProperties.KEY_ID, OssProperties.KEY_SECRET);
        return ossClient;
    }
}
