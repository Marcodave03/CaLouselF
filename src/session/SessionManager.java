package session;

import model.User;

public class SessionManager {
	 private static User currentUser;

	    public static void setCurrentUser(User user) {
	        currentUser = user;  //menyimpan user yang logged in
	    }

	    public static User getCurrentUser() {
	        return currentUser;
	    }

	    public static void clearSession() {
	        currentUser = null;
	    }

	    public static boolean isLoggedIn() {
	        return currentUser != null;
	    }
}	
