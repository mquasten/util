package de.mq.util.application.et;

import com.mscharhag.et.ExceptionTranslator;
import com.mscharhag.et.ReturningTryBlock;
import com.mscharhag.et.TryBlock;

public interface ExceptionTranslatorOperations {

	<T, A extends AutoCloseable> T doInTranslationWithResult(final ExceptionTranslator translator, final ResourceAccessor<A> resourceAccessor, final CloseableResourceOperationWithResult<A, T> resourceOperation);

	<A extends AutoCloseable> void doInTranslationWithVoid(final ExceptionTranslator translator, final ResourceAccessor<A> resourceAccessor, final CloseableResourceOperationWithVoid<A> resourceOperation);
	
	<T> T doInTranslationWithResult(final ExceptionTranslator translator, final ReturningTryBlock<T> returningTryBlock);
	
	void doInTranslationVoid(final ExceptionTranslator translator, final TryBlock tryBlock);
	
	@FunctionalInterface
	public interface CloseableResourceOperationWithResult<A extends AutoCloseable, T> {
		T run(final A closeable) throws Exception ; 
	}
	
	@FunctionalInterface
	public interface CloseableResourceOperationWithVoid<A extends AutoCloseable> {
		void run(final A closeable) throws Exception ; 
	}
	
	@FunctionalInterface
	public interface ResourceAccessor<A extends AutoCloseable> {
		A open() throws Exception ;
	}

	

	

	

}