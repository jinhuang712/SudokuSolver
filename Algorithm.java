

/**
 * this program was produced by Jin Huang
 */
public class Algorithm {
    static boolean status;
    public static void fill(Sudoku sudoku) {
        do {
            status = false;
            fillInRow(sudoku);
            fillInCol(sudoku);
            fillInBox(sudoku);
            scan_fillIfOnly(sudoku);
            if(status == false)
                break;
        } while(!sudoku.isFilled());
    }

    // fill in all row
    public static void fillInRow(Sudoku sudoku) {
        for(int num=1; num<=9; num++)
            fillIfRestCant_row(sudoku, num);
    }

    //do fillIfRestCant_row(Sudoku sudoku, int num, int row)
    //for all rows
    public static void fillIfRestCant_row(Sudoku sudoku, int num) {
        for(int row=0; row<9; row++)
            fillIfRestCant_row(sudoku, num, row);
    }

    //fill if the num is not fillable in the rest of the row
    public static void fillIfRestCant_row(Sudoku sudoku, int num, int row) {
        if(sudoku.ifNumberExistInRow(num, row))
            return;
        int[] place = new int[2];
        int count = 0;
        for(int col=0; col<9; col++) {
            if(sudoku.fillable(num, new int[]{row, col}))
                place = new int[]{row, col};
            if(!sudoku.fillable(num, new int[]{row, col}))
                count++;
        }
        if(count==8) {
            sudoku.sdk[place[0]][place[1]] = num;
            status = true;
        }
    }

    // fill in all columns
    public static void fillInCol(Sudoku sudoku) {
        for(int num=1; num<=9; num++)
            fillIfRestCant_col(sudoku, num);
    }

    //do fillIfRestCant_col(Sudoku sudoku, int num, int col)
    //for all columns
    public static void fillIfRestCant_col(Sudoku sudoku, int num) {
        for(int col=0; col<9; col++)
            fillIfRestCant_col(sudoku, num, col);
    }

    //fill if the num is not fillable in the rest of the col
    public static void fillIfRestCant_col(Sudoku sudoku, int num, int col) {
        if(sudoku.ifNumberExistInCol(num, col))
            return;
        int[] place = new int[2];
        int count = 0;
        for(int row=0; row<9; row++) {
            if(sudoku.fillable(num, new int[]{row,col}))
                place = new int[]{row,col};
            if(!sudoku.fillable(num, new int[]{row,col}))
                count++;
        }
        if(count==8) {
            sudoku.sdk[place[0]][place[1]] = num;
            status = true;
        }
    }

    //fill in all boxes
    public static void fillInBox(Sudoku sudoku) {
        for(int box=0; box<=360; box+=45)
            fillIfRestCant_box(sudoku, box);
    }

    //do fillIfRestCant_box(Sudoku sudoku, int num, int box)
    //for all numbers
    public static void fillIfRestCant_box(Sudoku sudoku, int box) {
        for(int i=1; i<=9; i++)
            fillIfRestCant_box(sudoku, i, box);
    }

    //fill if the num is not fillable in the rest of the box
    public static void fillIfRestCant_box(Sudoku sudoku, int num, int box) {
        if(sudoku.ifNumberExistInBox(num, box))
            return;
        int[][] initial = sudoku.toInitial(box);
        int x1 = initial[0][0], x2 = initial[0][1];
        int y1 = initial[1][0], y2 = initial[1][1];
        int count = 0;
        int[] place = new int[2];
        for(int ro=x1; ro<x2; ro++) {
            for(int co=y1; co<y2; co++) {
                if(sudoku.fillable(num, new int[]{ro,co}))
                    place = new int[]{ro,co};
                if(!sudoku.fillable(num, new int[]{ro,co}))
                    count++;
            }
        }
        if(count==8) {
            sudoku.sdk[place[0]][place[1]] = num;
            status = true;
        }
    }

    //fill in all positions in the sudoku if it only exists one solution
    public static void scan_fillIfOnly(Sudoku sudoku) {
        for(int ro=0; ro<9; ro++)
            for(int co=0; co<9; co++)
                scan_fillIfOnly(sudoku, new int[]{ro,co});
    }

    //fill if the position only has one solution
    public static void scan_fillIfOnly(Sudoku sudoku, int[] place) {
        int ro = place[0];
        int co = place[1];
        if(sudoku.getOnly(place) == -1)
            return;
        if(sudoku.getOnly(place) == sudoku.sdk[ro][co])
            return;
        sudoku.sdk[ro][co] = sudoku.getOnly(place);
        status = true;
    }

}
