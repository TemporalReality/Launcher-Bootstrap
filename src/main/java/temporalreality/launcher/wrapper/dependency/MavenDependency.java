package temporalreality.launcher.wrapper.dependency;

import temporalreality.launcher.wrapper.repository.MavenRepository;
import temporalreality.launcher.wrapper.repository.Repository;

/**
 * @author shadowfacts
 */
public class MavenDependency implements Dependency {

	private MavenRepository repo;
	private String group;
	private String name;
	private String version;
	private String classifier;

	public MavenDependency(String group, String name, String version, String classifier) {
		this.group = group;
		this.name = name;
		this.version = version;
		this.classifier = classifier;
	}

	@Override
	public void setRepo(Repository repo) {
		if (repo instanceof MavenRepository) {
			this.repo = (MavenRepository)repo;
		} else {
			throw new RuntimeException("Maven dependencies can only have Maven repositories");
		}
	}

	@Override
	public Repository getRepo() {
		return repo;
	}

	@Override
	public String getDownloadURL() {
		return repo.getDownloadURL(group, name, version, classifier);
	}

	@Override
	public String getFileName() {
		StringBuilder builder = new StringBuilder();
		builder.append(name);
		builder.append("-" + version);
		if (classifier != null && !classifier.equals("")) builder.append("-" + classifier);
		builder.append(".jar");
		return builder.toString();
	}
}
