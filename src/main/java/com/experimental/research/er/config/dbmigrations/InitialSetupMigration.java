package com.experimental.research.er.config.dbmigrations;

import com.experimental.research.er.config.Constants;
import com.experimental.research.er.domain.Authority;
import com.experimental.research.er.domain.User;
import com.experimental.research.er.security.AuthoritiesConstants;
import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.github.cloudyrock.mongock.driver.mongodb.springdata.v3.decorator.impl.MongockTemplate;
import java.time.Instant;

/**
 * Creates the initial database setup.
 */
public class InitialSetupMigration {}
