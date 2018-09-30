package com.walmartlabs.strati.migrationtools.oneops2k8migration.util;

/**
 * @author dsing17
 *
 */
public enum BomCiType {

  TOMCAT("tomcat"), ARTIFACT_APP("artifactApp");

  public String value;


  BomCiType(String ooPhase) {

    this.value = ooPhase;

  }


  public String getValue() {
    return value;
  }

  
}