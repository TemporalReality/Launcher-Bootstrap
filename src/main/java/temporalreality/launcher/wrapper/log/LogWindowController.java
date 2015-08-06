package temporalreality.launcher.wrapper.log;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import repack.net.shadowfacts.shadowlib.util.DesktopUtils;
import temporalreality.launcher.wrapper.Wrapper;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * @author shadowfacts
 */
public class LogWindowController {

	private Stage dialogStage;

	private TextAreaPrintStream printStream;

	@FXML
	private TextArea textArea;



	public LogWindowController() {

	}

	@FXML
	private void initialize() {
		printStream = new TextAreaPrintStream(textArea);
	}

	@FXML
	private void temporalRealityPressed() throws Exception {
		DesktopUtils.openWebpage("http://temporal-reality.com");
	}

	@FXML
	private void forumPressed() throws Exception {
		DesktopUtils.openWebpage("http://forum.temporal-reality.com");
	}

	@FXML
	private void wikiPressed() throws Exception {
		DesktopUtils.openWebpage("http://wiki.temporal-reality.com");
	}

	@FXML
	private void twitterPressed() throws Exception {
		DesktopUtils.openWebpage("https://twitter.com/TemporalReality");
	}

	@FXML
	private void redditPressed() throws Exception {
		DesktopUtils.openWebpage("https://reddit.com/r/TemporalReality");
	}

	@FXML
	private void killPressed() {
		Wrapper.getInstance().getLauncherProcess().destroy();
		System.exit(0);
	}

	public Stage getDialogStage() {
		return dialogStage;
	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	public TextAreaPrintStream getPrintStream() {
		return printStream;
	}




	public class TextAreaPrintStream extends PrintStream {

		public TextAreaPrintStream(TextArea textArea) {
			super(new TextAreaOutputStream(textArea));
		}

	}

	public class TextAreaOutputStream extends OutputStream {

		private TextArea textArea;

		public TextAreaOutputStream(TextArea textArea) {
			this.textArea = textArea;
		}

		@Override
		public void write(final int b) throws IOException {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					textArea.appendText(String.valueOf((char)b));
				}
			});
		}
	}
}
