package com.hackerrank.api.hackerrank.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Codes {
  /* Bash */
  private Integer bash = null;
  /* C */
  private Integer c = null;
  /* Clisp */
  private Integer clisp = null;
  /* Clojure */
  private Integer clojure = null;
  /* Cpp */
  private Integer cpp = null;
  /* Csharp */
  private Integer csharp = null;
  /* Erlang */
  private Integer erlang = null;
  /* Go */
  private Integer go = null;
  /* Haskell */
  private Integer haskell = null;
  /* Java */
  private Integer java = null;
  /* Lua */
  private Integer lua = null;
  /* Mysql */
  private Integer mysql = null;
  /* Oracle */
  private Integer oracle = null;
  /* Perl */
  private Integer perl = null;
  /* Php */
  private Integer php = null;
  /* Python */
  private Integer python = null;
  /* Ruby */
  private Integer ruby = null;
  /* Scala */
  private Integer scala = null;
  @JsonProperty("bash")
  public Integer getBash() {
    return bash;
  }
  public void setBash(Integer bash) {
    this.bash = bash;
  }

  @JsonProperty("c")
  public Integer getC() {
    return c;
  }
  public void setC(Integer c) {
    this.c = c;
  }

  @JsonProperty("clisp")
  public Integer getClisp() {
    return clisp;
  }
  public void setClisp(Integer clisp) {
    this.clisp = clisp;
  }

  @JsonProperty("clojure")
  public Integer getClojure() {
    return clojure;
  }
  public void setClojure(Integer clojure) {
    this.clojure = clojure;
  }

  @JsonProperty("cpp")
  public Integer getCpp() {
    return cpp;
  }
  public void setCpp(Integer cpp) {
    this.cpp = cpp;
  }

  @JsonProperty("csharp")
  public Integer getCsharp() {
    return csharp;
  }
  public void setCsharp(Integer csharp) {
    this.csharp = csharp;
  }

  @JsonProperty("erlang")
  public Integer getErlang() {
    return erlang;
  }
  public void setErlang(Integer erlang) {
    this.erlang = erlang;
  }

  @JsonProperty("go")
  public Integer getGo() {
    return go;
  }
  public void setGo(Integer go) {
    this.go = go;
  }

  @JsonProperty("haskell")
  public Integer getHaskell() {
    return haskell;
  }
  public void setHaskell(Integer haskell) {
    this.haskell = haskell;
  }

  @JsonProperty("java")
  public Integer getJava() {
    return java;
  }
  public void setJava(Integer java) {
    this.java = java;
  }

  @JsonProperty("lua")
  public Integer getLua() {
    return lua;
  }
  public void setLua(Integer lua) {
    this.lua = lua;
  }

  @JsonProperty("mysql")
  public Integer getMysql() {
    return mysql;
  }
  public void setMysql(Integer mysql) {
    this.mysql = mysql;
  }

  @JsonProperty("oracle")
  public Integer getOracle() {
    return oracle;
  }
  public void setOracle(Integer oracle) {
    this.oracle = oracle;
  }

  @JsonProperty("perl")
  public Integer getPerl() {
    return perl;
  }
  public void setPerl(Integer perl) {
    this.perl = perl;
  }

  @JsonProperty("php")
  public Integer getPhp() {
    return php;
  }
  public void setPhp(Integer php) {
    this.php = php;
  }

  @JsonProperty("python")
  public Integer getPython() {
    return python;
  }
  public void setPython(Integer python) {
    this.python = python;
  }

  @JsonProperty("ruby")
  public Integer getRuby() {
    return ruby;
  }
  public void setRuby(Integer ruby) {
    this.ruby = ruby;
  }

  @JsonProperty("scala")
  public Integer getScala() {
    return scala;
  }
  public void setScala(Integer scala) {
    this.scala = scala;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class Codes {\n");
    sb.append("  bash: ").append(bash).append("\n");
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
    sb.append("  mysql: ").append(mysql).append("\n");
    sb.append("  oracle: ").append(oracle).append("\n");
    sb.append("  perl: ").append(perl).append("\n");
    sb.append("  php: ").append(php).append("\n");
    sb.append("  python: ").append(python).append("\n");
    sb.append("  ruby: ").append(ruby).append("\n");
    sb.append("  scala: ").append(scala).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}

