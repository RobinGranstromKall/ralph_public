package se.homii;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import ru.vyarus.dropwizard.guice.GuiceBundle;
import se.homii.api.Ralph;
import se.homii.api.SocialMediaService;
import se.homii.image.ImageGuiceModule;
import se.homii.texttospeech.WatsonGuiceModule;
import se.homii.texttospeech.exceptions.TextToSpeechException;
import se.homii.google.GoogleGuiceModule;

import java.io.IOException;

public class RalphApplication extends Application<BootstrapConfiguration> {

  public static void main(String[] args)
      throws Exception {

    new RalphApplication().run(args);
  }

  @Override
  public void initialize(Bootstrap<BootstrapConfiguration> bootstrap) {

    bootstrap.addBundle(GuiceBundle.builder()
        .enableAutoConfig(getClass().getPackage().getName())
        .modules(instantiateGuiceModules())
        .build());
  }

  @Override
  public void run(BootstrapConfiguration config,
                  Environment environment) {

    Injector injector = Guice.createInjector(instantiateGuiceModules());
    Ralph ralph = injector.getInstance(
        Ralph.class);

    try {
      ralph.generateTest(
          "userId",
          "accessToken");
    } catch (TextToSpeechException | IOException e) {
      e.printStackTrace();
    }
  }

  private Module[] instantiateGuiceModules() {

    return new Module[]{
        new BootstrapGuiceModule(),
        new SocialMediaGuiceModule(),
        new OpenshotGuiceModule(),
        new BootstrapGuiceModule(),
        new ImageGuiceModule(),
        new S3ServiceGuiceModule(),
        new RalphGuiceModule(),
        new WatsonGuiceModule(),
        new GoogleGuiceModule(),
    };
  }
}
