/**
 * 
 */
package com.apple.reccommendation.example;

import com.apple.reccommendation.dating.UserMatchImpl;

/**
 * @author ajay.mall
 *
 */
public class TestDriver
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		
		  UserMatchImpl userMatchImpl = new UserMatchImpl();
		  userMatchImpl.printMaleUserProfileRecommendation();
	}
	
}