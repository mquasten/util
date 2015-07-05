package de.mq.util.application.et;

import de.mq.util.application.et.support.DefaultExceptionTranslatorConfigurer;
import de.mq.util.application.et.support.ExceptionTranslatorConfigurer;


/**
 * Operationen fuer ExceptionTranslations -> Checked nach Runtime
 * @author mq
 *
 */
public interface ExceptionTranslatorOperations {

	/**
	 * Fuehrt Operationen auf einem {@link AutoCloseable}, offnet/erzeugt es zuvor und schließt es nach Beeendigung.
	 * CheckExceptions aus der Steinzeit, werden vom Exceptiontranslator  in definierbare RuntimeExceptions ueberfuehrt.
	 * Der ausgefuehrte Codeblock gibt ein Ergebnis zurueck 
	 * @param translator der ExceptionTranslator
	 * @param resourceAccessor  Oeffnen des {@link AutoCloseable}
	 * @param resourceOperation Operationen  auf dem {@link AutoCloseable}
	 * @return Ergebnis der resourceOperation
	 */
	<T, A extends AutoCloseable> T doInTranslationWithResult(final ExceptionTranslator translator, final ResourceAccessor<A> resourceAccessor, final CloseableResourceOperationWithResult<A, T> resourceOperation);

	/**
	 * Fuehrt Operationen auf einem {@link AutoCloseable}, offnet/erzeugt es zuvor und schließt es nach Beeendigung.
	 * CheckExceptions aus der Steinzeit, werden vom Exceptiontranslator  in definierbare RuntimeExceptions ueberfuehrt.
	 * Der ausgefuehrte Codeblock gibt kein Ergebnis zurueck, er sist vom type void
	 * @param translator der ExceptionTranslator
	 * @param resourceAccessor  Oeffnen des {@link AutoCloseable}
	 * @param resourceOperation Operationen  auf dem {@link AutoCloseable}
	 * @return Ergebnis der resourceOperation
	 */
	<A extends AutoCloseable> void doInTranslationWithVoid(final ExceptionTranslator translator, final ResourceAccessor<A> resourceAccessor, final CloseableResourceOperationWithVoid<A> resourceOperation);
	/**
	 * Fuehrt eine operation aus. Die auftretenden checked Exceptions werden in RuntimeException verwandelt.
	 * Der CodeBlock liefert ein Ergebnis zuruck
	 * @param translator  der ExceptionTranslator
	 * @param returningTryBlock CodeBlock mit Ergebnis
	 * @return  Ergebnis des ausgefuehrten Codeblocks
	 */
	<T> T doInTranslationWithResult(final ExceptionTranslator translator, final ReturningTryBlock<T> returningTryBlock);
	
	/**
	 * Fuehrt eine operation aus. Die auftretenden checked Exceptions werden in RuntimeException verwandelt.
	 * Der CodeBlock liefert kein  Ergebnis zuruck. Er ist als void definiert.
	 * @param translator  der ExceptionTranslator
	 * @param returningTryBlock CodeBlock mit Ergebnis
	 * @return  Ergebnis des ausgefuehrten Codeblocks
	 */
	void doInTranslationVoid(final ExceptionTranslator translator, final TryBlock tryBlock);
	
	/**
	 * Operationen mit CheckedExceptions auf einem  {@link AutoCloseable}  mit beliebigem Ergenis
	 * @author mq
	 *
	 * @param <A> typ des {@link AutoCloseable}
	 * @param <T> Ergebnis der Ausfuehrung der run-Methode
	 */
	@FunctionalInterface
	public interface CloseableResourceOperationWithResult<A extends AutoCloseable, T> {
		/**
		 * Fuehrt eine Operation (z.B. Legacy-Code)  aus und gibt ein Ergbnis zurueck
		 * @param closeable ein {@link AutoCloseable}  als Parameter
		 * @return ein Ergebnis vom Type T
		 * @throws Exception eine Beliebige Checked Exception 
		 */
		T run(final A closeable) throws Exception ; 
	}
	
	/**
	 * Operationen mit CheckedExceptions auf einem  {@link AutoCloseable}  ohne Ergebnis.
	 * @author mq 
	 *
	 * @param <A> typ des {@link AutoCloseable}
	 */
	@FunctionalInterface
	public interface CloseableResourceOperationWithVoid<A extends AutoCloseable> {
		/**
		 *  Fuehrt eine Operation (z.B. Legacy-Code)  aus und gibt kein Ergbnis zurueck
		 * @param closeable ein {@link AutoCloseable}  als Parameter
		 * @throws Exception eine Beliebige Checked Exception 
		 */
		void run(final A closeable) throws Exception ; 
	}
	
	/**
	 * Zugriff auf ein {@link AutoCloseable}. Oeffnet/Erzeugt die Resource
	 * @author mq
	 *
	 * @param <A> typ des {@link AutoCloseable}
	 */
	@FunctionalInterface
	public interface ResourceAccessor<A extends AutoCloseable> {
		/**
		 * Oeffnet,Erzeugt oder bereitet  AutoCloseable fuer die Bentzung vor  
		 * @return die  geoeffnete Resource. 
		 * @throws Exception  eine Beliebige Checked Exception 
		 */
		A open() throws Exception ;
	}

	/**
	 * Ein einfacher CodeBlock zwischen try catch
	 * @author mq
	 *
	 */
	@FunctionalInterface
	public interface TryBlock {

		/**
		 * Den CodeBlock ausfuehren
		 * @throws Exception
		 */
	    void run() throws Exception;

	}

	/**
	 * Ein einfacher CodeBlock zwischen try catch mit Ergebnis
	 * @author mq
	 *
	 * @param <T> Ergebnis des CodeBlocks
	 */
	public interface ReturningTryBlock<T> {

		/**
		 * Den Codeblock ausfuehren
		 * @return das Erbnis des Blocks
		 * @throws Exception die dabei auftretende CheckedException aus der Steinzeit, post TRex mortem 
		 */
	    T run() throws Exception;
	}
	

	/**
	 * Eine ExceptionTranslator 
	 * @author mq
	 *
	 */
	public interface ExceptionTranslator {

		/**
		 * Einfachen CodeBlock zwischen try catch ausfuehren
		 * @param tryBlock
		 */
	    void withTranslation(final TryBlock tryBlock);

	    /**
	     * Einfachen CodeBlock zwischen try catch ausfuehren mit Returnwert ausfuehren
	     * @param invokable der CodeBlock
	     * @return das Ergebnis
	     */
	    <T> T withReturningTranslation(final ReturningTryBlock<T> invokable);

	    /**
	     * Erzeugt einen  @{ExceptionTranslatorConfigurer}
	     * @return der  @{ExceptionTranslatorConfigurer}
	     */
	    ExceptionTranslatorConfigurer newConfiguration();

	}
	
	


/**
 * Global factory that can be used to create new {@code ExceptionTranslator} configurations using
 * {@code ET.newConfiguration()}.
 * <p>Usage:
 * <pre>
 *     ExceptionTranslator et = ET.newConfiguration()
 *                                  // Exception translator configuration
 *                                  .translate(FooException.class).to(MyRuntimeException.class)
 *                                  .translate(BazException.class).using(OtherException::new)
 *                                  .translate(BarException.class).using((message, exception) -&gt; { .. })
 *                                  .done(); // Create ExceptionTranslator from configuration
 * </pre>
 */
public  interface ET {

  

    /**
     * Returns a new {@link ExceptionTranslatorConfigurer} that can be used to create a new
     * {@link ExceptionTranslator} configuration.
     * @return an {@link ExceptionTranslatorConfigurer}, never {@code null}
     */
    public static ExceptionTranslatorConfigurer newConfiguration() {
        return new DefaultExceptionTranslatorConfigurer();
    }

}
	

}