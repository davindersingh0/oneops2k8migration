package com.walmartlabs.strati.migrationtools.oneops2k8migration.controller;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.walmartlabs.strati.migrationtools.oneops2k8migration.service.CIAttributesService;
import com.walmartlabs.strati.migrationtools.oneops2k8migration.util.MigrationUtil;
import com.walmartlabs.strati.migrationtools.oneops2k8migration.util.OOPhases;
import com.walmartlabs.strati.migrationtools.oneops2k8migration.util.Platform;

/**
 * @author dsing17
 *
 */
@RestController
public class CIAttributesSvcController {

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private CIAttributesService svc;
	@Autowired
	private MigrationUtil util;
	@Autowired
	private Platform platform;

	public CIAttributesService getSvc() {
		return svc;
	}

	public void setSvc(CIAttributesService svc) {
		this.svc = svc;
	}

	public MigrationUtil getUtil() {
		return util;
	}

	public void setUtil(MigrationUtil util) {
		this.util = util;
	}

	public Platform getPlatform() {
		return platform;
	}

	public void setPlatform(Platform platform) {
		this.platform = platform;
	}

	/**
	 * 
	 * @param orgName
	 * @param assemblyName
	 * @param platformName
	 * @param envName
	 * @return Yamlified HashMap Object in String format
	 */
	@GetMapping("/org/{orgName}/assmebly/{assemblyName}/platform/{platformName}/env/{envName}")
	public String getCiAttributesForTomcatMigration(@PathVariable String orgName, @PathVariable String assemblyName,
			@PathVariable String platformName, @PathVariable String envName) {

		try {
			platform.setOrgName(orgName);
			platform.setAssemblyName(assemblyName);
			platform.setPlatformName(platformName);
			platform.setEnvName(envName);

			String ns = util.buildNsPath(orgName, assemblyName);
			platform.setNs(ns);
			platform.setNsForPlatformCiComponents(
					util.getnsForPlatformCiComponents(ns, platformName, OOPhases.OPERATE, envName));

			// TODO: VerifyPlatform
			Map<String, String> responseMap = svc.getPlatAttribsMapForTomcatAndArtifactCI(platform);
			log.info("responseMap: "+responseMap);

			responseMap.put("orgName", orgName);
			responseMap.put("assemblyName", assemblyName);
			responseMap.put("platformName", platformName);
			responseMap.put("envName", envName);
			
			String yamlifiedObjectStr = util.yamlifyObject(responseMap);
			log.info("yamlifiedObjectStr: "+yamlifiedObjectStr);


			return yamlifiedObjectStr;

		} catch (Exception e) {
			log.error("error while processing orgName {}, assemblyName {}, platformName {}, envName {}", orgName,
					assemblyName, platformName, envName);
			log.error("Error Message: " + e.getMessage());
			throw new RuntimeException("Error while fetching platform metadata: " + e.getMessage());
		}

	}

}
