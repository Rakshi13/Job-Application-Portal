package com.rakshith.JobApplication.Service;

import com.rakshith.JobApplication.Repository.Job;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class JobServiceImpl implements JobService{
    private List<Job>jobList=new ArrayList<>();
    private Long nextId=1L;

    //Search All Job
    @Override
    public List<Job> findAll() {
        return jobList;
    }

    //create Job
    @Override
    public void createJob(Job job) {
        job.setId(nextId++);
        jobList.add(job);
    }

    //find Job By Id
    @Override
    public Job findByID(Long id){
        for(Job job:jobList){
            if(job.getId().equals(id)){
                return job;
            }
        }
        return null;
//        return jobList.stream()
//                .filter(f->f.getId()==id)
//                .limit(1)
//                .toList();
    }


    //delete job by id
    @Override
    public Boolean deleteJobById(Long id) {
        Iterator<Job> itr=jobList.iterator();

        while(itr.hasNext()){
            if(itr.next().getId().equals(id)){
                itr.remove();
                return true;
            }
        }

       return false;
    }

    @Override
    public Boolean updateJobById(Job job, Long id) {
       Iterator<Job>jobListData=jobList.iterator();

       while(jobListData.hasNext()){
           Job data=jobListData.next();

           if(data.getId().equals(id)){
               data.setDescription(job.getDescription());
               data.setLocation(job.getLocation());
               data.setTitle(job.getTitle());
               data.setMinSalary(job.getMinSalary());
               data.setMaxSalary(job.getMaxSalary());

               return true;
           }
       }
       return false;
    }
}
