package temporalreality.launcher.wrapper;

import com.google.gson.*;
import repack.net.shadowfacts.shadowlib.util.InternetUtils;
import temporalreality.launcher.wrapper.dependency.Dependency;
import temporalreality.launcher.wrapper.dependency.MavenDependency;
import temporalreality.launcher.wrapper.repository.MavenRepository;
import temporalreality.launcher.wrapper.repository.Repository;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * @author shadowfacts
 */
public class DependencyManager {

	private static DependencyManager instance;

	private ArrayList<Repository> repos = new ArrayList<>();
	private ArrayList<Dependency> deps = new ArrayList<>();

	private DependencyManager() {

	}

	public void download() {
		for (Dependency dep : deps) {
			try {
				InternetUtils.downloadFile(dep.getDownloadURL(), System.getProperty("user.home") + "/.temporalreality/libs/" + dep.getFileName());
			} catch (IOException e) {
				System.err.println("There was a problem downloading the dependency " + dep.getFileName());
				e.printStackTrace();
			}
		}
	}

	public Repository getRepo(String name) {
		for (Repository repo : getRepos()) {
			if (repo.getName().equals(name)) {
				return repo;
			}
		}
		return null;
	}

	public ArrayList<Repository> getRepos() {
		return repos;
	}

	public ArrayList<Dependency> getDeps() {
		return deps;
	}

	public static DependencyManager load(File file) throws IOException {
		instance = Wrapper.gson.fromJson(new FileReader(file), DependencyManager.class);
		return instance;
	}

	public static DependencyManager getInstance() {
		return instance;
	}

	public static class Deserializer implements JsonDeserializer<DependencyManager> {

		@Override
		public DependencyManager deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
			DependencyManager manager = new DependencyManager();

			JsonObject obj = json.getAsJsonObject();

//			Repositories
			JsonArray repos = obj.get("repositories").getAsJsonArray();
			for (int i = 0; i < repos.size(); i++) {
				JsonObject repoObj = repos.get(i).getAsJsonObject();

				String type = repoObj.get("type").getAsString();
				if (type.equals("maven")) {

					String name = repoObj.get("name").getAsString();
					String url = repoObj.get("url").getAsString();

					MavenRepository repo = new MavenRepository(name, url);
					manager.getRepos().add(repo);

				} else {
					throw new JsonParseException("Invalid repository type " + type);
				}
			}

//			Dependencies
			JsonArray deps = obj.get("dependencies").getAsJsonArray();
			for (int i = 0; i < deps.size(); i++) {
				JsonObject depObj = deps.get(i).getAsJsonObject();

				String type = depObj.get("type").getAsString();
				if (type.equals("maven")) {

					String repoName = depObj.get("repo").getAsString();
					String group = depObj.get("group").getAsString();
					String name = depObj.get("name").getAsString();
					String version = depObj.get("version").getAsString();
					String classifier = depObj.get("classifier").getAsString();

					MavenDependency dep = new MavenDependency(group, name, version, classifier);
					dep.setRepo(manager.getRepo(repoName));

					manager.getDeps().add(dep);


				} else {
					throw new JsonParseException("Invalid dependency type " + type);
				}
			}

			return manager;
		}

	}


}
