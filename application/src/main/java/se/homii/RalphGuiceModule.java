package se.homii;

import com.google.inject.AbstractModule;
import se.homii.api.Ralph;

public class RalphGuiceModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(Ralph.class).to(RalphImpl.class);
  }
}
