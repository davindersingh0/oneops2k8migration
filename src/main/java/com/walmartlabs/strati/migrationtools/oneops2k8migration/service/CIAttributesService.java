package com.walmartlabs.strati.migrationtools.oneops2k8migration.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.walmartlabs.strati.migrationtools.oneops2k8migration.dal.KloopzCmDal;
import com.walmartlabs.strati.migrationtools.oneops2k8migration.util.BomCiType;
import com.walmartlabs.strati.migrationtools.oneops2k8migration.util.BomClazzes;
import com.walmartlabs.strati.migrationtools.oneops2k8migration.util.Circuit;
import com.walmartlabs.strati.migrationtools.oneops2k8migration.util.CircuitconsolidationUtil;
import com.walmartlabs.strati.migrationtools.oneops2k8migration.util.MigrationUtil;
import com.walmartlabs.strati.migrationtools.oneops2k8migration.util.OOPhases;

@Service
public class CIAttributesService {

	private final Logger log = LoggerFactory.getLogger(getClass());

	KloopzCmDal dal;
	MigrationUtil util;

	public CIAttributesService(KloopzCmDal dal, MigrationUtil util) {
		this.dal = dal;
		this.util = util;
	}

	public Map<String, String> getCiAttributesByCiId(int ciId) {

		Map<String, String> ciAttrNameAndValuesMap = new HashMap<>();
		ciAttrNameAndValuesMap = dal.getCIAttrNameAndValuesMapByCiId(ciId);

		return ciAttrNameAndValuesMap;

	}

	public Map<String, String> getPlatAttribsMapForTomcatAndArtifactCI(String ns, String platformName, String envName) {
		Map<String, String> platAttribsMapForTomcatAndArtifactCI = new HashMap<>();

		Circuit circuit = getCircuit(ns, platformName);
		String nsForPlatformCiComponents = CircuitconsolidationUtil.getnsForPlatformCiComponents(ns, platformName,
				OOPhases.OPERATE, envName);

		Map<String, String> tomcatBomCiAttributesMap = getBomCiAttributes(circuit, BomCiType.TOMCAT,
				nsForPlatformCiComponents);
		Map<String, String> artifactBomCiAttributesMap = getBomCiAttributes(circuit, BomCiType.ARTIFACT_APP,
				nsForPlatformCiComponents);

		platAttribsMapForTomcatAndArtifactCI.putAll(tomcatBomCiAttributesMap);
		platAttribsMapForTomcatAndArtifactCI.putAll(artifactBomCiAttributesMap);

		return platAttribsMapForTomcatAndArtifactCI;
	}

	private Map<String, String> getBomCiAttributes(Circuit circuit, BomCiType bomCiType,
			String nsForPlatformCiComponents) {
		BomClazzes bomCiClazz = getBomCiClazzForBomCiType(circuit, bomCiType);
		int bomCiId = getCiIdForBomCI(nsForPlatformCiComponents, bomCiClazz);

		Map<String, String> bomCiAttributesMap = getCiAttributesByCiId(bomCiId);
		return bomCiAttributesMap;
	}

	private BomClazzes getBomCiClazzForBomCiType(Circuit circuit, BomCiType bomCiType) {

		switch (circuit) {
		case main:

			switch (bomCiType) {
			case TOMCAT:
				return BomClazzes.main_tomcat_bom_clazz;

			case ARTIFACT_APP:
				return BomClazzes.main_artifact_bom_clazz;

			default:
				log.error("UnSupported bomCiType " + bomCiType);
				throw new RuntimeException("UnSupported bomCiType " + bomCiType);

			}

		case walmartlabs:

			switch (bomCiType) {
			case TOMCAT:
				return BomClazzes.walmartlabs_tomcat_bom_clazz;

			case ARTIFACT_APP:
				return BomClazzes.walmartlabs_artifact_bom_clazz;

			default:
				log.error("UnSupported bomCiType " + bomCiType);
				throw new RuntimeException("UnSupported bomCiType " + bomCiType);

			}

		case oneops:

			switch (bomCiType) {
			case TOMCAT:
				return BomClazzes.oneops_tomcat_bom_clazz;
			case ARTIFACT_APP:
				return BomClazzes.oneops_artifact_bom_clazz;

			default:
				log.error("UnSupported bomCiType " + bomCiType);
				throw new RuntimeException("UnSupported bomCiType " + bomCiType);

			}

		default:
			log.error("UnSupported bomCiType " + bomCiType);
			throw new RuntimeException("UnSupported bomCiType " + bomCiType);
		}

	}

	private Circuit getCircuit(String ns, String platformName) {
		String platformClazz = "catalog.Platform";
		Map<Integer, String> platformCiIdAndNameMap = dal.getCiIdsForNsClazzAndPlatformCiName(ns, platformClazz,
				platformName);
		Circuit circuit = null;
		for (int platformCiId : platformCiIdAndNameMap.keySet()) {

			String df_attribute_value = dal.getciAttrValueByCiIdAndAttrName(platformCiId, "source");

			circuit = Circuit.valueOf(df_attribute_value);
			return circuit;

		}

		return circuit;
	}

	private int getCiIdForBomCI(String nsForPlatformCiComponents, BomClazzes bomCiClazz) {
		List<Integer> ciIdsListForBomCIClazz = getCiIdsListForBomCIClazz(nsForPlatformCiComponents,
				bomCiClazz.getValue());

		try {
			return ciIdsListForBomCIClazz.get(0);
		} catch (ArrayIndexOutOfBoundsException e) {

			log.error("bomCiClazz: {} must have atleast 1 ciId", bomCiClazz);
			log.error("Message :", e.getMessage());
			throw new RuntimeException("bomCiClazz: " + bomCiClazz + "must have atleast 1 ciId");

		}

	}

	private List<Integer> getCiIdsListForBomCIClazz(String nsForPlatformCiComponents, String bomCiClazz) {

		Map<Integer, String> ciIdAndCiNamesMap = dal.getCiIdsAndCiNameForNsAndClazzMap(nsForPlatformCiComponents,
				bomCiClazz);

		return new ArrayList<Integer>(ciIdAndCiNamesMap.keySet());
	}

}
