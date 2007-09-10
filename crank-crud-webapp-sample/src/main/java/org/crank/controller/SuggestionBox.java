package org.crank.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.io.Serializable;

import org.richfaces.renderkit.html.SuggestionBoxRenderer;

/*
 * Suggestion component sandbox... will be refactored into the framework after it is proven
 * (Paul T)
 */
public class SuggestionBox {

    private ArrayList items;

    private ArrayList data;
    private ArrayList tokens;

    private String property;
    
    private String rows;
    private String first;
    private String cellspacing;
    private String cellpadding;
    private String minchars;
    private String frequency;
    private String rules;
    private boolean check;
    private String shadowDepth = Integer.toString(SuggestionBoxRenderer.SHADOW_DEPTH);
    private String border = "1";
    private String width = "200";
    private String height = "150";
    private String shadowOpacity = "4";

    private static final String [] cit = {"Abba", "Abbeville", "Acworth", "Adairsville", "Adel", "Adrian", "Ailey", "Alamo", "Alapaha", "Albany", "Allenhurst", "Alma", "Alpharetta", "Alston", "Amboy", "Ambrose", "Americus", "Appling", "Arlington", "Ashburn", "Athens", "Athens-Clarke County", "Atkinson", "Atlanta", "Attapulgus", "Auburn", "Augusta", "Augusta-Richmond County", "Austell", "Avondale Estates", "Axson"};
	
    public SuggestionBox() {
        this.rows = "0";
        this.first = "0";
        this.cellspacing = "2";
        this.cellpadding = "2";
        this.minchars = "1";
        this.frequency = "0";
        this.rules = "none";

        setItems(getAllData());
    }

    public List autocomplete(Object suggest) {
        String pref = (String)suggest;
        ArrayList result = new ArrayList();

        Iterator iterator = getAllData().iterator();
        while (iterator.hasNext()) {
            Suggestion elem = (Suggestion) iterator.next();
            if ((elem != null && elem.getText().toLowerCase().indexOf(pref.toLowerCase()) == 0) || "".equals(pref))
            {
                result.add(elem);
            }
        }
        return result;
    }

    public ArrayList getItems() {
        return items;
    }

    public void setItems(ArrayList items) {
        this.items = items;
    }

    public ArrayList getAllData() {
        ArrayList result = new ArrayList();
        for (int i = 0; i < cit.length; i++) {
            Suggestion data = new Suggestion(cit[i], String.valueOf(i + 1));
            result.add(data);
        }
        return result;
    }

    public ArrayList getData() {
        return data;
    }

    public void setData(ArrayList data) {
        this.data = data;
    }

    public String getCellpadding() {
        return cellpadding;
    }

    public void setCellpadding(String cellpadding) {
        this.cellpadding = cellpadding;
    }

    public String getCellspacing() {
        return cellspacing;
    }

    public void setCellspacing(String cellspacing) {
        this.cellspacing = cellspacing;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public String getFirst() {
        return first;
    }

    public int getIntFirst() {
        return Integer.parseInt(getFirst());
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getFrequency() {
        return frequency;
    }

    public double getDoubleFrequency() {
        return Double.parseDouble(getFrequency());
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getMinchars() {
        return minchars;
    }

    public void setMinchars(String minchars) {
        this.minchars = minchars;
    }

    public String getRows() {
        return rows;
    }

    public int getIntRows() {
        return Integer.parseInt(getRows());
    }

    public void setRows(String rows) {
        this.rows = rows;
    }

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }

    public ArrayList getTokens() {
        return tokens;
    }

    public void setTokens(ArrayList tokens) {
        this.tokens = tokens;
    }

    public void OnSelect() {
        System.out.print("Onselect works!!!");

    }

    public String getShadowDepth() {
        return shadowDepth;
    }

    public void setShadowDepth(String shadowDepth) {
        this.shadowDepth = shadowDepth;
    }

    public String getBorder() {
        return border;
    }

    public void setBorder(String border) {
        this.border = border;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getShadowOpacity() {
        return shadowOpacity;
    }

    public void setShadowOpacity(String shadowOpacity) {
        this.shadowOpacity = shadowOpacity;
    }

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}
	
}
