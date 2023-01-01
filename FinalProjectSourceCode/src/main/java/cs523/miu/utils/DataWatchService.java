package cs523.miu.utils;

import static java.nio.file.StandardWatchEventKinds.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;

import cs523.miu.kafka.KProducer;

public class DataWatchService {
	private static DataWatchService INSTANCE;
	
	private final WatchService watcher;
    private final Map<WatchKey, Path> keys;
 
    /**
     * Creates a WatchService and registers the given directory
     */
    DataWatchService(Path dir) throws IOException {
        this.watcher = FileSystems.getDefault().newWatchService();
        this.keys = new HashMap<WatchKey, Path>();
 
        walkAndRegisterDirectories(dir);
    }
    
    public static DataWatchService getInstance(Path dir) throws IOException {
		if (INSTANCE == null) {
			INSTANCE = new DataWatchService(dir);			
		}
		return INSTANCE;
	}
 
    /**
     * Register the given directory with the WatchService; This function will be called by FileVisitor
     */
    private void registerDirectory(Path dir) throws IOException {
        WatchKey key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
        keys.put(key, dir);
    }
 
    /**
     * Register the given directory, and all its sub-directories, with the WatchService.
     */
    private void walkAndRegisterDirectories(final Path start) throws IOException {
        // register directory and sub-directories
        Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                registerDirectory(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }
 
    /**
     * Process all events for keys queued to the watcher
     * @throws URISyntaxException 
     */
    void processEvents() throws URISyntaxException {
        while(true) {
 
            // wait for key to be signalled
            WatchKey key;
            try {
                key = watcher.take();
            } catch (InterruptedException x) {
                return;
            }
 
            Path dir = keys.get(key);
            if (dir == null) {
                System.err.println("WatchKey not recognized!!");
                continue;
            }
 
            for (WatchEvent<?> event : key.pollEvents()) {
                @SuppressWarnings("rawtypes")
                WatchEvent.Kind kind = event.kind();
 
                // Context for directory entry event is the file name of entry
                @SuppressWarnings("unchecked")
                Path name = ((WatchEvent<Path>)event).context();
                Path child = dir.resolve(name);
 
                // ignore modify & delete event
                if ((kind == ENTRY_MODIFY && event.count() > 1) && kind == ENTRY_DELETE)
                	continue;
                
                if (kind == ENTRY_CREATE || kind == ENTRY_MODIFY) {
                    System.out.format("%s: %s\n", event.kind().name(), child);
                	producer(child.toString());
                }
 
                // if directory is created, and watching recursively, then register it and its sub-directories
                if (kind == ENTRY_CREATE) {
                    try {
                        if (Files.isDirectory(child)) {
                            walkAndRegisterDirectories(child);
                        }
                    } catch (IOException e) {
                    	e.printStackTrace();
                    }                    
                }
            }
 
            // reset key and remove from set if directory no longer accessible
            boolean valid = key.reset();
            if (!valid) {
                keys.remove(key);
 
                // all directories are inaccessible
                if (keys.isEmpty()) {
                    break;
                }
            }
        }
    }
    
    private void producer(String filePath) throws URISyntaxException {        
		KProducer.publishMessages(filePath);
	    System.out.println("Producing job completed");
  	}   
 
    public static void main(String[] args) throws IOException, URISyntaxException {
    	String path = args.length > 0 ? args[0] : null;
    	if (path == null) {
    		path = System.getProperty("user.dir") + "/input";
    	}
        Path dir = Paths.get(path);
        DataWatchService.getInstance(dir).processEvents();
    }
}
