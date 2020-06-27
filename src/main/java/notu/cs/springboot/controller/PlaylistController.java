package notu.cs.springboot.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonProcessingException;

import notu.cs.springboot.entity.Playlist;
import notu.cs.springboot.entity.RegistrationParameter;
import notu.cs.springboot.service.ConverterService;
import notu.cs.springboot.service.FunctionService;
import notu.cs.springboot.service.UserService;

@Controller
public class PlaylistController {
	
	@Autowired
	private ConverterService converterService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private FunctionService functionService;

	@GetMapping("/index")
    public String homePage() {
        return "index";
    }
	
	@GetMapping("/registerPage")
    public String registerPage() {
        return "registerPage";
    }
	
	@GetMapping("/loginPage")
    public String loginPage() {
        return "loginPage";
    }
	
	@GetMapping("/memberIndex")
    public String memberIndex() {
        return "memberIndex";
    }
	
	@PostMapping("/parseurl")
	public ResponseEntity<String> parseUrl(@RequestBody String playlistUrl) throws IOException {
		String token = playlistUrl.substring(0, playlistUrl.length()-1);
		String listJsonStr = converterService.parsePlaylistToken(token);
		
		return ResponseEntity.ok().body(listJsonStr);
	}
	
	@RequestMapping(value="/register", method=RequestMethod.POST)
	public String register(@ModelAttribute RegistrationParameter para) {
		userService.register(para.getAccount(), para.getPassword(), para.getUserName());
		return "redirect:/index";
	}
	
	@PostMapping(value="/login")
	public String doLogin(@ModelAttribute RegistrationParameter para, HttpSession session) {
		if (userService.login(para.getAccount(), para.getPassword())) {
			session.setAttribute("uid", para.getAccount());
			return "redirect:/memberIndex";
		} else {
			return "redirect:/loginPage";
		}
	}
	
	@GetMapping("/getuserplaylist")
    public ResponseEntity<String> getUserPlaylist(HttpSession session) throws IOException {
		String list = functionService.getUserPlaylist(session.getAttribute("uid").toString());
        return ResponseEntity.ok().body(list);
    }
	
	@RequestMapping(value="/insertplaylist", method=RequestMethod.POST, produces="text/plain;charset=utf-8")
	public ResponseEntity<String> insertPlaylist(@RequestParam String name, @RequestBody String listStr, HttpSession session) throws JsonProcessingException, UnsupportedEncodingException {
		String owner = session.getAttribute("uid").toString();
		functionService.insertPlaylist(owner, name, listStr);
		return ResponseEntity.ok().body("Save playlist success!");
	}
	
	@DeleteMapping("/deleteplaylist")
	public ResponseEntity<Playlist> deletePlaylist(@RequestParam String name, HttpSession session) {
		String owner = session.getAttribute("uid").toString();
		functionService.deletePlaylist(owner, name);
		return ResponseEntity.noContent().build();
	}
}
