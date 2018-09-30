package com.walmartlabs.strati.migrationtools.oneops2k8migration.util;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import com.walmartlabs.strati.migrationtools.oneops2k8migration.Oneops2k8migrationApplicationTests;
import com.walmartlabs.strati.migrationtools.oneops2k8migration.dal.KloopzCmDal;

/**
 * @author dsing17
 *
 */
public class MigrationUtilTest extends Oneops2k8migrationApplicationTests {

	@Autowired
	MigrationUtil util;

	
	@MockBean
	KloopzCmDal dal;
	
	private String orgName;
	private String assemblyName;
	private String platformName;
	private String envName = "dev";

	@Before
	public void init() {

		orgName = "TestOrg";
		assemblyName = "TestAssembly";
		platformName = "TestPlatform";
	}

	@Test
	public void yamlifyObjectTest() {
		Map<String, String> map = new HashMap<>();
		map.put("key1", "value1");
		map.put("key2", "value2");
		map.put("key3", "value3");

		DumperOptions options = new DumperOptions();
		options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
		options.setPrettyFlow(true);

		Yaml yaml = new Yaml(options);
		String expectedYaml = yaml.dump(map);
		String actualYaml = util.yamlifyObject(map);

		Assert.assertEquals(expectedYaml, actualYaml);

	}

	@Test
	public void buildNsPathTest() {
		String expectedNs = "/TestOrg/TestAssembly";
		String actualNs = util.buildNsPath(orgName, assemblyName);
		Assert.assertEquals(expectedNs, actualNs);
	}

	@Test
	public void getnsForPlatformCiComponentsTest() {

		String expectedNsForPlatformCiComponents = "/TestOrg/TestAssembly/dev/bom/TestPlatform/1";
		String ns = util.buildNsPath(orgName, assemblyName);

		String actualNsForPlatformCiComponents = util.getnsForPlatformCiComponents(ns, platformName, OOPhases.OPERATE,
				envName);

		Assert.assertEquals(expectedNsForPlatformCiComponents, actualNsForPlatformCiComponents);
	}

}
