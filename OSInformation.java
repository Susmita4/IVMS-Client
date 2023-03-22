package com.videonetics.util;

import java.io.Serializable;

public class OSInformation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -986663561227492961L;
	
	private String name;
	private String architecture;
	private String version;
	private String description;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getArchitecture() {
		return architecture;
	}
	public void setArchitecture(String architecture) {
		this.architecture = architecture;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public String toString() {
		return "OSInformation [name=" + name + ", architecture=" + architecture + ", version=" + version
				+ ", description=" + description + "]";
	}
}
