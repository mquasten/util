package de.mq.util.application.et;

import com.mscharhag.et.ExceptionTranslator;
import com.mscharhag.et.ReturningTryBlock;
import com.mscharhag.et.TryBlock;
/**
 * 
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
		 * @return ein Erbnis vom Type T
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

	

	

	

}