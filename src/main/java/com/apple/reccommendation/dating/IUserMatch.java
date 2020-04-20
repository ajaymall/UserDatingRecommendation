package com.apple.reccommendation.dating;

public interface IUserMatch {
	
	/*
	 * <b> Description:</b>This utility method load data file and for each record line,add
	 * entry in profile Map which has key as gender profile index and value as
	 * user profile object.First it's prepare gender profile map than create cosine similarity 
	 * matrix for respective profile interest list.Once the matrix got created,
	 * It's do following changes in cosine similarity matrix. 
	 * 1. Change Cosine similarity index matrix value in 1's complimentary matrix for non-zero value. 
	 * 2. Add absolute Age difference factor to give preference of age difference factor over interest factor
	 */
	public void printMaleUserProfileRecommendation();
}
