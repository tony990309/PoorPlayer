package notu.cs.springboot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import notu.cs.springboot.entity.User;
import notu.cs.springboot.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	public void register(String id, String pw, String name) {
		User user = new User();
		user.set_id(id);
		user.setPassword(pw);
		user.setUserName(name);
		user.setStatus("offline");
		
		userRepository.insert(user);
	}
	
	public boolean login(String account, String password) {
		User user = userRepository.findBy_id(account);
		if (!password.equals(user.getPassword())) {
			System.out.println("Wrong password!");
			return false;
		}
		System.out.println("Login success!");
		return true;
	}
	
}
