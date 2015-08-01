package temporalreality.launcher.wrapper.repository;

/**
 * @author shadowfacts
 */
public class MavenRepository implements Repository {

	private String name;
	private String url;

	public MavenRepository(String name, String url) {
		this.name = name;
		this.url = url;
	}

	@Override
	public String getName() {
		return name;
	}

	public String getURL() {
		return url;
	}

	public String getDownloadURL(String group, String name, String version, String classifier) {
		StringBuilder builder = new StringBuilder();
		builder.append(url);
		builder.append("/" + group.replace('.', '/'));
		builder.append("/" + name);
		builder.append("/" + version);
		builder.append("/" + name + "-" + version);
		if (classifier != null && !classifier.equals("")) builder.append("-" + classifier);
		builder.append(".jar");
		return builder.toString();
	}
}
