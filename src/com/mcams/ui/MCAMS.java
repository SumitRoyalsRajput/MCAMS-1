package com.mcams.ui;

import java.io.Console;
import java.time.LocalDate;
import java.time.LocalTime;
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
			String userId;
			String password;
			int result;
			
			int choice;
			while(true){
				clearScreen();			
				System.out.println("*************Music_Composer_and_Artist_Management_System*************\n");
				
				System.out.println("1. Login");
				System.out.println("2. Exit");
				System.out.print("Enter your choice: ");
				choice = valService.validateChoice(scan.nextLine());
				
				if(choice == 1)	break;
				else if(choice == 2)	exit();
				else System.out.println("\nPlease enter valid choice!\n");
			}
			
			
				
			while(true) {
				System.out.print("\nEnter user id: ");
				userId = scan.nextLine();
				boolean isValid = valService.validateUserId(userId);
				if(isValid) break;
				else System.out.println("\nInvalid ID! (ID should be 6 digit number)\n");
			}
				
			while(true){
				if(attempt<3)
					System.out.println("Login (attempt remaining: "+attempt+")");
				
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
			
			if(userId.equals("100000") || userId.equals("100001"))
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
			System.out.println("2. Search Artist/Composer");
			System.out.println("3. Edit Artist/Composer");
			System.out.println("4. Delete Artist/Composer");
			System.out.println("5. Associate song to Artist/Composer");
			System.out.println("6. Search Artist/Composer song(s)");
			System.out.println("7. Delete Song");
			System.out.println("8. LOGOUT");
			System.out.println("9. EXIT");
			System.out.print("Enter your choice: ");
			
			choice = valService.validateChoice(scan.nextLine());
			
			switch(choice){
			case 1: createAC(userId,password); break;
			case 2: searchAC(); break;
			case 3: editAC(userId); break;	
			case 4: deleteAC(userId); break;
			case 5: songAssociate(userId); break;
			case 6: searchSong(userId); break;
			case 7: deleteSong(userId); break;
			case 8: attempt=3; return;
			case 9: exit();
			default: System.out.println("Please enter valid choice!\n");
			}
		}
	}
	
	private static void deleteSong(int userId) {
		while(true) {
			clearScreen();
			String name;
			while(true){
				System.out.println("\nDELETE SONG");
				System.out.print("Enter Song Name: ");
				name = scan.nextLine();
				boolean isValid = valService.validateName(name);
				if(isValid) break;
				else System.out.println("\nPlease enter valid name! (MinChar:3, MaxChar:50)\n");
			}
			
			SongBean getSong = adminService.searchSong(name);
			
			if(getSong!=null) {
				int result = adminService.deleteSong(getSong.getId(), userId);
				if(result==0) System.out.println("Song Deleted Successfully!");
				else System.out.println("Something went wrong! Please try again later...");
			}
			else System.out.println("\nNo Record Found!\n");
			
			while(true) {
				System.out.print("\nDo you want to continue? (y/n): ");
				String choice = scan.nextLine();
				
				if(choice.equalsIgnoreCase("y")) break;
				else if(choice.equalsIgnoreCase("n")) {clearScreen(); return;}
				else System.out.println("\nPlease enter valid choice!\n");
			}
		}
	}

	private static void deleteAC(int userId) {
		int choice;
		clearScreen();
		System.out.println("DELETE ARTIST/COMPOSER\n");
		while(true) {
			System.out.println("1. Delete Artist");
			System.out.println("2. Delete Composer");
			System.out.println("3. Back");
			System.out.println("4. Exit");
			System.out.print("Enter your choice: ");
		
			choice = valService.validateChoice(scan.nextLine());
			
			switch(choice) {
			case 1: deleteArtist(userId); break;
			case 2: deleteComposer(userId); break;
			case 3: {clearScreen(); return;}
			case 4: exit();
			default: System.out.println("\nPlease enter valid choice!\n");
			}
		}
	}

	private static void deleteComposer(int userId) {
		while(true) {
			clearScreen();
			System.out.println("DELETE COMPOSER\n");
			String name;
			while(true){
				System.out.print("Enter Composer name: ");
				name = scan.nextLine();
				boolean isValid = valService.validateName(name);
				if(isValid) break;
				else System.out.println("\nPlease enter valid name! (MinChar:3, MaxChar:50)\n");
			}
			
			ComposerBean getBean = adminService.searchComposer(name);
			String bornDate,diedDate, caeIpi, mSocietyId;
			if(getBean != null){
				
				if(getBean.getBornDate() == null) bornDate="NA";
				else bornDate = getBean.getBornDate().toString();
				if(getBean.getDiedDate() == null) diedDate="NA";
				else diedDate = getBean.getDiedDate().toString();
				
				caeIpi = getBean.getCaeipiNumber();
				mSocietyId = new String(getBean.getMusicSocietyId());
				
				System.out.println("Composer's Born Date: "+bornDate);
				System.out.println("Composer's Died Date: "+diedDate);
				System.out.println("Composer'c CAE IPI No.: "+caeIpi);
				System.out.println("Composer's Music Society ID: "+mSocietyId);
				
				System.out.print("\nDo you want to delete this composer? (y/n): ");
				String choice = scan.nextLine();

				if(choice.equalsIgnoreCase("y")) {
//					System.out.println("Please wait...");
					int result = adminService.deleteComposer(getBean.getId(),userId);
					if(result==0) System.out.println("Composer Deleted successfully!");
					else System.out.println("\nSomething went wrong! Please try again later...");
				}
				else return;			
			}
			else System.out.println("\nNo Record Found!\n");
			
			while(true) {
				System.out.print("\nDo you want to continue? (y/n): ");
				String choice = scan.nextLine();
				
				if(choice.equalsIgnoreCase("y")) break;
				else if(choice.equalsIgnoreCase("n")) {clearScreen(); return;}
				else System.out.println("\nPlease enter valid choice!\n");
			}
		}
		
	}

	private static void deleteArtist(int userId) {
		while(true) {
			clearScreen();
			String name;
			while(true){
				System.out.println("\nDELETE ARTIST");
				System.out.print("Enter Artist name: ");
				name = scan.nextLine();
				boolean isValid = valService.validateName(name);
				if(isValid) break;
				else System.out.println("\nPlease enter valid name! (MinChar:3, MaxChar:50)\n");
			}
			
			ArtistBean getBean = adminService.searchArtist(name);
			String bornDate,diedDate;
			if(getBean != null){
				
				if(getBean.getBornDate() == null) bornDate="NA";
				else bornDate = getBean.getBornDate().toString();
				if(getBean.getDiedDate() == null) diedDate="NA";
				else diedDate = getBean.getDiedDate().toString();
				
				System.out.println("Artist's Born Date: "+bornDate);
				System.out.println("Artist's Died Date: "+diedDate);
				
				System.out.print("\nDo you want to delete this artist? (y/n): ");
				String choice = scan.nextLine();
				
				if(choice.equalsIgnoreCase("y")) {
//					System.out.println("Please wait...");
					int result = adminService.deleteArtist(getBean.getId(),userId);
					if(result==0) System.out.println("Artist Deleted successfully!");
					else System.out.println("\nSomething went wrong! Please try again later...");
				}
				else return;			
			}
			else System.out.println("\nNo Record Found!\n");
			
			while(true) {
				System.out.print("\nDo you want to continue? (y/n): ");
				String choice = scan.nextLine();
				
				if(choice.equalsIgnoreCase("y")) break;
				else if(choice.equalsIgnoreCase("n")) {clearScreen(); return;}
				else System.out.println("\nPlease enter valid choice!\n");
			}
		}
	}

	private static void songAssociate(int userId) {
		int choice;
		clearScreen();
		System.out.println("ARTIST/COMPOSER SONG ASSOCIATION\n");
		while(true) {
			System.out.println("1. Associate songs to Artist");
			System.out.println("2. Associate songs to Composer");
			System.out.println("3. Back");
			System.out.println("4. Exit");
			System.out.print("Enter your choice: ");
		
			choice = valService.validateChoice(scan.nextLine());
			
			switch(choice) {
			case 1: artSongAssoc(userId); break;
			case 2: compSongAssoc(userId); break;
			case 3: {clearScreen(); return;}
			case 4: exit();
			default: System.out.println("\nPlease enter valid choice!\n");
			}
		}
	}

	private static void compSongAssoc(int userId) {
		while(true) {
			clearScreen();
			System.out.println("COMPOSER SONG ASSOCIATION\n");
			
			String songName, compName;
			LocalTime duration;
			
			while(true){
				System.out.print("Song name: ");
				songName = scan.nextLine();
				boolean isValid = valService.validateName(songName);
				if(isValid) break;
				else System.out.println("\nPlease enter valid name! (MinChar:3, MaxChar:50)\n");
			}
			
			while(true){
				System.out.print("Duration: ");
				duration = valService.validateDuration(scan.nextLine());
				if(duration!=null) break;
				else System.out.println("\nPlease enter valid Duration! (mm:ss)\n");
			}
			songBean.setName(songName);
			songBean.setDuration(duration);
			songBean.setCreatedBy(userId);
			songBean.setUpdatedBy(userId);
			
			while(true){
				
				while(true){
					System.out.print("Enter Composer Name you want to associate with: ");
					compName = scan.nextLine();
					boolean isValid = valService.validateName(compName);
					if(isValid) break;
					else System.out.println("\nPlease enter valid name! (MinChar:3, MaxChar:50)\n");
				}
				
				compBean = adminService.searchComposer(compName);
				
				if(compBean != null) {
					SongBean getSongBean = adminService.assocComposer(songBean, compBean, userId, false);
					if(getSongBean.getId()<0) {
						if(getSongBean.getId()>-400000) {
							songBean.setId(Math.abs(getSongBean.getId()));
							while(true){
								System.out.print("\nThis song already present.\nDo you want to update it with this information? (y/n): ");
								String persist = scan.nextLine();
								
								if(persist.equalsIgnoreCase("y")){						
									songBean = adminService.assocComposer(songBean,compBean,userId,true);
									System.out.println("\nSong record submitted with id: "+songBean.getId()+"\n");
									break;
								}
								else if(persist.equalsIgnoreCase("n")) {
									System.out.println("\nOperation Cancelled!\n");
									break;
								}
								else System.out.println("\nPlease enter valid input! (Y or N)\n");
							}
							break;
						}
						else {
							songBean.setId(Math.abs(getSongBean.getId()+100000));
							songBean = adminService.assocComposer(songBean,compBean,userId,true);
							System.out.println("\nSong record submitted with id: "+songBean.getId()+"\n");
							break;
						}
					}
					else {
						System.out.println("\nSong record submitted with id: "+songBean.getId()+"\n");
						break;
					}
				}
				else System.out.println("Composer not present. Please try others");
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

	private static void artSongAssoc(int userId) {
		while(true) {
			clearScreen();
			System.out.println("ARTIST SONG ASSOCIATION\n");
			
			String songName, artName;
			LocalTime duration;
			
			while(true){
				System.out.print("Song name: ");
				songName = scan.nextLine();
				boolean isValid = valService.validateName(songName);
				if(isValid) break;
				else System.out.println("\nPlease enter valid name! (MinChar:3, MaxChar:50)\n");
			}
			
			while(true){
				System.out.print("Duration: ");
				duration = valService.validateDuration(scan.nextLine());
				if(duration!=null) break;
				else System.out.println("\nPlease enter valid Duration! (mm:ss)\n");
			}
			songBean.setName(songName);
			songBean.setDuration(duration);
			songBean.setCreatedBy(userId);
			songBean.setUpdatedBy(userId);
			
			while(true){
				
				while(true){
					System.out.print("Enter Artist Name you want to associate with: ");
					artName = scan.nextLine();
					boolean isValid = valService.validateName(artName);
					if(isValid) break;
					else System.out.println("\nPlease enter valid name! (MinChar:3, MaxChar:50)\n");
				}
				
				artBean = adminService.searchArtist(artName);
				
				if(artBean != null) {
					SongBean getSongBean = adminService.assocArtist(songBean, artBean, userId, false);
					if(getSongBean.getId()<0) {
						if(getSongBean.getId()>-400000) {
							songBean.setId(Math.abs(getSongBean.getId()));
							while(true){
								System.out.print("\nThis song already present.\nDo you want to update it with this information? (y/n): ");
								String persist = scan.nextLine();
								
								if(persist.equalsIgnoreCase("y")){		
									songBean.setCreatedOn(getSongBean.getCreatedOn());
									songBean = adminService.assocArtist(songBean,artBean,userId,true);
									System.out.println("\nSong record submitted with id: "+songBean.getId()+"\n");
									break;
								}
								else if(persist.equalsIgnoreCase("n")) {
									System.out.println("\nOperation Cancelled!\n");
									break;
								}
								else System.out.println("\nPlease enter valid input! (Y or N)\n");
							}
							break;
						}
						else {
							songBean.setId(Math.abs(getSongBean.getId()+100000));
							songBean.setCreatedOn(LocalDate.now());
							songBean = adminService.assocArtist(songBean,artBean,userId,true);
							if(songBean.getId()==0) System.out.println("Something went wrong");
							else System.out.println("\nSong record submitted with id: "+songBean.getId()+"\n");
							break;
						}
					}
					else {
						System.out.println("\nSong record submitted with id: "+songBean.getId()+"\n");
						break;
					}
				}
				else System.out.println("Artist not present. Please try others");
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
			case 1: searchSong(userId); break;
			case 2: {attempt=3; return;}
			case 3: exit();
			default: System.out.println("\nPlease enter valid choice!\n");
			}
		}
	}
	
	private static void createAC(int userId, String password) throws AppException {
		String choice;
		
		clearScreen();
		System.out.println("CREATE ARTIST/COMPOSER\n");
		
		while(true) {
			System.out.println("1. Create Artist");
			System.out.println("2. Create Composer");
			System.out.println("3. Back");
			System.out.println("4. Exit");
			System.out.print("Enter choice: ");
			choice = scan.nextLine();
					
			if(choice.equals("1")) createArtist(userId);
			else if(choice.equals("2")) createComposer(userId);
			else if(choice.equals("3")) {clearScreen(); return;}
			else if(choice.equals("4")) exit();
			else System.out.println("\nPlease enter valid choice!\n");
		}
	
	}



	private static void editAC(int userId) {
		String choice;
		
		clearScreen();
		System.out.println("EDIT COMPOSER/ARTIST\n");
		
		while(true) {
			System.out.println("1. Edit Artist");
			System.out.println("2. Edit Composer");
			System.out.println("3. Back");
			System.out.println("4. Exit");
			System.out.print("Enter choice: ");
			choice = scan.nextLine();
		
			if(choice.equals("1")) editArtist(userId);
			else if(choice.equals("2")) editComposer(userId);
			else if(choice.equals("3")) {clearScreen(); return;}
			else if(choice.equals("4")) exit();
			else System.out.println("\nPlease enter valid choice!\n");
		}
	}
	
	////Edit composer details
	
	private static void editComposer(int userId) {
		while(true){
			clearScreen();
			String name, caeIpi, bornDate="", diedDate="";
			LocalDate bDate, dDate;
			char[] mSocietyId;
			while(true){
				System.out.println("\nEDIT COMPOSER");
				System.out.print("Enter Composer name: ");
				name = scan.nextLine();
				boolean isValid = valService.validateName(name);
				if(isValid) break;
				else System.out.println("\nPlease enter valid name! (MinChar:3, MaxChar:50)\n");
			}
			
			ComposerBean getBean = adminService.searchComposer(name);
			
			if(getBean != null){
				System.out.println("******Existing Information*******");
				
				if(getBean.getBornDate() == null) bornDate="NA";
				else bornDate = getBean.getBornDate().toString();
				if(getBean.getDiedDate() == null) diedDate="NA";
				else diedDate = getBean.getDiedDate().toString();
				
				System.out.println("Composer's Id: "+getBean.getId());
				System.out.println("Composer's Name: "+getBean.getName());
				System.out.println("Composer's Born Date: "+bornDate);
				System.out.println("Composer's Died Date: "+diedDate);
				System.out.println("Composer's CAE IPI Number: "+getBean.getCaeipiNumber());
				System.out.println("Composer's Music Society Id: "+new String(getBean.getMusicSocietyId()));
				System.out.println("Created By: "+getBean.getCreatedBy());
				System.out.println("Created On: "+getBean.getCreatedOn());
				System.out.println("Updated By: "+getBean.getUpdatedBy());
				System.out.println("Updated On: "+getBean.getUpdatedOn());
				
				System.out.println("Enter Details you want to update");
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
				
					compBean.setId(getBean.getId());
					compBean.setName(name);
					compBean.setBornDate(bDate);
					compBean.setDiedDate(dDate);
					compBean.setCaeipiNumber(caeIpi);
					compBean.setMusicSocietyId(mSocietyId);
					compBean.setUpdatedBy(userId);
				
					getBean = adminService.updateComposer(compBean);
					if(getBean!=null) System.out.println("Composer's information updated successfully.");
					else System.out.println("Problem while updating composer's information.");
			}
			else
				System.out.println("\nNo Record Found!\n");
			
			boolean isContinue;
			while(true) {
				System.out.print("Do you want to continue? (y/n): ");
				String choice = scan.nextLine();
			
				if(choice.equalsIgnoreCase("y")) {clearScreen(); isContinue=true; break;}
				else if(choice.equalsIgnoreCase("n")) {clearScreen(); isContinue=false; break;}
				else System.out.println("\nPlease enter valid choice!");
			}
			
			if(!isContinue) break;
		}
	}

	
	////edit artist details
	private static void editArtist(int userId) {
		while(true){
			clearScreen();
			String name, bornDate="", diedDate="";
			LocalDate bDate, dDate;
			while(true){
				System.out.println("\nEDIT ARTIST");
				System.out.print("Enter Artist name: ");
				name = scan.nextLine();
				boolean isValid = valService.validateName(name);
				if(isValid) break;
				else System.out.println("\nPlease enter valid name! (MinChar:3, MaxChar:50)\n");
			}
			
			ArtistBean getBean = adminService.searchArtist(name);
			
			if(getBean != null){
				System.out.println("******Existing Information*******");
				
				if(getBean.getBornDate() == null) bornDate="NA";
				else bornDate = getBean.getBornDate().toString();
				if(getBean.getDiedDate() == null) diedDate="NA";
				else diedDate = getBean.getDiedDate().toString();
				
				System.out.println("Artist's Id: "+getBean.getId());
				System.out.println("Artist Name: "+getBean.getName());
				System.out.println("Artist's Born Date: "+bornDate);
				System.out.println("Artist's Died Date: "+diedDate);
				System.out.println("Created By: "+getBean.getCreatedBy());
				System.out.println("Created On: "+getBean.getCreatedOn());
				System.out.println("Updated By: "+getBean.getUpdatedBy());
				System.out.println("Updated On: "+getBean.getUpdatedOn());
				
				System.out.println("Enter Details you want to update");
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
				
					artBean.setId(getBean.getId());
					artBean.setName(name);
					artBean.setBornDate(bDate);
					artBean.setDiedDate(dDate);
					artBean.setUpdatedBy(userId);
				
					getBean = adminService.updateArtist(artBean);
					if(getBean!=null) System.out.println("Artist's information updated successfully with id: "+getBean.getId());
					else System.out.println("\nProblem occured while updating artist's information...\n");
			}
			else
				System.out.println("\nNo Record Found!\n");
			
			boolean isContinue;
			while(true) {
				System.out.print("Do you want to continue? (y/n): ");
				String choice = scan.nextLine();
			
				if(choice.equalsIgnoreCase("y")) {clearScreen(); isContinue=true; break;}
				else if(choice.equalsIgnoreCase("n")) {clearScreen(); isContinue=false; break;}
				else System.out.println("\nPlease enter valid choice!");
			}
			
			if(!isContinue) break;
		}		
	}

	private static void searchAC() {
		clearScreen();
		System.out.println("SEARCH ARTIST/COMPOSER\n");
		
		String choice;
		while(true) {
			System.out.println("1. Search Artist");
			System.out.println("2. Search Composer");
			System.out.println("3. Back");
			System.out.println("4. Exit");
			System.out.print("Enter your choice: ");
			choice = scan.nextLine();
			
			if(choice.equals("1")) seachArtist();
			else if(choice.equals("2")) searchComposer();
			else if(choice.equals("3")) {clearScreen(); return;}
			else if(choice.equals("4")) exit();
			else System.out.println("\nPlease enter valid choice!\n");
		}
		
	}

	private static void searchComposer() {
		while(true){
			clearScreen();
			String name;
			while(true){
				System.out.println("\nSEARCH COMPOSER");
				System.out.print("Enter Composer name: ");
				name = scan.nextLine();
				boolean isValid = valService.validateName(name);
				if(isValid) break;
				else System.out.println("\nPlease enter valid name! (MinChar:3, MaxChar:50)\n");
			}
			
			ComposerBean getBean = adminService.searchComposer(name);
			String bornDate="", diedDate="";
			
			if(getBean != null){
				String mSocietyId = new String(getBean.getMusicSocietyId());
				
				if(getBean.getBornDate() == null) bornDate="NA";
				else bornDate = getBean.getBornDate().toString();
				if(getBean.getDiedDate() == null) diedDate="NA";
				else diedDate = getBean.getDiedDate().toString();
				
				System.out.println("Composer's Id: "+getBean.getId());
				System.out.println("Composer's Name: "+getBean.getName());
				System.out.println("Composer's Born Date: "+bornDate);
				System.out.println("Composer's Died Date: "+diedDate);
				System.out.println("Composer's CAE IPI Number: "+getBean.getCaeipiNumber());
				System.out.println("Composer's Music Society Id: "+mSocietyId);
				System.out.println("Created By: "+getBean.getCreatedBy());
				System.out.println("Created On: "+getBean.getCreatedOn());
				System.out.println("Updated By: "+getBean.getUpdatedBy());
				System.out.println("Updated On: "+getBean.getUpdatedOn());
			}
			else
				System.out.println("\nNo Record Found!\n");
			
			boolean isContinue;
			while(true) {
				System.out.print("Do you want to continue? (y/n): ");
				String choice = scan.nextLine();
			
				if(choice.equalsIgnoreCase("y")) {clearScreen(); isContinue=true; break;}
				else if(choice.equalsIgnoreCase("n")) {clearScreen(); isContinue=false; break;}
				else System.out.println("\nPlease enter valid choice!");
			}
			
			if(!isContinue) break;
		}
	}

	private static void seachArtist() {
		while(true){
			clearScreen();
			String name;
			while(true){
				System.out.println("\nSEARCH ARTIST");
				System.out.print("Enter Artist name: ");
				name = scan.nextLine();
				boolean isValid = valService.validateName(name);
				if(isValid) break;
				else System.out.println("\nPlease enter valid name! (MinChar:3, MaxChar:50)\n");
			}
			
			ArtistBean getBean = adminService.searchArtist(name);
			String bornDate,diedDate;
			if(getBean != null){
				
				if(getBean.getBornDate() == null) bornDate="NA";
				else bornDate = getBean.getBornDate().toString();
				if(getBean.getDiedDate() == null) diedDate="NA";
				else diedDate = getBean.getDiedDate().toString();
				
				System.out.println("Artist's Id: "+getBean.getId());
				System.out.println("Artist Name: "+getBean.getName());
				System.out.println("Artist's Born Date: "+bornDate);
				System.out.println("Artist's Died Date: "+diedDate);
				System.out.println("Created By: "+getBean.getCreatedBy());
				System.out.println("Created On: "+getBean.getCreatedOn());
				System.out.println("Updated By: "+getBean.getUpdatedBy());
				System.out.println("Updated On: "+getBean.getUpdatedOn());
			}
			else
				System.out.println("\nNo Record Found!\n");
			
			boolean isContinue;
			while(true) {
				System.out.print("Do you want to continue? (y/n): ");
				String choice = scan.nextLine();
			
				if(choice.equalsIgnoreCase("y")) {clearScreen(); isContinue=true; break;}
				else if(choice.equalsIgnoreCase("n")) {clearScreen(); isContinue=false; break;}
				else System.out.println("\nPlease enter valid choice!");
			}
			
			if(!isContinue) break;
		}
	}

	private static void searchSong(int userId) {
		clearScreen();
		System.out.println("SEARCH ARTIST/COMPOSER\n");
		
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
			else if(choice.equals("3")) {clearScreen(); return;}
			else if(choice.equals("4")) exit();
			else System.out.println("\nPlease enter valid choice!\n");
		}
	}

	private static void createArtist(int userId) {
		
		String name;
		LocalDate bDate, dDate;
		
		while(true){
			clearScreen();
			System.out.println("CREATE ARTIST\n");
			
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
					if(dDate!=null){
						if(bDate.isAfter(dDate)){
							System.out.println("\nDied date should be after Born date");
							continue;
						}
						break;
					}
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
				}
				else {
					artBean.setId(Math.abs(getBean.getId()+100000));
					getBean = adminService.createArtist(artBean,true);
					System.out.println("\nArtist record submitted with id: "+getBean.getId()+"\n");
				}
			}
			else System.out.println("Something went wrong... please try again later.");
			
			boolean isContinue;
			while(true) {
				System.out.print("Do you want to continue? (y/n): ");
				String choice = scan.nextLine();
			
				if(choice.equalsIgnoreCase("y")) {clearScreen(); isContinue=true; break;}
				else if(choice.equalsIgnoreCase("n")) {clearScreen(); isContinue=false; break;}
				else System.out.println("\nPlease enter valid choice!");
			}
			
			if(!isContinue) break;
		}	
	}
	
	private static void createComposer(int userId) {
		
		String name,caeIpi;
		LocalDate bDate, dDate;
		char[] mSocietyId;
		String mSocietyName=null;
		
		while(true){
			clearScreen();
			System.out.println("CREATE COMPOSER\n");
			
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
					if(dDate!=null){
						if(bDate.isAfter(dDate)){
							System.out.println("\nDied date should be after Born date");
							continue;
						}
						break;
					}
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
			
			if(!adminService.checkMSociety(new String(mSocietyId))) {
				System.out.print("Enter music society name: ");
				mSocietyName = scan.nextLine();
			}
			
			compBean.setName(name);
			compBean.setBornDate(bDate);
			compBean.setDiedDate(dDate);
			compBean.setCaeipiNumber(caeIpi);
			compBean.setMusicSocietyId(mSocietyId);
			compBean.setUpdatedBy(userId);
			compBean.setCreatedBy(userId);
			
			int genId = adminService.createComposer(compBean,false,mSocietyName);
			
			if(genId>0) System.out.println("\nComposer record submitted with id: "+genId+"\n");
			else if(genId<0){
				if(genId>-300000) {
					compBean.setId(Math.abs(genId));
					while(true){
						System.out.print("\nThis Composer already present.\nDo you want to update it with this information? (y/n): ");
						String persist = scan.nextLine();
						
						if(persist.equalsIgnoreCase("y")){
							genId = adminService.createComposer(compBean,true,mSocietyName);
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
					genId = adminService.createComposer(compBean,true,mSocietyName);
					System.out.println("\nComposer record submitted with id: "+genId+"\n");
				}
			}
			else System.out.println("Something went wrong... please try again later.");
			
			boolean isContinue;
			while(true) {
				System.out.print("Do you want to continue? (y/n): ");
				String choice = scan.nextLine();
			
				if(choice.equalsIgnoreCase("y")) {clearScreen(); isContinue=true; break;}
				else if(choice.equalsIgnoreCase("n")) {clearScreen(); isContinue=false; break;}
				else System.out.println("\nPlease enter valid choice!");
			}
			
			if(!isContinue) break;
		}
	}
	
	private static void seachArtistSongs(int userId) {
		String name, adminFormat = "%-8s %-35s %-8s %-10s %-10s %-10s %-10s", userFormat = "%-6s %-30s %-8s";
		ArrayList<SongBean> songList;
		clearScreen();
		System.out.println("SEARCH ARTIST SONGS\n");
		while(true) {
			
			while(true){
				System.out.print("Enter name: ");
				name = scan.nextLine();
				boolean isValid = valService.validateName(name);
				if(isValid) break;
				else System.out.println("\nPlease enter valid name! (MinChar:3, MaxChar:50)\n");
			}
			
			if(userId==100000 || userId==100001) songList=adminService.searchArtistSong(name);
			else songList=userService.searchArtistSong(name);
			
			if(songList == null) System.out.println("\nArtist not found");
			else if(songList.isEmpty()) System.out.println("\nNo record found!\n");
			else{
				System.out.println("\nSongs associated to "+name);
				int i=1;
				if(userId==100000 || userId==100001)
					System.out.printf(adminFormat, "Song_Id", "Song_Name", "Duration", "Created_By", "Created_On", "Updated_By", "Updated_On\n");
				else
					System.out.printf(userFormat, "Sr.No.", "Song_Name", "Duration\n");
				for (SongBean songBean : songList) {
					if(userId==100000 || userId==100001){
						System.out.printf(adminFormat, songBean.getId(), songBean.getName(), songBean.getDuration().getMinute()+":"+songBean.getDuration().getSecond(), songBean.getCreatedBy(), songBean.getCreatedOn(), songBean.getUpdatedBy(), songBean.getUpdatedOn());
						System.out.println();
					}
					else {
						System.out.printf(userFormat, i+")", songBean.getName(), songBean.getDuration().getMinute()+":"+songBean.getDuration().getSecond());
						System.out.println();
						i++;
					}
				}
			}
			
			boolean isContinue;
			while(true) {
				System.out.print("Do you want to continue? (y/n): ");
				String choice = scan.nextLine();
			
				if(choice.equalsIgnoreCase("y")) {clearScreen(); isContinue=true; break;}
				else if(choice.equalsIgnoreCase("n")) {clearScreen(); isContinue=false; break;}
				else System.out.println("\nPlease enter valid choice!");
			}
			
			if(!isContinue) break;
		}
	}

	private static void searchComposerSongs(int userId) {
		String name, adminFormat = "%-8s %-35s %-8s %-10s %-10s %-10s %-10s", userFormat = "%-6s %-30s %-8s";
		ArrayList<SongBean> songList;
		
		while(true) {
			clearScreen();
			System.out.println("SEARCH COMPOSER SONGS\n");
			
			while(true){
				System.out.print("Enter Composer name: ");
				name = scan.nextLine();
				boolean isValid = valService.validateName(name);
				if(isValid) break;
				else System.out.println("\nPlease enter valid name! (MinChar:3, MaxChar:50)\n");
			}
			
			if(userId==100000 || userId==100001) songList=adminService.searchComposerSong(name);
			else songList=userService.searchComposerSong(name);
			
			if(songList == null) System.out.println("\nComposer not found");
			else if(songList.isEmpty()) System.out.println("\nNo record found!\n");
			else{
				System.out.println("\nSongs associated to "+name);
				int i=1;
				if(userId==100000 || userId==100001)
					System.out.printf(adminFormat, "Song_Id", "Song_Name", "Duration", "Created_By", "Created_On", "Updated_By", "Updated_On\n");
				else
					System.out.printf(userFormat, "Sr.No.", "Song_Name", "Duration\n");
				for (SongBean songBean : songList) {
					if(userId==100000 || userId==100001){
						System.out.printf(adminFormat, songBean.getId(), songBean.getName(), songBean.getDuration().getMinute()+":"+songBean.getDuration().getSecond(), songBean.getCreatedBy(), songBean.getCreatedOn(), songBean.getUpdatedBy(), songBean.getUpdatedOn());
						System.out.println();
					}
					else {
						System.out.printf(userFormat, i+")",songBean.getName(), songBean.getDuration().getMinute()+":"+songBean.getDuration().getSecond());
						System.out.println();
						i++;
					}
				}
			}
			
			boolean isContinue;
			while(true) {
				System.out.print("Do you want to continue? (y/n): ");
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
		System.out.println("\n<-PROGRAM TERMINATED->");
		System.exit(0);		
	}

}
