package com.educator.eduo.util;

public class CodeGenerator {
    public static String generateCode(int length) {
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int random = (int)(Math.random() * 62);
            if(random < 10) {
                 code.append(random);
            } else if(random > 35) {
                 code.append((char)(random + 61));
            } else {
                code.append((char)(random + 55));
            }
        }
        return code.toString();
    }
}
