package task05.version02;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.Arrays;

public class TelephoneBook implements Comparable<TelephoneBook> {
    private int block;
    private String name;
    private String[] telephone;
    private String mark;
    private String email;
    private TelephoneBook next;

    public String getName() {
        return name;
    }

    public TelephoneBook(String name, String[] telephone, String mark, String email) {
        this.name = name;
        this.telephone = telephone;
        this.mark = mark;
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getTelephone() {
        return telephone;
    }

    public void setTelephone(String[] telephone) {
        this.telephone = telephone;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getBlock() {
        return getBookBlock();
    }

    public void setBlock(int block) {
        this.block = block;
    }

    public TelephoneBook getNext() {
        return next;
    }

    public void setNext(TelephoneBook next) {
        this.next = next;
    }

    @Override
    public String toString() {
        return "TelephoneBook{" +
                "name='" + name + '\'' +
                ", telephone=" + Arrays.toString(telephone) +
                ", mark='" + mark + '\'' +
                ", email='" + email + '\'' +
                '}';
    }


    //计算获得联系人所在分块
    public int getBookBlock() {
        int block = 0;
        for (int i = 0; i < 26; i++) {
            if (TelephoneBook.getFirstSpell(getName().substring(0, 1)).compareTo(String.valueOf((char) ('a' + i))) == 0) {
                block = i;
                break;
            }
        }
        return block;
    }

    //获取拼音首字母
    public static String getFirstSpell(String chinese) {
        StringBuffer pybf = new StringBuffer();
        char[] arr = chinese.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        //输出全部小写
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        //不带音调
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);

        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > 128) {               //不属于ASCII字符
                try {
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(arr[i], defaultFormat);
                    if (temp != null) {
                        pybf.append(temp[0].charAt(0));
                    }
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            } else {
                pybf.append(arr[i]);
            }
        }
        //去除空白字符
        return pybf.toString().replaceAll("\\W", "").trim();
    }


    @Override
    public int compareTo(TelephoneBook o) {
        if (getPingYin(this.getName()).compareTo(getPingYin(o.getName())) > 1) {
            return 1;
        } else if (getPingYin(this.getName()).compareTo(getPingYin(o.getName())) < 1) {
            return -1;
        } else {
            return 0;
        }
    }

    public static String getPingYin(String inputString) {
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);

        char[] input = inputString.trim().toCharArray();
        String output = "";

        try {
            for (int i = 0; i < input.length; i++) {
                if (Character.toString(input[i]).matches("[\\u4E00-\\u9FA5]+")) {
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(input[i], format);
                    output += temp[0];
                } else
                    output += Character.toString(input[i]);
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            e.printStackTrace();
        }
        return output;
    }
}
