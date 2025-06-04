package com.ruoyi.galaxy.job;

import com.ruoyi.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("GalaxyUserTask")
public class UserJob {
    @Autowired
    private ISysUserService userService;

    public void AutoDisableUser() {
        userService.disableUserJob();
    }
}
