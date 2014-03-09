package fr.icdc.dei.banque.lognavigator.controller;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.icdc.dei.banque.lognavigator.bean.LogAccessConfig;
import fr.icdc.dei.banque.lognavigator.exception.ConfigException;
import fr.icdc.dei.banque.lognavigator.service.AuthorizationService;
import fr.icdc.dei.banque.lognavigator.service.ConfigService;
import static fr.icdc.dei.banque.lognavigator.util.Constants.*;

@Controller
public class MainViewController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MainViewController.class);

	@Autowired
	private ConfigService configService;

	@Autowired
	private AuthorizationService authorizationService;
	

	@RequestMapping("/{logAccessConfigId}/prepare-main-view")
	public String prepareMainView(Model model, @PathVariable String logAccessConfigId) throws ConfigException {
		
		// Set nav bar data
		try {
			Authentication authorizedUser = SecurityContextHolder.getContext().getAuthentication();
			Set<LogAccessConfig> allLogAccessConfigs = configService.getLogAccessConfigs();
			Set<LogAccessConfig> authorizedLogAccessConfigs = authorizationService.getAuthorizedLogAccessConfigs(allLogAccessConfigs, authorizedUser);
			model.addAttribute(authorizedLogAccessConfigs);
			model.addAttribute(LOG_ACCESS_CONFIG_ID_KEY, logAccessConfigId);
		}
		catch (ConfigException e) {
			LOGGER.error("Error while loading configuration", e);
		}
		
		// Launch main view
		return MAIN_VIEW;
	}
}
