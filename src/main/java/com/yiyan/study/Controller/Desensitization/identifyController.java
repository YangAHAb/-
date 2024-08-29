package com.yiyan.study.Controller.Desensitization;

import cn.hutool.core.util.DesensitizedUtil;
import com.yiyan.study.model.DesensitizationDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
@Slf4j
@RestController
@RequestMapping("/identify")
public class identifyController {
    // private static final String NeedIdentify="6217003860002354304";
    private static final int[] WEIGHT = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };
    /**
     * 校验码对应表
     */
    private static final char[] CHECK_CODE = { '1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2' };

    @RequestMapping("/idcard")
    private static String ISidcard(String NeedIdentify) {
        if (!NeedIdentify
                .matches("[1-9][0-9]{5}(19[0-9]{2}|20[0-9]{2})(0[1-9]|1[0-2])(0[1-9]|[12][0-9]|3[01])[0-9]{3}[0-9Xx]"))
            return "illegal";
        int sum = 0;
        for (int i = 0; i < 17; i++) {
            sum += (NeedIdentify.charAt(i) - '0') * WEIGHT[i];
        }
        if (CHECK_CODE[sum % 11] == NeedIdentify.charAt(17)) {
            DesensitizationDTO DX = com.yiyan.study.model.DesensitizationDTO.builder().idCardNum(NeedIdentify).build();
            return "legal-idcard" + " " + DesensitizedUtil.idCardNum(String.valueOf(NeedIdentify), 6, 4);
        } else
            return "illegal";
    }

    @RequestMapping("/phonenumber")
    private static String ISphonenumber(String NeedIdentify) {
        int n = NeedIdentify.length();
        char a;
        if (n != 11) {
            return "illegal";
        } else {
            for (int i = 0; i < n; i++) {
                a = NeedIdentify.charAt(i);
                if (!(a >= '0' && a <= '9')) {
                    return "illegal";
                } /*
                   * else if(!(NeedIdentify.charAt(0)=='8'&&NeedIdentify.charAt(1)=='6')){
                   * return "illegal";
                   * }
                   */
            }
        }
        return "legal-phonenumber" + " " + DesensitizedUtil.mobilePhone(String.valueOf(NeedIdentify));
    }

    @RequestMapping("/Email")
    private static String ISEmail(String NeedIdentify) {
        int n = NeedIdentify.length(), i;
        char a;
        int flag1 = -1, flag2 = 0;
        for (i = 0; i < n; i++) {
            a = NeedIdentify.charAt(i);
            if (a == '@')
                flag1 = i;
        }
        for (i = 0; i < n; i++) {
            a = NeedIdentify.charAt(i);
            if (a == '.')
                flag2 = i;
        }
        if (flag1 < 3)
            return "illegal";
        if (flag2 - flag1 <= 2)
            return "illegal";
        if (NeedIdentify.charAt(flag2 + 1) == 'c' && NeedIdentify.charAt(flag2 + 2) == 'n' && flag2 + 2 == n - 1)
            return "legal-Email" + " " + DesensitizedUtil.email(String.valueOf(NeedIdentify));
        if (NeedIdentify.charAt(flag2 + 1) == 'c' && NeedIdentify.charAt(flag2 + 2) == 'o'
                && NeedIdentify.charAt(flag2 + 3) == 'm' && flag2 + 3 == n - 1)
            return "legal-Email" + " " + DesensitizedUtil.email(String.valueOf(NeedIdentify));
        return "illegal";
    }

    @RequestMapping("/address")
    private static String ISaddress(String NeedIdentify) {
        int n = NeedIdentify.length(), i;
        char a;
        int flag1 = -1, flag2 = 0;
        for (i = 0; i < n; i++) {
            a = NeedIdentify.charAt(i);
            if (a == '省')
                flag1 = i;
        }
        for (i = 0; i < n; i++) {
            a = NeedIdentify.charAt(i);
            if (a == '市')
                flag2 = i;
        }
        if (flag1 != 2 && flag1 != 3)
            return "illegal";
        if (flag2 - flag1 <= 2)
            return "illegal";
        return "legal-Address" + " " + DesensitizedUtil.address(String.valueOf(NeedIdentify), n - flag1 - 1);
    }

    @RequestMapping("/bankCard")
    public static String ISValidCardNumber(String NeedIdentify) {
        if (NeedIdentify == null || !NeedIdentify.matches("\\d{16,19}")) {
            return "illegal";
        }
        int sum = 0;
        boolean doubleDigit = false;
        for (int i = NeedIdentify.length() - 1; i >= 0; i--) {
            int digit = NeedIdentify.charAt(i) - '0';
            if (doubleDigit) {
                digit *= 2;
                if (digit > 9) {
                    digit -= 9;
                }
            }
            sum += digit;
            doubleDigit = !doubleDigit;
        }
        if (sum % 10 == 0)
            return "legal_bankcard" + " " + DesensitizedUtil.bankCard(String.valueOf(NeedIdentify));
        else
            return "illegal";
    }

    @RequestMapping("/Answer")
    public static List<String> checkandDesensitized() {
        List<String> list1 = new ArrayList<>();
        List<String> ans = new ArrayList<>();
        list1.add("340204200304073219");
        list1.add("19855362406");
        list1.add("3114077197@qq.com");
        list1.add("6217003860002354304");
        list1.add("安徽省芜湖市");
        list1.add("1234567");
        for (String NeedId : list1) {
            // System.out.println(NeedId);
            if (ISidcard(NeedId) != "illegal")
                ans.add(ISidcard(NeedId));
            else if (ISphonenumber(NeedId) != "illegal")
                ans.add(ISphonenumber(NeedId));
            else if (ISEmail(NeedId) != "illegal")
                ans.add(ISEmail(NeedId));
            else if (ISaddress(NeedId) != "illegal")
                ans.add(ISaddress(NeedId));
            else if (ISValidCardNumber(NeedId) != "illegal")
                ans.add(ISValidCardNumber(NeedId));
            else
                ans.add("normal" + " " + NeedId);
        }
        return ans;
    }

    public static boolean canMask(List<Object> list) {
        if (list.size() == 0) {
            return false;
        }

        Object obj = list.get(0);
        if (obj instanceof String) {
            String str = (String) obj;

            if (ISidcard(str) != "illegal" || ISphonenumber(str) != "illegal" || ISEmail(str) != "illegal"
                    || ISaddress(str) != "illegal" || ISValidCardNumber(str) != "illegal") {
                return true;
            }

        }
        return false;
    }

    public static List<Object> maskList(List<Object> list) {
        List<String> targetList = list.stream()
                .map(Object::toString) // 将每个对象转换为字符串
                .collect(Collectors.toList());
        List<String> ans = new ArrayList<>();
        for (String NeedId : targetList) {
            // System.out.println(NeedId);
            if (ISidcard(NeedId) != "illegal")
                ans.add(ISidcard(NeedId));
            else if (ISphonenumber(NeedId) != "illegal")
                ans.add(ISphonenumber(NeedId));
            else if (ISEmail(NeedId) != "illegal")
                ans.add(ISEmail(NeedId));
            else if (ISaddress(NeedId) != "illegal")
                ans.add(ISaddress(NeedId));
            else if (ISValidCardNumber(NeedId) != "illegal")
                ans.add(ISValidCardNumber(NeedId));
            else
                ans.add(NeedId);
        }
        return new ArrayList<>(ans);
    }
}
