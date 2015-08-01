package temporalreality.launcher.wrapper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import repack.net.shadowfacts.shadowlib.util.InternetUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;

/**
 * @author shadowfacts
 */
public class Wrapper {

	public static Gson gson;

	public static void main(String[] args) throws IOException {
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(DependencyManager.class, new DependencyManager.Deserializer());
		gson = builder.create();


		File latestDepUrlFile = new File(System.getProperty("user.home") + "/.temporalreality/latest.txt");
		if (!latestDepUrlFile.getParentFile().exists()) latestDepUrlFile.getParentFile().mkdirs();

		String storedDepUrl = "";
		try {
			storedDepUrl = new String(Files.readAllBytes(latestDepUrlFile.toPath()), Charset.defaultCharset());
		} catch (IOException e) {
			System.err.println("There was a problem reading the dependencies url from the file.");
			e.printStackTrace();
		}

		String latestDepUrl = "";
		try {
			latestDepUrl = InternetUtils.getResourceAsString("https://raw.githubusercontent.com/TemporalReality/Launcher/master/version.txt");
		} catch (IOException e) {
			System.err.println("There was a problem getting the latest dependencies url from GitHub");
			e.printStackTrace();
		}

		File libsFolder = new File(System.getProperty("user.home") + "/.temporalreality/libs/");

		if (!libsFolder.exists()) libsFolder.mkdirs();

		File dependencies = new File(System.getProperty("user.home") + "/.temporalreality/versions.json");
		if (!storedDepUrl.equals(latestDepUrl)) {
			try {

				InternetUtils.downloadFile(latestDepUrl, dependencies);
				DependencyManager.load(dependencies);

				for (File f : libsFolder.listFiles()) if (!f.isDirectory()) f.delete();

				DependencyManager.getInstance().download();

			} catch (IOException e) {
				System.err.println("There was a problem downloading the dependencies specification");
				e.printStackTrace();
			}
		}








//		String depUrl = "";
//		try {
//			depUrl = InternetUtils.getResourceAsString("https://raw.githubusercontent.com/TemporalReality/Launcher/master/version.txt");
//		} catch (IOException e) {
//			System.err.println("There was a problem getting the latest version.txt");
//		}
//
//		File getDeps = new File(System.getProperty("user.home") + "/.temporalreality/versions/latest.json");
//		if (!getDeps.getParentFile().exists()) {
//			getDeps.getParentFile().mkdirs();
//		}
//		if (!getDeps.exists()) {
//			try {
//				InternetUtils.downloadFile(depUrl, );
//			}
//		}

//		File getDeps = new File(System.getProperty("user.home") + "/.temporalreality/versions/v0.1.0-getDeps.json");
//		if (!getDeps.exists()) {
//			try {
//				InternetUtils.downloadFile("https://raw.githubusercontent.com/TemporalReality/Launcher/v0.1.0/dependencies.json", getDeps);
//			} catch (IOException e) {
//				System.err.println("There was a problem downloading the getDeps specification");
//				e.printStackTrace();
//			}
//		}

//		if (new Fil)
//
//		try {
//			DependencyManager.load(InternetUtils.getResourceAsString("https://raw.githubusercontent.com/TemporalReality/Launcher/v0.1.0/dependencies.json"))
//		} catch (IOException e) {
//			System.err.println("There was a problem downloading");
//		}
	}

}
