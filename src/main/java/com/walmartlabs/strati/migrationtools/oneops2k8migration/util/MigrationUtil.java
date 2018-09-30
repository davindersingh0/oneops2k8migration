package com.walmartlabs.strati.migrationtools.oneops2k8migration.util;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import com.walmartlabs.strati.migrationtools.oneops2k8migration.exception.UnSupportedOperation;

/**
 * @author dsing17
 *
 */
@Component
public class MigrationUtil {

	private final Logger log = LoggerFactory.getLogger(getClass());

	public String yamlifyObject(Map<String, String> map) {

		try {

			DumperOptions options = new DumperOptions();
			options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
			options.setPrettyFlow(true);

			Yaml yaml = new Yaml(options);
			String yamlifiedObject = yaml.dump(map);

			return yamlifiedObject;
		} catch (Exception e) {
			throw new RuntimeException("error while yamlifying object :" + map);
		}

	}

	public String buildNsPath(String orgName, String assemblyName) {

		StringBuffer buffer = new StringBuffer();
		buffer.append("/").append(orgName).append("/").append(assemblyName);
		return buffer.toString();
	}

	public String getnsForPlatformCiComponents(String ns, String platformName, OOPhases ooPhase, String envName) {

		switch (ooPhase) {
		case DESIGN:

			return ns + "/_design/" + platformName;
		case TRANSITION:

			return ns + "/" + envName + "/manifest/" + platformName + "/1";
		case OPERATE:
			return ns + "/" + envName + "/bom/" + platformName + "/1";

		default:
			log.error("ooPhase {} not supported", ooPhase);

			throw new UnSupportedOperation(ooPhase + " not supported");

		}

	}

}
