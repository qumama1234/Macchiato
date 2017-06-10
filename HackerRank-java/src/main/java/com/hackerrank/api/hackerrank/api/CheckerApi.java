package com.hackerrank.api.hackerrank.api;

import com.hackerrank.api.client.ApiException;
import com.hackerrank.api.client.ApiInvoker;

import com.hackerrank.api.hackerrank.model.Submission;
import com.hackerrank.api.hackerrank.model.LanguageResponse;
import com.sun.jersey.multipart.FormDataMultiPart;

import javax.ws.rs.core.MediaType;

import java.math.BigDecimal;
import java.io.File;
import java.util.*;

public class CheckerApi {
  String basePath = "http://api.hackerrank.com";
  ApiInvoker apiInvoker = ApiInvoker.getInstance();

  public ApiInvoker getInvoker() {
    return apiInvoker;
  }
  
  public void setBasePath(String basePath) {
    this.basePath = basePath;
  }
  
  public String getBasePath() {
    return basePath;
  }

  /**
   *   Submission
   * @param api_key API key
   * @param source The source code for a submission
   * @param lang The language code for the submission. Ex: 5 (Python)
   * @param testcases A JSON list of strings, each being a test case.
   * @param format JSON or XML
   * @param callback_url A callback url, on which the submission response will be posted as a JSON string under `data` parameter.
   * @param wait true means the response is sent only after the submission is compiled and run. false means the request returns immediately and submission response will posted through the callback URL.
   * @return Submission 
   */
  public Submission submission (String api_key, String source, Integer lang, String testcases, String format, String callback_url, String wait) throws ApiException {
    Object postBody = null;
    // verify required params are set
    if(api_key == null || source == null || lang == null || testcases == null || format == null ) {
       throw new ApiException(400, "missing required params");
    }
    // create path and map variables
    String path = "/checker/submission.json".replaceAll("\\{format\\}","json");

    // query params
    Map<String, String> queryParams = new HashMap<String, String>();
    Map<String, String> headerParams = new HashMap<String, String>();
    Map<String, String> formParams = new HashMap<String, String>();

    String[] contentTypes = {
      "application/x-www-form-urlencoded"};

    String contentType = contentTypes.length > 0 ? contentTypes[0] : "application/json";

    if(contentType.startsWith("multipart/form-data")) {
      boolean hasFields = false;
      FormDataMultiPart mp = new FormDataMultiPart();
      hasFields = true;
      mp.field("api_key", api_key, MediaType.MULTIPART_FORM_DATA_TYPE);
      hasFields = true;
      mp.field("source", source, MediaType.MULTIPART_FORM_DATA_TYPE);
      hasFields = true;
      mp.field("lang", lang, MediaType.MULTIPART_FORM_DATA_TYPE);
      hasFields = true;
      mp.field("testcases", testcases, MediaType.MULTIPART_FORM_DATA_TYPE);
      hasFields = true;
      mp.field("format", format, MediaType.MULTIPART_FORM_DATA_TYPE);
      hasFields = true;
      mp.field("callback_url", callback_url, MediaType.MULTIPART_FORM_DATA_TYPE);
      hasFields = true;
      mp.field("wait", wait, MediaType.MULTIPART_FORM_DATA_TYPE);
      if(hasFields)
        postBody = mp;
    }
    else {
      formParams.put("api_key", String.valueOf(api_key));
      formParams.put("source", String.valueOf(source));
      formParams.put("lang", String.valueOf(lang));
      formParams.put("testcases", String.valueOf(testcases));
      formParams.put("format", String.valueOf(format));
      formParams.put("callback_url", String.valueOf(callback_url));
      formParams.put("wait", String.valueOf(wait));
      }

    try {
      String response = apiInvoker.invokeAPI(basePath, path, "POST", queryParams, postBody, headerParams, formParams, contentType);
      if(response != null){
        return (Submission) ApiInvoker.deserialize(response, "", Submission.class);
      }
      else {
        return null;
      }
    } catch (ApiException ex) {
      if(ex.getCode() == 404) {
      	return null;
      }
      else {
        throw ex;
      }
    }
  }
  /**
   *   Languages
   * @return LanguageResponse 
   */
  public LanguageResponse languages () throws ApiException {
    Object postBody = null;
    // create path and map variables
    String path = "/checker/languages.json".replaceAll("\\{format\\}","json");

    // query params
    Map<String, String> queryParams = new HashMap<String, String>();
    Map<String, String> headerParams = new HashMap<String, String>();
    Map<String, String> formParams = new HashMap<String, String>();

    String[] contentTypes = {
      "application/x-www-form-urlencoded"};

    String contentType = contentTypes.length > 0 ? contentTypes[0] : "application/json";

    if(contentType.startsWith("multipart/form-data")) {
      boolean hasFields = false;
      FormDataMultiPart mp = new FormDataMultiPart();
      if(hasFields)
        postBody = mp;
    }
    else {
      }

    try {
      String response = apiInvoker.invokeAPI(basePath, path, "GET", queryParams, postBody, headerParams, formParams, contentType);
      if(response != null){
        return (LanguageResponse) ApiInvoker.deserialize(response, "", LanguageResponse.class);
      }
      else {
        return null;
      }
    } catch (ApiException ex) {
      if(ex.getCode() == 404) {
      	return null;
      }
      else {
        throw ex;
      }
    }
  }
  }

