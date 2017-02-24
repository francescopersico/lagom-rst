/*
 * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com>
 */
package cz.codingmonkeys.lagom.hello.impl;

import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;
import cz.codingmonkeys.lagom.hello.api.HelloService;

/**
 * The module that binds the HelloService so that it can be served.
 */
public class HelloModule extends AbstractModule implements ServiceGuiceSupport {
  @Override
  protected void configure() {
    bindServices(serviceBinding(HelloService.class, HelloServiceImpl.class));
  }
}
