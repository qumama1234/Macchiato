package com.hackerrank.api.hackerrank.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.hackerrank.api.hackerrank.model.Languages;
public class LanguageResponse {
  /* Languages */
  private Languages languages = null;
  @JsonProperty("languages")
  public Languages getLanguages() {
    return languages;
  }
  public void setLanguages(Languages languages) {
    this.languages = languages;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class LanguageResponse {\n");
    sb.append("  languages: ").append(languages).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}

