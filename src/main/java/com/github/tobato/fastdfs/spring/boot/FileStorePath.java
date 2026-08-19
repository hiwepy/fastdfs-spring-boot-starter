package com.github.tobato.fastdfs.spring.boot;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;

/**
 * <p>Auto-configuration for FileStorePath.</p>
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public class FileStorePath extends StorePath {

    /*
     * 解析路径
     */
    private static final String SPLIT_GROUP_NAME_AND_FILENAME_SEPERATOR = "/";
    
	/*
	 * 缩略图访问地址（图片类型文件）
	 */
	private String thumb;
	
    /*
     * 存储文件路径
     */
    public FileStorePath() {
        super();
    }

    public FileStorePath(StorePath store, String thumb) {
        super(store.getGroup(), store.getPath());
        this.thumb = thumb;
    }
    
    public FileStorePath(String group, String path, String thumb) {
        super(group, path);
        this.thumb = thumb;
    }

	/** @return return the thumb. */
	public String getThumb() {
		return thumb;
	}

	/** @param thumb set the thumb. */
	public void setThumb(String thumb) {
		this.thumb = thumb;
	}
	
    /** @return return the full thumb. */
    public String getFullThumb() {
        return this.getGroup().concat(SPLIT_GROUP_NAME_AND_FILENAME_SEPERATOR).concat(this.thumb);
    }
	
}
