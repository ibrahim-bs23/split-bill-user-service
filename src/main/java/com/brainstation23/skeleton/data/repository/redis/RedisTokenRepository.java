package com.brainstation23.skeleton.data.repository.redis;


import com.brainstation23.skeleton.common.utils.JacksonUtil;
import com.brainstation23.skeleton.core.domain.enums.ApplicationSettingsCode;
import com.brainstation23.skeleton.core.domain.enums.ResponseMessage;
import com.brainstation23.skeleton.core.domain.exceptions.DatabaseException;
import com.brainstation23.skeleton.core.service.ApplicationSettingsService;
import com.brainstation23.skeleton.data.entity.redis.RedisAccessToken;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class RedisTokenRepository {
    private final String FOLDER = "token:user-";
    private static final String FDR_SESSION_TOKEN = "token:cbs-eqcommonws-session";
    private static final String CBS_TOKEN = "token:cbs-eq-connect";
    @Value("${cbs.eq-connection.token.ttl-in-minutes}")
    private long timeoutInMinutes;

    private final RedisTemplate<String, String> template;
    private final ApplicationSettingsService applicationSettingService;

    public void save(RedisAccessToken redisAccessToken) {
        var val = JacksonUtil.objectToJson(redisAccessToken);
        if (val == null) {
            throw new DatabaseException(ResponseMessage.INVALID_REQUEST_DATA);
        }
        int tokenLiveMinutes =Integer.parseInt(applicationSettingService.getApplicationSettingByCode(ApplicationSettingsCode.JWT_TOKEN_LIVE_MIN.getCode()).get().getSettingValue());
        template.opsForValue().set(FOLDER.concat(redisAccessToken.getUserIdentity()), val, tokenLiveMinutes, TimeUnit.MINUTES);
    }

    public RedisAccessToken get(String userIdentity) {
        return JacksonUtil.jsonToInstance(template.opsForValue().get(FOLDER.concat(userIdentity)), RedisAccessToken.class);
    }

    public void delete(String userIdentity) {
        template.delete(FOLDER.concat(userIdentity));
    }

    public void saveCacheFDTokenAndSession(String token) {
        template.opsForValue().set(FDR_SESSION_TOKEN, token, timeoutInMinutes, TimeUnit.MINUTES);
    }

    public String getFdJwtTokenAndSession() {
        return template.opsForValue().get(FDR_SESSION_TOKEN);
    }

    public void deleteAll() {
        template.delete(FDR_SESSION_TOKEN);
        template.delete(CBS_TOKEN);
    }

    public void deleteEqCommonWsSession() {
        template.delete(FDR_SESSION_TOKEN);
    }
}
