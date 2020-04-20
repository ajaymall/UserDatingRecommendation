package com.apple.reccommendation.dating;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

public class UserMatchImpl implements IUserMatch, Serializable
{
	private static final long serialVersionUID = -8184534209437261729L;

	int							interestCount;
	int							maleProfileCount;
	int							femaleProfileCount;
	double						maleInterestMatrix[][];
	double						femaleInterestMatrix[][];
	double						femaleInterestTransposeMatrix[][];
	Double						maleFemaleSimilarityInterestMatrix[][];
	Integer						sortedMaleFemaleSimilarityInterestMatrix[][];
	Map<String, Integer>		interestIndexMap;
	Map<Integer, UserProfile>	maleProfileInterestMap;
	Map<Integer, UserProfile>	femaleProfileInterestMap;
	Map<String, List<String>>	maleProfileRecoListMap;

	public void printMaleUserProfileRecommendation()
	{
		if (generateWeightedSortMaleFemaleSimilarityInterestMatrix().equals(Constant.SUCCESS))
		{
			List<String> femaleProfileRecoSortedList;
			maleProfileRecoListMap = new HashMap<String, List<String>>();
			
			/*
			 * System.out.println("Sorted profile matrix "); for (int i = 0; i < maleProfileCount; i++) { for (int j =
			 * 0; j < femaleProfileCount; j++) System.out.print(sortedMaleFemaleSimilarityInterestMatrix[i][j] + " ");
			 * System.out.println(); }
			 */

			for (int i = 0; i < maleProfileCount; i++)
			{
				femaleProfileRecoSortedList = new ArrayList<String>();
				for (int j = 0; j < femaleProfileCount; j++)
				{
					femaleProfileRecoSortedList.add(femaleProfileInterestMap.get(sortedMaleFemaleSimilarityInterestMatrix[i][j]).name);
				}
				maleProfileRecoListMap.put(maleProfileInterestMap.get(i).name, femaleProfileRecoSortedList);
			}
			
			System.out.println("Recommended male profile matrix ");
			for (String maleUser :  maleProfileRecoListMap.keySet())
			{
					System.out.println("Recommendation for " + maleUser + ": -->" + maleProfileRecoListMap.get(maleUser));
			}
		}
	}

	private String generateWeightedSortMaleFemaleSimilarityInterestMatrix()
	{
		if (generateCosineSimilartyGenderInterestRecoMatrix().equals(Constant.SUCCESS))
		{
			List<Double> singleMaleProfileInterestList = new ArrayList<Double>();
			List<Integer> sortedIndexList;

			/*
			 * 1. Change Cosine similarity index matrix value in 1's complimentary matrix for non-zero value. 2. Add
			 * absolute Age difference factor to give preference of age difference factor over interest factor
			 */

			for (int i = 0; i < maleProfileCount; i++)
			{
				for (int j = 0; j < femaleProfileCount; j++)
				{
					/*
					 * Change Cosine similarity index matrix value in 1's complimentary matrix for non-zero value.
					 */
					if (!maleFemaleSimilarityInterestMatrix[i][j].equals(0.0))
					{
						maleFemaleSimilarityInterestMatrix[i][j] = 1.0 - maleFemaleSimilarityInterestMatrix[i][j];
					}
					/*
					 * Add absolute Age difference factor to give preference of age difference factor over interest
					 * factor
					 */
					maleFemaleSimilarityInterestMatrix[i][j] = maleFemaleSimilarityInterestMatrix[i][j]
							+ Math.abs(maleProfileInterestMap.get(i).age - femaleProfileInterestMap.get(j).age);
				}
			}

			/*
			 * System.out.println("1's Complimentry matrix "); for (int i = 0; i < maleProfileCount; i++) { for (int j =
			 * 0; j < femaleProfileCount; j++) System.out.print(maleFemaleSimilarityInterestMatrix[i][j] + " ");
			 * System.out.println(); }
			 */

			sortedMaleFemaleSimilarityInterestMatrix = new Integer[maleProfileCount][femaleProfileCount];
			for (int i = 0; i < maleProfileCount; i++)
			{
				singleMaleProfileInterestList = new ArrayList<Double>();
				for (int j = 0; j < femaleProfileCount; j++)
				{
					singleMaleProfileInterestList.add(maleFemaleSimilarityInterestMatrix[i][j]);

				}

				sortedIndexList = sortIndex(singleMaleProfileInterestList);

				for (int j = 0; j < femaleProfileCount; j++)
				{
					sortedMaleFemaleSimilarityInterestMatrix[i][j] = sortedIndexList.get(j);
				}

			}
			return Constant.SUCCESS;
		}

		return Constant.ERROR;
	}

	private String generateCosineSimilartyGenderInterestRecoMatrix()
	{

		if (generateL2NormalizeGenderProfileWiseInterestMatrix().equals(Constant.SUCCESS))
		{

			/* generate transpose for female profile matrix for matrix multiplication */

			femaleInterestTransposeMatrix = new double[interestCount][femaleProfileCount];
			for (int i = 0; i < interestCount; i++)
			{
				for (int j = 0; j < femaleProfileCount; j++)
					femaleInterestTransposeMatrix[i][j] = femaleInterestMatrix[j][i];
			}
			
			/*
			 * System.out.println("Female profile transpose matrix"); for (int i = 0; i < interestCount; i++) { for (int
			 * j = 0; j < femaleProfileCount; j++) System.out.print(femaleInterestTransposeMatrix[i][j] + " ");
			 * System.out.println(); }
			 */

			maleFemaleSimilarityInterestMatrix = new Double[maleProfileCount][femaleProfileCount];

			/* User Interest Profile matrix multiplication(Male,Transpose(female)) */

			for (int i = 0; i < maleProfileCount; i++)
			{
				for (int j = 0; j < femaleProfileCount; j++)
				{
					maleFemaleSimilarityInterestMatrix[i][j] = 0.0;
					for (int k = 0; k < interestCount; k++)
						maleFemaleSimilarityInterestMatrix[i][j] += maleInterestMatrix[i][k]
								* femaleInterestTransposeMatrix[k][j];
				}
			}

			/*
			 * System.out.println("Male-Female(T) profile matrix after matrix multiplication"); for (int i = 0; i <
			 * maleProfileCount; i++) { for (int j = 0; j < femaleProfileCount; j++)
			 * System.out.print(maleFemaleSimilarityInterestMatrix[i][j] + " "); System.out.println(); }
			 */

			return Constant.SUCCESS;
		}

		return Constant.ERROR;

	}

	private String generateL2NormalizeGenderProfileWiseInterestMatrix()
	{

		if (populategenderProfileInterestMap().equals(Constant.SUCCESS))
		{
			double rowSquareSum;
			double rowSquarerootSum;
			interestCount = interestIndexMap.size();
			maleProfileCount = maleProfileInterestMap.size();
			femaleProfileCount = femaleProfileInterestMap.size();

			maleInterestMatrix = new double[maleProfileCount][interestCount];
			femaleInterestMatrix = new double[femaleProfileCount][interestCount];

			for (Integer maleIndex : maleProfileInterestMap.keySet())
			{
				rowSquareSum = 0;
				rowSquarerootSum = 0.0;
				for (@SuppressWarnings("unused")
				Integer interestIndex : maleProfileInterestMap.get(maleIndex).getInterestIndexList())
				{
					rowSquareSum = rowSquareSum + Math.pow(1.0, 2);
				}
				rowSquarerootSum = Math.sqrt(rowSquareSum);
				for (Integer interestIndex : maleProfileInterestMap.get(maleIndex).getInterestIndexList())
				{
					maleInterestMatrix[maleIndex][interestIndex] = 1.0 / rowSquarerootSum;
				}
			}

			for (Integer femaleIndex : femaleProfileInterestMap.keySet())
			{

				rowSquareSum = 0;
				rowSquarerootSum = 0.0;
				for (@SuppressWarnings("unused")
				Integer interestIndex : femaleProfileInterestMap.get(femaleIndex).getInterestIndexList())
				{
					rowSquareSum = rowSquareSum + Math.pow(1.0, 2);
				}
				rowSquarerootSum = Math.sqrt(rowSquareSum);
				for (Integer interestIndex : femaleProfileInterestMap.get(femaleIndex).getInterestIndexList())
				{
					femaleInterestMatrix[femaleIndex][interestIndex] = 1.0 / rowSquarerootSum;
				}
			}

			/*
			 * for (int i = 0; i < maleProfileCount; i++) { for (int j = 0; j < interestCount; j++)
			 * System.out.print(maleInterestMatrix[i][j] + " "); System.out.println(); }
			 * 
			 * for (int i = 0; i < femaleProfileCount; i++) { for (int j = 0; j < interestCount; j++)
			 * System.out.print(femaleInterestMatrix[i][j] + " "); System.out.println(); }
			 */

			return Constant.SUCCESS;
		}

		return Constant.ERROR;

	}

	private String populategenderProfileInterestMap()
	{
		Scanner scan;
		ClassLoader classLoader = ClassLoader.getSystemClassLoader();

		try
		{
			InputStream input = classLoader.getResourceAsStream(Constant.CONFIGFILE);
			Properties prop = new Properties();
			prop.load(input);

		}
		catch (IOException e)
		{
			e.printStackTrace();
			return Constant.ERROR;
		}

		try
		{
			scan = new Scanner(new File(classLoader.getResource(Constant.DATAFILE).getFile()));
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return Constant.ERROR;
		}

		UserProfile userProfile;
		int lastInterestIndex = 0;
		int lastMaleProfileIndex = 0;
		int lastFemaleProfileIndex = 0;
		/* Memory allocation */
		interestIndexMap = new HashMap<String, Integer>();
		maleProfileInterestMap = new HashMap<Integer, UserProfile>();
		femaleProfileInterestMap = new HashMap<Integer, UserProfile>();

		/* Read Header Line & Skip */
		String curLine = scan.nextLine();

		/* Read line by line */
		while (scan.hasNext())
		{
			curLine = scan.nextLine();
			String[] splitted = curLine.split(Constant.DELEMETER);
			userProfile = new UserProfile();
			userProfile.setName(splitted[0].trim());
			userProfile.setAge(Integer.parseInt(splitted[1].trim()));
			userProfile.setGender(splitted[2].trim());
			String[] interestArray = splitted[3].split(Constant.COMMA);
			List<Integer> interestIndexList = new ArrayList<Integer>();
			for (String interest : interestArray)
			{
				interest = interest.trim();
				if (interestIndexMap.containsKey(interest))
				{
					interestIndexList.add(interestIndexMap.get(interest));
				}
				else
				{
					interestIndexMap.put(interest, lastInterestIndex);
					interestIndexList.add(lastInterestIndex);
					lastInterestIndex++;
				}
			}
			userProfile.setInterestIndexList(interestIndexList);

			if (userProfile.getGender().equals(Constant.MALE))
			{
				userProfile.setGenderIndex(lastMaleProfileIndex);
				maleProfileInterestMap.put(lastMaleProfileIndex, userProfile);

				lastMaleProfileIndex++;
			}
			else if (userProfile.getGender().equals(Constant.FEMALE))
			{
				userProfile.setGenderIndex(lastFemaleProfileIndex);
				femaleProfileInterestMap.put(lastFemaleProfileIndex, userProfile);
				lastFemaleProfileIndex++;
			}
			else
			{
				// Do nothing for other gender.
			}
		}
		scan.close();
		
		System.out.println("Interest Index Map");
		
		for (String intererest : interestIndexMap.keySet())
		{
			System.out.println(intererest+":"+interestIndexMap.get(intererest));
		}
		
		System.out.println("Gender-Wise User profile Map");

		for (Integer maleIndex : maleProfileInterestMap.keySet())
		{
			System.out.println(maleProfileInterestMap.get(maleIndex));
		}
		for (Integer femaleIndex : femaleProfileInterestMap.keySet())
		{
			System.out.println(femaleProfileInterestMap.get(femaleIndex));
		}

		return Constant.SUCCESS;
	}

	/*
	 * <b> Description:</b>This utility method iterate over list record and add entry in Map which has key as Select
	 * column for Average and value as amount in euro.First it's prepare map as per group by condition and than It's
	 * Iterate map to calculate total average amount as per group by.
	 * 
	 * @param avgColumnMap Map for group by columns as Key and TotalEuro Amount as value.
	 * 
	 * @param companyAccount Pojo Object for each record entry in file.
	 * 
	 * @return Map<AvgColumn, EuroAmount> Function return map with total Amount in Euro, aggregated as per group by
	 * column
	 */

	private <T extends Comparable<T>> List<Integer> sortIndex(List<T> in)
	{
		ArrayList<Integer> indexArray = new ArrayList<>();
		for (int i = 0; i < in.size(); i++)
		{
			indexArray.add(i);
		}

		Collections.sort(indexArray, new Comparator<Integer>() {
			@Override
			public int compare(Integer idx1, Integer idx2)
			{
				return in.get(idx1).compareTo(in.get(idx2));
			}
		});

		return indexArray;
	}
}
