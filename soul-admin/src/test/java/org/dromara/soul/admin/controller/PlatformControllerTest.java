package org.dromara.soul.admin.controller;

import org.dromara.soul.admin.service.DashboardUserService;
import org.dromara.soul.admin.service.EnumService;
import org.dromara.soul.admin.utils.SoulResultMessage;
import org.dromara.soul.admin.vo.DashboardUserVO;
import org.dromara.soul.common.exception.CommonErrorCode;
import org.dromara.soul.common.utils.DateUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;

import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * test case for PlatformController
 *
 * @author wangwenjie
 */
@RunWith(MockitoJUnitRunner.class)
public class PlatformControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private PlatformController platformController;

    @Mock
    private DashboardUserService dashboardUserService;

    @Mock
    private EnumService enumService;

    /**
     * loginDashboardUser mock data
     */
    private final DashboardUserVO dashboardUserVO = new DashboardUserVO("1", "admin", "123456", 1, true,
            DateUtils.localDateTimeToString(LocalDateTime.now()), DateUtils.localDateTimeToString(LocalDateTime.now()));

    /**
     * init mockmvc
     */
    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(platformController).build();
    }

    /**
     * test method loginDashboardUser
     */
    @Test
    public void testLoginDashboardUser() throws Exception {
        final String loginUri = "/platform/login?userName=admin&password=123456";

        given(this.dashboardUserService.login("admin", "123456")).willReturn(dashboardUserVO);
        this.mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.GET, loginUri))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(CommonErrorCode.SUCCESSFUL)))
                .andExpect(jsonPath("$.message", is(SoulResultMessage.PLATFORM_LOGIN_SUCCESS)))
                .andExpect(jsonPath("$.data.id", is(dashboardUserVO.getId())))
                .andReturn();
    }

    /**
     * test method queryEnums
     */
    @Test
    public void testQueryEnums() throws Exception {
        final String queryEnumsUri = "/platform/enum";

        this.mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.GET, queryEnumsUri))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(CommonErrorCode.SUCCESSFUL)))
                .andExpect(jsonPath("$.data", is(this.enumService.list())))
                .andReturn();
    }
}