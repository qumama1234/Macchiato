package com.hackerrank.api.hackerrank.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Names {
  /* C */
  private String c = null;
  /* Clisp */
  private String clisp = null;
  /* Clojure */
  private String clojure = null;
  /* Cpp */
  private String cpp = null;
  /* Csharp */
  private String csharp = null;
  /* Erlang */
  private String erlang = null;
  /* Go */
  private String go = null;
  /* Haskell */
  private String haskell = null;
  /* Java */
  private String java = null;
  /* Lua */
  private String lua = null;
  /* Perl */
  private String perl = null;
  /* Php */
  private String php = null;
  /* Python */
  private String python = null;
  /* Ruby */
  private String ruby = null;
  /* Scala */
  private String scala = null;
  @JsonProperty("c")
  public String getC() {
    return c;
  }
  public void setC(String c) {
    this.c = c;
  }

  @JsonProperty("clisp")
  public String getClisp() {
    return clisp;
  }
  public void setClisp(String clisp) {
    this.clisp = clisp;
  }

  @JsonProperty("clojure")
  public String getClojure() {
    return clojure;
  }
  public void setClojure(String clojure) {
    this.clojure = clojure;
  }

  @JsonProperty("cpp")
  public String getCpp() {
    return cpp;
  }
  public void setCpp(String cpp) {
    this.cpp = cpp;
  }

  @JsonProperty("csharp")
  public String getCsharp() {
    return csharp;
  }
  public void setCsharp(String csharp) {
    this.csharp = csharp;
  }

  @JsonProperty("erlang")
  public String getErlang() {
    return erlang;
  }
  public void setErlang(String erlang) {
    this.erlang = erlang;
  }

  @JsonProperty("go")
  public String getGo() {
    return go;
  }
  public void setGo(String go) {
    this.go = go;
  }

  @JsonProperty("haskell")
  public String getHaskell() {
    return haskell;
  }
  public void setHaskell(String haskell) {
    this.haskell = haskell;
  }

  @JsonProperty("java")
  public String getJava() {
    return java;
  }
  public void setJava(String java) {
    this.java = java;
  }

  @JsonProperty("lua")
  public String getLua() {
    return lua;
  }
  public void setLua(String lua) {
    this.lua = lua;
  }

  @JsonProperty("perl")
  public String getPerl() {
    return perl;
  }
  public void setPerl(String perl) {
    this.perl = perl;
  }

  @JsonProperty("php")
  public String getPhp() {
    return php;
  }
  public void setPhp(String php) {
    this.php = php;
  }

  @JsonProperty("python")
  public String getPython() {
    return python;
  }
  public void setPython(String python) {
    this.python = python;
  }

  @JsonProperty("ruby")
  public String getRuby() {
    return ruby;
  }
  public void setRuby(String ruby) {
    this.ruby = ruby;
  }

  @JsonProperty("scala")
  public String getScala() {
    return scala;
  }
  public void setScala(String scala) {
    this.scala = scala;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class Names {\n");
    sb.append("  c: ").append(c).append("\n");
    sb.append("  clisp: ").append(clisp).append("\n");
    sb.append("  clojure: ").append(clojure).append("\n");
    sb.append("  cpp: ").append(cpp).append("\n");
    sb.append("  csharp: ").append(csharp).append("\n");
    sb.append("  erlang: ").append(erlang).append("\n");
    sb.append("  go: ").append(go).append("\n");
    sb.append("  haskell: ").append(haskell).append("\n");
    sb.append("  java: ").append(java).append("\n");
    sb.append("  lua: ").append(lua).append("\n");
    sb.append("  perl: ").append(perl).append("\n");
    sb.append("  php: ").append(php).append("\n");
    sb.append("  python: ").append(python).append("\n");
    sb.append("  ruby: ").append(ruby).append("\n");
    sb.append("  scala: ").append(scala).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}

