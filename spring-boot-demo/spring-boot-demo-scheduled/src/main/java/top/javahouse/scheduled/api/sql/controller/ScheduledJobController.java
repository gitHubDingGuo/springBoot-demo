package top.javahouse.scheduled.api.sql.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.javahouse.scheduled.api.sql.ScheduledJob;
import top.javahouse.scheduled.api.sql.service.ScheduledJobService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/job")
public class ScheduledJobController {

    @Autowired
    private ScheduledJobService scheduledJobService;

    @PostMapping(value = "/update")
    public ResponseEntity<String> update(HttpServletRequest request, ScheduledJob scheduledJob) {
        if (scheduledJobService.updateOne(scheduledJob)) {
            return ResponseEntity.ok("修改成功");
        } else {
            return ResponseEntity.ok("修改成功");

        }

    }
}

