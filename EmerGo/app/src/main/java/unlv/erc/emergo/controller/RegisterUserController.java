package unlv.erc.emergo.controller;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import helper.DatabaseHelper;
import helper.MaskHelper;
import unlv.erc.emergo.R;
import unlv.erc.emergo.model.User;


public class RegisterUserController extends Activity {
    private EditText fullName;
    private EditText birthday;
    private Spinner typeBlood;
    private Spinner cardiac;
    private Spinner diabect;
    private Spinner hypertension;
    private Spinner seropositive;
    private Button saveButton;
    private Button updateButton;
    private Button deleteButton;
    private String nameUser;
    private String birthdayUser;
    private String typeBloodUser;
    private String cardiacUser;
    private String diabeticUser;
    private String hypertensionUser;
    private String seropositiveUser;
    private String id = "1";
    private static List<User> userList = new ArrayList<User>();

    DatabaseHelper myDatabase;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_user);

        myDatabase = new DatabaseHelper(this);

        fullName = (EditText) findViewById(R.id.fullNameEditText);
        birthday = (EditText) findViewById(R.id.birthdayEditText);
        typeBlood = (Spinner) findViewById(R.id.typeBloodSpinner);
        birthday.addTextChangedListener(MaskHelper.insert("##/##/####", birthday));
        cardiac = (Spinner) findViewById(R.id.cardiacSpinner);
        diabect = (Spinner) findViewById(R.id.diabeticSpinner);
        hypertension = (Spinner) findViewById(R.id.hipertensionSpinner);
        seropositive = (Spinner) findViewById(R.id.soropositiveSpinner);
        saveButton = (Button) findViewById(R.id.saveButton);
        updateButton = (Button) findViewById(R.id.updateButton);
        deleteButton = (Button) findViewById(R.id.deleteButton);

        if(verifDatabase() == false){
            saveButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    createUser();
                }
            });
            updateButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    upgradeUser();
                    showUser();
                }
            });
            deleteButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    deleteUser();
                }
            });
        }else{
            disableOptions();
        }
    }

    public void createUser(){

        nameUser = fullName.getText().toString();
        birthdayUser = birthday.getText().toString();
        typeBloodUser = typeBlood.getSelectedItem().toString();
        cardiacUser = cardiac.getSelectedItem().toString();
        diabeticUser = diabect.getSelectedItem().toString();
        hypertensionUser = hypertension.getSelectedItem().toString();
        seropositiveUser = seropositive.getSelectedItem().toString();

        if(nameUser.isEmpty()) {
            showMessage("Nome Vazio! Informe seu nome completo");
            fullName.requestFocus();
        }else if(birthdayUser.isEmpty()){
            showMessage("Data de Nascimento vazia! Informe sua data de nascimento");
            birthday.requestFocus();
        }else if(typeBloodUser.isEmpty()){
            showMessage("Tipo Sanguíneo vazio! Informe o seu tipo sanguíneo");
            typeBlood.requestFocus();
        }else if(cardiacUser.isEmpty()){
            showMessage("Você é cardiaco? Informe se sim ou não");
            cardiac.requestFocus();
        }else if(diabeticUser.isEmpty()){
            showMessage("Você é diabetico? Informe se sim ou não");
            diabect.requestFocus();
        }else if(hypertensionUser.isEmpty()){
            showMessage("Você é hipertenso? Informe se sim ou não");
            hypertension.requestFocus();
        }else if(seropositiveUser.isEmpty()){
            showMessage("Você possui soropositivo? Informe se sim ou não");
            seropositive.requestFocus();
        }else {
            myDatabase.insertUser(id,nameUser,birthdayUser,typeBloodUser,cardiacUser,typeBloodUser,
                                  hypertensionUser,seropositiveUser);
            showMessage("Usuário Cadastrado Com Sucesso!");
        }
    }

    public void upgradeUser(){
        nameUser = fullName.getText().toString();
        birthdayUser = birthday.getText().toString();
        typeBloodUser = typeBlood.getSelectedItem().toString();
        cardiacUser = cardiac.getSelectedItem().toString();
        diabeticUser = diabect.getSelectedItem().toString();
        hypertensionUser = hypertension.getSelectedItem().toString();
        seropositiveUser = seropositive.getSelectedItem().toString();



            myDatabase.updateUser(id,nameUser,birthdayUser,typeBloodUser,cardiacUser,typeBloodUser,
                    hypertensionUser,seropositiveUser);
            showMessage("Alteração Realizada Com Sucesso!");

    }

    public void deleteUser(){
        myDatabase.deleteUser(id);
        showMessage("Usuario excluido com sucesso");
    }
    public void disableOptions() {

        fullName.setClickable(false);
        birthday.setClickable(false);
        typeBlood.setClickable(false);
        cardiac.setClickable(false);
        diabect.setClickable(false);
        hypertension.setClickable(false);
        seropositive.setClickable(false);
        saveButton.setClickable(false);
        updateButton.setClickable(true);
        deleteButton.setClickable(true);
    }

    public void clearText(){

        fullName.setText("");
        birthday.setText("");
        typeBlood.requestFocus();
        cardiac.requestFocus();
        diabect.requestFocus();
        hypertension.requestFocus();
        seropositive.requestFocus();
    }

    public void showMessage(String message){
        Toast.makeText(this,""+message,Toast.LENGTH_SHORT).show();
    }
    public void showMessageDialog(String title,String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
    public void showUser(){
        Cursor res = myDatabase.getUser();
        if(res.getCount() == 0){
            showMessageDialog("Error","Nothing found");
            return;
        }
        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()){
            buffer.append("NAME :"+ res.getString(0)+"\n");
            buffer.append("BIRTHDAY :"+ res.getString(1)+"\n");
        }
        showMessageDialog("Data",buffer.toString());
    }
    public boolean verifDatabase(){
        Cursor cursor = myDatabase.getUser();
        if(cursor.getCount()==0){
            showMessageDialog("Error","Nothing found");
            return false;
        }else
            return true;
    }
}