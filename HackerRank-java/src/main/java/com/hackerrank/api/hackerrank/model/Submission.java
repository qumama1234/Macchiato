package com.hackerrank.api.hackerrank.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.hackerrank.api.hackerrank.model.Result;
public class Submission {
  /* Result */
  private Result result = null;
  @JsonProperty("result")
  public Result getResult() {
    return result;
  }
  public void setResult(Result result) {
    this.result = result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class Submission {\n");
    sb.append("  result: ").append(result).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}

