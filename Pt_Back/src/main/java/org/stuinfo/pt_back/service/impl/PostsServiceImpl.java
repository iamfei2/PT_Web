package org.stuinfo.pt_back.service.impl;

import org.stuinfo.pt_back.entity.Posts;
import org.stuinfo.pt_back.mapper.PostsMapper;
import org.stuinfo.pt_back.service.IPostsService;
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
public class PostsServiceImpl extends ServiceImpl<PostsMapper, Posts> implements IPostsService {

}
