package se.homii.handlers;

import se.homii.ClientHelper;
import se.homii.models.request.Export;
import se.homii.models.request.Project;
import se.homii.models.response.ExportResponse;
import se.homii.models.response.ProjectResponse;

import javax.inject.Inject;

public class ProjectHandler {

  @Inject
  ClientHelper clientHelper;


  public ProjectResponse initializeProject(String projectName) {

    if (projectName == null) {
      projectName = "test123";
    }
    // here i make the project that holds all the clips
    Project project = Project.builder()
        .name(projectName)
        .json("{}")
        .build();

    ProjectResponse projectResponse = clientHelper.buildProjectClientWithHeader()
        .buildPost(clientHelper.buildEntity(project))
        .invoke()
        .readEntity(ProjectResponse.class);

    clientHelper.setProjectUrl(projectResponse.getUrl());

    return projectResponse;
  }

  public ExportResponse initializeProjectExport() {

    //TODO Should export settings be static in the model?
    Export export = Export.builder()
        .exportType("video")
        .videoFormat("mp4")
        .videoCodec("libx264")
        .videoBitrate(15000000)
        .audioCodec("libmp3lame")
        .audioBitrate(1920000)
        .startFrame(1)
        .endFrame(0)
        .project(clientHelper.getProjectUrl())
        .webhook("http://1.1.1.0/webhook")
        .status("pending")
        .json("{}")
        .build();

    // Here i export the project with the set settings for audio and video

    return clientHelper.buildClientWithHeader("exports/", null)
        .buildPost(clientHelper.buildEntity(export))
        .invoke()
        .readEntity(ExportResponse.class);
  }
}
