package temporalreality.launcher.wrapper.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author shadowfacts
 */
public class StreamRedirect extends Thread {

	private InputStream in;

	public StreamRedirect(InputStream in) {
		this.in = in;
	}

	@Override
	public void run() {
		try {
			InputStreamReader streamReader = new InputStreamReader(in);
			BufferedReader reader = new BufferedReader(streamReader);
			String line;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
