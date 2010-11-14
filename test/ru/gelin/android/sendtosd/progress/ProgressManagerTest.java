package ru.gelin.android.sendtosd.progress;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class ProgressManagerTest {

    ProgressManager manager;
    
    @Before
    public void setUp() {
        manager = new ProgressManager();
    }
    
    static class TestFile implements File {
        
        String name;
        long size;
        
        TestFile(String name, long size) {
            this.name = name;
            this.size = size;
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public long getSize() {
            return this.size;
        }

        @Override
        public void setProgress(Progress progress) {
        }

    }
    
    @Test
    public void testSetFiles() {
        assertEquals(1, manager.files);
        assertEquals(-1, manager.file);
        manager.nextFile(new TestFile("0", File.UNKNOWN_SIZE));
        assertEquals(0, manager.file);
        manager.nextFile(new TestFile("1", File.UNKNOWN_SIZE));
        assertEquals(1, manager.file);
        manager.setFiles(5);
        assertEquals(5, manager.files);
        assertEquals(-1, manager.file);
    }

    @Test
    public void testNextFile() {
        manager.setFiles(2);
        assertEquals(2, manager.files);
        assertEquals(-1, manager.file);
        manager.nextFile(new TestFile("0", 2048));
        assertEquals(0, manager.file);
        assertEquals(0, manager.processed);
        manager.processBytes(1024);
        assertEquals(1024, manager.processed);
        manager.nextFile(new TestFile("1", File.UNKNOWN_SIZE));
        assertEquals(1, manager.file);
        assertEquals(0, manager.processed);
        manager.nextFile(new TestFile("2", File.UNKNOWN_SIZE));
        assertEquals(2, manager.file);
        manager.nextFile(null);
        assertEquals(2, manager.file);
    }

    @Test
    public void testProcessBytes() {
        manager.setFiles(2);
        assertEquals(2, manager.files);
        assertEquals(-1, manager.file);
        manager.nextFile(new TestFile("0", 2048));
        assertEquals(0, manager.file);
        assertEquals(0, manager.processed);
        manager.processBytes(1024);
        assertEquals(0, manager.file);
        assertEquals(1024, manager.processed);
        manager.processBytes(1024);
        assertEquals(0, manager.file);
        assertEquals(2048, manager.processed);
        manager.processBytes(1024);
        assertEquals(0, manager.file);
        assertEquals(2048, manager.processed);
    }
    
    @Test
    public void testGetSizeUnit() {
        manager.setFiles(3);
        manager.nextFile(new TestFile("b", 1023));
        assertEquals(SizeUnit.BYTE, manager.getSizeUnit());
        manager.nextFile(new TestFile("Kb", 1023 * 1024));
        assertEquals(SizeUnit.KILOBYTE, manager.getSizeUnit());
        manager.nextFile(new TestFile("Mb", 1024 * 1024 + 1));
        assertEquals(SizeUnit.MEGABYTE, manager.getSizeUnit());
        manager.nextFile(null);
        assertEquals(SizeUnit.MEGABYTE, manager.getSizeUnit());
    }
    
    public void testGetSizeInUnits() {
        manager.setFiles(3);
        manager.nextFile(new TestFile("b", 1023));
        assertEquals(1023, manager.getSizeInUnits(), 0.1);
        manager.nextFile(new TestFile("Kb", 1023 * 1024));
        assertEquals(1023, manager.getSizeInUnits(), 0.1);
        manager.nextFile(new TestFile("Mb", 1024 * 1024 + 1));
        assertEquals(1, manager.getSizeInUnits(), 0.1);
        manager.nextFile(null);
        assertEquals(1, manager.getSizeInUnits(), 0.1);
    }
    
    public void testGetProgressInUnits() {
        manager.setFiles(3);
        manager.nextFile(new TestFile("b", 1023));
        manager.processBytes(128);
        assertEquals(128, manager.getProgressInUnits(), 0.1);
        manager.nextFile(new TestFile("Kb", 1023 * 1024));
        manager.processBytes(128 * 1024 + 512);
        assertEquals(128.5, manager.getProgressInUnits(), 0.1);
        manager.nextFile(new TestFile("Mb", 1024 * 1024 + 1));
        manager.processBytes(128 * 1024 * 1024 + 512);
        assertEquals(128.5, manager.getProgressInUnits(), 0.1);
        manager.nextFile(null);
        assertEquals(128.5, manager.getProgressInUnits(), 0.1);
    }

}
