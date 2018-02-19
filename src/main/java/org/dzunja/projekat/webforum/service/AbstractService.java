package org.dzunja.projekat.webforum.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;


public abstract class AbstractService<T> {
	
	public final String relativeLocalHostPath =  System.getProperty("tomcatprop");
	public final String dataFolderLocation = relativeLocalHostPath + "resources/";
	public final String imageFolderLocation =  relativeLocalHostPath + "uploadedFiles/";
	
	private ObjectMapper mapper;
	private File jsonFile;
	private Class<T> objectType;
	
	protected AbstractService(String jsonFileName, Class<T> objectType) {
		this.mapper = new ObjectMapper();
		this.jsonFile = new File(dataFolderLocation + jsonFileName);
		this.objectType = objectType;
		
		this.mapper.enable(SerializationFeature.INDENT_OUTPUT); /// PRETTY PRINT
		
		System.out.println("Relative local host " + relativeLocalHostPath);
		System.out.println("JSON path " + dataFolderLocation);
		System.out.println("Image path " + imageFolderLocation);
		System.out.println("CATALINA base " + System.getProperty("catalina.base"));
		System.out.println("CATALINA home" + System.getProperty("catalina.home"));
		
		System.out.println("Accesing resource at " + this.jsonFile.getPath());
	}
	
	public List<T> get(){
		
		try {
			return mapper.readValue(jsonFile, mapper.getTypeFactory()
													.constructCollectionType(List.class, objectType));
		} catch (JsonParseException e) {
			e.printStackTrace();
			return null;
		} catch (JsonMappingException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void update(List<T> itemList) {
		
		try {
			mapper.writeValue(jsonFile, itemList);
		} catch (JsonGenerationException e) {
				e.printStackTrace();
		} catch (JsonMappingException e) {
				e.printStackTrace();
		} catch (IOException e) {
				e.printStackTrace();
		}
		
	}
	
	public void update(Map<String, T> map) {
		update(new ArrayList<T>(map.values()));
	}
	
	
	
	
	public abstract T add(T item);
	
	public abstract T update(T item);
	
	public void writeToFile(InputStream uploadedInputStream,
			String uploadedFileLocation) {

		try {
			OutputStream out = new FileOutputStream(new File(
					uploadedFileLocation));
			int read = 0;
			byte[] bytes = new byte[1024];

			out = new FileOutputStream(new File(uploadedFileLocation));
			while ((read = uploadedInputStream.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			out.flush();
			out.close();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
	
	public boolean isBadString(String s) {
		return s == null || s.trim().isEmpty();
	}
	
	
}

