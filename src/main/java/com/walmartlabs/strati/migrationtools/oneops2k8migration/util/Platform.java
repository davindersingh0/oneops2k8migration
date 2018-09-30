package com.walmartlabs.strati.migrationtools.oneops2k8migration.util;

import org.springframework.stereotype.Component;

/**
 * @author dsing17
 *
 */
@Component
public class Platform {

	String orgName;
	String assemblyName;
	String platformName;
	String envName;
	String ns;
	String nsForPlatformCiComponents;

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getAssemblyName() {
		return assemblyName;
	}

	public void setAssemblyName(String assemblyName) {
		this.assemblyName = assemblyName;
	}

	public String getPlatformName() {
		return platformName;
	}

	public void setPlatformName(String platformName) {
		this.platformName = platformName;
	}

	public String getEnvName() {
		return envName;
	}

	public void setEnvName(String envName) {
		this.envName = envName;
	}

	public String getNs() {
		return ns;
	}

	public void setNs(String ns) {
		this.ns = ns;
	}

	public String getNsForPlatformCiComponents() {
		return nsForPlatformCiComponents;
	}

	public void setNsForPlatformCiComponents(String nsForPlatformCiComponents) {
		this.nsForPlatformCiComponents = nsForPlatformCiComponents;
	}

}
