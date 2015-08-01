package temporalreality.launcher.wrapper.dependency;

import temporalreality.launcher.wrapper.repository.Repository;

/**
 * @author shadowfacts
 */
public interface Dependency {

	void setRepo(Repository repo);

	Repository getRepo();

	String getDownloadURL();

	String getFileName();

}
