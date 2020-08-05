
package ezfemapp.services;


import java.io.File;

import ezfemapp.blockProject.BlockProject;
import ezfemapp.main.GUImanager;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import serializableApp.objects.Project;
import android.os.Environment;
import ezfemapp.gui.mdcomponents.SelectFileBox;
import ezfemapp.gui.mdcomponents.InputBoxWithOptions;

public class BasicAppAndroid extends BasicApp {
    
//	public BasicAppDesktop(Stage primaryStage){
//        BasicApp.platform = PLATFORM_DESKTOP;
//        initializeGUI(primaryStage,false);
//    }
	
    String local = "/ezFEM";
    
    File extFolder;
	
	@Override
	public void initGUI(Stage primaryStage) {
		BasicApp.platform = PLATFORM_MOBILE;
		initializeGUI(primaryStage,true);
        extFolder = Environment.getExternalStorageDirectory();
        checkLocalFolderAndCreate();
	}
	
	@Override
    public boolean openProjectFromLocalFile() {
        
        SelectFileBox listPopup = new SelectFileBox(getGUImanager(),"Open File",getLocalFiles(),true);
        
        File filePath = new File(extFolder.getAbsoluteFile()+local);
        listPopup.setWorkingFolder(filePath);
        listPopup.show();
        
        listPopup.getPopUp().setOnHidden((event)->{
            if(listPopup.getResult()&&!listPopup.getSelectedItems().isEmpty()){  
                String result = listPopup.getSelectedItems().get(0);
                if(openLocalFile(result)){
                   getGUImanager().showNotification("File opened! "+result, GUImanager.NOTIFICATION_SUCCESS, 250,25);   
                }else{
                   getGUImanager().showNotification("Could not open file! <Error9>", GUImanager.NOTIFICATION_ERROR, 250,25);
                } 
            }else{
                
            }
        });
        
        return true;
        
    }

    @Override
    public boolean saveProjectToLocalFile(){
        if(getProjectPath().isEmpty()){
            return savelocal();
        }else{
            getBlocks().serializeProject(getProjectPath());
            getGUImanager().showNotification("File saved!", GUImanager.NOTIFICATION_SUCCESS, 250, 25);
            getGUImanager().getSidePanel().closePanel(); 
           
        }  
        return true;
    }

    @Override
    public boolean saveAs() {
       return savelocal();
     
    }
    
    private boolean savelocal(){
        InputBoxWithOptions popup = new InputBoxWithOptions(getGUImanager(), "Save File","Name: ",getLocalFiles());
        popup.show();
        
        popup.getPopUp().setOnHidden((event)->{
            if(popup.getResult()){
                saveToLocalFolder(popup.getTextResult());
            }else{
                //getGUImanager().showNotification("Cancel saving file", GUImanager.NOTIFICATION_ERROR, 300,25);
            }
        }); 
        
        return true;
    }

    @Override
    public boolean saveProjectToCurrentPath() {
        return true;
    }
    
    public String[] getLocalFiles(){        
        //File filePath = Services.get(StorageService.class).flatMap(s -> s.getPublicStorage(localFolder)).orElseThrow(() -> new RuntimeException("Folder not available"));

        File dir = new File(extFolder.getAbsoluteFile()+local);
        File[] directoryListing = dir.listFiles();
        String[] files = new String[directoryListing.length]; 
        int count=0;
        if (directoryListing != null) {
            for (File child : directoryListing) {
               files[count++] = child.getName();
            }
        } else {
          // Handle the case where dir is not really a directory.
          // Checking dir.isDirectory() above would not be sufficient
          // to avoid race conditions with another process that deletes
          // directories.
        }
        return files;
    }
    
    
    private boolean openLocalFile(String name){
        //File filePath = Services.get(StorageService.class).flatMap(s -> s.getPublicStorage(localFolder+"/"+name)).orElseThrow(() -> new RuntimeException("Folder not available"));
        File filePath = new File(extFolder.getAbsoluteFile()+local+"/"+name);
        if(filePath.exists()){
           Project proy = getBlocks().deserializeProject(filePath.getAbsolutePath());
           if(proy!=null){
               if(proy instanceof BlockProject){
                    setProjectPath(filePath.getAbsolutePath());
                    setProjectDirectory(filePath.getParent());
                    BlockProject blocks = (BlockProject)proy;
                    setProject(blocks);
               }
           }
        }else{

            return false;
        }
        return true;
    }
    
    private boolean checkLocalFolderAndCreate(){

        String path = extFolder.getAbsolutePath()+local;
        
        if(extFolder.exists()){
            
            File checkAppFolder = new File(path);
            
            if(!checkAppFolder.exists()){
                File createAppFolder = new File(extFolder,local);
                createAppFolder.mkdir();
            }
        }else{
           return false; 
        }  
        return true;
    }
    
    private boolean saveToLocalFolder(String fileName){
        File appFolder = new File(extFolder.getAbsoluteFile()+local);
        if(appFolder.exists()){
           String fullFilePath = (appFolder.getAbsolutePath()+"/"+fileName);
           getBlocks().serializeProject(fullFilePath);
           getGUImanager().showNotification("File saved!", GUImanager.NOTIFICATION_SUCCESS, 250, 25);
           setProjectPath(fullFilePath);
           setProjectDirectory(appFolder.getAbsolutePath());
           getGUImanager().getSidePanel().closePanel();
           return true;     
        }else{
            getGUImanager().showNotification("Could not save File! Error:13", GUImanager.NOTIFICATION_ERROR, 250, 25);
            return false;
        }
    }
}
