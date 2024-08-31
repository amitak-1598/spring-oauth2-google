package net.codejava;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AppController {

    private final ProductService service;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AppController(ProductService service, 
                         UserRepository userRepository, 
                         PasswordEncoder passwordEncoder, 
                         AuthenticationManager authenticationManager) {
        this.service = service;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody User signupRequest) {
    	System.out.println("Amit");
        if (userRepository.getUserByUsername(signupRequest.getUsername()) != null) {
            return new ResponseEntity<>("User already exists", HttpStatus.BAD_REQUEST);
        }

        User user = new User();
        user.setUsername(signupRequest.getUsername());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        user.setEnabled(true);
        user.setProvider(Provider.LOCAL);

        userRepository.save(user);

        return new ResponseEntity<>("User registered successfully", HttpStatus.OK);
    }

    @PostMapping("/signin")
    public ResponseEntity<String> login(@RequestBody User loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return new ResponseEntity<>("User logged in successfully", HttpStatus.OK);
    }

//    @RequestMapping("/list")
//    public String viewHomePage(Model model) {
//        List<Product> listProducts = service.listAll();
//        model.addAttribute("listProducts", listProducts);
//        return "products";
//    }
    @RequestMapping("/list")
    public String viewHomePage(Model model) {
        System.out.println("Hello");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        // Check if the principal is an instance of CustomOAuth2User
        String username;
        if (authentication.getPrincipal() instanceof CustomOAuth2User) {
            CustomOAuth2User oauthUser = (CustomOAuth2User) authentication.getPrincipal();
            username = oauthUser.getEmail(); // Use email from Google authentication
        } else {
            username = authentication.getName(); // Use the username from other authentication methods
        }
        
        System.out.println("type of username " + username);
        
        User user = userRepository.getUserByUsername(username); // Existing repository method
        Long userId = user.getId();
        
        // Get the products for the specific user
        List<Product> listProducts = service.listAll(userId);
        model.addAttribute("listProducts", listProducts);
        return "products";
    }


    @RequestMapping("/new")
    public String showNewProductForm(Model model) {
        Product product = new Product();
        model.addAttribute("product", product);
        return "new_product";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String saveProduct(@ModelAttribute("product") Product product) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
     
        String username;
        if (authentication.getPrincipal() instanceof CustomOAuth2User) {
            CustomOAuth2User oauthUser = (CustomOAuth2User) authentication.getPrincipal();
            username = oauthUser.getEmail();
        } else {
            username = authentication.getName(); 
        }
        
      
        User user = userRepository.getUserByUsername(username);   
        product.setUser(user);
        service.save(product);
        return "redirect:/list";
    }


    @RequestMapping("/edit/{id}")
    public ModelAndView showEditProductForm(@PathVariable(name = "id") Long id) {
        ModelAndView mav = new ModelAndView("edit_product");
        Product product = service.get(id);
        mav.addObject("product", product);
        return mav;
    }

    @RequestMapping("/delete/{id}")
    public String deleteProduct(@PathVariable(name = "id") Long id) {
        service.delete(id);
        return "redirect:/list";
    }
}










































//package net.codejava;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.servlet.ModelAndView;
//
//@Controller
//public class AppController {
//	@Autowired
//	private ProductService service;
//	
//    @Autowired
//    private UserRepository userrepository;
//	
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//    
//    @Autowired
//    private AuthenticationManager authenticationManager;
//	
//	@PostMapping("/signup")
//    public ResponseEntity<String> signup(@RequestBody User signupRequest) {
//       
//        if (userrepository.getUserByUsername(signupRequest.getUsername()) != null) {
//            return new ResponseEntity<>("User already exists", HttpStatus.BAD_REQUEST);
//        }
//
//        User user = new User();
//        user.setUsername(signupRequest.getUsername());
//        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
//        user.setEnabled(true);
//        user.setProvider(Provider.LOCAL);
//
//        userrepository.save(user);
//
//        return new ResponseEntity<>("User registered successfully", HttpStatus.OK);
//    }
//	
//	
//	 @PostMapping("/login")
//	    public ResponseEntity<String> login(@RequestBody User loginRequest) {
//	        Authentication authentication = authenticationManager.authenticate(
//	                new UsernamePasswordAuthenticationToken(
//	                        loginRequest.getUsername(),
//	                        loginRequest.getPassword()
//	                )
//	        );
//
//	        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//	        return new ResponseEntity<>("User logged in successfully", HttpStatus.OK);
//	    }
//	
//	@RequestMapping("/list")
//	public String viewHomePage(Model model) {
//		List<Product> listProducts = service.listAll();
//		model.addAttribute("listProducts", listProducts);
//		
//		return "products";
//	}
//	
//	@RequestMapping("/new")
//	public String showNewProductForm(Model model) {
//		Product product = new Product();
//		model.addAttribute("product", product);
//		
//		return "new_product";
//	}
//	
//	@RequestMapping(value = "/save", method = RequestMethod.POST)
//	public String saveProduct(@ModelAttribute("product") Product product) {
//		service.save(product);
//		
//		return "redirect:/list";
//	}
//	
//	@RequestMapping("/edit/{id}")
//	public ModelAndView showEditProductForm(@PathVariable(name = "id") Long id) {
//		ModelAndView mav = new ModelAndView("edit_product");
//		
//		Product product = service.get(id);
//		mav.addObject("product", product);
//		
//		return mav;
//	}	
//	
//	@RequestMapping("/delete/{id}")
//	public String deleteProduct(@PathVariable(name = "id") Long id) {
//		service.delete(id);
//		
//		return "redirect:/list";
//	}
//}
