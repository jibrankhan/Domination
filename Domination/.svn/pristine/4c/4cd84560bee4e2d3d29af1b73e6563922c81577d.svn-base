// source: http://n.ethz.ch/student/simonbr/mathinfo_applets_erde.html

package net.yura.domination.engine.guishared;

import java.awt.event.*;
import java.awt.*;
import java.awt.image.*;
import javax.swing.JPanel;
import net.yura.domination.engine.RiskUtil;

//////////////////////////////////////////////////////////////
//
// Class Erde: 3d-Darstellung der Erde mit Texture Mapping
//
//////////////////////////////////////////////////////////////

public class ErdeAnsicht extends JPanel implements ImageProducer {

double xs=5.0;
double xp=0.0;
double q=1.0; // zoom
double phi=0;
double phigrad=0;
double psi=0;
double psigrad=0;
double cosphi=1;
double sinphi=0;
double cospsi=1;
double sinpsi=0;
double alpha=0;

boolean zeichnetex=true; // texture
boolean wireframe=false;
boolean showinfo=false;

Image puffer;
Graphics pufferg;

double[][][] KugelNetz=new double[36][19][3];
double[][][] RotKugelNetz=new double[36][19][3];

double[][] punkte=new double[3000][3];
Color[] farbe=new Color[1500];
int anzlinie=0;

int[] text;
int textwidth=0;
int textheight=0;

double[][] mark=new double[4][3];
boolean ismark=false;

int addmxy=0;
int dux=0;
int duy=0;
int dvx=0;
int dvy=0;
int dudvdet=0;
double mdux=0;
double mduy=0;
double mdvx=0;
double mdvy=0;

//////////////////////////////////////////////////////////////
//
// Abschnitt Initialisieren
//
//////////////////////////////////////////////////////////////

public ErdeAnsicht(){
	super();	
	for(int b=0;b<=18;b++){
	 	double ps=toradians(b*10);
		double cosps=Math.cos(ps);
		double sinps=Math.sin(ps);		
		for(int l=0;l<36;l++){
			double ph=toradians(l*10);			
			double cosph=Math.cos(ph);
			double sinph=Math.sin(ph);
			KugelNetz[l][b][0]=sinps*cosph;
			KugelNetz[l][b][1]=sinps*sinph;
			KugelNetz[l][b][2]=cosps;
		}
	}

}

public void init(){
	setviewp(xs,q);	
	setrot(phigrad,psigrad);
	
	puffer=createImage(getSize().width,getSize().height);
	pufferg=puffer.getGraphics();
	//box();
	//kords();
	//kegel();
	//zylinder();
	//tangent(1);
	zeichnen();
}

public void setviewp(double setXs,double setQ){
	q=setQ;
	xs=setXs;
	double a=1/(Math.sqrt(xs*xs-1));
	double b=a*xs;
	double max=(double)getSize().height;
	if(puffer!=null) max=(double)puffer.getHeight(this); 
	xp=(q*max-2*b)/(2*a);
	alpha=1/(xs*xs-1);		
}

public void setmark(double l,double b,boolean setze){
	double[] p1={-1,0.05,0.05}; 	
	double[] p2={-1,-0.05,-0.05};
	double[] p3={-1,-0.05,0.05};
	double[] p4={-1,0.05,-0.05};
	ismark=setze;
	p1=rotonepoint(p1,0,l);p1=rotonepoint(p1,b,0);
	p2=rotonepoint(p2,0,l);p2=rotonepoint(p2,b,0);
	p3=rotonepoint(p3,0,l);p3=rotonepoint(p3,b,0);
	p4=rotonepoint(p4,0,l);p4=rotonepoint(p4,b,0);		 
	for(int i=0;i<3;i++){
		mark[0][i]=p1[i]; mark[1][i]=p2[i];
		mark[2][i]=p3[i]; mark[3][i]=p4[i];		
	}
}

//////////////////////////////////////////////////////////////
//
// Abschnitt Punkte drehen
//
//////////////////////////////////////////////////////////////

public double[] rotonepoint(double[] P,double rotphi,double rotpsi){
	double radrotphi=toradians(rotphi);
	double radrotpsi=toradians(rotpsi);
	double cosrotphi=Math.cos(radrotphi);
	double sinrotphi=Math.sin(radrotphi);	
	double cosrotpsi=Math.cos(radrotpsi);
	double sinrotpsi=Math.sin(radrotpsi);		
	double[] V=new double[3];
	V[0]=cosrotpsi*cosrotphi*P[0]-cosrotpsi*sinrotphi*P[1]-sinrotpsi*P[2];
	V[1]=sinrotphi*P[0]+cosrotphi*P[1];
	V[2]=sinrotpsi*cosrotphi*P[0]-sinrotpsi*sinrotphi*P[1]+cosrotpsi*P[2];	
	return V;
}

public void rotpoints(double rotphi,double rotpsi){
	double radrotphi=toradians(rotphi);
	double radrotpsi=toradians(rotpsi);
	double cosrotphi=Math.cos(radrotphi);
	double sinrotphi=Math.sin(radrotphi);	
	double cosrotpsi=Math.cos(radrotpsi);
	double sinrotpsi=Math.sin(radrotpsi);		
	for(int i=0;i<anzlinie*2+1;i++){
		double[] V=new double[3];
		V[0]=cosrotpsi*cosrotphi*punkte[i][0]-cosrotpsi*sinrotphi*punkte[i][1]-sinrotpsi*punkte[i][2];
		V[1]=sinrotphi*punkte[i][0]+cosrotphi*punkte[i][1];
		V[2]=sinrotpsi*cosrotphi*punkte[i][0]-sinrotpsi*sinrotphi*punkte[i][1]+cosrotpsi*punkte[i][2];	
		punkte[i]=V;		
	}		
}

public double toradians(double w){
	return((w*Math.PI)/180);
}

public void setrot(double setphi,double setpsi){
	setphi=setphi%360;
	if(setphi<-180) setphi=setphi+360;	
	if(setphi>180) setphi=setphi-360;	
	if(setpsi>90) setpsi=90;
	if(setpsi<-90) setpsi=-90;
	phigrad=setphi;
	psigrad=setpsi;
	phi=toradians(setphi);
	psi=toradians(setpsi);
	cosphi=Math.cos(phi);
	sinphi=Math.sin(phi);
	cospsi=Math.cos(psi);
	sinpsi=Math.sin(psi);	
}

public double[] rot(double[] V){
	double[] Vrot=new double[3];
	Vrot[0]=cospsi*cosphi*V[0]-cospsi*sinphi*V[1]-sinpsi*V[2];
	Vrot[1]=sinphi*V[0]+cosphi*V[1];
	Vrot[2]=sinpsi*cosphi*V[0]-sinpsi*sinphi*V[1]+cospsi*V[2];	
	return Vrot;
}

public void rotkugel(){
	for(int l=0;l<36;l++){
		for(int b=0;b<=18;b++){
			RotKugelNetz[l][b]=rot(KugelNetz[l][b]);
		}
	}		
}

//////////////////////////////////////////////////////////////
//
// Abschnitt Linien zeichnen
//
//////////////////////////////////////////////////////////////

public int[] proj(double[] V){
	int[] projvect={(int)(V[1]*(xs+xp)/(V[0]+xs)),-(int)(V[2]*(xs+xp)/(V[0]+xs))};
	return projvect;	
}

public void setline(double[] V1,double[] V2,Color C){
	for(int i=0;i<3;i++){
		punkte[anzlinie*2][i]=V1[i];
		punkte[anzlinie*2+1][i]=V2[i];
	}
	farbe[anzlinie]=C;	
	anzlinie++;
}

public void zeichneline(double[] V1,double[] V2,Color C){
	int[] P1=proj(V1);
	int[] P2=proj(V2);
	int transl=puffer.getHeight(this)/2;
	pufferg.setColor(C);	
	pufferg.drawLine(P1[0]+transl,P1[1]+transl,P2[0]+transl,P2[1]+transl);
	pufferg.drawLine(P2[0]+transl,P2[1]+transl,P1[0]+transl,P1[1]+transl);
}

public boolean inkegel(double[] V){
	return ((V[1]*V[1]+V[2]*V[2])<alpha*(V[0]+xs)*(V[0]+xs));
}

public void line3d(double[] V1,double[] V2,Color C){	
	double[] Vrot1=rot(V1);	
	double[] Vrot2=rot(V2);		
	if(Vrot1[0]<0 && Vrot2[0]<0){
		zeichneline(Vrot1,Vrot2,C);
	}
	else if(inkegel(Vrot1) && Vrot1[0]<0) zeichneline(Vrot1,Vrot2,C);		
	else if(inkegel(Vrot2) && Vrot2[0]<0) zeichneline(Vrot1,Vrot2,C);
	else{		
	
		double[] a=Vrot1;
		double[] r={Vrot2[0]-Vrot1[0],Vrot2[1]-Vrot1[1],Vrot2[2]-Vrot1[2]};	
		// Schnitt: Gerade-Kegel (Quadratische Gleichung agl*x^2+bgl*x+cgl=0)
		double agl=r[1]*r[1]+r[2]*r[2]-alpha*r[0]*r[0];
		double bgl=2*a[1]*r[1]+2*a[2]*r[2]-2*alpha*r[0]*a[0]-2*alpha*xs*r[0];
		double cgl=a[1]*a[1]+a[2]*a[2]-alpha*a[0]*a[0]-2*alpha*xs*a[0]-alpha*xs*xs;	
		double diskr=bgl*bgl-4*agl*cgl;
		if(diskr<=0){	
			zeichneline(Vrot1,Vrot2,C);				
		}
		else{
			double t1=(-bgl-Math.sqrt(diskr))/(2*agl);			
			double[] Ps1={a[0]+t1*r[0],a[1]+t1*r[1],a[2]+t1*r[2]};
			double t2=(-bgl+Math.sqrt(diskr))/(2*agl);			
			double[] Ps2={a[0]+t2*r[0],a[1]+t2*r[1],a[2]+t2*r[2]};										
			
			if(t1>0 && t1<1) zeichneline(Vrot1,Ps1,C);
			if(t2>0 && t2<1) zeichneline(Vrot2,Ps2,C);				
			if((t1<0 || t1>1) && (t2<0 || t2>1) && !(inkegel(Vrot1) && inkegel(Vrot2))) 
				zeichneline(Vrot1,Vrot2,C);								
		}
	}
}

public void line3dkugel(double[] V1,double[] V2,Color C){	
	if(V1[0]<0 && V2[0]<0){
		zeichneline(V1,V2,C);
	}
}

//////////////////////////////////////////////////////////////
//
// Abschnitt Texture Mapping
//
//////////////////////////////////////////////////////////////

public void addConsumer(ImageConsumer imageconsumer) { }
public boolean isConsumer(ImageConsumer imageconsumer) { return false; }
public void removeConsumer(ImageConsumer imageconsumer) { }
public void requestTopDownLeftRightResend(ImageConsumer imageconsumer) { }
public void startProduction(ImageConsumer imageconsumer)
     {
		int texttmpgr=(int)(puffer.getHeight(this)*q)+4;
		consumer=imageconsumer;		
		consumer.setDimensions(texttmpgr, texttmpgr);
		consumer.setColorModel(directCM);
		consumer.setHints(2);
     }

private ImageConsumer consumer;
private DirectColorModel directCM = new DirectColorModel(24, 0xff0000, 0xff00, 0xff);

public void settext(Image img){
	textheight=img.getHeight(this);
	textwidth=img.getWidth(this);	
	textsize=textheight*textwidth;
	text=new int[textsize];
	PixelGrabber pg = new PixelGrabber(img,0,0,textwidth,textheight,text,0,textwidth);	
	try{ 
		pg.grabPixels(); 
	}catch(Exception e){

		RiskUtil.printStackTrace(e);

	}		    
	prepareImage(textproj, null);		
}

public int[] mappoint(int l,int b){
	int[] p={textwidth-(l*textwidth)/36,(b*textheight)/18};	
	return p;
}

int textsize;
int[] texttmp;
int texttmpgr;
int texttmpsize;
int weiss=Color.BLACK.getRGB();
Image textproj=createImage(this);

public void gettext(){	
	if(text!=null){
	texttmpgr=(int)(puffer.getHeight(this)*q)+4;	
	texttmpsize=texttmpgr*texttmpgr;		
	texttmp=new int[texttmpsize];	
	for(int i=0;i<texttmpsize;i++) texttmp[i]=weiss;	
	for(int l=0;l<36;l++){
		for(int b=0;b<18;b++){			
			deform(l,b,l+1,b,l+1,b+1);
			deform(l,b,l,b+1,l+1,b+1);
		}
	}
	int transl=(puffer.getHeight(this)-texttmpgr)/2-2;	
	if(consumer != null){  		
		consumer.setPixels(0, 0, texttmpgr,texttmpgr, directCM, texttmp, 0, texttmpgr);
		consumer.imageComplete(2); 
 	}
	pufferg.drawImage(textproj,transl,transl,this);	
	}
}

public void deform(int l1,int b1,int l2,int b2,int l3,int b3){
	double[] V1=RotKugelNetz[l1%36][b1];
	double[] V2=RotKugelNetz[l2%36][b2];
	double[] V3=RotKugelNetz[l3%36][b3];
	if(V1[0]<0 && V2[0]<0 && V3[0]<0){
		int[] P1=proj(V1);
		int[] P2=proj(V2);
		int[] P3=proj(V3);	
		int[] map0=mappoint(l1,b1);
		int[] map1=mappoint(l2,b2);
		int[] map2=mappoint(l3,b3);
		int[] mapx={(int)map0[0],(int)map1[0],(int)map2[0]};
  		int[] mapy={(int)map0[1],(int)map1[1],(int)map2[1]};  		
  		int[] sx={P1[0],P2[0],P3[0]};
  		int[] sy={P1[1],P2[1],P3[1]};  		 	
  		draw3eck(mapx,mapy,sx,sy);  
	}	
}

public void scanline(int y,int xa,int xb,int[] mapx, int[] mapy,int[] scrx, int[] scry){
	int px=0; int py=y-scry[0];	
	int xstart=0; int xend=0;	
	int tmapx=0; int tmapy=0;	
	int tmpoffset=(y+addmxy)*texttmpgr+addmxy;	
	if(xa<=xb){ xstart=xa; xend=xb;} else { xstart=xb; xend=xa;}	
	px=xstart-scrx[0];		
	double ustart=(dvx*py)/((double)dudvdet);
	double vstart=(dux*py)/((double)dudvdet);
	double uschritt=dvy/((double)dudvdet);
	double vschritt=duy/((double)dudvdet);
	double u=px*uschritt-ustart;
	double v=vstart-px*vschritt;	
	double tmapxr=mapx[0]+u*mdux+v*mdvx;
	double tmapyr=mapy[0]+u*mduy+v*mdvy;
	double ux=uschritt*mdux;
	double uy=uschritt*mduy;
	double vx=vschritt*mdvx;
	double vy=vschritt*mdvy;
	double uxvx=ux-vx;
	double uyvy=uy-vy;
	int i=0;	
	int j=tmpoffset+xstart;
	for(int mx=xstart;mx<=xend;mx++){
		tmapxr=tmapxr+uxvx;
		tmapyr=tmapyr+uyvy;
		i=(int)tmapyr*textwidth+(int)tmapxr;
		if(i>=0 && i<textsize && j>=0 && j<texttmpsize)	texttmp[j]=text[i];
		j++;
	}	
}

public void draw3eck(int[] mapx,int[] mapy,int[] scrx,int[] scry){
	if(scry[0]>scry[1]){
		int tmp;
		tmp=scry[1]; scry[1]=scry[0]; scry[0]=tmp;
		tmp=scrx[1]; scrx[1]=scrx[0]; scrx[0]=tmp;
		tmp=mapy[1]; mapy[1]=mapy[0]; mapy[0]=tmp;
		tmp=mapx[1]; mapx[1]=mapx[0]; mapx[0]=tmp;		
	}
	if(scry[0]>scry[2]){
		int tmp;
		tmp=scry[0]; scry[0]=scry[2]; scry[2]=tmp;
		tmp=scrx[0]; scrx[0]=scrx[2]; scrx[2]=tmp;
		tmp=mapy[0]; mapy[0]=mapy[2]; mapy[2]=tmp;
		tmp=mapx[0]; mapx[0]=mapx[2]; mapx[2]=tmp;		
	}
	if(scry[1]>scry[2]){
		int tmp;
		tmp=scry[1]; scry[1]=scry[2]; scry[2]=tmp;
		tmp=scrx[1]; scrx[1]=scrx[2]; scrx[2]=tmp;
		tmp=mapy[1]; mapy[1]=mapy[2]; mapy[2]=tmp;
		tmp=mapx[1]; mapx[1]=mapx[2]; mapx[2]=tmp;		
	}	
	dux=scrx[1]-scrx[0];
	duy=scry[1]-scry[0];
	dvx=scrx[2]-scrx[0];
	dvy=scry[2]-scry[0];
	dudvdet=dux*dvy-duy*dvx;
	if(dudvdet!=0){
		addmxy=texttmpgr/2+2;			
		double delta01=0;
		double delta02=0;
		double delta12=0;
		mdux=mapx[1]-mapx[0]; mduy=mapy[1]-mapy[0]; 
		mdvx=mapx[2]-mapx[0]; mdvy=mapy[2]-mapy[0];	
		double xa=(double)scrx[0];
		double xb=xa;	
		if(scry[0]-scry[1]!=0)
			delta01=(dux)/(double)(duy);
		else 
			xa=scrx[1];	
		if(scry[0]-scry[2]!=0) delta02=(dvx)/(double)(dvy);
		if(scry[1]-scry[2]!=0) delta12=(scrx[1]-scrx[2])/(double)(scry[1]-scry[2]);				
		for(int y=scry[0];y<scry[1];y++){
			scanline(y,(int)xa,(int)xb, mapx, mapy, scrx, scry);	
			xa=xa+delta01;
			xb=xb+delta02;
		}
		for(int y=scry[1];y<=scry[2];y++){
			scanline(y,(int)xa,(int)xb, mapx, mapy, scrx, scry);	
			xa=xa+delta12;
			xb=xb+delta02;
		}
	}
}

//////////////////////////////////////////////////////////////
//
// Abschnitt Objekte
//
//////////////////////////////////////////////////////////////

public void box(){
	double[] p1={-1.0,-1.0,-1.0};
	double[] p2={-1.0,1.0,-1.0};
	double[] p3={-1.0,1.0,1.0};
	double[] p4={-1.0,-1.0,1.0};
	double[] p5={1.0,-1.0,-1.0};
	double[] p6={1.0,1.0,-1.0};
	double[] p7={1.0,1.0,1.0};
	double[] p8={1.0,-1.0,1.0};
	
	setline(p1,p2,Color.black);	
	setline(p2,p3,Color.black);	
	setline(p3,p4,Color.black);	
	setline(p4,p1,Color.black);	
	setline(p5,p6,Color.black);	
	setline(p6,p7,Color.black);	
	setline(p7,p8,Color.black);	
	setline(p8,p5,Color.black);	
	setline(p1,p5,Color.black);	
	setline(p2,p6,Color.black);	
	setline(p3,p7,Color.black);	
	setline(p4,p8,Color.black);	
}

public void kords(){
	double[] p1={1.0,0.0,0.0}; double[] p2={5.0,0.0,0.0}; setline(p1,p2,Color.black);	
	double[] p3={-1.0,0.0,0.0}; double[] p4={-5.0,0.0,0.0}; setline(p3,p4,Color.black);	
	double[] p5={0.0,1.0,0.0}; double[] p6={0.0,5.0,0.0}; setline(p5,p6,Color.black);	
	double[] p7={0.0,-1.0,0.0}; double[] p8={0.0,-5.0,0.0}; setline(p7,p8,Color.black);	
	double[] p9={0.0,0.0,1.0}; double[] p10={0.0,0.0,5.0}; setline(p9,p10,Color.black);	
	double[] p11={0.0,0.0,-1.0}; double[] p12={0.0,0.0,-5.0}; setline(p11,p12,Color.black);		
}

public void kegel(){	
	punkte=new double[3000][3];
	farbe=new Color[1500];
	anzlinie=0;
	double[] p2={0,0,Math.sqrt(2)};
	for(int i=0;i<36;i++){		
		double wi=toradians(i*10);
		double coswi=Math.cos(wi);
		double sinwi=Math.sin(wi);				
		double[] p1={coswi*Math.sqrt(2),sinwi*Math.sqrt(2),0};
		setline(p1,p2,Color.black);
	}
	for(int j=0;j<36;j++){
		double wi=toradians(j*10);
		double coswi=Math.cos(wi)*Math.sqrt(2);
		double sinwi=Math.sin(wi)*Math.sqrt(2);				
		double wi2=toradians(((j+1)*10)%360);
		double coswi2=Math.cos(wi2)*Math.sqrt(2);
		double sinwi2=Math.sin(wi2)*Math.sqrt(2);							
		double[] oben1={coswi,sinwi,0};
		double[] oben2={coswi2,sinwi2,0};
		setline(oben1,oben2,Color.black);		
	}	
}

public void zylinder(){	
	punkte=new double[3000][3];
	farbe=new Color[1500];
	anzlinie=0;
	for(int i=0;i<36;i++){
		double wi=toradians(i*10);
		double coswi=Math.cos(wi);
		double sinwi=Math.sin(wi);				
		double[] p1={coswi,sinwi,-1};
		double[] p2={coswi,sinwi,1};
		setline(p1,p2,Color.black);
	}	
	for(int j=0;j<36;j++){
		double wi=toradians(j*10);
		double coswi=Math.cos(wi);
		double sinwi=Math.sin(wi);				
		double wi2=toradians(((j+1)*10)%360);
		double coswi2=Math.cos(wi2);
		double sinwi2=Math.sin(wi2);							
		double[] oben1={coswi,sinwi,1};
		double[] oben2={coswi2,sinwi2,1};
		setline(oben1,oben2,Color.black);
		double[] unten1={coswi,sinwi,-1};
		double[] unten2={coswi2,sinwi2,-1};
		setline(unten1,unten2,Color.black);
	}			
}

public void tangent(double z){	
	punkte=new double[3000][3];
	farbe=new Color[1500];
	anzlinie=0;
	for(double i=-1;i<=1;i=i+0.1){
		double[] p1={i,1,z};
		double[] p2={i,-1,z};
		double[] p3={1,i,z};
		double[] p4={-1,i,z};		
		setline(p1,p2,Color.black);			
		setline(p3,p4,Color.black);			
	}
}

public void kugel(){
	// Breitenkreise
	for(int b=0;b<=18;b++){
		for(int l=0;l<36;l++){
			line3dkugel(RotKugelNetz[l][b],RotKugelNetz[(l+1)%36][b],b==9?Color.red:Color.lightGray);
		}
	}
	// Laengenkreise
	for(int l=0;l<36;l++){
		for(int b=0;b<18;b++){
			line3dkugel(RotKugelNetz[l][b],RotKugelNetz[l][b+1],(l==0||l==18)?Color.red:Color.lightGray);
		}
	}	
}

//////////////////////////////////////////////////////////////
//
// Abschnitt Zeichnungs und repaint-Prozeduren
//
//////////////////////////////////////////////////////////////

public void zeichnen() {

	if(puffer!=null){

		//pufferg.setColor(Color.BLACK);
		//pufferg.fillRect(0,0,this.getSize().width,this.getSize().height);
	
		rotkugel();

		if(zeichnetex==true) {

			gettext();

		}

		if (wireframe) {

			kugel();
		}

		for(int i=0;i<anzlinie;i++) line3d(punkte[i*2],punkte[i*2+1],farbe[i]);	
		if(ismark==true){
			line3d(mark[0],mark[1],Color.red); line3d(mark[2],mark[3],Color.red);
		}

		if (showinfo) {

			pufferg.setColor(Color.WHITE);

			pufferg.drawString("Longitude: "+Double.toString(Math.abs(phigrad))+(phigrad>=0?"E":"W"),10,20);
			pufferg.drawString("Latitude: "+Double.toString(Math.abs(psigrad))+(psigrad>=0?"N":"S"),10,35);

		}

		paintComponent(getGraphics());
		//repaint();		
	}
}

public void paintComponent(Graphics g){	
	if(puffer!=null) g.drawImage(puffer,0,0,this);	
}

//public void update(Graphics g){   
//	paint(g);
//}

//////////////////////////////////////////////////////////////
//
// Abschnitt Drehen mit Maus
//
//////////////////////////////////////////////////////////////

	public void rotate(double a,double b) {

		setrot(phigrad+a,psigrad+b);
		zeichnen();

	}

	public void toggleWireframe() {

		wireframe = !wireframe;
		zeichnen();
	}

	public void toggleInfo() {

		showinfo = !showinfo;
		zeichnen();
	}

}
