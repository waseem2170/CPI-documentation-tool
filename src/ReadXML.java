import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;  
import org.w3c.dom.Document;  
import org.w3c.dom.NodeList;

import net.sourceforge.plantuml.SourceStringReader;

import org.w3c.dom.Node;  
import org.w3c.dom.Element;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.w3c.dom.*;
import org.xml.sax.*;
import javax.xml.parsers.*;
import javax.xml.xpath.*;
import java.io.*;

	
public class ReadXML 
{
	static MakePng png = new MakePng();
	
	public static void readXmlFrom(String location)
	{
		List<XmlObject> objectList = new ArrayList<XmlObject>();
			
		try   
		{  
		//creating a constructor of file class and parsing an XML file  
			File file = new File(location);  
		//an instance of factory that gives a document builder
        
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();  
		//an instance of builder to parse the specified xml file  
		DocumentBuilder db = dbf.newDocumentBuilder();  
		Document doc = db.parse(file);  
		
		doc.getDocumentElement().normalize();  
		//System.out.println("Root element: " + doc.getDocumentElement().getNodeName());  
		
		NodeList nodeList;
			
		//Read callActivities (content modifiers, mappings,...)
		nodeList = doc.getElementsByTagName("bpmn2:callActivity");
		printMainNodeDetails(doc, nodeList, objectList);

		//Read service tasks
		nodeList = doc.getElementsByTagName("bpmn2:serviceTask");  	
		printMainNodeDetails(doc, nodeList, objectList);
		
		//Read routers
		nodeList = doc.getElementsByTagName("bpmn2:exclusiveGateway");  	
		printMainNodeDetails(doc, nodeList, objectList);
			
			System.out.println("-------------------------------------------------------------------------------------------------------------");
			System.out.println("Object List\n");
			for (int itr = 0; itr < objectList.size(); itr++)   
			{  
				System.out.println("NodeName :" + objectList.get(itr).getNodeName());
				System.out.println("Id :" + objectList.get(itr).getId());
				System.out.println("incomingSequence :" + objectList.get(itr).getIncomingSequence().toString());
				System.out.println("outgoingSequence :" + objectList.get(itr).getOutgoingSequence().toString());			
				System.out.println("Level :" + objectList.get(itr).getProcessLevel());			

			System.out.println("###################################");

			}
			System.out.println("Count: " + objectList.size());
			System.out.println("-------------------------------------------------------------------------------------------------------------");

			List<XmlObject> objectListArranged = arrangeXmlObjects(objectList);
			System.out.println("Total objects: " + objectListArranged.size());
			for (XmlObject xmlObject : objectListArranged) {
				System.out.println("Arranged: " + xmlObject);
			}
		}   
		catch (Exception e)   
		{  
		e.printStackTrace();  
		}  
		
		png.generateImageTo("C:\\Users\\malikw\\Downloads\\image.png");

    }
	
	private static List<XmlObject> arrangeXmlObjects(List<XmlObject> objectList)
	{
		List<XmlObject> objectListArranged = new ArrayList<XmlObject>();
	
			for (int itr = 0; itr < objectList.size(); itr++)   
			{  
				if(objectList.get(itr).getIncomingSequenceByIndex(0).getSourceRef().contains("StartEvent") && objectList.get(itr).getProcessLevel().getProcessLevel() == "IntegrationProcess")
				{
					objectListArranged.add(0, objectList.get(itr));
					
				}	
			}
				
		do {
			for (int itr = 0; itr < objectList.size(); itr++)   
			{  
				if(objectListArranged.get(0).getoutgoingSequenceByIndex(0).getTargetRef() == objectList.get(itr).getId())
				{
					objectListArranged.add(objectList.get(itr));
				}
			}
		} while (objectList.size() != objectListArranged.size());
		
		return objectListArranged;
	}
	
	private static void printMainNodeDetails(Document doc, NodeList nodeList, List<XmlObject> objectList)
	{
		XmlObject object;
		for (int itr = 0; itr < nodeList.getLength(); itr++)   
		{  
		Node node = nodeList.item(itr);  
		object = new XmlObject();
		
		//set Id
		object.setId(node.getAttributes().getNamedItem("id").getNodeValue());
		//set node name
		object.setNodeName(node.getAttributes().getNamedItem("name").getNodeValue());
			
		png.addElement(":" + node.getAttributes().getNamedItem("name").getNodeValue());
		System.out.println("Node Name :" + node.getNodeName() + " | Name: "+ node.getAttributes().getNamedItem("name").getNodeValue() + " | ID: "+ node.getAttributes().getNamedItem("id").getNodeValue());
		
		getSubNodes(doc, node, object);
		checkProcess(doc, node, object);
		System.out.println("-------------------------------------------------------------------------------------------------------------");
		objectList.add(object);
		}  
	}
	
	private static void getSubNodes(Document doc, Node node, XmlObject object)
	{
		SequenceObject sequence = null;

		if (node.getNodeType() == Node.ELEMENT_NODE)   
		{  
			Element eElement = (Element) node;  
			
			List<String> incomingSeq = getSequences(eElement, "bpmn2:incoming");
			List<String> outcomingSeq = getSequences(eElement, "bpmn2:outgoing");
				
			for (String string : incomingSeq) {
				System.out.println("incoming from ARRAY : "+ string); 
			}
			
			for (String string : outcomingSeq) {
				System.out.println("outgoing from ARRAY : " + string); 
			}
		
			NodeList subnodeList = doc.getElementsByTagName("bpmn2:sequenceFlow");  
			for (int subitr = 0; subitr < subnodeList.getLength(); subitr++)   
			{  
				//Sequence object initial
				sequence = new SequenceObject();
				
				Node subnode = subnodeList.item(subitr);  
				if (subnode.getNodeType() == Node.ELEMENT_NODE) 
				{
					String id = subnode.getAttributes().getNamedItem("id").getNodeValue();
					//Some sequences have a name and some don't
					String sequenceName = null;
					
					try {
						sequenceName = subnode.getAttributes().getNamedItem("name").getNodeValue();

					} catch (Exception e) {
						// TODO: handle exception
					}
					
					//all incoming sequences
					for (String seqIn : incomingSeq) {
						if(seqIn.equals(id))
						{
							System.out.println("id:" +  id  + "|Name: " + sequenceName + "| sourceRef: "+ subnode.getAttributes().getNamedItem("sourceRef").getNodeValue() + " | targetRef: "+ subnode.getAttributes().getNamedItem("targetRef").getNodeValue());
							sequence.setId(id);
							sequence.setSourceRef(subnode.getAttributes().getNamedItem("sourceRef").getNodeValue());
							sequence.setTargetRef(subnode.getAttributes().getNamedItem("targetRef").getNodeValue());
							sequence.setName(sequenceName);
							object.addIncomingSequence(sequence);
						}
					}
					
					//all outgoingsequences
					for (String seqOut : outcomingSeq) {
						if(seqOut.equals(id))
						{
							System.out.println("id:" +  id  + "|Name: " + sequenceName + "| sourceRef: "+ subnode.getAttributes().getNamedItem("sourceRef").getNodeValue() + " | targetRef: "+ subnode.getAttributes().getNamedItem("targetRef").getNodeValue());
							sequence.setId(id);
							sequence.setSourceRef(subnode.getAttributes().getNamedItem("sourceRef").getNodeValue());
							sequence.setTargetRef(subnode.getAttributes().getNamedItem("targetRef").getNodeValue());
							sequence.setName(sequenceName);
							//System.out.println("Ïncoming: " + sequence.toString());
							object.addOutgoingSequence(sequence);
						}
					}	
				}			
			}
		}	
	}
	
	//Get alle sequences from XML
	private static List<String> getSequences(Element eElement, String type)
	{
		List<String> seqList = new ArrayList<>();
		int counter = 0;
		//Loop while the next sequence is not null and add the not null ones in the array
		do {
			
			if(eElement.getElementsByTagName(type).item(counter) != null)
			{
				seqList.add(eElement.getElementsByTagName(type).item(counter).getTextContent());
			}
			counter++;
			
		} 
		while (seqList.size() == counter);
		return seqList;
	}	
	
	private static void checkProcess(Document doc, Node node,  XmlObject object)
	{	
		String parentString = null;
		Process processLevel = new Process();
			try {
				
				//Get parent from current node(mapping, request reply,...)
				Element eElement = (Element) node;  
				Node parent = eElement.getParentNode();
				
				//Get id attribuute of parent
				Element parentElement =  (Element) parent;
				parentString = parentElement.getElementsByTagName("value").item(2).getTextContent();
				if(parentString.contains("IntegrationProcess"))
				{
					System.out.println("Level: IntegrationProcess || Name: " + parentElement.getAttributes().getNamedItem("name").getNodeValue());
					processLevel.setProcessLevel("IntegrationProcess");
				}
				else
				{
					System.out.println("Level: LocalIntegration || Name: " + parentElement.getAttributes().getNamedItem("name").getNodeValue());
					processLevel.setProcessLevel("LocalIntegration");			
				}	
				
				processLevel.setProcessName(parentElement.getAttributes().getNamedItem("name").getNodeValue());
				processLevel.setProcessId(parentElement.getAttributes().getNamedItem("id").getNodeValue());
				object.setProcessLevel(processLevel);
	
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
			
		
	
	
	 
}  

