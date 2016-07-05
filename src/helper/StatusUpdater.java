package helper;

import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import data_managers.ExamManager;
import models.Exam;

public class StatusUpdater {
	
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
        }, 0, 1440, TimeUnit.MINUTES);
    }

    private void getDataFromDatabase() {
    	ExamManager man = new ExamManager();
    	ArrayList<Exam> exams = man.getExamsForEachDay();

    	while (exams.size() != 0) {
			Exam ex = exams.get(0);
			Timer timer = new Timer();
			Date startTime = ex.getStartTime();
			java.util.Date dd = new java.util.Date();

			long diffInMillies = startTime.getTime() - dd.getTime();
			long dif = TimeUnit.MILLISECONDS.convert(diffInMillies, TimeUnit.MILLISECONDS);
			
			timer.schedule(new TimerTask() {

				@Override
				public void run() {
					man.startingExam(ex);
				}
			}, dif, 1000*60*60*24); //Task for setting status live for the exam.
			
			dif += TimeUnit.MINUTES.toMillis(ex.getDuration());

			timer.schedule(new TimerTask() {

				@Override
				public void run() {
					man.finishingExam(ex);
				}
			}, dif, 1000*60*60*24); //Task for setting status finished for the exam.
			
			exams.remove(0);
		}
	}


    public static void main(String[] args) {
    	StatusUpdater ste = new StatusUpdater();
        ste.startScheduleTask();
    }
}
