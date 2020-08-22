package com.idutra.api.repository;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

@NoRepositoryBean
public interface GenericRepository<T, I> extends PagingAndSortingRepository<T, I>, QueryByExampleExecutor<T> {
}
