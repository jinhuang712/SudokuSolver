
import java.io.*;
import java.util.*;

/**
 * this program was produced by Jin Huang
 */
public class Sudoku {
    public int[][] sdk;
    public final int RIGHT = 0;
    public final int TOP_RIGHT = 45;
    public final int TOP = 90;
    public final int TOP_LEFT = 135;
    public final int LEFT = 180;
    public final int BOT_LEFT = 225;
    public final int BOT = 270;
    public final int BOT_RIGHT = 315;
    public final int MIDDLE = 360;

    public Sudoku() {
        this.sdk = new int[9][9];
    }

    public Sudoku(int[][] sdk) {
        this.sdk = sdk;
    }

    public void importSudoku(String filename) throws FileNotFoundException {
        Scanner input = new Scanner(new File(filename));
        for(int i=0; i<9; i++) {
            if(!input.hasNextLine())
                break;
            Scanner lineReader = new Scanner(input.nextLine());
            for(int j=0; j<9; j++)
                sdk[i][j] = lineReader.nextInt();
            lineReader.close();
        }
        input.close();
    }

    public ArrayList<int[]> compare(Sudoku sudoku) {
        ArrayList<int[]> list = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (sdk[i][j] != sudoku.sdk[i][j])
                    list.add(new int[] {i, j});
            }
        }
        return list;
    }

    //returns the position array of the number in box
    public int[] findNumberInBox(int num, int place) {
        int[][] initial = toInitial(place);
        int x1 = initial[0][0], x2 = initial[0][1];
        int y1 = initial[1][0], y2 = initial[1][1];

        for(int ro=x1; ro<x2; ro++) {
            for(int co=y1; co<y2; co++) {
                int answer = sdk[ro][co];
                if(answer == num)
                    return new int[]{ro, co};
            }
        }
        return new int[]{-1, -1};
    }

    public boolean ifNumberExistInBox(int num, int place) {
        int[] ans = findNumberInBox(num, place);
        if (ans[0] == -1)
            return false;
        return true;
    }

    //returns
    public int[][] toInitial(int place) {
        int x1 = -1, x2 = -1, y1 = -1, y2 = -1;
        switch(place) {
            case RIGHT: y1 = 3; y2 = 6; x1 = 6; x2 = 9;
                break;
            case TOP_RIGHT: y1 = 0; y2 = 3; x1 = 6; x2 = 9;
                break;
            case TOP: y1 = 0; y2 = 3; x1 = 3; x2 = 6;
                break;
            case TOP_LEFT: y1 = 0; y2 = 3; x1 = 0; x2 = 3;
                break;
            case LEFT: y1 = 3; y2 = 6; x1 = 0; x2 = 3;
                break;
            case BOT_LEFT: y1 = 6; y2 = 9; x1 = 0; x2 = 3;
                break;
            case BOT: y1 = 6; y2 = 9; x1 = 3; x2 = 6;
                break;
            case BOT_RIGHT: y1 = 6; y2 = 9; x1 = 6; x2 = 9;
                break;
            case MIDDLE: y1 = 3; y2 = 6; x1 = 3; x2 = 6;
                break;
        }
        return new int[][] {{x1,x2}, {y1,y2}};
    }

    //returns the row number of the number in col
    public int findNumberInCol(int num, int col) {
        for (int i = 0; i < 9; i++) {
            int answer = sdk[i][col];
            if (answer == num)
                return i;
        }
        return -1;
    }

    public boolean ifNumberExistInCol(int num, int col) {
        if (findNumberInCol(num, col) == -1)
            return false;
        return true;
    }

    //returns the column number of the number in row
    public int findNumberInRow(int num, int row) {
        for (int i = 0; i < 9; i++) {
            int answer = sdk[row][i];
            if (answer == num)
                return i;
        }
        return -1;
    }

    public boolean ifNumberExistInRow(int num, int row) {
        if (findNumberInRow(num, row) == -1)
            return false;
        return true;
    }

    //turns the location array of a number into its designated box
    public int axisToBox(int[] place) {
        int ro = place[0];
        int co = place[1];
        if (ro < 3) {
            if (co < 3)
                return TOP_LEFT;
            if (co < 6)
                return LEFT;
            if (co < 9)
                return BOT_LEFT;
        } else if (ro < 6) {
            if (co < 3)
                return TOP;
            if (co < 6)
                return MIDDLE;
            if (co < 9)
                return BOT;
        } else if (ro < 9) {
            if (co < 3)
                return TOP_RIGHT;
            if (co < 6)
                return RIGHT;
            if (co < 9)
                return BOT_RIGHT;
        }
        return -1;
    }

    //check if a number is fillable in a certain position
    public boolean fillable(int num, int[] place) {
        int ro = place[0];
        int co = place[1];
        if (sdk[ro][co] > 0)
            return false;
        if (ifNumberExistInBox(num, axisToBox(place)))
            return false;
        if (ifNumberExistInCol(num, co))
            return false;
        if (ifNumberExistInRow(num, ro))
            return false;
        return true;
    }

    //check if a number is the only solution for a position
    //if the place is the num itself, return true
    //if the place is not the num and not empty, return false
    //if the num is not fillable in the place, return false
    public boolean ifOnly(int num, int[] place) {
        if(!fillable(num, place))
            return false;
        int pos = sdk[place[0]][place[1]];
        if(pos != 0 && num == pos)
            return true;
        if(pos != 0)
            return false;
        int count = 0;
        for (int i = 1; i <= 9; i++) {
            if (fillable(i, place))
                count++;
            if(count>1)
                return false;
        }
        if(count == 1)
            return true;
        return false;
    }

    //get the only num fillable in a position
    //if there are numerous solutions
    //return -1
    public int getOnly(int[] place) {
        for(int num=1; num<=9; num++)
            if(ifOnly(num, place))
                return sdk[place[0]][place[1]];
        return -1;
    }

    //get number of empty positions in a sudoku
    public int getEmpty() {
        int empty = 0;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (sdk[i][j] == 0)
                    empty++;
            }
        }
        return empty;
    }

    public ArrayList<int[]> getEmptyCoordinates() {
        ArrayList<int[]> list = new ArrayList<int[]>();
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                if (sdk[x][y] == 0)
                    list.add(new int[]{x, y});
            }
        }
        return list;
    }

    //check if a sudoku is filled
    public boolean isFilled() {
        if(getEmpty()==0)
            return true;
        else
            return false;
    }
}
