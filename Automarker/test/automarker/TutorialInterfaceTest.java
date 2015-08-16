/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automarker;

import java.awt.event.ActionEvent;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Chris
 */
public class TutorialInterfaceTest {
    
    public TutorialInterfaceTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of main method, of class TutorialInterface.
     */
    @Test
    public void testMain() {
        System.out.println("main");
        String[] args = null;
        TutorialInterface.main(args);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of loadWelcomeMessage method, of class TutorialInterface.
     */
    @Test
    public void testLoadWelcomeMessage() {
        System.out.println("loadWelcomeMessage");
        TutorialInterface instance = new TutorialInterface();
        String expResult = "";
        String result = instance.loadWelcomeMessage();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of loadQuestions method, of class TutorialInterface.
     */
    @Test
    public void testLoadQuestions() {
        System.out.println("loadQuestions");
        TutorialInterface instance = new TutorialInterface();
        instance.loadQuestions();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of saveSolutions method, of class TutorialInterface.
     */
    @Test
    public void testSaveSolutions() {
        System.out.println("saveSolutions");
        TutorialInterface instance = new TutorialInterface();
        instance.saveSolutions();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of submitSolutions method, of class TutorialInterface.
     */
    @Test
    public void testSubmitSolutions() {
        System.out.println("submitSolutions");
        TutorialInterface instance = new TutorialInterface();
        instance.submitSolutions();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of actionPerformed method, of class TutorialInterface.
     */
    @Test
    public void testActionPerformed() {
        System.out.println("actionPerformed");
        ActionEvent e = null;
        TutorialInterface instance = new TutorialInterface();
        instance.actionPerformed(e);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
