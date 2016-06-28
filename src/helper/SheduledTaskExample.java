package helper;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import data_managers.ExamManager;
import models.Exam;

public class SheduledTaskExample {
	
    private final ScheduledExecutorService scheduler = Executors
        .newScheduledThreadPool(1);
    
	static final long ONE_MINUTE_IN_MILLIS=60000;//millisecs


    public void startScheduleTask() {
    /**
    * not using the taskHandle returned here, but it can be used to cancel
    * the task, or check if it's done (for recurring tasks, that's not
    * going to be very useful)
    */
    final ScheduledFuture<?> taskHandle = scheduler.scheduleAtFixedRate(
        new Runnable() {
            public void run() {
                try {
                    getDataFromDatabase();
                }catch(Exception ex) {
                    ex.printStackTrace(); 
                }
            }
        }, 0, 2, TimeUnit.MINUTES);
    }

    private void getDataFromDatabase() {
    	ExamManager man = new ExamManager();
    	
    	ArrayList<Exam> exams = man.getExamsForEachDay();
    	

		//while (exams.size() != 0) {
			Exam ex = exams.get(0);
		    System.out.println("aaa");
		    
			Timer timer = new Timer();
			// Use this if you want to execute it once
			timer.schedule(new TimerTask() {

				@Override
				public void run() {
					man.startingExam(ex);
				}
			}, ex.getStartDateTime());
			
//			 Date endTime = ex.getStartDateTime();
//			 int mm = endTime.getMinutes() +  ex.getDuration();
//			 endTime.setMinutes(mm);
//			
//			timer.schedule(new TimerTask() {
//
//				@Override
//				public void run() {
//					man.finishingExam(ex);
//				}
//			}, endTime);
			
			//exams.remove(0);
		//}
		
	}


    public static void main(String[] args) {
        SheduledTaskExample ste = new SheduledTaskExample();
        ste.startScheduleTask();
    }
}