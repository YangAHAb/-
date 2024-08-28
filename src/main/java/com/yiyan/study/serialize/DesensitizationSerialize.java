package com.yiyan.study.serialize;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.DesensitizedUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.yiyan.study.annotation.Desensitization;
import com.yiyan.study.enums.DesensitizationTypeEnum;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
public class DesensitizationSerialize extends JsonSerializer<String> implements ContextualSerializer {

    private DesensitizationTypeEnum type;

    private Integer startInclude;

    private Integer endExclude;

    @Override
    public void serialize(String str, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        switch (type) {
            // 自定义类型脱敏
            case CUSTOM : jsonGenerator.writeString(CharSequenceUtil.hide(String.valueOf(str), startInclude,
                    endExclude >= startInclude ? endExclude : str.length() + endExclude));
            // userId脱敏
            case USER_ID : jsonGenerator.writeString(String.valueOf(DesensitizedUtil.userId()));
            // 中文姓名脱敏
            case CHINESE_NAME : jsonGenerator.writeString(CharSequenceUtil.hide(String.valueOf(str), 1, str.length()));
            // 身份证脱敏
            case ID_CARD : jsonGenerator.writeString(DesensitizedUtil.idCardNum(String.valueOf(str), 6, 4));
            // 固定电话脱敏
            case FIXED_PHONE : jsonGenerator.writeString(DesensitizedUtil.fixedPhone(String.valueOf(str)));
            // 手机号脱敏
            case MOBILE_PHONE : jsonGenerator.writeString(DesensitizedUtil.mobilePhone(String.valueOf(str)));
            // 地址脱敏
            case ADDRESS : jsonGenerator.writeString(DesensitizedUtil.address(String.valueOf(str), 8));
            // 邮箱脱敏
            case EMAIL : jsonGenerator.writeString(DesensitizedUtil.email(String.valueOf(str)));
            // 密码脱敏
            case PASSWORD : jsonGenerator.writeString(DesensitizedUtil.password(String.valueOf(str)));
            // 中国车牌脱敏
            case CAR_LICENSE : jsonGenerator.writeString(DesensitizedUtil.carLicense(String.valueOf(str)));
            // 银行卡脱敏
            case BANK_CARD : jsonGenerator.writeString(DesensitizedUtil.bankCard(String.valueOf(str)));
        }
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty) throws JsonMappingException {
        if (beanProperty != null) {
            if (Objects.equals(beanProperty.getType().getRawClass(), String.class)) {
                // 获取定义的注解
                Desensitization desensitization = beanProperty.getAnnotation(Desensitization.class);
                if (desensitization == null) {
                    desensitization = beanProperty.getContextAnnotation(Desensitization.class);
                }
                if (desensitization != null) {
                    return new DesensitizationSerialize(desensitization.type(), desensitization.startInclude(),
                            desensitization.endExclude());
                }
            }

            return serializerProvider.findValueSerializer(beanProperty.getType(), beanProperty);
        }
        return serializerProvider.findNullValueSerializer(null);
    }
}
