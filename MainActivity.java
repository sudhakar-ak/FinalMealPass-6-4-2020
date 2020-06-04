package com.example.finalmealpass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

//import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private EditText Userid;
    private EditText Userpin;
    private TextView Msgbox;
    private EditText pin1;
    private EditText pin2;
    private TextView rMsgbox;
    private TextView rMsgbox1;
    private Spinner spinner;
    private EditText editText;
    private String verificationId;
    public String EIDGLOBAL;
    private FirebaseAuth mAuth;
    private EditText neditText;

    public void onBackPressed()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Do you want to exit ?");
        builder.setTitle("Alert !");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which)
                            {
                                finish();
                            }
                        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which)
                            {
                                dialog.cancel();
                            }
                        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_option);
        loginoption();
    }

    public void loginoption() {
        Button elbutton = (Button) findViewById(R.id.elbuttonid);
        elbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.activity_main);
                Mainscreen();
            }
        });

        Button vlbutton = (Button) findViewById(R.id.vlbuttonid);
        vlbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.vendor_login);
                Vendorscreen();
            }
        });
    }


    public void Mainscreen() {
        Msgbox = (TextView) findViewById(R.id.Msgboxid);
        Button lbutton = (Button) findViewById(R.id.lbuttonid);
        lbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Userid = (EditText) findViewById(R.id.vid);
                final String username = Userid.getText().toString().trim();
                Userpin = (EditText) findViewById(R.id.Ipinid);
                final String userpwd = Userpin.getText().toString().trim();
                if (username.equals("")) {
                    Msgbox.setText("EmpID cannot be empty");
                    Msgbox.setTextColor(Color.RED);
                } else {
                    if (username.length() < 7) {
                        Msgbox.setText("EmpID shoould be 7 digits");
                        Msgbox.setTextColor(Color.RED);
                    } else {
                        if (userpwd.equals("")) {
                            Msgbox.setText("Please enter PIN");
                            Msgbox.setTextColor(Color.RED);
                        } else {
                            if (userpwd.length() < 5) {
                                Msgbox.setText("PIN should be 5 digits");
                                Msgbox.setTextColor(Color.RED);
                            } else {
                                Log.d("MyApp", "I am here 3");
                                System.out.println("input id: " + username);
                                final DatabaseReference reference = FirebaseDatabase.getInstance("https://final-mealpass.firebaseio.com/").getReference().child("Passdata").child("E" + username);
                                System.out.println("input id1: " + username);
                                Log.d("MyApp", "I am here 4");
                                reference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            String PIN = dataSnapshot.child("PIN").getValue().toString();
                                            if (PIN.equals(userpwd)) {
                                                updatedatecount("meals");
                                                updatedatecount("snacks");
                                                setContentView(R.layout.pass_screen);
                                                passscreen(username);
                                            } else {
                                                Msgbox.setText("Incorrect PIN");
                                                Msgbox.setVisibility(View.VISIBLE);
                                                Msgbox.setTextColor(Color.RED);
                                            }

                                        } else {
                                            Msgbox.setText("You're not eligible" + "\n" + "Please contact Facilities");
                                            Msgbox.setVisibility(View.VISIBLE);
                                            Msgbox.setTextColor(Color.RED);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                        Msgbox.setText("Please try after sometime!!");
                                        Msgbox.setVisibility(View.VISIBLE);

                                    }
                                });
                            }
                        }
                    }
                }

            }
        });
        Button frbutton = (Button) findViewById(R.id.vbhomeid);
        frbutton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            setContentView(R.layout.reg_eid_verify);
                                            Verifyscreen();
                                        }
                                    }
        );

        Button rptback = (Button) findViewById(R.id.ebhomeid);
        rptback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.login_option);
                loginoption();
            }
        });

    }

    public void updatedatecount(String foodtype)

    {
        Calendar calendar = Calendar.getInstance();
        final String currentDate = DateFormat.getDateInstance().format(calendar.getTime());

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        final String strDate= formatter.format(date);

        final DatabaseReference dtreference1 = FirebaseDatabase.getInstance("https://final-mealpass.firebaseio.com/").getReference().child("Feedback").child(foodtype);

        dtreference1.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String dbdate = dataSnapshot.child("CDATE").getValue().toString();
                String dcount = dataSnapshot.child("COUNT").getValue().toString();
                String mcount = dataSnapshot.child("MCOUNT").getValue().toString();
                int dc = Integer.parseInt(dcount);
                int mc = Integer.parseInt(mcount);
                char check1 =strDate.charAt(0);
                char check2 =strDate.charAt(1);
                int fc = 1;
                if (check1 =='0') {
                    if (check2 == '1') {
                        fc = 0;
                    }
                    else
                    {
                        fc = dc + mc;
                    }
                } else {
                    fc = dc + mc;
                }

                String fcount = Integer.toString(fc);

                if (!dbdate.equals(strDate)) {
                    Map<String,Object> update = new HashMap<>();
                    update.put("CDATE",strDate);
                    update.put("COUNT","0");
                    update.put("MCOUNT",fcount);
                    update.put("STAR1","0");
                    update.put("STAR2","0");
                    update.put("STAR3","0");
                    update.put("STAR4","0");
                    update.put("STAR5","0");

                    dtreference1.updateChildren(update).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            System.out.println("update done!!!!!!!");
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    public void Verifyscreen() {
        EditText Reidv;
        final TextView rmsgid;
        rmsgid = (TextView) findViewById(R.id.reidmsgid);
        Button nbutton = (Button) findViewById(R.id.rnbuttonid);
        nbutton.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(final View view) {
                                           EditText Reidv;
                                           Reidv = (EditText) findViewById(R.id.reidvid);
                                           final String userid = Reidv.getText().toString();
                                           EIDGLOBAL =Reidv.getText().toString();
                                           final DatabaseReference reference = FirebaseDatabase.getInstance("https://final-mealpass.firebaseio.com/").getReference().child("Passdata").child("E" + userid);
                                           reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                               @Override
                                               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                   Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT).show();
                                                   if (dataSnapshot.exists()) {
                                                       // Validate Otp
                                                       Log.d("sudhakar", "datasnapshot exists");
                                                       Log.d("sudhakar", "pass to verify phone num");
//
//                                                       setContentView(R.layout.verify_phone_number);
//                                                       verifyphonenumber(EID, );
                                                       String EID = dataSnapshot.child("EID").getValue().toString();
                                                       String PIN = dataSnapshot.child("PIN").getValue().toString();
                                                       String PHONE = dataSnapshot.child("PHONE").getValue().toString();
  //                                                     String EIDGLOBAL = dataSnapshot.child("EID").getValue().toString();
                                                       verifyphonenumber(EID, PIN, PHONE);
                                                   } else {
                                                       rmsgid.setText("OOPS!! You're not eligible!");
                                                       rmsgid.setVisibility(View.VISIBLE);
                                                       rmsgid.setTextColor(Color.RED);
                                                   }
                                               }

                                               @Override
                                               public void onCancelled(@NonNull DatabaseError databaseError) {

                                                   rmsgid.setText("INVALID cancelled");
                                                   rmsgid.setVisibility(View.VISIBLE);

                                               }
                                           });


                                       }
                                   }
        );
        Button rbbutton = (Button) findViewById(R.id.rbbuttonid);
        rbbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.activity_main);
                Mainscreen();
            }
        });
    }
    //sudhakar
    public void verifyphonenumber(final String EID,final String PIN,final String PHONE) {

        String phoneNumber = "+" + "91" + PHONE;
        verifyotp(phoneNumber);
    }

    public void verifyotp(final String phonenumber) {

        Log.d("sudhakar", "pass to verify phone num");
                setContentView(R.layout.verify_otp);

                mAuth = FirebaseAuth.getInstance();
                editText = findViewById(R.id.otp);

//                String phonenumber = getIntent().getStringExtra("phonenumber");
                sendVerificationCode(phonenumber);

                findViewById(R.id.otp).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String code = editText.getText().toString().trim();

                        if (code.isEmpty() || code.length() < 6) {
                            editText.setError("Enter code...");
                            editText.requestFocus();
                            return;
                        }
                        verifyCode(code);
                    }
                });

            }

            private void verifyCode(String code) {
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
                signInWithCredential(credential);
                Toast.makeText(this, "Sit back and relax we are verifying OTP..", Toast.LENGTH_SHORT).show();
            }

            private void signInWithCredential(PhoneAuthCredential credential) {
                mAuth.signInWithCredential(credential)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(MainActivity.this, "OTP validated", Toast.LENGTH_LONG).show();
                                    Log.d("sudhakar", "Verify phone number Complete");
                                    setContentView(R.layout.set_pin);
                                    pinsetscreen();

                                } else {
                                    Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }

            private void sendVerificationCode(String number) {
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        number,
                        60,
                        TimeUnit.SECONDS,
                        TaskExecutors.MAIN_THREAD,
                        mCallBack
                );

            }

            private PhoneAuthProvider.OnVerificationStateChangedCallbacks
                    mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                @Override
                public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);
                    verificationId = s;
                }

                @Override
                public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                    String code = phoneAuthCredential.getSmsCode();
                    if (code != null) {
                        editText.setText(code);
                        verifyCode(code);
                    }
                }

                @Override
                public void onVerificationFailed(FirebaseException e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            };

    //sudhakar
    public void pinsetscreen() {
        TextView htext = (TextView) findViewById(R.id.Htextid);
        Button cbutton = (Button) findViewById(R.id.cbuttonid);
        cbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pin1 = (EditText) findViewById(R.id.pin1id);
                final String rpin1 = pin1.getText().toString().trim();
                pin2 = (EditText) findViewById(R.id.pin2id);
                final String rpin2 = pin2.getText().toString().trim();
                rMsgbox = (TextView) findViewById(R.id.rmsgid);
                if (rpin1.equals("")) {
                    rMsgbox.setText("PIN cannot be empty");
                    rMsgbox.setTextColor(Color.RED);
                } else {
                    if (rpin1.length() < 5) {
                        rMsgbox.setText("PIN should be 5 digits");
                        rMsgbox.setTextColor(Color.RED);
                    } else {
                        if (rpin2.equals("")) {
                            rMsgbox.setText("Confirm PIN cannot be empty");
                            rMsgbox.setTextColor(Color.RED);
                        } else {
                            if (rpin2.length() < 5) {
                                rMsgbox.setText("Confirm PIN should be 5 digits");
                                rMsgbox.setTextColor(Color.RED);
                            } else {
                                if (rpin1.equals(rpin2)) {
                                    final DatabaseReference reference = FirebaseDatabase.getInstance("https://final-mealpass.firebaseio.com/").getReference().child("Passdata").child("E" + EIDGLOBAL);
                                    reference.child("PIN").setValue(rpin2)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override

                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(MainActivity.this, "Updated successful. Please login again", Toast.LENGTH_SHORT).show();
                                                    rMsgbox1 = (TextView) findViewById(R.id.rmsgid);
                                                    rMsgbox1.setText("Register successful. Please Login again");
                                                    rMsgbox1.setTextColor(Color.GREEN);

                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(MainActivity.this, "Onfailure", Toast.LENGTH_SHORT).show();
                                                    rMsgbox.setText("System Error. Please try after sometime.");
                                                    rMsgbox.setTextColor(Color.RED);
                                                }
                                            });
                                } else {
                                    rMsgbox.setText("Confirm PIN doesn't match with PIN");
                                    rMsgbox.setTextColor(Color.RED);
                                }
                            }
                        }
                    }
                }
            }
        });
        htext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.activity_main);
                Mainscreen();
            }
        });
    }

    public void passscreen(final String EID) {

        final DatabaseReference reference = FirebaseDatabase.getInstance("https://final-mealpass.firebaseio.com/").getReference().child("Passdata").child("E" + EID);
        final DatabaseReference reference1 = FirebaseDatabase.getInstance("https://final-mealpass.firebaseio.com/").getReference().child("Feedback").child("meals");
        final DatabaseReference reference2 = FirebaseDatabase.getInstance("https://final-mealpass.firebaseio.com/").getReference().child("Feedback").child("snacks");

        Button gmpbutton = (Button) findViewById(R.id.gmpbuttonid);
        gmpbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle("Alert");
                alertDialog.setMessage("Are you sure want to generate Meal Pass?");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Confirm",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Calendar calendar = Calendar.getInstance();

                                Date date = new Date();
                                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                                final String strDate= formatter.format(date);
                                System.out.println(strDate);

//                                Date today;
//                                String result;
//                                SimpleDateFormat formatter;
//                                Locale current = getResources().getConfiguration().locale;
//
//
//                                formatter = new SimpleDateFormat("EEE d MMM yy", current);
//                                today = new Date();
//                                result = formatter.format(today);
//                                System.out.println("Locale: " + current.toString());
//                                System.out.println("Result: " + result);


                                final String currentDate = DateFormat.getDateInstance().format(calendar.getTime());
                                System.out.println("current date :" + currentDate);

                                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @SuppressLint("ResourceAsColor")
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        String dbdate = dataSnapshot.child("USEDDATE").getValue().toString();
                                        if (dbdate.equals(strDate)) {

                                            final TextView textView1 = findViewById(R.id.msid);
                                            textView1.setText("Used Already ");
                                            textView1.setTextColor(Color.RED);
                                            textView1.setVisibility(View.VISIBLE);
                                            textView1.postDelayed(new Runnable() {
                                                                      @Override
                                                                      public void run() {
                                                                          textView1.setVisibility(View.INVISIBLE);
                                                                      }
                                                                  }, 10000
                                            );
                                        } else {
                                            setContentView(R.layout.meal_pass);
                                            TextView mpdate = (TextView) findViewById(R.id.spdateid);
                                            mpdate.setText(strDate);
                                            mpdate.postDelayed(new Runnable() {
                                                                      @Override
                                                                      public void run() {
                                                                          setContentView(R.layout.pass_screen);
                                                                          passscreen(EID);
                                                                      }
                                                                  }, 10000
                                            );

                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    reference.child("USEDDATE").setValue(strDate)
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {

                                                                }
                                                            })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {

                                                                }
                                                            });

//
                                                }
                                            }, 5000);

                                            // update meals counter
                                            reference1.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    String mcount = dataSnapshot.child("COUNT").getValue().toString();
                                                    int m = Integer.parseInt(mcount);
                                                    m = m + 1;
                                                    String mc = Integer.toString(m);
                                                    reference1.child("COUNT").setValue(mc)
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {

                                                                }
                                                            })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {

                                                                }
                                                            });
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });


                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                });
//                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Cancel",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        });
                alertDialog.show();
            }
            //close onclick
        });

        Button gspbutton = (Button) findViewById(R.id.gspbuttonid);
        gspbutton.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                             public void onClick (View view){
                                                 AlertDialog alertDialog1 = new AlertDialog.Builder(MainActivity.this).create();
                                                 alertDialog1.setTitle("Alert");
                                                 alertDialog1.setMessage("Are you sure want to generate Snack Pass?");
                                                 alertDialog1.setButton(AlertDialog.BUTTON_NEUTRAL, "Confirm",
                                                         new DialogInterface.OnClickListener() {
                                                             public void onClick(DialogInterface dialog, int which) {
                                                                 Calendar calendar = Calendar.getInstance();
                                                                 final String currentDate = DateFormat.getDateInstance().format(calendar.getTime());
                                                                 System.out.println("current date :" + currentDate);

                                                                 Date date = new Date();
                                                                 SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                                                                 final String strDate= formatter.format(date);

                                                                 reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                     @SuppressLint("ResourceAsColor")
                                                                     @Override
                                                                     public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                                         String dbdate = dataSnapshot.child("USEDDATE1").getValue().toString();
                                                                         if (dbdate.equals(strDate)) {
                                                                             final TextView textView1 = findViewById(R.id.msid);
                                                                             textView1.setText("Used Already ");
                                                                             textView1.setTextColor(Color.RED);
                                                                             textView1.setVisibility(View.VISIBLE);
                                                                             textView1.postDelayed(new Runnable() {
                                                                                                       @Override
                                                                                                       public void run() {
                                                                                                           textView1.setVisibility(View.INVISIBLE);
                                                                                                       }
                                                                                                   }, 10000
                                                                             );
                                                                         } else {
                                                                             setContentView(R.layout.snack_pass);
                                                                             TextView spdate = (TextView) findViewById(R.id.spdateid);
                                                                             spdate.setText(strDate);
                                                                             spdate.postDelayed(new Runnable() {
                                                                                                    @Override
                                                                                                    public void run() {
                                                                                                        setContentView(R.layout.pass_screen);
                                                                                                        passscreen(EID);
                                                                                                    }
                                                                                                }, 10000
                                                                             );

                                                                             new Handler().postDelayed(new Runnable() {
                                                                                 @Override
                                                                                 public void run() {
                                                                                     reference.child("USEDDATE1").setValue(strDate)
                                                                                             .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                 @Override
                                                                                                 public void onSuccess(Void aVoid) {

                                                                                                 }
                                                                                             })
                                                                                             .addOnFailureListener(new OnFailureListener() {
                                                                                                 @Override
                                                                                                 public void onFailure(@NonNull Exception e) {

                                                                                                 }
                                                                                             });

//
                                                                                 }
                                                                             }, 5000);
                                                                             //update snack counter

                                                                             reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                 @Override
                                                                                 public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                     String mcount = dataSnapshot.child("COUNT").getValue().toString();
                                                                                     int s = Integer.parseInt(mcount);
                                                                                     s = s + 1;
                                                                                     String sc = Integer.toString(s);
                                                                                     reference2.child("COUNT").setValue(sc)
                                                                                             .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                 @Override
                                                                                                 public void onSuccess(Void aVoid) {

                                                                                                 }
                                                                                             })
                                                                                             .addOnFailureListener(new OnFailureListener() {
                                                                                                 @Override
                                                                                                 public void onFailure(@NonNull Exception e) {

                                                                                                 }
                                                                                             });
                                                                                 }

                                                                                 @Override
                                                                                 public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                 }
                                                                             });


                                                                         }
                                                                     }

                                                                     @Override
                                                                     public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                     }
                                                                 });
                                                             }

                                                         });
                                                 alertDialog1.show();
                                             //close onclick
                                         }
                                     });

        Button bbutton = (Button) findViewById(R.id.bbuttonid);
        bbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.activity_main);
                Mainscreen();
            }
        });

        Button fbutton = (Button) findViewById(R.id.fbuttonid);
        fbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setContentView(R.layout.feedback_main);
                RatingBar mratingbar = (RatingBar) findViewById(R.id.mratingBarid);
                mratingbar.setStepSize(Float.parseFloat("1.0"));
                mratingbar.setRating(Float.parseFloat("1.0"));
                Feedbackscreen(EID);


            }
        });

    }



    public void Feedbackscreen(final String EID) {

        String[] foodtype = {"Meals", "Snack"};
        ArrayAdapter<String> adapter;


        Spinner sp = findViewById(R.id.spinner3);

        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_expandable_list_item_1, foodtype);
        sp.setAdapter(adapter);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        Toast.makeText(MainActivity.this, "Meals selected", Toast.LENGTH_SHORT).show();
                        final DatabaseReference reference = FirebaseDatabase.getInstance("https://final-mealpass.firebaseio.com/").getReference().child("Feedback").child("meals");
                        final RatingBar mratingbar;
                        Button sbutton;

                        mratingbar = (RatingBar) findViewById(R.id.mratingBarid);
                        sbutton = (Button) findViewById(R.id.submitid);
                        sbutton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        String r = String.valueOf(mratingbar.getRating());
                                        String rchild = "";
                                        System.out.println("rating: " + r);
                                        if (r.equals("0")) {
                                            rchild = "STAR0";
                                        } else {
                                            if (r.equals("1.0")) {
                                                rchild = "STAR1";
                                            } else {
                                                if (r.equals("2.0")) {
                                                    rchild = "STAR2";
                                                } else {
                                                    if (r.equals("3.0")) {
                                                        rchild = "STAR3";
                                                    } else {
                                                        if (r.equals("4.0")) {
                                                            rchild = "STAR4";
                                                        } else {
                                                            if (r.equals("5.0")) {
                                                                rchild = "STAR5";
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }

                                        String r3 = dataSnapshot.child(rchild).getValue().toString();
                                        int i = Integer.parseInt(r3);
                                        i = i + 1;
                                        String rfinal = Integer.toString(i);
                                        reference.child(rchild).setValue(rfinal)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(MainActivity.this, "Your Feedback submitted! Thank  you!!", Toast.LENGTH_SHORT).show();

                                                        setContentView(R.layout.pass_screen);
                                                        final TextView textView1 = findViewById(R.id.msid);
                                                        textView1.setText("Your Feedback submitted! Thank  you!! ");
                                                        textView1.setTextColor(Color.GREEN);
                                                        textView1.setVisibility(View.VISIBLE);
                                                        textView1.postDelayed(new Runnable() {
                                                                                  @Override
                                                                                  public void run() {
                                                                                      textView1.setVisibility(View.INVISIBLE);
                                                                                  }
                                                                              }, 10000
                                                        );

                                                        passscreen(EID);
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {

                                                    }
                                                });
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                            }
                        });

                        break;
                    case 1:
                        Toast.makeText(MainActivity.this, "Snack selected ", Toast.LENGTH_SHORT).show();
                        final DatabaseReference reference1 = FirebaseDatabase.getInstance("https://final-mealpass.firebaseio.com/").getReference().child("Feedback").child("snacks");
                        final RatingBar mratingbar1;
                        Button sbutton1;

                        mratingbar1 = (RatingBar) findViewById(R.id.mratingBarid);
                        sbutton1 = (Button) findViewById(R.id.submitid);
                        sbutton1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                reference1.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        String r = String.valueOf(mratingbar1.getRating());
                                        String rchild = "";
                                        System.out.println("rating: " + r);
                                        if (r.equals("0")) {
                                            rchild = "STAR0";
                                        } else {
                                            if (r.equals("1.0")) {
                                                rchild = "STAR1";
                                            } else {
                                                if (r.equals("2.0")) {
                                                    rchild = "STAR2";
                                                } else {
                                                    if (r.equals("3.0")) {
                                                        rchild = "STAR3";
                                                    } else {
                                                        if (r.equals("4.0")) {
                                                            rchild = "STAR4";
                                                        } else {
                                                            if (r.equals("5.0")) {
                                                                rchild = "STAR5";
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }

                                        String r3 = dataSnapshot.child(rchild).getValue().toString();
                                        int i = Integer.parseInt(r3);
                                        i = i + 1;
                                        String rfinal = Integer.toString(i);
                                        reference1.child(rchild).setValue(rfinal)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(MainActivity.this, "Your Feedback submitted! Thank  you!!", Toast.LENGTH_SHORT).show();

                                                        setContentView(R.layout.pass_screen);
                                                        final TextView textView1 = findViewById(R.id.msid);
                                                        textView1.setText("Your Feedback submitted! Thank  you!! ");
                                                        textView1.setTextColor(Color.GREEN);
                                                        textView1.setVisibility(View.VISIBLE);
                                                        textView1.postDelayed(new Runnable() {
                                                                                  @Override
                                                                                  public void run() {
                                                                                      textView1.setVisibility(View.INVISIBLE);
                                                                                  }
                                                                              }, 10000
                                                        );
                                                        passscreen(EID);


                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {

                                                    }
                                                });
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                            }
                        });
                        break;

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        Button ebutton = (Button) findViewById(R.id.bexitid);
        ebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.pass_screen);

                passscreen(EID);


            }
        });

    }

    public void Vendorscreen() {
        Msgbox = (TextView) findViewById(R.id.Msgboxid);
        Button lbutton = (Button) findViewById(R.id.lbuttonid);
        lbutton.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View view) {
                                           Userid = (EditText) findViewById(R.id.vid);
                                           final String username = Userid.getText().toString().trim();
                                           Userpin = (EditText) findViewById(R.id.Ipinid);
                                           final String userpwd = Userpin.getText().toString().trim();
                                           if (username.equals("")) {
                                               Msgbox.setText("Vendor Name cannot be empty");
                                               Msgbox.setTextColor(Color.RED);
                                           } else {
                                               if (userpwd.equals("")) {
                                                   Msgbox.setText("Please enter PIN");
                                                   Msgbox.setTextColor(Color.RED);
                                               } else {
                                                   if (userpwd.length() < 5) {
                                                       Msgbox.setText("PIN should be 5 digits");
                                                       Msgbox.setTextColor(Color.RED);
                                                   } else {
                                                       DatabaseReference reference = FirebaseDatabase.getInstance("https://final-mealpass.firebaseio.com/").getReference().child("Vendor");
                                                       reference.addValueEventListener(new ValueEventListener() {
                                                           @Override
                                                           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                               if (dataSnapshot.exists()) {
                                                                   String Name = dataSnapshot.child("Name").getValue().toString();
                                                                   String PIN = dataSnapshot.child("PIN").getValue().toString();
                                                                   if (Name.equals(username)) {
                                                                       if (PIN.equals(userpwd)) {
                                                                           setContentView(R.layout.vendor_fbrpt);
                                                                           vfbrptscreen();
                                                                       } else {
                                                                           Msgbox.setText("Incorrect PIN");
                                                                           Msgbox.setVisibility(View.VISIBLE);
                                                                           Msgbox.setTextColor(Color.RED);
                                                                       }
                                                                   } else {
                                                                       Msgbox.setText("You're not eligible" + "\n" + "Please contact FIS Facilities");
                                                                       Msgbox.setVisibility(View.VISIBLE);
                                                                       Msgbox.setTextColor(Color.RED);
                                                                   }

                                                               } else {
                                                                   Msgbox.setText("You're not eligible" + "\n" + "Please contact FIS Facilities");
                                                                   Msgbox.setVisibility(View.VISIBLE);
                                                                   Msgbox.setTextColor(Color.RED);
                                                               }
                                                           }

                                                           @Override
                                                           public void onCancelled(@NonNull DatabaseError databaseError) {

                                                               Msgbox.setText("Please try after sometime!!");
                                                               Msgbox.setVisibility(View.VISIBLE);

                                                           }
                                                       });
                                                   }
                                               }
                                           }
                                       }

                                   }
        );

        Button rptback = (Button) findViewById(R.id.vbhomeid);
        rptback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.login_option);
                loginoption();
            }
        });

    }

    public void vfbrptscreen() {
        final TextView msgbox = (TextView) findViewById(R.id.textView10);
        msgbox.setVisibility(View.INVISIBLE);
        final TextView mmcch = (TextView) findViewById(R.id.textView11);
        mmcch.setVisibility(View.INVISIBLE);
        final TextView mscch = (TextView) findViewById(R.id.textView12);
        mscch.setVisibility(View.INVISIBLE);
        final TextView mmccd = (TextView) findViewById(R.id.mmccdid);
        mmccd.setVisibility(View.INVISIBLE);
        final TextView msccd = (TextView) findViewById(R.id.msccdid);
        msccd.setVisibility(View.INVISIBLE);
        String[] stattype = {"Please select a report","Today's Meals Feedback", "Today's Snacks Feedback", "Monthly Report"};
        ArrayAdapter<String> adapter;

        Spinner sp = findViewById(R.id.vspinnerid);

        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_expandable_list_item_1, stattype);
        sp.setAdapter(adapter);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        Button ldbutton = (Button) findViewById(R.id.ldbuttonid);
                        ldbutton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                msgbox.setText("Please select appropriate report!");
                                msgbox.setTextColor(Color.RED);
                                msgbox.setVisibility(View.VISIBLE);
                            }
                        });
                        break;
                    case 1:
                        Button ldbutton1 = (Button) findViewById(R.id.ldbuttonid);
                        ldbutton1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                final String type = "M";
                                setContentView(R.layout.fb_data);
                                fbdatascresen(type);
                            }
                        });
                        break;
                    case 2:
                        Button ldbutton2 = (Button) findViewById(R.id.ldbuttonid);
                        ldbutton2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                final String type = "S";
                                setContentView(R.layout.fb_data);
                                fbdatascresen(type);
                            }
                        });
                        break;
                    case 3:
                        Button ldbutton3 = (Button) findViewById(R.id.ldbuttonid);
                        ldbutton3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                DatabaseReference reference = FirebaseDatabase.getInstance("https://final-mealpass.firebaseio.com/").getReference().child("Feedback").child("meals");
                                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        String dcount = dataSnapshot.child("COUNT").getValue().toString();
                                        String mcount = dataSnapshot.child("MCOUNT").getValue().toString();
                                        int dc = Integer.parseInt(dcount);
                                        int mc = Integer.parseInt(mcount);
                                        int fc = dc + mc;
                                        String fcount = Integer.toString(fc);
                                        mmccd.setText(fcount);
                                        mmcch.setVisibility(View.VISIBLE);
                                        mmccd.setVisibility(View.VISIBLE);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                                DatabaseReference reference1 = FirebaseDatabase.getInstance("https://final-mealpass.firebaseio.com/").getReference().child("Feedback").child("snacks");
                                reference1.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        String dcount = dataSnapshot.child("COUNT").getValue().toString();
                                        String mcount = dataSnapshot.child("MCOUNT").getValue().toString();
                                        int dc = Integer.parseInt(dcount);
                                        int mc = Integer.parseInt(mcount);
                                        int fc = dc + mc;
                                        String fcount = Integer.toString(fc);
                                        msccd.setText(fcount);
                                        mscch.setVisibility(View.VISIBLE);
                                        msccd.setVisibility(View.VISIBLE);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        });
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Button rptback = (Button) findViewById(R.id.vlogotid);
        rptback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.vendor_login);
                Vendorscreen();
            }
        });

    }


    public void fbdatascresen(final String type) {

        if(type.equals("M"))
        {
        DatabaseReference reference = FirebaseDatabase.getInstance("https://final-mealpass.firebaseio.com/").getReference().child("Feedback").child("meals");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String star5 = dataSnapshot.child("STAR5").getValue().toString();
                String star4 = dataSnapshot.child("STAR4").getValue().toString();
                String star3 = dataSnapshot.child("STAR3").getValue().toString();
                String star2 = dataSnapshot.child("STAR2").getValue().toString();
                String star1 = dataSnapshot.child("STAR1").getValue().toString();
                String mcount = dataSnapshot.child("COUNT").getValue().toString();

                final TextView star5c = findViewById(R.id.s5did);
                star5c.setText(star5);
                final TextView star4c = findViewById(R.id.s4did);
                star4c.setText(star4);
                final TextView star3c = findViewById(R.id.s3did);
                star3c.setText(star3);
                final TextView star2c = findViewById(R.id.s2did);
                star2c.setText(star2);
                final TextView star1c = findViewById(R.id.s1did);
                star1c.setText(star1);
                final TextView mpassc = findViewById(R.id.mpcdid);
                mpassc.setText(mcount);

                final TextView mphdr = findViewById(R.id.fbdhid);
                mphdr.setText("Todays Meals Report");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        }

        if(type.equals("S"))
        {
            DatabaseReference reference = FirebaseDatabase.getInstance("https://final-mealpass.firebaseio.com/").getReference().child("Feedback").child("snacks");
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String star5 = dataSnapshot.child("STAR5").getValue().toString();
                    String star4 = dataSnapshot.child("STAR4").getValue().toString();
                    String star3 = dataSnapshot.child("STAR3").getValue().toString();
                    String star2 = dataSnapshot.child("STAR2").getValue().toString();
                    String star1 = dataSnapshot.child("STAR1").getValue().toString();
                    String mcount = dataSnapshot.child("COUNT").getValue().toString();

                    final TextView star5c = findViewById(R.id.s5did);
                    star5c.setText(star5);
                    final TextView star4c = findViewById(R.id.s4did);
                    star4c.setText(star4);
                    final TextView star3c = findViewById(R.id.s3did);
                    star3c.setText(star3);
                    final TextView star2c = findViewById(R.id.s2did);
                    star2c.setText(star2);
                    final TextView star1c = findViewById(R.id.s1did);
                    star1c.setText(star1);
                    final TextView mpassc = findViewById(R.id.mpcdid);
                    mpassc.setText(mcount);

                    final TextView mphdr = findViewById(R.id.fbdhid);
                    mphdr.setText("Todays Snack Report");

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

    }

      Button rptback = (Button) findViewById(R.id.rbbuttonid);
      rptback.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              setContentView(R.layout.vendor_fbrpt);
              vfbrptscreen();
          }
      });



}

}