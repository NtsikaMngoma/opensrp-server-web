package org.opensrp.web.utils;

import org.smartregister.domain.Client;

public class ChildMother {
	
	private Client child;
	
	private Client mother;
	
	public ChildMother(Client child, Client mother) {
		this.child = child;
		this.mother = mother;
	}
	
	public Client getMother() {
		return mother;
	}
	
	public Client getChild() {
		return child;
	}
}
