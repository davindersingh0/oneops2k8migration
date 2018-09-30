package com.walmartlabs.strati.migrationtools.oneops2k8migration.controller;

import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestComponent;

import com.walmartlabs.strati.migrationtools.oneops2k8migration.Oneops2k8migrationApplicationTests;

@TestComponent
public class CIAttributesSvcControllerTest extends Oneops2k8migrationApplicationTests{

	private final Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	CIAttributesSvcController controller;
	
	@Ignore
	@Test
	public void getCiAttributesForTomcatMigrationTest() {
		log.info("TODO getCiAttributesForTomcatMigrationTest(");
		
		
		
	}
	
	
	
	
	
}
