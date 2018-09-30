package com.walmartlabs.strati.migrationtools.oneops2k8migration.controller;

import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.walmartlabs.strati.migrationtools.oneops2k8migration.service.CIAttributesService;
import com.walmartlabs.strati.migrationtools.oneops2k8migration.util.MigrationUtil;
import com.walmartlabs.strati.migrationtools.oneops2k8migration.util.Platform;

@RunWith(SpringRunner.class)
@WebMvcTest(value = CIAttributesSvcController.class, secure = false)
public class CIAttributesSvcControllerTest {

	private final Logger log = LoggerFactory.getLogger(getClass());

	@MockBean
	private CIAttributesService service;

	@MockBean
	private MigrationUtil util;

	@MockBean
	Platform platform;

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void getCiAttributesForTomcatMigrationTest() throws Exception {

		HashMap<String, String> bomTomcatCiAttributesMap = new HashMap<>();

		bomTomcatCiAttributesMap.put("tomcatAttribName1", "tomcatAttribValue1");
		bomTomcatCiAttributesMap.put("tomcatAttribName2", "tomcatAttribValue2");

		HashMap<String, String> bomArtifactCiAttributesMap = new HashMap<>();

		bomArtifactCiAttributesMap.put("ArtifactAppAttribName1", "ArtifactAppAttribValue1");
		bomArtifactCiAttributesMap.put("ArtifactAppAttribName2", "ArtifactAppAttribValue2");

		Map<String, String> platAttribsMapForTomcatAndArtifactCI = new HashMap<>();

		platAttribsMapForTomcatAndArtifactCI.putAll(bomTomcatCiAttributesMap);
		platAttribsMapForTomcatAndArtifactCI.putAll(bomArtifactCiAttributesMap);

		when(service.getPlatAttribsMapForTomcatAndArtifactCI(platform)).thenReturn(platAttribsMapForTomcatAndArtifactCI);

		String mapObjectToYaml = new MigrationUtil().yamlifyObject(platAttribsMapForTomcatAndArtifactCI);

		when(util.yamlifyObject(platAttribsMapForTomcatAndArtifactCI)).thenReturn(mapObjectToYaml);

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get("/org/TestOrg/assmebly/TestAssembly/platform/TestPlatform/env/dev")
				.accept(MediaType.TEXT_PLAIN_VALUE);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		Assert.assertEquals(mapObjectToYaml, result.getResponse().getContentAsString());

	}

}
