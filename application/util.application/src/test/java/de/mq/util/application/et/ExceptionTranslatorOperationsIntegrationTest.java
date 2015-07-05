package de.mq.util.application.et;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;



import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.ResourceAccessException;


import de.mq.util.application.et.ExceptionTranslatorOperations;
import de.mq.util.application.et.ExceptionTranslatorOperations.ET;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/beans.xml"})
public class ExceptionTranslatorOperationsIntegrationTest {
	
	static final String RESULT = "Kylie will be  the best approximation ever for Eliza";
	static final byte[] CONTENT = "They call me The Wild Rose\nBut my name was Eliza Day\nWhy they call me it I do not know\nFor my name was Eliza Day".getBytes(); 
	
	@Autowired
	private  ExceptionTranslatorOperations exceptionTranslatorOperations;
	
	@Test
	public final void createFileWithResult()  {
		
		String result = exceptionTranslatorOperations.doInTranslationWithResult(ET.newConfiguration().translate(IOException.class).to(ResourceAccessException.class).done(), () ->  {
			final File file = File.createTempFile("whereTheWildRosesGrow", ".txt");
			//final File file = new File("xy:\\whereTheWildRosesGrow.txt");
			//System.out.println(file.getPath());
			file.deleteOnExit();
		   return new FileOutputStream(file);
		}, os -> { 
			  StreamUtils.copy(CONTENT , os);
			  return RESULT;
		}
		);
		
		Assert.assertEquals(RESULT, result);
	}
	
	
	@Test
	public final void createFileWithVoid()  {
		
		exceptionTranslatorOperations.doInTranslationWithVoid(ET.newConfiguration().translate(IOException.class).to(ResourceAccessException.class).done(), () ->  {
			final File file = File.createTempFile("whereTheWildRosesGrow", ".txt");
			//final File file = new File("xy:\\whereTheWildRosesGrow.txt");
			//System.out.println(file.getPath());
			file.deleteOnExit();
		   return new FileOutputStream(file);
		}, os -> { 
			  StreamUtils.copy(CONTENT , os);
		}
		);
		
	
		
	}

}
