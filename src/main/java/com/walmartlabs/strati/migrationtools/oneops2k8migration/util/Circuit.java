package com.walmartlabs.strati.migrationtools.oneops2k8migration.util;

import org.springframework.stereotype.Component;

@Component
public enum Circuit {

  oneops("oneops"), walmartlabs("walmartlabs"), main("main");

  public String value;


  Circuit(String ooPhase) {

    this.value = ooPhase;

  }


  public String getValue() {
    return value;
  }



}
