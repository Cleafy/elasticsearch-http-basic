package com.asquera.elasticsearch.plugins.http.auth.integration;

import com.asquera.elasticsearch.plugins.http.HttpBasicServerPlugin;
import org.elasticsearch.common.Base64;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.test.ESIntegTestCase;
import org.elasticsearch.test.rest.client.http.HttpRequestBuilder;

/**
 *
 * @author Ernesto Miguez (ernesto.miguez@asquera.de)
 */
public class HttpBasicServerPluginIntegrationTest extends ESIntegTestCase {

  protected final String localhost = "127.0.0.1";

  /**
   *
   * @return a Builder with the plugin included and bind_host and publish_host
   * set to localhost, from where the client's request ip will be done.
   */
  protected Settings.Builder builderWithPlugin() {
      return Settings.builder()
              .put("network.host", localhost)
              .put("plugin.types", HttpBasicServerPlugin.class.getName());
  }

  protected HttpRequestBuilder requestWithCredentials(String credentials) throws Exception {
        return httpClient().path("/_status")
          .addHeader("Authorization", "Basic " + Base64.encodeBytes(credentials.getBytes()));
    }

}
