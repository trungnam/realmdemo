package com.hydraz.trungnam1992.realmdemo;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import hugo.weaving.DebugLog;
import io.realm.Realm;
import io.realm.RealmBasedRecyclerViewAdapter;
import io.realm.RealmChangeListener;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.RealmViewHolder;
import io.realm.exceptions.RealmException;

public class ListPersonActivity extends AppCompatActivity {

    //@BindView(R.id.rv_person)
    RealmRecyclerView realmRecyclerViewPerson;

    private Realm mRealm;
    private RealmConfiguration mRealmConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_person);

        mRealmConfig = new RealmConfiguration
                .Builder()
                .build();
        Realm.setDefaultConfiguration(mRealmConfig);
        mRealm = Realm.getDefaultInstance();
        realmRecyclerViewPerson= (RealmRecyclerView)findViewById(R.id.rv_person);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        loadperson();

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRealm.close(); // Remember to close Realm when done.
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }
    private void loadperson() {
        RealmResults<Person> notes = mRealm.where(Person.class).findAll();
        PersonsRecyclerViewAdapter noteAdapter = new PersonsRecyclerViewAdapter(getApplicationContext(), notes);
        realmRecyclerViewPerson.setAdapter(noteAdapter);
    }

    public class PersonsRecyclerViewAdapter extends RealmBasedRecyclerViewAdapter<
            Person, PersonsRecyclerViewAdapter.ViewHolder>  implements RealmChangeListener {

        public PersonsRecyclerViewAdapter(
                Context context,
                RealmResults<Person> realmResults) {
            super(context, realmResults, true, true);
            realmResults.addChangeListener(this);

        }

        @Override
        public void onChange(Object element) {
            notifyDataSetChanged();
        }

        public class ViewHolder extends RealmViewHolder {
            private TextView mName;
            private TextView mDog;
            private Button deleteDog;

            public ViewHolder(RelativeLayout container) {
                super(container);
                this.mName = (TextView) container.findViewById(R.id.txt_person_name);
                this.mDog = (TextView) container.findViewById(R.id.txt_dogs);
                this.deleteDog = (Button) container.findViewById(R.id.btndelete);

            }

        }

        @Override
        public ViewHolder onCreateRealmViewHolder(ViewGroup viewGroup, int viewType) {
            View v = inflater.inflate(R.layout.person_item, viewGroup, false);

            return new ViewHolder((RelativeLayout) v);
        }

        @Override
        public void onBindRealmViewHolder(ViewHolder viewHolder, int position) {
            final Person person = realmResults.get(position);

            viewHolder.mName.setText("Person: "+ person.name);


            if(person.dogs!=null){
                viewHolder.mDog.setText("Dog: " + person.dogs.name);

            }else {
                viewHolder.mDog.setText("NULL");

            }

            viewHolder.deleteDog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(person.dogs!=null){
                        mRealm.executeTransaction(new Realm.Transaction() {
                            @DebugLog
                            @Override
                            public void execute(Realm realm) {
                                try {
                                    RealmResults<Dog> rows = realm.where(Dog.class).equalTo("id",person.dogs.id).findAll();
                                    rows.deleteAllFromRealm();
                                    Toast.makeText(getApplicationContext(),"Success: "+ person.name + " delete dog "  , Toast.LENGTH_SHORT).show();

                                }catch (RealmException e){
                                    Toast.makeText(getApplicationContext(),"Fail: "+ person.name , Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
                    }

                }
            });
        }


    }
}
