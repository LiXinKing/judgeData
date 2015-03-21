package com.example.judgedata;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;



import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.widget.Button;

public class MainActivity extends Activity implements  OnTouchListener {


	int wc;  //�������ݼ�¼����/��ͣ�Ŀ��Ʊ�ǣ�1�����¼���ݣ�0������ͣ��¼
    SensorManager mySensorManager;	//SensorManager��������	
	Sensor myaccelerometer;    //���ٶȴ�����������������
	Sensor myrotationSensor;
	private Button writebu;
	float gyrd[];  //���ڴ�����µ�����������
    float accd[];  //���ڴ�����µļ��ٶȴ���������
    float accdtest[];  //���ڴ�����µļ��ٶȴ���������
    float gravity[];
    float rotation[];
    float[] mRotationMatrix = new float[9]; 
    long time;
    long timeacc;
    long timerotation;
    Vibrator vibrator; //��
	//�������Ҫ����7λ��������Ƚϻ����


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        writebu=(Button)findViewById(R.id.writecon);//���ڿ������ݼ�¼����/��ͣ��ť����ʾ
        writebu.setOnTouchListener(this);
        vibrator= (Vibrator) getSystemService(VIBRATOR_SERVICE);
        gyrd=new float[3]; 
        accd=new float[3]; 
        accdtest=new float[3]; 
        gravity=new float[3]; 
        rotation=new float[3];
        wc=0;  //Ĭ�Ͽ������������ݼ�¼�����Ʊ��Ϊ1
        //���SensorManager����
        mySensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);	
        //��ȡȱʡ�����Լ��ٶȴ�����
        myaccelerometer=mySensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        myrotationSensor=mySensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        File sensorFile=new File("//sdcard/sensortestacc.txt");    
//        timer.schedule(task,0, SAMPLET);  //��һ����ʱ0ms��֮��ÿSAMPLETʱ�䣬��¼һ����ٶȴ�����������������
        if(!sensorFile.isFile()){
        	try {
				sensorFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
      }

    }	
    
    

 
    @Override
	protected void onResume(){ //��дonResume����
		super.onResume();
		//���������Ǵ�����

		//�������ٶȴ�����
		mySensorManager.registerListener(
				mySensorListener, 		//��Ӽ���
				myaccelerometer, 		//����������
				SensorManager.SENSOR_DELAY_FASTEST	//�������¼����ݵ�Ƶ��
		);

		mySensorManager.registerListener(
				mySensorListener, 		//��Ӽ���
				myrotationSensor, 		//����������
				SensorManager.SENSOR_DELAY_FASTEST	//�������¼����ݵ�Ƶ��
		);
	}	
	@Override
	protected void onPause(){//��дonPause����	
		super.onPause();
		mySensorManager.unregisterListener(mySensorListener);//ȡ��ע�������
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

	}
	
	private SensorEventListener mySensorListener = 
		new SensorEventListener(){//����ʵ����SensorEventListener�ӿڵĴ�����������
		public void onAccuracyChanged(Sensor sensor, int accuracy){}
		public void onSensorChanged(SensorEvent event){
			
		 //��������ȡ����������£���ȡ����������
			//��ϵͳ�տ�ʼ��ȡ���ٶȵ�ʱ�򣬼�¼��ϵͳ��ʱ�䲻�ȶ�����ò��Ǵ�һ����ʼ����
    			long time=System.currentTimeMillis();
    			time=time-time/10000000*10000000;
				float []values=event.values;//��ȡ����������������
				float offset=(float)Math.sqrt(values[0]*values[0]+values[1]*values[1]+values[2]*values[2]);
				//�����Ǵ������仯
				if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION){
				   //С��ACCMINʱ������Χ		
						accd[0]=values[0];
						accd[1]=values[1];
						accd[2]=values[2];  //�����µļ��ٶȴ��������ݴ��ڼ��ٶȴ�����������
						accdtest[0]=accd[0];
						accdtest[1]=accd[1];
						accdtest[2]=accd[2];  //�����µļ��ٶȴ��������ݴ��ڼ��ٶȴ�����������
						timeacc=time;
						if(wc==1){
						float[] accd1=new float[3];
						float[] n=new float[3];
						n[0]=1;n[1]=1;n[2]=1;

			        	float [][] buffer={{(float) accd[0],0,0},
        						{(float) accd[1],0,0},
        						{(float) accd[2],0,0}};
			        	float[] Orientation=new float[3];
						SensorManager.getRotationMatrixFromVector(mRotationMatrix, rotation);
						
						SensorManager.getOrientation(mRotationMatrix, Orientation);
						
						float[][] rotationversion=matrixinversion(mRotationMatrix);
						float[][] mk={{mRotationMatrix[0],mRotationMatrix[1],mRotationMatrix[2]},
										{mRotationMatrix[3],mRotationMatrix[4],mRotationMatrix[5]},
										{mRotationMatrix[6],mRotationMatrix[7],mRotationMatrix[8]}};
						rotationversion=maxtrixmutiply(mk,buffer);  
						accd[0]=rotationversion[0][0];
						accd[1]=rotationversion[1][0];
						accd[2]=rotationversion[2][0];
//						if(Math.abs(accd[0])<0.15)accd[0]=0;
//						if(Math.abs(accd[1])<0.15)accd[1]=0;
//						if(Math.abs(accd[2])<0.15)accd[2]=0;
				        try {

        		        	FileOutputStream foStream=new FileOutputStream(
                		        	"//sdcard/sensortestacc.txt",true); //���崫�������ݵ������
        					String sensorstr=accd[0]+" "+accd[1]+" "+accd[2]+" "+timeacc+" "
        					+accdtest[0]+" "+accdtest[1]+" "+accdtest[2]+" "+"0"+" "
        					+rotation[0]+" "+rotation[1]+" "+rotation[2]+" "+timerotation+" "+
        					-Orientation[0]*180/Math.PI+" "+-Orientation[1]*180/Math.PI+" "+-Orientation[2]*180/Math.PI+" "+"0"+"\n";  
		        			byte[] buffer11=new byte[sensorstr.length()*2];
		        			buffer11=sensorstr.getBytes();
		        			foStream.write(buffer11);
		        			foStream.close();
				    		} catch (FileNotFoundException e) {
				    			// TODO Auto-generated catch block
				    			Log.v("FileNotFoundException", "OK");
				    			e.printStackTrace();
				    		} catch (IOException e) {
				    			// TODO Auto-generated catch block
				    			e.printStackTrace();
				    		}
						}

					
				}
				

				else if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR){
						rotation[0]=(float)values[0];
						rotation[1]=(float)values[1];
						rotation[2]=(float)values[2];  //�����µļ��ٶȴ��������ݴ��ڼ��ٶȴ�����������
						timerotation=time;
					    
				}
				
			
			
	}
	
	};
	
	@Override
	public boolean onKeyDown(int keyCode,KeyEvent e)
	{
		switch(keyCode)
	    	{
		case 4:
			System.exit(0);
			break;
	    	}
		return true;
	}


	
	public void onClick_writecon(View view)  //�������ݼ�¼����/��ͣ��Ķ���
	{
	}
	
	public void onClick_delete(View view)  //���������Ķ���
	{
		File sensor=new File("//sdcard/sensortest.txt");  //��ȡ�ļ�����
		sensor.delete();  //���ļ�ɾ��
		File sensor1=new File("//sdcard/sensortestacc.txt");  //��ȡ�ļ�����
		sensor1.delete();  //���ļ�ɾ��
		vibrator.vibrate(200);
	}
    //��������ʽ�ļ���
    public static float getHL3(float[] input) {  
    	float unm1=input[0]*(input[4]*input[8]-input[5]*input[7]);
    	float unm2=-input[1]*(input[3]*input[8]-input[5]*input[6]);
    	float unm3=input[2]*(input[3]*input[7]-input[4]*input[6]);
        return unm1+unm2+unm3;
    }  
 
    private static float[][] matrixinversion(float[] input){
    	//���������ʽ
    	float[] buffer1=new float[9];

    	for(int i=0;i<input.length;i++){
    		float[] buffer0=input.clone();
    		if(i%3==0){buffer0[i]=1;buffer0[i+1]=0;buffer0[i+2]=0;}
    		if(i%3==1){buffer0[i-1]=0;buffer0[i]=1;buffer0[i+1]=0;}
    		if(i%3==2){buffer0[i-2]=0;buffer0[i-1]=0;buffer0[i]=1;}
    		buffer1[i]=getHL3(buffer0)/getHL3(input);
    		if(i%2==1)buffer1[i]=-buffer1[i];
    	}
    	  float[][] buffer=	{{buffer1[0],buffer1[1],buffer1[2]},
    						{buffer1[3],buffer1[4],buffer1[5]},
    						{buffer1[6],buffer1[7],buffer1[8]}}; 	
    	  return buffer;
    }
    private static float[][] maxtrixmutiply(float[][] maxtrileft, float[][] maxtriright) {
    	float[][] result = {{0,0,0},{0,0,0},{0,0,0}} ;
		// TODO Auto-generated method stub

    	for(int i=0;i<maxtrileft.length;i++)
    		for(int j=0;j<maxtrileft[0].length;j++){
    			result[i][j]=maxtrileft[i][0]*maxtriright[0][j]+
    			maxtrileft[i][1]*maxtriright[1][j]+maxtrileft[i][2]*maxtriright[2][j];
    		}
		return result;
	}//һ������˷�

	public boolean onTouch(View view, MotionEvent event) {
		// TODO Auto-generated method stub
		if (event.getAction() == MotionEvent.ACTION_UP)
		{
						wc=0;
			vibrator.vibrate(200);
			view.setBackgroundResource(R.drawable.button1);
			
		}
		else if (event.getAction() == MotionEvent.ACTION_DOWN)
		{			view.setBackgroundResource(R.drawable.button3);
    	try {
			FileOutputStream foStream=new FileOutputStream(
			    	"//sdcard/sensortestacc.txt",true);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} //���崫�������ݵ������
					wc=1;
		
		}

		return false;
	}





}
