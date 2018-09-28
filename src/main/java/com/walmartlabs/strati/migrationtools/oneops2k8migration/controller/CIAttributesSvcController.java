package com.walmartlabs.strati.migrationtools.oneops2k8migration.controller;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.walmartlabs.strati.migrationtools.oneops2k8migration.service.CIAttributesService;
import com.walmartlabs.strati.migrationtools.oneops2k8migration.util.MigrationUtil;

@RestController
public class CIAttributesSvcController {

	private final Logger log = LoggerFactory.getLogger(getClass());

	CIAttributesService svc;
	MigrationUtil util;

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

	@GetMapping("/ns/{ns}/platform/{platformName}/env/{envName}")
	public String getCiAttributesForTomcatMigration(@PathVariable String ns, @PathVariable String platformName,
			@PathVariable String envName) {

		try {
			Map<String, String> responseMap = svc.getPlatAttribsMapForTomcatAndArtifactCI(ns, platformName, envName);

			String yamlifiedObjectStr = util.yamlifyObject(responseMap);

			return yamlifiedObjectStr;

		} catch (Exception e) {
			log.error("error while processing ns {}, platformName {}, envName {}", ns, platformName, envName);
			log.error("Error while fetching platform metadata: " + e.getMessage());
			throw new RuntimeException("Error while fetching platform metadata: " + e.getMessage());
		}

	}

	@GetMapping("/hello")
	public String getDefaultMessage(@PathVariable String ns, @PathVariable String platformName,
			@PathVariable String envName) {

		try {

			return "Hello from oneops2k8migration tool";

		} catch (Exception e) {
			log.error("error while processing ns {}, platformName {}, envName {}", ns, platformName, envName);
			log.error("Error while fetching platform metadata: " + e.getMessage());
			throw new RuntimeException("Error while fetching platform metadata: " + e.getMessage());
		}

	}

}
