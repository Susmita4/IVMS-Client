package com.videonetics.util;

import java.io.Serializable;

public class VNICConfiguration implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3813788087772404950L;//
	
	public static final byte SOURCE_TYPE_INVALID = 0, SOURCE_TYPE_DHCP = 1, SOURCE_TYPE_STATIC = 2;
	public static final String[] SOURCE_TYPE_STR = {"Invalid", "DHCP", "Static"};

	private int sourceType = SOURCE_TYPE_INVALID;
	private String nicName;
	private String ip;
	private String publicIp;
	private String subnetMask;
	private String gateway;
	private String hostname;
	private String hardwareAdress;
	private String guid;

	public int getSourceType() {
		return sourceType;
	}
	public void setSourceType(int sourceType) {
		this.sourceType = sourceType;
	}
	public String getNicName() {
		return nicName;
	}
	public void setNicName(String nicName) {
		this.nicName = nicName;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getPublicIp() {
		return publicIp;
	}
	public void setPublicIp(String publicIp) {
		this.publicIp = publicIp;
	}
	public String getSubnetMask() {
		return subnetMask;
	}
	public void setSubnetMask(String subnetMask) {
		this.subnetMask = subnetMask;
	}
	public String getGateway() {
		return gateway;
	}
	public void setGateway(String gateway) {
		this.gateway = gateway;
	}
	public String getHostname() {
		return hostname;
	}
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	public String getHardwareAdress() {
		return hardwareAdress;
	}
	public void setHardwareAdress(String hardwareAdress) {
		this.hardwareAdress = hardwareAdress;
	}
	public String getGuid() {
		return guid;
	}
	public void setGuid(String guid) {
		this.guid = guid;
	}

	@Override
	public String toString() {
		return "VNICConfiguration [sourceType=" + SOURCE_TYPE_STR[sourceType] + ", nicName="
				+ nicName + ", ip=" + ip + ", publicIp=" + publicIp + ", subnetMask=" + subnetMask
				+ ", gateway=" + gateway + ", hostname=" + hostname
				+ ", guid=" + guid
				+ ", hardwareAdress=" + hardwareAdress + "]";
	}

	public String getJsonHtmlEscaped() {
		return VGsonUtils.getJsonHtmlEscaped(this);
	}
}
