package de.yaio.app.webapp.restcontroller;
//package de.yaio.rest.controller;
//
//import static org.junit.Assert.fail;
//
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.chrome.ChromeDriver;
//
//import com.thoughtworks.selenium.Selenium;
//import com.thoughtworks.selenium.webdriven.WebDriverBackedSelenium;
//
//public class SystestTasknodeCreateEditRemove {
//    private Selenium selenium;
//
//    @Before
//    public void setUp() throws Exception {
//        WebDriver  driver;
////        driver = new FirefoxDriver(
////                        new FirefoxBinary(new File("D:\\Programme\\firefox-28\\firefox.exe")), 
////                        null);
//        driver = new ChromeDriver();
////        driver = new HtmlUnitDriver(true);
////        driver.setJavascriptEnabled(true);
//        String baseUrl = "http://yaio-playground.local";
//        selenium = new WebDriverBackedSelenium(driver, baseUrl);
//        selenium.setSpeed("5000");
//    }
//
//    @Test
//    public void testSystest_tasknode_create_edit_remove() throws Exception {
//        //  
//        //   #######################################################
//        //     login
//        //   #######################################################
//
//        selenium.open("/yaio-explorerapp/yaio-explorerapp.html#/login");
//        for (int second = 0;; second++) {
//            if (second >= 5) fail("timeout");
//            try { if (selenium.isElementPresent("id=username")) break; } catch (Exception e) {}
//            Thread.sleep(1000);
//        }
//        selenium.type("id=username", "admin");
//        selenium.type("id=password", "secret");
//        selenium.click("css=button.button:nth-child(4)");
//        //  
//        //   #######################################################
//        //     open frontpage 
//        //   #######################################################
//
//        selenium.open("/yaio-explorerapp/yaio-explorerapp.html");
//        //  
//        //   #######################################################
//        //     wait and click explorer 
//        //   #######################################################
//
//        String nodeIdforShow = "MasterplanMasternode1";
//        for (int second = 0;; second++) {
//            if (second >= 60) fail("timeout");
//            try { if (selenium.isElementPresent("link=Baumansicht")) break; } catch (Exception e) {}
//            Thread.sleep(1000);
//        }
//
//        selenium.click("link=Baumansicht");
//        for (int second = 0;; second++) {
//            if (second >= 60) fail("timeout");
//            try { if (selenium.isElementPresent("//tr[@data-value = '" + nodeIdforShow + "']")) break; } catch (Exception e) {}
//            Thread.sleep(1000);
//        }
//
//        //  
//        //   #######################################################
//        //     wait and expand SysPlay1 
//        //   #######################################################
//
//        String nodeIdforExpand = "SysPlay1";
//        System.out.println("open expander for " + nodeIdforExpand);
//        for (int second = 0;; second++) {
//            if (second >= 60) fail("timeout");
//            try { if (selenium.isElementPresent("dom=document.getElementById(\"expander" + nodeIdforExpand + "\")")) break; } catch (Exception e) {}
//            Thread.sleep(1000);
//        }
//
//        selenium.click("dom=document.getElementById(\"expander" + nodeIdforExpand + "\")");
//        //  
//        //   #######################################################
//        //     wait and open createEditor for SysTest1 
//        //   #######################################################
//
//        String nodeIdforCreate = "SysTest1";
//        System.out.println("open createform for " + nodeIdforCreate);
//        for (int second = 0;; second++) {
//            if (second >= 60) fail("timeout");
//            try { if (selenium.isElementPresent("dom=document.getElementById(\"cmdCreate" + nodeIdforCreate + "\")")) break; } catch (Exception e) {}
//            Thread.sleep(1000);
//        }
//
//        selenium.click("dom=document.getElementById(\"cmdCreate" + nodeIdforCreate + "\")");
//        //  
//        //   #######################################################
//        //     create Task with Testtask + date 
//        //   #######################################################
//
//        String taskname = selenium.getEval(" \"testask\" + new Date().getTime() ");
//        System.out.println("create task with taskname: " + taskname);
//        selenium.select("id=inputCreateNodeType", "label=Aufgabe");
//        selenium.type("id=inputNameTaskNode", taskname);
//        selenium.select("id=inputTypeTaskNode", "label=+-- Offen");
//        selenium.type("id=inputPlanAufwandTaskNode", "1");
//        selenium.click("id=inputPlanStartTaskNode");
//        selenium.click("link=1");
//        selenium.click("id=inputPlanEndeTaskNode");
//        selenium.click("link=25");
//        selenium.type("id=inputNodeDescTaskNode", "fehlerhafte Tetsdaten");
//        selenium.click("//form[@id='nodeFormTaskNode']/fieldset[4]/button");
//        //  
//        //   #######################################################
//        //     wait for testTask 
//        //   #######################################################
//
//        for (int second = 0;; second++) {
//            if (second >= 60) fail("timeout");
//            try { if (selenium.isElementPresent("//span[text() = '" + taskname + "']")) break; } catch (Exception e) {}
//            Thread.sleep(1000);
//        }
//
//        String tasknameid = selenium.getAttribute("//span[text() = '" + taskname + "']@id");
//        String nodeIdCreated = selenium.getEval(" '" + tasknameid + "'.replace(\"title\", \"\"); ");
//        System.out.println("id=" + nodeIdCreated + " of new task with taskname: " + taskname);
//        //  
//        //   #######################################################
//        //     wait and open editEditor for edit Task
//        //   #######################################################
//
//        String nodeIdforEdit = nodeIdCreated;
//        System.out.println("open editform for " + nodeIdforEdit);
//        for (int second = 0;; second++) {
//            if (second >= 60) fail("timeout");
//            try { if (selenium.isElementPresent("dom=document.getElementById(\"cmdEdit" + nodeIdforEdit + "\")")) break; } catch (Exception e) {}
//            Thread.sleep(1000);
//        }
//
//        selenium.click("dom=document.getElementById(\"cmdEdit" + nodeIdforEdit + "\")");
//        //  
//        //   #######################################################
//        //     edit Task with new name, date, xdesc 
//        //   #######################################################
//
//        System.out.println("edit task with taskname: " + nodeIdforEdit);
//        selenium.type("id=inputIstAufwandTaskNode", "100");
//        selenium.click("id=inputIstStartTaskNode");
//        selenium.click("link=1");
//        selenium.click("id=inputIstEndeTaskNode");
//        selenium.click("link=25");
//        selenium.type("id=inputNodeDescTaskNode", "korrigierte Testdaten");
//        selenium.click("//form[@id='nodeFormTaskNode']/fieldset[4]/button");
//        //  
//        //   #######################################################
//        //     show SysPlay1 
//        //   #######################################################
//
//        nodeIdforShow = "SysTest1";
//        System.out.println("show for " + nodeIdforShow);
//        for (int second = 0;; second++) {
//            if (second >= 60) fail("timeout");
//            try { if (selenium.isElementPresent("css=a[href*='#/show/" + nodeIdforShow + "']")) break; } catch (Exception e) {}
//            Thread.sleep(1000);
//        }
//
//        selenium.click("css=a[href*='#/show/" + nodeIdforShow + "']");
//        for (int second = 0;; second++) {
//            if (second >= 60) fail("timeout");
//            try { if (selenium.isElementPresent("//tr[@data-value = '" + nodeIdforShow + "']")) break; } catch (Exception e) {}
//            Thread.sleep(1000);
//        }
//
//        //  
//        //   #######################################################
//        //     wait for testTask and remove 
//        //   #######################################################
//
//        String nodeIdForDelete = nodeIdCreated;
//        for (int second = 0;; second++) {
//            if (second >= 60) fail("timeout");
//            try { if (selenium.isElementPresent("dom=document.getElementById(\"cmdCreate" + nodeIdForDelete + "\")")) break; } catch (Exception e) {}
//            Thread.sleep(1000);
//        }
//
//        selenium.click("dom=document.getElementById(\"cmdRemove" + nodeIdForDelete + "\")");
//        String alertMsg = selenium.getConfirmation(); //Wollen Sie die Node wirklich löschen?
//        //  
//        //   #######################################################
//        //     show SysPlay1
//        //   #######################################################
//
//        nodeIdforShow = "SysPlay1";
//        System.out.println("show for " + nodeIdforShow);
//        for (int second = 0;; second++) {
//            if (second >= 60) fail("timeout");
//            try { if (selenium.isElementPresent("css=a[href*='#/show/" + nodeIdforShow + "']")) break; } catch (Exception e) {}
//            Thread.sleep(1000);
//        }
//
//        selenium.click("css=a[href*='#/show/" + nodeIdforShow + "']");
//        for (int second = 0;; second++) {
//            if (second >= 60) fail("timeout");
//            try { if (selenium.isElementPresent("//tr[@data-value = '" + nodeIdforShow + "']")) break; } catch (Exception e) {}
//            Thread.sleep(1000);
//        }
//
//        //  
//        //   #######################################################
//        //     show MasterplanMasternode1 
//        //   #######################################################
//
//        nodeIdforShow = "MasterplanMasternode1";
//        System.out.println("show for " + nodeIdforShow);
//        for (int second = 0;; second++) {
//            if (second >= 60) fail("timeout");
//            try { if (selenium.isElementPresent("css=a[href*='#/show/" + nodeIdforShow + "']")) break; } catch (Exception e) {}
//            Thread.sleep(1000);
//        }
//
//        selenium.click("css=a[href*='#/show/" + nodeIdforShow + "']");
//        for (int second = 0;; second++) {
//            if (second >= 60) fail("timeout");
//            try { if (selenium.isElementPresent("//tr[@data-value = '" + nodeIdforShow + "']")) break; } catch (Exception e) {}
//            Thread.sleep(1000);
//        }
//
//        //  
//        //   #######################################################
//        //     logout
//        //   #######################################################
//
//        selenium.open("/logout");
//    }
//
//    @After
//    public void tearDown() throws Exception {
////        selenium.stop();
//    }
//}
