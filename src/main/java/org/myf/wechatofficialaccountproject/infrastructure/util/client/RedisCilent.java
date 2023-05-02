package org.myf.wechatofficialaccountproject.infrastructure.util.client;

import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @Author: myf
 * @CreateTime: 2023-03-07 14:22
 * @Description: RedisCilent 操作redis客户端
 */
@Component
public class RedisCilent {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public Set<String> addValueToRedisSet(String setKey, String value) {
        if (StringUtils.isBlank(setKey) || StringUtils.isBlank(value)) {
            return new HashSet();
        } else {
            Set<String> setMembers = stringRedisTemplate.opsForSet().members(setKey);
            if (Objects.isNull(setMembers)) {
                setMembers = Sets.newHashSet();
            } else {
                setMembers.add(value);
            }
            stringRedisTemplate.opsForSet().add(setKey, setMembers.toArray(new String[] {}));
            return stringRedisTemplate.opsForSet().members(setKey);
        }
    }

    public String addValueToRedis(String setKey, String value, Long timeOut) {
        if (StringUtils.isBlank(setKey) || StringUtils.isBlank(value)) {
            return null;
        } else {
            if (Objects.isNull(timeOut)) {
                stringRedisTemplate.opsForValue().set(setKey, value);
            } else {
                stringRedisTemplate.opsForValue().set(setKey, value, timeOut, TimeUnit.MILLISECONDS);
            }
            return stringRedisTemplate.opsForValue().get(setKey);
        }
    }

    public Set<String> getAllKeys(String pattern) {
        if (StringUtils.isBlank(pattern)) {
            return Sets.newHashSet();
        }
        return stringRedisTemplate.keys(pattern);
    }

    public String getValueByKey(String key) {
        if (StringUtils.isBlank(key)) {
            return "";
        }
        String forValue = stringRedisTemplate.opsForValue().get(key);
        return StringUtils.isEmpty(forValue) ? "" : forValue;
    }

    public Boolean deleteValueByKey(String key) {
        if (StringUtils.isBlank(key)) {
            return false;
        }
        return stringRedisTemplate.delete(key);
    }
}
