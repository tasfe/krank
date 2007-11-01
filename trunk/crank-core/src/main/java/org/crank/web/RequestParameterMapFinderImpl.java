package org.crank.web;

import java.util.Map;

import org.crank.core.RequestParameterMapFinder;

/** This is to find request params from non-JSF apps.*/
public class RequestParameterMapFinderImpl implements RequestParameterMapFinder {

    @SuppressWarnings("unchecked")
    public Map<String, String[]> getMap() {
        return (Map<String, String[]>) HttpRequestUtils.request().getParameterMap();
    }

}
