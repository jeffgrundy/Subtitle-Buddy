package io.github.vincemann.subtitleBuddy.config.propertiesFile;

import io.github.vincemann.subtitleBuddy.config.propertiesFile.ApachePropertiesFile;
import io.github.vincemann.subtitleBuddy.config.propertiesFile.PropertiesFile;
import io.github.vincemann.subtitleBuddy.config.propertiesFile.PropertyNotFoundException;
import io.github.vincemann.subtitleBuddy.TestFiles;
import lombok.Getter;
import org.apache.commons.configuration.ConfigurationException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

@Getter
public abstract class AbstractApacheConfigFileTest {

    private PropertiesFile emptyTestPropertiesFile;



    @Before
    public void init() throws ConfigurationException {
        this.emptyTestPropertiesFile = new ApachePropertiesFile(new File(TestFiles.EMPTY_TEST_CONFIG_FILE_PATH));
        cleanUp();
    }


    @After
    public void cleanUp() throws ConfigurationException {
        emptyTestPropertiesFile.refresh();
        emptyTestPropertiesFile.clear();
        Assert.assertTrue(emptyTestPropertiesFile.isEmpty());
        emptyTestPropertiesFile.save();
    }

    protected boolean isPropertyInFile(String key, String value) throws FileNotFoundException, PropertyNotFoundException {
        Scanner scanner = new Scanner(emptyTestPropertiesFile.getFile());
        String currentLine = "";
        while (scanner.hasNextLine()){
            currentLine = scanner.nextLine();
            if(currentLine.contains(key)){
                if(currentLine.contains(value)){
                    scanner.close();
                    return true;
                }else {
                    scanner.close();
                    return false;
                }
            }
        }
        scanner.close();
        return false;
    }
}
