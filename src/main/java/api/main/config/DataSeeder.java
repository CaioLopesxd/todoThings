package api.main.config;

import api.main.models.TaskStatus;
import api.main.repositories.TaskStatusRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class DataSeeder {
    @Bean
    CommandLineRunner seedTaskStatus(TaskStatusRepository taskStatusRepository){
        return args -> {
            if(taskStatusRepository.count()==0){
                taskStatusRepository.save(new TaskStatus("Pendente"));
                taskStatusRepository.save(new TaskStatus("Pausado"));
                taskStatusRepository.save(new TaskStatus("Cancelado"));
                taskStatusRepository.save(new TaskStatus("Em Andamento"));
                taskStatusRepository.save(new TaskStatus("Concluido"));
            }
        };
    }
}
