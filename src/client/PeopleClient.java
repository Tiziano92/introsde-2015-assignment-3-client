package client;

import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.Holder;

import introsde.assignment.soap.*;
import introsde.assignment.soap.Person.HealthProfile;


public class PeopleClient{
    public static void main(String[] args) throws Exception {
        PeopleService service = new PeopleService();
        People people = service.getPeopleImplPort();
        Person p = people.readPerson(1);
        List<Person> pList = people.readPersonList();
        
        PrintWriter writer = new PrintWriter("client-server-xml.log", "UTF-8");
        
        writer.write("============================================\n");
        writer.write("Print server WSDL url\n");
        writer.write("============================================\n");
        writer.write(""+service.getWSDLDocumentLocation());
        
        
        System.out.println("============================================");
        System.out.println("Print server WSDL url");
        System.out.println("============================================");
        System.out.println(service.getWSDLDocumentLocation());
        
        
        writer.write("\n\n============================================\n");
        writer.write("Method #1 - readPersonList()\n");
        writer.write("============================================\n");
        
        System.out.println("============================================");
        System.out.println("Method #1 - readPersonList()");
        System.out.println("============================================");
        String buildXml = "<people>";
        
        for(int i = 0; i < pList.size(); i++){
        	buildXml += "<person><idPerson>"+pList.get(i).getIdPerson()+"</idPerson>";
        	buildXml +="<firstname>"+pList.get(i).getFirstname()+"</firstname>";
        	buildXml += "<lastname>"+pList.get(i).getLastname()+"</lastname>";
        	buildXml += "<birthdate>"+pList.get(i).getBirthdate()+"</birthdate>";
        	buildXml += "<email>"+pList.get(i).getEmail()+"</email>";
        	buildXml += "<username>"+pList.get(i).getUsername()+"</username>";
        	buildXml += "<healthProfile>";
        	
        	if(pList.get(i).getHealthProfile().getLifeStatus().size() > 0)
        	{
        		for(int k = 0; k < pList.get(i).getHealthProfile().getLifeStatus().size(); k++)
        		{
	        		buildXml += "<lifeStatus>";
	        		buildXml += "<measureType>";
	            	buildXml += "<idMeasureDef>"+pList.get(i).getHealthProfile().getLifeStatus().get(k).getMeasureType().getIdMeasureDef()+"</idMeasureDef>";
	            	buildXml += "<name>"+pList.get(i).getHealthProfile().getLifeStatus().get(k).getMeasureType().getName()+"</name>";
	            	buildXml += "</measureType>";
	            	buildXml += "<value>"+pList.get(i).getHealthProfile().getLifeStatus().get(k).getValue()+"</value>";
	            	buildXml += "</lifeStatus>";	
        		}
        	}
        	buildXml += "</healthProfile></person>";
        }
        
        buildXml += "</people>";
        
        int last_person_id = pList.get(pList.size()-1).getIdPerson();
        
        writer.write(prettyFormat(buildXml)+"\n");
        
        System.out.println(prettyFormat(buildXml));
        

        System.out.println("===================================================================");
        int firstIdPerson = pList.get(0).getIdPerson();
        System.out.println("Method #2 - readPerson(Long id) => readPerson("+firstIdPerson+")");
        System.out.println("===================================================================");
        
        writer.write("===================================================================\n");
        writer.write("Method #2 - readPerson(Long id) => readPerson("+firstIdPerson+")\n");
        writer.write("===================================================================\n");
        
        buildXml = getPersonFormat(people, firstIdPerson);
    	System.out.println(prettyFormat(buildXml));
    	
    	writer.write(prettyFormat(buildXml)+"\n");
    	
    	
    	System.out.println("===================================================================");
        System.out.println("Method #3 - updatePerson(Person p)");
        System.out.println("===================================================================");
        
        writer.write("===================================================================\n");
        writer.write("Method #3 - updatePerson(Person p)\n");
        writer.write("===================================================================\n");
        
        
        Person lastPerson = people.readPerson(last_person_id);
        String uuid = UUID.randomUUID().toString();
        String updateFirstName = "Chuck"+uuid;

        
        //Print the person before the update
        System.out.println("Person with id "+last_person_id+" before the update");
        buildXml = getPersonFormat(people, last_person_id);
    	System.out.println(prettyFormat(buildXml));
    	
    	writer.write("Person with id "+last_person_id+" before the update\n\n");
    	writer.write(prettyFormat(buildXml)+"\n");
    	
        
    	//Updating...
    	System.out.println("Updating person with id "+last_person_id+" with the new 'firstname' => "+updateFirstName);
        lastPerson.setFirstname(updateFirstName);
        Holder <Person> per = new Holder<Person>(lastPerson);
        people.updatePerson(per);
        
        writer.write("Updating person with id "+last_person_id+" with the new 'firstname' => "+updateFirstName+"\n");
        
        //Print the person after the update
        System.out.println("\nPerson with id "+last_person_id+" after the update");
        buildXml = getPersonFormat(people, last_person_id);
    	System.out.println(prettyFormat(buildXml));
    	
    	writer.write("\nPerson with id "+last_person_id+" after the update...\n\n");
    	writer.write(prettyFormat(buildXml)+"\n");
        
    	
    	System.out.println("===================================================================");
        System.out.println("Method #4 - createPerson(Person p)");
        System.out.println("===================================================================");

        System.out.println("People number before creating the new person: "+pList.size());
        
        writer.write("===================================================================\n");
        writer.write("Method #4 - createPerson(Person p)\n");
        writer.write("===================================================================\n");
        writer.write("People number before creating the new person: "+pList.size()+"\n");
        
        MeasureDefinition md = new MeasureDefinition();
        md.setIdMeasureDef(1);
        md.setName("weight");
        
        LifeStatus newLifeStatus = new LifeStatus();
        newLifeStatus.setValue("80");
        newLifeStatus.setMeasureType(md);
        
        Person newPerson = new Person();
        newPerson.setFirstname("Pippo");
        newPerson.setLastname("Rossi");
        newPerson.setBirthdate("1992-04-13");
        newPerson.setEmail("ciao@hotmail.it");
        newPerson.setUsername("Pi Ro");
        
        HealthProfile newCurrentHealth = new HealthProfile();
        newCurrentHealth.getLifeStatus().add(newLifeStatus);
        newPerson.setHealthProfile(newCurrentHealth);
        
        Holder <Person> newPersonCreate = new Holder<Person>(newPerson);

        //Create a new person
        people.createPerson(newPersonCreate);
        
        people = service.getPeopleImplPort();
        pList = people.readPersonList();
        
        //New created person
        System.out.println("New generated person ...");
        writer.write("New generated person ...\n\n");
        
        last_person_id = pList.get(pList.size()-1).getIdPerson();
        
        buildXml = getPersonFormat(people, last_person_id);
    	System.out.println(prettyFormat(buildXml));
    	
    	writer.write(prettyFormat(buildXml)+"\n");
        
        
        
        //Show up the new people list
        int newPersonNumber = pList.size();
        System.out.println("People number after creating the new person: "+newPersonNumber);
        System.out.println("Generating the new list of people ...\n");
        
        writer.write("People number after creating the new person: "+newPersonNumber+"\n");
        writer.write("Generating the new list of people ...\n\n");
        
        String buildXmlNew = "<people>";
        for(int i = 0; i < pList.size(); i++){
        	buildXmlNew += getPersonFormat(people, pList.get(i).getIdPerson());
        }
        buildXmlNew += "</people>";
        System.out.println(prettyFormat(buildXmlNew));
        
        writer.write(prettyFormat(buildXmlNew)+"\n");

        
        last_person_id = pList.get(pList.size()-1).getIdPerson();
        System.out.println("===================================================================");
        System.out.println("Method #5 - deletePerson(Long id) => deletePerson("+last_person_id+")");
        System.out.println("===================================================================");
        
        System.out.println("People number before deleting the new person: "+pList.size());
        System.out.println("The following person is going to be deleted from the database...\n");
        
        
        writer.write("===================================================================\n");
        writer.write("Method #5 - deletePerson(Long id) => deletePerson("+last_person_id+")\n");
        writer.write("===================================================================\n");
        writer.write("People number before deleting the new person: "+pList.size()+"\n");
        writer.write("The following person is going to be deleted from the database...\n\n");
        
        
        //Show up the person that is going to be deleted
        p = people.readPerson(last_person_id);
        buildXml = getPersonFormat(people, p.getIdPerson());
        System.out.println(prettyFormat(buildXml));
        
        writer.write(prettyFormat(buildXml)+"\n");
        
        //Deleting the person
        Holder <Integer> pers = new Holder<Integer>(last_person_id);
        people.deletePerson(pers);
        
        people = service.getPeopleImplPort();
        pList = people.readPersonList();
        
        if(pList.size() < newPersonNumber){
        	System.out.println("Person deleted successfully!!!");
        	writer.write("Person deleted successfully!!!\n");
        }
        else{
        	System.out.println("Person not deleted");
        	writer.write("Person not deleted\n");
        }
    	
    	System.out.println("People number after deleting the new person: "+pList.size());
    	
    	writer.write("People number after deleting the new person: "+pList.size()+"\n");
    	
    	//Showing up the new list of people
    	System.out.println("\nGenerating the new list of people...\n");
    	
    	writer.write("\nGenerating the new list of people...\n\n");
    	
    	buildXmlNew = "<people>";
        for(int i = 0; i < pList.size(); i++){
        	buildXmlNew += getPersonFormat(people, pList.get(i).getIdPerson());
        }
        buildXmlNew += "</people>";
        System.out.println(prettyFormat(buildXmlNew));
        
        writer.write(prettyFormat(buildXmlNew));
    	
        
        String measureType="weight";
        System.out.println("=====================================================================================================================");
        System.out.println("Method #6 - readPersonHistory(Long id, String measureType) => readPersonHistory("+firstIdPerson+", "+measureType+")");
        System.out.println("=====================================================================================================================");
        
        writer.write("\n===========================================================================================================================\n");
        writer.write("Method #6 - readPersonHistory(Long id, String measureType) => readPersonHistory("+firstIdPerson+", "+measureType+")\n");
        writer.write("===========================================================================================================================\n");
        
        List<HealthMeasureHistory> listMeasureHistory = people.readPersonHisotry(firstIdPerson, measureType);
        
        buildXml = getMeasureHistoryFormat(listMeasureHistory);
        System.out.println(prettyFormat(buildXml));
        
        writer.write(prettyFormat(buildXml));
        
        
        
        System.out.println("===================================================================");
        System.out.println("Method #7 - readMeasureTypes()");
        System.out.println("===================================================================");
        
        writer.write("\n===================================================================\n");
        writer.write("Method #7 - readMeasureTypes()\n");
        writer.write("===================================================================\n");
        
        CustomMeasureDefinition listMeasurements = people.readMeasureTypes();
        
        buildXml = "<measureTypeList>";
        for(int i = 0; i < listMeasurements.getMeasureType().size(); i++){
        	buildXml += "<measureType>"+listMeasurements.getMeasureType().get(i)+"</measureType>";
        }
        buildXml += "</measureTypeList>";
        System.out.println(prettyFormat(buildXml));
        
        writer.write(prettyFormat(buildXml)+"\n");
       
        
        int mid = 1;
        System.out.println("============================================================================================================================================");
        System.out.println("Method #8 - readPersonMeasure(Long id, String measureType, Long mid) => readPersonMeasure("+firstIdPerson+", "+measureType+ ", "+mid+")");
        System.out.println("============================================================================================================================================");
        
        writer.write("============================================================================================================================================\n");
        writer.write("Method #8 - readPersonMeasure(Long id, String measureType, Long mid) => readPersonMeasure("+firstIdPerson+", "+measureType+ ", "+mid+")\n");
        writer.write("============================================================================================================================================\n");
        
        HealthMeasureHistory hmh = people.readPersonMeasure(firstIdPerson, measureType, mid);
        
        buildXml = "<measureHistory>";
        buildXml += "<history><mid>"+hmh.getMid()+"</mid>";
        buildXml += "<value>"+hmh.getValue()+"</value>";
        buildXml +="<created>"+hmh.getCreated()+"</created>";
        /*buildXml += "<measureDefinition>";
        buildXml += "<idMeasureDef>"+hmh.getMeasureDefinition().getIdMeasureDef()+"</idMeasureDef>";
        buildXml += "<measureType>"+hmh.getMeasureDefinition().getMeasureType()+"</measureType>";
        buildXml += "<measureValueType>"+hmh.getMeasureDefinition().getMeasureValueType()+"</measureValueType>";*/
        buildXml += "</history>";
        buildXml += "</measureHistory>";
        System.out.println(prettyFormat(buildXml));
        
        writer.write(prettyFormat(buildXml)+"\n");
        
        
        System.out.println("============================================================================================================================================");
        System.out.println("Method #9 - savePersonMeasure(Long id, Measure m) => savePersonMeasure("+firstIdPerson+", "+measureType+")");
        System.out.println("============================================================================================================================================");
        
        writer.write("============================================================================================================================================\n");
        writer.write("Method #9 - savePersonMeasure(Long id, Measure m) => savePersonMeasure("+firstIdPerson+", "+measureType+")\n");
        writer.write("============================================================================================================================================\n");
        
        //Printing person before updating life status
        System.out.println("Number of measurements in the history (before updating the life status) for "+measureType+ " and id person "+firstIdPerson+" => "+listMeasureHistory.size());
        System.out.println("Printing information for person with id "+firstIdPerson+ " (before updating the life status) ...\n");
        
        writer.write("Number of measurements in the history (before updating the life status) for "+measureType+ " and id person "+firstIdPerson+" => "+listMeasureHistory.size()+"\n");
        writer.write("Printing information for person with id "+firstIdPerson+ " (before updating the life status) ...\n\n");
        
        buildXml = getPersonFormat(people, firstIdPerson);
    	System.out.println(prettyFormat(buildXml));
    	
    	writer.write(prettyFormat(buildXml)+"\n");
        
		LifeStatus lf = new LifeStatus();
		
		md = new MeasureDefinition();
        md.setIdMeasureDef(1);
        md.setName("weight");
		
		lf.setMeasureType(md);
		lf.setValue("89");

        //Create a new history for the person
        people.savePersonMeasure(firstIdPerson, lf);
        
    	//Printing person after updating life status
        people = service.getPeopleImplPort();
        System.out.println("Number of measurements in the history (after updating the life status) for "+measureType+ " and id person "+firstIdPerson+" => "+people.readPersonHisotry(firstIdPerson, measureType).size());
        System.out.println("Printing information for person with id "+firstIdPerson+ " (after updating the life status) ...");
        System.out.println("New updated value for "+measureType+ " => "+lf.getValue()+ "\n");
        
        writer.write("Number of measurements in the history (after updating the life status) for "+measureType+ " and id person "+firstIdPerson+" => "+people.readPersonHisotry(firstIdPerson, measureType).size()+"\n");
        writer.write("Printing information for person with id "+firstIdPerson+ " (after updating the life status) ...\n");
        writer.write("New updated value for "+measureType+ " => "+lf.getValue()+ "\n\n");
        
        buildXml = getPersonFormat(people, firstIdPerson);
    	System.out.println(prettyFormat(buildXml));
    	
    	writer.write(prettyFormat(buildXml)+"\n");
    	
    	
    	System.out.println("============================================================================================================================================");
        System.out.println("Method #10 - updatePersonMeasure(Long id, Measure m) => updatePersonMeasure("+firstIdPerson+", "+mid+")");
        System.out.println("============================================================================================================================================");
        
        writer.write("============================================================================================================================================\n");
        writer.write("Method #10 - updatePersonMeasure(Long id, Measure m) => updatePersonMeasure("+firstIdPerson+", "+mid+")\n");
        writer.write("============================================================================================================================================\n");
        
        HealthMeasureHistory hmhUpdate = new HealthMeasureHistory();
        hmhUpdate.setMid(mid);
        Random random = new Random();
        String randomNumber = Integer.toString(random.nextInt(200 - 10 + 1) + 10);
        hmhUpdate.setValue(randomNumber);
        
        Holder <HealthMeasureHistory> history = new Holder<HealthMeasureHistory>(hmhUpdate);
        
        System.out.println("Updating the measurement in the history with 'mid' = "+mid+" and id person = "+firstIdPerson);
        System.out.println("updated 'value' => "+hmhUpdate.getValue());
        
        writer.write("Updating the measurement in the history with 'mid' = "+mid+" and id person = "+firstIdPerson+"\n");
        writer.write("updated 'value' => "+hmhUpdate.getValue()+"\n");
        
        System.out.println("\nHistory of measurement before updating ...\n");
        
        writer.write("\nHistory of measurement before updating ...\n\n");
        
        //Printing measurement before updating
        hmh = people.readPersonMeasure(firstIdPerson, measureType, mid);
        buildXml = getSingleMeasureHistoryFormat(hmh);
        System.out.println(prettyFormat(buildXml));
        
        writer.write(prettyFormat(buildXml)+"\n");
        
        System.out.println("\nHistory of measurement after updating ...\n");
        
        writer.write("\nHistory of measurement after updating ...\n\n");

    	people.updatePersonMeasure(firstIdPerson, history);
    	
    	//Printing measurement after updating
    	hmh = people.readPersonMeasure(firstIdPerson, measureType, mid);
        buildXml = getSingleMeasureHistoryFormat(hmh);
        System.out.println(prettyFormat(buildXml));
        
        writer.write(prettyFormat(buildXml)+"\n");
        
        writer.close();
        
    }
    
    
    /**
     * return all the information about a single measurement
     * @param listMeasureHistory
     * @return
     */

    public static String getSingleMeasureHistoryFormat(HealthMeasureHistory hmh){
    	
    	String buildXml = "<measureHistory>";
        buildXml += "<history><mid>"+hmh.getMid()+"</mid>";
        buildXml += "<value>"+hmh.getValue()+"</value>";
        buildXml +="<created>"+hmh.getCreated()+"</created>";
        /*buildXml += "<measureDefinition>";
        buildXml += "<idMeasureDef>"+hmh.getMeasureDefinition().getIdMeasureDef()+"</idMeasureDef>";
        buildXml += "<measureType>"+hmh.getMeasureDefinition().getMeasureType()+"</measureType>";
        buildXml += "<measureValueType>"+hmh.getMeasureDefinition().getMeasureValueType()+"</measureValueType>";*/
        buildXml += "</history>";
        buildXml += "</measureHistory>";
    	
    	return buildXml;
    }
    
    
   
    /**
     * return all the information about a history of a specific measurement
     * @param listMeasureHistory
     * @return
     */

    public static String getMeasureHistoryFormat(List<HealthMeasureHistory> listMeasureHistory){
    	
    	String buildXml = "<measureHistory>";
        
        for(int i = 0; i < listMeasureHistory.size(); i++){
        	buildXml += "<history><mid>"+listMeasureHistory.get(i).getMid()+"</mid>";
        	buildXml += "<value>"+listMeasureHistory.get(i).getValue()+"</value>";
        	buildXml +="<created>"+listMeasureHistory.get(i).getCreated()+"</created>";
        	/*buildXml += "<measureDefinition>";
        	buildXml += "<idMeasureDef>"+listMeasureHistory.get(i).getMeasureDefinition().getIdMeasureDef()+"</idMeasureDef>";
        	buildXml += "<measureType>"+listMeasureHistory.get(i).getMeasureDefinition().getMeasureType()+"</measureType>";
        	buildXml += "<measureValueType>"+listMeasureHistory.get(i).getMeasureDefinition().getMeasureValueType()+"</measureValueType>";*/
        	buildXml += "</history>";
        }
        
        buildXml += "</measureHistory>";
    	
    	return buildXml;
    }
    
    /**
     * return all the information about a person
     * @param people
     * @param idPerson
     * @return
     */
    public static String getPersonFormat(People people, int idPerson){
    	
    	String buildXml;
    	
    	buildXml = "<person><idPerson>"+people.readPerson(idPerson).getIdPerson()+"</idPerson>";
    	buildXml +="<firstname>"+people.readPerson(idPerson).getFirstname()+"</firstname>";
    	buildXml += "<lastname>"+people.readPerson(idPerson).getLastname()+"</lastname>";
    	buildXml += "<birthdate>"+people.readPerson(idPerson).getBirthdate()+"</birthdate>";
    	buildXml += "<email>"+people.readPerson(idPerson).getEmail()+"</email>";
    	buildXml += "<username>"+people.readPerson(idPerson).getUsername()+"</username>";
    	buildXml += "<healthProfile>";
    	
    	if(people.readPerson(idPerson).getHealthProfile().getLifeStatus().size() > 0)
    	{
    		for(int k = 0; k < people.readPerson(idPerson).getHealthProfile().getLifeStatus().size(); k++)
    		{
        		buildXml += "<lifeStatus>";
        		buildXml += "<measureType>";
            	buildXml += "<idMeasureDef>"+people.readPerson(idPerson).getHealthProfile().getLifeStatus().get(k).getMeasureType().getIdMeasureDef()+"</idMeasureDef>";
            	buildXml += "<name>"+people.readPerson(idPerson).getHealthProfile().getLifeStatus().get(k).getMeasureType().getName()+"</name>";
            	buildXml += "</measureType>";
            	buildXml += "<value>"+people.readPerson(idPerson).getHealthProfile().getLifeStatus().get(k).getValue()+"</value>";
            	buildXml += "</lifeStatus>";	
    		}
    	}
    	
    	buildXml += "</healthProfile></person>";
    	
    	return buildXml;
    }
    
    /**
     * Format the xml body
     * @param input
     * @return
     */
    public static String prettyFormat(String input) {
	    return prettyFormat(input, 2);
	}
    
    
    /**
     * Format the xml body
     * @param input
     * @param indent
     * @return
     */
    public static String prettyFormat(String input, int indent) {
	    try {
	        Source xmlInput = new StreamSource(new StringReader(input));
	        StringWriter stringWriter = new StringWriter();
	        StreamResult xmlOutput = new StreamResult(stringWriter);
	        TransformerFactory transformerFactory = TransformerFactory.newInstance();
	        transformerFactory.setAttribute("indent-number", indent);
	        Transformer transformer = transformerFactory.newTransformer(); 
	        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	        transformer.transform(xmlInput, xmlOutput);
	        return xmlOutput.getWriter().toString();
	    } catch (Exception e) {
	        throw new RuntimeException(e); // simple exception handling, please review it
	    }
	}
    
    
}