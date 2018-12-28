# WatchDir

WatchDir is lightweight library for monitoring file system changes. The library works asynchronously.


## Getting Started

Add the dependency in your pom.xml 
```
<dependency>
  <groupId>net.n2oapp.watchdir</groupId>  
  <artifactId>watchdir</artifactId>
  <version>2.0</version>
</dependency>
```

Create listener class
```
FileChangeListener listener = new FileChangeListener() {
    @Override
        public void fileModified(Path file) {
        System.out.format("modified: %s\n", file);
    }

    @Override
    public void fileCreated(Path file) {
        System.out.format("created: %s\n", file);
    }

    @Override
    public void fileDeleted(Path file) {
        System.out.format("deleted: %s\n", file);
    }
}
```

Run and catch the events
```java
WatchDir watchDir = new WatchDir();
watchDir.setListener(listener);
watchDir.addPath("C:\temp");
watchDir.start();
...
watchDir.stop();
```
