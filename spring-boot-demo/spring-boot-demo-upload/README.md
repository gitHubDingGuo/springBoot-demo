# spring-boot-demo-upload

> 此 demo 主要演示了 Spring Boot 如何使用 文件上传

## Demo.java
```java
package top.javahouse.upload.config;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.common.auth.*;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.internal.Mimetypes;
import com.aliyun.oss.model.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Demo {

    public static void main(String[] args) throws Exception {
        // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
        String endpoint = "https://oss-cn-hangzhou.aliyuncs.com";
        // 从环境变量中获取访问凭证。运行本代码示例之前，请确保已设置环境变量OSS_ACCESS_KEY_ID和OSS_ACCESS_KEY_SECRET。
        EnvironmentVariableCredentialsProvider credentialsProvider = CredentialsProviderFactory.newEnvironmentVariableCredentialsProvider();
        // 填写Bucket名称，例如examplebucket。
        String bucketName = "examplebucket";
        // 填写Object完整路径，例如exampledir/exampleobject.txt。Object完整路径中不能包含Bucket名称。
        String objectName = "exampledir/exampleobject.txt";
        // 待上传本地文件路径。
        String filePath = "D:\\localpath\\examplefile.txt";

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, credentialsProvider);
        try {
            // 创建InitiateMultipartUploadRequest对象。
            InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(bucketName, objectName);

            // 如果需要在初始化分片时设置请求头，请参考以下示例代码。
            ObjectMetadata metadata = new ObjectMetadata();
            // metadata.setHeader(OSSHeaders.OSS_STORAGE_CLASS, StorageClass.Standard.toString());
            // 指定该Object的网页缓存行为。
            // metadata.setCacheControl("no-cache");
            // 指定该Object被下载时的名称。
            // metadata.setContentDisposition("attachment;filename=oss_MultipartUpload.txt");
            // 指定该Object的内容编码格式。
            // metadata.setContentEncoding(OSSConstants.DEFAULT_CHARSET_NAME);
            // 指定初始化分片上传时是否覆盖同名Object。此处设置为true，表示禁止覆盖同名Object。
            // metadata.setHeader("x-oss-forbid-overwrite", "true");
            // 指定上传该Object的每个part时使用的服务器端加密方式。
            // metadata.setHeader(OSSHeaders.OSS_SERVER_SIDE_ENCRYPTION, ObjectMetadata.KMS_SERVER_SIDE_ENCRYPTION);
            // 指定Object的加密算法。如果未指定此选项，表明Object使用AES256加密算法。
            // metadata.setHeader(OSSHeaders.OSS_SERVER_SIDE_DATA_ENCRYPTION, ObjectMetadata.KMS_SERVER_SIDE_ENCRYPTION);
            // 指定KMS托管的用户主密钥。
            // metadata.setHeader(OSSHeaders.OSS_SERVER_SIDE_ENCRYPTION_KEY_ID, "9468da86-3509-4f8d-a61e-6eab1eac****");
            // 指定Object的存储类型。
            // metadata.setHeader(OSSHeaders.OSS_STORAGE_CLASS, StorageClass.Standard);
            // 指定Object的对象标签，可同时设置多个标签。
            // metadata.setHeader(OSSHeaders.OSS_TAGGING, "a:1");
            // request.setObjectMetadata(metadata);

            // 根据文件自动设置ContentType。如果不设置，ContentType默认值为application/oct-srream。
            if (metadata.getContentType() == null) {
                metadata.setContentType(Mimetypes.getInstance().getMimetype(new File(filePath), objectName));
            }

            // 初始化分片。
            InitiateMultipartUploadResult upresult = ossClient.initiateMultipartUpload(request);
            // 返回uploadId。
            String uploadId = upresult.getUploadId();
            // 根据uploadId执行取消分片上传事件或者列举已上传分片的操作。
            // 如果您需要根据您需要uploadId执行取消分片上传事件的操作，您需要在调用InitiateMultipartUpload完成初始化分片之后获取uploadId。
            // 如果您需要根据您需要uploadId执行列举已上传分片的操作，您需要在调用InitiateMultipartUpload完成初始化分片之后，且在调用CompleteMultipartUpload完成分片上传之前获取uploadId。
            // System.out.println(uploadId);

            // partETags是PartETag的集合。PartETag由分片的ETag和分片号组成。
            List<PartETag> partETags =  new ArrayList<PartETag>();
            // 每个分片的大小，用于计算文件有多少个分片。单位为字节。
            final long partSize = 1 * 1024 * 1024L;   //1 MB。

            // 根据上传的数据大小计算分片数。以本地文件为例，说明如何通过File.length()获取上传数据的大小。
            final File sampleFile = new File(filePath);
            long fileLength = sampleFile.length();
            int partCount = (int) (fileLength / partSize);
            if (fileLength % partSize != 0) {
                partCount++;
            }
            // 遍历分片上传。
            for (int i = 0; i < partCount; i++) {
                long startPos = i * partSize;
                long curPartSize = (i + 1 == partCount) ? (fileLength - startPos) : partSize;
                UploadPartRequest uploadPartRequest = new UploadPartRequest();
                uploadPartRequest.setBucketName(bucketName);
                uploadPartRequest.setKey(objectName);
                uploadPartRequest.setUploadId(uploadId);
                // 设置上传的分片流。
                // 以本地文件为例说明如何创建FIleInputstream，并通过InputStream.skip()方法跳过指定数据。
                InputStream instream = new FileInputStream(sampleFile);
                instream.skip(startPos);
                uploadPartRequest.setInputStream(instream);
                // 设置分片大小。除了最后一个分片没有大小限制，其他的分片最小为100 KB。
                uploadPartRequest.setPartSize(curPartSize);
                // 设置分片号。每一个上传的分片都有一个分片号，取值范围是1~10000，如果超出此范围，OSS将返回InvalidArgument错误码。
                uploadPartRequest.setPartNumber( i + 1);
                // 每个分片不需要按顺序上传，甚至可以在不同客户端上传，OSS会按照分片号排序组成完整的文件。
                UploadPartResult uploadPartResult = ossClient.uploadPart(uploadPartRequest);
                // 每次上传分片之后，OSS的返回结果包含PartETag。PartETag将被保存在partETags中。
                partETags.add(uploadPartResult.getPartETag());
            }


            // 创建CompleteMultipartUploadRequest对象。
            // 在执行完成分片上传操作时，需要提供所有有效的partETags。OSS收到提交的partETags后，会逐一验证每个分片的有效性。当所有的数据分片验证通过后，OSS将把这些分片组合成一个完整的文件。
            CompleteMultipartUploadRequest completeMultipartUploadRequest =
                    new CompleteMultipartUploadRequest(bucketName, objectName, uploadId, partETags);

            // 如果需要在完成分片上传的同时设置文件访问权限，请参考以下示例代码。
            // completeMultipartUploadRequest.setObjectACL(CannedAccessControlList.Private);
            // 指定是否列举当前UploadId已上传的所有Part。仅在Java SDK为3.14.0及以上版本时，支持通过服务端List分片数据来合并完整文件时，将CompleteMultipartUploadRequest中的partETags设置为null。
            // Map<String, String> headers = new HashMap<String, String>();
            // 如果指定了x-oss-complete-all:yes，则OSS会列举当前UploadId已上传的所有Part，然后按照PartNumber的序号排序并执行CompleteMultipartUpload操作。
            // 如果指定了x-oss-complete-all:yes，则不允许继续指定body，否则报错。
            // headers.put("x-oss-complete-all","yes");
            // completeMultipartUploadRequest.setHeaders(headers);

            // 完成分片上传。
            CompleteMultipartUploadResult completeMultipartUploadResult = ossClient.completeMultipartUpload(completeMultipartUploadRequest);
            System.out.println(completeMultipartUploadResult.getETag());
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }
}
```
## MyWebMvcConfigurer.java
```java
package top.javahouse.upload.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/*
报错内容
        Failed to start bean 'documentationPluginsBootstrapper'; nested exception is java.lang.NullPointerException
报错原因
        Springfox使用的路径匹配是基于AntPathMatcher的，而Spring Boot 2.6.X使用的是PathPatternMatcher
解决方案
        在config目录下，新建MyWebMvcConfigurer，修改资源映射路径，完成mvc与swagger对接。
        注意，这一步是最核心的，由于springboot使用的是PathPatternMatcher，所以无法识别swagger的静态资源路径。网上很多的方法指导是将springboot2.6.x版本的路径匹配规则修改回AntPathMatcher，因此要改一堆配置。而直接添加addResourceHandlers的方式，能够避免复杂的配置工作。
        原文链接：https://blog.csdn.net/m0_70651612/article/details/126426084
 */
/*
 * @author:javahouse.top
 * @Date: 2023/7/21 10:56
 */
@Configuration
public class MyWebMvcConfigurer extends WebMvcConfigurationSupport {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // swagger配置
        registry.
                addResourceHandler("/swagger-ui/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/")
                .resourceChain(false);

        //解决knife4j的时候出现No mapping for GET /doc.html
        //详情：https://blog.csdn.net/niushijia/article/details/128839264
        registry.addResourceHandler("/**").addResourceLocations("classpath:/META-INF/resources/");
    }
}
```
## OssProperties.java
```java
package top.javahouse.upload.config;

import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class OssProperties implements InitializingBean {
    @Value("${aliyun.oss.endpoint}")
    private String endpoint;
    @Value("${aliyun.oss.keyId}")
    private String keyId;
    @Value("${aliyun.oss.keySecret}")
    private String keySecret;
    @Value("${aliyun.oss.bucketName}")
    private String bucketName;

    public static String ENDPOINT;
    public static String KEY_ID;
    public static String KEY_SECRET;
    public static String BUCKET_NAME;

    //当私有成员被赋值后，此方法自动被调用，从而初始化常量
    @Override
    public void afterPropertiesSet() throws Exception {
        ENDPOINT = endpoint;
        KEY_ID = keyId;
        KEY_SECRET = keySecret;
        BUCKET_NAME = bucketName;
    }
}
```
## SwaggerThreeConfig.java
```java
package top.javahouse.upload.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseBuilder;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Response;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.List;

/*
 * @author:javahouse.top
 * @Date: 2023/7/21 10:37
 */
@Configuration
@EnableOpenApi
//开启增强文档knife4j
@EnableKnife4j
public class SwaggerThreeConfig {
    /**
     * swagger3的配置文件
     */
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo()) //应用文档基本信息
                .select()
                //swagger扫描的包路径
                .apis(RequestHandlerSelectors.basePackage("top.javahouse.upload"))
                //或者扫描对应有注解的方法
                //.apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                //匹配所有路径
                .paths(PathSelectors.any())
                .build()
                //生成全局通用参数和下面方法对应
                //.globalRequestParameters(getGlobalRequestParameters())
                //生成通用响应信息和下面方法对应
                /*.globalResponses(HttpMethod.GET, getGlobalResponseMessage())
                .globalResponses(HttpMethod.POST, getGlobalResponseMessage())
                .globalResponses(HttpMethod.DELETE, getGlobalResponseMessage())
                .globalResponses(HttpMethod.PUT, getGlobalResponseMessage())*/
                ;
    }

    /**
     * 构建 api文档的详细信息函数,注意这里的注解引用的是哪个
     */
    private ApiInfo apiInfo() {
        //获取工程名称
        String projectName = System.getProperty("user.dir");
        return new ApiInfoBuilder()
                .title(projectName.substring(projectName.lastIndexOf("\\") + 1) + " API接口文档")
                .contact(new Contact("JavaHouse", "https://javahouse.top", "1@qq.com"))
                .version("1.0")
                .description("API文档")
                .build();
    }

    /**
     * 生成全局通用参数
     * @return
     */
    /*private List<RequestParameter> getGlobalRequestParameters() {
        List<RequestParameter> parameters = new ArrayList<>();
        parameters.add(new RequestParameterBuilder()
                .name("x-access-token")
                .description("令牌")
                .required(false)
                .in(ParameterType.HEADER)
                .build());
        parameters.add(new RequestParameterBuilder()
                .name("Equipment-Type")
                .description("产品类型")
                .required(false)
                .in(ParameterType.HEADER)
                .build());
        return parameters;
    }*/

    /**
     * 生成通用响应信息
     * @return
     */
    private List<Response> getGlobalResponseMessage() {
        List<Response> responseList = new ArrayList<>();
        responseList.add(new ResponseBuilder().code("404").description("找不到资源").build());
        return responseList;
    }

}
```
## FileController.java
```java
package top.javahouse.upload.controlller;

import io.swagger.annotations.ApiImplicitParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.javahouse.upload.service.IFileService;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

@Api(tags = "阿里云文件管理")
@CrossOrigin
@RestController
@Slf4j
@RequestMapping("/api/file")
public class FileController {

    @Resource
    private IFileService fileService;


    @ApiOperation("文件上传")
    @PostMapping("/upload")
    @ApiImplicitParam(name = "file", value = "上传的文件", dataType = "MultipartFile", dataTypeClass = MultipartFile.class, required = true)
    public ResponseEntity<?> upload(@RequestPart("file") MultipartFile file,
                               @ApiParam(value = "模块", required = true) @RequestParam("module") String module) {
        try {
            InputStream inputStream = file.getInputStream();
            String originalFilename = file.getOriginalFilename();
            String uploadUrl = fileService.upload(inputStream, module, originalFilename);
            return ResponseEntity.ok("文件上传成功url="+uploadUrl);
        } catch (IOException e) {
            log.error("文件上传错误：{}", ExceptionUtils.getStackTrace(e));
        }
        return ResponseEntity.ok("");
    }

    @ApiOperation(value = "删除oss文件")
    @DeleteMapping("/remove")
    public ResponseEntity<?> remove(@ApiParam(value = "要删除的文件路径", required = true) @RequestParam("url") String url) throws Exception {
        fileService.remove(url);
        return ResponseEntity.ok("文件删除成功");
    }




}

```
## IFileService.java
```java
package top.javahouse.upload.service;

import java.io.InputStream;

public interface IFileService {

    //上传
    String upload(InputStream inputStream, String module, String originalFilename);

    //删除
    void remove(String url) throws Exception;

}
```
## FileServiceImpl.java
```java
package top.javahouse.upload.service.impl;

import cn.hutool.core.date.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import top.javahouse.upload.config.OssProperties;
import top.javahouse.upload.service.IFileService;
import java.io.InputStream;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.PutObjectRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Service;
import top.javahouse.upload.uitl.OssUtil;

import java.util.UUID;

/**
 * @author pgx
 * @version V1.0.0
 * @date 2023/7/24 15:03
 */
@Service
@Slf4j
public class FileServiceImpl implements IFileService {

    @Override
    public String upload(InputStream inputStream, String module, String filename) {
        // 创建OSSClient实例。
        OSS ossClient = OssUtil.getOssClient();
        log.info("OSSClient实例创建成功");
        try {
            //判断oss实例是否存在：如果不存在则创建，如果存在则获取
            if (!ossClient.doesBucketExist(OssProperties.BUCKET_NAME)) {
                /*
                //创建bucket
                ossClient.createBucket(OssProperties.BUCKET_NAME);
                log.info("bucket存储空间【{}】创建成功", OssProperties.BUCKET_NAME);
                //设置oss实例的访问权限：公共读
                ossClient.setBucketAcl(OssProperties.BUCKET_NAME, CannedAccessControlList.PublicRead);
                log.info("【{}】存储空间访问权限设置为公共读成功");
                */
            }
            //构建日期路径：avatar/2019/02/26/文件名
            String folder = new DateTime().toString("yyyy/MM/dd");
            //文件名：uuid.扩展名
            filename = UUID.randomUUID().toString() + filename.substring(filename.lastIndexOf("."));
            //文件根路径
            String key = module + "/" + folder + "/" + filename;
            // 创建PutObjectRequest对象。
            PutObjectRequest putObjectRequest = new PutObjectRequest(OssProperties.BUCKET_NAME, key, inputStream);
            // 创建PutObject请求。
            ossClient.putObject(putObjectRequest);
            log.info("oss文件上传成功");
            //阿里云文件绝对路径
            String endpoint = OssProperties.ENDPOINT.substring(OssProperties.ENDPOINT.lastIndexOf("//") + 2);
            //返回文件的访问路径
            return "https://" + OssProperties.BUCKET_NAME + "." + endpoint + "/" + key;
        } catch (OSSException oe) {
            log.error("OSSException 文件上传失败：{}", oe);
        } catch (ClientException ce) {
            log.error("ClientException 文件上传失败：{}", ExceptionUtils.getStackTrace(ce));
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
                log.info("关闭ossClient");
            }
        }
        return "";
    }

    @Override
    public void remove(String url) throws Exception {
        OSS ossClient = OssUtil.getOssClient();
        log.info("OSSClient实例创建成功");
        try {
            String endpoint = OssProperties.ENDPOINT.substring(OssProperties.ENDPOINT.lastIndexOf("//") + 2);
            //文件名（服务器上的文件路径）
            String host = "https://" + OssProperties.BUCKET_NAME + "." + endpoint + "/";
            String objectName = url.substring(host.length());
            // 删除文件或目录。如果要删除目录，目录必须为空。
            ossClient.deleteObject(OssProperties.BUCKET_NAME, objectName);
            log.info("{}文件删除成功", objectName);
        } catch (OSSException oe) {
            log.error("OSSException 文件删除失败：{}", oe);
            throw new Exception("文件删除失败");
        } catch (ClientException ce) {
            log.error("ClientException 文件删除失败：{}", ExceptionUtils.getStackTrace(ce));
            throw new Exception("文件删除失败");
        } finally {
            if (ossClient != null) {
                // 关闭OSSClient。
                ossClient.shutdown();
                log.info("关闭ossClient");
            }
        }
    }
}

```
## SpringBootDemoUploadApplication.java
```java
package top.javahouse.upload;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/*
 * @author:javahouse.top
 * @Description: 文件上传
 * @Date: 2023/11/7 18:00
 */
@SpringBootApplication
public class SpringBootDemoUploadApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootDemoUploadApplication.class, args);
	}
}
```
## OssUtil.java
```java
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
```
## application.yml
```yml
server:
  port: 8000

aliyun:
  oss:
    endpoint: https://oss-cn-beijing.aliyuncs.com #概述下面可以看到外网的endpoint
    keyId: LTAI4FoRURY565651PJ4rL35avnBZ
    keySecret: 8hxMpnFHx5565bvqHteo0GtM2QYDsQcEsS
    bucketName: 123

spring:
  application:
    name: upload


knife4j:
  enable: true # 开启增强配置
```

## 参考

https://blog.csdn.net/m0_70810591/article/details/131889616
