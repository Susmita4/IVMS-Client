package com.videonetics.model.output;

import java.io.Serializable;

import com.videonetics.util.VUtilities;


public class VFile implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1085033017524221697L;
	private String path;
	private long startTimestamp;
	private long endTimestamp;
	private long lastModifiedTimestamp;
	private String md5Digest = "";

	private boolean isDirectory = false;
	private long length;
	
	public VFile(String path) {
		this.path = path;
	}

	public VFile(String path, long startTimestamp, long endTimestamp) {
		this.path = path;
		this.startTimestamp = startTimestamp;
		this.endTimestamp = endTimestamp;
	}
	
	public VFile(String path, long startTimestamp, long endTimestamp, boolean isDirectory) {
		this.path = path;
		this.startTimestamp = startTimestamp;
		this.endTimestamp = endTimestamp;
		this.isDirectory = isDirectory;
	}
	
	public VFile(String path, long startTimestamp, long endTimestamp, long lastModifiedTimestamp, boolean isDirectory) {
		this.path = path;
		this.startTimestamp = startTimestamp;
		this.endTimestamp = endTimestamp;
		this.lastModifiedTimestamp = lastModifiedTimestamp;
		this.isDirectory = isDirectory;
	}
	
	public VFile(String path, long startTimestamp, long endTimestamp, long lastModifiedTimestamp, boolean isDirectory, long length, String md5Digest) {
		this.path = path;
		this.startTimestamp = startTimestamp;
		this.endTimestamp = endTimestamp;
		this.lastModifiedTimestamp = lastModifiedTimestamp;
		this.isDirectory = isDirectory;
		this.length = length;
		this.md5Digest = md5Digest;
	}
	
	public VFile(String path, long lastModifiedTimestamp, boolean isDirectory) {
		this.path = path;
		this.startTimestamp = lastModifiedTimestamp;
		this.endTimestamp = lastModifiedTimestamp;
		this.lastModifiedTimestamp = lastModifiedTimestamp;
		this.isDirectory = isDirectory;
	}
	
	public VFile() {
		
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}
	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}
	/**
	 * @return the startTimestamp
	 */
	public long getStartTimestamp() {
		return startTimestamp;
	}
	/**
	 * @param startTimestamp the startTimestamp to set
	 */
	public void setStartTimestamp(long startTimestamp) {
		this.startTimestamp = startTimestamp;
	}
	/**
	 * @return the endTimestamp
	 */
	public long getEndTimestamp() {
		return endTimestamp;
	}
	/**
	 * @param endTimestamp the endTimestamp to set
	 */
	public void setEndTimestamp(long endTimestamp) {
		this.endTimestamp = endTimestamp;
	}
	
	public long lastModified() {
		return lastModifiedTimestamp;
	}

	public void lastModified(long lastModifiedTimestamp) {
		this.lastModifiedTimestamp = lastModifiedTimestamp;
	}
	
	public String getMd5Digest() {
		return md5Digest;
	}

	public void setMd5Digest(String md5Digest) {
		this.md5Digest = md5Digest;
	}
	
	public boolean isDirectory() {
		return isDirectory;
	}

	public void setDirectory(boolean isDirectory) {
		this.isDirectory = isDirectory;
	}
	
	public long length() {
		return length;
	}

	public void length(long length) {
		this.length = length;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "VFile [path=" + path + ", startTimestamp=" + VUtilities.timeToString(startTimestamp)
				+ ", endTimestamp=" + VUtilities.timeToString(endTimestamp)
				 + ", lastModifiedTimestamp=" + VUtilities.timeToString(lastModifiedTimestamp)
				 + ", md5Digest=" + md5Digest
				  + ", isDirectory=" + isDirectory + ", length=" + length + "]";
	}
}
