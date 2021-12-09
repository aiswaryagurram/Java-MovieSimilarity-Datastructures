import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class GoodMovie {
    private  int reviews;
    private  String movie_name;
    private int movie_number;
    private double r;
    public GoodMovie(double r, int movie_number, int reviews, String movie_name) {
        this.r = r;
        this.movie_number =movie_number ;
        this.reviews= reviews;
        this.movie_name = movie_name;
    }

    public void setMovie_name(String movie_name) {
        this.movie_name = movie_name;
    }

    public void setMovie_number(int movie_number) {
        this.movie_number = movie_number;
    }

    public void setR(double r) {
        this.r = r;
    }

    public void setReviews(int reviews) {
        this.reviews = reviews;
    }

    public double getR() {
        return r;
    }

    public int getMovie_number() {
        return movie_number;
    }

    public int getReviews() {
        return reviews;
    }

    public String getMovie_name() {
        return movie_name;
    }

    public static class good_movie implements Comparator<GoodMovie>{
                @Override
                public int compare(GoodMovie o1, GoodMovie o2) {
                    return Double.compare(o1.r, o2.r);

                }
            };

}
