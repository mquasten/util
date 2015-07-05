package de.mq.util.application.et;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.web.client.ResourceAccessException;




import de.mq.util.application.et.ExceptionTranslatorOperations.CloseableResourceOperationWithResult;
import de.mq.util.application.et.ExceptionTranslatorOperations.CloseableResourceOperationWithVoid;
import de.mq.util.application.et.ExceptionTranslatorOperations.ExceptionTranslator;
import de.mq.util.application.et.ExceptionTranslatorOperations.ResourceAccessor;
import de.mq.util.application.et.ExceptionTranslatorOperations.ReturningTryBlock;
import de.mq.util.application.et.ExceptionTranslatorOperations.TryBlock;

public class ExceptionTranslatorTemplateTest {

	private static final String ERROR_MESSAGE = "Sucks";
	private static final String RESULT = "result";
	private final ExceptionTranslatorOperations exceptionTranslatorOperations = new ExceptionTranslatorTemplate();
	private final ExceptionTranslator et = Mockito.mock(ExceptionTranslator.class);
	@SuppressWarnings("unchecked")
	private final ResourceAccessor<AutoCloseable> resourceAccessor = Mockito.mock(ResourceAccessor.class);
	@SuppressWarnings({ "unchecked" })
	private final CloseableResourceOperationWithResult<AutoCloseable, String> operation = Mockito.mock(CloseableResourceOperationWithResult.class);

	@SuppressWarnings({ "unchecked" })
	private final CloseableResourceOperationWithVoid<AutoCloseable> voidoperation = Mockito.mock(CloseableResourceOperationWithVoid.class);

	AutoCloseable autoCloseable = Mockito.mock(AutoCloseable.class);

	@Test
	public void doInTranslationWithResult() throws Exception {

		Mockito.when(resourceAccessor.open()).thenReturn(autoCloseable);
		Mockito.doAnswer(i -> {
			((ReturningTryBlock<?>) i.getArguments()[0]).run();

			return RESULT;

		}).when(et).withReturningTranslation(Mockito.any());

		Assert.assertEquals(RESULT, exceptionTranslatorOperations.doInTranslationWithResult(et, resourceAccessor, operation));
		Mockito.verify(autoCloseable).close();
		Mockito.verify(operation).run(autoCloseable);

	}

	@Test(expected = ResourceAccessException.class)
	public void doInTranslationWithResultOpenSucks() throws Exception {
		final IOException ex = new IOException();
		Mockito.doAnswer(i -> {
			try {
				((ReturningTryBlock<?>) i.getArguments()[0]).run();
			} catch (IOException io) {
				Assert.assertEquals(ex, io);
				throw new ResourceAccessException(ERROR_MESSAGE, io);
			}

			return RESULT;

		}).when(et).withReturningTranslation(Mockito.any());

		Mockito.doThrow(ex).when(resourceAccessor).open();
		exceptionTranslatorOperations.doInTranslationWithResult(et, resourceAccessor, operation);

		Mockito.verify(autoCloseable).close();
	}

	@Test(expected = ResourceAccessException.class)
	public void doInTranslationWithResultOpenSucks2() throws Exception {
		Mockito.when(resourceAccessor.open()).thenReturn(autoCloseable);
		Mockito.doAnswer(i -> {

			try {
				((ReturningTryBlock<?>) i.getArguments()[0]).run();
			} catch (IOException io) {
				throw new ResourceAccessException("sucks");
			}
			return null;

		}).when(et).withReturningTranslation(Mockito.any());

		Mockito.doThrow(new IOException()).when(operation).run(autoCloseable);

		exceptionTranslatorOperations.doInTranslationWithResult(et, resourceAccessor, operation);

		Mockito.verify(autoCloseable).close();
		Mockito.verify(operation).run(null);
	}

	@Test()
	public void doInTranslationWithResultWithoutAutoCloseable() throws Exception {
		Mockito.doAnswer(i -> {
			((ReturningTryBlock<?>) i.getArguments()[0]).run();

			return RESULT;

		}).when(et).withReturningTranslation(Mockito.any());

		Mockito.when(resourceAccessor.open()).thenReturn(null);

		exceptionTranslatorOperations.doInTranslationWithResult(et, resourceAccessor, operation);

		Mockito.verify(operation).run(null);
		Mockito.verify(autoCloseable, Mockito.times(0)).close();

	}

	@Test(expected = ResourceAccessException.class)
	public void doInTranslationWithResultWithoutAutoCloseableSucks() throws Exception {
		Mockito.doAnswer(i -> {

			try {
				((ReturningTryBlock<?>) i.getArguments()[0]).run();
			} catch (IOException io) {
				throw new ResourceAccessException("sucks");
			}
			return null;

		}).when(et).withReturningTranslation(Mockito.any());

		Mockito.when(resourceAccessor.open()).thenReturn(null);
		Mockito.doThrow(new IOException()).when(operation).run(null);

		exceptionTranslatorOperations.doInTranslationWithResult(et, resourceAccessor, operation);

	}

	@Test
	public void doInTranslationWithVoid() throws Exception {

		Mockito.when(resourceAccessor.open()).thenReturn(autoCloseable);
		Mockito.doAnswer(i -> {
			((TryBlock) i.getArguments()[0]).run();
			return null;

		}).when(et).withTranslation(Mockito.any());

		exceptionTranslatorOperations.doInTranslationWithVoid(et, resourceAccessor, voidoperation);
		Mockito.verify(autoCloseable).close();
		Mockito.verify(voidoperation).run(autoCloseable);

	}

	@Test(expected = ResourceAccessException.class)
	public void doInTranslationWithVoidOpenSucks() throws Exception {
		final IOException ex = new IOException();
		Mockito.doAnswer(i -> {
			try {
				((TryBlock) i.getArguments()[0]).run();
			} catch (IOException io) {
				Assert.assertEquals(ex, io);
				throw new ResourceAccessException(ERROR_MESSAGE, io);
			}

			return RESULT;

		}).when(et).withTranslation(Mockito.any());

		Mockito.doThrow(ex).when(resourceAccessor).open();
		exceptionTranslatorOperations.doInTranslationWithVoid(et, resourceAccessor, voidoperation);

		Mockito.verify(autoCloseable).close();
	}

	@Test(expected = ResourceAccessException.class)
	public void doInTranslationWithVoidOpenSucks2() throws Exception {
		Mockito.when(resourceAccessor.open()).thenReturn(autoCloseable);
		Mockito.doAnswer(i -> {

			try {
				((TryBlock) i.getArguments()[0]).run();
			} catch (IOException io) {
				throw new ResourceAccessException("sucks");
			}
			return null;

		}).when(et).withTranslation(Mockito.any());

		Mockito.doThrow(new IOException()).when(voidoperation).run(autoCloseable);

		exceptionTranslatorOperations.doInTranslationWithVoid(et, resourceAccessor, voidoperation);

		Mockito.verify(autoCloseable).close();
		Mockito.verify(operation).run(null);
	}

	@Test()
	public void doInTranslationWithVoidWithoutAutoCloseable() throws Exception {
		Mockito.doAnswer(i -> {
			((TryBlock) i.getArguments()[0]).run();

			return null;

		}).when(et).withTranslation(Mockito.any());

		Mockito.when(resourceAccessor.open()).thenReturn(null);

		exceptionTranslatorOperations.doInTranslationWithVoid(et, resourceAccessor, voidoperation);

		Mockito.verify(voidoperation).run(null);
		Mockito.verify(autoCloseable, Mockito.times(0)).close();

	}

	@Test(expected = ResourceAccessException.class)
	public void doInTranslationWithVoidWithoutAutoCloseableSucks() throws Exception {
		Mockito.doAnswer(i -> {

			try {
				((TryBlock) i.getArguments()[0]).run();
			} catch (IOException io) {
				throw new ResourceAccessException("sucks");
			}
			return null;

		}).when(et).withTranslation(Mockito.any());

		Mockito.when(resourceAccessor.open()).thenReturn(null);
		Mockito.doThrow(new IOException()).when(voidoperation).run(null);

		exceptionTranslatorOperations.doInTranslationWithVoid(et, resourceAccessor, voidoperation);

	}
	
	@Test
	public void doInTranslationWithResultWithoutCloseable() throws Exception {

	
		@SuppressWarnings("unchecked")
		final ReturningTryBlock<String> operation = Mockito.mock(ReturningTryBlock.class);
		
	
		Mockito.doAnswer(i -> {
			final ReturningTryBlock<?> block =  (ReturningTryBlock<?>) i.getArguments()[0];
			Assert.assertEquals(operation, block);
			block.run();
			return RESULT;

		}).when(et).withReturningTranslation(Mockito.any());

		Assert.assertEquals(RESULT, exceptionTranslatorOperations.doInTranslationWithResult(et,operation));
	
		Mockito.verify(operation).run();

	}
	
	
	@Test
	public void doInTranslationWithVoidWithoutCloseable() throws Exception {

	
		final TryBlock operation = Mockito.mock(TryBlock.class);
		
	
		Mockito.doAnswer(i -> {
			final TryBlock block =  (TryBlock) i.getArguments()[0];
			Assert.assertEquals(operation, block);
			block.run();
			return null;

		}).when(et).withTranslation(Mockito.any());

		exceptionTranslatorOperations.doInTranslationVoid(et,operation);
	
		Mockito.verify(operation).run();

	}

}
