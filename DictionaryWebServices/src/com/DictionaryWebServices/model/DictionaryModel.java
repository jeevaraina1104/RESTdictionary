package com.DictionaryWebServices.model;


public class DictionaryModel {
	
	private String word;
	private String mean;
	
	
	
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	
	public String getMean() {
		return mean;
	}
	public void setMean(String mean) {
		this.mean = mean;
	}
	
	 @Override
	    public String toString() {
	        return "Dictionary [word=" + word + ", mean=" + mean + "]";
	    }

}
