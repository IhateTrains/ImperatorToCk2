package ImperatorToCK2;  

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

        Importer importer = new Importer();

        ck2CultureInfo = importer.importCultList("cultureConversion.txt",irCulture)[1];

        return ck2CultureInfo;
    }

    public static String religionOutput(String irRel) throws IOException
    {

        String VM = "\\"; 
        VM = VM.substring(0);

        String ck2CultureInfo;   // Owner Culture Religeon PopTotal Buildings

        Importer importer = new Importer();

        ck2CultureInfo = importer.importCultList("religionConversion.txt",irRel)[1];

        return ck2CultureInfo;
    }

    public static String titleCreationCommon(String irTAG, String irColor, String government,String capital,String rank, String Directory) throws IOException
    {

        String tab = "	";
        String VM = "\\"; 
        VM = VM.substring(0);
        Directory = Directory + VM + "common" + VM + "landed_titles";
        FileOutputStream fileOut= new FileOutputStream(Directory + VM + rank+"_" + irTAG + "_LandedTitle.txt");
        PrintWriter out = new PrintWriter(fileOut);

        out.println (rank+"_"+irTAG+" = {");
        if (!irColor.equals("none")) {
            out.println (tab+"color={ "+irColor+" }");
            out.println (tab+"color2={ "+irColor+" }");
        }

        if (!capital.equals("none")) { //governorships don't have set capitals

            capital = Importer.importConvList("provinceConversion.txt",Integer.parseInt(capital))[1];

            out.println (tab+"capital = "+capital);

        }
        if ( government.equals("republic") ) {
            out.println (tab+tab+tab+"is_republic = yes"); //if it is a republic and republics are enabled  
        } else if (government.equals("imperium") && rank.equals("e")) {
            out.println (tab+"purple_born_heirs = yes"); //if government is imperial, enable born in purple mechanic
            out.println (tab+"has_top_de_jure_capital = yes");
        }
        out.println ("}");

        out.flush();
        fileOut.close();

        return irColor;
    }

    public static ArrayList<String> titleCreation(String irTAG, String irKING, String irCOLOR, String government, String capital,String rank,String liege,
    String date1,String republicOption,String irDynasty,ArrayList<String> dynList,ArrayList<String[]> impCharInfoList,ArrayList<String> convertedCharacters,
    int tagIDNum,String liegeGov,String Directory) throws IOException
    {

        String VM = "\\"; 
        VM = VM.substring(0);

        String irKING100 = "9"+irKING;

        String tab = "	";

        String oldDynasty = irDynasty;

        if (!government.equals("palace")) { //if palace, don't recalculate dynasty and recreate title
            titleCreationCommon(irTAG,irCOLOR,government,capital,rank,Directory);
            irDynasty = Processing.calcDynID(irDynasty);
        }
        String oldDirectory = Directory;
        Directory = Directory + VM + "history" + VM + "titles";
        String ck2CultureInfo ="a";   // Owner Culture Religeon PopTotal Buildings
        Importer importer = new Importer();

        FileOutputStream fileOut= new FileOutputStream(Directory + VM + rank+"_" + irTAG + ".txt");
        PrintWriter out = new PrintWriter(fileOut);

        int flag = 0;

        //String date1 = "100.1.1";
        String date2 = "1066.9.15";
        out.println (date1+"={");

        String overlordRank = "k";
        if (rank.equals("k")){
            overlordRank = "e";
        } else if (rank.equals("b") && government.equals("palace")) {
            overlordRank = liege.split(",")[0];
            liege = liege.split(",")[1];
            out.println (tab+"holding_dynasty = "+irDynasty);
        }

        if (!liege.equals("no_liege")) { //If country is a subject
            out.println (tab+"liege="+overlordRank+"_"+liege);
            if (!rank.equals("b") && !government.equals("palace")) {
                out.println (tab+"de_jure_liege="+overlordRank+"_"+liege);
            }
            if (liegeGov.equals("republic") && republicOption.equals("repMer")) { //If subject, don't convert as merchant republic
                republicOption = "repFeu";
            }
        }
        out.println("\tholder="+irKING100);
        if (government.equals("imperium") && rank.equals("e")) { //If I:R government is imperial, set government to CK II imperial (roman_imperial_government)
            out.println (tab+"law = crown_authority_2");
            out.println (tab+"law = succ_byzantine_elective");
            out.println (tab+"law = centralization_3");
            out.println (tab+"law = imperial_administration");
            out.println (tab+"law = ze_administration_laws_2");
            out.println (tab+"law = vice_royalty_2");
            out.println (tab+"law = revoke_title_law_1");
            govCreation(irTAG,rank,"i",oldDirectory);
            imperialSuccession(irTAG,rank,oldDirectory);
        }
        if (government.equals("republic") && republicOption.equals("repMer")) { 
            //If I:R government is republic and option is enabled, set to CK2 merchant republic (regardless of coastline requirements)

            String palace = irDynasty+"_"+irTAG;
            titleCreationCommon(palace,"none","none","none","b",oldDirectory); //creates merchant palace for ruler's family
            convertedCharacters = titleCreation(palace,irKING,irCOLOR,"palace",capital,"b",rank+","+irTAG,date1,republicOption,irDynasty,
                dynList,impCharInfoList,convertedCharacters,tagIDNum,liegeGov,oldDirectory);
            convertedCharacters = createFamilies(dynList,irTAG,oldDynasty,rank,impCharInfoList,convertedCharacters,date1,republicOption,tagIDNum,
                liegeGov,oldDirectory);

            govCreation(irTAG,rank,"m",oldDirectory);
        }
        out.println ("}");
        out.println ();
        out.flush();
        fileOut.close();
        //System.out.println(irTAG+"_"+government+"__"+republicOption+"__"+liege+"_"+liegeGov);

        return convertedCharacters;
    }

    public static String provinceCreation(String ckProv, String ckCult, String ckRel, String Directory, String landType, 
    String name, String gov, String pops, String[] bList, String saveMonuments, String republicOption, int id) throws IOException
    {

        String tab = "	";
        String VM = "\\"; 
        VM = VM.substring(0);
        String bracket1 = "{{"; 
        bracket1 = bracket1.substring(0);
        Directory = Directory + VM + "history" + VM + "provinces";

        FileOutputStream fileOut= new FileOutputStream(Directory + VM + ckProv + " - " + name + ".txt");
        PrintWriter out = new PrintWriter(fileOut);

        String[] barony = bList[id].split(",");

        name = name.toLowerCase();

        name = name.replace(' ','_');

        if (id == 103) { //Leon in Brittany and Spain have the same name in definition.csv
            name = "french_leon";  
        } else if (id == 1955) {
            name = "hy_many";  
        } else if (id == 1966) {
            name = "aurillac";  
        } else if (id == 1781) {
            name = "alqusair";  
        } else if (id == 722) {
            name = "al_aqabah";  
        } else if (id == 1379) {
            name = "asayita";  
        } else if (id == 1234) {
            name = "damin_i_koh";  
        } else if (id == 254) {
            name = "wurzburg";  
        } else if (id == 446) {
            name = "znojmo";  
        } else if (id == 715) {
            name = "zanjan_abhar";  
        } else if (id == 242) {
            name = "aargau";  
        } else if (id == 355) {
            name = "padova";  
        } else if (id == 1949) {
            name = "anglesey";  
        } else if (id == 935) {
            //name = "amalfi";  
        }

        String holding1 = "castle";
        String holding2 = "city";
        String holding3 = "temple";
        boolean convRepublic = false;
        if (republicOption.equals("repMer") || republicOption.equals("repRep")) {
            convRepublic = true;
        }

        if (gov.equals ("tribal")) {
            holding1 = "tribal";
            holding2 = "tribal";
            holding3 = "tribal";
        }
        else if (gov.equals ("republic") && convRepublic == true) { //If republic, primary holding becomes city instead of castle
            holding1 = "city";   
            holding2 = "castle";   
        }

        //System.out.println(gov+","+convRepublic);

        int popNum = Integer.parseInt(pops);
        int holdingTot = 1;

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
        out.println ("b_"+barony[0]+" = "+holding1);

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
        out.println ("");

        out.println ("# Misc");
        out.println ("culture = "+ckCult);
        out.println ("religion = "+ckRel);
        if (landType.equals ("plains")){ 
        }
        else {
            out.println (landType);
        }

        if (id == 23) { //Stonehenge in pre-2.0 saves
            if (Processing.checkMonumentList(saveMonuments) == 0 || 1 == 1) {//if 0, it is an old save before dynamic/custom monuments
                out.println ("");
                out.println ("# History");
                out.println ("1.1.1 = {");
                out.println (tab+"build_wonder = wonder_pagan_stones_stonehenge");
                out.println (tab+"set_wonder_stage = 3");
                out.println (tab+"set_wonder_damaged = yes");
                out.println ("}");
            }
        }

        if (id == 800) { //The great pyramids of Giza in pre-2.0 saves
            if (Processing.checkMonumentList(saveMonuments) == 0 || 1 == 1) {//if 0, it is an old save before dynamic/custom monuments
                out.println ("");
                out.println ("# History");
                out.println ("1.1.1 = {");
                out.println (tab+"build_wonder = wonder_pyramid_giza");
                out.println (tab+"set_wonder_stage = 3");
                out.println (tab+"build_wonder_upgrade = upgrade_mythological_beast");
                out.println ("}");
            }
        }

        out.flush();
        fileOut.close();

        return ckProv;
    }

    public static String ctitleCreation(String name, String irKING, String Directory, int id,String date1) throws IOException
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
        }  else if (id == 1955) {
            name = "hy_many";  
        } else if (id == 1966) {
            name = "aurillac";  
        } else if (id == 1781) {
            name = "alqusair";  
        } else if (id == 722) {
            name = "al_aqabah";  
        } else if (id == 1379) {
            name = "asayita";  
        } else if (id == 1234) {
            name = "damin_i_koh";  
        } else if (id == 254) {
            name = "wurzburg";  
        } else if (id == 446) {
            name = "znojmo";  
        } else if (id == 715) {
            name = "zanjan_abhar";  
        } else if (id == 242) {
            name = "aargau";  
        } else if (id == 355) {
            name = "padova";  
        } else if (id == 1949) {
            name = "anglesey";  
        } else if (id == 935) {
            //name = "amalfi";  
        }

        Directory = Directory + VM + "history" + VM + "titles";
        String ck2CultureInfo ="a";   // Owner Culture Religeon PopTotal Buildings
        FileOutputStream fileOut= new FileOutputStream(Directory + VM + "c_" + name + ".txt");
        PrintWriter out = new PrintWriter(fileOut);

        int flag = 0;

        //String date1 = "100.1.1";
        String date2 = "1066.9.15";

        out.println (date1+"={");
        out.println ("    holder="+irKING100);
        out.println ("}");
        out.println ();

        //No longer needed due to using one start date
        //out.println (date2+"={");
        //out.println ("    holder="+irKING);
        //out.println ("}");
        out.flush();
        fileOut.close();

        return ck2CultureInfo;
    }

    public static ArrayList<String> characterCreation(String irKING, String cult, String rel, String age, String name, String dynasty,
    String sex, String traits, String martial, String zeal, String charisma, String finesse, String spouse, String children,String government,String father,
    String mother,ArrayList<String> convertedList,ArrayList<String[]> charList,String date1,String Directory) throws IOException
    {

        int characterCount = 0;

        while (characterCount < convertedList.size()) { //checks if a character has been converted or not
            if (irKING.equals(convertedList.get(characterCount))) {
                return convertedList; //If a character has already been converted, no point to repeat and avoids children with jobs not having fathers
            } else {
                characterCount = characterCount + 1;    
            }

        }

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

        if (father != "q") {
            hasFather = 1;

        }

        if (mother != "q") {
            hasMother = 1;

        }

        int aq4 = 0;

        if (spouse != "0") {//Recursively calls to get rest of family
            int spouseID = Integer.parseInt(spouse);
            spouseInfo = charList.get(spouseID);

            characterCreation( spouse1066,  cultureOutput(spouseInfo[1]),  religionOutput(spouseInfo[2]),  spouseInfo[3],  spouseInfo[0],  spouseInfo[7],
                spouseInfo[4],  spouseInfo[8],  martial,  zeal,  charisma,  finesse,  "0",  "0", "no","q",  "q",convertedList,charList,date1, Directory);
        }

        if (children != "0") {
            childCount = 1;
            try {
                if (children.split(" ")[1] != null) {
                    childCount = children.split(" ").length-1;   
                }
            }catch (java.lang.ArrayIndexOutOfBoundsException exception) {

            }
            String isPurple = "no";
            if (government.equals("imperium") || government.equals("purple")) {
                isPurple = "purple";
            }

            while (aq4 < childCount) {//Recursively calls to get rest of family

                int childID = Integer.parseInt(children.split(" ")[aq4]);

                childInfo = charList.get(childID);
                //oldlogPrint ("Child " + aq4 + " out of " + childCount);
                child1066 = Integer.toString( 1000000 + Integer.parseInt(children.split(" ")[aq4]) );

                characterCreation( child1066,  cultureOutput(childInfo[1]),  religionOutput(childInfo[2]),  childInfo[3],  childInfo[0],  childInfo[7],
                    childInfo[4],  childInfo[8],  martial,  zeal,  charisma,  finesse,  childInfo[14],  childInfo[15], isPurple,irKING,spouse1066,
                    convertedList,charList,date1,Directory);

                aq4 = aq4 + 1;
            }
            aq4 = 0;
        }

        String irKING100 = "9" + irKING;

        String spouse100 = "9" + spouse1066;

        int numAge = Integer.parseInt(age);

        Directory = Directory + VM + "history" + VM + "characters";
        if (sex != "69") {
            //for all non-generated characters
            //dynasty = Integer.toString(Integer.parseInt(dynasty) + 700000000);
            dynasty = Processing.calcDynID(dynasty);
        }

        String tempTrait = "a";
        int aqq = 0;
        int aq2 = 0;

        ArrayList<String> convTraitList = new ArrayList<String>();

        if (traits != "q") {
            //oldlogPrint("traitsGood"); 
            String[] traitList = traits.split(" ");

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

        Importer importer = new Importer();

        //any filename with non-asci characters won't render in CK II, changed file output name to use GloriousCharacter instead of character name
        //in case player created interesting names in I:R

        FileOutputStream fileOut= new FileOutputStream(Directory + VM + "GloriousCharacter" + "_" + irKING + ".txt");
        PrintWriter out = new PrintWriter(fileOut);

        int flag = 0;

        //String date1 = "100.1.1";
        String tmpDate = date1.replace(".",","); //'.' character breaks .split function
        String monthDay = "." + tmpDate.split(",")[1] + "." + tmpDate.split(",")[2];
        monthDay = monthDay.replace(",",".");
        String date2 = "1066.9.15";
        int yearNum = Integer.parseInt(tmpDate.split(",")[0]);
        int birthdayNum2 = 1066 - numAge;
        int birthdayNum = yearNum - numAge;
        String birthday2 = Integer.toString(birthdayNum2)+".9.15";
        String birthday = Integer.toString(birthdayNum)+monthDay;

        //Military/Martial Charisma/Stewardship Zeal/Learning

        aqq = 0;
        //100 Start date
        out.println ();
        out.println (irKING100+"={");
        out.println (tab+"name="+quote+name+quote);
        if (sex.equals("f")) {
            out.println (tab+"female = yes");    
        }
        if (government.equals("purple")) {
            out.println (tab+"trait="+quote+"born_in_the_purple"+quote);    
        }
        out.println (tab+"dynasty="+dynasty);
        out.println (tab+"martial="+martial);
        out.println (tab+"diplomacy="+zeal);
        out.println (tab+"intrigue="+finesse);
        out.println (tab+"stewardship="+charisma);
        out.println (tab+"religion="+quote+rel+quote);
        out.println (tab+"culture="+quote+cult+quote);
        if (rel.equals("hindu") || rel.equals("buddhist") || rel.equals("jain")) { //Caste system defaults to merchant unless specified, sets to ruler
            out.println (tab+"trait=kshatriya");
        }

        if (traits != "q") {
            while (aqq < aq2) {
                if (convTraitList.get(aqq).charAt(convTraitList.get(aqq).length()-1) == 'B' && hasFather == 0) { //bloodline
                    out.println (tab+date1+"={");
                    out.println (tab+tab+"create_bloodline = {");
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
            out.println (tab+date1+"={");
            out.println (tab+tab+"add_spouse="+spouse100);
            out.println (tab+"}");
        }

        //default death date so the character will be dead in the 1066 start date
        if (dead.equals("yes")) {
            out.println (tab+date1+" ={");   
        } else {
            out.println (tab+(yearNum+250)+".1.1 ={");    
        }
        out.println (tab+tab+"death= yes");
        out.println (tab+"}");
        out.println ("}");

        out.flush();
        fileOut.close();

        convertedList.add(irKING);

        return convertedList;
    }

    public static String dynastyCreation(String name, String id, String backupName, String Directory) throws IOException
    {

        String VM = "\\"; 
        VM = VM.substring(0);
        char VMq = '"';
        String tab = "	";

        if (name.split(" ")[0].equals ("minor")) {
            name = backupName;
        }

        if (backupName.equals("debug")) { //gives all IR character dynasties + 700000000 to prevent conflict, generated ones (debug) use + 6000000
        } else {

            id = Processing.calcDynID(id);
        }

        Directory = Directory + VM + "common" + VM + "dynasties";
        String ck2CultureInfo ="a";   // debug output
        FileOutputStream fileOut= new FileOutputStream(Directory + VM + "c_" + id + ".txt");
        PrintWriter out = new PrintWriter(fileOut);

        int flag = 0;

        String date1 = "100.1.1";
        String date2 = "1066.9.15";

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
        } else if (id == 1955) {
            name = "hy_many";  
        } else if (id == 1966) {
            name = "aurillac";  
        } else if (id == 1781) {
            name = "alqusair";  
        } else if (id == 722) {
            name = "al_aqabah";  
        } else if (id == 1379) {
            name = "asayita";  
        } else if (id == 1234) {
            name = "damin_i_koh";  
        } else if (id == 254) {
            name = "wurzburg";  
        } else if (id == 446) {
            name = "znojmo";  
        } else if (id == 715) {
            name = "zanjan_abhar";  
        } else if (id == 242) {
            name = "aargau";  
        } else if (id == 355) {
            name = "padova";  
        } else if (id == 1949) {
            name = "anglesey";  
        } else if (id == 935) {
            //name = "amalfi";  
        }

        Directory = Directory + VM + "common" + VM + "landed_titles";

        FileOutputStream fileOut= new FileOutputStream(Directory + VM + "b_" + name + ".txt");
        PrintWriter out = new PrintWriter(fileOut);

        int flag = 0;

        out.println ("c_"+name+"={");
        out.println (tab+"b_"+name+"={");
        out.println (tab+"}");
        out.println ("}");
        out.flush();
        fileOut.close();

        return "a";
    }

    public static String localizationCreation(String[] name, String title, String rank, String Directory) throws IOException
    {

        String VM = "\\"; 
        VM = VM.substring(0);
        char VMq = '"';

        ArrayList<String> oldFile = new ArrayList<String>();

        oldFile = Importer.importModLocalisation(Directory);

        Directory = Directory + VM + "localisation";
        String ck2CultureInfo ="a";   // Owner Culture Religeon PopTotal Buildings

        FileOutputStream fileOut= new FileOutputStream(Directory + VM + "converted_title_localisation.csv");
        PrintWriter out = new PrintWriter(fileOut);

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

        out.println (rank+"_"+title+";"+name[0]+";"+name[0]+";"+name[0]+";;"+name[0]+";;;;;;;;;x");
        out.println (rank+"_"+title+"_adj"+";"+name[1]+";"+name[1]+";"+name[1]+";;"+name[1]+";;;;;;;;;x");
        out.flush();
        fileOut.close();

        return ck2CultureInfo;
    }

    public static String localizationBlankFile(String Directory) throws IOException
    {

        String VM = "\\"; 
        VM = VM.substring(0);
        char VMq = '"';

        Directory = Directory + VM + "localisation";
        String ck2CultureInfo ="a";   // blank default
        FileOutputStream fileOut= new FileOutputStream(Directory + VM + "converted_title_localisation.csv");
        PrintWriter out = new PrintWriter(fileOut);

        out.println ("#Localization for all kingdom titles");
        out.flush();
        fileOut.close();

        return ck2CultureInfo;
    }

    public static void logPrint(String name) throws IOException //outputs to log.txt
    {

        String logFile = "debugTest.txt";

        ArrayList<String> oldFile = new ArrayList<String>();
        oldFile = Importer.importBasicFile(logFile);

        FileOutputStream fileOut= new FileOutputStream(logFile);
        PrintWriter out = new PrintWriter(fileOut);

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

        out.println(name);
        out.flush();
        fileOut.close();

    }

    public static void logBlank() throws IOException //creates/replaces new log file
    {

        String logFile = "log.txt";

        FileOutputStream fileOut= new FileOutputStream(logFile);
        PrintWriter out = new PrintWriter(fileOut);

        out.println ("Log File for I:R to CK II converter");
        out.println ("If I:R to CK II crashes or has problems, send this log file and your I:R save game to the Paradox Converter team ASAP!");
        out.println ("");
        out.flush();
        fileOut.close();

    }

    public static void dejureTitleCreation(ArrayList<String[]> impTagInfo, int empireRank, int[] ck2LandTot, ArrayList<String> dejureDuchies,
    ArrayList<String> impSubjectInfo, String Directory) throws IOException
    {

        String tab = "	";
        String VM = "\\"; 
        VM = VM.substring(0);
        Directory = Directory + VM + "common" + VM + "landed_titles";
        FileOutputStream fileOut= new FileOutputStream(Directory + VM + "titlesDejure.txt"); //CK II's engine is picky with the file name
        PrintWriter out = new PrintWriter(fileOut);

        int aqq = 0;

        while (aqq < dejureDuchies.size()) {

            String duchy = dejureDuchies.get(aqq).split(",")[2];
            String tag = dejureDuchies.get(aqq).split(",")[0];
            String region = dejureDuchies.get(aqq).split(",")[3];
            String culture = dejureDuchies.get(aqq).split(",")[1];

            if (tag.equals("99999") || tag.equals("none") || tag.equals("null")) {

            } 

            else if (tag.equals("9999")) { //uncolonized province, creates dejure provinces based off of culture

                String[] cultureTitles = Processing.defaultDejureConversion(culture);
                cultureTitles = Processing.calculateUsedTitles(cultureTitles,impTagInfo,empireRank,ck2LandTot); //determines if tag exists in I:R

                out.println (cultureTitles[1]+" = {");
                out.println (tab+cultureTitles[2]+" = {");
                out.println (tab+tab+duchy+" = {");
                out.println (tab+tab+"}");
                out.println (tab+"}");
                out.println ("}");

            } else {

                int tagID = Integer.parseInt(tag);

                String rank = "k";

                if (ck2LandTot[tagID] >= empireRank || impTagInfo.get(tagID)[17].equals("imperium")) {
                    rank = "e";

                    int aq2 = 0;

                    int flag = 0;

                    out.println (rank+"_"+impTagInfo.get(tagID)[0]+" = {");

                    if (impTagInfo.get(tagID)[20] != "none") {
                        String[] governorships = impTagInfo.get(tagID)[20].split(",");
                        aq2 = 0;
                        while (aq2 < governorships.length) {
                            String govReg = governorships[aq2].split("~")[0];
                            if (region.equals(govReg)) {
                                tag = impTagInfo.get(tagID)[0]+"__"+govReg;
                                aq2 = aq2 + governorships.length;
                                flag = 1;

                            } else {
                                aq2 = aq2 + 1;    
                            }
                        }

                    }

                    if (flag == 0) {
                        out.println (tab+"k"+"_"+impTagInfo.get(tagID)[0]+" = {");
                    } else {
                        out.println (tab+"k"+"_"+tag+" = {");
                    }

                    out.println (tab+tab+duchy+" = {");
                    out.println (tab+tab+"}");
                    out.println (tab+"}");

                    out.println ("}");

                }
                else {
                    int subjectOrNot = Processing.checkSubjectList(tagID,impSubjectInfo);
                    int flag2 = 0;

                    if (subjectOrNot != 9999) { //if tag is not free
                        String[] subjectInfo = impSubjectInfo.get(subjectOrNot).split(",");
                        tagID = Integer.parseInt(subjectInfo[0]);

                        if (ck2LandTot[tagID] >= empireRank) { //if overlord is e tier, give subject dejure land with overlord as liege
                            out.println (tagID+" = {");
                            tagID = Integer.parseInt(tag);
                            flag2 = 1;
                        }
                    } else { //if tag is independent k tier, assign appropriate dejure culture empire liege
                        String[] cultureTitles = Processing.defaultDejureConversion(cultureOutput(impTagInfo.get(tagID)[6]));
                        cultureTitles = Processing.calculateUsedTitles(cultureTitles,impTagInfo,empireRank,ck2LandTot); //determines if tag exists in I:R
                        out.println (cultureTitles[1]+" = {");
                    }

                    out.println (tab+rank+"_"+impTagInfo.get(tagID)[0]+" = {");
                    out.println (tab+tab+duchy+" = {");
                    out.println (tab+tab+"}");
                    out.println (tab+"}");

                    if (subjectOrNot == 9999 || flag2 == 1) {
                        out.println ("}");
                    }
                }
            }

            aqq = aqq + 1;

        }

        out.flush();
        fileOut.close();

    }

    public static String govCreation(String title, String rank, String govFile, String Directory) throws IOException 
    //needed to allow TAGs imperial government and merchant republic government
    {

        String VM = "\\"; 
        VM = VM.substring(0);
        char VMq = '"';
        String tab = "	";

        if (govFile.equals("i")) { //for convienience
            govFile = "imperial_governments.txt";
        } else if (govFile.equals("m")) {
            govFile = "merchant_republic_governments.txt";
        }

        Directory = Directory + VM + "common" + VM + "governments" + VM + govFile;

        ArrayList<String> oldFile = new ArrayList<String>();

        oldFile = Importer.importBasicFile(Directory);

        String ck2CultureInfo ="a";   // Owner Culture Religeon PopTotal Buildings
        FileOutputStream fileOut= new FileOutputStream(Directory);
        PrintWriter out = new PrintWriter(fileOut);

        int aqq = 0;

        while (aqq < oldFile.size()) {
            out.println (oldFile.get(aqq));
            String key = oldFile.get(aqq).replace(tab,"");
            key = key.replace("#","");
            if (key.equals("title = e_TAG")) {
                String imperialTag = oldFile.get(aqq).replace("#","");
                imperialTag = imperialTag.replace("e_TAG",rank+"_"+title);
                out.println (imperialTag);
            }

            aqq = aqq + 1;

        }

        out.flush();
        fileOut.close();

        return ck2CultureInfo;
    }

    public static String imperialSuccession(String title, String rank, String Directory) throws IOException //needed to allow TAGs imperial laws
    {

        String VM = "\\"; 
        VM = VM.substring(0);
        char VMq = '"';
        String tab = "	";

        Directory = Directory + VM + "common" + VM + "laws" + VM + "succession_laws.txt";

        ArrayList<String> oldFile = new ArrayList<String>();

        oldFile = Importer.importBasicFile(Directory);

        String ck2CultureInfo ="a";   // Owner Culture Religeon PopTotal Buildings
        FileOutputStream fileOut= new FileOutputStream(Directory);
        PrintWriter out = new PrintWriter(fileOut);

        int aqq = 0;

        while (aqq < oldFile.size()) {
            out.println (oldFile.get(aqq));
            if (oldFile.get(aqq).contains("e_TAG")) {
                String imperialTag = oldFile.get(aqq).replace("e_TAG",rank+"_"+title);
                imperialTag = imperialTag.replace("#","");
                out.println (imperialTag);
            }

            aqq = aqq + 1;

        }

        out.flush();
        fileOut.close();

        return ck2CultureInfo;
    }

    public static void copyRaw(String dir1, String dir2) throws IOException
    {

        String VM = "\\";
        VM = VM.substring(0);

        FileInputStream fileIn= new FileInputStream(dir1);
        FileOutputStream fileOut= new FileOutputStream(dir2);

        boolean endOrNot = true;
        int aqq = 0;

        try {
            while (endOrNot = true && aqq != -1){
                if (aqq != -1) {
                    aqq = fileIn.read();
                    fileOut.write(aqq);
                }

            }
        }catch (java.util.NoSuchElementException exception){
            endOrNot = false;
            fileIn.close();
            fileOut.close();

        }   

    }

    public static void copyFlag(String ck2Dir, String modDirectory, String rank, String prov, String tag) throws IOException //copies flag files
    {

        String VM = "\\";
        VM = VM.substring(0);
        if (!tag.contains("dynamic") && !tag.contains("__")) { //if the tag is dynamically generated or is governorship, already uses CK II province ID
            prov = Importer.importConvList("provinceConversion.txt",Integer.parseInt(prov))[1];
        }

        prov = Processing.importNames("a",Integer.parseInt(prov),ck2Dir)[0];
        prov = Processing.formatProvName(prov);
        try {
            Output.copyRaw(ck2Dir+VM+"gfx"+VM+"flags"+VM+"c_"+prov+".tga",modDirectory+VM+"gfx"+VM+"flags"+VM+rank+"_"+tag+".tga");
        }catch (java.io.FileNotFoundException exception) { //if flag cannot be found, will use default one
            Output.copyRaw("defaultOutput"+VM+"gfx"+VM+"flags"+VM+"c_default.tga",modDirectory+VM+"gfx"+VM+"flags"+VM+rank+"_"+tag+".tga");
        }

    }

    //creates I:R flag
    public static int generateFlag(String ck2Dir, String irDirectory, String rank, ArrayList<String[]> flagList, String tag, String flagID,
    ArrayList<String[]> colorList, ArrayList<String> flagGFXList, String modDirectory) throws IOException
    {
        int flagCreated = 0; //if flag is successfully created, change to 1
        //output[2] format = hsvOrRgb,r g b
        //output[3] format = hsvOrRgb,r g b
        // output[4] format is texture~_~color1~_~color2~_~scale~_~position~_~rotation~~(nextEmblem)
        int aqq = 1;
        int flag = 0;
        while (aqq < flagList.size() && flag == 0) {
            if (flagList.get(aqq)[0].equals(flagID)) {
                flag = 1; //end loop
                String[] flagSource = flagList.get(aqq);
                String ck2Tag = rank+"_"+tag;
                String pattern = irDirectory + "/game/gfx/coat_of_arms/patterns/" + flagSource[1];
                String color1 = flagSource[2];
                String color2 = flagSource[3];
                String emblems = flagSource[4];

                int aq3 = 0;
                while (aq3 < flagGFXList.size()) {
                    if (flagGFXList.get(aq3).contains(flagSource[1])) {
                        pattern = flagGFXList.get(aq3);
                        aq3 = flagGFXList.size();
                    }
                    aq3 = aq3 + 1;
                }

                color1 = getColor(color1,colorList);
                color2 = getColor(color2,colorList);

                String devFlagName = "defaultOutput/flagDev/"+ck2Tag+"Dev.gif";
                String flagName = modDirectory+"/gfx/flags/"+ck2Tag+".tga";

                irFlagBackground(pattern,devFlagName,color1,color2);

                String[] emblemList = emblems.split("~~");
                int aq2 = 0;
                while (aq2 < emblemList.length) {
                    String[] emblem = emblemList[aq2].split("~_~");
                    String eTexture = irDirectory+"/game/gfx/coat_of_arms/colored_emblems/"+emblem[0];
                    String eColor1 = emblem[1];
                    String eColor2 = emblem[2];
                    String eScale = emblem[3];
                    String ePos = emblem[4];
                    String eRot= emblem[5];
                    String eNameOld = "defaultOutput/flagDev/emblem"+aq2+"Old"+".gif";
                    String eName = "defaultOutput/flagDev/emblem"+aq2+".gif";

                    int aq4 = 0;
                    while (aq4 < flagGFXList.size()) {
                        if (flagGFXList.get(aq4).contains(emblem[0])) {
                            eTexture = flagGFXList.get(aq4);
                            aq4 = flagGFXList.size();
                        }
                        aq4 = aq4 + 1;
                    }

                    eColor1 = getColor(eColor1,colorList);
                    if (!eColor2.equals("none")) {
                        eColor2 = getColor(eColor2,colorList);
                    }

                    irFlagEmblem(eTexture,eNameOld,eColor1,eColor2,eName,eScale,eRot,ePos,devFlagName,flagName);
                    flagCreated = 1; //Flag has been created
                    aq2 = aq2+1;
                }

            }
            aqq = aqq + 1;

        }
        return flagCreated;

    }

    public static ArrayList<String> createFamilies (ArrayList<String> dynList, String tag, String rulerFamily, String rank,
    ArrayList<String[]> impCharInfoList,ArrayList<String> convertedCharacters,String date,String republicOption, int tagIDNum, 
    String liegeGov,String directory) throws IOException
    //creates families for Merchant Republics
    {
        int aqq = 1;
        String VM = "\\"; 
        VM = VM.substring(0);
        String tagID = Integer.toString(tagIDNum);
        String families = Characters.getMajorFamilies(dynList,tagID);
        String[] familyList = families.split(",");
        String rulerFamilyOld = rulerFamily;
        rulerFamily = Processing.calcDynID(rulerFamily);
        String bDirectory = directory;
        while (aqq < familyList.length && aqq <= 5) {
            String[] dynasty = Characters.searchWholeDynasty(dynList,familyList[aqq]);
            String newDynasty = Processing.calcDynID(dynasty[1]);
            if (!newDynasty.equals(rulerFamilyOld)) { //if not of ruler's family
                String palace = newDynasty + "_" + tag;
                String palaceDir = bDirectory + VM + "b_" + palace + ".txt";
                if (Processing.checkFile(palaceDir) == false) { //if barony doesn't already exist
                    String head = Processing.calcHead(impCharInfoList,dynasty[4]);
                    String headNum = Processing.calcCharID(head);
                    String[] headCharacter = impCharInfoList.get(Integer.parseInt(head));

                    convertedCharacters = characterCreation(headNum, cultureOutput(headCharacter[1]),Output.religionOutput(headCharacter[2]),
                        headCharacter[3],headCharacter[0],headCharacter[7],headCharacter[4],headCharacter[8],headCharacter[10],headCharacter[11],
                        headCharacter[12],headCharacter[13],headCharacter[14],
                        headCharacter[15],"palace","q","q",convertedCharacters,impCharInfoList,date,directory);

                    dynastyCreation(dynasty[0],headCharacter[7],headCharacter[16],directory);

                    titleCreationCommon(palace,"none","none","none","b",directory); //creates merchant palace for ruler's family
                    convertedCharacters = titleCreation(palace,headNum,"none","palace","none","b",rank+","+tag,date,republicOption,newDynasty,dynList,
                        impCharInfoList,convertedCharacters,tagIDNum,liegeGov,directory);

                }
            }
            aqq = aqq + 1;

        }

        return convertedCharacters;
    }

    public static void irFlagEmblem(String eTexture,String eNameOld,String eColor1,String eColor2,String eName,String eScale,
    String eRot,String ePos,String devFlagName,String flagName) throws IOException //generates and applies emblem to flag
    {
        irFlagBackground(eTexture,eNameOld,eColor1,eColor2);
        irFlagScaleExact(eNameOld,eName,"256","256"); //set's size to 256 x 256

        if (!eScale.equals("none")) {
            irFlagScale(eName,eScale);
        }

        if (!eRot.equals("none")) {
            irFlagRotate(eName,eRot);
        }
        if (!ePos.equals("none")) {
            irFlagPos(eName,ePos);
        }

        irFlagCombine(devFlagName,eName,devFlagName);
        irFlagScaleExact(devFlagName,flagName,"128","128");
    }

    public static void irFlagBackground(String oldName, String name, String color, String color2) throws IOException
    {
        irFlagColor(name,oldName,color,1);
        if (!color2.equals("none") && !oldName.contains("pattern_solid.tga")) {
            String layer2Name = name.replace(".gif","layer2.gif");
            irFlagColor(layer2Name,oldName,"none",1);
            irFlagColor(layer2Name,layer2Name,color2,2);
            irFlagCombine(name,layer2Name,name);
        }
    }

    public static void irFlagColor(String name, String oldName, String color, int oneOrTwo) throws IOException
    {
        String replaceColor = "red";
        if (oneOrTwo == 2) {
            replaceColor = "yellow";
        }
        String[] rakalyCommand = new String [10];
        rakalyCommand[0] = "magick.exe";
        rakalyCommand[1] = "convert";
        rakalyCommand[2] = oldName;
        rakalyCommand[3] = "-fuzz";
        rakalyCommand[4] = "40%";
        rakalyCommand[5] = "-fill";
        rakalyCommand[6] = color;
        rakalyCommand[7] = "-opaque";
        rakalyCommand[8] = replaceColor;
        rakalyCommand[9] = name;
        Processing.fileExcecute(rakalyCommand);
    }

    public static String irFlagScale(String name, String percent) throws IOException
    {

        percent = percent.replace("  "," ");
        String[] numbers = percent.split(" ");
        double scaleNum1 = Double.parseDouble(numbers[0]) * 256;
        double scaleNum2 = Double.parseDouble(numbers[1]) * 256;
        if (scaleNum1 < 0) {
            scaleNum1 = scaleNum1 * -1;
            irFlagFlip(name,name,"x");
        }
        if (scaleNum2 < 0) {
            scaleNum2 = scaleNum2 * -1;
            irFlagFlip(name,name,"y");
        }

        String[] rakalyCommand = new String [8];
        rakalyCommand[0] = "magick.exe";
        rakalyCommand[1] = "convert";
        rakalyCommand[2] = name;
        rakalyCommand[3] = "-resize";
        rakalyCommand[4] = scaleNum1 + "x" + scaleNum2 + "!";
        rakalyCommand[5] = "-quality";
        rakalyCommand[6] = "92";
        rakalyCommand[7] = name;
        Processing.fileExcecute(rakalyCommand);
        irFlagCanvas(name,"256","256");
        return scaleNum1 + "x" + scaleNum2;
    }

    public static void irFlagScaleExact(String oldName,String newName, String dim1, String dim2) throws IOException //scales based on exact dimensions
    {
        String[] rakalyCommand = new String [8];
        rakalyCommand[0] = "magick.exe";
        rakalyCommand[1] = "convert";
        rakalyCommand[2] = oldName;
        rakalyCommand[3] = "-resize";
        rakalyCommand[4] = dim1 + "x" + dim2 + "!";
        rakalyCommand[5] = "-quality";
        rakalyCommand[6] = "92";
        rakalyCommand[7] = newName;
        Processing.fileExcecute(rakalyCommand);
    }

    public static void irFlagCanvas(String name,String dim1,String dim2) throws IOException //set's the canvas
    {
        String[] rakalyCommand = new String [8];
        rakalyCommand[0] = "magick.exe";
        rakalyCommand[1] = "convert";
        rakalyCommand[2] = name;
        rakalyCommand[3] = "-gravity";
        rakalyCommand[4] = "center";
        rakalyCommand[5] = "-extent";
        rakalyCommand[6] = dim1+"x"+dim2;
        rakalyCommand[7] = name;
        Processing.fileExcecute(rakalyCommand);

    }

    public static void irFlagRotate(String name, String degrees) throws IOException
    {
        String[] rakalyCommand = new String [11];
        rakalyCommand[0] = "magick.exe";
        rakalyCommand[1] = "convert";
        rakalyCommand[2] = name;
        rakalyCommand[3] = "-background";
        rakalyCommand[4] = "none";
        rakalyCommand[5] = "-virtual-pixel";
        rakalyCommand[6] = "background";
        rakalyCommand[7] = "-distort";
        rakalyCommand[8] = "ScaleRotateTranslate";
        rakalyCommand[9] = degrees;
        rakalyCommand[10] = name;
        Processing.fileExcecute(rakalyCommand);
    }

    public static String irFlagPos(String name, String position) throws IOException
    {
        String[] numbers = position.split(" ");
        if (numbers.length < 2) { //in case flag has broken formatting (OEO's 0.5340.5, for example)
            //numbers[0] = position.split(".")[0] + "." + position.split(".")[1];
            return "Malformed position data " + position;
        }
        int posNumX = (int)(Double.parseDouble(numbers[0]) * 256);
        int posNumY = (int)(Double.parseDouble(numbers[1]) * 256);
        String posXY = posNumX+","+posNumY;
        String test = "'128,128 "+posXY+"'";

        String[] rakalyCommand = new String [9];
        rakalyCommand[0] = "magick.exe";
        rakalyCommand[1] = "convert";
        rakalyCommand[2] = name;
        rakalyCommand[3] = "-virtual-pixel";
        rakalyCommand[4] = "none";
        rakalyCommand[5] = "-distort";
        rakalyCommand[6] = "Affine";
        rakalyCommand[7] = "128,128 "+posXY;
        rakalyCommand[8] = name;
        Processing.fileExcecute(rakalyCommand);

        return test;
    }

    public static void irFlagCombine(String background, String emblem, String product) throws IOException //combined test
    {
        String[] rakalyCommand = new String [7];
        rakalyCommand[0] = "magick.exe";
        rakalyCommand[1] = "composite";
        rakalyCommand[2] = "-gravity";
        rakalyCommand[3] = "center";
        rakalyCommand[4] = emblem;
        rakalyCommand[5] = background;
        rakalyCommand[6] = product;
        Processing.fileExcecute(rakalyCommand);
    }

    public static void irFlagFlip(String background, String product, String dim) throws IOException //combined test
    {
        String flipOrFlop = "-flop"; //flof for x, flip for y
        if (dim.equals("y")) {
            flipOrFlop = "-flip";
        }
        String[] rakalyCommand = new String [4];
        rakalyCommand[0] = "magick.exe";
        rakalyCommand[1] = background;
        rakalyCommand[2] = flipOrFlop;
        rakalyCommand[3] = product;
        Processing.fileExcecute(rakalyCommand);
    }

    public static String getColor(String colorName,ArrayList<String[]> colorList) throws IOException
    //get's and converts I:R color to correct format
    {

        int aqq = 0;
        int flag = 0;
        while (aqq < colorList.size() && flag == 0) {
            String color = colorName;
            if (colorList.get(aqq)[0].equals(colorName)) {
                flag = 1; //end loop
                color = colorList.get(aqq)[1];
                color = color.replace("  "," ");

            }
            if (color.split(",")[0].equals("rgb")) {
                color = color.split(",")[1];
                color = "rgb(" + color.replace(" ",",") + ")";
                return color;
            }
            if (color.split(",")[0].equals("hsv")) {
                color = color.split(",")[1];
                color = Processing.deriveRgbFromHsv(color);
                color = "rgb(" + color.replace(" ",",") + ")";
                return color;
            }
            aqq = aqq + 1;
        }
        return colorName;

    }

}
