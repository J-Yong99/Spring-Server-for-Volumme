package Alpha.alphaspring.service;

import Alpha.alphaspring.DTO.RoutineRegisterRequestDto;
import Alpha.alphaspring.DTO.RoutineResponseDto;
import Alpha.alphaspring.DTO.SubRoutineRegisterRequestDto;
import Alpha.alphaspring.DTO.SubRoutineResponseDto;
import Alpha.alphaspring.Utils.CommonTokenUtils;
import Alpha.alphaspring.Utils.KakaoTokenUtils;
import Alpha.alphaspring.domain.Routine;
import Alpha.alphaspring.domain.SubRoutine;
import Alpha.alphaspring.domain.User;
import Alpha.alphaspring.repository.RoutineRepository;
import Alpha.alphaspring.repository.SubRoutineRepository;
import Alpha.alphaspring.repository.UserRepository;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Transactional
@Service
public class SubRoutineServiceImpl {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SubRoutineRepository subRoutineRepository;
    @Autowired
    private RoutineRepository routineRepository;
    @Autowired
    private CommonTokenUtils tokenUtils;
    @Autowired
    private KakaoTokenUtils kakaoTokenUtils;

    public List<SubRoutineResponseDto> findSubRoutines() throws Exception {
        List<SubRoutine> subRoutines = subRoutineRepository.findAll();
        List<SubRoutineResponseDto> responseSubRoutine = new ArrayList<>();
        Stream<SubRoutine> stream = subRoutines.stream();
        stream.forEach(subRoutine -> {
            responseSubRoutine.add(new SubRoutineResponseDto().fromEntity(subRoutine));
        });
        return responseSubRoutine;
    }

    public void join(Map<String, Object> headers, SubRoutineRegisterRequestDto request) throws ParseException {
        String authorizationHeader = (String) headers.get("authorization");
        String[] bearerHeader = authorizationHeader.split(" ");
        String jwtToken = bearerHeader[1];

        String subject = tokenUtils.getSubject(jwtToken);
        String provider = tokenUtils.getIssuer(jwtToken);

        Map<String, Object> args = new HashMap<>();
        args.put("provider", provider);
        args.put("username", subject);

        User user = userRepository
                .findByUsernameAndProvider(
                        (String) args.get("username"),
                        (String) args.get("provider"))
                .orElseThrow(() -> new UsernameNotFoundException("cannot find such user"));

        Routine routine = routineRepository.findByNameAndUser(request.getRoutineName(), user).orElseThrow(() -> new UsernameNotFoundException("cannot find such user"));
        SubRoutine subRoutine = request.toEntity(routine);
        subRoutineRepository.save(subRoutine);
    }
}
