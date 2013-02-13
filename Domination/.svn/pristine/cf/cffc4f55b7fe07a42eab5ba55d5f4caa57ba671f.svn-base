package net.yura.domination.android;

import java.util.HashMap;
import java.util.List;
import net.yura.android.AndroidMeApp;
import net.yura.domination.engine.core.Player;
import net.yura.domination.mobile.flashgui.DominationMain;
import net.yura.domination.mobile.flashgui.GameActivity;
import net.yura.mobile.util.Properties;
import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class StatsActivity extends Activity {

    Properties resb = GameActivity.resb;
    
    public interface EnumConverter<T> {
        public T convert();
    }
    
    public class ReverseEnumMap<T,V extends Enum<V> & EnumConverter<T>> extends HashMap<T, V> {
        public ReverseEnumMap(Class<V> valueType) {
            for (V v : valueType.getEnumConstants()) {
                put(v.convert(), v);
            }
        }
    }

    
    

    enum Stat implements EnumConverter<Integer> {
        COUNTRIES("countries",1),
        ARMIES("armies",2),
        KILLS("kills",3),
        CASUALTIES("casualties",4),
        REINFORCEMENTS("reinforcements",5),
        CONTINENTS("continents",6),
        EMPIRE("empire",7),
        ATTACKS("attacks",8),
        RETREATS("retreats",9),
        VICTORIES("victories",10),
        DEFEATS("defeats",11),
        ATTACKED("attacked" ,12);

        private final String name;
        private final int id;
        Stat(String name, int id) {
            this.name = name;
            this.id = id;
        }
        @Override
        public Integer convert() {
            return id;
        }
    }
    
    final ReverseEnumMap<Integer,Stat> lookup = new ReverseEnumMap<Integer,Stat>(Stat.class);
    
    List<Player> getPlayersStats() {
        DominationMain dmain = (DominationMain)AndroidMeApp.getMIDlet();
        return dmain.risk.getGame().getPlayersStats();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setTitle( resb.getString("swing.tab.statistics") );
        showGraph( Stat.COUNTRIES );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        for (Stat s:Stat.values()) {
            MenuItem item = menu.add( android.view.Menu.NONE, s.id, android.view.Menu.NONE, resb.getString("swing.toolbar." + s.name ) );
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        
        int id = item.getItemId();
        
        Stat stat = lookup.get( id );
        if (stat!=null) {
            showGraph(stat);
        }
        
        return true;
    }
    
    public void showGraph(Stat a) {
        
        setTitle( resb.getString("swing.tab.statistics")+" - "+resb.getString("swing.toolbar." + a.name ) );
        
        GraphicalView gview = ChartFactory.getLineChartView(this, getDataset(a.id), getRenderer() );
        setContentView(gview);
    }
    
    private XYMultipleSeriesRenderer getRenderer() {
        
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        
        List<Player> players = getPlayersStats();

        for (int c = 0; c < players.size(); c++) {
        
            Player p = players.get(c);
            
            SimpleSeriesRenderer r = new XYSeriesRenderer();
            r.setColor( p.getColor() );
            renderer.addSeriesRenderer(r);

        }
        return renderer;
    }

    public XYMultipleSeriesDataset getDataset(int a) {

        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        
        List<Player> players = getPlayersStats();

        //draw each player graph.
        for (int c = 0; c < players.size(); c++) {
            
            Player p = players.get(c);
            
            CategorySeries series = new CategorySeries( p.getName() );
            
            int[] PointToDraw = p.getStatistics(a);

            int newPoint=0;
            
            series.add( newPoint ); // everything starts from 0
            
            for (int i = 0; i < PointToDraw.length; i++) {

                // TODO why 0? this makes no sense??
                if ( a==0 || a==1 || a==2 || a==6 || a==7) {
                    newPoint = PointToDraw[i] ;
                }
                else {
                    newPoint += PointToDraw[i] ;
                }

                series.add( newPoint );

            }

            dataset.addSeries(series.toXYSeries());

        }
        
        return dataset;

    }

}
