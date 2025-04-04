/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package main;

import java.awt.Graphics2D;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author mrsch
 */
public class GameInfoTest {
    
    public GameInfoTest() {
    }

    @org.junit.jupiter.api.BeforeAll
    public static void setUpClass() throws Exception {
    }

    @org.junit.jupiter.api.AfterAll
    public static void tearDownClass() throws Exception {
    }

    @org.junit.jupiter.api.BeforeEach
    public void setUp() throws Exception {
    }

    @org.junit.jupiter.api.AfterEach
    public void tearDown() throws Exception {
    }
    

    /**
     * Test of draw method, of class GameInfo.
     */
    @org.junit.jupiter.api.Test
    public void testDraw() {
        System.out.println("draw");
        Graphics2D g2 = null;
        GameInfo instance = null;
        instance.draw(g2);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of update method, of class GameInfo.
     */
    @org.junit.jupiter.api.Test
    public void testUpdate() {
        System.out.println("update");
        GameInfo instance = null;
        instance.update();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of startRound method, of class GameInfo.
     */
    @org.junit.jupiter.api.Test
    public void testStartRound() {
        System.out.println("startRound");
        GameInfo instance = null;
        instance.startRound();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of endRound method, of class GameInfo.
     */
    @org.junit.jupiter.api.Test
    public void testEndRound() {
        System.out.println("endRound");
        GameInfo instance = null;
        instance.endRound();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
