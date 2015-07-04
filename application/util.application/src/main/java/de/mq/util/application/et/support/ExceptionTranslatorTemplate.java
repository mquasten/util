package de.mq.util.application.et.support;

import com.mscharhag.et.ExceptionTranslator;
import com.mscharhag.et.ReturningTryBlock;
import com.mscharhag.et.TryBlock;

import de.mq.util.application.et.ExceptionTranslatorOperations;

public class ExceptionTranslatorTemplate implements ExceptionTranslatorOperations {
	
	/* (non-Javadoc)
	 * @see de.mq.util.application.et.support.ExceptionTranslatorOperations#doInTranslation(com.mscharhag.et.ExceptionTranslator, de.mq.util.application.et.support.ResourceAccessor, de.mq.util.application.et.support.CloseableResourceOperation)
	 */
	@Override
	public final <T, A extends AutoCloseable> T doInTranslationWithResult(final ExceptionTranslator translator, final ResourceAccessor<A> resourceAccessor, final CloseableResourceOperationWithResult<A,T> resourceOperation)    {
		return translator.withReturningTranslation( () -> handleTranslation(translator, resourceAccessor, resourceOperation));
	}
	
	@Override
	public final <A extends AutoCloseable> void doInTranslationWithVoid(final ExceptionTranslator translator, final ResourceAccessor<A> resourceAccessor, final CloseableResourceOperationWithVoid<A> resourceOperation)    {
		translator.withTranslation(() -> handleTranslation(translator, resourceAccessor, resourceOperation));
	}
	
	@Override
	public final <T> T doInTranslationWithResult(final ExceptionTranslator translator, final ReturningTryBlock<T>  returningTryBlock)    {
		return translator.withReturningTranslation(returningTryBlock);
	}
	
	@Override
	public void  doInTranslationVoid(final ExceptionTranslator translator, final TryBlock  tryBlock)    {
		translator.withTranslation(tryBlock);
	}
	
	

	private <T, A extends AutoCloseable> T  handleTranslation(final ExceptionTranslator translator, final ResourceAccessor<A> resourceAccessor, CloseableResourceOperationWithResult<A,T> resourceOperation) throws Exception {
		try(final A resource = resourceAccessor.open()){
		   return resourceOperation.run(resource);	
		}
	}
	
	private <A extends AutoCloseable> void  handleTranslation(final ExceptionTranslator translator, final ResourceAccessor<A> resourceAccessor, CloseableResourceOperationWithVoid<A> resourceOperation) throws Exception {
		try(final A resource = resourceAccessor.open()){
		    resourceOperation.run(resource);	
		}
	}

}
