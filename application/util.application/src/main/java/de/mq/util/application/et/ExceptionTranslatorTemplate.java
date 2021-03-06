package de.mq.util.application.et;




class ExceptionTranslatorTemplate implements ExceptionTranslatorOperations {
	
	/* (non-Javadoc)
	 * @see de.mq.util.application.et.ExceptionTranslatorOperations#doInTranslation(com.mscharhag.et.ExceptionTranslator, de.mq.util.application.et.ResourceAccessor, de.mq.util.application.et.CloseableResourceOperation)
	 */
	@Override
	public final <T, A extends AutoCloseable> T doInTranslationWithResult(final ExceptionTranslator translator, final ResourceAccessor<A> resourceAccessor, final CloseableResourceOperationWithResult<A,T> resourceOperation)    {
		return translator.withReturningTranslation( () -> handleTranslation(translator, resourceAccessor, resourceOperation));
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.mq.util.application.et.ExceptionTranslatorOperations#doInTranslationWithVoid(com.mscharhag.et.ExceptionTranslator, de.mq.util.application.et.ExceptionTranslatorOperations.ResourceAccessor, de.mq.util.application.et.ExceptionTranslatorOperations.CloseableResourceOperationWithVoid)
	 */
	@Override
	public final <A extends AutoCloseable> void doInTranslationWithVoid(final ExceptionTranslator translator, final ResourceAccessor<A> resourceAccessor, final CloseableResourceOperationWithVoid<A> resourceOperation)    {
		translator.withTranslation(() -> handleTranslation(translator, resourceAccessor, resourceOperation));
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.mq.util.application.et.ExceptionTranslatorOperations#doInTranslationWithResult(com.mscharhag.et.ExceptionTranslator, com.mscharhag.et.ReturningTryBlock)
	 */
	@Override
	public final <T> T doInTranslationWithResult(final ExceptionTranslator translator, final ReturningTryBlock<T>  returningTryBlock)    {
		return translator.withReturningTranslation(returningTryBlock);
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.mq.util.application.et.ExceptionTranslatorOperations#doInTranslationVoid(com.mscharhag.et.ExceptionTranslator, com.mscharhag.et.TryBlock)
	 */
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
