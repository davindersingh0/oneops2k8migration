package com.walmartlabs.strati.migrationtools.oneops2k8migration.exception;

import org.springframework.stereotype.Component;
/**
 * @author dsing17
 *
 */
@Component
public final class UnSupportedOperation extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public UnSupportedOperation() {
    super();
  }

  public UnSupportedOperation(Exception e) {
    super(e);
  }

  public UnSupportedOperation(String message) {
    super(message);
  }

  public UnSupportedOperation(String message, Exception e) {
    super(message, e);
  }

}