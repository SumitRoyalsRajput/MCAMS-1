package com.mcams.ui;

import java.io.Console;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

import com.mcams.bean.ArtistBean;
import com.mcams.bean.AuthenticationBean;
import com.mcams.bean.ComposerBean;
import com.mcams.bean.SongBean;
import com.mcams.exception.AppException;
import com.mcams.service.AdminService;
import com.mcams.service.AuthenticationService;
import com.mcams.service.UserService;
import com.mcams.service.ValidationService;

public class MCAMS {
	static Console console = System.console();
	static Scanner scan = new Scanner(System.in);
	
	static int attempt = 3;
	static AuthenticationBean authBean = new AuthenticationBean();
	static ArtistBean artBean = new ArtistBean();
	static ComposerBean compBean = new ComposerBean();
	static SongBean songBean = new SongBean();
	static AuthenticationService authService = new AuthenticationService();
	static ValidationService valService = new ValidationService();
	static AdminService adminService = new AdminService();
	static UserService userService = new UserService();
	
	public static void main(String[] args) throws AppException {loginHome();}

	private static void loginHome() throws AppException {
		while(true) {
			clearScreen();			
			System.out.println("*************Music_Composer_and_Artist_Management_System*************\n");
			
			String userId;
			String password;
			int result;
			
			int choice;
			while(true){
				System.out.println("1. Login");
				System.out.println("2. Exit");
				System.out.print("Enter your choice: ");
				choice = valService.validateChoice(scan.nextLine());
				
				if(choice == 1)	break;
				else if(choice == 2)	exit();
				else System.out.println("\nPlease enter valid choice!\n");
			}
			
			while(true){
				if(attempt<3)
					System.out.println("Login (attempt remaining: "+attempt+")");
				
				while(true) {
					System.out.print("\nEnter user id: ");
					userId = scan.nextLine();
					boolean isValid = valService.validateUserId(userId);
					if(isValid) break;
					else System.out.println("\nInvalid ID! (ID should be 6 digit number)\n");
				}
				
				System.out.print("Enter password: ");
				if(console != null)
					password = new String(console.readPassword());
				else
					password = scan.nextLine();
		
				authBean.setUserId(Integer.parseInt(userId));
				authBean.setPassword(password);
				
				/*
				 * int result:-
				 *  0 => Correct credentials
				 *  1 => User record found but password not matched
				 * -1 => User record not present in database
				 */
				try {
					result = authService.checkCredentials(authBean);
				} catch (AppException e) {
					throw new AppException(e.getMessage());
				}
				
				if(result == 0) {
					System.out.println("\nLogin Successful!\n\n");
					break;
				}
				else {
					if(result == 1)
						System.out.println("\nERROR: Invalid Password!\n");
					else
						System.out.println("\nERROR: user "+authBean.getUserId()+" doesn't exist.\n");
					
					attempt--;
					
					if(attempt==0) {
						System.out.println("\nMaximum attempt reached!");
						exit();
					}
				}		
			}
			
			clearScreen();
			
			if(userId.equals("100000"))
				adminConsole(authBean.getUserId(),authBean.getPassword());
			else
				clientConsole(authBean.getUserId(),authBean.getPassword());
		}
	}

	private static void adminConsole(int userId,String password) throws AppException {
		int choice;
		System.out.println("Welcome admin!\n");
		
		while(true){
			
			System.out.println("1. Create Artist/Composer");
			System.out.println("2. Edit Artist/Composer");
			System.out.println("3. Associate song(s) to Artist/Composer");
			System.out.println("4. Search Artist/Composer");
			System.out.println("5. LOGOUT");
			System.out.println("6. EXIT");
			System.out.print("Enter your choice: ");
			
			choice = valService.validateChoice(scan.nextLine());
			
			switch(choice){
			case 1: createAC(userId,password); break;
			case 2: editAC(); break;	
			case 3: songAssociation(); break;
			case 4: searchAC(userId); break;
			case 5: attempt=3; return;
			case 6: exit();
			default: System.out.println("Please enter valid choice!\n");
			}
		}
	}
	
	private static void clientConsole(int userId, String password) {
		int choice;
		
		while(true) {
			System.out.println("Welcome user: "+userId+"\n");
			System.out.println("1. Search Artist/Composer's songs");
			System.out.println("2. LOGOUT");
			System.out.println("3. EXIT");
			System.out.print("Enter your choice: ");
			
			choice = valService.validateChoice(scan.nextLine());
			
			switch(choice) {
			case 1: searchAC(userId); break;
			case 2: return;
			case 3: exit();
			default: System.out.println("\nPlease enter valid choice!\n");
			}
		}
	}
	
	private static void createAC(int userId, String password) throws AppException {
		int choice;
		
		clearScreen();
		
		while(true) {
			System.out.println();
			System.out.println("1. Create Artist");
			System.out.println("2. Create Composer");
			System.out.println("3. Back");
			System.out.println("4. Exit");
			System.out.print("Enter choice: ");
			choice = scan.nextInt();
			scan.nextLine();
		
			if(choice==1) {
				createArtist(userId);
				break;
			}
			else if(choice==2) {
				createComposer(userId);
				break;
			}
			else if(choice==3) {clearScreen(); return;}
			else if(choice==4) exit();
			else System.out.println("\nPlease enter valid choice!\n");
		}
	
	}



	private static void editAC() {
		int choice;
		
		clearScreen();
		
		while(true) {
			System.out.println();
			System.out.println("1. Edit Artist");
			System.out.println("2. Edit Composer");
			System.out.println("3. Back");
			System.out.println("4. Exit");
			System.out.print("Enter choice: ");
			choice = scan.nextInt();
			scan.nextLine();
		
			if(choice==1) {
				editArtist(userId);
				break;
			}
			else if(choice==2) {
				editComposer(userId);
				break;
			}
			else if(choice==3) {clearScreen(); return;}
			else if(choice==4) exit();
			else System.out.println("\nPlease enter valid choice!\n");
		}
	}
	
	////Edit composer details
	
	private static void editComposer(int userId) {
		// TODO Auto-generated method stub
		System.out.println("Edit composer method");
	}

	
	////edit artist details
	private static void editArtist(int userId) {
		// TODO Auto-generated method stub
		
	}

	private static void songAssociation() {
		// TODO Auto-generated method stub
		
	}

	private static void searchAC(int userId) {
		clearScreen();
		String choice;
		while(true) {
			System.out.println("1. Search Artist's songs");
			System.out.println("2. Search Composer's songs");
			System.out.println("3. Back");
			System.out.println("4. Exit");
			System.out.print("Enter your choice: ");
			choice = scan.nextLine();
			
			if(choice.equals("1")) seachArtistSongs(userId);
			else if(choice.equals("2")) searchComposerSongs(userId);
			else if(choice.equals("3")) return;
			else if(choice.equals("4")) exit();
			else System.out.println("\nPlease enter valid choice!\n");
		}
	}

	private static void createArtist(int userId) {
		
		String name;
		LocalDate bDate, dDate;
		
		while(true){
			clearScreen();
		
			while(true){
				System.out.println("\nCREATE ARTIST");
				System.out.print("Enter name: ");
				name = scan.nextLine();
				boolean isValid = valService.validateName(name);
				if(isValid) break;
				else System.out.println("\nPlease enter valid name! (MinChar:3, MaxChar:50)\n");
			}
		
			while(true){
				System.out.print("Enter Born Date (dd/mm/yyyy) (If not available then type 'NA'): ");
				String input = scan.nextLine();
			
				if(!input.equalsIgnoreCase("NA")){
					bDate = valService.validateDate(input);
					if(bDate!=null) break;
					else System.out.println("\nPlease enter valid date! (dd/mm/yyyy)\n");
				}
				else{
					bDate=null;
					break;
				}
			}
		
			while(true){
				System.out.print("Enter Died Date (mm/dd/yyyy) (If not available then type 'NA'): ");
				String input = scan.nextLine();
			
				if(!input.equalsIgnoreCase("NA")){
					dDate = valService.validateDate(input);
					if(bDate.isAfter(dDate)){
						System.out.println("\nDied date should be after Born date");
						continue;
					}
					if(dDate!=null) break;
					else System.out.println("\nPlease enter valid date! (dd/mm/yyyy)\n");
				}
				else{
					dDate=null;
					break;
				}
			}
		
			artBean.setName(name);
			artBean.setBornDate(bDate);
			artBean.setDiedDate(dDate);
			artBean.setUpdatedBy(userId);
			artBean.setCreatedBy(userId);
		
			ArtistBean getBean = adminService.createArtist(artBean,false);
			
			if(getBean.getId()>0) System.out.println("\nArtist record submitted with id: "+getBean.getId()+"\n");
			
			else if(getBean.getId()<0){
				
				if(getBean.getId()>-500000) {
					artBean.setId(Math.abs(getBean.getId()));
					
					while(true){
						System.out.print("\nThis Artist already present.\nDo you want to update it with this information? (y/n): ");
						String persist = scan.nextLine();
						
						if(persist.equalsIgnoreCase("y")){						
							getBean = adminService.createArtist(artBean,true);
							System.out.println("\nArtist record submitted with id: "+getBean.getId()+"\n");
							break;
						}
						else if(persist.equalsIgnoreCase("n")) {
							System.out.println("\nOperation Cancelled!\n");
							break;
						}
						else System.out.println("\nPlease enter valid input! (Y or N)\n");
					}
				// TODO 2sec thread sleep
				}
				else {
					artBean.setId(Math.abs(getBean.getId()+100000));
					getBean = adminService.createArtist(artBean,true);
					System.out.println("\nArtist record submitted with id: "+getBean.getId()+"\n");
				}
			}
			else System.out.println("Something went wrong... please try again later.");
		}	
	}
	
	private static void createComposer(int userId) {
		
		String name,caeIpi;
		LocalDate bDate, dDate;
		char[] mSocietyId;
		
		while(true){
			clearScreen();
		
			while(true){
				System.out.print("Enter name: ");
				name = scan.nextLine();
				boolean isValid = valService.validateName(name);
				if(isValid) break;
				else System.out.println("\nPlease enter valid name! (MinChar:3, MaxChar:50)\n");
			}
		
			while(true){
				System.out.print("Enter Born Date (dd/mm/yyyy) (If not available then type 'NA'): ");
				String input = scan.nextLine();
			
				if(!input.equalsIgnoreCase("NA")){
					bDate = valService.validateDate(input);
					if(bDate!=null) break;
					else System.out.println("\nPlease enter valid date! (dd/mm/yyyy)\n");
				}
				else{
					bDate=null;
					break;
				}
			}
		
			while(true){
				System.out.print("Enter Died Date (mm/dd/yyyy) (If not available then type 'NA'): ");
				String input = scan.nextLine();
			
				if(!input.equalsIgnoreCase("NA")){
					dDate = valService.validateDate(input);
					if(dDate!=null) break;
					else System.out.println("\nPlease enter valid date! (dd/mm/yyyy)\n");
				}
				else{
					dDate=null;
					break;
				}
			}
			
			while(true){
				System.out.print("Enter CAE IPI number: ");
				caeIpi = scan.nextLine();
				boolean isValid = valService.validateCaeIpi(caeIpi);
				if(isValid) break;
				else System.out.println("\nPlease enter valid name! (MinChar:3, MaxChar:50)\n");
			}
		
			while(true) {
				System.out.print("Enter music society ID: ");
				mSocietyId = scan.nextLine().toCharArray();
				boolean isValid = valService.validateMSocietyId(mSocietyId);
				if(isValid) break;
				else System.out.println("\nPlease enter valid input!\n");
			}
			
			compBean.setName(name);
			compBean.setBornDate(bDate);
			compBean.setDiedDate(dDate);
			compBean.setCaeipiNumber(caeIpi);
			compBean.setMusicSocietyId(mSocietyId);
			compBean.setUpdatedBy(userId);
			
			int genId = adminService.createComposer(compBean,false);
			
			if(genId>0) System.out.println("\nComposer record submitted with id: "+genId+"\n");
			else if(genId<0){
				if(genId>-300000) {
					compBean.setId(Math.abs(genId));
					while(true){
						System.out.print("\nThis Composer already present.\nDo you want to update it with this information? (y/n): ");
						String persist = scan.nextLine();
						
						if(persist.equalsIgnoreCase("y")){
							genId = adminService.createComposer(compBean,true);
							System.out.println("\nComposer record submitted with id: "+genId+"\n");
							break;
						}
						else if(persist.equalsIgnoreCase("n")) {
							System.out.println("\nOperation Cancelled!\n");
							break;
						}
						else System.out.println("\nPlease enter valid input! (Y or N)\n");
					}
				// TODO 2sec thread sleep
				}
				else {
					compBean.setId(Math.abs(genId+100000));
					genId = adminService.createComposer(compBean,true);
					System.out.println("\nComposer record submitted with id: "+genId+"\n");
				}
			}
			else System.out.println("Something went wrong... please try again later.");
		}
	}
	
	private static void seachArtistSongs(int userId) {
		String name;
		ArrayList<SongBean> songList;
		while(true) {
			System.out.print("Enter artist name: ");
			name=scan.nextLine();
			
			if(userId==100000) songList=adminService.searchArtist(name);
			else songList=userService.searchArtist(name);
			
			if(songList==null) System.out.println("\nNo record found!\n");
			else{
				System.out.println("\nSongs associated to "+name);
				int i=1;
				for (SongBean songBean : songList) {
					if(userId==100000)
						System.out.println(songBean.getId()+" "+songBean.getName()+" "+songBean.getDuration()+" "+songBean.getCreatedBy()+" "+songBean.getCreatedOn()+" "+songBean.getUpdatedBy()+" "+songBean.getUpdatedOn());
					else {
						System.out.println(i+") "+songBean.getName()+" "+songBean.getDuration());
						i++;
					}
				}
			}
			
			boolean isContinue;
			while(true) {
				System.out.println("Do you want to continue? (y/n): ");
				String choice = scan.nextLine();
			
				if(choice.equalsIgnoreCase("y")) {clearScreen(); isContinue=true; break;}
				else if(choice.equalsIgnoreCase("n")) {clearScreen(); isContinue=false; break;}
				else System.out.println("\nPlease enter valid choice!");
			}
			
			if(!isContinue) break;
		}
	}

	private static void searchComposerSongs(int userId) {
		String name;
		ArrayList<SongBean> songList;
		while(true) {
			System.out.print("Enter composer name: ");
			name=scan.nextLine();
			
			if(userId==100000) songList=adminService.searchComposer(name);
			else songList=userService.searchComposer(name);
			
			if(songList==null) System.out.println("\nNo record found!\n");
			else{
				System.out.println("\nSongs associated to "+name);
				int i=1;
				for (SongBean songBean : songList) {
					if(userId==100000)
						System.out.println(songBean.getId()+" "+songBean.getName()+" "+songBean.getDuration()+" "+songBean.getCreatedBy()+" "+songBean.getCreatedOn()+" "+songBean.getUpdatedBy()+" "+songBean.getUpdatedOn());
					else {
						System.out.println(i+") "+songBean.getName()+" "+songBean.getDuration());
						i++;
					}
				}
			}
			
			boolean isContinue;
			while(true) {
				System.out.println("Do you want to continue? (y/n): ");
				String choice = scan.nextLine();
			
				if(choice.equalsIgnoreCase("y")) {clearScreen(); isContinue=true; break;}
				else if(choice.equalsIgnoreCase("n")) {clearScreen(); isContinue=false; break;}
				else System.out.println("\nPlease enter valid choice!");
			}
			
			if(!isContinue) break;
		}
	}
	
	public static void clearScreen() {
		if(console != null)
			try {
				new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
			} catch (Exception e) {
				//e.printStackTrace();
			}
	}
	
	public static void exit() {
		System.out.println("Exiting program...");
		System.exit(0);		
	}

}
