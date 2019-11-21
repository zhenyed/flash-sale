package io.github.zhenyed.api.lock;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import io.github.zhenyed.api.common.vo.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@EnableConfigurationProperties({RedisProperties.class})
@EnableCaching
public class RedisAutoConfigure {

    @Autowired
    private RedisProperties redisProperties;

    @Bean
    public RedisCacheConfiguration redisCacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(
                        RedisSerializationContext
                                .SerializationPair
                                .fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(
                        RedisSerializationContext
                                .SerializationPair
                                .fromSerializer(new FastJsonRedisSerializer(CommonResult.class)));
    }

    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        FastJsonRedisSerializer fastJsonRedisSerializer = new FastJsonRedisSerializer(Object.class);
        // value值的序列化采用 fastJsonRedisSerializer
        template.setValueSerializer(fastJsonRedisSerializer);
        template.setHashValueSerializer(fastJsonRedisSerializer);
        // key的序列化采用 StringRedisSerializer
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

    @Bean
    public JedisConnectionFactory redisConnectionFactory(RedisStandaloneConfiguration config) {
        return new JedisConnectionFactory(config);
    }
//
//    @Bean
//    public JedisConnectionFactory redisClusterConnectionFactory(RedisClusterConfiguration config) {
//        return new JedisConnectionFactory(config);
//    }

//    @Bean
//    public RedisClusterConfiguration redisClusterConfig() {
//        return new RedisClusterConfiguration(redisProperties.getCluster().getNodes());
//    }

    @Bean
    public RedisStandaloneConfiguration redisConfig() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(redisProperties.getHost());
        config.setPort(redisProperties.getPort());
        config.setDatabase(redisProperties.getDatabase());
        config.setPassword(RedisPassword.of(redisProperties.getPassword()));
        return config;
    }

}
