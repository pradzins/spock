package org.spockframework.runtime;

import org.junit.platform.commons.util.ClassFilter;
import org.junit.platform.engine.support.discovery.EngineDiscoveryRequestResolver;
import spock.lang.Specification;

import java.lang.reflect.Modifier;
import java.util.Optional;

import org.junit.platform.engine.*;
import org.junit.platform.engine.discovery.ClassSelector;
import org.junit.platform.engine.support.hierarchical.HierarchicalTestEngine;

public class SpockEngine extends HierarchicalTestEngine<SpockExecutionContext> {

  @Override
  protected SpockExecutionContext createExecutionContext(ExecutionRequest request) {
    return new SpockExecutionContext();
  }

  @Override
  public String getId() {
    return "spock";
  }

  @Override
  public TestDescriptor discover(EngineDiscoveryRequest discoveryRequest, UniqueId uniqueId) {
    RunContext runContext = RunContext.get(); // TODO cleanup
    SpockEngineDescriptor engineDescriptor = new SpockEngineDescriptor(uniqueId, runContext);
    EngineDiscoveryRequestResolver.builder()
      .withDefaultsForClassBasedTestEngines(SpecUtil::isRunnableSpec)
      .addSelectorResolver(context -> new ClassSelectorResolver(context.getClassNameFilter(), runContext))
      .addSelectorResolver(new MethodSelectorResolver())
      .build()
      .resolve(discoveryRequest, engineDescriptor);
    return engineDescriptor;
  }

  @Override
  public Optional<String> getGroupId() {
    return Optional.of("org.spockframework");
  }

  @Override
  public Optional<String> getArtifactId() {
    return Optional.of("spock-core");
  }

  @Override
  public Optional<String> getVersion() {
    return Optional.empty(); // TODO later
  }
}
