package merit_server.merit.repository;

import jakarta.transaction.Transactional;
import merit_server.merit.domain.Company;
import merit_server.merit.domain.Project;
import merit_server.merit.domain.ProjectStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class ProjectRepositoryTest {

    @Autowired
    private ProjectRepository projectRepository;

    @Test
    @DisplayName("create project")
    @Transactional
    void saveProject() throws Exception {
        //given
        Project project = Project.builder().id(1L).name("Test Project")
                .projectDescription("Test Description")
                .skillList(new HashSet<>(Arrays.asList("Java", "Spring", "Hibernate")))
                .minExpReqd(3)
                .maxExpReqd(5)
                .status(ProjectStatus.OPEN)
                .createdBy("Test User")
                .expectedPay(5000)
                .expectedPayCurrency("USD")
                .createdOn(LocalDateTime.now())
                .modifiedOn(LocalDateTime.now())
                .build();

        //when
        projectRepository.save(project);
        Optional<Project> findProject = projectRepository.findById(1L);

        //then
        assertNotNull(findProject);
        assertThat(findProject.get().getId()).isEqualTo(project.getId());
        assertThat(findProject.get().getName()).isEqualTo(project.getName());
    }

    @Test
    @Transactional
    @DisplayName("update project")
    void updateProject() throws Exception {
        //given
        Project project = Project.builder().id(1L).name("Test Project")
                .projectDescription("Test Description")
                .skillList(new HashSet<>(Arrays.asList("Java", "Spring", "Hibernate")))
                .minExpReqd(3)
                .maxExpReqd(5)
                .status(ProjectStatus.OPEN)
                .createdBy("Test User")
                .expectedPay(5000)
                .expectedPayCurrency("USD")
                .createdOn(LocalDateTime.now())
                .modifiedOn(LocalDateTime.now())
                .build();

        projectRepository.save(project);

        //when
        Optional<Project> findProject = projectRepository.findById(1L);
        Project updatedProject = findProject.get();
        updatedProject.setName("Updated Test Project");
        projectRepository.save(updatedProject);

        //then
        Optional<Project> findUpdatedProject = projectRepository.findById(1L);


        assertThat(findUpdatedProject.get().getName()).isEqualTo("Updated Test Project");
    }

    @Test
    @Transactional
    @DisplayName("delete project")
    public void deleteProject() throws Exception
    {
        //given
        Project project = Project.builder().id(1L).name("Test Project")
                .projectDescription("Test Description")
                .skillList(new HashSet<>(Arrays.asList("Java", "Spring", "Hibernate")))
                .minExpReqd(3)
                .maxExpReqd(5)
                .status(ProjectStatus.OPEN)
                .createdBy("Test User")
                .expectedPay(5000)
                .expectedPayCurrency("USD")
                .createdOn(LocalDateTime.now())
                .modifiedOn(LocalDateTime.now())
                .build();

        projectRepository.save(project);

        //when
        projectRepository.deleteAll();

        //then
        Optional<Project> findDeletedProject = projectRepository.findById(1L);
        assertThat(findDeletedProject.isPresent()).isFalse();
    }

}