package com.walmartlabs.strati.migrationtools.oneops2k8migration.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PreDestroy;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.walmartlabs.strati.migrationtools.oneops2k8migration.exception.UnSupportedOperation;

/**
 * @author dsing17
 *
 */
@Repository
public class KloopzCmDal {

	private final Logger log = LoggerFactory.getLogger(getClass());

	Connection conn;

	public KloopzCmDal(@Autowired DataSource dataSource) throws SQLException {
		super();
		//conn = dataSource.getConnection();
	}

/*	@PreDestroy
	public void cleanUp() throws SQLException {
		conn.close();
	}*/

	public HashMap<String, String> getCIAttrNameAndValuesMapByCiId(int ciId) {

		HashMap<String, String> cIAttrNameAndValuesMap = new HashMap<>();

		try {

			String SQL_SELECT_cm_ci_attributes = "SELECT ca.df_attribute_value, ca.dj_attribute_value, cla.attribute_name"
					+ " from cm_ci_attributes ca, md_class_attributes cla, cm_ci ci"
					+ " where ca.ci_id=ci.ci_id and ca.attribute_id=cla.attribute_id" + " and ci.ci_id=?; ";

			log.info("SQL_SELECT_cm_ci_attributes : " + SQL_SELECT_cm_ci_attributes);
			PreparedStatement preparedStatement = conn.prepareStatement(SQL_SELECT_cm_ci_attributes);
			preparedStatement.setInt(1, ciId);

			log.info("preparedStatement: " + preparedStatement);
			ResultSet resultSet = preparedStatement.executeQuery();

			int numberOfRecords = 0;
			while (resultSet.next()) {
				String attrName = resultSet.getString("attribute_name");
				String attrVal = resultSet.getString("dj_attribute_value");

				cIAttrNameAndValuesMap.put(attrName, attrVal);
				numberOfRecords++;

			}

			log.info("numberOfRecords: " + numberOfRecords);

			return cIAttrNameAndValuesMap;
		} catch (Exception e) {
			throw new UnSupportedOperation("Error while fetching records" + e.getMessage());
		}

	}

	public String getciAttrValueByCiIdAndAttrName(int platformCiId, String attributeName) {

		String SQL_SELECT_ciAttrValueByCiIdAndAttrName = "SELECT ca.df_attribute_value, ca.dj_attribute_value  "
				+ "from cm_ci_attributes ca, md_class_attributes cla, cm_ci ci " + " where ca.ci_id=ci.ci_id "
				+ " and ca.attribute_id=cla.attribute_id " + " and ci.ci_id=? and cla.attribute_name=? ";

		try {

			log.info("SQL_SELECT_ciAttrValueByCiIdAndAttrName : " + SQL_SELECT_ciAttrValueByCiIdAndAttrName);
			PreparedStatement preparedStatement = conn.prepareStatement(SQL_SELECT_ciAttrValueByCiIdAndAttrName);

			preparedStatement.setInt(1, platformCiId);
			preparedStatement.setString(2, attributeName);

			log.info("preparedStatement: " + preparedStatement);
			ResultSet result = preparedStatement.executeQuery();

			String attributeValue = null;

			int numberOfRecords = 0;
			while (result.next()) {

				String df_attribute_value = result.getString("df_attribute_value");
				String dj_attribute_value = result.getString("dj_attribute_value");

				attributeValue = df_attribute_value;

				log.info("ciId {}, attributeName {}, df_attribute_value {} , dj_attribute_value {}" + platformCiId,
						attributeName, df_attribute_value, dj_attribute_value);
				numberOfRecords++;

			}
			log.info("Number of CMSCI attribute values : {}", numberOfRecords);

			if (numberOfRecords > 1) {
				throw new UnSupportedOperation("numberOfRecords for ciId: " + platformCiId + ", attributeName: "
						+ attributeName + "not supported");
			}

			return attributeValue;

		} catch (Exception e) {
			throw new UnSupportedOperation("Error while fetching records" + e.getMessage());
		}

	}

	public Map<Integer, String> getCiIdsForNsClazzAndPlatformCiName(String ns, String clazz, String platformName) {

		Map<Integer, String> map = new HashMap<Integer, String>();
		// List<Integer> ciIds = new ArrayList<Integer>();

		try {

			String SQL_SELECT_PlatformCiIdsbyNsClazzAndPlatformName = "select " + "ci.ci_id as ciId, "
					+ "ci.ci_name as ciName," + "ci.class_id as ciClassId," + "cl.class_name as ciClassName,"
					+ "cl.impl as impl, " + "ci.ns_id as nsId, " + "ns.ns_path as nsPath, " + "ci.ci_goid as ciGoid, "
					+ "ci.comments, " + "ci.ci_state_id as ciStateId, " + "st.state_name as ciState, "
					+ "ci.last_applied_rfc_id as lastAppliedRfcId, " + "ci.created_by as createdBy, "
					+ "ci.updated_by as updatedBy, " + "ci.created, " + "ci.updated "
					+ "from cm_ci ci, md_classes cl, ns_namespaces ns, cm_ci_state st " + "where ns.ns_path = ? "
					+ "and cl.class_name = ? " + "and ci.class_id = cl.class_id " + "and ci.ns_id = ns.ns_id "
					+ "and ci.ci_state_id = st.ci_state_id and ci.ci_name=?;";

			log.info("SQL_SELECT_PlatformCiIdsbyNsClazzAndPlatformName : "
					+ SQL_SELECT_PlatformCiIdsbyNsClazzAndPlatformName);
			PreparedStatement preparedStatement = conn
					.prepareStatement(SQL_SELECT_PlatformCiIdsbyNsClazzAndPlatformName);
			preparedStatement.setString(1, ns);
			preparedStatement.setString(2, clazz);
			preparedStatement.setString(3, platformName);

			log.info("preparedStatement: " + preparedStatement);
			ResultSet resultSet = preparedStatement.executeQuery();

			int numberOfRecords = 0;
			while (resultSet.next()) {
				int ciId = resultSet.getInt("ciId");
				String ciName = resultSet.getString("ciName");
				map.put(ciId, ciName);

				numberOfRecords++;

			}

			if (map.size() != 1) {
				log.info("platformName {} does not exists for ns {} and with clazzName ", platformName, ns, clazz);
				throw new UnSupportedOperation("UnSupported Platform Name");
			}
			log.info("numberOfRecords: " + numberOfRecords);
		} catch (Exception e) {
			throw new UnSupportedOperation("Error while fetching records" + e.getMessage());
		}
		return map;

	}

	public Map<Integer, String> getCiIdsAndCiNameForNsAndClazzMap(String ns, String clazz) {

		Map<Integer, String> ciIdAndCiNameMap = new HashMap<Integer, String>();
		try {

			String SQL_SELECT_NakedCMSCIByNsAndClazz = "select " + "ci.ci_id as ciId, " + "ci.ci_name as ciName,"
					+ "ci.class_id as ciClassId," + "cl.class_name as ciClassName," + "cl.impl as impl, "
					+ "ci.ns_id as nsId, " + "ns.ns_path as nsPath, " + "ci.ci_goid as ciGoid, " + "ci.comments, "
					+ "ci.ci_state_id as ciStateId, " + "st.state_name as ciState, "
					+ "ci.last_applied_rfc_id as lastAppliedRfcId, " + "ci.created_by as createdBy, "
					+ "ci.updated_by as updatedBy, " + "ci.created, " + "ci.updated "
					+ "from cm_ci ci, md_classes cl, ns_namespaces ns, cm_ci_state st " + "where ns.ns_path = ? "
					+ "and cl.class_name = ? " + "and ci.class_id = cl.class_id " + "and ci.ns_id = ns.ns_id "
					+ "and ci.ci_state_id = st.ci_state_id;";

			log.info("SQL_SELECT_NakedCMSCIByNsAndClazz : " + SQL_SELECT_NakedCMSCIByNsAndClazz);
			PreparedStatement preparedStatement_SELECT_NakedCMSCIByNsAndClazz = conn
					.prepareStatement(SQL_SELECT_NakedCMSCIByNsAndClazz);
			preparedStatement_SELECT_NakedCMSCIByNsAndClazz.setString(1, ns);
			preparedStatement_SELECT_NakedCMSCIByNsAndClazz.setString(2, clazz);

			log.info("preparedStatement_SELECT_NakedCMSCIByNsAndClazz: "
					+ preparedStatement_SELECT_NakedCMSCIByNsAndClazz);
			ResultSet resultSet = preparedStatement_SELECT_NakedCMSCIByNsAndClazz.executeQuery();

			int numberOfRecords = 0;
			while (resultSet.next()) {
				ciIdAndCiNameMap.put(resultSet.getInt("ciId"), resultSet.getString("ciName"));
				numberOfRecords++;

			}

			log.info("numberOfRecords: " + numberOfRecords);
		} catch (Exception e) {
			throw new UnSupportedOperation("Error while fetching records" + e.getMessage());
		}
		return ciIdAndCiNameMap;
	}

}
