package me.gracenam.restapiwithspring.events;

import me.gracenam.restapiwithspring.accounts.Account;
import me.gracenam.restapiwithspring.accounts.AccountRepository;
import me.gracenam.restapiwithspring.accounts.AccountRole;
import me.gracenam.restapiwithspring.accounts.AccountService;
import me.gracenam.restapiwithspring.common.BaseControllerTest;
import me.gracenam.restapiwithspring.commons.AppProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.common.util.Jackson2JsonParser;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class EventControllerTests extends BaseControllerTest {

    @Autowired
    EventRepository eventRepository;

    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AppProperties appProperties;

    @BeforeEach
    public void setUp() {
        this.eventRepository.deleteAll();
        this.accountRepository.deleteAll();
    }

    @Test
    @DisplayName("??????????????? ???????????? ???????????? ?????????")
    public void createEvent() throws Exception {
        EventDto event = EventDto.builder()
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2022, 9, 22, 11, 48))
                .closeEnrollmentDateTime(LocalDateTime.of(2022, 9, 23, 11, 48))
                .beginEventDateTime(LocalDateTime.of(2022, 9, 24, 11, 48))
                .endEventDateTime(LocalDateTime.of(2022, 9, 25, 11, 48))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("????????? D2 ???????????? ?????????")
                .build();

        mockMvc.perform(post("/api/events/")
                        .header(HttpHeaders.AUTHORIZATION, getBearerToken(true))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(event))
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("free").value(false))
                .andExpect(jsonPath("offline").value(true))
                .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))
                .andDo(document("create-event",
                        links(
                                linkWithRel("self").description("link to self"),
                                linkWithRel("query-events").description("link to query events"),
                                linkWithRel("update-event").description("link to update an existing event"),
                                linkWithRel("profile").description("link to profile")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        requestFields(
                                fieldWithPath("name").description("Name of new event"),
                                fieldWithPath("description").description("Description of new event"),
                                fieldWithPath("beginEnrollmentDateTime").description("BeginEnrollmentDateTime of new event"),
                                fieldWithPath("closeEnrollmentDateTime").description("CloseEnrollmentDateTime of new event"),
                                fieldWithPath("beginEventDateTime").description("BeginEventDateTime of new event"),
                                fieldWithPath("endEventDateTime").description("EndEventDateTime of new event"),
                                fieldWithPath("location").description("Location of new event"),
                                fieldWithPath("basePrice").description("BasePrice of new event"),
                                fieldWithPath("maxPrice").description("MaxPrice of new event"),
                                fieldWithPath("limitOfEnrollment").description("LimitOfEnrollment of new event")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("Location header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("id").description("Identifier of new event"),
                                fieldWithPath("name").description("Name of new event"),
                                fieldWithPath("description").description("Description of new event"),
                                fieldWithPath("beginEnrollmentDateTime").description("BeginEnrollmentDateTime of new event"),
                                fieldWithPath("closeEnrollmentDateTime").description("CloseEnrollmentDateTime of new event"),
                                fieldWithPath("beginEventDateTime").description("BeginEventDateTime of new event"),
                                fieldWithPath("endEventDateTime").description("EndEventDateTime of new event"),
                                fieldWithPath("location").description("Location of new event"),
                                fieldWithPath("basePrice").description("Base Price of new event"),
                                fieldWithPath("maxPrice").description("Max Price of new event"),
                                fieldWithPath("limitOfEnrollment").description("Limit Of Enrollment"),
                                fieldWithPath("free").description("It tells if this event is free or not"),
                                fieldWithPath("offline").description("It tells if this event is online event or offline event"),
                                fieldWithPath("eventStatus").description("Event Status"),
                                fieldWithPath("_links.self.href").description("link to self"),
                                fieldWithPath("_links.query-events.href").description("link to query event list"),
                                fieldWithPath("_links.update-event.href").description("link to update existing event"),
                                fieldWithPath("_links.profile.href").description("link to profile")
                        )
                ))
        ;
    }

    private String getBearerToken(boolean needToCreatedAccount) throws Exception {
        return "Bearer " + getAccessToken(needToCreatedAccount);
    }

    private String getAccessToken(boolean needToCreatedAccount) throws Exception {
        if (needToCreatedAccount) {
            createdAccount();
        }

        ResultActions perform = this.mockMvc.perform(post("/oauth/token")
                .with(httpBasic(appProperties.getClientId(), appProperties.getClientSecret()))
                .param("username", appProperties.getUserUsername())
                .param("password", appProperties.getUserPassword())
                .param("grant_type", "password")
        );

        var responseBody = perform.andReturn().getResponse().getContentAsString();

        Jackson2JsonParser parser = new Jackson2JsonParser();

        return parser.parseMap(responseBody).get("access_token").toString();
    }

    private Account createdAccount() {
        Account spring = Account.builder()
                .email(appProperties.getUserUsername())
                .password(appProperties.getUserPassword())
                .roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
                .build();

        return this.accountService.saveAccount(spring);
    }

    @Test
    @DisplayName("?????? ?????? ??? ?????? ?????? ????????? ?????? ????????? ???????????? ?????????")
    public void createEvent_Bad_Request() throws Exception {
        Event event = Event.builder()
                .id(100)
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2022, 9, 22, 11, 48))
                .closeEnrollmentDateTime(LocalDateTime.of(2022, 9, 23, 11, 48))
                .beginEventDateTime(LocalDateTime.of(2022, 9, 24, 11, 48))
                .endEventDateTime(LocalDateTime.of(2022, 9, 25, 11, 48))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("????????? D2 ???????????? ?????????")
                .free(true)
                .offline(false)
                .eventStatus(EventStatus.PUBLISHED)
                .build();

        mockMvc.perform(post("/api/events/")
                        .header(HttpHeaders.AUTHORIZATION, getBearerToken(true))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(event))
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("?????? ?????? ???????????? ?????? ????????? ???????????? ?????????")
    public void createEvent_Bad_Request_Empty_Input() throws Exception {
        EventDto eventDto = EventDto.builder().build();

        this.mockMvc.perform(post("/api/events")
                        .header(HttpHeaders.AUTHORIZATION, getBearerToken(true))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(eventDto))
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("?????? ?????? ????????? ?????? ????????? ???????????? ?????????")
    public void createEvent_Bad_Request_Wrong_Input() throws Exception {
        EventDto eventDto = EventDto.builder()
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2022, 9, 25, 11, 48))
                .closeEnrollmentDateTime(LocalDateTime.of(2022, 9, 24, 11, 48))
                .beginEventDateTime(LocalDateTime.of(2022, 9, 23, 11, 48))
                .endEventDateTime(LocalDateTime.of(2022, 9, 22, 11, 48))
                .basePrice(10000)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("????????? D2 ???????????? ?????????")
                .build();

        this.mockMvc.perform(post("/api/events")
                        .header(HttpHeaders.AUTHORIZATION, getBearerToken(true))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(eventDto))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors[0].objectName").exists())
                .andExpect(jsonPath("errors[0].defaultMessage").exists())
                .andExpect(jsonPath("errors[0].code").exists())
                .andExpect(jsonPath("_links.index").exists())
        ;
    }

    @Test
    @DisplayName("????????? ??????")
    public void queryEvents() throws Exception {
        // Given
        IntStream.range(0, 30).forEach(this::generateEvent);

        // When & Then
        this.mockMvc.perform(get("/api/events")
                        .param("page", "1")
                        .param("size", "10")
                        .param("sort", "name,DESC")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("page").exists())
                .andExpect(jsonPath("_embedded.eventList[0]._links.self").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("query-events"))
        ;
    }

    @Test
    @DisplayName("????????? ?????? - ??????")
    public void queryEventsWithAuthentication() throws Exception {
        // Given
        IntStream.range(0, 30).forEach(this::generateEvent);

        // When & Then
        this.mockMvc.perform(get("/api/events")
                        .header(HttpHeaders.AUTHORIZATION, getBearerToken(true))
                        .param("page", "1")
                        .param("size", "10")
                        .param("sort", "name,DESC")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("page").exists())
                .andExpect(jsonPath("_embedded.eventList[0]._links.self").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andExpect(jsonPath("_links.create-event").exists())
                .andDo(document("query-events"))
        ;
    }

    @Test
    @DisplayName("?????? ????????? ?????? ??????")
    public void getEvent() throws Exception {
        // Given
        Account account = this.createdAccount();
        Event event = this.generateEvent(100, account);

        // When & Then
        this.mockMvc.perform(get("/api/events/{id}", event.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").exists())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
        ;
    }

    @Test
    @DisplayName("?????? ????????? ??????")
    public void getEvent404() throws Exception {
        // When & Then
        this.mockMvc.perform(get("/api/events/12345"))
                .andDo(print())
                .andExpect(status().isNotFound())
        ;
    }

    @Test
    @DisplayName("????????? ??????")
    public void updateEvent() throws Exception {
        // Given
        Account account = this.createdAccount();
        Event event = this.generateEvent(200, account);
        EventDto eventDto = this.modelMapper.map(event, EventDto.class);
        String eventName = "Updated Event";

        eventDto.setName(eventName);

        // When & Then
        this.mockMvc.perform(put("/api/events/{id}", event.getId())
                        .header(HttpHeaders.AUTHORIZATION, getBearerToken(false))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(eventDto))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value(eventName))
                .andExpect(jsonPath("_links.self").exists())
                .andDo(document("update-event"))
        ;
    }

    @Test
    @DisplayName("????????? ?????? ?????? - ?????? ?????? ?????? ??????")
    public void updateEvent400_Empty() throws Exception {
        // Given
        Event event = this.generateEvent(200);
        EventDto eventDto = new EventDto();

        // When & Then
        this.mockMvc.perform(put("/api/events/{id}", event.getId())
                        .header(HttpHeaders.AUTHORIZATION, getBearerToken(true))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(eventDto))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;
    }

    @Test
    @DisplayName("????????? ?????? ?????? - ?????? ?????? ????????? ??????")
    public void updateEvent400_Wrong() throws Exception {
        // Given
        Event event = this.generateEvent(200);
        EventDto eventDto = this.modelMapper.map(event, EventDto.class);

        eventDto.setBasePrice(20000);
        eventDto.setMaxPrice(1000);

        // When & Then
        this.mockMvc.perform(put("/api/events/{id}", event.getId())
                        .header(HttpHeaders.AUTHORIZATION, getBearerToken(true))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(eventDto))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;
    }

    @Test
    @DisplayName("????????? ?????? ?????? - ???????????? ?????? ??????")
    public void updateEvent404() throws Exception {
        // Given
        Event event = this.generateEvent(200);
        EventDto eventDto = this.modelMapper.map(event, EventDto.class);

        // When & Then
        this.mockMvc.perform(put("/api/events/99999")
                        .header(HttpHeaders.AUTHORIZATION, getBearerToken(true))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(eventDto))
                )
                .andDo(print())
                .andExpect(status().isNotFound())
        ;
    }

    private Event generateEvent(int index, Account account) {
        Event event = buildEvent(index);
        event.setManager(account);

        return this.eventRepository.save(event);
    }


    private Event generateEvent(int index) {
        Event event = buildEvent(index);

        return this.eventRepository.save(event);
    }

    private static Event buildEvent(int index) {
        Event event = Event.builder()
                .name("event " + index)
                .description("test event")
                .beginEnrollmentDateTime(LocalDateTime.of(2022, 9, 22, 11, 48))
                .closeEnrollmentDateTime(LocalDateTime.of(2022, 9, 23, 11, 48))
                .beginEventDateTime(LocalDateTime.of(2022, 9, 24, 11, 48))
                .endEventDateTime(LocalDateTime.of(2022, 9, 25, 11, 48))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("????????? D2 ???????????? ?????????")
                .free(false)
                .offline(true)
                .eventStatus(EventStatus.DRAFT)
                .build();
        return event;
    }

    @ParameterizedTest()
    @MethodSource("parametersForTestFree")
    public void testFree(int basePrice, int maxPrice, boolean isFree) {
        // Given
        Event event = Event.builder()
                .basePrice(basePrice)
                .maxPrice(maxPrice)
                .build();

        // When
        event.update();

        // Then
        assertThat(event.isFree()).isEqualTo(isFree);
    }

    private static Object[] parametersForTestFree() {
        return new Object[] {
                new Object[] {0, 0, true},
                new Object[] {100, 0, false},
                new Object[] {0, 100, false},
                new Object[] {100, 200, false}
        };
    }



    @ParameterizedTest
    @MethodSource("parametersForTestOffline")
    public void testOffline(String location, boolean isOffline) {
        // Given
        Event event = Event.builder()
                .location(location)
                .build();

        // When
        event.update();

        // Then
        assertThat(event.isOffline()).isEqualTo(isOffline);
    }

    private static Object[] parametersForTestOffline() {
        return new Object[] {
                new Object[] {"????????? ????????? D2 ???????????? ?????????", true},
                new Object[] {" ", false},
                new Object[] {null, false}
        };
    }
}
