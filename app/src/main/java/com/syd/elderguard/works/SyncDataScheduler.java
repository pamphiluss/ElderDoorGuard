package com.syd.elderguard.works;


import android.os.Build;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LifecycleOwner;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.jeremyliao.liveeventbus.LiveEventBus;
import com.syd.elderguard.Constant;

public class SyncDataScheduler {

    //0表示没有同步任务 3开始同步
    public static int uploadSyscState = 0;
    public static int downloadSyscState = 0;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void refreshSyncOneTimeWork(LifecycleOwner lifecycleOwner) {

        uploadSyscState = 3;
        Constraints myConstraints = new Constraints.Builder()
                .setRequiresDeviceIdle(false)
                .setRequiresCharging(false)
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true)
                .setRequiresStorageNotLow(true)
                .build();

        //One time work request
        OneTimeWorkRequest eventWorker =
                new OneTimeWorkRequest.Builder(UploadEventWorker.class)
                        .setConstraints(myConstraints)
                        .build();

        OneTimeWorkRequest relationshipWorker =
                new OneTimeWorkRequest.Builder(UploadRelationshipWorker.class)
                        .setConstraints(myConstraints)
                        .build();

        OneTimeWorkRequest accountWorker =
                new OneTimeWorkRequest.Builder(UploadAccountWorker.class)
                        .setConstraints(myConstraints)
                        .build();

        List<OneTimeWorkRequest> workerList = new ArrayList<OneTimeWorkRequest>();
        workerList.add(eventWorker);
        workerList.add(relationshipWorker);

        WorkManager.getInstance().getWorkInfoByIdLiveData(eventWorker.getId()).observe(lifecycleOwner, workInfo -> {
            if (workInfo!=null&&workInfo.getState().isFinished()) {
                uploadSyscState--;
                LiveEventBus.get(Constant.EVENT_SYNC_UPLOAD_LESS_COUNTER).post(uploadSyscState);
            }
        });

        WorkManager.getInstance().getWorkInfoByIdLiveData(relationshipWorker.getId()).observe(lifecycleOwner, workInfo -> {
            if (workInfo!=null&&workInfo.getState().isFinished()) {
                uploadSyscState--;
                LiveEventBus.get(Constant.EVENT_SYNC_UPLOAD_LESS_COUNTER).post(uploadSyscState);
            }
        });

        WorkManager.getInstance().getWorkInfoByIdLiveData(accountWorker.getId()).observe(lifecycleOwner, workInfo -> {
            if (workInfo!=null&&workInfo.getState().isFinished()) {
                uploadSyscState--;
                LiveEventBus.get(Constant.EVENT_SYNC_UPLOAD_LESS_COUNTER).post(uploadSyscState);
            }
        });

        //enqueue the work request
        WorkManager.getInstance().beginWith(workerList)
                .then(accountWorker).enqueue();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void refreshDownloadTimeWork(LifecycleOwner lifecycleOwner) {

        downloadSyscState = 3;
        Constraints myConstraints = new Constraints.Builder()
                .setRequiresDeviceIdle(false)
                .setRequiresCharging(false)
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true)
                .setRequiresStorageNotLow(true)
                .build();

        //One time work request
        OneTimeWorkRequest eventWorker =
                new OneTimeWorkRequest.Builder(DownloadEventWorker.class)
                        .setConstraints(myConstraints)
                        .build();

        OneTimeWorkRequest relationshipWorker =
                new OneTimeWorkRequest.Builder(DownloadRelationshipWorker.class)
                        .setConstraints(myConstraints)
                        .build();

        OneTimeWorkRequest accountWorker =
                new OneTimeWorkRequest.Builder(DownloadAccountWorker.class)
                        .setConstraints(myConstraints)
                        .build();

        List<OneTimeWorkRequest> workerList = new ArrayList<OneTimeWorkRequest>();
        workerList.add(eventWorker);
        workerList.add(relationshipWorker);

        WorkManager.getInstance().getWorkInfoByIdLiveData(eventWorker.getId()).observe(lifecycleOwner, workInfo -> {
            if (workInfo!=null&&workInfo.getState().isFinished()) {
                downloadSyscState--;
                LiveEventBus.get(Constant.EVENT_SYNC_DOWNLOAD_LESS_COUNTER).post(uploadSyscState);
            }
        });

        WorkManager.getInstance().getWorkInfoByIdLiveData(relationshipWorker.getId()).observe(lifecycleOwner, workInfo -> {
            if (workInfo!=null&&workInfo.getState().isFinished()) {
                downloadSyscState--;
                LiveEventBus.get(Constant.EVENT_SYNC_DOWNLOAD_LESS_COUNTER).post(uploadSyscState);
            }
        });

        WorkManager.getInstance().getWorkInfoByIdLiveData(accountWorker.getId()).observe(lifecycleOwner, workInfo -> {
            if (workInfo!=null&&workInfo.getState().isFinished()) {
                downloadSyscState--;
                LiveEventBus.get(Constant.EVENT_SYNC_DOWNLOAD_LESS_COUNTER).post(uploadSyscState);
            }
        });

        //enqueue the work request
        WorkManager.getInstance().beginWith(workerList)
                .then(accountWorker).enqueue();
    }

}
