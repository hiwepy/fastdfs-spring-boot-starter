package com.github.tobato.fastdfs.spring.boot;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.github.tobato.fastdfs.spring.boot.utils.FastdfsUtils;

@ConfigurationProperties(FastdfsProperties.PREFIX)
/**
 * <p>Auto-configuration for FastdfsProperties.</p>
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public class FastdfsProperties {

	public static final String PREFIX = "fdfs";

	/** Whether Enable Fastdfs. */
	private boolean enabled = false;
	
	/**
	 * 存储服务对外服务的主机地址或域名
	 */
	private String endpoint;
	/**
	 * token secret key
	 */
	private String secretKey;
	/**
	 * token 过期时间，默认：100（单位秒）
	 */
	private int expire = 100;
	/**
	 */
	private String charset = FastdfsUtils.g_charset;
	
	
	/** @return return whether enabled is enabled. */
	public boolean isEnabled() {
		return enabled;
	}

	/** @param enabled set the enabled. */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	/** @return return the endpoint. */
	public String getEndpoint() {
		return endpoint;
	}

	/** @param endpoint set the endpoint. */
	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	/** @return return the secret key. */
	public String getSecretKey() {
		return secretKey;
	}

	/** @param secretKey set the secret key. */
	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	/** @return return the expire. */
	public int getExpire() {
		return expire;
	}

	/** @param expire set the expire. */
	public void setExpire(int expire) {
		this.expire = expire;
	}

	/** @return return the charset. */
	public String getCharset() {
		return charset;
	}

	/** @param charset set the charset. */
	public void setCharset(String charset) {
		this.charset = charset;
	}

}