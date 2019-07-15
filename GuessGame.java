package word.count;
import java.util.*;
import java.io.*;

public class GuessGame {
    private int correct;
    private int incorrect;

    public GuessGame(){
        this.correct = 0;
        this.incorrect = 0;
    }

    public char[] check(char c, String movie, char[] tmp_mv){
        boolean found_it = false;
        for (int i = 0; i < movie.length(); i++) {
            if (movie.charAt(i) == c){
                    tmp_mv[2*i] = c;
                    found_it = true;
                    correct++;
                    //System.out.println("Correct values:" + correct);
            }
        }
        if(found_it == false){
            incorrect++;
        }
        return tmp_mv;
    }

    public int get_incorrect(){
        return incorrect;
    }

    public int get_correct(){
        return correct;
    }
}


class Main {
    public static void main(String[] args) throws Exception{
        int points = 0;
        GuessGame game = new GuessGame();
        File file = new File("movies.txt");
        String s = choose(file);
        int movie_len = 0;
        for (int i = 0; i < s.length() ; i++) {
            if(Character.isLetter(s.charAt(i))){
                movie_len++;
            }
        }
        char[] blank_mv = new char[s.length()*2];
        System.out.print("You are Guessing: ");
        for (int i = 0; i < s.length(); i++) {
            if((s.charAt(i) >= 65 && s.charAt(i) <= 90) || (s.charAt(i) >= 97 && s.charAt(i) <= 122)){
                blank_mv[2*i] = '_';
                blank_mv[2*i+1] = ' ';
            }
            else if(s.charAt(i) == ' '){
                blank_mv[2*i] = ' ';
                blank_mv[2*i+1] = ' ';
            }
            else{
                blank_mv[2*i] = s.charAt(i);
                blank_mv[2*i + 1] = ' ';
            }
        }
        System.out.println(blank_mv);

        while (points < 10){
            Scanner scan = new Scanner(System.in);
            char guess = scan.nextLine().charAt(0);
            char[] tmp_mv = game.check(guess, s, blank_mv);
            points = game.get_incorrect();
            //System.out.println("Movie length:"+ movie_len);
            //System.out.println("correct value"+game.get_correct());
            if(game.get_correct() == movie_len){
                System.out.println("You won!");
                System.out.println("You have guessed \"" + s + "\" correctly");
                System.exit(0);
            }
            System.out.println("You are guessing: " + new String(tmp_mv));
            System.out.println("You are guessed (" + points + ") wrongs letters");

        }
        if(points == 10){
            System.out.println("You loose!");
            System.out.println("Correct movie was: " + s );
        }
    }

    public static String choose(File f) throws FileNotFoundException
    {
        String result = null;
        Random rand = new Random();
        int n = 0;
        for(Scanner sc = new Scanner(f); sc.hasNext(); )
        {
            ++n;
            String line = sc.nextLine();
            if(rand.nextInt(n) == 0)
                result = line;
        }

        return result;
    }
}
