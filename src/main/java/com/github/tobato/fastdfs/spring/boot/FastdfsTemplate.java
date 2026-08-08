/*
 * Copyright (c) 2018, hiwepy (https://github.com/easy-4-java).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.github.tobato.fastdfs.spring.boot;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.spring.boot.utils.FastdfsUtils;

/**
 * TODO
 * @author [@Loong Wan](https://github.com/loong10k)
 */
public class FastdfsTemplate {

	private Logger logger = LoggerFactory.getLogger(getClass());
	private FastdfsProperties fastdfsProperties; 
	
	public FastdfsTemplate(FastdfsProperties fastdfsProperties) {
		this.fastdfsProperties = fastdfsProperties;
	}

	public String getEndpoint() {
		return StringUtils.endsWith(fastdfsProperties.getEndpoint(), "/") ? fastdfsProperties.getEndpoint() : fastdfsProperties.getEndpoint() + "/";
	}
	
	public String getAccsssURL(String group, String path) throws Exception {
		
		// 以秒为单位
		int ts = (int)(System.currentTimeMillis()/1000);
		// 最小有效期限制5秒
		ts = ts + Math.max(5, fastdfsProperties.getExpire()); 
		
		// 重置字符集
		if(!StringUtils.equalsIgnoreCase(FastdfsUtils.g_charset, fastdfsProperties.getCharset())) {
			FastdfsUtils.g_charset = fastdfsProperties.getCharset();
		}
		
		// 生成token
		String token = FastdfsUtils.getToken(path, ts, fastdfsProperties.getSecretKey());
		
		if(logger.isDebugEnabled()) {
			logger.debug("ts:" + ts + ", token:" + token);
		}

		//	输出为：ts:1484735390, token:ada4c7f1a65e125e3a55a837d0bff1eb
		//	那么请求的url为:192.168.14.153/M00/01/74/wKgOmVh_dH6AE-f3AAA2_mDXnps704.jpg?ts=1484735390&token=ada4c7f1a65e125e3a55a837d0bff1eb
		
		return getEndpoint() + group + "/" + path + "?ts=" + ts + "&token=" + token;
		
	}
	
	public String getAccsssURL(StorePath storePath) throws Exception {
		
		// 以秒为单位
		int ts = (int)(System.currentTimeMillis()/1000);
		// 最小有效期限制5秒
		ts = ts + Math.max(5, fastdfsProperties.getExpire()); 
		
		// 重置字符集
		if(!StringUtils.equalsIgnoreCase(FastdfsUtils.g_charset, fastdfsProperties.getCharset())) {
			FastdfsUtils.g_charset = fastdfsProperties.getCharset();
		}
		
		// 生成token
		String token = FastdfsUtils.getToken(storePath.getPath(), ts, fastdfsProperties.getSecretKey());
		
		if(logger.isDebugEnabled()) {
			logger.debug("ts:" + ts + ", token:" + token);
		}

		//	输出为：ts:1484735390, token:ada4c7f1a65e125e3a55a837d0bff1eb
		//	那么请求的url为:192.168.14.153/M00/01/74/wKgOmVh_dH6AE-f3AAA2_mDXnps704.jpg?ts=1484735390&token=ada4c7f1a65e125e3a55a837d0bff1eb
		
		return getEndpoint() + storePath.getFullPath() + "?ts=" + ts + "&token=" + token;
	}
	
	public String getThumbAccsssURL(FileStorePath storePath) throws Exception {
		
		// 以秒为单位
		int ts = (int)(System.currentTimeMillis()/1000);
		// 最小有效期限制5秒
		ts = ts + Math.max(5, fastdfsProperties.getExpire()); 
		
		// 重置字符集
		if(!StringUtils.equalsIgnoreCase(FastdfsUtils.g_charset, fastdfsProperties.getCharset())) {
			FastdfsUtils.g_charset = fastdfsProperties.getCharset();
		}
		
		// 生成token
		String token = FastdfsUtils.getToken(storePath.getThumb(), ts, fastdfsProperties.getSecretKey());
		
		if(logger.isDebugEnabled()) {
			logger.debug("ts:" + ts + ", token:" + token);
		}

		//	输出为：ts:1484735390, token:ada4c7f1a65e125e3a55a837d0bff1eb
		//	那么请求的url为:192.168.14.153/M00/01/74/wKgOmVh_dH6AE-f3AAA2_mDXnps704_100x100.jpg?ts=1484735390&token=ada4c7f1a65e125e3a55a837d0bff1eb
		
		return getEndpoint() + storePath.getFullThumb() + "?ts=" + ts + "&token=" + token;
	}
	
	
}
