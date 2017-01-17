package com.hydraz.trungnam1992.realmdemo;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import io.realm.Realm;
import io.realm.RealmBasedRecyclerViewAdapter;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.RealmViewHolder;

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
            Person, PersonsRecyclerViewAdapter.ViewHolder> {

        public PersonsRecyclerViewAdapter(
                Context context,
                RealmResults<Person> realmResults) {
            super(context, realmResults, true, true);
        }

        public class ViewHolder extends RealmViewHolder {
            private TextView mName;
            private TextView mDog;

            public ViewHolder(RelativeLayout container) {
                super(container);
                this.mName = (TextView) container.findViewById(R.id.txt_person_name);
                this.mDog = (TextView) container.findViewById(R.id.txt_dogs);
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

            viewHolder.mName.setText(person.name);


            if(person.dogs!=null){
                viewHolder.mDog.setText(person.dogs.name);

            }else {
                viewHolder.mDog.setText("NULL");

            }

        }
    }
}
