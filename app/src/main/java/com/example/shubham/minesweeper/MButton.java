package com.example.shubham.minesweeper;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.Button;

/**
 * Created by shubham on 6/18/2018.
 */

public class MButton extends android.support.v7.widget.AppCompatButton {
    private int mines=0;
    public int x,y;
    public boolean revealStatus=false,flag=false;

private int mineStatus=MainActivity.Mine_Not_Set;


    public MButton(Context context) {
        super(context);
    }

    public void setXY(int x,int y) {
        this.x = x;
        this.y=y;
    }



    public void setMines(MButton board[][], int nrow, int ncol)
    {
        mineStatus=MainActivity.Mine_Set;
        int  i,j;
        for(i=this.x-1;i<this.x+2;i++)
        {
            for (j=this.y-1;j<this.y+2;j++)
            {
                if(i>=0&&i<nrow&&j>=0&&j<ncol&&board[i][j].mineStatus==MainActivity.Mine_Not_Set)
                    board[i][j].mines++;

            }
        }

    }
    public int getMineStatus()
    {
        return this.mineStatus;
    }
    private int getMines()
    {
        if(this.mineStatus==MainActivity.Mine_Not_Set)
            return this.mines;
        else
            return this.mineStatus;
    }
    public void reveal(MButton board[][],int nrow,int ncol)
    {
        int m=this.getMines();
        if(!revealStatus&&!flag) {
            if (m != MainActivity.Mine_Set && mines == 0) {
                this.setEnabled(false);
                revealStatus=true;
                revealAround(board, nrow, ncol, this.x, this.y);
                this.setBackgroundResource(R.color.buttonDis);
            } else if (m != MainActivity.Mine_Set && mines != 0) {
                this.setText("" + m);
                revealStatus=true;
                this.setEnabled(false);

            } else if(MainActivity.Mine_Set==this.mineStatus) {
                this.setBackgroundResource(R.drawable.mines1);
                revealStatus=true;
                MainActivity.currentStatus=MainActivity.PLAYER_LOST;
                revealMines(board,nrow,ncol);
            }
        }
    }

    public void revealAround(MButton[][] board, int nrow, int ncol, int x, int y) {
        int i,j;
        for(i=x-1;i<x+2;i++)
        {
            for (j=y-1;j<y+2;j++)
            {
                if(i>=0&&i<nrow&&j>=0&&j<ncol&&board[i][j].mineStatus==MainActivity.Mine_Not_Set&&!(i==x&&j==y)) {
                    int m = board[i][j].getMines();
                    if (m != MainActivity.Mine_Set && m == 0) {
                        board[i][j].setEnabled(false);
                        revealStatus = true;
                        board[i][j].reveal(board, nrow, ncol);
                        }

                    else if(m!=MainActivity.Mine_Set&& m!=0)
                    {
                        board[i][j].setText(""+m);
                        revealStatus=true;
                        board[i][j].setEnabled(false);
                    }
                }


            }
        }
    }

    public void revealMines(MButton board[][],int nrow,int ncol)
    {
        for(int i=0;i<nrow;i++)
        {
            for(int j=0;j<ncol;j++)
            {
                if(board[i][j].mineStatus==MainActivity.Mine_Set)
                board[i][j].reveal(board,nrow,ncol);
            }
        }
    }



    public boolean notMine() {
        return this.mineStatus==MainActivity.Mine_Not_Set;
    }
}
