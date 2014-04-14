package upv.welcomeincoming.app;

import android.app.Activity;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class Home extends ActionBarActivity {

    private	String[]	opcionesMenu;
    private	DrawerLayout	drawerLayout;
    private LinearLayout drawerList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        opcionesMenu	=	new	String[]{getString(R.string.menu_option1),getString(R.string.menu_option2),getString(R.string.menu_option3),getString(R.string.menu_option4)};
        drawerLayout	=	(DrawerLayout)	findViewById(R.id.drawer_layout);
        drawerList	=	(LinearLayout)	findViewById(R.id.home_drawer);
        inicializarElementos();


    }

    private void inicializarElementos() {
        //Home
        View itemHome = generateItem(getString(R.string.menu_option1),R.drawable.home);
        itemHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        drawerList.addView(itemHome);
        addDividier();

        //Find
        View itemFind = generateItem(getString(R.string.menu_option2), R.drawable.ic_location_place);
        itemFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        drawerList.addView(itemFind);
        addDividier();

        //Info
        View itemInfo = generateItem(getString(R.string.menu_option3), R.drawable.ic_action_about);
        itemInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        drawerList.addView(itemInfo);
        addDividier();

        //KitDev
        View itemKitDev = generateItem(getString(R.string.menu_option4), R.drawable.ic_device_access_mic);
        itemKitDev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        drawerList.addView(itemKitDev);
        addDividier();

    }


    private View generateItem(String texto, int idIcon){
        View item = this.getLayoutInflater().inflate(R.layout.item_drawer, null);
        TextView tv = (TextView)item.findViewById(R.id.tv_item_listdrawer);
        ImageView iv = (ImageView)item.findViewById(R.id.iv_item_listdrawer);
        tv.setText(texto);
        tv.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/futura_font.ttf"));
        iv.setBackgroundResource(idIcon);
        return item;
    }

    private void addDividier() {
       View v = this.getLayoutInflater().inflate(R.layout.dividier_itemdrawer, null);
       drawerList.addView(v);
    }
}