package merit_server.merit.service;

import lombok.RequiredArgsConstructor;
import merit_server.merit.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    // * add Project
}
