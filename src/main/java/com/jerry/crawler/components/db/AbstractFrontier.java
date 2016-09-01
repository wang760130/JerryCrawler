package com.jerry.crawler.components.db;

import java.io.File;

import com.sleepycat.bind.serial.StoredClassCatalog;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;

/**
 * @author Jerry Wang
 * @Email  jerry002@126.com
 * @date   2016年8月31日
 */
public abstract class AbstractFrontier {

	private Environment environment = null;
	private static final String CLASS_CATALOG = "java_class_catalog";
	protected StoredClassCatalog storedClassCatalog;
	protected Database catalogdatabase = null;
	protected Database database = null;
	
	public AbstractFrontier(String homeDirectory) {
		EnvironmentConfig config = new EnvironmentConfig();
		config.setTransactional(true);
		config.setAllowCreate(true);
		environment = new Environment(new File(homeDirectory), config);
		DatabaseConfig dbConfig = new DatabaseConfig();
		dbConfig.setTemporary(true);
		dbConfig.setAllowCreate(true);
		catalogdatabase = environment.openDatabase(null, CLASS_CATALOG, dbConfig);
		storedClassCatalog = new StoredClassCatalog(catalogdatabase);
		database = environment.openDatabase(null, "URL", dbConfig);
	}
	
	public void close() throws DatabaseException {
		database.close();
		storedClassCatalog.close();
		environment.close();
	}
	
	protected abstract Object getNext();
	
	protected abstract void put(Object key, Object value) ;
	
	protected abstract Object get(Object key);
	
	protected abstract Object delete(Object key);	

}
