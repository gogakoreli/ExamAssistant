package helper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
                	//System.out.println("Timer in onweee" + new Date());

                }catch(Exception ex) {
                    ex.printStackTrace(); 
                }
            }
        }, 0, 3, TimeUnit.MINUTES);
    }

    private void getDataFromDatabase() {
    	ExamManager man = new ExamManager();
    	
    	ArrayList<Exam> exams = man.getExamsForEachDay();
    	
    	System.out.println("aaa");
    	DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    	Date date = new Date();
    	System.out.println(dateFormat.format(date)); 
    	
    	while (exams.size() != 0) {
			Exam ex = exams.get(0);
		    
			System.out.println(ex.getStartDateTime());
			System.out.println(ex.getStartTime());

			Timer timer = new Timer();
			
			timer.schedule(new TimerTask() {

				@Override
				public void run() {
					System.out.println("We got here");
					man.startingExam(ex);
				}
			}, ex.getStartDateTime(), 1);
			
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
			
			exams.remove(0);
		}
		
	}


    public static void main(String[] args) {
        SheduledTaskExample ste = new SheduledTaskExample();
        ste.startScheduleTask();
    }
}