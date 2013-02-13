package net.yura.domination.engine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.net.URL;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.prefs.Preferences;
import net.yura.domination.engine.core.Player;
import net.yura.domination.engine.core.RiskGame;
import net.yura.domination.engine.translation.MapTranslator;

public class RiskUtil {

	public static final String RISK_VERSION_URL;
	public static final String RISK_LOBBY_URL;
//	public static final String RISK_POST_URL; // look in Grasshopper.jar now
	public static final String GAME_NAME;
//	private static final String DEFAULT_MAP;

	public static RiskIO streamOpener;

	private final static Properties settings;
	static {

		settings = new Properties();

		try {
			settings.load(RiskUtil.class.getResourceAsStream("settings.ini"));
		}
		catch (Exception ex) {
			throw new RuntimeException("can not find settings.ini file!",ex);
		}

		RISK_VERSION_URL = settings.getProperty("VERSION_URL");
		RISK_LOBBY_URL = settings.getProperty("LOBBY_URL");
//		RISK_POST_URL = settings.getProperty("POST_URL");
		GAME_NAME = settings.getProperty("name");
		//DEFAULT_MAP = settings.getProperty("defaultmap");
		Risk.RISK_VERSION = settings.getProperty("version");

		String dmap = settings.getProperty("defaultmap");
		String dcards = settings.getProperty("defaultcards");

		RiskGame.setDefaultMapAndCards( dmap , dcards );

	}

	public static InputStream openMapStream(String a) throws IOException {
            return streamOpener.openMapStream(a);
	}

	public static InputStream openStream(String a) throws IOException {
            return streamOpener.openStream(a);
	}

	public static ResourceBundle getResourceBundle(Class c,String n,Locale l) {
            return streamOpener.getResourceBundle(c,n,l);
	}

	public static void openURL(URL url) throws Exception {
            streamOpener.openURL(url);
	}

	public static void openDocs(String docs) throws Exception {
            streamOpener.openDocs(docs);
	}
        public static void saveFile(String file, RiskGame aThis) throws Exception {
            streamOpener.saveGameFile(file, aThis);
        }
        public static InputStream getLoadFileInputStream(String file) throws Exception {
            return streamOpener.loadGameFile(file);
        }

        /**
         * option string looks like this:
         * 
         *   0
         *   2
         *   2
         *   choosemap luca.map
         *   startgame domination increasing
         */
        public static String createGameString(/** @Deprecated */ int crapAI,int normalAI,int goodAI,int gameMode,int cardsMode,boolean AutoPlaceAll,boolean recycle,String mapFile) {
            String players = 
            //human +"\n" +
            crapAI+"\n" + // deprecated
            normalAI +"\n" +
            goodAI +"\n";

            String type="";

            switch(gameMode) {
                case RiskGame.MODE_DOMINATION: type = "domination"; break;
                case RiskGame.MODE_CAPITAL: type = "capital"; break;
                case RiskGame.MODE_SECRET_MISSION: type = "mission"; break;
            }
            
            switch(cardsMode) {
                case RiskGame.CARD_INCREASING_SET: type += " increasing"; break;
                case RiskGame.CARD_FIXED_SET: type += " fixed"; break;
                case RiskGame.CARD_ITALIANLIKE_SET: type += " italianlike"; break;
            }

            if ( AutoPlaceAll ) type += " autoplaceall";
            if ( recycle ) type += " recycle";
            
            return players+ "choosemap "+mapFile +"\nstartgame " + type;
        }
        public static String getMapNameFromLobbyStartGameOption(String options) {
            String[] lines = options.split( RiskUtil.quote("\n") );
            String choosemap = lines[3];
            return choosemap.substring( "choosemap ".length() );
        }
        public static String getGameDescriptionFromLobbyStartGameOption(String options) {
            String[] lines = options.split( RiskUtil.quote("\n") );
            int ai=0;
            for (int c=0;c<3;c++) {
                ai = ai + Integer.parseInt(lines[c]);
            }
            return "AI:"+ai+" "+lines[4].substring( "startgame ".length() );
        }

        public static void printStackTrace(Throwable ex) {
            java.util.logging.Logger.getLogger(RiskUtil.class.getName()).log(java.util.logging.Level.WARNING, null, ex);
        }

        public static void donate() throws Exception {
		openURL(new URL("http://domination.sourceforge.net/donate.shtml"));
	}
        
	public static void donatePayPal() throws Exception {
		openURL(new URL("https://www.paypal.com/cgi-bin/webscr?cmd=_donations&business=yura%40yura%2enet&item_name="+GAME_NAME+"%20Donation&no_shipping=0&no_note=1&tax=0&currency_code=GBP&lc=GB&bn=PP%2dDonationsBF&charset=UTF%2d8"));
	}

        public static void loadPlayers(Risk risk,Class uiclass) {

            Preferences prefs=null;
            try {
                 prefs = Preferences.userNodeForPackage( uiclass );
            }
            catch(Throwable th) { } // security

            for (int cc=1;cc<=RiskGame.MAX_PLAYERS;cc++) {
                String nameKey = "default.player"+cc+".name";
                String colorKey = "default.player"+cc+".color";
                String typeKey = "default.player"+cc+".type";

                String name = risk.getRiskConfig(nameKey);
                String color = risk.getRiskConfig(colorKey);
                String type = risk.getRiskConfig(typeKey);

                if (prefs!=null) {
                    name = prefs.get(nameKey, name);
                    color = prefs.get(colorKey, color);
                    type = prefs.get(typeKey, type);
                }

                if (!"".equals(name)&&!"".equals(color)&&!"".equals(type)) {
                    risk.parser("newplayer " + type+" "+ color+" "+ name );
                }
            }

        }

        public static void savePlayers(Risk risk,Class uiclass) {

            Preferences prefs=null;
            try {
                 prefs = Preferences.userNodeForPackage( uiclass );
            }
            catch(Throwable th) { } // security

            if (prefs!=null) {

                Vector players = risk.getGame().getPlayers();

                for (int cc=1;cc<=RiskGame.MAX_PLAYERS;cc++) {
                    String nameKey = "default.player"+cc+".name";
                    String colorKey = "default.player"+cc+".color";
                    String typeKey = "default.player"+cc+".type";

                    Player player = (cc<=players.size())?(Player)players.elementAt(cc-1):null;

                    String name = "";
                    String color = "";
                    String type = "";

                    if (player!=null) {
                        name = player.getName();
                        color = ColorUtil.getStringForColor( player.getColor() );
                        type = Risk.getType( player.getType() );
                    }
                    prefs.put(nameKey, name);
                    prefs.put(colorKey, color);
                    prefs.put(typeKey, type);

                }

                // on android this does not work, god knows why
                // whats the point of including a class if its
                // most simple and basic operation does not work?
                try {
                    prefs.flush();
                }
                catch(Exception ex) {
                    RiskUtil.printStackTrace(ex);
                }

            }
        }

        public static BufferedReader readMap(InputStream in) throws IOException {

            PushbackInputStream pushback = new PushbackInputStream(in,3);

            int first = pushback.read();
            if (first == 0xEF) {
                int second = pushback.read();
                if (second == 0xBB) {
                    int third = pushback.read();
                    if (third == 0xBF) {
                        return new BufferedReader(new InputStreamReader( pushback, "UTF-8" ) );
                    }
                    pushback.unread(third);
                }
                pushback.unread(second);
            }
            pushback.unread(first);

            return new BufferedReader(new InputStreamReader( pushback, "ISO-8859-1" ) );
        }

        /**
         * gets the info for a map or cards file
         * in the case of map files it will get the "name" "crd" "prv" "pic" "map" and any "comment"
         * and for cards it will have a "missions" that will contain the String[] of all the missions
         */
	public static java.util.Map loadInfo(String fileName,boolean cards) {

            Hashtable info = new Hashtable();

            for (int c=0;true;c++) {

                BufferedReader bufferin=null;

                try {

                        bufferin= RiskUtil.readMap(RiskUtil.openMapStream(fileName));
                        Vector misss=null;

                        if (cards) {
                            MapTranslator.setCards( fileName );
                            misss = new Vector();
                        }

                        String input = bufferin.readLine();
                        String mode = null;

                        while(input != null) {

                                if (input.equals("")) {
                                        // do nothing
                                        //System.out.print("Nothing\n"); // testing
                                }
                                else if (input.charAt(0)==';') {
                                    String comment = (String)info.get("comment");
                                    String com = input.substring(1).trim();
                                    if (comment==null) {
                                        comment = com;
                                    }
                                    else {
                                        comment = comment +"\n"+com;
                                    }
                                    info.put("comment", comment);
                                }
                                else {

                                        if (input.charAt(0)=='[' && input.charAt( input.length()-1 )==']') {
                                                mode="newsection";
                                        }

                                        if ("files".equals(mode)) {

                                                int space = input.indexOf(' ');
                                            
                                                String fm = input.substring(0,space);
                                                String val = input.substring(space+1);

                                                info.put( fm , val);

                                        }
                                        else if ("continents".equals(mode)) {

                                                break;

                                        }
                                        else if ("missions".equals(mode)) {

                                                StringTokenizer st = new StringTokenizer(input);
                                            
                                                String description=MapTranslator.getTranslatedMissionName(st.nextToken()+"-"+st.nextToken()+"-"+st.nextToken()+"-"+st.nextToken()+"-"+st.nextToken()+"-"+st.nextToken());

                                                if (description==null) {

                                                        StringBuffer d = new StringBuffer();

                                                        while (st.hasMoreElements()) {

                                                                d.append( st.nextToken() );
                                                                d.append( " " );
                                                        }

                                                        description = d.toString();

                                                }

                                                misss.add( description );

                                        }
                                        else if ("newsection".equals(mode)) {

                                                mode = input.substring(1, input.length()-1); // set mode to the name of the section

                                        }
                                        else if (mode == null) {
                                            if (input.indexOf(' ')>0) {
                                                info.put( input.substring(0,input.indexOf(' ')) , input.substring(input.indexOf(' ')+1) );
                                            }
                                        }

                                }

                                input = bufferin.readLine(); // get next line

                        }

                        if (cards) {
                            info.put("missions", (String[])misss.toArray(new String[misss.size()]) );
                            misss = null;
                        }

                        break;
                }
                catch(IOException ex) {
                        System.err.println("Error trying to load: "+fileName);
                        RiskUtil.printStackTrace(ex);
                        if (c < 5) { // retry
                                try { Thread.sleep(1000); } catch(Exception ex2) { }
                        }
                        else { // give up
                                break;
                        }
                }
                finally {
                    if (bufferin!=null) {
                        try { bufferin.close(); } catch(Exception ex2) { }
                    }
                }
            }

            return info;

	}

        public static OutputStream getOutputStream(File dir,String fileName) throws Exception {
            File outFile = new File(dir,fileName);
            // as this could be dir=.../maps fileName=preview/file.jpg
            // we need to make sure the preview dir exists, and if it does not, we must make it
            File parent = outFile.getParentFile();
            if (!parent.isDirectory() && !parent.mkdirs()) { // if it does not exist and i cant make it
                throw new RuntimeException("can not create dir "+parent);
            }
            return new FileOutputStream( outFile );
        }

        public static void rename(File oldFile,File newFile) {
            if (newFile.exists() && !newFile.delete()) {
                throw new RuntimeException("can not del dest file: "+newFile);
            }
            if (!oldFile.renameTo(newFile)) {
                try {
                    copy(oldFile, newFile);
                    if (!oldFile.delete()) {
                        // this is not so bad, but still very strange
                        System.err.println("can not del source file: "+oldFile);
                    }
                }
                catch(Exception ex) {
                    throw new RuntimeException("rename failed: from: "+oldFile+" to: "+newFile,ex);
                }
            }
        }



    public static Vector asVector(java.util.List list) {
        return list instanceof Vector?(Vector)list:new Vector(list);
    }



    public static String replaceAll(String string, String notregex, String replacement) {
        return string.replaceAll( quote(notregex) , quoteReplacement(replacement));
    }

    /**
     * @see java.util.regex.Pattern#quote(java.lang.String)
     */
    public static String quote(String s) {
        int slashEIndex = s.indexOf("\\E");
        if (slashEIndex == -1)
            return "\\Q" + s + "\\E";

        StringBuilder sb = new StringBuilder(s.length() * 2);
        sb.append("\\Q");
        slashEIndex = 0;
        int current = 0;
        while ((slashEIndex = s.indexOf("\\E", current)) != -1) {
            sb.append(s.substring(current, slashEIndex));
            current = slashEIndex + 2;
            sb.append("\\E\\\\E\\Q");
        }
        sb.append(s.substring(current, s.length()));
        sb.append("\\E");
        return sb.toString();
    }

    /**
     * @see java.util.regex.Matcher#quoteReplacement(java.lang.String)
     */
    public static String quoteReplacement(String s) {
        if ((s.indexOf('\\') == -1) && (s.indexOf('$') == -1))
            return s;
        StringBuffer sb = new StringBuffer();
        for (int i=0; i<s.length(); i++) {
            char c = s.charAt(i);
            if (c == '\\') {
                sb.append('\\'); sb.append('\\');
            } else if (c == '$') {
                sb.append('\\'); sb.append('$');
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    
    
    
    
    public static void copy(File src, File dest) throws IOException {
     
            if(src.isDirectory()){
     
                    //if directory not exists, create it
                    if(!dest.exists()){
                       dest.mkdir();
                       System.out.println("Directory copied from " 
                                  + src + "  to " + dest);
                    }
     
                    //list all the directory contents
                    String files[] = src.list();
     
                    for (int c=0;c<files.length;c++) {
                       //construct the src and dest file structure
                       File srcFile = new File(src, files[c]);
                       File destFile = new File(dest, files[c]);
                       //recursive copy
                       copy(srcFile,destFile);
                    }
     
            }else{
                    //if file, then copy it
                    //Use bytes stream to support all file types
                    InputStream in = new FileInputStream(src);
                    OutputStream out = new FileOutputStream(dest); 
     
                    byte[] buffer = new byte[1024];
     
                    int length;
                    //copy the file content in bytes 
                    while ((length = in.read(buffer)) > 0){
                       out.write(buffer, 0, length);
                    }
     
                    in.close();
                    out.close();
                    System.out.println("File copied from " + src + " to " + dest);
            }
    }

}
