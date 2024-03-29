package de.fhb.graebitz;

import twitter4j.DirectMessage;
import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import java.util.List;

import de.fhb.graebitz.GetConfiguredTwitterObject;

public final class AgendSmith {
	
    public static void main(String[] args) {
    	   
   		getUserTimeline("@sqs_fhb");
   		
    	int mode = 9;
    	In in = new In();

    	do{
    		System.out.println("Bitte waehlen: ");
    		System.out.println("1 - Nachrichten beantworten | 0 - beenden");
    		mode = in.readInt();
    		if(mode == 1){
    			getMessage();
    		}
    		else if(mode == 0){
    			System.out.println("Beendet!");
    			System.exit(0);
    		}
    		else System.out.println("Modus falsch");
    		
    	}while(mode != 0);
   		
   		
    }
    public static void getUserTimeline(String username){
    	
    	GetConfiguredTwitterObject co = new GetConfiguredTwitterObject();
    	Twitter twitter = co.getTwitter();
		
        try {
            List<Status> statuses = twitter.getUserTimeline(username);
            System.out.println("Showing Timeline:");
            for (Status status : statuses) {
                System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText());
            }
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to get timeline: " + te.getMessage());
        }
    }
    public static void getMessage() {
    	
    	GetConfiguredTwitterObject co = new GetConfiguredTwitterObject();
    	Twitter twitter = co.getTwitter();
    	
        try {
        	long uid = 0l;
        	String answer = "Please be patient, my master will answer soon...";
        	boolean hasAnswered = false;
            Paging paging = new Paging(1);
            List<DirectMessage> messages = null;
            do {
                messages = twitter.getDirectMessages(paging);
                for (DirectMessage message : messages) {
                    System.out.println("From: @" + message.getSenderScreenName() + " id:" + message.getId() + " - "
                            + message.getText());
                    uid = message.getSenderId();
                    if(message.getText().equals(answer)){
                    	hasAnswered = true;
                    }
                    
                }
                paging.setPage(paging.getPage() + 1);
            } while (messages.size() > 0 && paging.getPage() < 10);
            if(hasAnswered || uid == 0){System.out.println("bereits geantwortet");}
            else{
            	System.out.println("noch nicht geantwortet");
            	DirectMessage message = twitter.sendDirectMessage(uid, answer);
            	System.out.println("Message sent to UserId: " + uid);
            }
            System.out.println("done.");
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to get messages: " + te.getMessage());
        }
    }
}