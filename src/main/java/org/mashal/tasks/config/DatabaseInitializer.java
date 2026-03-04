package org.mashal.tasks.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;

public class DatabaseInitializer implements InitializingBean {
    private static final String SCHEMA_CREATION = "create schema if not exists %s";

    private final DataSource dataSource;
    private final String liquibaseSchema;

    @Autowired
    public DatabaseInitializer(DataSource dataSource, String liquibaseSchema) {
        this.dataSource = dataSource;
        this.liquibaseSchema = liquibaseSchema;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (StringUtils.isEmpty(liquibaseSchema)) {
            return;
        }

        try (Connection conn = dataSource.getConnection()) {
            Statement statement = conn.createStatement();
            statement.execute(String.format(SCHEMA_CREATION, liquibaseSchema));
        }
    }
}
