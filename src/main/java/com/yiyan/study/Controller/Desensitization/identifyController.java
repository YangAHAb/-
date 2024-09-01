package com.yiyan.study.controller.desensitization;

import cn.hutool.core.util.DesensitizedUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.*;
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
            // DesensitizationDTO DX =
            // com.yiyan.study.model.DesensitizationDTO.builder().idCardNum(NeedIdentify).build();
            return DesensitizedUtil.idCardNum(String.valueOf(NeedIdentify), 6, 4);
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
        return DesensitizedUtil.mobilePhone(String.valueOf(NeedIdentify));
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
            return DesensitizedUtil.email(String.valueOf(NeedIdentify));
        return "illegal";
    }

    @RequestMapping("/address")
    private static String ISaddress(String NeedIdentify) {
        int n = NeedIdentify.length();
        if (n < 4)
            return "illegal";
        int flag = -1;
        char a = NeedIdentify.charAt(0), b = NeedIdentify.charAt(1), c = NeedIdentify.charAt(2),
                d = NeedIdentify.charAt(3);
        if (a == '河' && b == '北' && c == '省')
            flag = 2;
        if (a == '山' && b == '西' && c == '省')
            flag = 2;
        if (a == '辽' && b == '宁' && c == '省')
            flag = 2;
        if (a == '吉' && b == '林' && c == '省')
            flag = 2;
        if (a == '黑' && b == '龙' && c == '江' && d == '省')
            flag = 3;
        if (a == '江' && b == '苏' && c == '省')
            flag = 2;
        if (a == '安' && b == '徽' && c == '省')
            flag = 2;
        if (a == '浙' && b == '江' && c == '省')
            flag = 2;
        if (a == '福' && b == '建' && c == '省')
            flag = 2;
        if (a == '江' && b == '西' && c == '省')
            flag = 2;
        if (a == '山' && b == '东' && c == '省')
            flag = 2;
        if (a == '河' && b == '南' && c == '省')
            flag = 2;
        if (a == '湖' && b == '北' && c == '省')
            flag = 2;
        if (a == '湖' && b == '南' && c == '省')
            flag = 2;
        if (a == '广' && b == '东' && c == '省')
            flag = 2;
        if (a == '海' && b == '南' && c == '省')
            flag = 2;
        if (a == '四' && b == '川' && c == '省')
            flag = 2;
        if (a == '贵' && b == '州' && c == '省')
            flag = 2;
        if (a == '云' && b == '南' && c == '省')
            flag = 2;
        if (a == '陕' && b == '西' && c == '省')
            flag = 2;
        if (a == '甘' && b == '肃' && c == '省')
            flag = 2;
        if (a == '青' && b == '海' && c == '省')
            flag = 2;
        if (a == '台' && b == '湾' && c == '省')
            flag = 2;
        if (a == '北' && b == '京' && c == '市')
            flag = 2;
        if (a == '上' && b == '海' && c == '市')
            flag = 2;
        if (a == '天' && b == '津' && c == '市')
            flag = 2;
        if (a == '重' && b == '庆' && c == '市')
            flag = 2;
        if (flag == -1)
            return "illegal";
        else
            return DesensitizedUtil.address(String.valueOf(NeedIdentify), n - flag - 1);
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
            return DesensitizedUtil.bankCard(String.valueOf(NeedIdentify));
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

    public static String Encrypt(String sSrc, String sKey) throws Exception {
        byte[] raw = sKey.getBytes("utf-8");
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");// "算法/模式/补码方式"
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));
        return Base64.getEncoder().encodeToString(encrypted);// 此处使用BASE64做转码功能，同时能起到2次加密的作用。
    }

    public static String Upset(String str) {
        char[] arr = str.toCharArray();
        Random r = new Random();
        for (int i = 0; i < arr.length; i++) {
            int index = r.nextInt(arr.length);
            char temp = arr[i];
            arr[i] = arr[index];
            arr[index] = temp;
        }
        str = new String(arr);
        return str;
    }

    public static String Des_idcard(String IDcard, int type) throws Exception {
        if (type == 1)
            return ISidcard(IDcard);
        else if (type == 2)
            return Encrypt(IDcard, "123456789!@#$%^&");
        else
            return Upset(IDcard);
    }

    public static String Des_phonenumber(String Phonenumber, int type) throws Exception {
        if (type == 1)
            return ISphonenumber(Phonenumber);
        else if (type == 2)
            return Encrypt(Phonenumber, "123456789!@#$%^&");
        else
            return Upset(Phonenumber);
    }

    public static String Des_Email(String email, int type) throws Exception {
        if (type == 1)
            return ISEmail(email);
        else if (type == 2)
            return Encrypt(email, "123456789!@#$%^&");
        else
            return Upset(email);
    }

    public static String Des_address(String Address, int type) throws Exception {
        if (type == 1)
            return ISaddress(Address);
        else if (type == 2)
            return Encrypt(Address, "123456789!@#$%^&");
        else
            return Upset(Address);
    }

    public static String Des_CardNumber(String CardNumber, int type) throws Exception {
        if (type == 1)
            return ISValidCardNumber(CardNumber);
        else if (type == 2)
            return Encrypt(CardNumber, "123456789!@#$%^&");
        else
            return Upset(CardNumber);
    }

    public static boolean canMask(List<Object> list) {
        List<Double> percentage = maskTypePercentageList(list);
        int maxIdx = percentage.indexOf(Collections.max(percentage));
        return maxIdx == 0 ? false : true;
    }

    public static Integer getColumnType(List<Object> list) {
        List<Double> percentage = maskTypePercentageList(list);
        return percentage.indexOf(Collections.max(percentage));
    }

    public static List<Double> maskTypePercentageList(List<Object> list) {
        if (list.size() == 0) {
            return new ArrayList<Double>();
        }
        int flag0 = 0, flag1 = 0, flag2 = 0, flag3 = 0, flag4 = 0, flag5 = 0, N = list.size();
        for (int i = 0; i < list.size(); i++) {
            Object obj = list.get(i);
            if (obj instanceof String) {
                String str = (String) obj;
                if (ISidcard(str) != "illegal")
                    flag1++;
                else if (ISphonenumber(str) != "illegal")
                    flag2++;
                else if (ISEmail(str) != "illegal")
                    flag3++;
                else if (ISaddress(str) != "illegal")
                    flag4++;
                else if (ISValidCardNumber(str) != "illegal")
                    flag5++;
                else
                    flag0++;
            }
        }
        double percent0 = flag0 * 1.0 / N, percent1 = flag1 * 1.0 / N, percent2 = flag2 * 1.0 / N,
                percent3 = flag3 * 1.0 / N,
                percent4 = flag4 * 1.0 / N, percent5 = flag5 * 1.0 / N;

        // 返回List of double
        return new ArrayList<Double>(List.of(percent0, percent1, percent2, percent3, percent4, percent5));
    }

    public static List<Object> maskList(List<Object> list, int Destype) throws Exception {
        List<String> targetList = list.stream()
                .map(Object::toString) // 将每个对象转换为字符串
                .collect(Collectors.toList());
        List<String> ans = new ArrayList<>();
        for (String NeedId : targetList) {
            // System.out.println(NeedId);
            if (ISidcard(NeedId) != "illegal")
                ans.add(Des_idcard(NeedId, Destype));
            else if (ISphonenumber(NeedId) != "illegal")
                ans.add(Des_phonenumber(NeedId, Destype));
            else if (ISEmail(NeedId) != "illegal")
                ans.add(Des_Email(NeedId, Destype));
            else if (ISaddress(NeedId) != "illegal")
                ans.add(Des_address(NeedId, Destype));
            else if (ISValidCardNumber(NeedId) != "illegal")
                ans.add(Des_CardNumber(NeedId, Destype));
            else
                ans.add(NeedId);
        }
        return new ArrayList<>(ans);
    }
}
