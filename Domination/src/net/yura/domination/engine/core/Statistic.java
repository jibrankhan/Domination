// Yura Mamyrin

package net.yura.domination.engine.core;

import java.io.Serializable;

/**
 * <p> Risk Player </p>
 * @author Yura Mamyrin
 */

public class Statistic implements Serializable {

	private static final long serialVersionUID = 1L;

    // RISK II Statistics
    public static final int countries = 0;
    public static final int armies = 1;
    public static final int kills = 2;
    public static final int casualties = 3;
    public static final int reinforcements = 4;
    public static final int continents = 5;
    public static final int conectedEmpire = 6;
    public static final int attacks = 7;

    public static final int retreats = 8;
    public static final int countriesWon = 9;
    public static final int countriesLost = 10;
    public static final int attacked = 11;

    // in the rest of the game they are knows as num + 1

    public int[] statistics;

    public Statistic() {

	statistics = new int[12];

	for (int i = 0; i < statistics.length; i++) {
	    statistics[i]=0;
	}

    }

    // at the end of a persons go this gets called
    public void endGoStatistics(int a, int b, int c, int d) {

	statistics[0] = a;
	statistics[1] = b;
	statistics[5] = c;
	statistics[6] = d;
/*
	System.out.print("\nStatistic for the last go:\n");
	System.out.print("countries "+statistics[0]+"\n");
	System.out.print("armies "+statistics[1]+"\n");
	System.out.print("kills "+statistics[2]+"\n");
	System.out.print("casualties "+statistics[3]+"\n");
	System.out.print("reinforcements "+statistics[4]+"\n");
	System.out.print("continents "+statistics[5]+"\n");
	System.out.print("conectedEmpire "+statistics[6]+"\n");
	System.out.print("attacks "+statistics[7]+"\n");

	System.out.print("retreats "+statistics[8]+"\n");
	System.out.print("countriesWon "+statistics[9]+"\n");
	System.out.print("countriesLost "+statistics[10]+"\n");
	System.out.print("attacked "+statistics[11]+"\n");
*/
    }

    public void addReinforcements(int a) {
	statistics[4] = statistics[4] + a;
    }

    public void addKill() {
	statistics[2]++;
    }

    public void addCasualty() {
	statistics[3]++;
    }

    public void addAttack() {
	statistics[7]++;
    }

    public void addAttacked() {
	statistics[11]++;
    }

    public void addRetreat() {
	statistics[8]++;
    }

    public void addCountriesWon() {
	statistics[9]++;
    }

    public void addCountriesLost() {
	statistics[10]++;
    }

    public int get(int a) {

	return statistics[a-1];

    }

}
