package com.example.demoback.api.security.rest;

import com.example.demoback.api.security.security.LockUser;
import com.example.demoback.api.security.service.LockUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 锁定用户控制器
 */
@RestController
@RequestMapping("/auth/")
public class LockUserController {

    @Autowired
    private LockUserService lockUserService;

    // 查询锁定用户
    @GetMapping(value = "/lockUserAll")
    @PreAuthorize("hasAnyRole('ADMIN','LOCK_ALL','LOCK_SELECT')")
    public ResponseEntity lockAll(String filter, Pageable pageable) {
        return new ResponseEntity<>(lockUserService.getAll(filter, pageable), HttpStatus.OK);
    }

    // 锁定用户解锁
    @PostMapping(value = "/unLockUser")
    @PreAuthorize("hasAnyRole('ADMIN','LOCK_ALL','LOCK_DELETE')")
    public ResponseEntity unLock(LockUser lockUser) throws Exception {
        lockUserService.unLock(lockUser);
        return new ResponseEntity(HttpStatus.OK);
    }

}
