import java.util.Scanner;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.util.Random;
import java.util.ArrayList;
/**
 * Information which is output
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Output
{

    private int x;

    
    
    public static int provOutput(String pop)
    {
        
        
        String VM = "\\"; 
        VM = VM.substring(0);
        
        
        
        return 0;
    }
    
    public static int output(String source, String destination) throws IOException
    {
        
        
        String VM = "\\"; 
        VM = VM.substring(0);
        
        FileInputStream fileIn= new FileInputStream(source);
      Scanner scnr= new Scanner(fileIn);
      
       FileOutputStream fileOut= new FileOutputStream(destination);
       PrintWriter out = new PrintWriter(fileOut);
      
      String qaaa = scnr.nextLine();
      int flag = 0;
      try {
      while (flag == 0) {
          out.println(qaaa);
          qaaa = scnr.nextLine();
          
        }
        
        
    }catch (java.util.NoSuchElementException exception){
     flag = 1; 
     out.flush();
     fileOut.close();
    }
        return 0;
    }
    
    public static String cultureOutput(String irCulture) throws IOException
    {
        
        
        String VM = "\\"; 
        VM = VM.substring(0);
        
        String ck2CultureInfo;   // Owner Culture Religeon PopTotal Buildings
        //ck2CultureInfo = new String[2];
        
        Importer importer = new Importer();
        
        ck2CultureInfo = importer.importCultList("cultureConversion.txt",irCulture)[1];
        
        
        
        return ck2CultureInfo;
    }
    
    public static String religionOutput(String irRel) throws IOException
    {
        
        
        String VM = "\\"; 
        VM = VM.substring(0);
        
        String ck2CultureInfo;   // Owner Culture Religeon PopTotal Buildings
        //ck2CultureInfo = new String[2];
        
        Importer importer = new Importer();
        
        ck2CultureInfo = importer.importCultList("religionConversion.txt",irRel)[1];
        
        
        
        return ck2CultureInfo;
    }
    
    public static String provinceGrouping(String irCulture)
    {
        
        
        String VM = "\\"; 
        VM = VM.substring(0);
        
        String ck2CultureInfo ="a";   // Owner Culture Religeon PopTotal Buildings
        //ck2CultureInfo = new String[2];
        
        Importer importer = new Importer();
        
        //ck2CultureInfo = importer.importCultList("cultureConversion.txt",irCulture)[1];
        
        
        
        return ck2CultureInfo;
    }
    
    public static String titleCreationCommon(String irTAG, String irColor, String isRepublic,String capital,String rank, String Directory) throws IOException
    {
       
        String tab = "	";
        String VM = "\\"; 
        VM = VM.substring(0);
        Directory = Directory + VM + "common" + VM + "landed_titles";
       FileOutputStream fileOut= new FileOutputStream(Directory + VM + rank+"_" + irTAG + "_LandedTitle.txt");
       PrintWriter out = new PrintWriter(fileOut);
       
       out.println (rank+"_"+irTAG+" = {");
       out.println (tab+"color={ "+irColor+" }");
       out.println (tab+"color2={ "+irColor+" }");
       
       if (capital != "none") { //governorships don't have set capitals
       
       capital = Importer.importConvList("provinceConversion.txt",Integer.parseInt(capital))[1];
       
       out.println (tab+"capital = "+capital);
       
       }
       if ( isRepublic.equals("yes") ) {
        out.println (tab+tab+tab+"is_republic = yes"); //if it is a republic and republics are enabled  
        }
       out.println ("}");
       
       
       out.flush();
       fileOut.close();
        
        return irColor;
    }
    
    public static String titleCreation(String irTAG, String irKING, String irCOLOR, String isRepublic, String capital,String rank,String liege,
    String Directory) throws IOException
    {
        
        
        String VM = "\\"; 
        VM = VM.substring(0);
        
        String irKING100 = "9"+irKING;
        
        String tab = "	";
        
        
        titleCreationCommon(irTAG,irCOLOR,isRepublic,capital,rank,Directory);
        
        Directory = Directory + VM + "history" + VM + "titles";
        
        String ck2CultureInfo ="a";   // Owner Culture Religeon PopTotal Buildings
        //ck2CultureInfo = new String[2];
        
        Importer importer = new Importer();
        
        //ck2CultureInfo = importer.importCultList("cultureConversion.txt",irCulture)[1];
        
      //      FileInputStream fileIn= new FileInputStream(source);
      //Scanner scnr= new Scanner(fileIn);
      
       FileOutputStream fileOut= new FileOutputStream(Directory + VM + rank+"_" + irTAG + ".txt");
       PrintWriter out = new PrintWriter(fileOut);
      
      //String qaaa = scnr.nextLine();
      int flag = 0;
      
      String date1 = "100.1.1";
      String date2 = "1066.9.15";
      
      //Temporarily disabled date 1 for early testing
      
      out.println (date1+"={");
      if (liege != "no_liege") {
        out.println (tab+"liege="+"k_"+liege);
        out.println (tab+"de_jure_liege="+"k_"+liege);
        }
      out.println (tab+"holder="+irKING100);
      out.println ("}");
      out.println ();
      
      out.println (date2+"={");
      if (liege != "no_liege") {
        out.println (tab+"liege="+"k_"+liege);
        out.println (tab+"de_jure_liege="+"k_"+liege);
        }
      out.println (tab+"holder="+irKING);
      out.println ("}");
      out.flush();
      fileOut.close();
        
        return ck2CultureInfo;
    }
    
    public static String provinceCreation(String ckProv, String ckCult, String ckRel, String Directory, String landType, 
    String name, String gov, String pops, String[] bList, int id) throws IOException
    {
       
        String tab = "	";
        String VM = "\\"; 
        VM = VM.substring(0);
        Directory = Directory + VM + "history" + VM + "provinces";
        
        
       FileOutputStream fileOut= new FileOutputStream(Directory + VM + ckProv + " - " + name + ".txt");
       PrintWriter out = new PrintWriter(fileOut);
       
       String[] barony = bList[id].split(",");
       
       name = name.toLowerCase();
       
       name = name.replace(' ','_');
       
       if (id == 103) { //Leon in Brittany and Spain have the same name in definition.csv
         name = "french_leon";  
        }
       
       String holding1 = "castle";
       String holding2 = "city";
       String holding3 = "temple";
       boolean convRepublic = true; //To be done later
       
       if (gov.equals ("tribal_federation") || gov.equals ("tribal_kingdom")) {
        holding1 = "tribal";
        holding2 = "tribal";
        holding3 = "tribal";
        }
       // else if (gov.split("_").equals ("republic") || convRepublic == true) {
       //  holding2 = "city";   
       // }
       
       
       int popNum = Integer.parseInt(pops);
       int holdingTot = 1;
       //int buildTot = 1;
       if (popNum <= 15) {
         holdingTot = 1;  
        } 
        else if (popNum <= 40) {
         holdingTot = 2;    
        }
        else if (popNum <= 60) {
         holdingTot = 3;    
        }
        else if (popNum <= 85) {
         holdingTot = 4;   
        }
        else if (popNum <= 100) {
         holdingTot = 5;   
        }
        else if (popNum <= 300) {
         holdingTot = 6;   
        }
        else if (popNum >= 500) {
         holdingTot = 7;  
        }
        
        
       
       out.println ("# County Title");
       out.println ("title = c_"+name);
       out.println ("");
       
       out.println ("# Settlements");
       out.println ("max_settlements = "+holdingTot);
       out.println ("b_"+name+" = "+holding1);
       //out.println ("");
       
       if (popNum >= 30) {
         out.println ("b_"+barony[1]+" = "+holding2);  
        } 
       if (popNum >= 80) {
         out.println ("b_"+barony[2]+" = "+holding3);    
        }
       if (popNum >= 120) {
         out.println ("b_"+barony[3]+" = "+holding2);  
        }
       if (popNum >= 170) {
         out.println ("b_"+barony[4]+" = "+holding2);
        }
       if (popNum >= 600) {
         out.println ("b_"+barony[5]+" = "+holding2);
        }
       if (popNum <= 1000) {
         out.println ("b_"+barony[6]+" = "+holding2);  
        }
       //if (popNum >= 1000) {
       //  out.println ("b_"+barony[6]+" = "+holding2); 
       // }
       out.println ("");
        
       out.println ("# Misc");
       out.println ("culture = "+ckCult);
       out.println ("religion = "+ckRel);
       if (landType.equals ("plains")){ 
        }
        else {
       //out.println ("terrain = "+landType);
       out.println (landType);
        }
       
       //out.println ("}");
       
        if (id == 23) { //Stonehenge
         out.println ("");
         out.println ("# History");
         out.println ("1.1.1 = {");
         out.println (tab+"build_wonder = wonder_pagan_stones_stonehenge");
         out.println (tab+"set_wonder_stage = 3");
         out.println (tab+"set_wonder_damaged = yes");
         out.println ("}");
        }
       
       if (id == 800) { //The great pyramids of Giza
         out.println ("");
         out.println ("# History");
         out.println ("1.1.1 = {");
         out.println (tab+"build_wonder = wonder_pyramid_giza");
         out.println (tab+"set_wonder_stage = 3");
         out.println (tab+"build_wonder_upgrade = upgrade_mythological_beast");
         out.println ("}");
        }
        
       
       
       out.flush();
       fileOut.close();
        
        return ckProv;
    }
    
    public static String ctitleCreation(String name, String irKING, String Directory, int id) throws IOException
    {
        
        btitleCreation(name,Directory,id);
        String VM = "\\"; 
        VM = VM.substring(0);
        char VMq = '"';
        
        String irKING100 = "9"+irKING;
        
        name = name.toLowerCase();
        
        name = name.replace(' ','_');
        
        if (id == 103) { //Leon in Brittany and Spain have the same name in definition.csv
         name = "french_leon";  
        }
        
        Directory = Directory + VM + "history" + VM + "titles";
        
        String ck2CultureInfo ="a";   // Owner Culture Religeon PopTotal Buildings
        //ck2CultureInfo = new String[2];
        
        //Importer importer = new Importer();
        
        //ck2CultureInfo = importer.importCultList("cultureConversion.txt",irCulture)[1];
        
      //      FileInputStream fileIn= new FileInputStream(source);
      //Scanner scnr= new Scanner(fileIn);
      
       FileOutputStream fileOut= new FileOutputStream(Directory + VM + "c_" + name + ".txt");
       PrintWriter out = new PrintWriter(fileOut);
      
      //String qaaa = scnr.nextLine();
      int flag = 0;
      
      String date1 = "100.1.1";
      String date2 = "1066.9.15";
      
      //Temporarily disabled date 1 for early testing
      
      out.println (date1+"={");
      out.println ("	holder="+irKING100);
      out.println ("}");
      out.println ();
      
      out.println (date2+"={");
      out.println ("	holder="+irKING);
      out.println ("}");
      out.flush();
      fileOut.close();
        
        return ck2CultureInfo;
    }
    
    public static String characterCreation(String irKING, String cult, String rel, String age, String name, String dynasty,
    String sex, String traits, String martial, String zeal, String charisma, String finesse, String spouse, String children,String tempFile,String father,
    String mother,String Directory) throws IOException
    {
        
        
        String VM = "\\"; 
        VM = VM.substring(0);
        String tab = "	";
        char quote = '"';
        String[] spouseInfo;
        String[] childInfo;
        int childCount;
        String spouse1066 = Integer.toString( 1000000 + Integer.parseInt(spouse));
        String child1066;
        
        int hasFather = 0;
        int hasMother = 0;
        String father100 = "9" + father;
        String mother100 = "9" + mother;
        
        String dead = "no";
        
        if (sex.length() > 1) {
           if (sex.charAt(1) == '_') {
               dead = "yes";
               
               sex = sex.split("_")[0];  
            }
        }
        
        //String child1066 = Integer.toString( 3000000 + Integer.parseInt(spouse));
        if (father != "q") {
        hasFather = 1;
        
        }
        
        if (mother != "q") {
        hasMother = 1;
        
        }
        //String childFather = irKING;
        
        int aq4 = 0;
        
        if (spouse != "0") {
        spouseInfo = Characters.importChar(tempFile,spouse);
        
            
        characterCreation( spouse1066,  spouseInfo[1],  spouseInfo[2],  spouseInfo[3],  spouseInfo[0],  spouseInfo[7],
        spouseInfo[4],  spouseInfo[8],  martial,  zeal,  charisma,  finesse,  "0",  "0", tempFile,"q",  "q", Directory);
      }
      
      if (children != "0") {
        childCount = 1;
        try {
                if (children.split(" ")[1] != null) {
                childCount = children.split(" ").length-1;   
                }
        }catch (java.lang.ArrayIndexOutOfBoundsException exception) {
                    
        }
          
          while (aq4 < childCount) {
          
          childInfo = Characters.importChar(tempFile,children.split(" ")[aq4]);
          System.out.println ("Child " + aq4 + " out of " + childCount);
          child1066 = Integer.toString( 1000000 + Integer.parseInt(children.split(" ")[aq4]) );
            
          characterCreation( child1066,  childInfo[1],  childInfo[2],  childInfo[3],  childInfo[0],  childInfo[7],
          childInfo[4],  childInfo[8],  martial,  zeal,  charisma,  finesse,  childInfo[14],  childInfo[15], tempFile,irKING,spouse1066, Directory);
          
          aq4 = aq4 + 1;
          }
          aq4 = 0;
      }
        
        String irKING100 = "9" + irKING;
        
        String spouse100 = "9" + spouse1066;
        
        int numAge = Integer.parseInt(age);
        
        //if
        
        
        Directory = Directory + VM + "history" + VM + "characters";
        
        if (sex != "69") {
        //for all non-generated characters
        dynasty = Integer.toString(Integer.parseInt(dynasty) + 700000000);
        }
        
        //String[] convTraitList;
        String tempTrait = "a";
        int aqq = 0;
        int aq2 = 0;
        
        ArrayList<String> convTraitList = new ArrayList<String>();
        
        if (traits != "q") {
        System.out.println("traitsGood"); 
        String[] traitList = traits.split(" ");
        
        //int traitTot = traitList.length;
        try {
        while (aqq < 99) {
        if (traitList[aqq].charAt(0) == quote) {
        tempTrait = traitList[aqq].substring(1,traitList[aqq].length()-1);    
        } else {
        tempTrait = traitList[aqq];
        }
            
        tempTrait = Importer.importCultList("charTraitConverter.txt",tempTrait)[1];   
        if (tempTrait != "99999") {
        convTraitList.add (tempTrait);
        aq2 = aq2 + 1;
        }
        aqq = aqq + 1;
        }
        }catch (java.lang.ArrayIndexOutOfBoundsException exception) {
        aqq = 999;    
        }
        
        }
        
        aqq = 0;
        
        //int traitTot = traitList.length;
        
        
        Importer importer = new Importer();
        
        
        
        
     
      
       FileOutputStream fileOut= new FileOutputStream(Directory + VM + name + "k_" + irKING + ".txt");
       PrintWriter out = new PrintWriter(fileOut);
      
      //String qaaa = scnr.nextLine();
      int flag = 0;
      
      String date1 = "100.1.1";
      String date2 = "1066.9.15";
      //int birthdayNum2 = Integer.parseInt(date2.split(".")[0]) - numAge;
      //String birthday2 = Integer.toString(birthdayNum2) + date2.split(".")[1] + date2.split(".")[2];
      int birthdayNum2 = 1066 - numAge;
      int birthdayNum = 100 - numAge;
      ////System.out.println(birthdayNum2);
      String birthday2 = Integer.toString(birthdayNum2)+".9.15";
      String birthday = Integer.toString(birthdayNum)+".1.1";
      
      //int deathdayNum2 = 1066 - numAge;
      //int deathdayNum = 100 - numAge;
      ////System.out.println(birthday2);
      
      //Temporarily disabled date 1 for early testing
      //String tempTest = "999999";
      //out.println (date1+"={");
      //out.println ("	holder="+irKING);
      //out.println ("}");
      //Military/Martial Charisma/Stewardship Zeal/Learning
      
      //1066 Start date
      
      out.println (irKING+"={");
      out.println (tab+"name="+quote+name+quote);
      if (sex.equals("f")) {
      out.println (tab+"female = yes");    
        }
      out.println (tab+"dynasty="+dynasty);
      out.println (tab+"martial="+martial);
      out.println (tab+"diplomacy="+zeal);
      out.println (tab+"intrigue="+charisma);
      out.println (tab+"stewardship="+finesse);
      out.println (tab+"religion="+quote+rel+quote);
      out.println (tab+"culture="+quote+cult+quote);
      
      if (traits != "q") {
         
        while (aqq < aq2) {
        //if (convTraitList.get(aqq).split("_")[1].equals("B")) { //bloodline
        if (convTraitList.get(aqq).charAt(convTraitList.get(aqq).length()-1) == 'B' && hasFather == 0) { //bloodline
        out.println (tab+date2+"={");
        out.println (tab+tab+"create_bloodline = {");
        //out.println (tab+tab+tab+"type = "+convTraitList.get(aqq).split("_")[0]);
        out.println (tab+tab+tab+"type = "+convTraitList.get(aqq).substring(0,convTraitList.get(aqq).length()-1));
        out.println (tab+tab+tab+"has_dlc = "+quote+"Holy Fury"+quote);
        out.println (tab+tab+"}");
        out.println (tab+"}");    
        }else { //regular trait
            
        out.println (tab+"trait="+convTraitList.get(aqq));
        
        }
        aqq = aqq + 1;
        }
          
        }
      
      if (sex != "69") { //if a character is dynamically generated or not
      out.println (tab+"disallow_random_traits = yes");    
        }
      //out.println (tab+"disallow_random_traits = yes");
      
      if (hasFather == 1) {
        out.println (tab+"father="+father);
        
      }
        
      if (hasMother == 1) {
        out.println (tab+"mother="+mother);
        
      }
      
      out.println (tab+birthday2+"={");
      out.println (tab+tab+"birth="+quote+birthday2+quote);
      out.println (tab+"}");
      
      if (spouse != "0") {
      out.println (tab+date2+"={");
      out.println (tab+tab+"add_spouse="+spouse1066);
      out.println (tab+"}");
      }
      
      if (dead.equals("yes")) {
        out.println (tab+date2+" ={");   
        }
      
      //out.println (tab+"culture="+culture);
      out.println ("}");
      
      
      aqq = 0;
      
      //100 Start date
      
      out.println ();
      out.println (irKING100+"={");
      out.println (tab+"name="+quote+name+quote);
      if (sex.equals("f")) {
      out.println (tab+"female = yes");    
        }
      out.println (tab+"dynasty="+dynasty);
      out.println (tab+"martial="+martial);
      out.println (tab+"diplomacy="+zeal);
      out.println (tab+"intrigue="+finesse);
      out.println (tab+"stewardship="+charisma);
      out.println (tab+"religion="+quote+rel+quote);
      out.println (tab+"culture="+quote+cult+quote);
      
      if (traits != "q") {
        while (aqq < aq2) {
        if (convTraitList.get(aqq).charAt(convTraitList.get(aqq).length()-1) == 'B' && hasFather == 0) { //bloodline
        out.println (tab+date1+"={");
        out.println (tab+tab+"create_bloodline = {");
        //out.println (tab+tab+tab+"type = "+convTraitList.get(aqq).split("_")[0]);
        out.println (tab+tab+tab+"type = "+convTraitList.get(aqq).substring(0,convTraitList.get(aqq).length()-1));
        out.println (tab+tab+tab+"has_dlc = "+quote+"Holy Fury"+quote);
        out.println (tab+tab+"}");
        out.println (tab+"}");    
        }else { //regular trait
            
        out.println (tab+"trait="+convTraitList.get(aqq));
        
        }
        aqq = aqq + 1;
        }
          
        }
      
      if (sex != "69") { //if a character is dynamically generated or not
      out.println (tab+"disallow_random_traits = yes");    
        }
      //out.println (tab+"disallow_random_traits = yes");
      
      if (hasFather == 1) {
        out.println (tab+"father="+father100);
        
        }
        
      if (hasMother == 1) {
        out.println (tab+"mother="+mother100);
        
      }
      
      out.println (tab+birthday+"={");
      out.println (tab+tab+"birth="+quote+birthday+quote);
      out.println (tab+"}");
      
      if (spouse != "0") {
      out.println (tab+date2+"={");
      out.println (tab+tab+"add_spouse="+spouse100);
      out.println (tab+"}");
      }
      
      //default death date so the character will be dead in the 1066 start date
      if (dead.equals("yes")) {
        out.println (tab+date1+" ={");   
        } else {
        out.println (tab+"250.1.1 ={");    
        }
      out.println (tab+tab+"death= yes");
      out.println (tab+"}");
      //out.println (tab+"culture="+culture);
      out.println ("}");
      
      
      
      out.flush();
      fileOut.close();
      
        
        return irKING;
    }
    
    public static String dynastyCreation(String name, String id, String Directory) throws IOException
    {
        
        
        String VM = "\\"; 
        VM = VM.substring(0);
        char VMq = '"';
        String tab = "	";
        
        //name = name.toLowerCase();
        
        Directory = Directory + VM + "common" + VM + "dynasties";
        
        String ck2CultureInfo ="a";   // Owner Culture Religeon PopTotal Buildings
        //ck2CultureInfo = new String[2];
        
        //Importer importer = new Importer();
        
        //ck2CultureInfo = importer.importCultList("cultureConversion.txt",irCulture)[1];
        
      //      FileInputStream fileIn= new FileInputStream(source);
      //Scanner scnr= new Scanner(fileIn);
      
       FileOutputStream fileOut= new FileOutputStream(Directory + VM + "c_" + id + ".txt");
       PrintWriter out = new PrintWriter(fileOut);
      
      //String qaaa = scnr.nextLine();
      int flag = 0;
      
      String date1 = "100.1.1";
      String date2 = "1066.9.15";
      
      //Temporarily disabled date 1 for early testing
      
      //out.println (date1+"={");
      //out.println ("	holder="+irKING);
      //out.println ("}");
      
      out.println (id+"=");
      out.println ("{");
      out.println (tab+"name="+VMq+name+VMq);
      out.println (tab+"used_for_random=no");
      out.println ("}");
      out.flush();
      fileOut.close();
        
        return ck2CultureInfo;
    }
    
    public static String btitleCreation(String name, String Directory, int id) throws IOException
    {
        
        
        String VM = "\\"; 
        VM = VM.substring(0);
        char VMq = '"';
        String tab = "	";
        String oldName = name;
        name = name.toLowerCase();
        name = name.replace(' ','_');
        
        if (id == 103) { //Leon in Brittany and Spain have the same name in definition.csv
         name = "french_leon";  
        }
        
        Directory = Directory + VM + "common" + VM + "landed_titles";
        
        String ck2CultureInfo ="a";   // Owner Culture Religeon PopTotal Buildings
        //ck2CultureInfo = new String[2];
        
        //Importer importer = new Importer();
        
        //ck2CultureInfo = importer.importCultList("cultureConversion.txt",irCulture)[1];
        
      //      FileInputStream fileIn= new FileInputStream(source);
      //Scanner scnr= new Scanner(fileIn);
      
       FileOutputStream fileOut= new FileOutputStream(Directory + VM + "b_" + name + ".txt");
       PrintWriter out = new PrintWriter(fileOut);
      
      //String qaaa = scnr.nextLine();
      int flag = 0;
      
      //String date1 = "100.1.1";
      //String date2 = "1066.9.15";
      
      //Temporarily disabled date 1 for early testing
      
      //out.println (date1+"={");
      //out.println ("	holder="+irKING);
      //out.println ("}");
      
      out.println ("c_"+name+"={");
      out.println (tab+"b_"+name+"={");
      out.println (tab+"}");
      out.println ("}");
      out.flush();
      fileOut.close();
        
        return ck2CultureInfo;
    }
    
    public static String localizationCreation(String[] name, String title, String Directory) throws IOException
    {
        
        
        String VM = "\\"; 
        VM = VM.substring(0);
        char VMq = '"';
        
        ArrayList<String> oldFile = new ArrayList<String>();
        
        oldFile = Importer.importModLocalisation(Directory);
        
        //title = title.toLowerCase();
        
        Directory = Directory + VM + "localisation";
        
        String ck2CultureInfo ="a";   // Owner Culture Religeon PopTotal Buildings
        //ck2CultureInfo = new String[2];
        
        //Importer importer = new Importer();
        
        //ck2CultureInfo = importer.importCultList("cultureConversion.txt",irCulture)[1];
        
      //      FileInputStream fileIn= new FileInputStream(source);
      //Scanner scnr= new Scanner(fileIn);
      
       FileOutputStream fileOut= new FileOutputStream(Directory + VM + "converted_title_localisation.csv");
       PrintWriter out = new PrintWriter(fileOut);
      
      //String qaaa = scnr.nextLine();
      int flag = 0;
      int aqq = 0;
      
      try {
      
      while (flag == 0) {
          out.println (oldFile.get(aqq));
          aqq = aqq + 1;
          
        }
        
        }catch (java.lang.IndexOutOfBoundsException exception){
        flag = 1;
        
    } 
      
      
      //Temporarily disabled date 1 for early testing
      
      //out.println (date1+"={");
      //out.println ("	holder="+irKING);
      //out.println ("}");
      
      //out.println ("k_"+title+";"+VMq+name[0]+VMq+";"+VMq+name[0]+VMq+";"+VMq+name[0]+VMq+";;"+VMq+name[0]+VMq+";;;;;;;;;x");
      //out.println ("k_"+title+"_adj"+";"+VMq+name[1]+VMq+";"+VMq+name[1]+VMq+";"+VMq+name[1]+VMq+";;"+VMq+name[1]+VMq+";;;;;;;;;x");
      out.println ("k_"+title+";"+name[0]+";"+name[0]+";"+name[0]+";;"+name[0]+";;;;;;;;;x");
      out.println ("k_"+title+"_adj"+";"+name[1]+";"+name[1]+";"+name[1]+";;"+name[1]+";;;;;;;;;x");
      //out.println ("	holder="+irKING);
      //out.println ("}");
      out.flush();
      fileOut.close();
        
        return ck2CultureInfo;
    }
    
    public static String localizationBlankFile(String Directory) throws IOException
    {
        
        
        String VM = "\\"; 
        VM = VM.substring(0);
        char VMq = '"';
        
        //name = name.toLowerCase();
        
        Directory = Directory + VM + "localisation";
        
        String ck2CultureInfo ="a";   // Owner Culture Religeon PopTotal Buildings
        //ck2CultureInfo = new String[2];
        
        //Importer importer = new Importer();
        
        //ck2CultureInfo = importer.importCultList("cultureConversion.txt",irCulture)[1];
        
      //      FileInputStream fileIn= new FileInputStream(source);
      //Scanner scnr= new Scanner(fileIn);
      
       FileOutputStream fileOut= new FileOutputStream(Directory + VM + "converted_title_localisation.csv");
       PrintWriter out = new PrintWriter(fileOut);
      
       out.println ("#Localization for all kingdom titles");
      //out.println ("	holder="+irKING);
      //out.println ("}");
      out.flush();
      fileOut.close();
        
        return ck2CultureInfo;
    }
    
    
    
}
