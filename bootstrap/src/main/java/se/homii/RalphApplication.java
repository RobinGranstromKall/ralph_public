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
          "10202404586340829",
          "EAACEdEose0cBAL6To1LNyzQWNwCoEzaT944LK9blyTPZBh8coRcO4ltmbm1bDHjSBk5utXlejpkNxGaZBhxOiUaa2bfPzK2X73EuBBKRCyIcVOd45EWtJFysTut0TAZAwqgWYnyNneA44nxScnSrPamGeXQJ1NALvfknxUZBlTdMSLZAUtr6WhzqzZCuaOVTwZD");
    } catch (TextToSpeechException | IOException e) {
      e.printStackTrace();
    }

  //  SocialMediaService socialMediaService = injector.getInstance(
  //      SocialMediaService.class);
  //  socialMediaService.getFeedWithTopThreeFriends(
  //      "10204762531800355",
  //      "EAACEdEose0cBALAAikq1g1M08PliHlLaz44k6TJ5mTpd6oO9ZC5U3T22BpCSIA8GGECzx6cM34aB7I7NJbrPvmL3GHH1XnmQVKSXDsnOojZAaDRSj3GHC99ose3It66aySBzmzaOCsJ3dLEDcXMU6xx0G4Y7ZB62nlSoMRa9exgKfs9EY1uzMZAxkiYuiVlKCg9W0rKcTwZDZD");
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
