package temporalreality.launcher.wrapper;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.jboss.shrinkwrap.resolver.api.maven.ConfigurableMavenResolverSystem;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import repack.net.shadowfacts.shadowlib.util.InternetUtils;
import temporalreality.launcher.wrapper.log.LogWindowController;
import temporalreality.launcher.wrapper.util.StreamRedirect;

import java.io.IOException;
import java.net.URL;

/**
 * @author shadowfacts
 */
public class Wrapper extends Application {

	private static Wrapper instance;

	private Stage primaryStage;
	private Process launcherProcess;


	public Wrapper() {
		instance = this;
	}

	@Override
	public void start(Stage stage) throws Exception {
		primaryStage = stage;

		if (!System.getProperty("java.version").startsWith("1.8")) {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Wrapper.class.getResource("warning/JavaWarning.fxml"));

			AnchorPane pane = (AnchorPane)loader.load();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Invalid Java Version");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(pane);
			dialogStage.setScene(scene);

			dialogStage.showAndWait();

			System.exit(1); // Invalid Java Version
		}

		LogWindowController controller = showLogWindow();

		launcherProcess = new ProcessBuilder(getLaunchCommand().split(" ")).start();

		StreamRedirect output = new StreamRedirect(launcherProcess.getInputStream());
		StreamRedirect error = new StreamRedirect(launcherProcess.getErrorStream());
		output.start();
		error.start();

		Thread exitThread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					final int exitCode = launcherProcess.waitFor();
					System.exit(exitCode);
				} catch (Exception e) {
					System.err.println("There was a problem while waiting for the launcher process to exit!");
					e.printStackTrace();
				}
			}
		});
		exitThread.start();

		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent windowEvent) {
				launcherProcess.destroy();
			}
		});
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				launcherProcess.destroy();
			}
		});
	}

	public LogWindowController showLogWindow() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Wrapper.class.getResource("log/LogWindow.fxml"));

		AnchorPane pane = (AnchorPane)loader.load();

		Stage dialogStage = new Stage();
		dialogStage.setTitle("Temporal Reality Launcher");
		dialogStage.initModality(Modality.NONE);
		dialogStage.initOwner(primaryStage);
		Scene scene = new Scene(pane);
		dialogStage.setScene(scene);

		LogWindowController controller = loader.getController();

		controller.setDialogStage(dialogStage);

		String prop = System.getProperty("temporalreality.launcher.wrapper.log");
		prop = prop == null ? "true" : prop;
		if (!prop.equals("false")) {
			System.setOut(controller.getPrintStream());
			System.setErr(controller.getPrintStream());
		}

		dialogStage.show();

		return controller;

	}

	private static String getLaunchCommand() throws Exception {
		String latest = InternetUtils.getResourceAsString("https://raw.githubusercontent.com/TemporalReality/Launcher/master/latest.txt").split("\n")[0];

		ConfigurableMavenResolverSystem resolver = Maven.configureResolver()
				.withRemoteRepo("shadowfacts", "http://mvn.rx14.co.uk/shadowfacts/", "default")
				.withRemoteRepo("jcenter", "http://jcenter.bintray.com/", "default");


		URL[] libs = resolver.resolve(latest).withTransitivity().as(URL.class);

		char pathSeparator;
		if (System.getProperty("os.name").startsWith("Win")) pathSeparator = ';';
		else pathSeparator = ':';

		StringBuilder builder = new StringBuilder();
		builder.append("java -classpath ");

		for (int i = 0; i < libs.length; i++) {
			builder.append(libs[i].getPath());

			if (i != libs.length - 1) builder.append(pathSeparator);
		}

		builder.append(" temporalreality.launcher.TRLauncher");

		return builder.toString();
	}

	public Process getLauncherProcess() {
		return launcherProcess;
	}

	public static Wrapper getInstance() {
		return instance;
	}

	public static void main(String[] args) throws Exception {
		launch(args);
	}

}
