package com.hackerrank.api.hackerrank.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.*;
import java.util.Date;
public class Result {
  /* Callback URL */
  private String callback_url = null;
  /* Censored compile message */
  private String censored_compile_message = null;
  /* Censored stderr */
  private List<String> censored_stderr = new ArrayList<String>();
  /* Codechecker hash */
  private String codechecker_hash = null;
  /* Compilemessage */
  private String compilemessage = null;
  /* Created at */
  private Date created_at = null;
  /* Custom score */
  private String custom_score = null;
  /* Custom status */
  private String custom_status = null;
  /* Diff status */
  private String diff_status = null;
  /* Hash */
  private String hash = null;
  /* Memory */
  private List<Integer> memory = new ArrayList<Integer>();
  /* Message */
  private List<String> message = new ArrayList<String>();
  /* Result */
  private Integer result = null;
  /* Server */
  private String server = null;
  /* Signal */
  private List<Integer> signal = new ArrayList<Integer>();
  /* Stderr */
  private List<Boolean> stderr = new ArrayList<Boolean>();
  /* Stdout */
  private List<String> stdout = new ArrayList<String>();
  /* Time */
  private List<Float> time = new ArrayList<Float>();
  @JsonProperty("callback_url")
  public String getCallbackUrl() {
    return callback_url;
  }
  public void setCallbackUrl(String callback_url) {
    this.callback_url = callback_url;
  }

  @JsonProperty("censored_compile_message")
  public String getCensoredCompileMessage() {
    return censored_compile_message;
  }
  public void setCensoredCompileMessage(String censored_compile_message) {
    this.censored_compile_message = censored_compile_message;
  }

  @JsonProperty("censored_stderr")
  public List<String> getCensoredStderr() {
    return censored_stderr;
  }
  public void setCensoredStderr(List<String> censored_stderr) {
    this.censored_stderr = censored_stderr;
  }

  @JsonProperty("codechecker_hash")
  public String getCodecheckerHash() {
    return codechecker_hash;
  }
  public void setCodecheckerHash(String codechecker_hash) {
    this.codechecker_hash = codechecker_hash;
  }

  @JsonProperty("compilemessage")
  public String getCompilemessage() {
    return compilemessage;
  }
  public void setCompilemessage(String compilemessage) {
    this.compilemessage = compilemessage;
  }

  @JsonProperty("created_at")
  public Date getCreatedAt() {
    return created_at;
  }
  public void setCreatedAt(Date created_at) {
    this.created_at = created_at;
  }

  @JsonProperty("custom_score")
  public String getCustomScore() {
    return custom_score;
  }
  public void setCustomScore(String custom_score) {
    this.custom_score = custom_score;
  }

  @JsonProperty("custom_status")
  public String getCustomStatus() {
    return custom_status;
  }
  public void setCustomStatus(String custom_status) {
    this.custom_status = custom_status;
  }

  @JsonProperty("diff_status")
  public String getDiffStatus() {
    return diff_status;
  }
  public void setDiffStatus(String diff_status) {
    this.diff_status = diff_status;
  }

  @JsonProperty("hash")
  public String getHash() {
    return hash;
  }
  public void setHash(String hash) {
    this.hash = hash;
  }

  @JsonProperty("memory")
  public List<Integer> getMemory() {
    return memory;
  }
  public void setMemory(List<Integer> memory) {
    this.memory = memory;
  }

  @JsonProperty("message")
  public List<String> getMessage() {
    return message;
  }
  public void setMessage(List<String> message) {
    this.message = message;
  }

  @JsonProperty("result")
  public Integer getResult() {
    return result;
  }
  public void setResult(Integer result) {
    this.result = result;
  }

  @JsonProperty("server")
  public String getServer() {
    return server;
  }
  public void setServer(String server) {
    this.server = server;
  }

  @JsonProperty("signal")
  public List<Integer> getSignal() {
    return signal;
  }
  public void setSignal(List<Integer> signal) {
    this.signal = signal;
  }

  @JsonProperty("stderr")
  public List<Boolean> getStderr() {
    return stderr;
  }
  public void setStderr(List<Boolean> stderr) {
    this.stderr = stderr;
  }

  @JsonProperty("stdout")
  public List<String> getStdout() {
    return stdout;
  }
  public void setStdout(List<String> stdout) {
    this.stdout = stdout;
  }

  @JsonProperty("time")
  public List<Float> getTime() {
    return time;
  }
  public void setTime(List<Float> time) {
    this.time = time;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class Result {\n");
    sb.append("  callback_url: ").append(callback_url).append("\n");
    sb.append("  censored_compile_message: ").append(censored_compile_message).append("\n");
    sb.append("  censored_stderr: ").append(censored_stderr).append("\n");
    sb.append("  codechecker_hash: ").append(codechecker_hash).append("\n");
    sb.append("  compilemessage: ").append(compilemessage).append("\n");
    sb.append("  created_at: ").append(created_at).append("\n");
    sb.append("  custom_score: ").append(custom_score).append("\n");
    sb.append("  custom_status: ").append(custom_status).append("\n");
    sb.append("  diff_status: ").append(diff_status).append("\n");
    sb.append("  hash: ").append(hash).append("\n");
    sb.append("  memory: ").append(memory).append("\n");
    sb.append("  message: ").append(message).append("\n");
    sb.append("  result: ").append(result).append("\n");
    sb.append("  server: ").append(server).append("\n");
    sb.append("  signal: ").append(signal).append("\n");
    sb.append("  stderr: ").append(stderr).append("\n");
    sb.append("  stdout: ").append(stdout).append("\n");
    sb.append("  time: ").append(time).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}

