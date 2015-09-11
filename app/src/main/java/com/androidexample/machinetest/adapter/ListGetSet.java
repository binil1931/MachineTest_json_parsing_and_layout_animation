package com.androidexample.machinetest.adapter;

public class ListGetSet {

	private String id;
	private String title;
    private  String property_title;


	public ListGetSet() {
		// TODO Auto-generated constructor stub
	}

	public ListGetSet(String id, String title,String property_title) {
		super();
		this.id = id;
		this.title = title;
        this.property_title = property_title;
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


	public String getTitle() {
		return title;
	}

	public void setTitlel(String title) {
		this.title = title;
	}

    public String getProperty_title() {
        return property_title;
    }

    public void setProperty_title(String property_title) {
        this.property_title = property_title;
    }


}
