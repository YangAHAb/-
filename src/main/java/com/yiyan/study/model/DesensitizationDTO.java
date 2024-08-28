package com.yiyan.study.model;

import com.yiyan.study.annotation.Desensitization;
import com.yiyan.study.enums.DesensitizationTypeEnum;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class DesensitizationDTO implements Serializable {

    @Desensitization(type = DesensitizationTypeEnum.CHINESE_NAME)
    private String username;

    @Desensitization(type = DesensitizationTypeEnum.EMAIL)
    private String email;

    @Desensitization(type = DesensitizationTypeEnum.ADDRESS)
    private String address;

    @Desensitization(type = DesensitizationTypeEnum.MOBILE_PHONE)
    private String phoneNumber;

    @Desensitization(type = DesensitizationTypeEnum.ID_CARD)
    private String idCardNum;

    @Desensitization(type = DesensitizationTypeEnum.CUSTOM, startInclude = 1, endExclude = -2)
    private String note;
}
