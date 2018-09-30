package com.walmartlabs.strati.migrationtools.oneops2k8migration.util;

public enum BomClazzes {

  oneops_tomcat_bom_clazz("bom.oneops.1.Tomcat"), 
  oneops_artifact_bom_clazz("bom.oneops.1.Artifact"), 
  main_tomcat_bom_clazz("bom.Tomcat"), 
  main_artifact_bom_clazz("bom.Artifact"), 
  walmartlabs_tomcat_bom_clazz("bom.Tomcat"), 
  walmartlabs_artifact_bom_clazz("bom.Artifact");

  public String value;

  BomClazzes(String ooPhase) {

    this.value = ooPhase;

  }


  public String getValue() {
    return value;
  }



}
