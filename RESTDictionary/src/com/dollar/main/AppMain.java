package com.dollar.main;


import com.dollar.service.DollarService;
import com.wipro.c4.core.utils.infra.Config;
import com.wipro.c4.core.utils.infra.ConfigUtility;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

/**
 * The main application class from where the application starts. It registers
 * all the services which we are using.It configures all the required
 * configuration to run the dropwizard project.
 * 
 * @author RI351215 Rinkesh Jha
 *
 * @see Application
 * @see Environment
 */
public class AppMain extends Application<Config> {

	public static void main(String[] args) throws Exception {
		System.out.println("r");
		new AppMain().run(args);
	}

	@Override
	public String getName() {
		return "dollar-service";
	}

	@Override
	public void initialize(Bootstrap<Config> bootstrap) {
	}
	
	
	@Override
	public void run(Config configuration, Environment environment) {
		Config.setConfig(configuration);
		ConfigUtility.configureCors(environment, Config.getConfig().getcORSConfig().getSupportedMethods(),
				Config.getConfig().getcORSConfig().getAllowedHeaders());	
		environment.jersey().register(DollarService.class);
	}
	

}