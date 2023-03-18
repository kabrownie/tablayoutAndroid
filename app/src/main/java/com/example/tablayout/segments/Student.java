package com.example.tablayout.segments;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.tablayout.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;


public class Student extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.student, container,false);
// Get reference
        EditText fName = view.findViewById(R.id.editTextFirstName);
        EditText sName = view.findViewById(R.id.editTextMiddleName);
        EditText lName = view.findViewById(R.id.editTextLastName);
        EditText idNo = view.findViewById(R.id.editTextIdNumber);
        EditText RegNo = view.findViewById(R.id.editTextRegistrationNo);
        RadioGroup genderGrup = view.findViewById(R.id.radio_gender);
        Button submit = view.findViewById(R.id.buttonSubmit);
        Button cancel = view.findViewById(R.id.buttonCancel);

        int radioButtonID = genderGrup.getCheckedRadioButtonId();
        RadioButton selectedGender = view.findViewById(radioButtonID);

        Spinner SchSp = view.findViewById(R.id.spinnerSchool);
        Spinner DepSp = view.findViewById(R.id.spinnerDept);
        Spinner cousSp = view.findViewById(R.id.spinnerCourse);
//define option for spinners
        String[] school_arr = new String[]{"School of Engineering", "School of CS & IT", "School of Nursing", "School of business"};
        String[][]dept_arr = {{" Department of Mechanical Engineering", "Department of Mechatronic Engineering", "Department of Electrical and Electronic Engineering","  Department of Civil Engineering"},
                                {"  Department of Computer science", "  Department of It"},
                                {"  Department of nursing"},
                                {"  Department of BBA","  Department of Bcom"}};
        String[][]course_arr = {{" Bsc Mechanical Engineering", "Bsc Mechatronic Engineering", "Bsc Electrical and Electronic Engineering","  Bsc Civil Engineering"},
                {"  Bsc Computer science", " Bsc IT","BB IT"},
                {"  Bsc nursing"},
                {"  Bachelor of business administration"," Bachelor of commerce"}};



// Set up the dept spinner based on the selected value of the school
        SchSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] options = dept_arr[position];
                ArrayAdapter<String> adapter2 = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, options);
                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                DepSp.setAdapter(adapter2);

                // Set up the course spinner based on the selected value of the school
                        String[] options3 = course_arr[position];
                        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, options3);
                        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        cousSp.setAdapter(adapter3);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }

        });



//set adapter for spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, school_arr);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SchSp.setAdapter(adapter);


//send data to firestore when button is clicked
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String firstName, SecondName, LastNAme, idNamba, RegNamba;
                firstName = String.valueOf(fName.getText());
                SecondName = String.valueOf(sName.getText());
                LastNAme = String.valueOf(lName.getText());
                idNamba = String.valueOf(idNo.getText());
                RegNamba = String.valueOf(RegNo.getText());

                String selectedGender_Value = selectedGender.getText().toString();
                String selectedSchool= (String) SchSp.getSelectedItem();
                String selectedDep= (String) DepSp.getSelectedItem();
                String selectedcourse= (String) cousSp.getSelectedItem();




                if (TextUtils.isEmpty(firstName) | TextUtils.isEmpty(SecondName) | TextUtils.isEmpty(LastNAme) | TextUtils.isEmpty(idNamba) | TextUtils.isEmpty(RegNamba)) {
                    Toast.makeText(getContext(), "Enter all inputs!", Toast.LENGTH_SHORT).show();
                }
                else if(idNamba.length() != 8) {
                    Toast.makeText(getContext(), "id number must be 8 characters!", Toast.LENGTH_SHORT).show();

                }



                else{
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    HashMap<String,String> data=new HashMap<>();
                    data.put("First Name",firstName);
                    data.put("Second Name",SecondName);
                    data.put("Last Name",LastNAme);
                    data.put("Id Number",idNamba);
                    data.put("Registration number",RegNamba);
                    data.put("Gender",selectedGender_Value);
                    data.put("School",selectedSchool);
                    data.put("department",selectedDep);
                    data.put("course",selectedcourse);


//check if user with that id exist
                    DocumentReference docRef = db.collection("Students").document(idNo.getText().toString());

                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Toast.makeText(getContext(), "student with that id already exists!", Toast.LENGTH_LONG).show();
                                }
                                else{
                                    Toast.makeText(getContext(), "data added successfully.", Toast.LENGTH_LONG).show();

                                }
                    };

//add user
                    db.collection("Students")
                            .document(idNo.getText().toString()).set(data)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getContext(), "data added successfully.", Toast.LENGTH_LONG).show();

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(), "Error adding data, check internet and try again", Toast.LENGTH_LONG).show();

                                }
                            });

                }


            });
        }

    }
});
        return view;
    }}















