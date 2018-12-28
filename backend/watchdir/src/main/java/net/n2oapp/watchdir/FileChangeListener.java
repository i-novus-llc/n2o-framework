package net.n2oapp.watchdir;

import java.nio.file.Path;


public interface FileChangeListener {
  
	void fileModified(Path file);
	
	void fileCreated(Path file);
	
	void fileDeleted(Path file);
	
}
