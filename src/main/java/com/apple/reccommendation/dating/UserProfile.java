/**
 * 
 */
package com.apple.reccommendation.dating;

import java.util.List;

/**
 * @author ajay.mall
 *
 */
public class UserProfile {
	String name;
	String gender;
	/*Numeric unique gender index for user as per their respective gender(male/female)*/ 
	Integer genderIndex;
	Integer age;
	List<Integer> interestIndexList;
	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}
	/**
	 * @return the gender
	 */
	public String getGender()
	{
		return gender;
	}
	/**
	 * @param gender the gender to set
	 */
	public void setGender(String gender)
	{
		this.gender = gender;
	}
	
	/**
	 * @return the genderIndex
	 */
	public Integer getGenderIndex()
	{
		return genderIndex;
	}
	/**
	 * @param genderIndex the genderIndex to set
	 */
	public void setGenderIndex(Integer genderIndex)
	{
		this.genderIndex = genderIndex;
	}
	/**
	 * @return the age
	 */
	public Integer getAge()
	{
		return age;
	}
	/**
	 * @param age the age to set
	 */
	public void setAge(Integer age)
	{
		this.age = age;
	}
	/**
	 * @return the interestIndexList
	 */
	public List<Integer> getInterestIndexList()
	{
		return interestIndexList;
	}
	/**
	 * @param interestIndexList the interestIndexList to set
	 */
	public void setInterestIndexList(List<Integer> interestIndexList)
	{
		this.interestIndexList = interestIndexList;
	}
	@Override
	public String toString()
	{
		return "UserProfile [name=" + name + ", gender=" + gender + ", genderIndex=" + genderIndex + ", age=" + age
				+ ", interestIndexList=" + interestIndexList + "]";
	}
	
}
