package security.service;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

public class SchedulerServiceTest {

    private final ScheduleService service = Mockito.mock(ScheduleService.class);

    @Test

    public void testRunScheduler() throws Exception {

        Mockito.doNothing().when(service).run(ArgumentMatchers.isA(String.class));
        service.run("ar");
        Mockito.verify(service, Mockito.times(1)).run("ar");
    }
}
