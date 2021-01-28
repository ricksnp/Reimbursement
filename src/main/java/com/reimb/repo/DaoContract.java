package com.reimb.repo;

import java.util.List;

public interface DaoContract<T,I> {

	List<T> findAll();
	
	T findById(I i);
	
	int update(T t);
	
	boolean create(T t);
	
	boolean delete(String name);
	
	T findByName(String name);
	
	
}