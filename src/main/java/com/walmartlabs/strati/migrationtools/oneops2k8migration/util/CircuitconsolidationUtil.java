package com.walmartlabs.strati.migrationtools.oneops2k8migration.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.walmartlabs.strati.migrationtools.oneops2k8migration.exception.UnSupportedOperation;

@Component
public class CircuitconsolidationUtil {

	private final static Logger log = LoggerFactory.getLogger(CircuitconsolidationUtil.class);

	public static String getnsForPlatformCiComponents(String ns, String platformName, OOPhases ooPhase,
			String envName) {

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
