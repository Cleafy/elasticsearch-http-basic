package com.asquera.elasticsearch.plugins.http;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.elasticsearch.common.component.LifecycleComponent;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.inject.Module;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.plugins.Plugin;

/**
 * @author Florian Gilcher (florian.gilcher@asquera.de)
 */
public class HttpBasicServerPlugin extends Plugin {

    private boolean enabledByDefault = true;
    private final Settings settings;

    @Inject 
    public HttpBasicServerPlugin(Settings settings) {
    	this.settings = settings;
    }

    @Override 
    public String name() {
    	return "http-basic-server-plugin";
    }

    @Override 
    public String description() {
    	return "HTTP Basic Server Plugin";
    }

    @Override 
    public Collection<Module> nodeModules() {
      Collection<Class<? extends Module>> modules = new ArrayList<Class<? extends Module>>();
      if (settings.getAsBoolean("http.basic.enabled", enabledByDefault)) {
          modules.add(HttpBasicServerModule.class);
      }
      return Collections.<Module>singletonList(new HttpBasicServerModule(settings));
    }

    @Override 
    public Collection<Class<? extends LifecycleComponent>> nodeServices() {
      Collection<Class<? extends LifecycleComponent>> services = new ArrayList<Class<? extends LifecycleComponent>>();
      if (settings.getAsBoolean("http.basic.enabled", enabledByDefault)) {
          services.add(HttpBasicServer.class);
      }
      return services;
    }

    @Override 
    public Settings additionalSettings() {
      if (settings.getAsBoolean("http.basic.enabled", enabledByDefault)) {
          return Settings.settingsBuilder().
                  put("http.enabled", false).                    
                  build();
      } else {
          return Settings.Builder.EMPTY_SETTINGS;
      }
    }
}
