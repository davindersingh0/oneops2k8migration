package com.walmartlabs.strati.migrationtools.oneops2k8migration.util;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

@Component
public class MigrationUtil {

  
  public String yamlifyObject(Map<String, String> map) {

    try {

      DumperOptions options = new DumperOptions();
      options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
      options.setPrettyFlow(true);

      Yaml yaml = new Yaml(options);
      String yamlifiedObject = yaml.dump(map);

      return yamlifiedObject;
    } catch (Exception e) {
      throw new RuntimeException("error while yamlifying object :" + map);
    }



  }


  
}
