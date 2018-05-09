package se.homii.api;

import lombok.Data;

import java.util.List;

@Data
public class RenderResult {

  String videoUrl;
  List<String> bestFriends;

}
