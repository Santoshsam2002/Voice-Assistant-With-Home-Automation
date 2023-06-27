package com.example.projectva;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
public class voicesection extends AppCompatActivity {
    private SpeechRecognizer recognizer;
    private TextView tvresult;
    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voicesection);
        Context context=this;
        Dexter.withContext(this)
                .withPermission(Manifest.permission.RECORD_AUDIO)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        System.exit(0);
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
        findById();
        initializeTextToSpeech();
        initializeResult();

    }

    private void initializeTextToSpeech() {
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (tts.getEngines().size() == 0) {
                    Toast.makeText(voicesection.this, "Engine is not available", Toast.LENGTH_SHORT).show();
                } else {
                    speak("well come back sir");
                }
            }
        });
    }

    private void speak(String msg) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts.speak(msg, TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            tts.speak(msg, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    private void findById() {
        tvresult = (TextView) findViewById(R.id.textView);
    }

    private void initializeResult() {
        if (SpeechRecognizer.isRecognitionAvailable(this)) {
            recognizer = SpeechRecognizer.createSpeechRecognizer(this);
            recognizer.setRecognitionListener(new RecognitionListener() {
                @Override
                public void onReadyForSpeech(Bundle bundle) {

                }

                @Override
                public void onBeginningOfSpeech() {

                }

                @Override
                public void onRmsChanged(float v) {

                }

                @Override
                public void onBufferReceived(byte[] bytes) {

                }

                @Override
                public void onEndOfSpeech() {

                }

                @Override
                public void onError(int i) {

                }

                @Override
                public void onResults(Bundle bundle) {
                    ArrayList<String> result = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                    Toast.makeText(voicesection.this, "" + result.get(0), Toast.LENGTH_SHORT).show();
                    tvresult.setText(result.get(0));
                    response(result.get(0));
                }

                @Override
                public void onPartialResults(Bundle bundle) {
                }

                @Override
                public void onEvent(int i, Bundle bundle) {
                }
            });

        }
    }

    private void response(String msg) {
        String msgs = msg.toLowerCase();
        if (msgs.indexOf("hi") != -1) {
            speak("hello sir ! How are you ?");
        } else if (msgs.indexOf("time") != -1) {
            if (msgs.indexOf("now") != -1) {
                Date date = new Date();
                String time = DateUtils.formatDateTime(this, date.getTime(), DateUtils.FORMAT_SHOW_TIME);
                speak("the time now is " + time);
            }
        } else if (msgs.indexOf("today") != -1) {
            if (msgs.indexOf("date") != -1) {
                SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");
                Calendar cal = Calendar.getInstance();
                String todays_date = df.format(cal.getTime());
                speak("The Today's date is " + todays_date);
            }
        } else if (msgs.indexOf("desktop") != -1 || msgs.indexOf("pc") != -1 || msgs.indexOf("computer") != -1) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("pc message");
            myRef.setValue(msgs);
        }
        if (msgs.indexOf("light") != -1) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("pi message");
            if (msgs.indexOf("on") != -1) {
                myRef.setValue("switch1on");
                speak("Turning light on");
            }
            if (msgs.indexOf("off") != -1) {
                myRef.setValue("switch1off");
                speak("Turning light off");
            }
        } else if (msgs.indexOf("fan") != -1) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("pi message");
            if (msgs.indexOf("on") != -1) {
                myRef.setValue("switch2on");
                speak("Turning fan on");
            }
            if (msgs.indexOf("off") != -1) {
                myRef.setValue("switch2off");
                speak("Turning fan off");
            }
        } else if (msgs.indexOf("lock") != -1 && msgs.indexOf("door") != -1 && msgs.indexOf("unlock") == -1) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("pi message");
            myRef.setValue("switch3on");
            speak("Door locked");
        } else if (msgs.indexOf("unlock") != -1 && msgs.indexOf("door") != -1) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("pi message");
            Context ctx = this;
            Intent intent = new Intent(ctx,voicesection.class);
            ctx.startActivity(intent);
            myRef.setValue("switch3off");
            speak("Door unlocked");
        } else if (msgs.indexOf("open") != -1 && msgs.indexOf("desktop") == -1 && msgs.indexOf("computer") == -1 && msgs.indexOf("pc") == -1) {
            if (msgs.indexOf("google") != -1) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com"));
                startActivity(intent);
                speak("opening google");
            }
            if (msgs.indexOf("browser") != -1) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com"));
                startActivity(intent);
                speak("opening browser");
            }
            if (msgs.indexOf("chrome") != -1) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com"));
                startActivity(intent);
                speak("opening chrome");
            }
            if (msgs.indexOf("youtube") != -1) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com"));
                startActivity(intent);
                speak("opening youtube");
            }
            if (msgs.indexOf("facebook") != -1) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com"));
                startActivity(intent);
                speak("opening facebook");
            }
            if (msgs.indexOf("instagram") != -1) {
                Context ctx = this;
                Intent intent = ctx.getPackageManager().getLaunchIntentForPackage("com.instagram.android");
                ctx.startActivity(intent);
                speak("opening facebook");
            }
            if (msgs.indexOf("whatsapp") != -1) {
                Context ctx = this;
                Intent intent = ctx.getPackageManager().getLaunchIntentForPackage("com.whatsapp");
                ctx.startActivity(intent);
                speak("opening whatsapp");
            }
            if (msgs.indexOf("mx player") != -1) {
                Context ctx = this;
                Intent intent = ctx.getPackageManager().getLaunchIntentForPackage("com.mxtech.videoplayer.ad");
                ctx.startActivity(intent);
                speak("opening mx player");
            }
        }
        if (msgs.indexOf("search") != -1 || msgs.indexOf("mean") != -1 || msgs.indexOf("what is") != -1 || msgs.indexOf("what") != -1) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?q=" + msgs));
            startActivity(intent);
            speak("searching " + msgs);
        }
        if (msgs.indexOf("open") == -1 && msgs.indexOf("desktop") == -1 && msgs.indexOf("computer") == -1 && msgs.indexOf("pc") == -1 && msgs.indexOf("light") != -1 && msgs.indexOf("fan") != -1) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?q=" + msgs));
            startActivity(intent);
            speak("search results");
        }
        if (msgs.indexOf("door") != -1 && msgs.indexOf("status") != -1) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("door");
            String a = String.valueOf(myRef.getClass());
            speak("ok sir");
            speak("Door locked" + a);
        }
        /*if(msgs.indexOf("call") != -1){
            final String[] listName={""};
            final String name;
            name = fetchName(msgs);
            Log.d( "Name", name);
            Dexter.withContext(this)
                    .withPermissions(
                            Manifest.permission.READ_CONTACTS,
                            Manifest.permission. CALL_PHONE ).withListener(new MultiplePermissionsListener() {

                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                            if (report.areAllPermissionsGranted()) {
                                if (checkForPreviousCallList(MainActivity.this)) {
                                    speak(makeCallFromSavedContactList(MainActivity.this, name));
                                } else {
                                    HashMap<String, String> list = getContactList(MainActivity.this, name):
                                    if (list.size() > 1) {
                                        for (String i : List.keySet()) {
                                            listName[0] = listName[0].concat("...........................................!" + i);
                                        }
                                        speak(msg"Which one sir 7.. There is" + listName[0]);
                                    } else if (list.size() == 1) {
                                        if (Build.VERSION.SDK_INT Build.VERSION_CODES.N){
                                            makeCall(MainActivity.this, list.values().stream().findFirst().get());
                                            clearContactListSavedData(MainActivity.this);
                                        }
                                    } else {
                                        speak("No contact found !");
                                        clearContactListSavedData(MainActivity.this);
                                    }
                                }
                            }
                            if(report.isAnyPermissionPermanentlyDenied()){

                            }
                        }
                        @Override public void onPermissionRationale*/
    }
    public void startRecording(View view){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,1);
        recognizer.startListening(intent);
    }
}