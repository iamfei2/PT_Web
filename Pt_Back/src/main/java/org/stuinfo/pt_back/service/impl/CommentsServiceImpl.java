package org.stuinfo.pt_back.service.impl;

import org.stuinfo.pt_back.entity.Comments;
import org.stuinfo.pt_back.mapper.CommentsMapper;
import org.stuinfo.pt_back.service.ICommentsService;
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
public class CommentsServiceImpl extends ServiceImpl<CommentsMapper, Comments> implements ICommentsService {

}
