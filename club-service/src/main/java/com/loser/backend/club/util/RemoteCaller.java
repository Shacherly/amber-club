package com.loser.backend.club.util;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.loser.backend.club.api.http.Response;
import com.loser.backend.club.constant.Constant;
import com.loser.backend.club.exception.BusinessException;
import com.loser.backend.club.exception.ExceptionEnum;
import com.loser.backend.club.exception.RemoteCallException;
import com.loser.backend.club.http.ContextHeader;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.DefaultAccessTokenRequest;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;


/**
 * @author ~~ trading.s
 * @date 12:17 10/15/21
 */
@Slf4j
public class RemoteCaller {

    @Autowired
    @Qualifier("oAuth2RestOperations")
    private OAuth2RestOperations aceupTemplate;
    @Value("${remote-call.aceup.domain}")
    private String aceupHost;
    @Value("${remote-call.domain}")
    private String domain;
    @Value("${spring.profiles.active}")
    private String env;

    private final RestTemplate restTemplate;

    public RemoteCaller(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    /**
     *
     * @param url
     * @param body 可以传Map对象或者对应的实体对象
     * @param headers
     * @param type
     * @param <BODY>
     * @param <ENTITIES>
     * @return
     */
    public <BODY, ENTITIES> Response<ENTITIES> postForEntity(
            String url, BODY body, /*@Nullable Map<String, String> params,*/
            @Nullable Map<String, String> headers, @NonNull TypeReference<Response<ENTITIES>> type) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        if (CollectionUtil.isNotEmpty(headers)) headers.forEach(httpHeaders::add);
        concertedHeaders().forEach(httpHeaders::add);

        HttpEntity<BODY> entity = new HttpEntity<>(body, httpHeaders);
        String jsonResponse = restTemplate.postForObject(url, entity, String.class);
        Response<ENTITIES> typeEntity = JSON.parseObject(jsonResponse, type);
        Objects.requireNonNull(typeEntity);

        if (typeEntity.getCode() == 0) {
            Map<String, Object> requestEntity = new LinkedHashMap<>(16);
            requestEntity.put("uri", url);
            requestEntity.put("httpEntity", JSONUtil.parseObj(entity));
            log.info("#postForEntity, request = {}, response = {}", JSONObject.toJSONString(requestEntity), JSONObject.toJSONString(typeEntity));
            return typeEntity;
        }
        throw new RemoteCallException(typeEntity.getCode(), typeEntity.getMsg(), ExceptionEnum.REMOTE_SERVER_ERROR);
    }

    public <BODY> List<JSONObject> post4JSONObject(String url, BODY body, @Nullable Map<String, String> headers) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        if (CollectionUtil.isNotEmpty(headers)) headers.forEach(httpHeaders::add);
        concertedHeaders().forEach(httpHeaders::add);

        HttpEntity<BODY> entity = new HttpEntity<>(body, httpHeaders);
        String jsonResponse = restTemplate.postForObject(url, entity, String.class);
        JSONObject jsonObject = JSONObject.parseObject(jsonResponse);
        if (jsonObject.getIntValue("code") == 0) {
            List<JSONObject> result = new ArrayList<>();
            JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("items");
            jsonArray.forEach(arr -> result.add((JSONObject) arr));
            Map<String, Object> requestEntity = new LinkedHashMap<>(16);
            requestEntity.put("uri", url);
            requestEntity.put("httpEntity", JSONUtil.parseObj(entity));
            log.info("#post4JSONObject, request = {}, response = {}", JSONObject.toJSONString(requestEntity), JSONObject.toJSONString(result));
            return result;
        }
        throw new RemoteCallException(jsonObject.getIntValue("code"), jsonObject.getString("msg"), ExceptionEnum.REMOTE_SERVER_ERROR);
    }

    public JSONObject get4JSONObject(String url, Map<String, String> headers, Map<String, String> params) {
        if (CollectionUtil.isNotEmpty(params)) {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
            params.forEach(builder::queryParam);
            url = builder.build().encode().toString();
        }
        HttpHeaders httpHeaders = new HttpHeaders();
        if (CollectionUtil.isNotEmpty(headers)) headers.forEach(httpHeaders::add);
        HttpEntity<?> entity = new HttpEntity<>(httpHeaders);

        String response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class).getBody();
        JSONObject jsonObject = JSONObject.parseObject(response);
        Objects.requireNonNull(jsonObject);
        if (jsonObject.getInteger("code") == 0) {
            JSONObject data = jsonObject.getJSONObject("data");
            Map<String, Object> requestEntity = new LinkedHashMap<>(16);
            requestEntity.put("uri", url);
            log.info("#get4JSONObject, request = {}, response = {}", JSONObject.toJSONString(requestEntity), JSONObject.toJSONString(data));
            return data;
        }
        throw new RemoteCallException(jsonObject.getInteger("code"), jsonObject.getString("msg"), ExceptionEnum.REMOTE_SERVER_ERROR);
    }

    public JSONArray get4JSONArray(String url, Map<String, String> headers, Map<String, Object> params) {
        if (CollectionUtil.isNotEmpty(params)) {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
            params.forEach(builder::queryParam);
            url = builder.build().encode().toString();
        }
        HttpHeaders httpHeaders = new HttpHeaders();
        if (CollectionUtil.isNotEmpty(headers)) headers.forEach(httpHeaders::add);
        HttpEntity<?> entity = new HttpEntity<>(httpHeaders);

        String response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class).getBody();
        JSONObject jsonObject = JSONObject.parseObject(response);
        Objects.requireNonNull(jsonObject);
        if (jsonObject.getInteger("code") == 0) {
            JSONArray data = jsonObject.getJSONArray("data");
            Map<String, Object> requestEntity = new LinkedHashMap<>(16);
            requestEntity.put("uri", url);
            log.info("#get4JSONObject, request = {}, response = {}", JSONObject.toJSONString(requestEntity), JSONObject.toJSONString(data));
            return data;
        }
        throw new RemoteCallException(jsonObject.getInteger("code"), jsonObject.getString("msg"), ExceptionEnum.REMOTE_SERVER_ERROR);
    }

    private Map<String, String> concertedHeaders() {
        Map<String, String> headers = new HashMap<>(32);
        ContextHeader header = ContextHolder.get();
        header.setXGwUser(null);
        if (StringUtils.isNotBlank(MDC.get(Constant.TRACE_ID))) {
            header.setXGwRequestid(MDC.get(Constant.TRACE_ID));
        }
        else {
            header.setXGwRequestid(UUID.randomUUID().toString());
        }
        JSONObject.parseObject(JSON.toJSONString(header)).forEach((k, v) -> headers.put(k, String.valueOf(v)));
        if (StringUtils.contains(env, "local")) {
            headers.put("X-HMAC-SPEC-KEY", "L9lVVbxPPZfmsZFAEC1DLErbLLgM5J");
        }
        return headers;
    }

    public <BODY> String authExchange(String uri, Map<String, String> params, BODY body, HttpMethod method) {
        String url = aceupHost + uri;
        if (CollectionUtil.isNotEmpty(params)) {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
            params.forEach(builder::queryParam);
            url = builder.build().encode().toString();
        }
        aceupTemplate.getAccessToken();
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> entity = new HttpEntity<>(body, requestHeaders);
        ResponseEntity<String> exchange = null;


        try {
            exchange = aceupTemplate.exchange(url, method, entity, String.class);
            log.info("Aceup Exchange url = {}, entity = {}, exchange = {}", url, entity, exchange);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            if (HttpStatus.UNAUTHORIZED.value() == e.getStatusCode().value()) {
                aceupTemplate.getOAuth2ClientContext().setAccessToken(this.refreshToken(aceupTemplate.getResource()));
                exchange = aceupTemplate.exchange(url, method, entity, String.class);
                log.info("Aceup Exchange url = {}, entity = {}, exchange = {}", url, entity, exchange);
            }
            throw new BusinessException(e.getMessage(), ExceptionEnum.REMOTE_SERVER_ERROR);
        }
        return exchange.getBody();
    }

    private OAuth2AccessToken refreshToken(OAuth2ProtectedResourceDetails resource) {
        OAuth2RestTemplate oAuth2RestTemplate = new OAuth2RestTemplate(resource, new DefaultOAuth2ClientContext(new DefaultAccessTokenRequest()));
        log.info("AceUp exchange refreshToken");
        return oAuth2RestTemplate.getOAuth2ClientContext().getAccessToken();
    }
}
