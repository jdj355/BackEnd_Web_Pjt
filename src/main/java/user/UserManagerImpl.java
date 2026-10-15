package user;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class UserManagerImpl implements UserManager {
	
	Map<String ,User> userMap = new HashMap<>();
	private File file = new File("db/userData.json");
	private Gson gson = new Gson();
	
	// 싱글톤
	private static UserManagerImpl um = new UserManagerImpl();
	private UserManagerImpl() { }
	public static UserManagerImpl getInstance() {
		if(um == null) {
			return new UserManagerImpl();
		}
		return um;
	}
	
	@Override
	public boolean add(User user) {		
		if(user.getUserId().isEmpty() || user.getPassword().isEmpty() 
				|| !isAlphabet(user.getUserId()) || !isAlphabet(user.getPassword())) {
			return false;
		}
		userMap.put(user.getUserId(), user);
		return true;
	}
	
	@Override
	public User getUser(String userId) {
		return userMap.get(userId);
	}
	
	@Override
	public boolean update(User user) {
		if(!userMap.containsKey(user.getUserId())) return false;
		userMap.put(user.getUserId(), user);
		return true;
	}
	
	@Override
	public boolean delete(String userId) {
		if(!userMap.containsKey(userId)) return false;
		
		userMap.remove(userId);
		
		return true;
	}
	
	@Override
	public boolean login(String userId, String password) throws LoginException {
	    User user = userMap.get(userId);
	    if (user == null || !user.getPassword().equals(password)) {
	        throw new LoginException(userId, password);
	    }
	    return true;
	}
	
	@Override
	public boolean logout(String userId) {
		User user = userMap.get(userId);
		
		if(user == null || !user.getLoginStatus().equals("Y")) return false;
		
		user.setLoginStatus("N");
		return true;
	}
	
	@Override
	public void loadUserData() {
		if(file.exists()) {
			try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
				
				String line;
				StringBuilder sb = new StringBuilder();
				
				while((line = br.readLine()) != null) {
					sb.append(line);
				}
				
				Type userListType = new TypeToken<ArrayList<User>> () {
				}.getType();
				
				userMap = gson.fromJson(sb.toString(), userListType);
				
				if (userMap == null) {
				    userMap = new HashMap<>();
				}
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			    System.out.println("경로: " + file.getAbsolutePath());
			    System.out.println("파일인가? " + file.isFile());
			    System.out.println("읽기 가능? " + file.canRead());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public boolean isAlphabet(String str) {
		return Pattern.matches("^[a-zA-Z]*$", str);
	}
	
}
