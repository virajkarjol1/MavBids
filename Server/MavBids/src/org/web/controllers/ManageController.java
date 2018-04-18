package org.web.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.web.dao.DBMgr;

@Controller
public class ManageController {

	final static Logger logger = Logger.getLogger(ManageController.class);

	@Autowired
	DBMgr dbMgr;
}
