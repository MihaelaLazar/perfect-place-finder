package com.cronJob.controller;


import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import twitter4j.*;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@RestController
public class TwitterController {

    public Map<String, String> tweetsList = Collections.synchronizedMap(new HashMap<String,String>());

    Date lastAccessedTime = new Date();

    LocalDateTime lastAccessedTimeInstant = LocalDateTime.now();

    @RequestMapping(path = "/get/noise/tweets", method = RequestMethod.GET)
    public ResponseEntity<List<String>> getNoiseTweets(Request request, Response response) {
//        List<String> noiseTweets = new ArrayList<>();
        if (!new Date().toString().split(" ")[1].equals(lastAccessedTime.toString().split(" ")[1])) {
            lastAccessedTime = new Date();
            clearListOfTweets();
        }
        long diffInHours = ChronoUnit.HOURS.between(lastAccessedTimeInstant, LocalDateTime.now());
        if (diffInHours >= 24 || tweetsList.isEmpty()) {
            getTweets();
        }
        System.out.println("Seconds difference: " + ChronoUnit.SECONDS.between(lastAccessedTimeInstant, LocalDateTime.now()));
        System.out.println(lastAccessedTime.toString());
        for(Map.Entry<String,String> entry: tweetsList.entrySet()) {
            System.out.println(entry.getKey());
        }
        return new ResponseEntity<>(getNoiseTweetsFromMap(), HttpStatus.OK);
    }


    public void getTweets(){
        ConfigurationBuilder cf = new ConfigurationBuilder();
        cf.setDebugEnabled(true)
                .setOAuthConsumerKey("Hlx4LyqjQiT9qX1IXD38pLtYW")
                .setOAuthConsumerSecret("WAdRsgyMXkJEf1OzH1dw5U8Ehq64jnE60dVQeW7VshsstEWCCm")
                .setOAuthAccessToken("842739519556337664-diYBmRjQn4Wvxxs0BmH6AZyfNQN5JNh")
                .setOAuthAccessTokenSecret("DaE1pFCGK6WJB9cnGoLgfaSj2DgrBPtn9j2dqKYSEe9Qm");
        FileWriter fstream = null;
        try {
            fstream = new FileWriter("pollutionTweets.txt", true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        PrintWriter out = new PrintWriter(fstream);
        Twitter twitter = new TwitterFactory(cf.build()).getInstance();
        Query query = new Query("#pollution");
        query.count(1000);
        int numberOfTweets = 1000;
        long lastID = Long.MAX_VALUE;
        ArrayList<Status> tweets = new ArrayList<Status>();
        while (tweets.size () < numberOfTweets) {
            if (numberOfTweets - tweets.size() > 1000)
                query.setCount(1000);
            else
                query.setCount(numberOfTweets - tweets.size());
            try {
                QueryResult result = twitter.search(query);
                tweets.addAll(result.getTweets());
                System.out.println("Gathered " + tweets.size() + " tweets"+"\n");
                for (Status t: tweets)
                    if(t.getId() < lastID)
                        lastID = t.getId();

            }
            catch (TwitterException te) {
                System.out.println("Couldn't connect: " + te);
                break;
            }
            query.setMaxId(lastID-1);
        }
        out.println("\nLast update");
        out.flush();
        for (int i = 0; i < tweets.size(); i++) {
            Status t = (Status) tweets.get(i);
            GeoLocation loc = t.getGeoLocation();
            String user = t.getUser().getScreenName();
            String msg = t.getText();
            String time = t.getCreatedAt().toString();
            if (loc != null) {
                tweetsList.put(loc.getLatitude() +","+ loc.getLongitude(), new Date().toString().split(" ")[1]);
                out.print("["+loc.getLatitude() +", "+ loc.getLongitude() + "]");
                out.println("");
                out.flush();
                System.out.println(i + " USER: " + user + " wrote: " + msg);
                System.out.println("location: " + loc.getLatitude() + ", " + loc.getLongitude() + " created at: " + time);
            }
        }
        out.close();
    }

    public void clearListOfTweets() {
        Iterator<Map.Entry<String,String>> iter = tweetsList.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String,String> entry = iter.next();
            if(!entry.getValue().equals(new Date().toString().split(" ")[1])){
                iter.remove();
            }
        }
    }

    public List<String> getNoiseTweetsFromMap(){
        List<String> toReturn = new ArrayList<>();
        for (Map.Entry<String,String> entry: tweetsList.entrySet()) {
            toReturn.add(entry.getKey());
        }

        return toReturn;
    }

}
