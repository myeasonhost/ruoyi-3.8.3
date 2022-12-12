package com.ruoyi.tron.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

@Configuration
public class RedisQueueConfig {

	/**
	 * 初始化监听器
	 */
	@Bean
	public RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
												   MessageListenerAdapter listenerAdapterTRX,
												   MessageListenerAdapter listenerAdapterETH,
												   MessageListenerAdapter listenerAdapterUSDT_TRC20,
												   MessageListenerAdapter listenerAdapterUSDT_ERC20,
												   MessageListenerAdapter listenerAdapterFROMServiceNO,
												   MessageListenerAdapter listenerAdapterFROMServiceYES,
												   MessageListenerAdapter listenerAdapterCreateIpArea,
												   MessageListenerAdapter listenerAdapterSendMsg) {
		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.addMessageListener(listenerAdapterTRX, new PatternTopic("transferTRX"));
		container.addMessageListener(listenerAdapterETH, new PatternTopic("transferETH"));
		container.addMessageListener(listenerAdapterUSDT_TRC20, new PatternTopic("transferUSDT_TRC20"));
		container.addMessageListener(listenerAdapterUSDT_ERC20, new PatternTopic("transferUSDT_ERC20"));
		container.addMessageListener(listenerAdapterFROMServiceNO, new PatternTopic("transferFROMServiceNO"));
		container.addMessageListener(listenerAdapterFROMServiceYES, new PatternTopic("transferFROMServiceYES"));
		container.addMessageListener(listenerAdapterCreateIpArea, new PatternTopic("createIpArea"));
		container.addMessageListener(listenerAdapterSendMsg, new PatternTopic("sendMsg"));
		return container;
	}

	/**
	 * 配置监听器1
	 */
	@Bean
	public MessageListenerAdapter listenerAdapterTRX(Receiver receiver) {
		MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(receiver, "transferTRX");
		// 使用Jackson2JsonRedisSerialize 替换默认序列化(默认采用的是JDK序列化)
		Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
		ObjectMapper om = new ObjectMapper();
		om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		jackson2JsonRedisSerializer.setObjectMapper(om);
		messageListenerAdapter.setSerializer(jackson2JsonRedisSerializer);
		return messageListenerAdapter;
	}

	/**
	 * 配置监听器1
	 */
	@Bean
	public MessageListenerAdapter listenerAdapterETH(Receiver receiver) {
		MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(receiver, "transferETH");
		// 使用Jackson2JsonRedisSerialize 替换默认序列化(默认采用的是JDK序列化)
		Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
		ObjectMapper om = new ObjectMapper();
		om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		jackson2JsonRedisSerializer.setObjectMapper(om);
		messageListenerAdapter.setSerializer(jackson2JsonRedisSerializer);
		return messageListenerAdapter;
	}

	/**
	 * 配置监听器2
	 */
	@Bean
	public MessageListenerAdapter listenerAdapterUSDT_TRC20(Receiver receiver) {
		MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(receiver, "transferUSDT_TRC20");
		// 使用Jackson2JsonRedisSerialize 替换默认序列化(默认采用的是JDK序列化)
		Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
		ObjectMapper om = new ObjectMapper();
		om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		jackson2JsonRedisSerializer.setObjectMapper(om);
		messageListenerAdapter.setSerializer(jackson2JsonRedisSerializer);
		return messageListenerAdapter;
	}

	/**
	 * 配置监听器2
	 */
	@Bean
	public MessageListenerAdapter listenerAdapterUSDT_ERC20(Receiver receiver) {
		MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(receiver, "transferUSDT_ERC20");
		// 使用Jackson2JsonRedisSerialize 替换默认序列化(默认采用的是JDK序列化)
		Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
		ObjectMapper om = new ObjectMapper();
		om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		jackson2JsonRedisSerializer.setObjectMapper(om);
		messageListenerAdapter.setSerializer(jackson2JsonRedisSerializer);
		return messageListenerAdapter;
	}

	/**
	 * 配置监听器3
	 */
	@Bean
	public MessageListenerAdapter listenerAdapterFROMServiceNO(Receiver receiver) {
		MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(receiver, "transferFROMServiceNO");
		// 使用Jackson2JsonRedisSerialize 替换默认序列化(默认采用的是JDK序列化)
		Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
		ObjectMapper om = new ObjectMapper();
		om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		jackson2JsonRedisSerializer.setObjectMapper(om);
		messageListenerAdapter.setSerializer(jackson2JsonRedisSerializer);
		return messageListenerAdapter;
	}

	/**
	 * 配置监听器4
	 */
	@Bean
	public MessageListenerAdapter listenerAdapterFROMServiceYES(Receiver receiver) {
		MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(receiver, "transferFROMServiceYES");
		// 使用Jackson2JsonRedisSerialize 替换默认序列化(默认采用的是JDK序列化)
		Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
		ObjectMapper om = new ObjectMapper();
		om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		jackson2JsonRedisSerializer.setObjectMapper(om);
		messageListenerAdapter.setSerializer(jackson2JsonRedisSerializer);
		return messageListenerAdapter;
	}

	/**
	 * 配置监听器5
	 */
	@Bean
	public MessageListenerAdapter listenerAdapterCreateIpArea(Receiver receiver) {
		MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(receiver, "createIpArea");
		// 使用Jackson2JsonRedisSerialize 替换默认序列化(默认采用的是JDK序列化)
		Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
		ObjectMapper om = new ObjectMapper();
		om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		jackson2JsonRedisSerializer.setObjectMapper(om);
		messageListenerAdapter.setSerializer(jackson2JsonRedisSerializer);
		return messageListenerAdapter;
	}

	/**
	 * 配置监听器6
	 */
	@Bean
	public MessageListenerAdapter listenerAdapterSendMsg(Receiver receiver) {
		MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(receiver, "sendMsg");
		// 使用Jackson2JsonRedisSerialize 替换默认序列化(默认采用的是JDK序列化)
		Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
		ObjectMapper om = new ObjectMapper();
		om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		jackson2JsonRedisSerializer.setObjectMapper(om);
		messageListenerAdapter.setSerializer(jackson2JsonRedisSerializer);
		return messageListenerAdapter;
	}


}
