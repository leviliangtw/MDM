package org.ncsist.mdm.PassWordMaker;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * Created by Levi on 2016/12/4.
 */
public class PassWordMakerTest extends TestCase {

    public void setUp() throws Exception {
        super.setUp();

    }

    public void tearDown() throws Exception {

    }

    public void testGetUnlockPass() throws Exception {
        //arrange
        String hint = "wwwwww";
        String text = "公館營區:GPS訊號不佳";
        String expect = "753762";
        PassWordMaker pwdMaker = new PassWordMaker();

        //act
        String actual = pwdMaker.getUnlockPass(hint, text);

        //assert
        Assert.assertEquals(expect, actual);
    }
}