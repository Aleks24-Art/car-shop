package com.car.shop.core.controller.v1;

import com.car.shop.core.exception.MarkNotFoundException;
import com.car.shop.core.helper.TestHelper;
import com.car.shop.core.model.dto.mark.CreateMarkDto;
import com.car.shop.core.model.entity.Mark;
import com.car.shop.core.service.mark.MarkService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static com.car.shop.core.dictionary.ErrorMessage.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
class MarkControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MarkService markService;

    @Autowired
    private TestHelper testHelper;


    @Test
    void findAllMarks() throws Exception {
        when(markService.findAll()).thenReturn(Arrays.asList(
                new Mark(1L, "Mercedes-Benz", "Germany"),
                new Mark(2L, "BMW", "United Kingdom")
        ));

        mockMvc.perform((get("/v1/mark/all")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].id", containsInAnyOrder(1, 2)))
                .andExpect(jsonPath("$[*].name", containsInAnyOrder("Mercedes-Benz", "BMW")))
                .andExpect(jsonPath("$[*].producingCountry", containsInAnyOrder("Germany", "United Kingdom")));
    }

    @Test
    void findMarkById() throws Exception {
        final Long id = 1L;

        when(markService.findById(anyLong())).thenReturn(
                new Mark(id, "Mercedes-Benz", "Germany")
        );

        mockMvc.perform(get("/v1/mark/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(id.intValue())))
                .andExpect(jsonPath("$.name", equalTo("Mercedes-Benz")))
                .andExpect(jsonPath("$.producingCountry", equalTo("Germany")));
    }

    @Test
    void findMarkWithIncorrectId() throws Exception {
        mockMvc.perform(get("/v1/mark/-1"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", equalTo(CONSTRAINT_VIOLATION_ERROR)))
                .andExpect(jsonPath("$.errors[0]", containsString("ID должно быть больше 0")));
    }

    @Test
    void findMarkWithNonExistedId() throws Exception {
        final Long id = 1L;

        when(markService.findById(anyLong())).thenThrow(
                new MarkNotFoundException(id)
        );

        mockMvc.perform(get("/v1/mark/" + id))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", equalTo(MARK_NOT_FOUND_ERROR + id)))
                .andExpect(jsonPath("$.errors[0]", containsString(MARK_NOT_FOUND_ERROR + id)));
    }

    @Test
    void saveMark() throws Exception {
        final Long id = 1L;

        when(markService.save(
                Mockito.any(Mark.class)
        )).thenReturn(
                new Mark(id, "Mercedes-Benz", "Germany")
        );

        mockMvc.perform(post("/v1/mark")
                .contentType(MediaType.APPLICATION_JSON)
                .content(testHelper.readFile("input-json/mark.json")))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", equalTo(id.intValue())))
                .andExpect(jsonPath("$.name", equalTo("Mercedes-Benz")))
                .andExpect(jsonPath("$.producingCountry", equalTo("Germany")));

    }

    @Test
    void saveMarkWithEmptyBody() throws Exception {
        mockMvc.perform(post("/v1/mark")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", equalTo(METHOD_ARGUMENT_ERROR)))
                .andExpect(jsonPath("$.errors[*]", containsInAnyOrder(
                        "producingCountry: Название страны-производителя не может быть пустым",
                        "name: Название марки не может быть пустым")
                ));
    }

    @Test
    void saveMarkWithNullFields() throws Exception {
        mockMvc.perform(post("/v1/mark")
                .contentType(MediaType.APPLICATION_JSON)
                .content(testHelper.asJsonString(new CreateMarkDto(null, null))))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", equalTo("Method argument not valid")))
                .andExpect(jsonPath("$.errors[*]", containsInAnyOrder(
                        "producingCountry: Название страны-производителя не может быть пустым",
                        "name: Название марки не может быть пустым")
                ));
    }

    @Test
    void saveMarkWithEmptyFields() throws Exception {
        mockMvc.perform(post("/v1/mark")
                .contentType(MediaType.APPLICATION_JSON)
                .content(testHelper.asJsonString(new CreateMarkDto("", ""))))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", equalTo("Method argument not valid")))
                .andExpect(jsonPath("$.errors[*]", containsInAnyOrder(
                        "producingCountry: Название страны-производителя не может быть пустым",
                        "name: Название марки не может быть пустым")
                ));
    }


    @Test
    void updateMark() throws Exception {
        final Long id = 1L;

        when(markService.update(anyLong(), any(Mark.class)))
                .thenReturn(new Mark(id, "Mercedes-Benz", "Germany"));

        mockMvc.perform(put("/v1/mark/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(testHelper.readFile("input-json/mark.json")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(id.intValue())))
                .andExpect(jsonPath("$.name", equalTo("Mercedes-Benz")))
                .andExpect(jsonPath("$.producingCountry", equalTo("Germany")));
    }

    @Test
    void updateNonExistedMark() throws Exception {
        final Long id = 1L;

        when(markService.update(anyLong(), any(Mark.class)))
                .thenThrow(new MarkNotFoundException(id));

        mockMvc.perform(put("/v1/mark/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(testHelper.readFile("input-json/mark.json")))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", equalTo(MARK_NOT_FOUND_ERROR + id)))
                .andExpect(jsonPath("$.errors[0]", containsString(MARK_NOT_FOUND_ERROR + id)));
    }

    @Test
    void updateMarkWithNullFields() throws Exception {
        mockMvc.perform(put("/v1/mark/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(testHelper.asJsonString(new CreateMarkDto(null, null))))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", equalTo("Method argument not valid")))
                .andExpect(jsonPath("$.errors[*]", containsInAnyOrder(
                        "producingCountry: Название страны-производителя не может быть пустым",
                        "name: Название марки не может быть пустым")
                ));
    }

    @Test
    void updateMarkWithEmptyFields() throws Exception {
        mockMvc.perform(put("/v1/mark/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(testHelper.asJsonString(new CreateMarkDto("", ""))))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", equalTo("Method argument not valid")))
                .andExpect(jsonPath("$.errors[*]", containsInAnyOrder(
                        "producingCountry: Название страны-производителя не может быть пустым",
                        "name: Название марки не может быть пустым")
                ));
    }

    @Test
    void updateMarkWithEmptyBody() throws Exception {
        mockMvc.perform(put("/v1/mark/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", equalTo("Method argument not valid")))
                .andExpect(jsonPath("$.errors[*]", containsInAnyOrder(
                        "producingCountry: Название страны-производителя не может быть пустым",
                        "name: Название марки не может быть пустым")
                ));
    }

    @Test
    void deleteMark() throws Exception {
        final Long id = 1L;

        mockMvc.perform(delete("/v1/mark/" + id))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(markService, times(1)).deleteById(id);
    }

    @Test
    void deleteNonExistedMark() throws Exception {
        final Long id = 1L;

        doThrow(new MarkNotFoundException(id))
                .when(markService)
                .deleteById(id);

        mockMvc.perform(delete("/v1/mark/" + id))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", equalTo(MARK_NOT_FOUND_ERROR + id)))
                .andExpect(jsonPath("$.errors[0]", containsString(MARK_NOT_FOUND_ERROR + id)));

        verify(markService, times(1)).deleteById(id);
    }
}