package com.hydraz.trungnam1992.realmdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.exceptions.RealmException;

public class MainActivity extends AppCompatActivity {

    private Realm realm;

    private RealmConfiguration realmConfiguration;

    @BindView(R.id.btn_add_person)
    Button buttonAddPerson;

    @BindView(R.id.edit_person_name)
    EditText editTextPersonName;

    @BindView(R.id.edit_dog_name)
    EditText editTextDogName;

    @BindView(R.id.edit_dog_color)
    EditText editTextDogColor;

    @BindView(R.id.select_dog_list)
    AppCompatSpinner spinnerSelectDog;

    RealmChangeListener callback;
    private ArrayAdapter<String> spinnerAdapter;
    private RealmResults<Dog> listdog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ButterKnife.bind(this);


        realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);
        realm = Realm.getDefaultInstance();


        

    }

    public void initSpinner(){

        listdog= realm.where(Dog.class).findAllAsync();
        spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, android.R.id.text1);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSelectDog.setAdapter(spinnerAdapter);

        
        listdog.addChangeListener(new RealmChangeListener<RealmResults<Dog>>() {
            @Override
            public void onChange(RealmResults<Dog> element) {
                listdog = element;
                spinnerAdapter.clear();
                for (int i = 0 ; i < element.size() ; i++ ){
                    spinnerAdapter.add(element.get(i).name.toString());
                    spinnerAdapter.notifyDataSetChanged();

                }
            }
        });





    }

    @OnClick(R.id.btn_add_person)
    public void addPerson(){
        final Person person  = new Person();
        person.name= editTextPersonName.getText().toString();

        person.dogs= listdog.get(spinnerSelectDog.getSelectedItemPosition());

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                try {
                    realm.copyToRealm(person);
                    Toast.makeText(getApplicationContext(),"Success: "+ person.name , Toast.LENGTH_SHORT).show();

                }catch (RealmException e){
                    Toast.makeText(getApplicationContext(),"Fail: "+ person.name , Toast.LENGTH_SHORT).show();

                }
            }
        });



    }

    @OnClick(R.id.btn_add_dog)
    public void addDog(){

        final Dog dog = new Dog();
        dog.color = editTextDogColor.getText().toString();
        dog.name = editTextDogName.getText().toString();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm)  {
                try {
                    realm.copyToRealm(dog);
                    Toast.makeText(getApplicationContext(),"Success: "+ dog.name , Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),"Fail: "+ dog.name , Toast.LENGTH_SHORT).show();

                }

            }
        });

        initSpinner();

    }

    @OnClick(R.id.btn_view_all_person)
    public void openListAllPerson()
    {
        Intent intent = new Intent(this, ListPersonActivity.class);
        this.startActivity(intent);
    }

    @Override
    protected void onStart(){
            super.onStart();
            initSpinner();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close(); // Remember to close Realm when done.
    }

}
