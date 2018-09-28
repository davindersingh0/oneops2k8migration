package com.walmartlabs.strati.migrationtools.oneops2k8migration.util;

import org.springframework.stereotype.Component;

@Component
public enum OOPhases {

	DESIGN("design"), TRANSITION("transition"), OPERATE("operate");

	public String value;

	OOPhases(String ooPhase) {

		this.value = ooPhase;

	}

	public String getValue() {
		return value;
	}
}
