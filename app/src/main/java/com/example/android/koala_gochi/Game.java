package com.example.android.koala_gochi;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class Game extends AppCompatActivity {

    //NotificationManager
    NotificationManager nm;

    //all the buttons
    private Button giveDrinkButton;
    private Button feedButton;
    private Button cuddleButton;
    private Button sleepButton;
    private Button poopButton;
    private Button startButton;

    //All the variables
    public int thirst_status = 0;
    public int hunger_status = 0;
    public int sleep_status = 0;
    public int happiness_status = 0;
    public int poop_status = 0;
    public int count_turn = 0;
    public int timing = 0;

    //all the texts
    private TextView return_thirst_status_textview;
    private TextView return_hunger_status_textview;
    private TextView return_happiness_status_textview;
    private TextView return_sleepiness_status_textview;
    private TextView return_poopiness_status_textview;
    private TextView return_turn_count_number;
    private TextView koala_main_status;

    //Handler
    private Handler handler = new Handler();

    //Koala Face
    private ImageView koala_face;

    //Boolean Variables
    private boolean isAsleep;
    private boolean isPoop;
    private boolean isSad;
    private boolean isAlive;
    private boolean isShortGame;
    private boolean isNotifiedActivity;
    private boolean isFull;
    private boolean isProgressive;

    //string variable
    private String koala_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        //Attribute NotificationManagrr
        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        //Attributing TextView Id's
        return_thirst_status_textview = (TextView) findViewById(R.id.koala_thirst_status_text);
        return_hunger_status_textview = (TextView) findViewById(R.id.koala_hunger_status_text);
        return_happiness_status_textview = (TextView) findViewById(R.id.koala_happiness_status_text);
        return_sleepiness_status_textview = (TextView) findViewById(R.id.koala_sleepiness_status_text);
        return_poopiness_status_textview = (TextView) findViewById(R.id.koala_toilette_status_text);
        return_turn_count_number = (TextView) findViewById(R.id.text_turn_count);
        koala_main_status = (TextView) findViewById(R.id.status);

        //Attributing Button Id's
        giveDrinkButton = (Button) findViewById(R.id.give_drink_koala_button);
        feedButton = (Button) findViewById(R.id.feed_koala_button);
        cuddleButton = (Button) findViewById(R.id.cuddle_koala_button);
        sleepButton = (Button) findViewById(R.id.sleep_koala_button);
        poopButton = (Button) findViewById(R.id.toilette_koala_button);
        startButton = (Button) findViewById(R.id.start_button);

        //Attributing ImageView's ID
        koala_face = (ImageView) findViewById(R.id.koala_image);

        //Attributing the Notification type
        Intent inty = getIntent();
        isNotifiedActivity = inty.getBooleanExtra("Type", false);

        //retrieving data from Intent

        Intent i = getIntent();

        koala_name = i.getStringExtra("name");
        isShortGame = i.getBooleanExtra("length", false);
        isProgressive = i.getBooleanExtra("rythm", false);

        if (isNotifiedActivity) {
            thirst_status = inty.getIntExtra("thirst", 5000);
            hunger_status = inty.getIntExtra("hunger", 5000);
            happiness_status = inty.getIntExtra("happiness", 5000);
            sleep_status = inty.getIntExtra("sleep", 5000);
            poop_status = inty.getIntExtra("poop", 5000);
            koala_name = inty.getStringExtra("name");
            isShortGame = inty.getBooleanExtra("length", false);
            count_turn = inty.getIntExtra("score", 0);
        } else {
            //set default variables
            thirst_status = 3000;
            hunger_status = 4000;
            happiness_status = 5000;
            sleep_status = 7000;
            poop_status = 7000;
            count_turn = 0;
        }

        //what happens when you give him milk
        giveDrinkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isAsleep) {
                    if (thirst_status <= 9500) {
                        thirst_status = thirst_status + 3500;
                    } else {
                        thirst_status += (10000 - thirst_status);
                    }

                } else {
                    Toast.makeText(Game.this, "Tu peux pas lui donner à boire quand il fait dodo", Toast.LENGTH_SHORT).show();
                }

            }
        });

        //what happens when you feed him
        feedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isAsleep) {
                    if (hunger_status <= 9500) {
                        hunger_status = hunger_status + 3000;
                    } else {
                        hunger_status += (10000 - hunger_status);
                    }
                } else {
                    Toast.makeText(Game.this, "Tu peux pas le nourrir quand il fait dodo", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //What happens when you cuddle him
        cuddleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (happiness_status <= 9500) {
                    happiness_status += 1500;
                    if (isSad) {
                        isSad = false;
                    }
                } else {
                    happiness_status += (10000 - happiness_status);
                }

            }
        });

        //What happens when put him to sleep
        sleepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isAsleep && isAlive) {
                    isAsleep = true;
                } else if (isAsleep && isAlive) {
                    wakeupKoala();
                }
            }
        });

        //What happens when you change his diaper
        poopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPoop) {
                    if (!isAsleep) {
                        poop_status = 7500;
                        isPoop = false;
                    } else {
                        boolean isClumpy = getRandomBoolean();
                        if (isClumpy) {
                            isPoop = false;
                            wakeupKoala();
                            happiness_status -= 1000;
                            sleep_status -= 100;
                            poop_status = 7500;
                            Toast.makeText(Game.this, "Tu as été maladroit et a reveillé le koala !", Toast.LENGTH_SHORT).show();
                        } else {
                            poop_status = 7500;
                            isPoop = false;
                        }
                    }
                } else {
                    Toast.makeText(Game.this, "Sa couche est déjà propre !", Toast.LENGTH_SHORT).show();
                }
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runGame(true);
            }
        });

        //What happens when you start the game
        runGame(false);
    }

    public void runGame(boolean isRestarted) {

        if (isShortGame){
            timing = 500;
        } else{
            timing = 1000;
        }

        if (isRestarted) {
            thirst_status = 3000;
            hunger_status = 4000;
            happiness_status = 5000;
            sleep_status = 7000;
            poop_status = 7000;
            count_turn =0;
        }

        //Set the koala image to alive
        koala_face.setImageResource(R.drawable.emoji_koala);

        //Show a Welcome Message
        if (!isNotifiedActivity) {
            Toast.makeText(Game.this, "Un tout mignon Koala du nom de " + koala_name + " a été crée", Toast.LENGTH_LONG).show();
        } else {
            //Get the nasty notif off
            nm.cancel(1);
        }

        //Set the booleans
        isPoop = false;
        isFull = false;
        isAsleep = false;
        isSad = false;
        isAlive = true;


        //Disable the start Button
        startButton.setVisibility(View.INVISIBLE);
        return_turn_count_number.setVisibility(View.INVISIBLE);

        //How much is lost per turn
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isAlive) {
                    count_turn += 1;
                    if (isShortGame) {
                        if (!isAsleep) {
                            if (isSad) {
                                sleep_status -= 40;
                            }
                            if (isPoop) {
                                happiness_status -= 70;
                            }
                            if (isFull) {
                                poop_status -= 30;
                            }
                            thirst_status -= 100;
                            hunger_status -= 80;
                            happiness_status -= 70;
                            poop_status -= 50;
                            sleep_status -= 60;
                        } else {
                            if (isPoop) {
                                happiness_status -= 50;
                            }
                            if (isFull) {
                                poop_status -= 50;
                            }
                            sleep_status += 200;
                            thirst_status -= 30;
                            hunger_status -= 20;
                            happiness_status += 15;
                            poop_status -= 100;

                            if (sleep_status > 10000) {
                                sleep_status = 10000;
                                isAsleep = false;
                            }
                        }
                    } else {
                        if (!isAsleep) {
                            if (isSad) {
                                sleep_status -= 10;
                            }
                            if (isPoop) {
                                happiness_status -= 5;
                            }
                            if (isFull) {
                                poop_status -= 2;
                            }
                            sleep_status -= 4;
                            thirst_status -= 7;
                            hunger_status -= 5;
                            happiness_status -= 5;
                            poop_status -= 3;
                        } else {
                            if (isPoop) {
                                happiness_status -= 3;
                            }
                            if (isFull) {
                                poop_status -= 5;
                            }
                            sleep_status += 7;
                            thirst_status -= 1;
                            hunger_status -= 1;
                            happiness_status += 1;
                            poop_status -= 3;

                            if (sleep_status > 10000) {
                                sleep_status = 10000;
                                isAsleep = false;
                            }
                        }
                    }


                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            //No values under 0 authorized
                            if (thirst_status < 0) {
                                thirst_status = 0;
                            } else if (hunger_status < 0) {
                                hunger_status = 0;
                            } else if (happiness_status < 0) {
                                happiness_status = 0;
                            } else if (sleep_status < 0) {
                                sleep_status = 0;
                            } else if (poop_status < 0) {
                                poop_status = 0;
                            }

                            //Return Status to main screen
                            if (thirst_status > 7500) {
                                isFull = true;
                                return_thirst_status_textview.setText("Le koala a trop bu !");
                            } else if (thirst_status < 2500) {
                                return_thirst_status_textview.setText("Le koala a soif!");
                            } else if (thirst_status < 5000) {
                                return_thirst_status_textview.setText("Le koala a un peu soif!");
                            } else {
                                return_thirst_status_textview.setText("Le koala n'a pas soif.");
                                isFull = false;
                            }
                            if (hunger_status > 7500) {
                                return_hunger_status_textview.setText("Le koala a trop mangé !");
                                isFull = true;
                            } else if (hunger_status < 2500) {
                                return_hunger_status_textview.setText("Le koala a faim!");
                            } else if (hunger_status < 5000) {
                                return_hunger_status_textview.setText("Le koala a un peu faim");
                            } else {
                                return_hunger_status_textview.setText("Le koala n'a pas faim.");
                                isFull = false;
                            }

                            //if happy or sad
                            if (happiness_status > 7500) {
                                return_happiness_status_textview.setText("Le koala est très content !");
                                if (isPoop) {
                                    koala_face.setImageResource(R.drawable.emoji_koala_happy_poop);
                                } else {
                                    koala_face.setImageResource(R.drawable.emoji_koala_happy);
                                }
                            } else if (happiness_status < 2500) {
                                return_happiness_status_textview.setText("Le koala est triste !");
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        isSad = true;
                                    }
                                });
                                if (!isAsleep) {
                                    if (isPoop) {
                                        koala_face.setImageResource(R.drawable.emoji_koala_sad_poop);
                                    } else {
                                        koala_face.setImageResource(R.drawable.emoji_koala_sad);
                                    }
                                }
                            } else {
                                return_happiness_status_textview.setText("Le koala est content");
                                if (!isAsleep) {
                                    if (isPoop) {
                                        koala_face.setImageResource(R.drawable.emoji_koala_poop);
                                    } else {
                                        koala_face.setImageResource(R.drawable.emoji_koala);
                                    }
                                }
                            }

                            if (!isAsleep) {
                                if (sleep_status > 7500) {
                                    return_sleepiness_status_textview.setText("Le koala est en pleine forme!");
                                } else if (sleep_status < 2500) {
                                    return_sleepiness_status_textview.setText("Le koala est très fatigué!");
                                } else if (sleep_status < 5000) {
                                    return_sleepiness_status_textview.setText("Le koala est un peu fatigué.");
                                } else {
                                    return_sleepiness_status_textview.setText("Le koala n'est pas fatigué");
                                }
                            } else {
                                return_sleepiness_status_textview.setText("Le koala fait un gros dodo");

                                if (isPoop) {
                                    koala_face.setImageResource(R.drawable.emoji_koala_sleep_poop);
                                } else {
                                    koala_face.setImageResource(R.drawable.koala_emoji_sleeping);
                                }
                            }

                            if (poop_status < 2500) {
                                if (poop_status <= 0) {
                                    poop_status = 0;
                                }
                                if (isPoop) {
                                    return_poopiness_status_textview.setText("Le koala a fait caca !");
                                } else {
                                    isPoop = getRandomBoolean();
                                }

                            } else {
                                return_poopiness_status_textview.setText("Le koala a une couche propre !");
                            }

                        }
                    });

                    ///if one of those variables gets under 0 the koala dies and everything stops
                    if ((hunger_status < 1500 && hunger_status > 0) || (sleep_status < 1500 && sleep_status > 0) || (thirst_status < 1500 && thirst_status > 0)) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (hunger_status < 1500) {
                                    koala_main_status.setText(koala_name + " le koala va mourir !");
                                    notifyUpcomingDeath();
                                } else if (sleep_status < 1500) {
                                    koala_main_status.setText(koala_name + " le koala va mourir !");
                                    notifyUpcomingDeath();
                                } else {
                                    koala_main_status.setText(koala_name + " le koala va mourir !");
                                    notifyUpcomingDeath();
                                }
                            }
                        });
                    } else if ((hunger_status <= 0) || (sleep_status <= 0) || (thirst_status <= 0)) {
                        notifyDeath();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (isPoop) {
                                    koala_face.setImageResource(R.drawable.emoji_koala_dead_poop);
                                } else {
                                    koala_face.setImageResource(R.drawable.emoji_koala_dead);
                                }
                                isAlive = false;

                                if (hunger_status <= 0) {
                                    koala_main_status.setText(koala_name + " le koala est mort de faim");
                                } else if (sleep_status <= 0) {
                                    koala_main_status.setText(koala_name + " le koala est mort de fatigue");
                                } else if (thirst_status <= 0) {
                                    koala_main_status.setText(koala_name + " le koala est mort de soif");
                                }
                                if (isShortGame) {
                                    return_turn_count_number.setText("Score: " + count_turn);
                                } else {
                                    return_turn_count_number.setText("Score: " + (count_turn/20));
                                }
                                startButton.setVisibility(View.VISIBLE);
                                return_turn_count_number.setVisibility(View.VISIBLE);
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                koala_main_status.setText(koala_name + " le koala est vivant, huh !");
                            }
                        });
                    }

                    ///time waited, in millisecond
                    try {
                        if (isProgressive) {
                            if (isShortGame){
                                Thread.sleep((timing - (count_turn)));
                            } else{
                                Thread.sleep(timing - count_turn/2);
                            }
                        } else {
                            Thread.sleep(timing);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }

        }).start();
    }

    public void notifyUpcomingDeath() {

        isNotifiedActivity = true;

        Intent inty = new Intent(getApplicationContext(), Game.class);
        inty.putExtra("name", koala_name);
        inty.putExtra("Type", isNotifiedActivity);
        inty.putExtra("hunger", hunger_status);
        inty.putExtra("thirst", thirst_status);
        inty.putExtra("sleep", sleep_status);
        inty.putExtra("poop", poop_status);
        inty.putExtra("happiness", happiness_status);
        inty.putExtra("length", isShortGame);
        inty.putExtra("score", count_turn);


        TaskStackBuilder tasky = TaskStackBuilder.create(getApplicationContext());
        tasky.addParentStack(Game.class);
        tasky.addNextIntent(inty);

        PendingIntent pendy = tasky.getPendingIntent(123, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setAutoCancel(true)
                        .setSmallIcon(R.drawable.emoji_koala)
                        .setContentTitle("Koala Go-Chi")
                        .setContentText("Le Koala va mourir !")
                        .setContentIntent(pendy);

        Notification notif = mBuilder.build();

        nm.notify(1, notif);
    }

    public void notifyDeath() {
        Intent inty = new Intent(getApplicationContext(), Game.class);
        inty.putExtra("name", koala_name);
        inty.putExtra("Type", isNotifiedActivity);
        inty.putExtra("hunger", hunger_status);
        inty.putExtra("thirst", thirst_status);
        inty.putExtra("sleep", sleep_status);
        inty.putExtra("poop", poop_status);
        inty.putExtra("happiness", happiness_status);
        inty.putExtra("length", isShortGame);
        inty.putExtra("score", count_turn);


        TaskStackBuilder tasky = TaskStackBuilder.create(getApplicationContext());
        tasky.addParentStack(Game.class);
        tasky.addNextIntent(inty);

        PendingIntent pendy = tasky.getPendingIntent(123, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setAutoCancel(true)
                        .setSmallIcon(R.drawable.emoji_koala_dead)
                        .setContentTitle("Koala Go-Chi")
                        .setContentText("Le Koala est mort")
                        .setContentIntent(pendy);

        Notification notif = mBuilder.build();
        nm.notify(1, notif);
    }

    public static boolean getRandomBoolean() {
        return Math.random() < 0.25;
    }

    public void wakeupKoala() {
        isAsleep = false;
        if (isPoop) {
            koala_face.setImageResource(R.drawable.emoji_koala_poop);
        } else {
            koala_face.setImageResource(R.drawable.emoji_koala);
        }
    }
}