/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.asquera.elasticsearch.plugins.http.auth.integration;

import static org.hamcrest.Matchers.equalTo;

import org.apache.http.client.methods.HttpHead;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.test.ESIntegTestCase;
import org.elasticsearch.test.rest.client.http.HttpResponse;
import org.junit.Test;
import org.junit.Ignore;


/**
 * Test a rest action that sets special response headers
 */
@ESIntegTestCase.ClusterScope(transportClientRatio = 0.0, scope = ESIntegTestCase.Scope.SUITE, numDataNodes = 1)
@Ignore
public class DisabledWhitelistIntegrationTest extends HttpBasicServerPluginIntegrationTest {

    @Override
    protected Settings nodeSettings(int nodeOrdinal) {
        return  builderWithPlugin().
                put("http.basic.ipwhitelist", false)
                 .build();
    }

// TODO put the set credentials ussing Setter
    @Test
    public void clientIpAuthenticationFails() throws Exception {
        HttpResponse response = httpClient().path("/_status").execute();
        assertThat(response.getStatusCode(), equalTo(RestStatus.UNAUTHORIZED.getStatus()));
    }

    @Test
    // GET by default
    public void testHealthCheck() throws Exception {
        HttpResponse response = httpClient().path("/").execute();
        assertThat(response.getStatusCode(), equalTo(RestStatus.OK.getStatus()));
    }

    @Test
    public void testHealthCheckHeadMethod() throws Exception {
        HttpResponse response = httpClient().method(HttpHead.METHOD_NAME).path("/").execute();
        assertThat(response.getStatusCode(), equalTo(RestStatus.OK.getStatus()));
    }

    @Test
    public void clientGoodCredentialsBasicAuthenticationSuceeds() throws Exception {
        HttpResponse response = requestWithCredentials("admin:admin_pw")
          .addHeader("X-Forwarded-For", "1.1.1.1" ).execute();
        assertThat(response.getStatusCode(), equalTo(RestStatus.OK.getStatus()));
    }

    @Test
    public void clientBadCredentialsBasicAuthenticationFails() throws Exception {
        HttpResponse response = requestWithCredentials("admin:wrong").execute();
        assertThat(response.getStatusCode()
            , equalTo(RestStatus.UNAUTHORIZED.getStatus()));
    }

    @Test
    public void clientBadCredentialsSanityCheckOk() throws Exception {
        HttpResponse response = requestWithCredentials("admin:wrong").path("/").execute();
        assertThat(response.getStatusCode()
            , equalTo(RestStatus.OK.getStatus()));
    }
}
