package com.ednaldoluiz.websocket.v1.integration;

import org.junit.platform.suite.api.IncludeClassNamePatterns;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@IncludeEngines("junit-jupiter")
@SelectPackages("com.ednaldoluiz.websocket.v1.integration.auth")
@IncludeClassNamePatterns(".*IT")
public class RunITTests {}