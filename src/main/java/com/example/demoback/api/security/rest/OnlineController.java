package com.example.demoback.api.security.rest;

import com.example.demoback.api.security.service.OnlineUserService;
import io.swagger.annotations.Api;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/auth/online")
@Api(tags = "系统：在线用户管理")
public class OnlineController {

    private final OnlineUserService onlineUserService;

    public OnlineController(OnlineUserService onlineUserService) {
        this.onlineUserService = onlineUserService;
    }

    // 查询在线用户
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','ONLINE_ALL','ONLINE_ALL_SELECT')")
    public ResponseEntity getAll(String filter, Pageable pageable){
        return new ResponseEntity<>(onlineUserService.getAll(filter, pageable),HttpStatus.OK);
    }

    // 导出数据
    @GetMapping(value = "/download")
    @PreAuthorize("hasAnyRole('ADMIN','ONLINE_ALL','ONLINE_ALL_DOWNLOAD')")
    public void download(HttpServletResponse response, String filter) throws IOException {
        onlineUserService.download(onlineUserService.getAll(filter), response);
    }

    // 踢出用户
    @DeleteMapping(value = "/{key}")
    @PreAuthorize("hasAnyRole('ADMIN','ONLINE_ALL','ONLINE_ALL_DELETE')")
    public ResponseEntity delete(@PathVariable String key) throws Exception {
        onlineUserService.kickOut(key);
        return new ResponseEntity(HttpStatus.OK);
    }
}
