package de.pfiva.data.ingestion;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.web.client.RestTemplate;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

@Configuration
@EnableAsync
@EnableScheduling
public class DataIngestionConfiguration {

	@Autowired private DataIngestionProperties properties;
	
	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}
	
	@Bean
	public DataSource dataSource() {
		MysqlDataSource dataSource = new MysqlDataSource();
		//SQLiteDataSource dataSource = new SQLiteDataSource();
		dataSource.setDatabaseName(properties.getDatabaseName());
		dataSource.setUrl(properties.getDatabaseUrl());
		dataSource.setUser(properties.getDbUser());
		dataSource.setPassword(properties.getDbPassword());
		return dataSource;
	}
	
	@Bean 
	public JdbcTemplate jdbcTemplate(DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}
	
	@Bean
	public TaskScheduler taskScheduler() {
		ScheduledExecutorService localExecutor = Executors.newSingleThreadScheduledExecutor();
		return new ConcurrentTaskScheduler(localExecutor);
	}
	
//	@Bean
//	public Executor asyncExecutor() {
//		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//		executor.setQueueCapacity(500);
//        executor.setThreadNamePrefix("GithubLookup-");
//        executor.initialize();
//		return executor;
//	}
}
