/*******************************************************************************
 Author:    Aiswarya Gurram

 Purpose:   This java program will ask the user for a movie number
            and display the 20 movies in the list that are most similar to it.
            It will measure similarity by using the Pearson r coefficient
            among same reviewers.

 Execution: java Hw4

 java hw4
 ******************************************************************************/

import java.io.*;
import java.util.*;

public class Hw4
{
    static List<List<Integer>> matrix;
    public static ArrayList movie_list;
    private static Object GoodMovie;

    static {
            try {
                matrix = reading_matrix();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        static {
            try {
                movie_list = reading_movies();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        public static void main(String[] args) throws IOException, ClassNotFoundException {
            String userInput;
            matrix = reading_matrix();
            movie_list = reading_movies();
            String path_file = "F:\\Hw3\\output\\hw3-gurr-movie-matrix2.ser";
            int index = path_file.lastIndexOf("\\");
            String name_of_file = path_file.substring(index + 1);
            System.out.println("Table name is: "+ name_of_file + "\n");
            System.out.println("*** No. of movie names = "+ movie_list.size() + "\n");
            System.out.println("Rows "+matrix.size()+","+" columns "+matrix.get(0).size());
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.print("Movie number: ");
                userInput = scanner.next();
                if (userInput.equals("q") || userInput.equals("quit")) {
                    break;
                }
                if (!(userInput != null && userInput.matches("^[-+]?\\d*$"))) {
                    System.out.println("Invalid entry, try again"+"\n");
                    continue;
                }
                if (Integer.parseInt(userInput) > movie_list.size() || Integer.parseInt(userInput) < 1) {
                    System.out.println("Movie number must be between 1 and 1682" + "\n");
                    continue;
                }
                else
                {

                    String new_number = numberConversion(userInput);
                    for(int i=0;i<movie_list.size();i++)
                    {
                        String movie_name= (String) movie_list.get(i);
                        if(movie_name.substring(0,4).equals(new_number))
                        {
                            System.out.println("Movie: "+userInput+" "+movie_name.substring(4));
                            common_reviewers(userInput);
                        }
                    }
                }

            }
            scanner.close();
        }

        public static void common_reviewers(String target_movie_num)
        {
            int index = Integer.parseInt(target_movie_num) - 1;
            ArrayList<GoodMovie> movies = new ArrayList<GoodMovie>();
            for( int i =0; i < matrix.size(); i++)
            {
                int count  =0;
                int c =0;
                String movie_name= (String) movie_list.get(i);
                List<Integer> common_reviews =  new ArrayList<Integer>();
                for( int j =0; j < matrix.get(i).size(); j++)
                {
                    if(matrix.get(index).get(j)!=0 && matrix.get(i).get(j)!=0)
                    {
                        count++;
                        common_reviews.add(j+1);
                    }
                }
                System.out.println("Common reviewers for movie "+ (i + 1)+":");
                for(int k = 0; k < common_reviews.size();k++)
                {
                    c++;
                    System.out.printf("%4d ", common_reviews.get(k));
                    if (c % 10 == 0)
                    {
                        System.out.println();
                    }
                }
                System.out.println("\n");
                System.out.println("Movie "+target_movie_num+", comparison movie "+(i+1)+" ("+movie_name.substring(4,movie_name.length()-1)+") common reviewers "+common_reviews.size());
                System.out.print("compare movie is "+movie_name.substring(4));
                System.out.println("no. of common reviewers "+common_reviews.size());
                if( count >= 10)
                {
                    System.out.printf("%.6f  %.6f  %.6f  %.6f  ",calcAvg(index,common_reviews),calcAvg(i,common_reviews),
                            calcStd(index,common_reviews),calcStd(i,common_reviews));
                    System.out.printf("%s:  %.6f \n" ,"r", calcPearson(i,index,common_reviews));
                    movies.add(new GoodMovie(calcPearson(i,index, common_reviews),i,count,movie_name.substring(4)));
                }
                System.out.println();
            }
            Collections.sort(movies,Collections.reverseOrder(new GoodMovie.good_movie()));
            if( movies.size() > 20) {
                int count = 1;
                System.out.printf("%11s %7s %11s %11s \n","R","No.","Reviews","Name");
                for (int i = 0; i < 20; i++) {
                    double r = movies.get(i).getR();
                    String movie_name = movies.get(i).getMovie_name();
                    int movie_number = movies.get(i).getMovie_number() + 1 ;
                    int review_number = movies.get(i).getReviews();

                    System.out.printf("%2d.   %.6f %4d (%4d %s) %s", count,r,movie_number,review_number,"reviews",movie_name);
                    count++;
                }
                System.out.println("\n");
            }
            else
            {
                System.out.println("Insufficient comparison movies");
            }
        }

        public static int calcSum(int number, List<Integer> movie)
        {
            int sum = 0;
            for(int i = 0; i < movie.size();i++)
            {
                sum =  sum +matrix.get(number).get(movie.get(i)-1);
            }
            return sum;
        }

        public static  double calcAvg(int number,List<Integer> movie)
        {
            double avg = (double) calcSum(number,movie)/movie.size();
            return avg;
        }


        public static double calcStd(int number,List<Integer> movie)
        {
            double value = 0.0;
            double std;
            for(int i = 0;i < movie.size();i++) {
                 value = value + Math.pow(matrix.get(number).get(movie.get(i)-1) - calcAvg(number,movie),2);
            }
            std = (double) value / (movie.size()-1);
            return Math.sqrt(std);
        }

        public static double calcPearson(int compared_number, int target_number, List<Integer> Ratings)
        {
            double product = 0.0;
            double r = 0.0;
            List<Double> value1 =  new ArrayList<Double>();
            List<Double> value2 =  new ArrayList<Double>();
            for (int i = 0; i < Ratings.size();i++)
            {
                value1.add((matrix.get(target_number).get(Ratings.get(i)-1) - calcAvg(target_number,Ratings))/calcStd(target_number,Ratings));
            }
            for (int i = 0; i < Ratings.size();i++)
            {
                value2.add((matrix.get(compared_number).get(Ratings.get(i)-1) - calcAvg(compared_number,Ratings))/calcStd(compared_number,Ratings));
            }
            for( int i = 0; i < Ratings.size();i++)
            {
                product = product + (value1.get(i) * value2.get(i));
            }
            r = (( product)/(Ratings.size()-1));
            return r;
        }

        public static ArrayList reading_movies() throws IOException
        {
            BufferedReader reader = new BufferedReader(new FileReader("F:\\Hw3\\output\\movienames2.txt"));
            List<String> movies_list = new ArrayList<String>();
            String line;
            while ((line = reader.readLine()) != null)
            {
                movies_list.add(line + "\n");
            }
            reader.close();
            return (ArrayList)movies_list;
        }
        public static String numberConversion(String str)
        {
            if(str.length()==3){
                str="0"+str;
            }
            if(str.length()==2){
                str="00"+str;
            }
            if(str.length()==1){
                str="000"+str;
            }
            return str;
        }

        public static List<List<Integer>> reading_matrix() throws IOException,ClassNotFoundException
        {
            List<List<Integer>> matrix = new ArrayList<List<Integer>>();
            FileInputStream fs = new FileInputStream("F:\\Hw3\\output\\hw3-gurr-movie-matrix2.ser");
            ObjectInputStream os = new ObjectInputStream(fs);
            matrix = (List<List<Integer>>) os.readObject();
            os.close();
            return matrix;
            }
        }