/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serializableApp.objects;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import serializableApp.DimensionUnits.UnitsManagerPro;



/**
 *
 * @author GermanSR
 */
public abstract class Project extends SerializableObject{
    
    public static final String OBJECT_TYPE="Project"; 
    public static final String PROPNAME_UNITS_MANAGER="UnitsManager";
    
    HashMap<String,PropertyObjectList> allProjectList;
    
    public Project(String id){
        super(OBJECT_TYPE,id);
        UnitsManagerPro unitsMng = new UnitsManagerPro();
        addProperty(new PropertyObject(PROPNAME_UNITS_MANAGER,unitsMng));     
    }
    
    public abstract SerializableObject getDefaultObject(String... params);
    
    
    public void addObjectToList(SerializableObject obj){
        PropertyObjectList list = getListByType(obj.getObjectType());
        if(list!=null){ 
            list.addObjectAnyID(obj);
        }
    }
    

    public void addObjectToProject(SerializableObject obj){
        addProperty(new PropertyObject(obj.getID(),obj));
    }   
    
    public SerializableObject getObject(String id){
        Property prop = getProperty(id);
        if(prop!=null){
            if( prop.getPropertyType()==Property.TYPE_OBJECT){
                PropertyObject objProp = (PropertyObject)prop;
                return objProp.getObject();
            }
        }
        return null;
    }
    
    
    public UnitsManagerPro getUnitsManager(){
        PropertyObject prop = (PropertyObject)getProperty(PROPNAME_UNITS_MANAGER);
        return (UnitsManagerPro)prop.getObject();
    }
       
    public SerializableObject getObjectFromListByID(String id, String list){
           return getList(list).getObjectByID(id);
    }

    public PropertyObjectList getList(String name){
        Property prop = getProperty(name);
        if(prop!=null){ 
            if(getProperty(name).getPropertyType()==Property.TYPE_OBJECTLIST){
                PropertyObjectList list =(PropertyObjectList) getProperty(name);
                return list;
            }else{
               return null; 
            }
        }else{
            return null;
        }
    }

    public PropertyObjectList getListByType(String objType){        
        for(PropertyObjectList prop:getObjectListProperties()){  
            if(prop.getObjectType().equals(objType)){
                return prop;

            }
        }
        return null;
    }

    public void initializeReferenceProperties(){
        intializeReferencedObjects(this);
    }
  
    public void changeRef(String oldID, String newID, String listName){
        System.out.println("debug: CHANGING REFERENCE "+oldID+" FOR "+newID +"recursive operation!");
        changeReference(oldID,newID,listName,this);
        updateReferenceProperties();
    }
    
    public Project deserializeProject(String file) {
        
        try {

            File fXmlFile = new File(file);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            doc.getDocumentElement().normalize();

            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

            NodeList nList = doc.getElementsByTagName("Object");
            Node nNode = nList.item(0);
            //project
            Element eElement = (Element) nNode;
            //properties
            //NodeList propertiesNodeList = eElement.getChildNodes();
            
            Project proy = (Project)createObjectFromXML(eElement);
            
            return proy;
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return null;
        
    }
    
    
    public SerializableObject createObjectFromXML(Element eElement){
        if(eElement.getNodeName().equals("Object")){
            String objType = eElement.getAttribute(SerializableObject.PROPNAME_OBJTYPE);
            String objID = eElement.getAttribute(SerializableObject.PROPNAME_ID);
            //System.out.println("Object: "+objType+"   ID: "+objID);
            //CREATE THE OBJECT
            //THE ID PARAM IS ONLY NEEDED IN CERTAIN OCCASIONS 
            SerializableObject obj = getDefaultObject(objType,objID); 
            //sometimes returned obje have empty id
            obj.setID(objID);
            //System.out.println("obj created: "+obj.getID());
           //ACCES OBJECT PROPERTIES
            NodeList propertiesNodeList = eElement.getChildNodes();
            //READ THE PROPERTY VALES FROM THE XML
            for (int temp = 0; temp < propertiesNodeList.getLength(); temp++) {
                Node n = propertiesNodeList.item(temp);
                Element e = (Element)n;
                String propName = e.getAttribute("Name");
                //String propType = e.getAttribute("Type"); 
                //System.out.println("propName: "+propName);
                Property prop = obj.getProperty(propName);
                prop.deserializeXML(this,e);
               // obj.getProperty(propName).deserializeXML(e);
            }
            return obj;      
        }else{
            System.out.println("xml read error: node is not an object");
            return null;
        }  
    }
    
    /*
    public SerializableObject createObject(Element eElement){
        if(eElement.getNodeName().equals("Object")){
            String objType = eElement.getAttribute(SerializableObject.PROPNAME_OBJTYPE);
            SerializableObject obj = getDefaultObject(objType);   
            //properties
            NodeList propertiesNodeList = eElement.getChildNodes();
            for (int temp = 0; temp < propertiesNodeList.getLength(); temp++) {
                Node n = propertiesNodeList.item(temp);
                Element e = (Element)n;
                String propName = e.getAttribute("Name");
                String propType = e.getAttribute("Type"); 
                Property prop = obj.getProperty(propName);
                switch(Integer.parseInt(propType)){
                    case Property.TYPE_BOOLEAN:
                        prop.castToPropertyBoolean().deserializeXML(e);
                    break; 
                    case Property.TYPE_DIMENSION:
                        prop.castoToPropertyDimension().deserializeXML(e);
                    break; 
                    case Property.TYPE_DOUBLE:
                        prop.castToPropertyDouble().deserializeXML(e);
                    break; 
                    case Property.TYPE_INTEGER:
                        prop.castToPropertyInteger().deserializeXML(e);
                    break; 
                    case Property.TYPE_OBJECT:
                        prop.castoToPropertyObject().deserializeXML(e);
                    break; 
                    case Property.TYPE_OBJECTLIST:
                        prop.castoToPropertyObjectList().deserializeXML(e);
                    break; 
                    case Property.TYPE_REFERENCE:
                        prop.castToPropertyRef().deserializeXML(e);
                    break; 
                    case Property.TYPE_STRING:
                        prop.castoToPropertyString().deserializeXML(e);
                    break; 
                    case Property.TYPE_STRINGID:
                        prop.castoToPropertyStringID().deserializeXML(e);
                    break; 
                }
                obj.getProperty(propName).deserializeXML(e);
            }
            return obj;
            
        }else{
            System.out.println("xml read error: node is not an object");
            return null;
        }  
    }*/
    
    
    
    public void serializeProject(String xml){
 
        Document dom;
        Element e = null;

        // instance of a DocumentBuilderFactory
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            // use factory to get an instance of document builder
            DocumentBuilder db = dbf.newDocumentBuilder();
            // create instance of DOM
            dom = db.newDocument();
            dom.appendChild(serializeObject(dom));

            try {
                Transformer tr = TransformerFactory.newInstance().newTransformer();
                tr.setOutputProperty(OutputKeys.METHOD, "xml");
                tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                
                // send DOM to file
                if(!xml.endsWith("xml")){
                    xml+=".xml";
                }
                
                tr.transform(new DOMSource(dom),new StreamResult(xml));

            } catch (TransformerException te) {
                System.out.println(te.getMessage());
            }

        } catch (ParserConfigurationException pce) {
            System.out.println("UsersXML: Error trying to instantiate DocumentBuilder " + pce);
        } 
        

    }
    
    public abstract void createDefaultProject();
 
}
