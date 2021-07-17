package com.car.shop.core.controller.v1;

import com.car.shop.core.exception.ModelNotFoundException;
import com.car.shop.core.helper.TestHelper;
import com.car.shop.core.model.dto.model.CreateModelDto;
import com.car.shop.core.model.entity.Model;
import com.car.shop.core.service.model.ModelService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ModelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ModelService modelService;

    @Autowired
    private TestHelper testHelper;


    @Test
    void findAllModels() throws Exception {
        when(modelService.findAll()).thenReturn(Arrays.asList(
                new Model(1L, "M5 e34", 1982, 2005),
                new Model(2L, "S-Class w223", 2003, null)
        ));

        mockMvc.perform((get("/v1/model/all")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].id", containsInAnyOrder(1, 2)))
                .andExpect(jsonPath("$[*].name", containsInAnyOrder("M5 e34", "S-Class w223")))
                .andExpect(jsonPath("$[*].productionStartYear", containsInAnyOrder(1982, 2003)))
                .andExpect(jsonPath("$[*].productionEndYear", containsInAnyOrder(2005, null)));
    }

    @Test
    void findModelById() throws Exception {
        final Long id = 1L;

        when(modelService.findById(anyLong())).thenReturn(
                new Model(id, "M5 e34", 1982, 2005)
        );

        mockMvc.perform(get("/v1/model/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(id.intValue())))
                .andExpect(jsonPath("$.name", equalTo("M5 e34")))
                .andExpect(jsonPath("$.productionStartYear", equalTo(1982)))
                .andExpect(jsonPath("$.productionEndYear", equalTo(2005)));
    }

    @Test
    void findModelWithIncorrectId() throws Exception {
        mockMvc.perform(get("/v1/model/-1"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", equalTo(CONSTRAINT_VIOLATION_ERROR)))
                .andExpect(jsonPath("$.errors[0]", containsString("ID должно быть больше 0")));
    }

    @Test
    void findModelWithNonExistedId() throws Exception {
        final Long id = 1L;

        when(modelService.findById(anyLong())).thenThrow(
                new ModelNotFoundException(id)
        );

        mockMvc.perform(get("/v1/model/" + id))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", equalTo(MODEL_NOT_FOUND_ERROR + id)))
                .andExpect(jsonPath("$.errors[0]", containsString(MODEL_NOT_FOUND_ERROR + id)));
    }

    @Test
    void saveModel() throws Exception {
        final Long id = 1L;

        when(modelService.save(
                Mockito.any(Model.class)
        )).thenReturn(
                new Model(id, "S-Class w223", 1989, 2017)
        );

        mockMvc.perform(post("/v1/model")
                .contentType(MediaType.APPLICATION_JSON)
                .content(testHelper.readFile("input-json/model.json")))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", equalTo(id.intValue())))
                .andExpect(jsonPath("$.name", equalTo("S-Class w223")))
                .andExpect(jsonPath("$.productionStartYear", equalTo(1989)))
                .andExpect(jsonPath("$.productionEndYear", equalTo(2017)));

    }

    @Test
    void saveModelWithEmptyBody() throws Exception {
        mockMvc.perform(post("/v1/model")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", equalTo(METHOD_ARGUMENT_ERROR)))
                .andExpect(jsonPath("$.errors[*]", hasSize(2)))
                .andExpect(jsonPath("$.errors[*]", containsInAnyOrder(
                        "name: Название модели не может быть пустым",
                        "productionStartYear: Год начала производства модели не может быть пустым")
                ));
    }

    @Test
    void saveModelWithIncorrectFields() throws Exception {
        mockMvc.perform(post("/v1/model")
                .contentType(MediaType.APPLICATION_JSON)
                .content(testHelper.asJsonString(new CreateModelDto("", -200, -100))))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", equalTo(METHOD_ARGUMENT_ERROR)))
                .andExpect(jsonPath("$.errors[*]", containsInAnyOrder(
                        "name: Название модели не может быть пустым",
                        "productionEndYear: Год окончания производства модели не может быть отрицательным",
                        "productionStartYear: Год начала производства модели не может быть отрицательным")
                ));
    }


    @Test
    void updateModel() throws Exception {
        final Long id = 1L;

        when(modelService.update(anyLong(), any(Model.class)))
                .thenReturn(new Model(id, "S-Class w223", 1989, 2017));

        mockMvc.perform(put("/v1/model/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(testHelper.readFile("input-json/model.json")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(id.intValue())))
                .andExpect(jsonPath("$.name", equalTo("S-Class w223")))
                .andExpect(jsonPath("$.productionStartYear", equalTo(1989)))
                .andExpect(jsonPath("$.productionEndYear", equalTo(2017)));
    }

    @Test
    void updateNonExistedModel() throws Exception {
        final Long id = 1L;

        when(modelService.update(anyLong(), any(Model.class)))
                .thenThrow(new ModelNotFoundException(id));

        mockMvc.perform(put("/v1/model/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(testHelper.readFile("input-json/model.json")))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", equalTo(MODEL_NOT_FOUND_ERROR + id)))
                .andExpect(jsonPath("$.errors[0]", containsString(MODEL_NOT_FOUND_ERROR + id)));
    }

    @Test
    void updateModelWithIncorrectFields() throws Exception {
        mockMvc.perform(put("/v1/model/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(testHelper.asJsonString(new CreateModelDto("", -200, -100))))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", equalTo(METHOD_ARGUMENT_ERROR)))
                .andExpect(jsonPath("$.errors[*]", containsInAnyOrder(
                        "name: Название модели не может быть пустым",
                        "productionEndYear: Год окончания производства модели не может быть отрицательным",
                        "productionStartYear: Год начала производства модели не может быть отрицательным")
                ));
    }

    @Test
    void updateModelWithEmptyBody() throws Exception {
        mockMvc.perform(put("/v1/model/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", equalTo(METHOD_ARGUMENT_ERROR)))
                .andExpect(jsonPath("$.errors[*]", hasSize(2)))
                .andExpect(jsonPath("$.errors[*]", containsInAnyOrder(
                        "name: Название модели не может быть пустым",
                        "productionStartYear: Год начала производства модели не может быть пустым")
                ));
    }

    @Test
    void deleteModel() throws Exception {
        final Long id = 1L;

        mockMvc.perform(delete("/v1/model/" + id))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(modelService, times(1)).deleteById(id);
    }

    @Test
    void deleteNonExistedModel() throws Exception {
        final Long id = 1L;

        doThrow(new ModelNotFoundException(id))
                .when(modelService)
                .deleteById(id);

        mockMvc.perform(delete("/v1/model/" + id))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", equalTo(MODEL_NOT_FOUND_ERROR + id)))
                .andExpect(jsonPath("$.errors[0]", containsString(MODEL_NOT_FOUND_ERROR + id)));

        verify(modelService, times(1)).deleteById(id);
    }
}