package top.javahouse.upload.controlller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.javahouse.upload.entity.Student;
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

    @PostMapping("/date")
    public String date(@RequestBody Student student) {
        System.out.println(student);
        return "";
    }



}

