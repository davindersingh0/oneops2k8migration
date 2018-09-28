package com.walmartlabs.strati.migrationtools.oneops2k8migration.util;

import org.springframework.stereotype.Component;

@Component
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