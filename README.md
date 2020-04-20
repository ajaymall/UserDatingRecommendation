User-based-Dating-Recommendation

Compile and Run TestDriver.java file

[a.] javac TestDriver.java

[b.] java TestDriver

For running java file, following files were used-

Input files: [i] User-Profile.txt {Name-Age-Gender-Interests}

After running java file, following output was generated-

Java Console Output :

Interest Index Map
Cricket:0
Tennis:1
Rugby:5
Badminton:4
Movies:3
Football:2
Gender-Wise User profile Map
UserProfile [name=UserB, gender=Male, genderIndex=0, age=27, interestIndexList=[0, 2, 3]]
UserProfile [name=UserC, gender=Male, genderIndex=1, age=26, interestIndexList=[3, 1, 2, 0]]
UserProfile [name=UserF, gender=Male, genderIndex=2, age=32, interestIndexList=[0, 2, 3, 4]]
UserProfile [name=UserA, gender=Female, genderIndex=0, age=25, interestIndexList=[0, 1]]
UserProfile [name=UserD, gender=Female, genderIndex=1, age=24, interestIndexList=[1, 2, 4]]
UserProfile [name=UserE, gender=Female, genderIndex=2, age=32, interestIndexList=[0, 2, 3, 4]]
UserProfile [name=UserG, gender=Female, genderIndex=3, age=32, interestIndexList=[5]]
Recommended male profile matrix 
Recommendation for UserB: -->[UserA, UserD, UserG, UserE]
Recommendation for UserF: -->[UserE, UserG, UserA, UserD]
Recommendation for UserC: -->[UserA, UserD, UserG, UserE]


#Pseudo code-

In class UserMatchImpl, Initialize a printMaleUserProfileRecommendation() which reads User-Profile.txt file and forms matrix based on data provided by input file.
This utility method load data file and for each record line,add entry in profile Map which has key as gender profile index and value as user profile object.First it's prepare gender profile map than create cosine similarity matrix for respective profile interest list.Once the matrix got created,It's do following changes in cosine similarity matrix. 
	 
	 1. Change Cosine similarity index matrix value in 1's complimentary matrix for non-zero value.
	  
	 2. Add absolute Age difference factor to give preference of age difference factor over interest factor
After that It's sorted final matrix based on array index and finally generate recommendation for male user profile. 