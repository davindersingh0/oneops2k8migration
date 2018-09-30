package com.walmartlabs.strati.migrationtools.oneops2k8migration.service;

import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.walmartlabs.strati.migrationtools.oneops2k8migration.Oneops2k8migrationApplicationTests;
import com.walmartlabs.strati.migrationtools.oneops2k8migration.dal.KloopzCmDal;
import com.walmartlabs.strati.migrationtools.oneops2k8migration.util.BomClazzes;
import com.walmartlabs.strati.migrationtools.oneops2k8migration.util.Circuit;
import com.walmartlabs.strati.migrationtools.oneops2k8migration.util.MigrationUtil;
import com.walmartlabs.strati.migrationtools.oneops2k8migration.util.OOPhases;
import com.walmartlabs.strati.migrationtools.oneops2k8migration.util.Platform;

/**
 * @author dsing17
 *
 */
public class CIAttributesServiceTest2 extends Oneops2k8migrationApplicationTests {

	private final Logger log = LoggerFactory.getLogger(getClass());
	
	@MockBean
	KloopzCmDal dal;
	
	@InjectMocks
	private CIAttributesService service;

	@Autowired
	MigrationUtil util;



	@Autowired
	private Platform platform;

	private String orgName = "TestOrg2";
	private String assemblyName = "TestAssembly";
	private String platformName = "tomcat3";
	private String envName = "dev";

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		platform.setOrgName(orgName);
		platform.setAssemblyName(assemblyName);
		platform.setPlatformName(platformName);
		platform.setEnvName(envName);

		String ns = util.buildNsPath(orgName, assemblyName);
		platform.setNs(ns);
		platform.setNsForPlatformCiComponents(
				util.getnsForPlatformCiComponents(ns, platformName, OOPhases.OPERATE, envName));

	}

	@Test
	public void getPlatAttribsMapForTomcatAndArtifactCITest() {

		int platformCiId = 1001;
		Map<Integer, String> platformCiIdAndCiNameMap = new HashMap<Integer, String>();
		platformCiIdAndCiNameMap.put(platformCiId, platformName);

		Map<Integer, String> tomcatCiIdAndCiNameMap = new HashMap<Integer, String>();
		tomcatCiIdAndCiNameMap.put(2001, "TestTomcatComponent-3333-1");
		tomcatCiIdAndCiNameMap.put(2002, "TestTomcatComponent-3333-2");

		Map<Integer, String> artifactCiIdAndCiNameMap = new HashMap<Integer, String>();

		artifactCiIdAndCiNameMap.put(3001, "TestArtifactComponent-3333-1");
		artifactCiIdAndCiNameMap.put(3002, "TestArtifactComponent-3333-2");

		String platformDesignPhaseClazzClazz = "catalog.Platform";
		when(dal.getCiIdsForNsClazzAndPlatformCiName(platform.getNs(), platformDesignPhaseClazzClazz,
				platform.getPlatformName())).thenReturn(platformCiIdAndCiNameMap);

		when(dal.getciAttrValueByCiIdAndAttrName(platformCiId, "source")).thenReturn(Circuit.oneops.getValue());

		when(dal.getCiIdsAndCiNameForNsAndClazzMap(platform.getNsForPlatformCiComponents(),
				BomClazzes.oneops_tomcat_bom_clazz.getValue())).thenReturn(tomcatCiIdAndCiNameMap);

		when(dal.getCiIdsAndCiNameForNsAndClazzMap(platform.getNsForPlatformCiComponents(),
				BomClazzes.oneops_artifact_bom_clazz.getValue())).thenReturn(artifactCiIdAndCiNameMap);

		HashMap<String, String> bomTomcatCiAttributesMap = new HashMap<>();

		bomTomcatCiAttributesMap.put("tomcatAttribName1", "tomcatAttribValue1");
		bomTomcatCiAttributesMap.put("tomcatAttribName2", "tomcatAttribValue2");

		when(dal.getCIAttrNameAndValuesMapByCiId(2001)).thenReturn(bomTomcatCiAttributesMap);
		when(dal.getCIAttrNameAndValuesMapByCiId(2002)).thenReturn(bomTomcatCiAttributesMap);

		HashMap<String, String> bomArtifactCiAttributesMap = new HashMap<>();

		bomArtifactCiAttributesMap.put("ArtifactAppAttribName1", "ArtifactAppAttribValue1");
		bomArtifactCiAttributesMap.put("ArtifactAppAttribName2", "ArtifactAppAttribValue2");

		when(dal.getCIAttrNameAndValuesMapByCiId(3001)).thenReturn(bomArtifactCiAttributesMap);
		when(dal.getCIAttrNameAndValuesMapByCiId(3002)).thenReturn(bomArtifactCiAttributesMap);

		Map<String, String> expectedPlatAttribsMapForTomcatAndArtifactCIMap = new HashMap<>();
		expectedPlatAttribsMapForTomcatAndArtifactCIMap.putAll(bomTomcatCiAttributesMap);
		expectedPlatAttribsMapForTomcatAndArtifactCIMap.putAll(bomArtifactCiAttributesMap);

		Map<String, String> actualPlatAttribsMapForTomcatAndArtifactCIMap = service
				.getPlatAttribsMapForTomcatAndArtifactCI(platform);

		Assert.assertEquals(expectedPlatAttribsMapForTomcatAndArtifactCIMap,
				actualPlatAttribsMapForTomcatAndArtifactCIMap);

	}

}