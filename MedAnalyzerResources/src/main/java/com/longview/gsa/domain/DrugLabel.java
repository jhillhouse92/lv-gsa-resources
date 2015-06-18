package com.longview.gsa.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "drugs")
public class DrugLabel {
	
	@Id String id;
	String[] adverse_reactions;
	String[] description;
	String[] warnings;
	OpenFDA openfda;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String[] getAdverse_reactions() {
		return adverse_reactions;
	}
	public void setAdverse_reactions(String[] adverse_reactions) {
		this.adverse_reactions = adverse_reactions;
	}
	public String[] getDescription() {
		return description;
	}
	public void setDescription(String[] description) {
		this.description = description;
	}
	public String[] getWarnings() {
		return warnings;
	}
	public void setWarnings(String[] warnings) {
		this.warnings = warnings;
	}
	public OpenFDA getOpenfda() {
		return openfda;
	}
	public void setOpenfda(OpenFDA openfda) {
		this.openfda = openfda;
	}
	
	
}
