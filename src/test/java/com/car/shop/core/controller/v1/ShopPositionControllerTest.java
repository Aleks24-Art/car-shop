package com.car.shop.core.controller.v1;

import com.car.shop.core.exception.MarkNotFoundException;
import com.car.shop.core.exception.ModelNotFoundException;
import com.car.shop.core.exception.ShopPositionNotFoundException;
import com.car.shop.core.helper.TestHelper;
import com.car.shop.core.model.dto.shop.position.CreateShopPositionDto;
import com.car.shop.core.model.entity.Mark;
import com.car.shop.core.model.entity.Model;
import com.car.shop.core.model.entity.ShopPosition;
import com.car.shop.core.service.mark.MarkService;
import com.car.shop.core.service.model.ModelService;
import com.car.shop.core.service.shop.position.ShopPositionService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
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
class ShopPositionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShopPositionService positionService;

    @MockBean
    private ModelService modelService;

    @MockBean
    private MarkService markService;

    @Autowired
    private TestHelper testHelper;


    @Test
    void findAllShopPositions() throws Exception {
        Mark mark1 = new Mark(1L, "Mercedes-Benz", "Germany");
        Mark mark2 = new Mark(2L, "Land Rover", "United Kingdom");
        Model model1 = new Model(1L, "S-Class w223", 1989, 2017);
        Model model2 = new Model(2L, "Range Rover Discovery", 2000, null);

        when(positionService.findAll()).thenReturn(Arrays.asList(
                new ShopPosition(1L, mark1, model1, 2010, 256, new BigDecimal("10528.24")),
                new ShopPosition(2L, mark2, model2, 2018, 95, new BigDecimal("25550.58"))
        ));

        mockMvc.perform((get("/v1/shop-position/all")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].id", containsInAnyOrder(1, 2)))
                .andExpect(jsonPath("$[*].producedYear", containsInAnyOrder(2010, 2018)))
                .andExpect(jsonPath("$[*].price", containsInAnyOrder(10528.24, 25550.58)))
                .andExpect(jsonPath("$[*].kilometrage", containsInAnyOrder(256, 95)))

                .andExpect(jsonPath("$[*].mark.id", containsInAnyOrder(1, 2)))
                .andExpect(jsonPath("$[*].mark.name", containsInAnyOrder(mark1.getName(), mark2.getName())))
                .andExpect(jsonPath("$[*].mark.producingCountry",
                        containsInAnyOrder(mark1.getProducingCountry(), mark2.getProducingCountry())))

                .andExpect(jsonPath("$[*].model.id", containsInAnyOrder(1, 2)))
                .andExpect(jsonPath("$[*].model.name", containsInAnyOrder(model1.getName(), model2.getName())))
                .andExpect(jsonPath("$[*].model.productionStartYear",
                        containsInAnyOrder(model1.getProductionStartYear(), model2.getProductionStartYear())))
                .andExpect(jsonPath("$[*].model.productionEndYear",
                        containsInAnyOrder(model1.getProductionEndYear(), model2.getProductionEndYear())));
    }

    @Test
    void findShopPositionById() throws Exception {
        Model model = new Model(1L, "S-Class w223", 1989, 2017);
        Mark mark = new Mark(1L, "Mercedes-Benz", "Germany");

        when(positionService.findById(anyLong())).thenReturn(
                new ShopPosition(1L, mark, model, 2018, 95, new BigDecimal("25550.58"))
        );

        mockMvc.perform((get("/v1/shop-position/1")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.producedYear", equalTo(2018)))
                .andExpect(jsonPath("$.price", equalTo(25550.58)))
                .andExpect(jsonPath("$.kilometrage", equalTo(95)))

                .andExpect(jsonPath("$.mark.id", equalTo(1)))
                .andExpect(jsonPath("$.mark.name", equalTo(mark.getName())))
                .andExpect(jsonPath("$.mark.producingCountry", equalTo(mark.getProducingCountry())))

                .andExpect(jsonPath("$.model.id", equalTo(1)))
                .andExpect(jsonPath("$.model.name", equalTo(model.getName())))
                .andExpect(jsonPath("$.model.productionStartYear", equalTo(model.getProductionStartYear())))
                .andExpect(jsonPath("$.model.productionEndYear", equalTo(model.getProductionEndYear())));
    }

    @Test
    void findShopPositionWithIncorrectId() throws Exception {
        mockMvc.perform(get("/v1/shop-position/-1"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", equalTo(CONSTRAINT_VIOLATION_ERROR)))
                .andExpect(jsonPath("$.errors[0]", containsString("ID должно быть больше 0")));
    }

    @Test
    void findShopPositionWithNonExistedId() throws Exception {
        final Long id = 1L;

        when(positionService.findById(anyLong())).thenThrow(
                new ShopPositionNotFoundException(id)
        );

        mockMvc.perform(get("/v1/shop-position/" + id))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", equalTo(SHOP_POSITION_NOT_FOUND_ERROR + id)))
                .andExpect(jsonPath("$.errors[0]", containsString(SHOP_POSITION_NOT_FOUND_ERROR + id)));
    }

    @Test
    void saveShopPosition() throws Exception {
        final Long id = 1L;
        Model model = new Model(1L, "S-Class w223", 1989, 2017);
        Mark mark = new Mark(1L, "Mercedes-Benz", "Germany");

        when(modelService.findById(model.getId()))
                .thenReturn(model);
        when(markService.findById(mark.getId()))
                .thenReturn(mark);

        when(positionService.save(
                Mockito.any(ShopPosition.class)
        )).thenReturn(
                new ShopPosition(id, mark, model, 2008, 256, new BigDecimal("10599.54"))
        );

        mockMvc.perform(post("/v1/shop-position")
                .contentType(MediaType.APPLICATION_JSON)
                .content(testHelper.readFile("input-json/shop-position.json")))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.producedYear", equalTo(2008)))
                .andExpect(jsonPath("$.price", equalTo(10599.54)))
                .andExpect(jsonPath("$.kilometrage", equalTo(256)))

                .andExpect(jsonPath("$.mark.id", equalTo(1)))
                .andExpect(jsonPath("$.mark.name", equalTo(mark.getName())))
                .andExpect(jsonPath("$.mark.producingCountry", equalTo(mark.getProducingCountry())))

                .andExpect(jsonPath("$.model.id", equalTo(1)))
                .andExpect(jsonPath("$.model.name", equalTo(model.getName())))
                .andExpect(jsonPath("$.model.productionStartYear", equalTo(model.getProductionStartYear())))
                .andExpect(jsonPath("$.model.productionEndYear", equalTo(model.getProductionEndYear())));
    }

    @Test
    void saveShopPositionWithNonExistedModelId() throws Exception {
        final Long modelId = 1L;

        when(modelService.findById(modelId)).thenThrow(
                new ModelNotFoundException(modelId)
        );

        mockMvc.perform(post("/v1/shop-position")
                .contentType(MediaType.APPLICATION_JSON)
                .content(testHelper.readFile("input-json/shop-position.json")))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", equalTo(MODEL_NOT_FOUND_ERROR + modelId)))
                .andExpect(jsonPath("$.errors[0]", containsString(MODEL_NOT_FOUND_ERROR + modelId)));
    }

    @Test
    void saveShopPositionWithNonExistedMarkId() throws Exception {
        final Long markId = 1L;

        when(markService.findById(markId)).thenThrow(
                new MarkNotFoundException(markId)
        );

        mockMvc.perform(post("/v1/shop-position")
                .contentType(MediaType.APPLICATION_JSON)
                .content(testHelper.readFile("input-json/shop-position.json")))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", equalTo(MARK_NOT_FOUND_ERROR + markId)))
                .andExpect(jsonPath("$.errors[0]", containsString(MARK_NOT_FOUND_ERROR + markId)));
    }

    @Test
    void saveShopPositionWithEmptyBody() throws Exception {
        mockMvc.perform(post("/v1/shop-position")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", equalTo(METHOD_ARGUMENT_ERROR)))
                .andExpect(jsonPath("$.errors[*]", hasSize(5)))
                .andExpect(jsonPath("$.errors[*]", containsInAnyOrder(
                        "kilometrage: Пробег автомобиля не может быть пустым",
                        "modelId: ID модели автомобиля не может быть пустым",
                        "producedYear: Год выпуска автомобиля не может быть пустым",
                        "price: Цена автомобиля не может быть пустой",
                        "markId: ID марки автомобиля не может быть пустым")
                ));
    }

    @Test
    void saveShopPositionWithIncorrectFields() throws Exception {
        mockMvc.perform(post("/v1/shop-position")
                .contentType(MediaType.APPLICATION_JSON)
                .content(testHelper.asJsonString(
                        new CreateShopPositionDto(-1L, -2L, -100, -200, new BigDecimal("-5500.24"))
                )))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", equalTo(METHOD_ARGUMENT_ERROR)))
                .andExpect(jsonPath("$.errors[*]", containsInAnyOrder(
                        "markId: ID марки автомобиля должно быть больше 0",
                        "producedYear: Год выпуска автомобиля не может быть отрицательным",
                        "modelId: ID модели автомобиля должно быть больше 0",
                        "price: Цена автомобиля не может быть отрицательной",
                        "kilometrage: Пробег автомобиля не может быть отрицательным")
                ));
    }


    @Test
    void updateShopPosition() throws Exception {
        final Long id = 1L;
        Model model = new Model(1L, "S-Class w223", 1989, 2017);
        Mark mark = new Mark(1L, "Mercedes-Benz", "Germany");

        when(modelService.findById(model.getId()))
                .thenReturn(model);
        when(markService.findById(mark.getId()))
                .thenReturn(mark);

        when(positionService.update(
                anyLong(), Mockito.any(ShopPosition.class)
        )).thenReturn(
                new ShopPosition(id, mark, model, 2008, 256, new BigDecimal("10599.54"))
        );

        mockMvc.perform(put("/v1/shop-position/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(testHelper.readFile("input-json/shop-position.json")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.producedYear", equalTo(2008)))
                .andExpect(jsonPath("$.price", equalTo(10599.54)))
                .andExpect(jsonPath("$.kilometrage", equalTo(256)))

                .andExpect(jsonPath("$.mark.id", equalTo(1)))
                .andExpect(jsonPath("$.mark.name", equalTo(mark.getName())))
                .andExpect(jsonPath("$.mark.producingCountry", equalTo(mark.getProducingCountry())))

                .andExpect(jsonPath("$.model.id", equalTo(1)))
                .andExpect(jsonPath("$.model.name", equalTo(model.getName())))
                .andExpect(jsonPath("$.model.productionStartYear", equalTo(model.getProductionStartYear())))
                .andExpect(jsonPath("$.model.productionEndYear", equalTo(model.getProductionEndYear())));

    }

    @Test
    void updateShopPositionWithNonExistedModelId() throws Exception {
        final Long modelId = 1L;

        when(modelService.findById(modelId)).thenThrow(
                new ModelNotFoundException(modelId)
        );

        mockMvc.perform(put("/v1/shop-position/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(testHelper.readFile("input-json/shop-position.json")))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", equalTo(MODEL_NOT_FOUND_ERROR + modelId)))
                .andExpect(jsonPath("$.errors[0]", containsString(MODEL_NOT_FOUND_ERROR + modelId)));
    }

    @Test
    void updateShopPositionWithNonExistedMarkId() throws Exception {
        final Long markId = 1L;

        when(markService.findById(markId)).thenThrow(
                new MarkNotFoundException(markId)
        );

        mockMvc.perform(put("/v1/shop-position/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(testHelper.readFile("input-json/shop-position.json")))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", equalTo(MARK_NOT_FOUND_ERROR + markId)))
                .andExpect(jsonPath("$.errors[0]", containsString(MARK_NOT_FOUND_ERROR + markId)));
    }

    @Test
    void updateNonExistedShopPosition() throws Exception {
        final Long id = 1L;

        when(positionService.update(anyLong(), any(ShopPosition.class)))
                .thenThrow(new ShopPositionNotFoundException(id));

        mockMvc.perform(put("/v1/shop-position/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(testHelper.readFile("input-json/shop-position.json")))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", equalTo(SHOP_POSITION_NOT_FOUND_ERROR + id)))
                .andExpect(jsonPath("$.errors[0]", containsString(SHOP_POSITION_NOT_FOUND_ERROR + id)));
    }

    @Test
    void updateShopPositionWithIncorrectFields() throws Exception {
        mockMvc.perform(put("/v1/shop-position/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(testHelper.asJsonString(
                        new CreateShopPositionDto(-1L, -2L, -100, -200, new BigDecimal("-5500.24"))
                )))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", equalTo(METHOD_ARGUMENT_ERROR)))
                .andExpect(jsonPath("$.errors[*]", containsInAnyOrder(
                        "markId: ID марки автомобиля должно быть больше 0",
                        "producedYear: Год выпуска автомобиля не может быть отрицательным",
                        "modelId: ID модели автомобиля должно быть больше 0",
                        "price: Цена автомобиля не может быть отрицательной",
                        "kilometrage: Пробег автомобиля не может быть отрицательным")
                ));
    }

    @Test
    void updateShopPositionWithEmptyBody() throws Exception {
        mockMvc.perform(put("/v1/shop-position/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", equalTo(METHOD_ARGUMENT_ERROR)))
                .andExpect(jsonPath("$.errors[*]", hasSize(5)))
                .andExpect(jsonPath("$.errors[*]", containsInAnyOrder(
                        "kilometrage: Пробег автомобиля не может быть пустым",
                        "modelId: ID модели автомобиля не может быть пустым",
                        "producedYear: Год выпуска автомобиля не может быть пустым",
                        "price: Цена автомобиля не может быть пустой",
                        "markId: ID марки автомобиля не может быть пустым")
                ));
    }

    @Test
    void deleteShopPosition() throws Exception {
        final Long id = 1L;

        mockMvc.perform(delete("/v1/shop-position/" + id))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(positionService, times(1)).deleteById(id);
    }

    @Test
    void deleteNonExistedShopPosition() throws Exception {
        final Long id = 1L;

        doThrow(new ShopPositionNotFoundException(id))
                .when(positionService)
                .deleteById(id);

        mockMvc.perform(delete("/v1/shop-position/" + id))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", equalTo(SHOP_POSITION_NOT_FOUND_ERROR + id)))
                .andExpect(jsonPath("$.errors[0]", containsString(SHOP_POSITION_NOT_FOUND_ERROR + id)));

        verify(positionService, times(1)).deleteById(id);
    }
}