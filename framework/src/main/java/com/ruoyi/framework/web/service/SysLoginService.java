package com.ruoyi.framework.web.service;

import javax.annotation.Resource;

import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.RegisterBody;
import com.ruoyi.common.exception.user.RegisterException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.galaxy.domain.BitInviteCode;
import com.ruoyi.galaxy.service.IBitInviteCodeService;
import com.ruoyi.galaxy.service.IBitPointsRecordService;
import com.ruoyi.system.service.ISysConfigService;
import com.ruoyi.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.CustomException;
import com.ruoyi.common.exception.user.CaptchaException;
import com.ruoyi.common.exception.user.CaptchaExpireException;
import com.ruoyi.common.exception.user.UserPasswordNotMatchException;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.framework.manager.AsyncManager;
import com.ruoyi.framework.manager.factory.AsyncFactory;

/**
 * 登录校验方法
 * 
 * @author ruoyi
 */
@Component
public class SysLoginService
{
    @Autowired
    private TokenService tokenService;

    @Resource
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private IBitInviteCodeService inviteCodeService;

    @Autowired
    private IBitPointsRecordService pointsRecordService;

    public AjaxResult register(RegisterBody registerBody) {
        String verifyKey = Constants.CAPTCHA_CODE_KEY + registerBody.getUuid();
        String captcha = redisCache.getCacheObject(verifyKey);
        redisCache.deleteObject(verifyKey);
        if (captcha == null)
        {
            throw new CaptchaExpireException();
        }
        if (!registerBody.getCode().equalsIgnoreCase(captcha))
        {
            throw new CaptchaException();
        }
        if(!registerBody.getPasswordConfirm().equals(registerBody.getPassword())) {
            throw new RegisterException(Constants.PASSWORD_NOT_MATCH, null);
        }
        BitInviteCode inviteCode = null;
        if(configService.selectConfigByKey("sys.user.invite.only").equals("0")) {
            //必须邀请注册
            if (StringUtils.isEmpty(registerBody.getInviteCode())) {
                throw new RegisterException(Constants.INVITE_CODE_ERROR, null);
            }
            inviteCode = inviteCodeService.selectGlxInviteCodeByCode(registerBody.getInviteCode());
            if (inviteCode == null || !inviteCode.getStatus().equals("0")) {
                throw new RegisterException(Constants.INVITE_CODE_ERROR, null);
            }
            inviteCode.setStatus("1");
        }
        if (userService.checkUserNameUnique(registerBody.getUsername()).equals(UserConstants.NOT_UNIQUE)) {
            throw new RegisterException(Constants.USERNAME_TAKEN, null);
        }
        SysUser user = new SysUser();
        user.setNickName(UUID.fastUUID().toString().substring(0, 8));
        user.setUserName(registerBody.getUsername());
        user.setPassword(registerBody.getPassword());
        user.setEmail(registerBody.getEmail());
        user.setDownloaded(0L);
        user.setUploaded(0L);
        user.setPoints(0D);
        if (userService.checkEmailUnique(user).equals(UserConstants.NOT_UNIQUE)) {
            throw new RegisterException(Constants.EMAIL_TAKEN, null);
        }
        user.setRoleIds(new Long[]{100L});

        user.setToken(UUID.fastUUID().toString());
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        int ret = userService.insertUser(user);

        Double pointAward = Double.parseDouble(configService.selectConfigByKey("sys.user.reward.points"));
        if (pointAward > 0) {
            pointsRecordService.addPointToUser(user.getUserId(), pointAward, "[系统]注册赠送");
        }

        if (inviteCode != null) {
            inviteCode.setInviteUserId(user.getUserId());
            inviteCode.setUpdateBy(user.getUserName());
            inviteCodeService.updateGlxInviteCode(inviteCode);
        }
        return AjaxResult.success(ret);
    }
    /**
     * 登录验证
     * 
     * @param username 用户名
     * @param password 密码
     * @param code 验证码
     * @param uuid 唯一标识
     * @return 结果
     */
    public String login(String username, String password, String code, String uuid)
    {
        String verifyKey = Constants.CAPTCHA_CODE_KEY + uuid;
        String captcha = redisCache.getCacheObject(verifyKey);
        redisCache.deleteObject(verifyKey);
        if (captcha == null)
        {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.expire")));
            throw new CaptchaExpireException();
        }
        if (!code.equalsIgnoreCase(captcha))
        {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.error")));
            throw new CaptchaException();
        }
        // 用户验证
        Authentication authentication = null;
        try
        {
            // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(username, password));
        }
        catch (Exception e)
        {
            if (e instanceof BadCredentialsException)
            {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
                throw new UserPasswordNotMatchException();
            }
            else
            {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, e.getMessage()));
                throw new CustomException(e.getMessage());
            }
        }
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        // 生成token
        return tokenService.createToken(loginUser);
    }
}
