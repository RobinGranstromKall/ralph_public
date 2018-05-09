package se.homii;

import com.google.inject.AbstractModule;
import org.glassfish.jersey.client.JerseyClient;
import org.glassfish.jersey.client.JerseyClientBuilder;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

public class BootstrapGuiceModule extends AbstractModule {

  @Override
  protected void configure() {

    bind(Client.class).to(JerseyClient.class);
    bind(ClientBuilder.class).to(JerseyClientBuilder.class);

  }
}
