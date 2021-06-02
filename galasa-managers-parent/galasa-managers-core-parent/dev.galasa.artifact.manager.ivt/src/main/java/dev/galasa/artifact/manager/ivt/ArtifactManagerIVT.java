/*
 * Licensed Materials - Property of IBM
 * 
 * (c) Copyright IBM Corp. 2019.
 */
package dev.galasa.artifact.manager.ivt;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import org.apache.commons.logging.Log;

import dev.galasa.Test;
import dev.galasa.artifact.ArtifactManager;
import dev.galasa.artifact.BundleResources;
import dev.galasa.artifact.IArtifactManager;
import dev.galasa.artifact.IBundleResources;
import dev.galasa.artifact.ISkeletonProcessor.SkeletonType;
import dev.galasa.artifact.TestBundleResourceException;
import dev.galasa.core.manager.Logger;

@Test
public class ArtifactManagerIVT {

    @Logger
    public Log              logger;

    @ArtifactManager
    public IArtifactManager artifacts;

    @BundleResources
    public IBundleResources resources;

    @Test
    public void checkManagerNotNull() throws Exception {
        assertThat(artifacts).isNotNull();
        assertThat(resources).isNotNull();
    }

    @Test
    public void testRetrieveFileAsStringMethod() throws Exception {
        String textContent = resources.retrieveFileAsString("/resources/textFiles/test1.txt");
        logger.debug("Read the following from the file test1.txt: " + textContent);
        assertThat(textContent.trim()).isEqualTo("Hello from Galasa");
    }

    @Test
    public void testRetrieveSkeletonFileAsStringMethod() throws Exception {
        String textContent = resources.retrieveSkeletonFileAsString("/resources/skeletons/test1.skel", buildHashMap());
        logger.info("Received the following from the skeleton file: " + textContent);
        assertThat(textContent.trim()).isEqualTo("The third parameter is ITEM NUMBER THREE");
    }
   

    @Test
    public void readTextFileArtifactManager() throws Exception, TestBundleResourceException, IOException {
        InputStream file = resources.retrieveFile("/resources/textFiles/test1.txt");
        String textContent = artifacts.getBundleResources(this.getClass()).streamAsString(file);
        logger.debug("Read the following from the file test1.txt: " + textContent);
        assertThat(textContent.trim()).isEqualTo("Hello from Galasa");
    }

    @Test
    public void readTextFileBundleResources() throws Exception, TestBundleResourceException, IOException {
        String textContent = resources.streamAsString(resources.retrieveFile("/resources/textFiles/test1.txt"));
        logger.debug("Read the following from the file test1.txt: " + textContent);
        assertThat(textContent.trim()).isEqualTo("Hello from Galasa");
    }

    private HashMap<String,Object> buildHashMap(){
        HashMap<String, Object> props = new HashMap<>();
        props.put("ITEM1", "THIS IS ITEM1");
        props.put("ITEM2", "SECOND ITEM");
        props.put("ITEM3", "ITEM NUMBER THREE");
        return props;
    }

    @Test
    public void readSkeletonBundleResources() throws TestBundleResourceException, Exception, IOException {
        String textContent = resources.streamAsString(resources.retrieveSkeletonFile("/resources/skeletons/test1.skel", buildHashMap()));
        logger.info("Received the following from the skeleton file: " + textContent);
        assertThat(textContent.trim()).isEqualTo("The third parameter is ITEM NUMBER THREE");
    }
    
    @Test
    public void readSkeletonBundleResourcesVelocity() throws TestBundleResourceException, Exception, IOException {
        String textContent = resources.streamAsString(resources.retrieveSkeletonFile("/resources/velocity/velocityTest.skel", buildHashMap(), SkeletonType.VELOCITY));
        logger.info("Received the following from the skeleton file: " + textContent);
        assertThat(textContent.trim()).isEqualTo("The third parameter is ITEM NUMBER THREE");
    }

    @Test
    public void readSkeletonFileArtifactManager() throws TestBundleResourceException, Exception, IOException {
        InputStream is = resources.retrieveSkeletonFile("/resources/skeletons/test1.skel", buildHashMap());
        String textContent = artifacts.getBundleResources(this.getClass()).streamAsString(is);
        logger.info("Received the following from the skeleton file: " + textContent);
        
        assertThat(textContent.trim()).isEqualTo("The third parameter is ITEM NUMBER THREE");
    }

    @Test
    public void readSkeletonFile2ArtifactManager() throws TestBundleResourceException, Exception, IOException {
        HashMap<String, Object> props = new HashMap<>();
        props.put("ITEM1", "THIS IS ITEM1");
        props.put("ITEM2", "SECOND ITEM");
        props.put("ITEM3", "ITEM NUMBER THREE");

        InputStream is = resources.retrieveSkeletonFile("/resources/skeletons/test2.skel", props);
        String textContent = artifacts.getBundleResources(this.getClass()).streamAsString(is);
        logger.info("Received the following from the skeleton file: " + textContent);

        assertThat(textContent.trim()).contains("The third parameter is ITEM NUMBER THREE");
        assertThat(textContent.trim()).contains("The first item was \"THIS IS ITEM1\" and this is the second line");
    }
    
    @Test 
    public void readFileAsList() throws TestBundleResourceException, IOException {
    	InputStream is = resources.retrieveFile("/resources/textFiles/list.txt");
    	List<String> listContent = artifacts.getBundleResources(this.getClass()).streamAsList(is);
    	logger.info("Received the following from the text file: " + listContent);
    	
    	assertThat(listContent.get(0)).isEqualTo("List item number 1"); 
        assertThat(listContent.get(1)).isEqualTo("List item number 2");
        assertThat(listContent.get(2)).isEqualTo("List item number 3");
    }
 
  
    @Test
    public void retrieveDirectoryContentsTest() throws Exception {
    	Map<String, InputStream> contents = resources.retrieveDirectoryContents("/resources/textFiles");

    	logger.info("Received the following from the directory: " + contents);
    	
    	assertThat(contents).containsKey("resources/textFiles/list.txt"); 
    	assertThat(contents).containsKey("resources/textFiles/test1.txt");    	
    }
    
    @Test
    public void retrieveEmptyDirectoryContentsTest() throws Exception {
    	Map<String, InputStream> contents = resources.retrieveDirectoryContents("/resources/emptyDirectory");

    	logger.info("Received the following from the directory: " + contents);
    	
    	assertThat(contents).isEmpty();
    }
    
    @Test
    public void retrieveNestedirectoryContentsTest() throws Exception {
    	Map<String, InputStream> contents = resources.retrieveDirectoryContents("/resources/directory");

    	logger.info("Received the following from the directory: " + contents);
    	
    	assertThat(contents).containsKey("resources/directory/nestedDirectory/nestedFile.txt");
    }
    
    @Test
    public void retrieveSkeletonDirectoryContentsTest() throws Exception {    	
    	Map<String, InputStream> contents = resources.retrieveSkeletonDirectoryContents("/resources/skeletons", buildHashMap(), 0);

    	logger.info("Received the following from the directory: " + contents);
    	
    	//Asserts that directory contains correct files
    	assertThat(contents).containsKey("resources/skeletons/test1.skel");
    	assertThat(contents).containsKey("resources/skeletons/test2.skel");
        
    	int i = 1;
    	//Iterates through the directory contents
    	for (Map.Entry<String, InputStream> entry : contents.entrySet()) {
    		//Switch checks i and uses case associated, initially set to 1
    		switch(i)
    		{
    		//For the first inputStream stored in Map, stream as a string and assert that variables are correctly substituted
    			case 1:
    		    	String textContent = resources.streamAsString(entry.getValue());
    	    		logger.info("Received the following from the file: " + textContent);	

    				assertThat(textContent).contains("ITEM NUMBER THREE");    		    	
    				break;
    	    //For the second inputStream stored in Map, stream as a string and assert that variables are correctly substituted
    			case 2:
    		    	String textContent2 = resources.streamAsString(entry.getValue()); 
    	    		logger.info("Received the following from the file: " + textContent2);	
    	    		
    				assertThat(textContent2).contains("The third parameter is ITEM NUMBER THREE\n" + 
    						"The first item was \"THIS IS ITEM1\" and this is the second line ");
    				break;
    		//If there are more entries than the two expected, throw error 
    			default:
    				throw new Exception("Invalid content in directory files");		
    		}	
    		// increments i so that case 2 is used on the next loop
    		i++;
    	}
    }
    
    @Test
    public void retrieveJarTest() throws Exception {

    	InputStream is = resources.retrieveJar("HelloGalasa", "0.0.1", "/resources/jars/");
    	
    	String jarContent = resources.streamAsString(is);
    	//If the class file is found then the jar has been retrieved successfully
    	assertThat(jarContent).contains("HelloGalasa.class");
    }
    
    @Test
    public void retrieveJarTestWithVersionCompare() throws Exception {

    	InputStream is = resources.retrieveJar("dev.galasa", "0.15.0.202105120649", "/resources/jars/");
    	    	
    	String jarContent = resources.streamAsString(is);
    	
    	//If this string is found within the contents then the jar has been retrieved successfully
    	assertThat(jarContent).contains("dev/galasa");
    }
    
    @Test
    public void retrieveJarTestWithinRange() throws Exception {

    	InputStream is = resources.retrieveJar("dev.galasa", "(0.14.0.202105120649,0.16.0.202105120649)", "/resources/jars/");
    	    	
    	String jarContent = resources.streamAsString(is);
    	
    	//If this string is found within the contents then the jar has been retrieved successfully
    	assertThat(jarContent).contains("dev/galasa");
    }
    
   
    @Test
    public void retrieveJarTestNoVersion() throws Exception {

    	InputStream is = resources.retrieveJar("HelloGalasa", "", "/resources/jars/");
    	
    	String jarContent = resources.streamAsString(is);
    	
    	assertThat(jarContent).contains("HelloGalasa.class");
    }
    
    @Test
    public void retrieveJarTestDifferSeperator() throws Exception {

    	InputStream is = resources.retrieveJar("HelloGalasa", "0.0.1", "/resources/jarsHyphenVersion/");
    	
    	String jarContent = resources.streamAsString(is);
    	
    	assertThat(jarContent).contains("HelloGalasa.class");	
    }
    

    
    @Test
    public void zipDirectoryTestGZIP() throws TestBundleResourceException, IOException {
    	InputStream is = resources.zipDirectoryContents("/resources/zipFiles/", buildHashMap(), "US-ASCII", true);  	    	    	
    	GZIPInputStream gzip = new GZIPInputStream(is);
    	    	
    	//Decode zip using "US-ASCII"
    	String text = "Decoded Zip: ";
    	int data = gzip.read();
    	while(data != -1){
    		char ch = (char) data;
    		text = text + ch;
    	    data = gzip.read();
    	}    	
    
    	assertThat(text).contains("zipTest.txt");   	
    }
    
    @Test
    public void zipDirectoryTest() throws TestBundleResourceException, IOException {
    	InputStream is = resources.zipDirectoryContents("/resources/zipFiles/", buildHashMap(), "US-ASCII", false);  	    	    	
    	
    	//Decode zip using "US-ASCII"
    	String text = "Decoded Zip: ";
    	int data = is.read();
    	while(data != -1){
    		char ch = (char) data;
    		text = text + ch;
    	    data = is.read();
    	}    	
    	    
    	assertThat(text).contains("zipTest.txt");  	
    }
    
    @Test
    public void zipDirectoryTestNoEncoding() throws TestBundleResourceException, IOException {
    	InputStream is = resources.zipDirectoryContents("/resources/zipFiles/", buildHashMap(), null, false);  	    	    	
    	
    	//Read zip using no encoding"
    	String text = "Zip: ";
    	int data = is.read();
    	while(data != -1){
    		char ch = (char) data;
    		text = text + ch;
    	    data = is.read();
    	}    	
    	    
    	assertThat(text).contains("zipTest.txt");  	
    }
      

}