package com.cinovo.backend.DB.Util.Resolver;

import com.cinovo.backend.DB.Model.Credit;
import com.cinovo.backend.DB.Model.Media;
import com.cinovo.backend.DB.Model.Person;
import com.cinovo.backend.DB.Service.CreditService;
import com.cinovo.backend.TMDB.Response.Common.MediaResponse;
import lombok.extern.jbosslog.JBossLog;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@JBossLog
@Service
public class CreditResolver
{
    private final CreditService creditService;

    public CreditResolver(@Lazy CreditService creditService)
    {
        this.creditService = creditService;
    }

    @Async("customExecutorCredit")
    @Transactional
    public void generateDateAsync(final MediaResponse mediaResponse, final String cinevo_id, final Person person, final Media media)
    {
        Credit credit = this.creditService.findCreditByCinevoId(cinevo_id);
        List<String> departments = credit.getDepartment() != null ? new ArrayList<>(credit.getDepartment()) : new ArrayList<>();
        if(mediaResponse.getDepartment() != null && !departments.contains(mediaResponse.getDepartment()))
        {
            departments.add(mediaResponse.getDepartment());
        }
        credit.setDepartment(departments);

        List<String> jobs = credit.getJob() != null ? new ArrayList<>(credit.getJob()) : new ArrayList<>();
        if(mediaResponse.getJob() != null && !jobs.contains(mediaResponse.getJob()))
        {
            jobs.add(mediaResponse.getJob());
        }
        credit.setJob(jobs);

        if(mediaResponse.getRoles() != null)
        {
            List<Credit.Role> roles = new ArrayList<>();
            for(MediaResponse.Role role : mediaResponse.getRoles())
            {
                Credit.Role ro = this.creditService.findCreditRoleByCreditId(role.getCredit_id());
                ro.setCredit_id(role.getCredit_id());
                ro.setCharacter(role.getCharacter());
                ro.setEpisode_count(role.getEpisode_count());

                roles.add(ro);
            }
            credit.setRoles(roles);
        }

        if(mediaResponse.getJobs() != null)
        {
            List<Credit.Job> jobList = new ArrayList<>();
            for(MediaResponse.Job job : mediaResponse.getJobs())
            {
                Credit.Job ro = this.creditService.findCreditJobByCreditId(job.getCredit_id());
                ro.setCredit_id(job.getCredit_id());
                ro.setJob(job.getJob());
                ro.setEpisode_count(job.getEpisode_count());

                jobList.add(ro);
            }
            credit.setJobs(jobList);
        }

        if(person != null && media != null)
        {
            if(person.getMedias() == null)
            {
                person.setMedias(new ArrayList<>());
            }
            if(!person.getMedias().contains(media))
            {
                person.getMedias().add(media);
            }

            if(!media.getCreated_by().contains(person))
            {
                media.getCreated_by().add(person);
            }
        }

        credit.setMedia(media);
        credit.setPerson(person);

        this.creditService.saveAndUpdate(credit);
    }
}
