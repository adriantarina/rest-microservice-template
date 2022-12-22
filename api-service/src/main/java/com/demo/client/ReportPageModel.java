package com.demo.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.core.ResolvableType;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;

import java.util.Collection;

class ReportPageModel<T> extends PagedModel<T> {

    public ReportPageModel() {
    }

    public ReportPageModel(Collection<T> content, PageMetadata metadata) {
        super(content, metadata);
    }

    public ReportPageModel(Collection<T> content, PageMetadata metadata, Iterable<Link> links) {
        super(content, metadata, links);
    }

    public ReportPageModel(Collection<T> content, PageMetadata metadata, Iterable<Link> links, ResolvableType fallbackType) {
        super(content, metadata, links, fallbackType);
    }

    @JsonProperty("reportList")
    @Override
    public Collection<T> getContent() {
        return super.getContent();
    }
}
