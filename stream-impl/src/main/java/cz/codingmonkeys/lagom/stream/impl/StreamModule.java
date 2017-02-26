/*
 * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com>
 */
package cz.codingmonkeys.lagom.stream.impl;

import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;
import cz.codingmonkey.lagom.stream.api.StreamService;
import cz.codingmonkeys.lagom.hello.api.HelloService;

/**
 * The module that binds the StreamService so that it can be served.
 */
public class StreamModule extends AbstractModule implements ServiceGuiceSupport {
  @Override
  protected void configure() {
    // Bind the StreamService service
    bindServices(serviceBinding(StreamService.class, StreamServiceImpl.class));
    // Bind the HelloService client
    bindClient(HelloService.class);
  }
}
