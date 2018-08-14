package com.todo.filters;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Preconditions;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.todo.dao.IRedisRepository;
import com.todo.utility.JwtTokenBuilder;

/**
 * @author JAYANTA ROY
 * @since 12/08/18
 */
public class PreFilter extends ZuulFilter {

	public static final Logger logger = LoggerFactory.getLogger(PreFilter.class);
	@Autowired
	private IRedisRepository redisRepository;

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.netflix.zuul.IZuulFilter#shouldFilter()
	 */
	@Override
	public boolean shouldFilter() {
		return true;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.netflix.zuul.ZuulFilter#filterType()
	 */
	@Override
	public String filterType() {
		return "pre";
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.netflix.zuul.ZuulFilter#filterOrder()
	 */
	@Override
	public int filterOrder() {
		return 1;
	}

	/**
	 * overridden run method to to provide filter condition
	 * 
	 * @see com.netflix.zuul.IZuulFilter#run()
	 */
	@Override
	public Object run() {
		logger.info("Prefilter run method started");
		// System.out.println("entering PreZuulFilter");
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();

		// System.out.println("noteService entered");
		if (request.getRequestURI().matches("(.*)/note_microservice/(.*)")) {
			logger.info("note-microservice found ");
			if(request.getHeader("JWTToken").equals(""))
			{
				try {
					throw new Exception("No Header provided");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			Preconditions.checkNotNull(request.getHeader("JWTToken"), "No Header provided");
			String tokenFromHeader = request.getHeader("JWTToken");
			System.out.println("token fom header " + tokenFromHeader);
			String userId = JwtTokenBuilder.parseJWT(tokenFromHeader).getId();
			Preconditions.checkNotNull(userId, "Invalid JWT token");
			String tokenFromRedis = redisRepository.getToken(userId);
			Preconditions.checkNotNull(tokenFromRedis, "error in setting token");
			ctx.addZuulRequestHeader("userId", userId);
			logger.info("Header set");
		}

		logger.info("Leaving filter");
		return "";
	}
}
