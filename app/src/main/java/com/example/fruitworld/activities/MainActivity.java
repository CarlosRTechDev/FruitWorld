package com.example.fruitworld.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.fruitworld.R;
import com.example.fruitworld.adapter.FruitAdapter;
import com.example.fruitworld.model.Fruit;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView listView;
    private GridView gridView;
    //CHA: FALTA ADAPTER
    private FruitAdapter adapterListView;
    private FruitAdapter adapterGridView;

    //Lista del modelo
    private List<Fruit> fruits;

    //items en option menu
    private MenuItem itemListView;
    private MenuItem itemGridView;

    //Variables
    private int counter = 0;
    private final int SWITCH_TO_LIST_VIEW = 0;
    private final int SWITCH_TO_GRID_VIEW = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //mostrar icono
        this.enforceIconBar();

        this.fruits = getAllFruits();

        this.listView = findViewById(R.id.listView);
        this.gridView = findViewById(R.id.gridView);

        //Adjuntamos mismo metodo clic para ambos
        this.listView.setOnItemClickListener(this);
        this.gridView.setOnItemClickListener(this);

        this.adapterListView = new FruitAdapter(this, R.layout.list_view_item_fruit, fruits);
        this.adapterGridView = new FruitAdapter(this, R.layout.grid_view_item_fruit, fruits);

        this.listView.setAdapter(adapterListView);
        this.gridView.setAdapter(adapterGridView);

        //Registrar el context menu para ambos
        registerForContextMenu(this.listView);
        registerForContextMenu(this.gridView);
    }

    private void enforceIconBar(){
        getSupportActionBar().setIcon(R.mipmap.ic_launcher_devil_fruit);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        this.clickFruit(fruits.get(position));
    }

    private void clickFruit(Fruit fruit){
        //Diferenciamos entre las frutas conocidas y desconocidas
        if(fruit.getOrigin().equals("Unknown")){
            Toast.makeText(this, "Sorry, we don't have many info about: " + fruit.getName(), Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "The best fruit from: " + fruit.getOrigin() + " is " + fruit.getName(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflamos nuestro option menu con nuestro layout
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        //Despues de inflar,, recogemos las referencias a los botones que nos interesan
        this.itemListView = menu.findItem(R.id.list_view);
        this.itemGridView = menu.findItem(R.id.grid_view);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Eventos para los clics en los botones del option menu
        switch (item.getItemId()){
            case R.id.add_fruit:
                this.addFruit(new Fruit("Added nº" + (++counter), R.drawable.devil_fruit, "Unknown"));
                return true;
            case R.id.list_view:
                this.switchListGridView(this.SWITCH_TO_LIST_VIEW);
                return true;
            case R.id.grid_view:
                this.switchListGridView(this.SWITCH_TO_GRID_VIEW);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        //Inflamos el context menu con nuestro layout
        MenuInflater inflater = getMenuInflater();
        //Antes de inflar, añadimos el header dependiendo del objeto que se pinche
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        menu.setHeaderTitle(this.fruits.get(info.position).getName());
        //Imflamos
        inflater.inflate(R.menu.context_menu_fruits, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        //Obtener info en el context menu del objeto que se pinche
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()){
            case R.id.delete_fruit:
                this.deleteFruit(info.position);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void switchListGridView(int option){
        //Metodo para cambiar entre List/Grid
        if(option == SWITCH_TO_LIST_VIEW) {
            //Si queremos cambiar a listview, y el listview esta en modo invisible...
            if(this.listView.getVisibility() == View.INVISIBLE) {
                //escondemos el gridview y mostramos el boton en el menu de opciones
                this.gridView.setVisibility(View.INVISIBLE);
                this.itemGridView.setVisible(true);
                //No olvidemos mostrar el list view, y esconder su boton en el menu de opciones
                this.listView.setVisibility(View.VISIBLE);
                this.itemListView.setVisible(false);
            }
        } else if(option == SWITCH_TO_GRID_VIEW) {
            //Si queremos cambiar a gridview, y el gridview esta en modo invisible...
            if(this.gridView.getVisibility() == View.INVISIBLE) {
                //escondemos el listView y mostramos el boton en el menu de opciones
                this.listView.setVisibility(View.INVISIBLE);
                this.itemListView.setVisible(true);
                //no olvides mostrar el gridView y esconder el boton en el menu de opciones
                this.gridView.setVisibility(View.VISIBLE);
                this.itemGridView.setVisible(false);
            }
        }
    }


    //CRUD actions Get, Add, Delete
    private List<Fruit> getAllFruits(){
        List<Fruit> list = new ArrayList<Fruit>(){{
            add(new Fruit("Banana", R.drawable.ic_banana, "Mexico"));
            add(new Fruit("Strawberry", R.drawable.ic_strawberry, "USA"));
            add(new Fruit("Orange", R.drawable.ic_orang, "China"));
            add(new Fruit("Apple", R.drawable.ic_apple, "Italy"));
            add(new Fruit("Cherry", R.drawable.ic_cherry, "Turkey"));
            add(new Fruit("Pear", R.drawable.ic_pear, "Europe"));
            add(new Fruit("Raspberry", R.drawable.ic_raspberry, "Greece"));
        }};

        return list;
    }

    private void addFruit(Fruit fruit){
        this.fruits.add(fruit);
        //Avisamos del cambio en ambos adapters
        this.adapterListView.notifyDataSetChanged();
        this.adapterGridView.notifyDataSetChanged();
    }

    private void deleteFruit(int position){
        this.fruits.remove(position);
        //Avisamos del cambio en ambos adapters
        this.adapterListView.notifyDataSetChanged();
        this.adapterGridView.notifyDataSetChanged();
    }
}