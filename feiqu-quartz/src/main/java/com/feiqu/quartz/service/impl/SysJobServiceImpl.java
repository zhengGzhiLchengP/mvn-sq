package com.feiqu.quartz.service.impl;


import cn.hutool.core.convert.Convert;
import com.feiqu.common.constant.ScheduleConstants;
import com.feiqu.common.enums.DataSourceType;
import com.feiqu.common.exception.job.TaskException;
import com.feiqu.framwork.datasource.DynamicDataSourceContextHolder;
import com.feiqu.quartz.mapper.SysJobMapper;
import com.feiqu.quartz.model.SysJob;
import com.feiqu.quartz.model.SysJobExample;
import com.feiqu.quartz.service.SysJobService;
import com.feiqu.quartz.util.ScheduleUtils;
import com.feiqu.system.annotation.BaseService;
import com.feiqu.system.base.BaseServiceImpl;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.text.ParseException;
import java.util.List;

/**
* SysJobService实现
* Created by cwd on 2019/3/13.
*/
@Service
@Transactional
@BaseService
public class SysJobServiceImpl extends BaseServiceImpl<SysJobMapper, SysJob, SysJobExample> implements SysJobService {

    private static Logger logger = LoggerFactory.getLogger(SysJobServiceImpl.class);

    @Resource
    private Scheduler scheduler;

    @Resource
    SysJobMapper sysJobMapper;

    /**
     * 项目启动时，初始化定时器
     */
    @PostConstruct
    public void init() throws TaskException, SchedulerException {
        scheduler.clear();
        DynamicDataSourceContextHolder.setDataSoureType(DataSourceType.FEIQU_TASK_DATA.getSchemaName());
        List<SysJob> jobList = sysJobMapper.selectByExample(new SysJobExample());
        for (SysJob job : jobList)
        {
            ScheduleUtils.createScheduleJob(scheduler, job);
        }
    }

    @Override
    public boolean checkCronExpressionIsValid(String cronExpression) {
        try {
            new CronExpression(cronExpression);
            return true;
        } catch (ParseException var2) {
            return false;
        }
    }

    @Override
    @Transactional
    public void run(SysJob job) throws SchedulerException {
        Integer jobId = job.getJobId();
        SysJob tmpObj = sysJobMapper.selectByPrimaryKey(job.getJobId());
        // 参数
        JobDataMap dataMap = new JobDataMap();
        dataMap.put(ScheduleConstants.TASK_PROPERTIES, tmpObj);
        scheduler.triggerJob(ScheduleUtils.getJobKey(jobId, tmpObj.getJobGroup()), dataMap);
    }

    @Override
    public void deleteJobByIds(String ids) throws SchedulerException {
        Integer[] jobIds = Convert.toIntArray(ids);
        for (Integer jobId : jobIds)
        {
            SysJob job = sysJobMapper.selectByPrimaryKey(jobId);
            deleteJob(job);
        }
    }

    /**
     * 删除任务后，所对应的trigger也将被删除
     *
     * @param job 调度信息
     */
    @Transactional
    public int deleteJob(SysJob job) throws SchedulerException
    {
        Integer jobId = job.getJobId();
        String jobGroup = job.getJobGroup();
        int rows = sysJobMapper.deleteByPrimaryKey(jobId);
        if (rows > 0)
        {
            scheduler.deleteJob(ScheduleUtils.getJobKey(jobId, jobGroup));
        }
        return rows;
    }
    @Override
    public void changeStatus(String ids, String status) throws SchedulerException {
        Integer[] jobIds = Convert.toIntArray(ids);
        if (ScheduleConstants.Status.NORMAL.getValue().equals(status))
        {
            for (Integer jobId : jobIds)
            {
                SysJob job = selectByPrimaryKey(jobId);
                resumeJob(job);
            }
        }
        else if (ScheduleConstants.Status.PAUSE.getValue().equals(status))
        {
            for (Integer jobId : jobIds)
            {
                SysJob job = selectByPrimaryKey(jobId);
                pauseJob(job);
            }
        }
    }
    /**
     * 暂停任务
     *
     * @param job 调度信息
     */
    @Override
    @Transactional
    public int pauseJob(SysJob job) throws SchedulerException
    {
        Integer jobId = job.getJobId();
        String jobGroup = job.getJobGroup();
        job.setStatus(ScheduleConstants.Status.PAUSE.getValue());
        int rows = sysJobMapper.updateByPrimaryKey(job);
        if (rows > 0)
        {
            scheduler.pauseJob(ScheduleUtils.getJobKey(jobId, jobGroup));
        }
        return rows;
    }

    /**
     * 恢复任务
     *
     * @param job 调度信息
     */
    @Override
    @Transactional
    public int resumeJob(SysJob job) throws SchedulerException
    {
        Integer jobId = job.getJobId();
        String jobGroup = job.getJobGroup();
        job.setStatus(ScheduleConstants.Status.NORMAL.getValue());
        int rows = sysJobMapper.updateByPrimaryKey(job);
        if (rows > 0)
        {
            scheduler.resumeJob(ScheduleUtils.getJobKey(jobId, jobGroup));
        }
        return rows;
    }

    @Override
    public int updateJob(SysJob sysJob) throws TaskException, SchedulerException {
        SysJob properties = selectByPrimaryKey(sysJob.getJobId());
        int rows = sysJobMapper.updateByPrimaryKey(sysJob);
        if (rows > 0)
        {
            updateSchedulerJob(sysJob, properties.getJobGroup());
        }
        return rows;
    }

    private void updateSchedulerJob(SysJob job, String jobGroup) throws SchedulerException, TaskException {
        Integer jobId = job.getJobId();
        // 判断是否存在
        JobKey jobKey = ScheduleUtils.getJobKey(jobId, jobGroup);
        if (scheduler.checkExists(jobKey))
        {
            // 防止创建时存在数据问题 先移除，然后在执行创建操作
            scheduler.deleteJob(jobKey);
        }
        ScheduleUtils.createScheduleJob(scheduler, job);
    }
}