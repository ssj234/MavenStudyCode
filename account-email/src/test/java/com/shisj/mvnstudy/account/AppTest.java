package com.shisj.mvnstudy.account;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.shisj.mvnstudy.account.email.AccountEmailService;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp() throws Exception
    {
    	ApplicationContext ctx = new ClassPathXmlApplicationContext("account-email.xml");
    	
    	AccountEmailService service = (AccountEmailService) ctx.getBean("accountEmailService");
    	
    	service.sendMail("carol@163.com", "hello", "A good friend!");
        assertTrue( true );
    }
}
