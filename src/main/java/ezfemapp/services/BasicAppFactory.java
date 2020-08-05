package ezfemapp.services;

import java.util.Iterator;
import java.util.ServiceLoader;
import java.util.logging.Logger;

import javafx.stage.Stage;

public class BasicAppFactory {
	
	private static final Logger LOG = Logger.getLogger(BasicAppFactory.class.getName());

    private static BasicAppFactory instance;
    public static synchronized BasicAppFactory getInstance() {
        if (instance == null) {
            instance = new BasicAppFactory();
        }
        return instance;
    }
    
    private final ServiceLoader<BasicApp> serviceLoader;
    private BasicApp provider;
    
    private BasicAppFactory() {
        serviceLoader = ServiceLoader.load(BasicApp.class);

        Iterator<BasicApp> iterator = serviceLoader.iterator();
        while (iterator.hasNext()) {
            if (provider == null) {
                provider = iterator.next();
                //LOG.info(format("Using PlatformProvider: %s", provider.getClass().getName()));
            } else {
                //LOG.info(format("This PlatformProvider is ignored: %s", iterator.next().getClass().getName()));
            }
        }

        if (provider == null) {
            LOG.severe("No PlatformProvider implementation could be found!");
        }
    }
    
    public void initGUI(Stage primaryStage) {
    	provider.initGUI(primaryStage);
    }
    public BasicApp getBasicApp() {
    	return provider == null ? null : provider;
    }
    public boolean openProjectFromLocalFile() {
    	return provider == null ? false : provider.openProjectFromLocalFile();
    }
    public boolean saveProjectToLocalFile() {
    	return provider == null ? false : provider.saveProjectToLocalFile();
    }
    public boolean saveAs() {
    	return provider == null ? false : provider.saveAs();
    }
    public boolean saveProjectToCurrentPath() {
    	return provider == null ? false : provider.saveProjectToCurrentPath();
    }
}
