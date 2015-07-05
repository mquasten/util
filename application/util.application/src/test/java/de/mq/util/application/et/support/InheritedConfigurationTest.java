package de.mq.util.application.et.support;
import org.junit.Test;




import de.mq.util.application.et.ExceptionTranslatorOperations.ET;
import de.mq.util.application.et.ExceptionTranslatorOperations.ExceptionTranslator;

public class InheritedConfigurationTest {

    private ExceptionTranslator baseEt = ET.newConfiguration()
            .translate(FooException.class).to(FooRuntimeException.class)
            .done();

    @Test
    public void baseConfigurationIsInherited() {
        final ExceptionTranslator et = baseEt.newConfiguration()
                .translate(BarException.class).to(BarRuntimeException.class)
                .done();
        final RuntimeException result = TestUtil.translateException(et, TestUtil.FOO_EXCEPTION);
        TestUtil.expectException(result, FooRuntimeException.class, TestUtil.FOO_EXCEPTION_MESSAGE, TestUtil.FOO_EXCEPTION);
    }


    @Test
    public void baseConfigurationCanBeOverridden() {
        final ExceptionTranslator et = baseEt.newConfiguration()
                .translate(FooException.class).to(BarRuntimeException.class)
                .done();
        final RuntimeException result = TestUtil.translateException(et, TestUtil.FOO_EXCEPTION);
        TestUtil.expectException(result, BarRuntimeException.class, TestUtil.FOO_EXCEPTION_MESSAGE, TestUtil.FOO_EXCEPTION);
    }

    @Test
    public void configurationCanBeAdded() {
        final ExceptionTranslator et = baseEt.newConfiguration()
                .translate(BarException.class).to(BarRuntimeException.class)
                .done();
        final RuntimeException result = TestUtil.translateException(et, TestUtil.BAR_EXCEPTION);
        TestUtil.expectException(result, BarRuntimeException.class, TestUtil.BAR_EXCEPTION_MESSAGE, TestUtil.BAR_EXCEPTION);
    }
}
