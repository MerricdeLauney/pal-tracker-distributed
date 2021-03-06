package io.pivotal.pal.tracker.timesheets;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.springframework.web.client.RestOperations;

public class ProjectClient {

  private final RestOperations restOperations;
  private final String endpoint;
  private final ConcurrentMap<Long, ProjectInfo> projects = new ConcurrentHashMap();

  public ProjectClient(RestOperations restOperations, String registrationServerEndpoint) {
    this.restOperations = restOperations;
    this.endpoint = registrationServerEndpoint;
  }

  @HystrixCommand(fallbackMethod = "getProjectFromCache")
  public ProjectInfo getProject(long projectId) {
    projects.put(
        projectId,
        restOperations.getForObject(endpoint + "/projects/" + projectId, ProjectInfo.class));
    return projects.get(projectId);
  }

  public ProjectInfo getProjectFromCache(long projctId) {
    return projects.get(projctId);
  }
}
