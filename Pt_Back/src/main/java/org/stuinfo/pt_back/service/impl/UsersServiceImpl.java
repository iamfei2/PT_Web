package org.stuinfo.pt_back.service.impl;

import org.stuinfo.pt_back.entity.Users;
import org.stuinfo.pt_back.mapper.UsersMapper;
import org.stuinfo.pt_back.service.IUsersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lt
 * @since 2025-04-26
 */
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements IUsersService {

}
