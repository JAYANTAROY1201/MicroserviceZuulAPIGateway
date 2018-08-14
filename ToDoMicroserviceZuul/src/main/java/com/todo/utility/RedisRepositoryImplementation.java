package com.todo.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import com.todo.dao.IRedisRepository;




/**
 * Purpose:
 * <p>
 * <b>To store token generated at the time of login operation and provides
 * setToken,getToken and deleteToken methoods.Redis is an Open source in-memory
 * data structure store, used as a database, cache and message broker</b>
 * </p>
 * 
 * @author jayanta Roy
 * @since 19/07/2018
 * 
 *
 */
@Component
public class RedisRepositoryImplementation implements IRedisRepository {

	JwtTokenBuilder tokenProvider = new JwtTokenBuilder();

	@SuppressWarnings({ "rawtypes", "unused" })
	private RedisTemplate redisTemplate;
	private static HashOperations<String, String, String> hashOperations;
	private static String KEY = "TOKEN";

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Autowired
	public RedisRepositoryImplementation(RedisTemplate redisTemplate) {
		this.redisTemplate = redisTemplate;
		hashOperations = redisTemplate.opsForHash();
	}

	private static Logger logger = LoggerFactory.getLogger(RedisRepositoryImplementation.class);


	/**
	 * @param clientId
	 * @param jwtToken
	 */
	@SuppressWarnings("static-access")
	@Override
	public void setToken(String jwtToken) {
		String userId = tokenProvider.parseJWT(jwtToken).getId();
		hashOperations.put(KEY, userId, jwtToken);
		logger.info("Token set in redis");
	}

	/**
	 * @param clientId
	 * @return token
	 */
	@Override
	public String getToken(String userId) {
		logger.info("Getting token from redis");
		return hashOperations.get(KEY, userId);
	}

	/**
	 * @param clientId
	 */
	@Override
	public void deleteToken(String userId) {
		logger.info("Deleting token from redis");
		hashOperations.delete(KEY, userId);
	}
}
