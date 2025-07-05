package com.events.api.infrastructure.configuration.exceptions;

/**
 * Based on RFC7807(<a href="https://datatracker.ietf.org/doc/html/rfc7807">Problem Details for HTTP APIs</a>)
 */

public class ProblemDetails {

    private String title;
    private Integer code;
    private String status;
    private String detail;
    private String instance;

    public ProblemDetails() {
    }

    public ProblemDetails(String title, Integer code, String status, String detail, String instance) {
        this.title = title;
        this.code = code;
        this.status = status;
        this.detail = detail;
        this.instance = instance;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getInstance() {
        return instance;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }
}
