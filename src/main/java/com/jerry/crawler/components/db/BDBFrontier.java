package com.jerry.crawler.components.db;

import java.io.File;
import java.util.Map.Entry;
import java.util.Set;

import com.jerry.crawler.common.Global;
import com.sleepycat.bind.EntryBinding;
import com.sleepycat.bind.serial.SerialBinding;
import com.sleepycat.collections.StoredMap;

/**
 * @author Jerry Wang
 * @Email  jerry002@126.com
 * @date   2016年8月31日
 */
public class BDBFrontier extends AbstractFrontier {

	private StoredMap<Object, Object> db = null;
	
	public BDBFrontier(String homeDirectory) {
		super(homeDirectory);
		EntryBinding keyBinding = new SerialBinding(storedClassCatalog, Object.class);
		EntryBinding valueBinding = new SerialBinding(storedClassCatalog, Object.class);
		db = new StoredMap(database, keyBinding, valueBinding, true);
	}

	@Override
	public Object getNext() {
		Object object = null;
		if(!db.isEmpty()) {
			Set<Entry<Object, Object>> entrys = db.entrySet();
			Entry<Object, Object> entry = entrys.iterator().next();
			object = entry.getValue();
			delete(entry.getKey());
		}
		return object;
	}
	
	@Override
	protected void put(Object key, Object value) {
		db.put(key, value);
	}

	@Override
	protected Object get(Object key) {
		return db.get(key);
	}

	@Override
	protected Object delete(Object key) {
		return db.remove(key);
	}

	public static void main(String[] args) {
		BDBFrontier db = null;
		try {
			db = new BDBFrontier(Global.DB_PATH);
			db.put("key", "value");
			System.out.println(db.getNext());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
	}
}
