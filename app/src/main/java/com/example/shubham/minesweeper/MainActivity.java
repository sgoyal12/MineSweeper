package com.example.shubham.minesweeper;

import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;



public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    LinearLayout rootLayout;
    int nrow=10,ncol=10;
    ArrayList<LinearLayout> rows;
    public MButton[][] board;
    public static final int Mine_Set=-1,Mine_Not_Set=-2;
    public static final int INCOMPLETE=1,PLAYER_WON=2,PLAYER_LOST=3;
    public static boolean k=true;
    private static int x1,y1;
     static int currentStatus;
     static int diff=10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rootLayout=findViewById(R.id.RootLayout);
        setBoard();

    }
    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuItem menuItem,menuItem1;
//        SubMenu subMenu,subMenu1;
//        menuItem=menu.findItem(R.id.Preset);
//        menuItem1=menu.findItem(R.id.difficulty);
//        subMenu=menuItem.getSubMenu();
//        subMenu1=menuItem1.getSubMenu();
//        subMenu.setGroupCheckable(R.id.pre,true,true);
//        subMenu1.setGroupCheckable(R.id.diff,true,true);
        getMenuInflater().inflate(R.menu.main_menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.RESET)
            setBoard();
        else if(item.getItemId()==R.id.X6)
        {
            nrow=6;
            ncol=6;
            item.setChecked(true);
            setBoard();
        }
        else if (item.getItemId()==R.id.X8)
        {
            nrow=8;
            ncol=8;
            item.setChecked(true);
            setBoard();
        }
        else if (item.getItemId()==R.id.X10)
        {
            nrow=10;
            ncol=10;
            item.setChecked(true);
            setBoard();
        }

        else if(item.getItemId()==R.id.easy)
        {
            diff=10;
            item.setChecked(true);
            setBoard();

        }
        else if (item.getItemId()==R.id.medium)
        {
            diff=6;
            item.setChecked(true);
            setBoard();
        }
        else if (item.getItemId()==R.id.high)
        {
            diff=4;
            item.setChecked(true);
            setBoard();
        }
        return true;
    }

    public void setBoard()
    {   board=new MButton[nrow][ncol];
        rows=new ArrayList<>();
        k=true;
        currentStatus=INCOMPLETE;
        rootLayout.removeAllViews();
        for(int i=0;i<nrow;i++)
        {
            LinearLayout linearLayout=new LinearLayout(this);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,0,1);
            linearLayout.setLayoutParams(layoutParams);
            rootLayout.addView(linearLayout);
            rows.add(linearLayout);
        }
        for(int i=0;i<nrow;i++)
        {
            for (int j=0;j<ncol;j++)
            {
                MButton button=new MButton(this);
                LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT,1);
                button.setLayoutParams(layoutParams);
               button.setOnLongClickListener(this);
                button.setOnClickListener(this);

                button.setTextColor(getResources().getColor(R.color.textColor));
                GradientDrawable drawable=new GradientDrawable();
                drawable.setShape(GradientDrawable.RECTANGLE);
                drawable.setColor(getResources().getColor(R.color.button));
                drawable.setCornerRadius(25);
                drawable.setStroke(2,getResources().getColor(R.color.textColor));
                button.setBackgroundDrawable(drawable);
                LinearLayout row=rows.get(i);
                row.addView(button);
                board[i][j]=button;
                board[i][j].setXY(i,j);
            }
        }

    }
    public void setupMines(){
        Random random=new Random(),random1=new Random();
        for(int p=0;p<(ncol*nrow)/diff;)
        {
            int x,y;
            boolean z=true;
            x=random.nextInt(nrow);
            y=random1.nextInt(ncol);
            for(int i=x1-1;i<x1+2;i++)
            {
                for (int j=y1-1;j<y1+2;j++)
                {
                    if(x==i&&y==j)
                        z=false;
                }
            }
            if(board[x][y].getMineStatus()!=Mine_Set&&z)
            {
                board[x][y].setMines(board,nrow,ncol);
                p++;
            }
        }
    }
    @Override
    public void onClick(View v) {
        MButton button = (MButton) v;
        if(k)
        {
           k=false;
           x1=button.x;
           y1=button.y;
           setupMines();
        }
        if(currentStatus==INCOMPLETE) {

            button.reveal(board, nrow, ncol);
            updateStatus();
            checkStatus();
        }

    }

    private void checkStatus() {
        if(currentStatus==PLAYER_LOST)
        {
            Toast.makeText(MainActivity.this,"You Lost",Toast.LENGTH_SHORT).show();
        }
        else if(currentStatus==PLAYER_WON){
            Toast.makeText(MainActivity.this,"You Won",Toast.LENGTH_SHORT).show();
        }
    }

    public void updateStatus() {
        boolean abc=true;
        int a=0;
        for(int i=0;i<nrow;i++) {
            for (int j = 0; j < ncol; j++) {
                if (!board[i][j].revealStatus && board[i][j].notMine()) {
                    abc = false;
                }

            }
        }
        for(int i=0;i<nrow;i++)
        {
            for(int j=0;j<ncol;j++)
            {
                if(board[i][j].flag)
                {
                    if(board[i][j].getMineStatus()==Mine_Set)
                    {
                      a++;
                    }
                    else{
                        a=0;
                        break;
                    }
                }
            }
        }
        if(a==nrow*ncol/diff)
            currentStatus=PLAYER_WON;
        if(abc) {
                currentStatus = PLAYER_WON;
            }
        }

    @Override
    public boolean onLongClick(View v) {
        MButton button=(MButton)v;
       if(currentStatus==INCOMPLETE) {
           if (!button.flag) {
               button.flag = true;
               button.setBackgroundResource(R.drawable.flags);
               updateStatus();
               checkStatus();

           } else {
               button.flag = false;
               button.setBackgroundResource(R.color.button);
               GradientDrawable drawable = new GradientDrawable();
               drawable.setShape(GradientDrawable.RECTANGLE);
               drawable.setCornerRadius(25);

               drawable.setColor(getResources().getColor(R.color.button));
               drawable.setStroke(2, getResources().getColor(R.color.textColor));
               button.setBackgroundDrawable(drawable);
           }


       }
        return true;
    }
}

