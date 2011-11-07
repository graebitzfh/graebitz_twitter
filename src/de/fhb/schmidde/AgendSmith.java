package de.fhb.schmidde;

import twitter4j.DirectMessage;
import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import java.util.List;

public final class AgendSmith {
	
    public static void main(String[] args) {
        
    	int mode = 9;
    	long id = 0L;
    	In in = new In();
    
    	
    	do{
    		System.out.println("Modus eingeben: ");
    		System.out.println("1 - User folgen | 2 - lauschen | 0 - beenden");
    		mode = in.readInt();
    		if(mode == 1){
	    		System.out.println("UserID eingeben:");
	    		id = in.readLong();
	    		getMyTimeline(id);
    		}
    		else if(mode == 2){
    			//Agend has to answer questions
    			getMessage();
    		}
    		else if(mode == 0){
    			System.out.println("Beendet!");
    			System.exit(0);
    		}
    		else System.out.println("Modus falsch");
    		
    	}while(mode != 0);
    }
    public static void getMyTimeline(long id){
    	
    	GetConfiguredTwitterObject co = new GetConfiguredTwitterObject();
    	Twitter twitter = co.getTwitter();
		
        try {
        	twitter.createFavorite(id);
            List<Status> statuses = twitter.getHomeTimeline();
            System.out.println("Showing Timeline:");
            for (Status status : statuses) {
                System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText());
            }
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to get timeline: " + te.getMessage());
            System.exit(-1);
        }
    }
    public static void getMessage() {
    	
    	GetConfiguredTwitterObject co = new GetConfiguredTwitterObject();
    	Twitter twitter = co.getTwitter();
    	
        try {
            Paging paging = new Paging(1);
            List<DirectMessage> messages;
            do {
                messages = twitter.getDirectMessages(paging);
                for (DirectMessage message : messages) {
                    System.out.println("From: @" + message.getSenderScreenName() + " id:" + message.getId() + " - "
                            + message.getText());
                }
                paging.setPage(paging.getPage() + 1);
            } while (messages.size() > 0 && paging.getPage() < 10);
            System.out.println("done.");
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to get messages: " + te.getMessage());
            System.exit(-1);
        }
    }
}